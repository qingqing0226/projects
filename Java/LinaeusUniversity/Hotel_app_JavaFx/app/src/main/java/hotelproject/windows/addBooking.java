package hotelproject.windows;

import hotelproject.Booking;
import hotelproject.Customer;
import hotelproject.Date;
import hotelproject.Room;
import hotelproject.dbHandeler;

import java.sql.SQLException;
import java.util.List;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.text.Font;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class addBooking extends Windows {

  addBooking(BorderPane root) {
    super(root);

    /* logo image */
    HBox imgBox = new HBox();
    imgBox.setAlignment(Pos.CENTER_RIGHT);
    imgBox.setPadding(new Insets(30, 30, 1, 1));
    ImageView iv = new ImageView(new Image("file:resources/round_logo.png", 130, 130, true, true));
    imgBox.getChildren().add(iv);
    root.setTop(imgBox);

    /* the style for all buttons */
    String buttonStyle = "-fx-font-size:30;-fx-font-family:'arial rounded mt bold';-fx-background-color: DarkGoldenRod;-fx-text-fill:Silver;-fx-background-radius:10;";

    /* outerBox contains: leftBox, rightBox */
    GridPane outerBox = new GridPane();
    outerBox.setAlignment(Pos.CENTER);
    outerBox.setPadding(new Insets(20, 1, 1, 1));
    outerBox.setStyle("-fx-font-size: 30;-fx-font-family: 'arial rounded mt bold';");

    /* leftBox items: */
    String textColor = "-fx-text-fill: silver;";
    Label enterRoom = new Label("Enter room number: ");
    enterRoom.setStyle(textColor);
    outerBox.add(enterRoom, 0, 2);
    Label startDate = new Label("Choose start date: ");
    startDate.setStyle(textColor);
    outerBox.add(startDate, 0, 0);
    Label endDate = new Label("Choose end date: ");
    endDate.setStyle(textColor);
    outerBox.add(endDate, 0, 1);
    Label phoneNumber = new Label("Phone number: ");
    phoneNumber.setStyle(textColor);
    outerBox.add(phoneNumber, 0, 4);

    /* rightBox items: */
    String inputStyle = "-fx-background-radius: 10;-fx-opacity:0.7;";

    TextField roomInput = new TextField();
    roomInput.setStyle(inputStyle);
    roomInput.setAlignment(Pos.CENTER);
    roomInput.setPromptText("i.e. 1");
    outerBox.add(roomInput, 1, 2);

    Button availableButton = new Button("Available rooms");
    hoverOverAnimation(availableButton);
    availableButton.setStyle(buttonStyle);
    outerBox.add(availableButton, 2, 2);

    Button searchARoom = new Button("Room availability calendar ");
    searchARoom.setStyle(buttonStyle);
    hoverOverAnimation(searchARoom);
    searchARoom.setOnAction(e -> new searchRoom(root));
    hoverOverAnimation(searchARoom);
    outerBox.add(searchARoom, 2, 3);

    DatePicker startDatePicker = new DatePicker();
    startDatePicker.setStyle(inputStyle);
    outerBox.add(startDatePicker, 1, 0);
    DatePicker endDatePicker = new DatePicker();
    endDatePicker.setStyle(inputStyle);
    outerBox.add(endDatePicker, 1, 1);

    TextField phoneInput = new TextField();
    phoneInput.setStyle(inputStyle);
    phoneInput.setAlignment(Pos.CENTER);
    phoneInput.setPromptText("i.e. 0763204621");
    outerBox.add(phoneInput, 1, 4);

    Button createCustomer = new Button("Add customer");
    hoverOverAnimation(createCustomer);
    createCustomer.setStyle(buttonStyle);

    Button verifyPhone = new Button("Verify");
    hoverOverAnimation(verifyPhone);
    verifyPhone.setStyle(buttonStyle);

    HBox customerBox = new HBox(10);
    customerBox.getChildren().addAll(createCustomer, verifyPhone);
    outerBox.add(customerBox, 2, 4);

    outerBox.setHgap(10);
    outerBox.setVgap(10);

    root.setCenter(outerBox);

    EventHandler<ActionEvent> createUser = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        new addCustomer(root);
      }
    };

    createCustomer.setOnAction(createUser);
    /*
     * check if the temparary strings are empty, if not, take their values to fill
     * in the textfields
     */
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
      ObservableList<RoomTable> roomTable = FXCollections.observableArrayList();

      @Override
      public void handle(ActionEvent event) {
        List<Room> roomList;
        try {
          if (!startDatePicker.getEditor().getText().isEmpty() && !endDatePicker.getEditor().getText().isEmpty()) {
            if (startDatePicker.getEditor().getText().contains("/")
                && endDatePicker.getEditor().getText().contains("/")) {
              roomList = dbHandeler.getRooms(startDatePicker.getEditor().getText(),
                  endDatePicker.getEditor().getText());
              for (Room r : roomList) {
                roomTable.add(new RoomTable(r, root));
              }
              TableView<RoomTable> table = new TableView<RoomTable>();

              final Label label = new Label("Available room details");
              label.setFont(new Font("Arial", 20));

              table.setEditable(false);

              TableColumn<RoomTable, Integer> rNumber = new TableColumn<>("Room number");
              TableColumn<RoomTable, Double> rPrice = new TableColumn<>("Price");
              TableColumn<RoomTable, String> rBeds = new TableColumn<>("Beds");
              TableColumn<RoomTable, Double> rSize = new TableColumn<>("Size");
              TableColumn<RoomTable, String> rLocation = new TableColumn<>("Location");
              TableColumn<RoomTable, String> rComments = new TableColumn<>("Comments");
              TableColumn<RoomTable, SelectButton> rSelect = new TableColumn<>("Select");
              rNumber.setCellValueFactory(new PropertyValueFactory<RoomTable, Integer>("number"));
              rNumber.setStyle("-fx-alignment: CENTER;");
              rPrice.setCellValueFactory(new PropertyValueFactory<RoomTable, Double>("price"));
              rPrice.setStyle("-fx-alignment: CENTER;");
              rBeds.setCellValueFactory(new PropertyValueFactory<RoomTable, String>("bedsString"));
              rBeds.setStyle("-fx-alignment: CENTER;");
              rSize.setCellValueFactory(new PropertyValueFactory<RoomTable, Double>("size"));
              rSize.setStyle("-fx-alignment: CENTER;");
              rLocation.setCellValueFactory(new PropertyValueFactory<RoomTable, String>("location"));
              rLocation.setStyle("-fx-alignment: CENTER;");
              rComments.setCellValueFactory(new PropertyValueFactory<RoomTable, String>("comment"));
              rComments.setStyle("-fx-alignment: CENTER;");
              rSelect.setCellValueFactory(new PropertyValueFactory<RoomTable, SelectButton>("SelectButton"));
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
              root.getChildren().clear();
              Label roomOverview = new Label("Rooms available within the selected time-frame");
              roomOverview
                  .setStyle("-fx-font-size: 45;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;");
              HBox titleBox = new HBox();
              titleBox.getChildren().add(roomOverview);
              titleBox.setAlignment(Pos.CENTER);
              titleBox.setPadding(new Insets(15, 1, 15, 1));
              root.setTop(titleBox);
              setBackground(root, "background2.jpg");
              HBox tableBox = new HBox();
              tableBox.getChildren().add(table);
              tableBox.setAlignment(Pos.CENTER);
              tableBox.setPadding(new Insets(10, 10, 10, 10));
              root.setCenter(tableBox);
              Button back = new Button("Back");
              hoverOverAnimation(back);
              back.setStyle(buttonStyle);
              HBox hboxButton = new HBox();
              hboxButton.getChildren().add(back);
              hboxButton.setAlignment(Pos.CENTER);
              hboxButton.setPadding(new Insets(15, 1, 15, 1));
              root.setBottom(hboxButton);
              back.setOnAction(e -> {
                new addBooking(root);
              });
            } else {
              Alert alert = new Alert(AlertType.INFORMATION);
              alert.setHeaderText("Invalid date format");
              alert.show();
            }
          } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText("Invalid date");
            alert.setContentText("The force is not with this one!");
            alert.show();
          }

        } catch (SQLException e1) {
          e1.printStackTrace();
        }
      }
    };
    availableButton.setOnAction(availableButtonEvent);
    /* cancelDone contains: cancel, done */
    HBox cancelDone = new HBox(480);
    cancelDone.setAlignment(Pos.CENTER);
    cancelDone.setPadding(new Insets(1, 1, 30, 1));
    Button cancel = new Button("Back");
    cancel.setStyle(buttonStyle);
    hoverOverAnimation(cancel);

    Button done = new Button("Done");
    done.setStyle(buttonStyle);
    hoverOverAnimation(done);
    cancelDone.getChildren().addAll(cancel, done);

    cancel.setOnAction(e -> {
      // clear strings
      startDateString = "";
      endDateString = "";
      selectRoomNumber = "";
      new bookingSearch(root);
    });

    /* verifyPhone button */
    EventHandler<ActionEvent> verifyPhoneButtonEvent = new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        try {
          if (dbHandeler.getCustomerDetail(phoneInput.getText()) != null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText("Customer exists");
            alert.show();
          } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText("Customer does not exist");
            alert.show();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }

    };
    verifyPhone.setOnAction(verifyPhoneButtonEvent);

    root.setBottom(cancelDone);

    EventHandler<ActionEvent> doneButtonEvent = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        // clear strings
        startDateString = "";
        endDateString = "";
        selectRoomNumber = "";
        Room room = null;
        boolean roomInputValid = false;
        boolean starDateValid = false;
        boolean endDateValid = false;
        boolean phoneNumberValid = false;
        try {
          if (!roomInput.getText().isEmpty()) {
            if (isNumeric(roomInput.getText())) {
              room = dbHandeler.getRoomDetail(Integer.parseInt(roomInput.getText()));
              roomInputValid = true;
            }
          }
        } catch (NumberFormatException | SQLException e1) {
          e1.printStackTrace();
        }
        Customer customer = null;
        try {
          customer = dbHandeler.getCustomerDetail(phoneInput.getText());
        } catch (SQLException e1) {
          e1.printStackTrace();
        }
        Booking booking = null;
        if (startDatePicker.getEditor().getText().contains("/") && endDatePicker.getEditor().getText().contains("/")) {
          String[] sDate = startDatePicker.getEditor().getText().split("/");
          String[] eDate = endDatePicker.getEditor().getText().split("/");
          Date start = new Date(Integer.parseInt(sDate[2]), Integer.parseInt(sDate[0]), Integer.parseInt(sDate[1]));
          Date end = new Date(Integer.parseInt(eDate[2]), Integer.parseInt(eDate[0]), Integer.parseInt(eDate[1]));
          booking = new Booking(room, customer, start, end, false);
          starDateValid = true;
          endDateValid = true;
        }
        if (!phoneInput.getText().isEmpty()) {
          if (isNumeric(phoneInput.getText())) {
            phoneNumberValid = true;
          }
        }
        try {
          if (starDateValid == true && endDateValid == true && roomInputValid == true && phoneNumberValid == true) {
            int bookingPossibility = dbHandeler.bookingPossible(Integer.parseInt(roomInput.getText()),
                startDatePicker.getEditor().getText(), endDatePicker.getEditor().getText());
            if (bookingPossibility == 1) {
              if (dbHandeler.addBooking(booking)) {
                Alert booked = new Alert(AlertType.INFORMATION);
                booked.setHeaderText("The booking has been added");
                booked.show();
                new addBooking(root);
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
          } else {
            Alert wrongType = new Alert(AlertType.INFORMATION);
            wrongType.setHeaderText("Check that everything is written in the correct format");
            wrongType.show();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }

      }

    };
    done.setOnAction(doneButtonEvent);
  }

  public class RoomTable extends Room {
    private SimpleIntegerProperty roomNumber;
    private SimpleDoubleProperty roomPrice;
    private SimpleStringProperty bedString;
    private SimpleDoubleProperty roomSize;
    private SimpleStringProperty roomLocation;
    private SimpleStringProperty roomComments;
    private SimpleObjectProperty<Button> selectButton;

    public RoomTable(Room room, BorderPane root) throws SQLException {
      // super(room.getNumber(), room.getPrice(), room.getBedsString(),
      // room.getSize(), room.getLocation(),
      // room.getComment(), room.getButton());
      // super(room.getNumber(), room.getPrice(), room.getSize(), room.getLocation(), room.getComment());
      super(room.getNumber(), room.getPrice(), room.getBedsString(), room.getSize(), room.getLocation(),
          room.getComment(), room.getButton());

      bedString = new SimpleStringProperty(room.getBedsString());
      selectButton = new SimpleObjectProperty<Button>(new SelectButton("Select", room, root));
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

  public class SelectButton extends Button {
    private Room room;

    public SelectButton(String name, Room room, BorderPane root) {
      super(name);
      this.room = room;
      setOnAction((event) -> {
        if (this.getText().equals("Select")) {
          this.setText("Selected");
          selectRoomNumber = String.valueOf(room.getNumber());
          new addBooking(root);
        } else {
          this.setText("Selected");
          selectRoomNumber = "";
        }
      });
    }

    public Room getRoom() {
      return this.room;
    }
  }
}
