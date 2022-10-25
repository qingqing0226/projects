package hotelproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import hotelproject.Room.Bed;
import hotelproject.User.AccessLevel;
import hotelproject.Customer.PaymentMethod;

// In order for connections to work you have to use your Mysql username/password and you might have to change the port number in String connection (3308, 3306).
public class dbHandeler {
  /*
   * <<<<< LOOK HERE! >>>>> Before using this class, 1. Adjust username & password
   * & connection details according to your local settings 2. Drop database
   * 'hotel' if there is something wrong with the database
   */
  private String userName;
  private String password;
  private String connection;
  public static Connection conn;
  public static Statement stmt;
  public boolean connected = false;

  protected enum UserAttribute {
    userName, Password, accessLevel, firstName, lastName, None
  }

  public dbHandeler(String username, String pass, String port) {
    this.userName = username;
    this.password = pass;
    this.connection = String.format(
        "jdbc:mysql://localhost:%s/HOTEL?createDatabaseIfNotExist=true&serverTimezone=UTC&useSSL=FALSE&autoreconnect=True&allowPublicKeyRetrieval=True",
        port);
    try {
      conn = DriverManager.getConnection(this.connection, this.userName, this.password);
    } catch (SQLException e) {
      System.out.println("Failed to connect to the database.");
    }
    try {
      if (conn != null) {
        this.connected = true;
        stmt = conn.createStatement();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * add some table in a database.
   * 
   * @param stmt
   * @param tables
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public void initilizeDatabase() throws ClassNotFoundException, SQLException {
    /**
     * All table init here.
     */
    // user table
    String userTable = "CREATE TABLE IF NOT EXISTS `user` (" + "`userName` varchar(45) BINARY NOT NULL,"
        + "`password` varchar(45) NOT NULL," + "`accessLevel` enum('Admin','Staff') NOT NULL DEFAULT 'Staff',"
        + "`firstName` varchar(45) DEFAULT NULL," + "`lastName` varchar(45) DEFAULT NULL," + "PRIMARY KEY (`userName`),"
        + "UNIQUE KEY `userName_UNIQUE` (`userName`)" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    // room table
    String roomTable = "CREATE TABLE IF NOT EXISTS `rooms` (" + "`Number` Int NOT NULL," + "`Price` double,"
        + "`Beds` varchar(45)," + "`Size` double," + "`Location` varchar(45)," + "`Comment` varchar(45),"
        + "PRIMARY KEY (`Number`)," + "UNIQUE KEY `Number_UNIQUE` (`Number`)" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    // Customer table
    String customerTable = "CREATE TABLE IF NOT EXISTS `customer` (" + "`phoneNumber` varchar(45) NOT NULL,"
        + "`Name` varchar(45)," + "`Address` varchar(45),"
        + "`paymentMethod` ENUM('Mobile', 'MasterCard', 'Visa', 'Cash')NOT NULL DEFAULT 'MasterCard',"
        + "PRIMARY KEY (`phoneNumber`)," + "UNIQUE KEY `Number_UNIQUE` (`phoneNumber`)"
        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    // booking table
    String bookingTable = "CREATE TABLE IF NOT EXISTS `booking` (" + "`Booking_number` Int NOT NULL AUTO_INCREMENT,"
        + "`Room_number` int," + "`Customer_phone` varchar(45)," + "`Start_date` varchar(45) ,"
        + "`End_date` varchar(45)," + "`Paid`  ENUM ('Paid', 'Not Paid'),`Price` double,"
        + "PRIMARY KEY (`Booking_number`)," + "UNIQUE KEY `Booking_number_UNIQUE` (`Booking_number`)"
        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    List<String> tables = new ArrayList<String>();
    tables.add(userTable);
    tables.add(roomTable);
    tables.add(customerTable);
    tables.add(bookingTable);

    Class.forName("com.mysql.cj.jdbc.Driver");
    for (String s : tables) {
      stmt.execute(s);
    }

    User u;
    u = new Administrator("admin", "Admin", "Admin", "Admin", false);
    addUserDetail(u);
    u = new ReceptionStaff("staff", "Staff", false);
    addUserDetail(u);
  }

  public void closeDb() throws SQLException {
    stmt.close();
    conn.close();
  }

  /**
   * return user detail in database.
   * 
   * @param conn
   * @param user
   * @return
   * @throws SQLException
   */
  public static User getUserDetail(String inputuserName) throws SQLException {
    String query = "SELECT userName, password, accessLevel, firstName, lastName FROM `hotel`.`user` WHERE BINARY (`userName` = ?)";
    PreparedStatement preparedstatement = conn.prepareStatement(query);
    preparedstatement.setString(1, inputuserName);
    ResultSet rs = preparedstatement.executeQuery();
    User user = null;
    String userName = "";
    String password = "";
    String userType = "";
    String firstName = "";
    String lastName = "";
    while (rs.next()) {
      userName = rs.getString(1);
      password = rs.getString(2);
      userType = rs.getString(3);
      firstName = rs.getString(4);
      lastName = rs.getString(5);
    }
    if (userType.equals("Admin")) {
      user = new Administrator(userName, password, firstName, lastName, true);
    } else if (userType.equals("Staff")) {
      user = new ReceptionStaff(userName, password, firstName, lastName, true);
    } else {
      user = new ReceptionStaff(userName, password, firstName, lastName, true);
      user.setAccessToNone();
    }
    rs.close();
    return user;
  }

  /**
   * add a user in database with details.
   * 
   * @param conn
   * @param user
   * @return
   * @throws SQLException
   */
  public static Boolean addUserDetail(User user) throws SQLException {
    // System.out.println(getUserDetail(user.getUserName()).userDetail());
    if (getUserDetail(user.getUserName()).getAccessLevel().equals(AccessLevel.None)) {
      String query = "INSERT INTO `hotel`.`user` (`userName`, `password`, `accessLevel`, `firstName`, `lastName`) VALUES (?,?,?,?,?);";
      PreparedStatement preparedstatement = conn.prepareStatement(query);
      preparedstatement.setString(1, user.getUserName());
      preparedstatement.setString(2, user.getPassword());
      preparedstatement.setString(3, user.getStringAccessLevel());
      preparedstatement.setString(4, user.getFirstName());
      preparedstatement.setString(5, user.getLastName());
      preparedstatement.execute();
      return true;
    } else {
      return false;
    }
  }

  /**
   * add a user in database with details.
   * 
   * @param conn
   * @param user
   * @return
   * @throws SQLException
   */
  public static Boolean deleteUserDetail(User user) throws SQLException {
    if (!(getUserDetail(user.getUserName()).getAccessLevel().equals(AccessLevel.None))) {
      String query = "DELETE FROM `hotel`.`user` WHERE (`userName` = ?)";
      PreparedStatement preparedstatement = conn.prepareStatement(query);
      preparedstatement.setString(1, user.getUserName());
      preparedstatement.execute();
      return true;
    } else {
      return false;
    }
  }

  public static void changeUserDetail(User user) throws SQLException {
    deleteUserDetail(user);
    addUserDetail(user);
  }

  // adding Room functions

  // fetching room details based on room number and returning a room object.
  public static Room getRoomDetail(int number) throws SQLException {
    Room room = null;
    String query = "SELECT * FROM `rooms` WHERE Number = ?";
    PreparedStatement preparedstatement = conn.prepareStatement(query);
    preparedstatement.setInt(1, number);
    ResultSet rs = preparedstatement.executeQuery();

    String beds = "";
    while (rs.next()) {
      beds = rs.getString(3);
      room = new Room(rs.getInt(1), rs.getDouble(2), rs.getDouble(4), rs.getString(5), rs.getString(6));
      String[] bedStr = beds.split(" ");
      for (String i : bedStr) {
        if (i.equalsIgnoreCase("SINGLE")) {
          room.addBed(Bed.SINGLE);
        } else if (i.equalsIgnoreCase("DOUBLE")) {
          room.addBed(Bed.DOUBLE);
        } else if (i.equalsIgnoreCase("EXTRA_LARGE")) {
          room.addBed(Bed.EXTRA_LARGE);
        }
      }
    }

    rs.close();
    return room;
  }

  // a room is checked if it exists already then if not we add it
  public static boolean addRoom(Room room) throws SQLException {
    // create a case if room with existing number is added
    String query = "SELECT * FROM `rooms` WHERE Number = ?";
    PreparedStatement preparedstatement = conn.prepareStatement(query);
    preparedstatement.setInt(1, room.getNumber());
    ResultSet rs = preparedstatement.executeQuery();
    if (!rs.next()) {
      String beds = "";
      for (Bed i : room.getBeds()) {
        if (i == Bed.SINGLE) {
          beds += "Single ";
        }
        if (i == Bed.DOUBLE) {
          beds += "Double ";
        }
        if (i == Bed.EXTRA_LARGE) {
          beds += "Extra_Large ";
        }
      }
      String newRoom = "Insert into `rooms` (Number, Price, Beds, Size, Location, Comment)"
          + " values (?, ?, ?, ?, ?,?)";
      PreparedStatement preparedstatementUpload = conn.prepareStatement(newRoom);
      preparedstatementUpload.setInt(1, room.getNumber());
      preparedstatementUpload.setDouble(2, room.getPrice());
      preparedstatementUpload.setString(3, beds);
      preparedstatementUpload.setDouble(4, room.getSize());
      preparedstatementUpload.setString(5, room.getLocation());
      preparedstatementUpload.setString(6, room.getComment());

      preparedstatementUpload.executeUpdate();
      rs.close();
      return true;
    } else {
      rs.close();
      return false;
    }
  }

  // we look for a room if there is a room matching that number we delete it
  public static void removeRoom(Room room) throws SQLException {
    String query = "SELECT * FROM `rooms` WHERE Number = ?";
    PreparedStatement preparedstatement = conn.prepareStatement(query);
    preparedstatement.setInt(1, room.getNumber());
    ResultSet rs = preparedstatement.executeQuery();
    if (rs.next()) {
      String deletedRoom = "DELETE FROM `rooms` WHERE Number = ?";
      PreparedStatement preparedstatementUpload = conn.prepareStatement(deletedRoom);
      preparedstatementUpload.setInt(1, room.getNumber());
      preparedstatementUpload.executeUpdate();
    }
    rs.close();
  }

  public static void removeRoom(Integer roomNumber) throws SQLException {
    String query = "SELECT * FROM `rooms` WHERE Number = ?";
    PreparedStatement preparedstatement = conn.prepareStatement(query);
    preparedstatement.setInt(1, roomNumber);
    ResultSet rs = preparedstatement.executeQuery();
    if (rs.next()) {
      String deletedRoom = "DELETE FROM `rooms` WHERE Number = ?";
      PreparedStatement preparedstatementUpload = conn.prepareStatement(deletedRoom);
      preparedstatementUpload.setInt(1, roomNumber);
      preparedstatementUpload.executeUpdate();
    }
    rs.close();
  }

  public static boolean checkRemovableRoom(Integer roomNumber) throws SQLException {
    String query = "SELECT * FROM `booking` WHERE Room_number = ?";
    PreparedStatement preparedstatement = conn.prepareStatement(query);
    preparedstatement.setInt(1, roomNumber);
    ResultSet rs = preparedstatement.executeQuery();
    if (rs.next()) {
      return false;
    } else {
      return true;
    }

  }

  // we look for a room if we find it we edit details
  public static boolean editRoomDetail(Room room) throws SQLException {
    // convert beds from Bed to string
    // int oldRoomNumber =
    String beds = "";
    for (Bed b : room.getBeds()) {
      if (b == Bed.SINGLE) {
        beds += "Single ";
      } else if (b == Bed.DOUBLE) {
        beds += "Double ";
      } else if (b == Bed.EXTRA_LARGE) {
        beds += "Extra_large ";
      }
    }
    String query = "SELECT * FROM `rooms` WHERE Number = ?";
    PreparedStatement preparedstatement = conn.prepareStatement(query);
    preparedstatement.setInt(1, room.getNumber());
    ResultSet rs = preparedstatement.executeQuery();
    if (rs.next()) {
      rs.close();
      String editPrice = "UPDATE rooms set Price = ? WHERE Number = ?";
      PreparedStatement preparedstatementPrice = conn.prepareStatement(editPrice);
      preparedstatementPrice.setDouble(1, room.getPrice());
      preparedstatementPrice.setInt(2, room.getNumber());
      preparedstatementPrice.executeUpdate();
      String editBeds = "UPDATE rooms set Beds = ? WHERE Number = ?";
      PreparedStatement preparedstatementBeds = conn.prepareStatement(editBeds);
      preparedstatementBeds.setString(1, beds);
      preparedstatementBeds.setInt(2, room.getNumber());
      preparedstatementBeds.executeUpdate();
      String editSize = "UPDATE rooms set Size = ? WHERE Number = ?";
      PreparedStatement preparedstatementSize = conn.prepareStatement(editSize);
      preparedstatementSize.setDouble(1, room.getSize());
      preparedstatementSize.setInt(2, room.getNumber());
      preparedstatementSize.executeUpdate();
      String editLocation = "UPDATE rooms set Location = ? WHERE Number = ?";
      PreparedStatement preparedstatementLocation = conn.prepareStatement(editLocation);
      preparedstatementLocation.setString(1, room.getLocation());
      preparedstatementLocation.setInt(2, room.getNumber());
      preparedstatementLocation.executeUpdate();
      String editComment = "UPDATE rooms set Comment = ? WHERE Number = ?";
      PreparedStatement preparedstatementComment = conn.prepareStatement(editComment);
      preparedstatementComment.setString(1, room.getComment());
      preparedstatementComment.setInt(2, room.getNumber());
      preparedstatementComment.executeUpdate();
      return true;
    } else
      return false;
  }

  public static List<String> getListofRoomNo() throws SQLException {
    List<String> roomNo = new ArrayList<String>();
    String query = "SELECT Number FROM `rooms`";
    PreparedStatement preparedstatement = conn.prepareStatement(query);
    ResultSet rs = preparedstatement.executeQuery();
    while (rs.next()) {
      roomNo.add("Room No: " + rs.getString(1));
    }
    rs.close();
    return roomNo;
  }

  public static Boolean addCustomer(Customer customer) throws SQLException {
    if (getCustomerDetail(customer.getPhoneNo()) == null) {
      String query = "INSERT INTO `hotel`.`customer` (`phoneNumber`, `Name`, `Address`, `paymentMethod`) VALUES (?,?,?,?);";
      PreparedStatement preparedstatement = conn.prepareStatement(query);
      preparedstatement.setString(1, customer.getPhoneNo());
      preparedstatement.setString(2, customer.getName());
      preparedstatement.setString(3, customer.getAddress());
      preparedstatement.setString(4, customer.getStringpaymentMethod());
      preparedstatement.execute();
      return true;
    } else {
      return false;
    }
  }

  public static Customer getCustomerDetail(String inputphoneNumber) throws SQLException {
    String query = "SELECT phoneNumber, Name, Address, paymentMethod FROM `hotel`.`customer` WHERE (`phoneNumber` = ?)";
    PreparedStatement preparedstatement = conn.prepareStatement(query);
    preparedstatement.setString(1, inputphoneNumber);
    ResultSet rs = preparedstatement.executeQuery();
    String phoneNumber = "";
    String name = "";
    String address = "";
    String paymentMethod = "";
    while (rs.next()) {
      phoneNumber = rs.getString(1);
      name = rs.getString(2);
      address = rs.getString(3);
      paymentMethod = rs.getString(4);
    }
    rs.close();
    PaymentMethod method;
    switch (paymentMethod) {
      case "Mobile":
        method = PaymentMethod.Mobile;
        break;
      case "MasterCard":
        method = PaymentMethod.MasterCard;
        break;
      case "Visa":
        method = PaymentMethod.Visa;
        break;
      case "Cash":
        method = PaymentMethod.Cash;
        break;
      default:
        method = PaymentMethod.None;
        return null;
    }
    return new Customer(name, address, phoneNumber, method);
  }

  public static List<String> getListofCustomer() throws SQLException {
    List<String> customers = new ArrayList<String>();
    String query = "SELECT phoneNumber FROM `customer`";
    PreparedStatement preparedstatement = conn.prepareStatement(query);
    ResultSet rs = preparedstatement.executeQuery();
    while (rs.next()) {
      customers.add("Phone number: " + rs.getString(1));
    }
    rs.close();
    return customers;
  }

  public static Boolean deleteCustomerDetail(Customer customer) throws SQLException {
    if (!(getCustomerDetail(customer.getPhoneNo()).getPaymentMethod().equals(PaymentMethod.None))) {
      String query = "DELETE FROM `hotel`.`customer` WHERE (`phoneNumber` = ?)";
      PreparedStatement preparedstatement = conn.prepareStatement(query);
      preparedstatement.setString(1, customer.getPhoneNo());
      preparedstatement.execute();
      return true;
    } else {
      return false;
    }
  }

  public static boolean changeCustomerDetail(Customer customer) throws SQLException {
    if (deleteCustomerDetail(customer) && addCustomer(customer))
      return true;
    return false;
  }

  /*
   * This method works with the first verify button in addBooking page It returns
   * 1-3 telling if booking is possible 1: booking possible 2: invalid date range
   * 3: room does not exist
   */
  public static int bookingPossible(int roomNumber, String startDate, String endDate) throws SQLException {
    /* Check if room exists */
    try {
      String queryRoom = "SELECT * from rooms where Number=?";
      PreparedStatement preparedStatementRoom = conn.prepareStatement(queryRoom);
      preparedStatementRoom.setInt(1, roomNumber);
      ResultSet rs1 = preparedStatementRoom.executeQuery();
      if (rs1.next()) {
        /* make sure past dates are excluded */
        LocalDate today = LocalDate.now();
        String[] s = startDate.split("/");
        LocalDate inputStart = LocalDate.of(Integer.parseInt(s[2]), Integer.parseInt(s[0]), Integer.parseInt(s[1]));
        if (inputStart.compareTo(today) < 0) {
          // past dates are invalid
          return 2;
        }
        /* make sure start date is after end date */
        int userinputStart = convertDate(startDate); // user input
        int userinputEnd = convertDate(endDate); // user input
        if (userinputStart >= userinputEnd) {
          // invalid date range when start >= end
          return 2;
        }
        String query = "SELECT Booking_number, Room_number, Customer_phone, Start_date, End_date, Paid, Price FROM booking WHERE Room_number = ?";
        PreparedStatement preparedstatement = conn.prepareStatement(query);
        preparedstatement.setInt(1, roomNumber);
        ResultSet rs = preparedstatement.executeQuery();
        while (rs.next()) {
          // make sure userinput date is either smaller than booked start date
          // or greater than booked end date
          int bookedStart = convertDate(rs.getString(4)); // from database
          int bookedEnd = convertDate(rs.getString(5)); // from database
          if ((userinputStart >= bookedStart && userinputStart < bookedEnd)
              || (userinputEnd > bookedStart && userinputEnd <= bookedEnd)) {
            // user input date range overlaps with booked date range
            return 2;
          }
        }
        // user input range is outside the range in database
        return 1;
      } else {
        // room does not exist
        return 3;
      }
    } catch (ArrayIndexOutOfBoundsException | DateTimeException e) {
      return 0;
    }
  }

  /* return a list of available rooms within a date range */
  public static ArrayList<Room> getRooms(String startDate, String endDate) throws SQLException {
    ArrayList<Room> roomList = new ArrayList<>();
    String query = "SELECT * FROM rooms";
    PreparedStatement preparedstatement = conn.prepareStatement(query);
    ResultSet rs = preparedstatement.executeQuery();
    while (rs.next()) {
      Room room = getRoomDetail(rs.getInt(1));
      if (bookingPossible(room.getNumber(), startDate, endDate) == 1) {
        room.setButton();
        room.bedsToString();
        Room newRoom = new Room(room.getNumber(), room.getPrice(), room.getBedsString(), room.getSize(),
            room.getLocation(), room.getComment(), room.getButton());

        roomList.add(newRoom);
      }
    }
    return roomList;
  }

  /* add a booking if possible, return true if successful */
  public static boolean addBooking(Booking booking) throws SQLException {
    Date start = booking.getStartDate();
    String sDate = start.getMonth() + "/" + start.getDayOfMonth() + "/" + start.getYear();
    Date end = booking.getEndDate();
    String eDate = end.getMonth() + "/" + end.getDayOfMonth() + "/" + end.getYear();
    if (booking.getRoom() == null || booking.getCustomer() == null) {
      return false;
    } else if (bookingPossible(booking.getRoom().getNumber(), sDate, eDate) == 1) {
      /* calculate price based on days */
      double totalPrice = calculateTotalPriceForBooking(booking);
      /* add a booking in the database */
      String uploadQuery = "Insert into booking (Room_number,Customer_phone, Start_date, End_date,  Paid, Price)"
          + " values (?, ?, ?, ?, ?, ?)";
      PreparedStatement preparedstatementForUpload = conn.prepareStatement(uploadQuery);
      preparedstatementForUpload.setInt(1, booking.getRoom().getNumber());
      preparedstatementForUpload.setString(2, booking.getCustomer().getPhoneNo());
      preparedstatementForUpload.setString(3, sDate);
      preparedstatementForUpload.setString(4, eDate);
      preparedstatementForUpload.setString(5, "Not Paid");
      preparedstatementForUpload.setDouble(6, totalPrice);
      preparedstatementForUpload.executeUpdate();
      return true;
    } else {
      return false;
    }
  }

  // addBooking
  public static boolean addBooking2(Booking booking) throws SQLException {
    Date start = booking.getStartDate();
    String sDate = start.getMonth() + "/" + start.getDayOfMonth() + "/" + start.getYear();
    Date end = booking.getEndDate();
    String eDate = end.getMonth() + "/" + end.getDayOfMonth() + "/" + end.getYear();
    if (booking.getRoom() == null || booking.getCustomer() == null) {
      return false;
    } else if (bookingPossible(booking.getRoom().getNumber(), sDate, eDate) == 1) {
      /* calculate price based on days */
      double totalPrice = calculateTotalPriceForBooking(booking);
      /* add a booking in the database */
      String uploadQuery = "Insert into booking (Booking_number, Room_number,Customer_phone, Start_date, End_date,  Paid, Price)"
          + " values (?, ?, ?, ?, ?, ?, ?)";
      PreparedStatement preparedstatementForUpload = conn.prepareStatement(uploadQuery);
      preparedstatementForUpload.setInt(1, booking.getBookingNumber());
      preparedstatementForUpload.setInt(2, booking.getRoom().getNumber());
      preparedstatementForUpload.setString(3, booking.getCustomer().getPhoneNo());
      preparedstatementForUpload.setString(4, sDate);
      preparedstatementForUpload.setString(5, eDate);
      preparedstatementForUpload.setString(6, "Not Paid");
      preparedstatementForUpload.setDouble(7, totalPrice);
      preparedstatementForUpload.executeUpdate();
      return true;
    } else {
      return false;
    }
  }

  // calculate total price of a booking based on days
  public static double calculateTotalPriceForBooking(Booking b) {
    Date start = b.getStartDate();
    Date end = b.getEndDate();
    double totalPrice = 0;
    LocalDate localStart = LocalDate.of(start.getYear(), start.getMonth(), start.getDayOfMonth());
    LocalDate localend = LocalDate.of(end.getYear(), end.getMonth(), end.getDayOfMonth());
    int days = (int) Duration.between(localStart.atStartOfDay(), localend.atStartOfDay()).toDays();
    totalPrice = b.getRoom().getPrice() * days;
    return totalPrice;
  }

  // convert string date to int date
  public static int convertDate(String string) {
    String[] dateList = string.split("/");
    /* if length of month/day is 1, append it with 0 */
    if (dateList[0].length() == 1) {
      dateList[0] = "0" + dateList[0];
    }
    if (dateList[1].length() == 1) {
      dateList[1] = "0" + dateList[1];
    }
    // format: yyyymmdd
    String dateString = dateList[2] + dateList[0] + dateList[1];
    int dateInt = Integer.parseInt(dateString);
    return dateInt;
  }

  public static int convertDateMysql(String string) {
    String[] dateList = string.split("/");
    /* if length of month/day is 1, append it with 0 */
    if (dateList[1].length() == 1) {
      dateList[1] = "0" + dateList[1];
    }
    if (dateList[2].length() == 1) {
      dateList[2] = "0" + dateList[2];
    }
    // format: yyyymmdd
    String dateString = dateList[0] + dateList[1] + dateList[2];
    int dateInt = Integer.parseInt(dateString);
    return dateInt;
  }

  public static List<Booking> allBookings() throws SQLException {
    List<Booking> lst = new ArrayList<Booking>();
    String queryRoom = "SELECT Booking_number, Room_number, Customer_phone, Start_date, End_date, Paid, Price FROM `hotel`.`booking`";
    PreparedStatement preparedStatementRoom = conn.prepareStatement(queryRoom);
    ResultSet rs = preparedStatementRoom.executeQuery();
    while (rs.next()) {
      int bookingNumber = rs.getInt(1);
      int roomNumber = rs.getInt(2);
      String customerPhone = rs.getString(3);
      Date startDate = new Date(Integer.valueOf(rs.getString(4).split("/")[2]),
          Integer.valueOf(rs.getString(4).split("/")[0]), Integer.valueOf(rs.getString(4).split("/")[1]));
      Date endDate = new Date(Integer.valueOf(rs.getString(5).split("/")[2]),
          Integer.valueOf(rs.getString(5).split("/")[0]), Integer.valueOf(rs.getString(5).split("/")[1]));
      String paid = rs.getString(6);
      Double price = rs.getDouble(7);
      Room room = getRoomDetail(roomNumber);
      Customer customer = getCustomerDetail(customerPhone);
      Booking book;
      if (paid.equals("Paid")) {
        book = new Booking(bookingNumber, room, customer, startDate, endDate, true, price);
      } else {
        book = new Booking(bookingNumber, room, customer, startDate, endDate, false, price);
      }
      lst.add(book);
    }
    return lst;
  }

  public static Booking getBookingDetail(String inputBookingNo) throws SQLException {
    String query = "SELECT Booking_number, Room_number, Customer_phone, Start_date, End_date, Paid, Price FROM `hotel`.`booking` WHERE (`Booking_number` = ?)";
    PreparedStatement preparedstatement = conn.prepareStatement(query);
    preparedstatement.setString(1, inputBookingNo);
    ResultSet rs = preparedstatement.executeQuery();
    Booking booking = null;
    Integer bookingNumber = 0;
    int roomNumber = 0;
    String customerPhone = "";
    Date startDate = null;
    Date endDate = null;
    String paid = "";
    Double price = 0.0;
    if (rs.next()) {
      bookingNumber = rs.getInt(1);
      roomNumber = rs.getInt(2);
      customerPhone = rs.getString(3);
      startDate = new Date(Integer.valueOf(rs.getString(4).split("/")[2]),
          Integer.valueOf(rs.getString(4).split("/")[0]), Integer.valueOf(rs.getString(4).split("/")[1]));
      endDate = new Date(Integer.valueOf(rs.getString(5).split("/")[2]), Integer.valueOf(rs.getString(5).split("/")[0]),
          Integer.valueOf(rs.getString(5).split("/")[1]));
      paid = rs.getString(2);
      price = rs.getDouble(7);

      if (paid.equals("Paid")) {
        booking = new Booking(bookingNumber, getRoomDetail(roomNumber), getCustomerDetail(customerPhone), startDate,
            endDate, true, price);
      } else if (paid.equals("Not Paid")) {
        booking = new Booking(bookingNumber, getRoomDetail(roomNumber), getCustomerDetail(customerPhone), startDate,
            endDate, false, price);
      } else {
        booking = new Booking(bookingNumber, getRoomDetail(roomNumber), getCustomerDetail(customerPhone), startDate,
            endDate, false, price);
      }
    } else {
      booking = new Booking(getRoomDetail(roomNumber), getCustomerDetail(customerPhone), startDate, endDate, false);
    }

    rs.close();
    return booking;
  }

  /*
   * Qingqing: This method is not used It is replaced by
   * calculateTotalPriceForBooking(Booking b) at line 482 public static Double
   * getBookingPrice(String inputBookingNo) throws SQLException{ String query =
   * "SELECT Booking_number, Room_number, Customer_phone, Start_date, End_date, Paid, Price FROM `hotel`.`booking` WHERE (`Booking_number` = ?)"
   * ; PreparedStatement preparedstatement = conn.prepareStatement(query);
   * preparedstatement.setString(1, inputBookingNo); ResultSet rs =
   * preparedstatement.executeQuery(); Double price = 0.0; while (rs.next()) {
   * price = Double.valueOf(rs.getString(7)); } rs.close(); return price; }
   */

  public static Boolean addBookingDetail(Booking booking) throws SQLException {
    if (getBookingDetail(String.valueOf(booking.getBookingNumber())).getBookingNumber() == 0) {
      // convert Date objects to strings (mm/dd/yyyy)
      Date startDate = booking.getStartDate();
      String start = "" + startDate.getMonth() + "/" + startDate.getDayOfMonth() + "/" + startDate.getYear();
      Date endDate = booking.getEndDate();
      String end = "" + endDate.getMonth() + "/" + endDate.getDayOfMonth() + "/" + endDate.getYear();

      String query = "INSERT INTO `hotel`.`booking` (`Booking_number`, `Room_number`, `Customer_phone`, `Start_date`, `End_date`, `Paid`, `Price`) VALUES (?,?,?,?,?,?,?);";
      PreparedStatement preparedstatement = conn.prepareStatement(query);
      preparedstatement.setString(1, String.valueOf(booking.getBookingNumber()));
      preparedstatement.setString(2, String.valueOf(booking.getRoom().getNumber()));
      preparedstatement.setString(3, String.valueOf(booking.getCustomer().getPhoneNo()));
      preparedstatement.setString(4, start);
      preparedstatement.setString(5, end);
      preparedstatement.setString(6, String.valueOf(booking.isPaidString()));
      preparedstatement.setString(7, String.valueOf(booking.getTotalPrice()));
      preparedstatement.execute();
      return true;
    } else {
      return false;
    }
  }

  public static Boolean deleteBookingDetail(Booking booking) throws SQLException {
    if (getBookingDetail(String.valueOf(booking.getBookingNumber())).getBookingNumber() != 0) {
      String query = "DELETE FROM `hotel`.`booking` WHERE (`Booking_number` = ?)";
      PreparedStatement preparedstatement = conn.prepareStatement(query);
      preparedstatement.setString(1, String.valueOf(booking.getBookingNumber()));
      preparedstatement.execute();
      return true;
    }
    return false;
  }

  public static void changeBookingDetail(Booking booking) throws SQLException {
    deleteBookingDetail(booking);
    addBookingDetail(booking);
  }

  public static void setPhoneNoInBooking(String oldPhoneNumber, String newPhoneNumber) throws SQLException {
    String query = "Select Booking_number, Room_number, Customer_phone, Start_date, End_date, Paid, Price FROM `hotel`.`booking` WHERE (`Customer_phone` = ?)";
    PreparedStatement preparedstatement = conn.prepareStatement(query);
    preparedstatement.setString(1, oldPhoneNumber);
    ResultSet rs = preparedstatement.executeQuery();
    if (rs.next()) {
      String update = "Update booking set Customer_phone = ? WHERE (`Customer_phone` = ?)";
      PreparedStatement preparedstatementUpdate = conn.prepareStatement(update);
      preparedstatementUpdate.setString(1, newPhoneNumber);
      preparedstatementUpdate.setString(2, oldPhoneNumber);
      preparedstatementUpdate.executeUpdate();
    }
  }

  public static List<User> getAllUser() throws SQLException {
    List<User> users = new ArrayList<User>();

    String query = "SELECT userName, password, accessLevel, firstName, lastName FROM `hotel`.`user`";
    PreparedStatement preparedstatement = conn.prepareStatement(query);
    ResultSet rs = preparedstatement.executeQuery();
    User user = null;
    while (rs.next()) {
      String userName = rs.getString(1);
      String password = rs.getString(2);
      String userType = rs.getString(3);
      String firstName = rs.getString(4);
      String lastName = rs.getString(5);
      if (userType.equals("Admin")) {
        user = new Administrator(userName, password, firstName, lastName, true);
      } else {
        user = new ReceptionStaff(userName, password, firstName, lastName, true);
      }
      users.add(user);
    }
    rs.close();

    return users;
  }
}
