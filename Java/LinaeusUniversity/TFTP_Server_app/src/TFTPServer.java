import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TFTPServer {
  public static final int TFTPPORT = 4970;
  public static final int BUFSIZE = 516;

  public static final String READDIR = "Read/"; //TODO if you are using WSL use this path: "Read/"  if you are using Intellij use this path : "src/Read/"
  public static final String WRITEDIR = "Write/"; //TODO if you are using WSL use this path: "Write/" if you are using Intellij use this path : "src/Write/"
  // OP codes
  public static final int OP_RRQ = 1;
  public static final int OP_WRQ = 2;
  public static final int OP_DAT = 3;
  public static final int OP_ACK = 4;
  public static final int OP_ERR = 5;
  public boolean receivedLastPacket = false;

  public static void main(String[] args) {
    if (args.length > 0) {
      System.err.printf("usage: java %s\n", TFTPServer.class.getCanonicalName());
      System.exit(1);
    }
    // Starting the server
    try {
      TFTPServer server = new TFTPServer();
      server.start();
    } catch (SocketException e) {
      System.err.println("ERROR! a socket exception has occurred");
    } catch (IOException e) {
      System.err.println("ERROR! an IOE exception has occurred");
    }
  }

  private void start() throws IOException {
    byte[] buf = new byte[BUFSIZE];

    // Create socket
    DatagramSocket socket = new DatagramSocket(null);

    // Create local bind point
    SocketAddress localBindPoint = new InetSocketAddress(TFTPPORT);
    socket.bind(localBindPoint);
    System.out.printf("Listening at port %d for new requests\n", TFTPPORT);
    // Loop to handle client requests
    while (true) {

      final InetSocketAddress clientAddress = receiveFrom(socket, buf);

      // If clientAddress is null, an error occurred in receiveFrom()
      if (clientAddress == null) continue;
      final StringBuffer requestedFile = new StringBuffer();
      final int reqtype = ParseRQ(buf, requestedFile);
      new Thread() {
        public void run() {
          try {
            DatagramSocket sendSocket = new DatagramSocket(0);
            // Connect to client
            sendSocket.connect(clientAddress);
            System.out.printf(
                "%s request for %s from %s using port %d\n",
                (reqtype == OP_RRQ) ? "Read" : "Write",
                clientAddress.getHostName(),
                clientAddress.getAddress(),
                clientAddress.getPort());

            // Read request
            if (reqtype == OP_RRQ) {
              requestedFile.insert(0, READDIR);
              HandleRQ(sendSocket, requestedFile.toString(), OP_RRQ);
            }
            // Write request
            else {
              requestedFile.insert(0, WRITEDIR);
              HandleRQ(sendSocket, requestedFile.toString(), OP_WRQ);
            }
            sendSocket.close();
          } catch (SocketException e) {
            System.err.println("ERROR! a socket exception has occurred");
          }
        }
      }.start();
    }
  }
  /**
   * Reads the first block of data, i.e., the request for an action (read or write).
   *
   * @param socket (socket to read from)
   * @param buf (where to store the read data)
   * @return socketAddress (the socket address of the client)
   */
  private InetSocketAddress receiveFrom(DatagramSocket socket, byte[] buf) throws IOException {
    // Create datagram packet
    DatagramPacket packet = new DatagramPacket(buf, 512);
    // Receive packet
    socket.receive(packet);
    // Get client address and port from the packet
    InetSocketAddress socketAddress = new InetSocketAddress(packet.getAddress(), packet.getPort());
    return socketAddress;
  }
  /**
   * Parses the request in buf to retrieve the type of request and requestedFile
   *
   * @param buf (received request)
   * @param requestedFile (enam of file to read/write)
   * @return opcode (request type: RRQ or WRQ)
   */
  private int ParseRQ(byte[] buf, StringBuffer requestedFile) {
    // See "TFTP Formats" in TFTP specification for the RRQ/WRQ request contents
    ByteBuffer bb = ByteBuffer.wrap(buf);
    int indexZero = 0; // the index of first zero in the array
    for (int i = 2; i < buf.length; i++) {
      if (buf[i] == Byte.parseByte("0")) {
        indexZero = i;
        break;
      }
    }
    byte[] fileNameBytes = Arrays.copyOfRange(buf, 2, indexZero);
    requestedFile.append(new String(fileNameBytes, StandardCharsets.UTF_8));
    return bb.getShort();
  }
  /**
   * Handles RRQ and WRQ requests
   *
   * @param sendSocket (socket used to send/receive packets)
   * @param requestedFile (name of file to read/write)
   * @param opcode (RRQ or WRQ)
   */
  private void HandleRQ(DatagramSocket sendSocket, String requestedFile, int opcode) {
    File file = new File(requestedFile);
    if (file.getName().equals("Deny.txt")) {
      send_ERR(sendSocket, (short) 2, "Error! Access Denied");
    }
    if (file.getName().equals("zero.txt")) {
      send_ERR(sendSocket, (short) 0, "Error! you did something wrong");
    }
    if (opcode == OP_RRQ) {
      readRequest(sendSocket, file, opcode);
    } else if (opcode == OP_WRQ) {
      WriteRequest(sendSocket, file, opcode);
    } else {
      // See "TFTP Formats" in TFTP specification for the ERROR packet contents
      send_ERR(sendSocket, (short) 0, "ERROR! unknown request");
      System.err.println("ERROR! unknown request");
    }
  }

  private void readRequest(DatagramSocket sendSocket, File requestedFile, int opcode) {
    byte[] buf = new byte[512];
    FileInputStream content = null;
    short blockNumber = 1;
    try {
      content = new FileInputStream(requestedFile);
    } catch (FileNotFoundException e) {
      send_ERR(sendSocket, (short) 1, "Error! File does not exist! please write another name");
      System.err.println("Error! File does not exist! please write another name");
    }
    while (true) {
      int dataSize = 0;
      try {
        assert content != null;
        dataSize = content.read(buf);

      } catch (NullPointerException e) {
        send_ERR(sendSocket, (short) 1, "Error! File does not exist!");
        System.err.println("Error! File does not exist!");
      } catch (IOException e) {
        send_ERR(sendSocket, (short) 2, "Access Denied! an error occurred please try again!");
        System.err.println("Access Denied! an error occurred please try again!");
        return;
      }
      if (dataSize < 0) {
        dataSize = 0;
      }
      ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
      bb.putShort((short) OP_DAT);
      bb.putShort(blockNumber);
      bb.put(buf, 0, dataSize);
      DatagramPacket packet = new DatagramPacket(bb.array(), dataSize + 4);
      System.out.println("Sending packet ");
      boolean result = send_DATA_receive_ACK(sendSocket, packet, blockNumber++);
      if (result) {
        System.out.println("The packet has been received successfully! ");
      } else {
        System.out.println("Error! The packet has not been received");
        send_ERR(
            sendSocket,
            (short) 0,
            "ERROR! Either the packet is not received or the acknowledgement is lost during transmission.");
        return;
      }
      if (dataSize < 512) {
        try {
          content.close();
        } catch (IOException e) {
          System.out.println("Error! File cannot be closed properly.");
        }
        break;
      }
    }
  }

  private void WriteRequest(DatagramSocket sendSocket, File requestedFile, int opcode) {
    FileOutputStream content = null;
    short blockNumber = 0;
    if (requestedFile.exists()) {
      send_ERR(
          sendSocket,
          (short) 6,
          "ERROR! This file already exists please try with a different file name!");
    } else {
      try {
        content = new FileOutputStream(requestedFile);
      } catch (FileNotFoundException e) {
        send_ERR(sendSocket, (short) 1, "ERROR! The file that you requested cannot be created!");
        return;
      }
      boolean result = true;
      while (!receivedLastPacket && result) {
        result =
            receive_DATA_send_ACK(
                sendSocket, constructAckPacket(blockNumber++), blockNumber, content);
      }
      receivedLastPacket = false;
    }
  }

  private DatagramPacket constructAckPacket(short block) {
    ByteBuffer buffer = ByteBuffer.allocate(BUFSIZE);
    buffer.putShort((short) OP_ACK);
    buffer.putShort(block);
    return new DatagramPacket(buffer.array(), 4);
  }
  /** To be implemented */
  private boolean send_DATA_receive_ACK(
      DatagramSocket sendSocket, DatagramPacket packet, short blockNumber) {
    byte[] acknowledgement = new byte[BUFSIZE];
    DatagramPacket ackPacket = new DatagramPacket(acknowledgement, BUFSIZE);
    try {
      sendSocket.send(packet);
      sendSocket.setSoTimeout(1000);
      sendSocket.receive(ackPacket);
    } catch (SocketTimeoutException t) {
      System.out.println("Timeout error!");
    } catch (IOException e) {
      System.out.println("Did not receive acknowledgement!");
    }
    ByteBuffer wrap = ByteBuffer.wrap(ackPacket.getData());
    short opcode = wrap.getShort();
    return wrap.getShort() == blockNumber;
  }

  private boolean receive_DATA_send_ACK(
      DatagramSocket sendSocket,
      DatagramPacket ackPacket,
      short blockNr,
      FileOutputStream content) {
    byte[] receivedData = new byte[BUFSIZE];
    DatagramPacket receivedPacket = new DatagramPacket(receivedData, BUFSIZE);
    try {
      sendSocket.send(ackPacket);
      sendSocket.setSoTimeout(1000);
      sendSocket.receive(receivedPacket);

      ByteBuffer buffer = ByteBuffer.wrap(receivedPacket.getData());
      if (buffer.getShort() == (short) OP_ERR) {
        send_ERR(sendSocket, (short) 0, "Error packet received!");
        return false;
      }
      short block = buffer.getShort();
      if (block == -1) {
        send_ERR(sendSocket, (short) 0, "Packet is lost");
        System.out.println("Packet is lost!");
        return false;
      } else if (block != blockNr) {
        send_ERR(sendSocket, (short) 0, "Packet is sent repeatedly");
        System.out.println("Duplicate packet!");
        return false;
      }
      content.write(buffer.array(), 4, receivedPacket.getLength() - 4);
      System.out.println("Data is stored successfully ! ");
      System.out.println("The data-packet length: " + receivedPacket.getLength());
      if (receivedPacket.getLength() - 4 < 512) {
        sendSocket.send(ackPacket);
        System.out.println("Acknowledgement has been sent!");
        content.close();
        receivedLastPacket = true;
        return false;
      }
    } catch (PortUnreachableException e) {
      send_ERR(sendSocket, (short) 2, "Access Denied! You are trying to send from an empty file!");
      System.out.println("You are trying to send from an empty file!");
      System.out.println("The system will exit you need to start the program again");
      System.exit(1);

    } catch (IOException e) {
      send_ERR(sendSocket, (short) 2, "Access Denied! an error occurred please try again!");
      System.out.println(
          "IOE exception occurred, Something happened while trying to receive data and send acknowledgement ");
      System.out.println("The system will exit, you need to start the program again");
      System.exit(1);
    }
    return true;
  }

  private void send_ERR(DatagramSocket sendSocket, short errorCode, String message) {
    ByteBuffer buffer = ByteBuffer.allocate(BUFSIZE);
    buffer.putShort((short) OP_ERR);
    buffer.putShort(errorCode);
    buffer.put(message.getBytes());
    buffer.put((byte) 0);
    DatagramPacket errorPacket = new DatagramPacket(buffer.array(), buffer.array().length);
    try {
      sendSocket.send(errorPacket);
    } catch (IOException e) {
      System.err.println("Error message cannot be sent.");
    }
  }
}
