import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  static int port;
  static String folder; // the root folder: public
  static ServerSocket serverSocket;

  public static void main(String[] args) {
    port = Integer.parseInt(args[0]);
    folder = args[1];
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("Server is listening on port " + port);
      createConnection(serverSocket, folder);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // connect client to server and handle requests
  public static void createConnection(ServerSocket serverSocket, String folder) throws IOException {
    int clientID = 0; // client ID
    DataOutputStream writer = null; // it is used to send file to client
    String received = "get /public/index.html"; // a string used to store http header info: http
                                                // method, file path, HTTP version
    while (true) {
      Socket socket = null;
      try {
        clientID++;
        socket = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        received = reader.readLine(); // http request
        System.out.println("Client: " + clientID);
        System.out.println(received);
        String filePath = received.split(" ")[1];
        // if file name length is greater than 20, give 500 server error
        if (fileNameFetcher(filePath).length() >= 20) {
          sendFileToClient("text/html", writer, "public/500.html", "500 Internal Server Error");
          System.out.println("500 Server Error");
          socket.close();
        } else {
          // Send responses according to the requests:
          // 302 Found (redirect)
          // 404 Page Not Found
          // 500 Internal Server Error
          // 200 OK
          if (filePath.equals("/public/302.html") || filePath.equals("/302.html")) {
            System.out.println("Click the link to be redirected to: bee.png");
            String movedFile = fileFinder("302.html", folder);
            sendFileToClient(classifyFileType(movedFile), writer, movedFile, "302 Found");
            System.out.println("302 Found");
          } else if (filePath.equals("/public/404.html") || filePath.equals("/404.html")) {
            sendFileToClient("text/html", writer, "public/404.html", "404 Page Not Found");
            System.out.println("404 Page Not Found");
          } else if (filePath.equals("/public/500.html") || filePath.equals("/500.html")) {
            sendFileToClient("text/html", writer, "public/500.html", "500 Internal Server Error");
            System.out.println("500 Server Error");
          } else {
            String wantedFile = fileFinder(fileNameFetcher(filePath), folder);
            sendFileToClient(classifyFileType(wantedFile), writer, wantedFile, "200 OK");
            System.out.println("200 OK");
          }
        } // handle exceptions:
      } catch (FileNotFoundException e) {
        sendFileToClient("text/html", writer, "public/404.html", "404 Page Not Found");
        System.out.println("404 Page Not Found");
      } catch (NullPointerException e) {
        sendFileToClient("text/html", writer, "public/404.html", "404 Page Not Found");
        System.out.println("404 Page Not Found");
      } catch (IOException e) {
        sendFileToClient("text/html", writer, "public/500.html", "500 Internal Server Error");
        System.out.println("500 Server Error");
      }
      socket.close(); // close the socket
    }
  }

  // return the name of the file
  public static String fileNameFetcher(String rawPath) {
    StringBuilder builder = new StringBuilder(rawPath);
    builder.append("Content-Type");
    builder.delete(builder.indexOf("Content-Type"), builder.length());
    if (builder.lastIndexOf("/") == builder.length() - 1) {
      if (builder.length() == 1 || builder.toString().equals("/public/")) {
        return "index.html";
      }
      builder.deleteCharAt(builder.lastIndexOf("/"));
    } else if (builder.toString().equals("/public")) {
      return "index.html";
    }
    return builder.substring(builder.lastIndexOf("/") + 1, builder.length());
  }

  // check if the file ("fileName") exists under the folder("rootFolder")
  // if so, return the path, otherwise, return empty string
  public static String fileFinder(String fileName, String rootFolder) {
    File[] childrenOfRoot = new File(rootFolder).listFiles();
    String correctPath = "";
    if (fileName.equals("/") || fileName.equals("/" + rootFolder)
        || fileName.equals("/" + rootFolder + "/")) {
      return rootFolder + "/index.html";
    }
    for (File file : childrenOfRoot) {
      if (correctPath.equals("")) {
        if (file.getName().equals(fileName)) {
          if (file.isDirectory()) {
            return file.getPath() + "/index.html";
          }
          return file.getPath();
        }
        if (file.isDirectory()) {
          if (file.getName().equals(fileName)) {
            return file.getPath() + "/index.html";
          }
          correctPath = fileFinder(fileName, file.getPath());
        }
      }
    }
    return correctPath;
  }

  // return the type of file
  public static String classifyFileType(String string) {
    String[] filename = string.split("\\.");
    if (filename[filename.length - 1].equalsIgnoreCase("html")) {
      return "text/html";
    } else if (filename[filename.length - 1].equalsIgnoreCase("png")) {
      return "image/png";
    } else {
      return null;
    }
  }

  // send file to client with responses 200, 302, 404, 500
  public static void sendFileToClient(String type, DataOutputStream writer, String path,
      String response) throws IOException {
    // Setting HTTP response headers
    writer.writeBytes("HTTP/1.1 " + response + "\r\n");
    writer.writeBytes("Content-Type: " + type + "\r\n");
    writer.writeBytes("\r\n");
    byte[] bytes = new byte[1024];
    InputStream in = new FileInputStream(path);
    int count;
    while ((count = in.read(bytes)) > 0) {
      writer.write(bytes, 0, count);
    }
    writer.flush();
    in.close();
  }
}
