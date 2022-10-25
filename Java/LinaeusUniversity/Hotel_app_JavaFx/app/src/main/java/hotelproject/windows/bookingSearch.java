package hotelproject.windows;

import hotelproject.Booking;
import hotelproject.Customer;
import hotelproject.Date;
import hotelproject.Room;
import hotelproject.dbHandeler;

import java.util.*;
import java.sql.SQLException;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class bookingSearch extends Windows {
  protected List<Booking> b;

  bookingSearch(BorderPane root) {
    super(root);

    // pagenameLabel
    BorderPane bp = new BorderPane();
    HBox titleBox = new HBox();
    Label pagenameLabel = new Label("Booking overview");
    pagenameLabel.setStyle("-fx-font-size: 45;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;");
    titleBox.getChildren().addAll(pagenameLabel);
    titleBox.setAlignment(Pos.CENTER_LEFT);

    String buttonStyle = "-fx-font-size:30;-fx-font-family:'arial rounded mt bold';-fx-background-color: DarkGoldenRod;-fx-text-fill:Silver;-fx-background-radius:10;";

    // Serach_text
    Label searchtext = new Label("Search Booking");
    searchtext.setStyle("-fx-font-size: 15;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;");

    // calender
    TextField roomInput = new TextField();
    roomInput.setAlignment(Pos.CENTER_LEFT);
    roomInput.setPromptText("Room No. i.e. 1023 ");
    roomInput.setVisible(false);// so that enter button postioning is right
    roomInput.setManaged(false);// so that enter button postioning is right

    DatePicker DatePicker = new DatePicker();
    DatePicker.setCenterShape(true);
    DatePicker.setPromptText("MM/DD/YYYY");
    DatePicker.setVisible(true);
    DatePicker.setMaxWidth(150);

    TextField name_detail = new TextField();
    name_detail.setPromptText("Name/details");
    name_detail.setVisible(false);// so that enter button postioning is right
    name_detail.setManaged(false);

    Button enterButton = new Button("Enter");
    enterButton.setAlignment(Pos.TOP_LEFT);
    enterButton.setStyle(
        "-fx-font-size:13;-fx-font-family:'arial rounded mt bold';-fx-background-color: DarkGoldenRod;-fx-text-fill:Silver;-fx-background-radius:10;");
    hoverOverAnimation(enterButton);

    // Page name + Drop down menu
    ComboBox<String> comboBox = new ComboBox<String>();
    comboBox.getSelectionModel().select(0);
    comboBox.setCenterShape(true);
    comboBox.setPrefWidth(140);

    List<String> options = new ArrayList<>();
    options.add("By Date");
    options.add("By Room");
    options.add("By Name/Detail");
    comboBox.getItems().addAll(options);
    comboBox.setValue("By Date");

    // roomInput.setManaged(false);
    HBox hb = new HBox();
    hb.getChildren().addAll(searchtext, comboBox, DatePicker, roomInput, name_detail, enterButton);
    hb.setSpacing(10);

    hb.setPadding(new Insets(20, 0, 20, 0));
    Label label = new Label("Bookings");
    label.setStyle("-fx-font-size: 30;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;");

    VBox top = new VBox();
    top.getChildren().addAll(titleBox, hb, label);
    bp.setTop(top);

    ObservableList<BookingTable> bookings = FXCollections.observableArrayList();
    try {
      b = dbHandeler.allBookings();
      for (Booking booking : b) {
        bookings.add(new BookingTable(booking));
      }
    } catch (SQLException e1) {
      Alert invalidDates = new Alert(AlertType.INFORMATION);
      invalidDates.setHeaderText("something wrong in database.");
      invalidDates.showAndWait();
      e1.printStackTrace();;
    }
    TableView<BookingTable> table = new TableView<BookingTable>();
    table.setEditable(true);

    TableColumn<BookingTable, Integer> bookingNumber = new TableColumn<BookingTable, Integer>("Booking No.");
    bookingNumber.setMinWidth(100);
    TableColumn<BookingTable, Integer> roomNumber = new TableColumn<BookingTable, Integer>("Room No.");
    roomNumber.setMinWidth(100);
    TableColumn<BookingTable, String> startingDate = new TableColumn<BookingTable, String>("Starting Date");
    startingDate.setMinWidth(100);
    TableColumn<BookingTable, String> endingDate = new TableColumn<BookingTable, String>("Ending Date");
    endingDate.setMinWidth(100);
    TableColumn<BookingTable, String> customerName = new TableColumn<BookingTable, String>("Customer Name");
    customerName.setMinWidth(300);
    TableColumn<BookingTable, Integer> customerPhone = new TableColumn<BookingTable, Integer>("Customer Phone");
    customerPhone.setMinWidth(300);
    TableColumn<BookingTable, Boolean> paid = new TableColumn<BookingTable, Boolean>("Paid");
    paid.setMinWidth(50);
    TableColumn<BookingTable, Double> fixedPrice = new TableColumn<BookingTable, Double>("Fixed Price");
    fixedPrice.setMinWidth(200);
    // TableColumn<BookingTable, Button> paidButton = new TableColumn<BookingTable,
    // Button>("Paid Button");
    // paidButton.setMinWidth(200);

    bookingNumber.setCellValueFactory(new PropertyValueFactory<BookingTable, Integer>("bookingNumber"));
    roomNumber.setCellValueFactory(new PropertyValueFactory<BookingTable, Integer>("roomNumber"));
    startingDate.setCellValueFactory(new PropertyValueFactory<BookingTable, String>("startDate"));
    endingDate.setCellValueFactory(new PropertyValueFactory<BookingTable, String>("endDate"));
    customerName.setCellValueFactory(new PropertyValueFactory<BookingTable, String>("customerName"));
    customerPhone.setCellValueFactory(new PropertyValueFactory<BookingTable, Integer>("customerPhone"));
    paid.setCellValueFactory(new PropertyValueFactory<BookingTable, Boolean>("paid"));
    fixedPrice.setCellValueFactory(new PropertyValueFactory<BookingTable, Double>("fixedPrice"));
    // paidButton.setCellValueFactory(new PropertyValueFactory<BookingTable,
    // Button>("paidButton"));

    table.setItems(bookings);
    table.getColumns().addAll(bookingNumber, roomNumber, startingDate, endingDate, customerName, customerPhone, paid,
        fixedPrice);// ,paidButton);

    table.setRowFactory(tv -> {
      TableRow<BookingTable> row = new TableRow<BookingTable>();
      row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (!row.isEmpty())) {
          BookingTable bookingInfo = row.getItem();
          bookingsearchPopWindow(bookingInfo, root);
        }
      });
      return row;
    });
    bp.setCenter(table);

    EventHandler<ActionEvent> roomSearchEvent = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {

        if (comboBox.getValue().equals("By Date")) {
          DatePicker.setVisible(true);
          DatePicker.setManaged(true);
          roomInput.setVisible(false);
          roomInput.setManaged(false);
          name_detail.setVisible(false);
          name_detail.setManaged(false);
          comboBox.setValue("By Date");
          hb.setSpacing(10);
          ObservableList<BookingTable> allbookings = FXCollections.observableArrayList();
          try {
            for (int i = 0; i < b.size(); i++) {
              allbookings.add(new BookingTable(b.get(i)));
            }
          } catch (SQLException e1) {
            Alert invalidDates = new Alert(AlertType.INFORMATION);
            invalidDates.setHeaderText("something wrong in database.");
            invalidDates.showAndWait();
            e1.printStackTrace();
          }
          table.getItems().clear();
          table.setItems(allbookings);
        }

        else if (comboBox.getValue().equals("By Room")) {
          DatePicker.setVisible(false);
          DatePicker.setManaged(false);
          roomInput.setVisible(true);
          roomInput.setManaged(true);
          name_detail.setVisible(false);
          name_detail.setManaged(false);
          comboBox.setValue("By Room");
          hb.setSpacing(10);
          ObservableList<BookingTable> allbookings = FXCollections.observableArrayList();
          try {
            for (int i = 0; i < b.size(); i++) {
              allbookings.add(new BookingTable(b.get(i)));
            }
          } catch (SQLException e1) {
            Alert invalidDates = new Alert(AlertType.INFORMATION);
            invalidDates.setHeaderText("something wrong in database.");
            invalidDates.showAndWait();
            e1.printStackTrace();
          }
          table.getItems().clear();
          table.setItems(allbookings);
        }

        else if (comboBox.getValue().equals("By Name/Detail")) {
          DatePicker.setVisible(false);
          DatePicker.setManaged(false);
          roomInput.setVisible(false);
          roomInput.setManaged(false);
          name_detail.setVisible(true);
          name_detail.setManaged(true);
          comboBox.setValue("By Name/Detail");
          hb.setSpacing(10);
          ObservableList<BookingTable> allbookings = FXCollections.observableArrayList();
          try {
            for (int i = 0; i < b.size(); i++) {
              allbookings.add(new BookingTable(b.get(i)));
            }
          } catch (SQLException e1) {
            Alert invalidDates = new Alert(AlertType.INFORMATION);
            invalidDates.setHeaderText("something wrong in database.");
            invalidDates.showAndWait();
            e1.printStackTrace();
          }
          table.getItems().clear();
          table.setItems(allbookings);
        } else {
        }
      }
    };

    comboBox.setOnAction(roomSearchEvent);

    Button backButton = new Button("Back");
    backButton.setAlignment(Pos.CENTER);
    backButton.setStyle(buttonStyle);
    hoverOverAnimation(backButton);

    Button searchARoom = new Button("Room availability calendar");
    searchARoom.setStyle(buttonStyle);
    hoverOverAnimation(searchARoom);
    searchARoom.setOnAction(e -> new searchRoom(root));
    hoverOverAnimation(searchARoom);

    EventHandler<ActionEvent> mainMenuBack = new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        new mainMenu(root);
      }
    };
    backButton.setOnAction(mainMenuBack);

    Button addBookingButton = new Button("Add a booking");
    addBookingButton.setAlignment(Pos.CENTER);
    addBookingButton.setStyle(buttonStyle);
    hoverOverAnimation(addBookingButton);

    EventHandler<ActionEvent> addBookinglink = new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        new addBooking(root);
      }
    };
    addBookingButton.setOnAction(addBookinglink);

    // search by Room number
    // MM/DD/YYYY

    enterButton.setOnAction(e -> {
      String input = DatePicker.getEditor().getText();
      String input1 = roomInput.getText();
      String input2 = name_detail.getText();

      int flag = 0;
      // Dateseach
      if ((comboBox.getValue() == "By Date") && input.contains("/")) {
        ArrayList<String> resultsForDates = new ArrayList<String>();
        ObservableList<BookingTable> matchingResults = FXCollections.observableArrayList();

        for (int i = 0; i < b.size(); i++) {
          Booking result = b.get(i);
          // datepicker input>
          if (((dbHandeler.convertDateMysql(result.getStartDate().toString()) <= dbHandeler.convertDate(input))
              && (dbHandeler.convertDateMysql(result.getEndDate().toString()) >= dbHandeler.convertDate(input)))) {
            table.getItems().clear();
            resultsForDates.add(result.getStartDate().toString());

            try {
              matchingResults.add(new BookingTable(result));
            } catch (SQLException e1) {
              Alert invalidDates = new Alert(AlertType.INFORMATION);
              invalidDates.setHeaderText("something wrong in database.");
              invalidDates.showAndWait();
              e1.printStackTrace();
            }
          }
        }
        table.setItems(matchingResults);

        // roomsearch
      } else if ((comboBox.getValue() == "By Room") && isInteger(input1)) {
        ObservableList<BookingTable> matchingResults = FXCollections.observableArrayList();
        for (int i = 0; i < b.size(); i++) {
          Booking result = b.get(i);
          if (result.getRoom().getNumber() == Integer.parseInt(input1)) {

            table.getItems().clear();
            try {
              matchingResults.add(new BookingTable(result));
            } catch (SQLException e1) {
              Alert invalidDates = new Alert(AlertType.INFORMATION);
              invalidDates.setHeaderText("something wrong in database.");
              invalidDates.showAndWait();
              e1.printStackTrace();
            }
          }
        }
        table.setItems(matchingResults);

      }

      // Name/Detail Search
      else if ((comboBox.getValue().equals("By Name/Detail")) && (input2.length() > 0)) {
        ObservableList<BookingTable> matchingResults = FXCollections.observableArrayList();
        for (int i = 0; i < b.size(); i++) {
          Booking result = b.get(i);
          if (result.getCustomer().getName().contains(input2) || result.getRoom().getComment().contains(input2)
              || result.getRoom().getLocation().contains(input2)) {
            table.getItems().clear();
            flag = 1;

            try {
              matchingResults.add(new BookingTable(result));
            } catch (SQLException e1) {
              Alert invalidDates = new Alert(AlertType.INFORMATION);
              invalidDates.setHeaderText("something wrong in database.");
              invalidDates.showAndWait();
              e1.printStackTrace();
            }

          } else {

          }
        }
        if (flag == 0) {
          Alert errorInvalid = new Alert(AlertType.INFORMATION);
          errorInvalid.setHeaderText("Booking not found");
          errorInvalid.show();
        } else {
          table.setItems(matchingResults);
        }
      } else if (input1.isEmpty() || input.isEmpty()) {
        ObservableList<BookingTable> allbookings = FXCollections.observableArrayList();
        try {
          for (int i = 0; i < b.size(); i++) {
            allbookings.add(new BookingTable(b.get(i)));
          }
        } catch (SQLException e1) {
          Alert invalidDates = new Alert(AlertType.INFORMATION);
          invalidDates.setHeaderText("something wrong in database.");
          invalidDates.showAndWait();
          e1.printStackTrace();
        }
        table.getItems().clear();
        table.setItems(allbookings);
      } else {
        Alert errorInvalid = new Alert(AlertType.INFORMATION);
        errorInvalid.setHeaderText("Invalid Input");
        errorInvalid.show();
      }
    });

    BorderPane hbutton = new BorderPane();
    hbutton.setLeft(backButton);
    hbutton.setRight(addBookingButton);
    hbutton.setCenter(searchARoom);
    hbutton.setPadding(new Insets(15, 27, 0, 27));

    bp.setBottom(hbutton);
    bp.setPadding(new Insets(30, 30, 30, 30));

    root.setCenter(bp);
  }

  public void bookingsearchPopWindow(BookingTable bookingInfo, BorderPane root) {
    Stage popstage = new Stage();

    Scene c = bookingsearchPopWindowChangeScene(root, bookingInfo, popstage);
    Scene d = bookingsearchPopWindowDetailScene(root, bookingInfo, popstage, c);

    popstage.setTitle("Booking No " + bookingInfo.getBookingNumber());
    popstage.setScene(d);
    popstage.initModality(Modality.APPLICATION_MODAL);
    popstage.setAlwaysOnTop(true);
    popstage.setResizable(false);

    if (!popstage.isShowing()) {
      popstage.show();
    }
  }

  public void bookingsearchPopWindowChangeBegin(BookingTable bookingInfo, BorderPane root) {
    Stage popstage = new Stage();

    Scene c = bookingsearchPopWindowChangeScene(root, bookingInfo, popstage);

    popstage.setTitle("Booking No " + bookingInfo.getBookingNumber());
    popstage.setScene(c);
    popstage.initModality(Modality.APPLICATION_MODAL);
    popstage.setAlwaysOnTop(true);
    popstage.setResizable(false);

    if (!popstage.isShowing()) {
      popstage.show();
    }
  }

  public Scene bookingsearchPopWindowDetailScene(BorderPane root, BookingTable bookingInfo, Stage popstage,
      Scene changScene) {
    BorderPane popbp = new BorderPane();
    BorderPane popButton = new BorderPane();

    String popTextStyle1 = "-fx-font-size: 25;-fx-font-family: 'arial rounded mt bold';-fx-fill: White;";
    String popTextStyle2 = "-fx-font-size: 17;-fx-font-family: 'arial rounded mt bold';-fx-fill: White;";
    String popButtonStyleCanUse = "-fx-font-size:20;-fx-font-family:'arial rounded mt bold';-fx-background-color: DarkGoldenRod;-fx-text-fill:Silver;-fx-background-radius:10;";
    String popButtonStyleCanNotUse = "-fx-font-size:20;-fx-font-family:'arial rounded mt bold';-fx-background-color: Gray;-fx-text-fill:White;-fx-background-radius:10;";

    Text popBookingNo = new Text("Booking Number: " + bookingInfo.getBookingNumber());
    popBookingNo.setStyle(popTextStyle1);
    Text popBookingDates = new Text("From " + bookingInfo.getStartDate() + " To " + bookingInfo.getEndDate());
    popBookingDates.setStyle(popTextStyle1);
    Text popBookingTotalPrice = new Text("Total price: " + bookingInfo.getFixedPrice());
    popBookingTotalPrice.setStyle(popTextStyle1);

    Text popRoomNo = new Text("Room " + bookingInfo.getRoomNumber() + " Detail");
    popRoomNo.setStyle(popTextStyle2);

    Text popPrice = new Text("Price:");
    popPrice.setStyle(popTextStyle2);
    Text popBeds = new Text("Beds:");
    popBeds.setStyle(popTextStyle2);
    Text popSize = new Text("Size:");
    popSize.setStyle(popTextStyle2);
    Text popLocation = new Text("Location:");
    popLocation.setStyle(popTextStyle2);
    Text popComment = new Text("Comment:");
    popComment.setStyle(popTextStyle2);

    Text popPriceD = new Text(bookingInfo.getRoom().getPrice() + "/ day");
    popPriceD.setStyle(popTextStyle2);
    Text popBedsD = new Text(String.valueOf(bookingInfo.getRoom().getBeds()).substring(1,
        String.valueOf(bookingInfo.getRoom().getBeds()).length() - 1));
    popBedsD.setStyle(popTextStyle2);
    Text popSizeD = new Text(String.valueOf(bookingInfo.getRoom().getSize()));
    popSizeD.setStyle(popTextStyle2);
    Text popLocationD = new Text(bookingInfo.getRoom().getLocation());
    popLocationD.setStyle(popTextStyle2);
    Text popCommentD = new Text(bookingInfo.getRoom().getComment());
    popCommentD.setStyle(popTextStyle2);

    Text popCustomerName = new Text("Mr./Ms. " + bookingInfo.getCustomerName());
    popCustomerName.setStyle(popTextStyle2);

    Text popPhone = new Text("Phone number:");
    popPhone.setStyle(popTextStyle2);
    Text popAddress = new Text("Address:");
    popAddress.setStyle(popTextStyle2);
    Text popPayment = new Text("Payment method:");
    popPayment.setStyle(popTextStyle2);

    Text popPhoneD = new Text(bookingInfo.getCustomerPhone());
    popPhoneD.setStyle(popTextStyle2);
    Text popAddressD = new Text(bookingInfo.getCustomer().getAddress());
    popAddressD.setStyle(popTextStyle2);
    Text popPaymentD = new Text(bookingInfo.getCustomer().getStringpaymentMethod());
    popPaymentD.setStyle(popTextStyle2);

    Button popPaidButton = new Button("paid");
    if (bookingInfo.isPaid()) {
      popPaidButton.setStyle(popButtonStyleCanNotUse);
      popPaidButton.setOnAction(null);
      popPaidButton.setOnMouseEntered(null);
      popPaidButton.setOnMouseExited(null);
      setBackground(popbp, "background3.jpg");
    } else {
      hoverOverAnimation(popPaidButton);
      popPaidButton.setStyle(popButtonStyleCanUse);
      setBackground(popbp, "background2.jpg");
      popPaidButton.setOnAction(e -> {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setHeaderText("Pay now?");
        popstage.setAlwaysOnTop(false);
        confirm.showAndWait();
        if (confirm.getResult().equals(ButtonType.OK)) {
          try {
            bookingInfo.setPaid(true);
          } catch (SQLException e1) {
            Alert invalidDates = new Alert(AlertType.INFORMATION);
            invalidDates.setHeaderText("something wrong in database.");
            invalidDates.showAndWait();
            e1.printStackTrace();
          }
          new bookingSearch(root);
          popPaidButton.setStyle(popButtonStyleCanNotUse);
          popPaidButton.setOnAction(null);
          popPaidButton.setOnMouseEntered(null);
          popPaidButton.setOnMouseExited(null);
          setBackground(popbp, "background3.jpg");
          popButton.setCenter(null);
        }
        popstage.setAlwaysOnTop(true);
      });
    }

    Button popChangeButton = new Button("change");
    hoverOverAnimation(popChangeButton);
    popChangeButton.setStyle(popButtonStyleCanUse);
    popChangeButton.setOnAction(e -> {
      popstage.setScene(changScene);
    });

    Button popBackButton = new Button("Back");
    hoverOverAnimation(popBackButton);
    popBackButton.setStyle(popButtonStyleCanUse);
    popBackButton.setOnAction(e -> {
      popstage.close();
    });

    BorderPane popTitle = new BorderPane(popBookingDates, popBookingNo, null, popBookingTotalPrice, null);
    BorderPane.setAlignment(popBookingNo, Pos.CENTER);
    BorderPane.setAlignment(popBookingTotalPrice, Pos.CENTER);
    popTitle.setPadding(new Insets(0, 0, 10, 0));

    VBox popRoomLabel = new VBox(5, popPrice, popBeds, popSize, popLocation, popComment);
    VBox popRoom = new VBox(5, popPriceD, popBedsD, popSizeD, popLocationD, popCommentD);
    HBox popRoomDetail = new HBox(10, popRoomLabel, popRoom);
    popRoomDetail.setPadding(new Insets(7, 0, 0, 0));
    BorderPane popRoomInfo = new BorderPane(popRoomDetail, popRoomNo, null, null, null);
    BorderPane.setAlignment(popRoomNo, Pos.CENTER);
    BorderPane.setAlignment(popRoomInfo, Pos.CENTER);

    VBox popCustomerLabel = new VBox(5, popPhone, popAddress, popPayment);
    VBox popCustomer = new VBox(5, popPhoneD, popAddressD, popPaymentD);
    HBox popCustomerDetail = new HBox(10, popCustomerLabel, popCustomer);
    popCustomerDetail.setPadding(new Insets(7, 0, 0, 0));
    BorderPane popCustomerInfo = new BorderPane(popCustomerDetail, popCustomerName, null, null, null);
    BorderPane.setAlignment(popCustomerName, Pos.CENTER);
    BorderPane.setAlignment(popCustomerInfo, Pos.CENTER);

    HBox popInfo = new HBox(70, popRoomInfo, popCustomerInfo);
    popInfo.setAlignment(Pos.CENTER);

    popButton.setLeft(popBackButton);
    popButton.setRight(popPaidButton);
    if (!bookingInfo.isPaid()) {
      popButton.setCenter(popChangeButton);
    }
    popButton.setPadding(new Insets(5, 0, 0, 0));

    popbp.setCenter(popInfo);
    BorderPane.setAlignment(popInfo, Pos.CENTER);
    popbp.setTop(popTitle);
    popbp.setBottom(popButton);
    BorderPane.setAlignment(popButton, Pos.CENTER);

    popbp.setPadding(new Insets(15));

    Scene s = new Scene(popbp);

    return s;
  }

  /**
   * back button can use
   * 
   * @param root
   * @param bookingInfo
   * @param popstage
   * @return
   */
  public Scene bookingsearchPopWindowChangeScene(BorderPane root, BookingTable bookingInfo, Stage popstage) {
    BorderPane popbp = new BorderPane();
    popbp.setPadding(new Insets(15));
    setBackground(popbp, "background2.jpg");

    /* the style for all buttons */
    String buttonStyle = "-fx-font-size:20;-fx-font-family:'arial rounded mt bold';-fx-background-color: DarkGoldenRod;-fx-text-fill:Silver;-fx-background-radius:10;";
    String labelStyle = "-fx-font-size: 20;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: white;";
    String textStyle = "-fx-font-size: 20;-fx-font-family: 'arial rounded mt bold';-fx-fill: white;";

    Text bookingNumber = new Text("Booking Number: " + bookingInfo.getBookingNumber());
    bookingNumber.setStyle(textStyle);
    popbp.setTop(bookingNumber);
    BorderPane.setAlignment(bookingNumber, Pos.CENTER);

    BorderPane startPane = new BorderPane();
    startPane.setPadding(new Insets(10, 10, 0, 10));
    BorderPane endPane = new BorderPane();
    endPane.setPadding(new Insets(0, 10, 0, 10));
    BorderPane roomPane = new BorderPane();
    roomPane.setPadding(new Insets(0, 10, 0, 10));
    BorderPane phonePane = new BorderPane();
    phonePane.setPadding(new Insets(0, 10, 0, 10));
    VBox centerBox = new VBox(10, startPane, endPane, roomPane, phonePane);
    centerBox.setAlignment(Pos.CENTER);
    popbp.setCenter(centerBox);
    BorderPane.setAlignment(centerBox, Pos.CENTER);

    // start date
    Label startDate = new Label("Choose start date: ");
    startDate.setStyle(labelStyle);
    startPane.setLeft(startDate);

    DatePicker startDatePicker = new DatePicker();
    startDatePicker.setPromptText(String.valueOf(bookingInfo.getStartDate()));
    startPane.setRight(startDatePicker);

    // end date
    Label endDate = new Label("Choose end date: ");
    endDate.setStyle(labelStyle);
    endPane.setLeft(endDate);

    DatePicker endDatePicker = new DatePicker();
    endDatePicker.setPromptText(String.valueOf(bookingInfo.getEndDate()));
    endPane.setRight(endDatePicker);

    // room number
    Label enterRoom = new Label("Enter room number: ");
    enterRoom.setStyle(labelStyle);
    roomPane.setLeft(enterRoom);

    TextField roomInput = new TextField();
    roomInput.setPromptText(String.valueOf(bookingInfo.getRoomNumber()));

    Button availableButton = new Button("Available rooms");
    hoverOverAnimation(availableButton);
    availableButton.setStyle(
        "-fx-font-size:13;-fx-font-family:'arial rounded mt bold';-fx-background-color: DarkGoldenRod;-fx-text-fill:Silver;-fx-background-radius:5;");

    HBox roomRightBox = new HBox(roomInput, availableButton);
    roomRightBox.setPadding(new Insets(0, 0, 0, 10));
    roomRightBox.setAlignment(Pos.CENTER);
    roomPane.setRight(roomRightBox);
    BorderPane.setAlignment(roomRightBox, Pos.CENTER_LEFT);

    // phone number
    Label phoneNumber = new Label("Phone number: ");
    phoneNumber.setStyle(labelStyle);
    phonePane.setLeft(phoneNumber);

    Text phoneInput = new Text(String.valueOf(bookingInfo.getCustomer().getPhoneNo()));
    phoneInput.setStyle(textStyle);

    HBox phoneRightBox = new HBox(0, phoneInput);
    phonePane.setRight(phoneRightBox);
    BorderPane.setAlignment(phoneRightBox, Pos.CENTER_LEFT);

    popbp.setCenter(centerBox);

    /* cancelDone contains: cancel, done */
    BorderPane cancelDone = new BorderPane();
    cancelDone.setPadding(new Insets(10, 10, 5, 10));

    Button cancel = new Button("Back");
    cancel.setStyle(buttonStyle);
    hoverOverAnimation(cancel);
    cancelDone.setLeft(cancel);
    cancel.setOnAction(e -> {
      popstage.close();
      bookingsearchPopWindow(bookingInfo, root);
    });

    Button done = new Button("Done");
    done.setStyle(buttonStyle);
    hoverOverAnimation(done);
    cancelDone.setRight(done);

    popbp.setBottom(cancelDone);


    EventHandler<ActionEvent> doneButtonEvent = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        // clear strings
        startDateString = "";
        endDateString = "";
        selectRoomNumber = "";
        Date start = null;
        Date end = null;
        // temporarily store the current booking here
        Booking temp = new Booking(bookingInfo.getBookingNumber(), bookingInfo.getRoom(), bookingInfo.getCustomer(), bookingInfo.getStartDate(), bookingInfo.getEndDate(), bookingInfo.isPaid());
        try {
          dbHandeler.deleteBookingDetail(temp);
        } catch (SQLException e2) {
          e2.printStackTrace();
        }

        try {
          Room room = dbHandeler.getRoomDetail(Integer.parseInt(roomInput.getText()));
          Customer customer = dbHandeler.getCustomerDetail(phoneInput.getText());
          String[] sDate = startDatePicker.getEditor().getText().split("/");
          String[] eDate = endDatePicker.getEditor().getText().split("/");
          start = new Date(Integer.parseInt(sDate[2]), Integer.parseInt(sDate[0]), Integer.parseInt(sDate[1]));
          end = new Date(Integer.parseInt(eDate[2]), Integer.parseInt(eDate[0]), Integer.parseInt(eDate[1]));
          Booking booking = new Booking(bookingInfo.getBookingNumber(), room, customer, start, end, false);
          dbHandeler.deleteBookingDetail(booking);
          int bookingPossibility = dbHandeler.bookingPossible(Integer.parseInt(roomInput.getText()),
              startDatePicker.getEditor().getText(), endDatePicker.getEditor().getText());
          if (bookingPossibility == 1) {
            if (dbHandeler.addBooking(booking)) {
              Alert booked = new Alert(AlertType.INFORMATION);
              booked.setHeaderText("Booking edited successfully");
              booked.show();
              popstage.close();
              new bookingSearch(root);
            } else {
              Alert notBooked = new Alert(AlertType.INFORMATION);
              notBooked.setHeaderText("Booking failed");
              notBooked.show();
            }
          } else if (bookingPossibility == 2) {
            Alert invalidDates = new Alert(AlertType.INFORMATION);
            invalidDates.setHeaderText("Invalid dates");
            invalidDates.show();
          } else if (bookingPossibility == 3) {
            Alert roomNotExist = new Alert(AlertType.INFORMATION);
            roomNotExist.setHeaderText("Room " + roomInput.getText() + " does not exist.");
            roomNotExist.show();
          }
        } catch (NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException e2) {
          popstage.setAlwaysOnTop(false);
          Alert invalidDates = new Alert(AlertType.INFORMATION);
          invalidDates.setHeaderText("you need select start date, end date in right format(MM/DD/YYYY) and a room, then click Done button.");
          invalidDates.showAndWait();
          popstage.setAlwaysOnTop(true);
        } catch (SQLException e1) {
          popstage.setAlwaysOnTop(false);
          Alert invalidDates = new Alert(AlertType.INFORMATION);
          invalidDates.setHeaderText("something wrong in database.");
          invalidDates.showAndWait();
          popstage.setAlwaysOnTop(true);
          e1.printStackTrace();
        }
        
      }
    };
    done.setOnAction(doneButtonEvent);

    Scene s = new Scene(popbp);

    if (!startDateString.isEmpty() && !endDateString.isEmpty()) {
      // fill in the date fields with prestored data
      startDatePicker.getEditor().setText(startDateString);
      endDatePicker.getEditor().setText(endDateString);
    }
    if (!selectRoomNumber.isEmpty()) {
      // fill in the room field with number
      roomInput.setText(selectRoomNumber);
    }

    EventHandler<ActionEvent> availableButtonEvent = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        // temporarily store the current booking here
        Booking temp = new Booking(bookingInfo.getBookingNumber(), bookingInfo.getRoom(), bookingInfo.getCustomer(), bookingInfo.getStartDate(), bookingInfo.getEndDate(), bookingInfo.isPaid());
        try {
          dbHandeler.deleteBookingDetail(temp);
        } catch (SQLException e2) {
          e2.printStackTrace();
        }
        BorderPane popbp1 = new BorderPane();
        List<Room> roomList;
        ObservableList<PopRoomTable> roomTable = FXCollections.observableArrayList();
        try {
          roomList = dbHandeler.getRooms(startDatePicker.getEditor().getText(), endDatePicker.getEditor().getText());
          for (Room r : roomList) {
            roomTable.add(new PopRoomTable(r, popstage, s, bookingInfo, root));
          }
          TableView<PopRoomTable> table = new TableView<PopRoomTable>();

          final Label label = new Label("Available room details");
          label.setFont(new Font("Arial", 20));

          table.setEditable(false);

          TableColumn<PopRoomTable, Integer> rNumber = new TableColumn<>("Room number");
          TableColumn<PopRoomTable, Double> rPrice = new TableColumn<>("Price");
          TableColumn<PopRoomTable, String> rBeds = new TableColumn<>("Beds");
          TableColumn<PopRoomTable, Double> rSize = new TableColumn<>("Size");
          TableColumn<PopRoomTable, String> rLocation = new TableColumn<>("Location");
          TableColumn<PopRoomTable, String> rComments = new TableColumn<>("Comments");
          TableColumn<PopRoomTable, PopSelectButton> rSelect = new TableColumn<>("Select");
          rNumber.setCellValueFactory(new PropertyValueFactory<PopRoomTable, Integer>("number"));
          rNumber.setStyle("-fx-alignment: CENTER;");
          rPrice.setCellValueFactory(new PropertyValueFactory<PopRoomTable, Double>("price"));
          rPrice.setStyle("-fx-alignment: CENTER;");
          rBeds.setCellValueFactory(new PropertyValueFactory<PopRoomTable, String>("bedsString"));
          rBeds.setStyle("-fx-alignment: CENTER;");
          rSize.setCellValueFactory(new PropertyValueFactory<PopRoomTable, Double>("size"));
          rSize.setStyle("-fx-alignment: CENTER;");
          rLocation.setCellValueFactory(new PropertyValueFactory<PopRoomTable, String>("location"));
          rLocation.setStyle("-fx-alignment: CENTER;");
          rComments.setCellValueFactory(new PropertyValueFactory<PopRoomTable, String>("comment"));
          rComments.setStyle("-fx-alignment: CENTER;");
          rSelect.setCellValueFactory(new PropertyValueFactory<PopRoomTable, PopSelectButton>("SelectButton"));
          rSelect.setStyle("-fx-alignment: CENTER;");

          table.setItems(roomTable);
          table.getColumns().addAll(rNumber, rPrice, rBeds, rSize, rLocation, rComments, rSelect);
          table.setMinWidth(1000);
          table.setMaxWidth(2000);
          table.setMaxHeight(1500);

          // store the dates
          startDateString = startDatePicker.getEditor().getText();
          endDateString = endDatePicker.getEditor().getText();

          // room table window
          popbp1.setPadding(new Insets(15));
          Label roomOverview = new Label("Rooms available within the selected time-frame");
          roomOverview.setStyle("-fx-font-size: 45;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;");
          HBox titleBox = new HBox();
          titleBox.getChildren().add(roomOverview);
          titleBox.setAlignment(Pos.CENTER);
          titleBox.setPadding(new Insets(15, 1, 15, 1));
          popbp1.setTop(titleBox);
          setBackground(popbp1, "background2.jpg");
          HBox tableBox = new HBox();
          tableBox.getChildren().add(table);
          tableBox.setAlignment(Pos.CENTER);
          tableBox.setPadding(new Insets(10, 10, 10, 10));
          popbp1.setCenter(tableBox);
          Button back = new Button("Back");
          hoverOverAnimation(back);
          back.setStyle(buttonStyle);
          HBox hboxButton = new HBox();
          hboxButton.getChildren().addAll(back);
          hboxButton.setAlignment(Pos.CENTER);
          hboxButton.setPadding(new Insets(15, 1, 15, 1));
          popbp1.setBottom(hboxButton);
          Scene s1 = new Scene(popbp1);
          popstage.setScene(s1);
          back.setOnAction(e -> {
            popstage.setScene(s);
          });
        } catch (SQLException e1) {
          try {
            dbHandeler.addBooking2(temp);
          } catch (SQLException e) {
            e.printStackTrace();
          }
          popstage.setAlwaysOnTop(false);
          Alert invalidDates = new Alert(AlertType.INFORMATION);
          invalidDates.setHeaderText("something wrong in database.");
          invalidDates.showAndWait();
          popstage.setAlwaysOnTop(true);
          e1.printStackTrace();
        }
        try {
          dbHandeler.addBooking2(temp);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    };
    availableButton.setOnAction(availableButtonEvent);

    return s;
  }

  public class PaidButton extends Button {

    public PaidButton(String name, Booking booking, BorderPane root) {
      super(name);
      setOnAction((event) -> {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setHeaderText("Pay now?");
        confirm.show();
        if (confirm.getResult().equals(ButtonType.OK)) {
          try {
            booking.setPaid(!booking.isPaid());
          } catch (SQLException e1) {
            Alert invalidDates = new Alert(AlertType.INFORMATION);
            invalidDates.setHeaderText("something wrong in database.");
            invalidDates.showAndWait();
            e1.printStackTrace();
          }
        }
        new bookingSearch(root);
      });
    }
  }

  public class BookingTable extends Booking {
    private final SimpleIntegerProperty roomNumber;
    private final SimpleStringProperty customerName;
    private final SimpleStringProperty customerPhone;
    private SimpleDoubleProperty fixedPrice;

    public BookingTable(Booking booking) throws SQLException {
      super(booking.getBookingNumber(), booking.getRoom(), booking.getCustomer(), booking.getStartDate(),
          booking.getEndDate(), booking.isPaid());
      this.roomNumber = new SimpleIntegerProperty(booking.getRoom().getNumber());
      this.customerName = new SimpleStringProperty(booking.getCustomer().getName());
      this.customerPhone = new SimpleStringProperty(booking.getCustomer().getPhoneNo());
      this.fixedPrice = new SimpleDoubleProperty(dbHandeler.calculateTotalPriceForBooking(booking));
    }

    public Integer getRoomNumber() {
      return roomNumber.get();
    }

    public String getCustomerName() {
      return customerName.get();
    }

    public String getCustomerPhone() {
      return customerPhone.get();
    }

    public Double getFixedPrice() {
      return fixedPrice.get();
    }

  }

  public class PopRoomTable extends Room {
    private SimpleIntegerProperty roomNumber;
    private SimpleDoubleProperty roomPrice;
    private SimpleStringProperty bedString;
    private SimpleDoubleProperty roomSize;
    private SimpleStringProperty roomLocation;
    private SimpleStringProperty roomComments;
    private SimpleObjectProperty<Button> selectButton;

    public PopRoomTable(Room room, Stage popstage, Scene s, BookingTable bookingInfo, BorderPane root)
        throws SQLException {
      // super(room.getNumber(), room.getPrice(), room.getBedsString(), room.getSize(), room.getLocation(),
      //     room.getComment(), room.getButton());
      super(room.getNumber(), room.getPrice(), room.getBedsString(), room.getSize(), room.getLocation(),
      room.getComment(), room.getButton());
      bedString = new SimpleStringProperty(room.getBedsString());
      selectButton = new SimpleObjectProperty<Button>(
          new PopSelectButton("Select", room, popstage, s, bookingInfo, root));
    }

    public Integer getRoomNumber() {
      return roomNumber.get();
    }

    public Double getRoomPrice() {
      return roomPrice.get();
    }

    public String getBedString() {
      return bedString.get();
    }

    public Double getRoomSize() {
      return roomSize.get();
    }

    public String getRoomLocation() {
      return roomLocation.get();
    }

    public String getRoomComments() {
      return roomComments.get();
    }

    public Button getSelectButton() {
      return selectButton.get();
    }

  }

  public class PopSelectButton extends Button {
    private Room room;

    public PopSelectButton(String name, Room room, Stage popstage, Scene s, BookingTable bookingInfo, BorderPane root) {
      super(name);
      this.room = room;
      setOnAction((event) -> {
        selectRoomNumber = String.valueOf(room.getNumber());
        popstage.close();
        bookingsearchPopWindowChangeBegin(bookingInfo, root);
        
      });
    }

    public Room getRoom() {
      return this.room;
    }
  }
}
