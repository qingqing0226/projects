package hotelproject.windows;

import hotelproject.Room;
import hotelproject.dbHandeler;
import hotelproject.Room.Bed;
import hotelproject.User.AccessLevel;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert.AlertType;

public class roomMenu extends Windows {
  protected Room room;

  roomMenu(BorderPane root) {
    super(root);

    // set style for all buttons
    String styles = "-fx-font-size: 21;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;";
    // Buttons
    String buttonStyle = " -fx-font-size:30;-fx-font-family:'arial rounded mt bold';-fx-background-color: DarkGoldenRod;-fx-text-fill:Silver;-fx-background-radius:10;";
    // textfield style
    String textFieldStyle = "-fx-background-radius: 10;-fx-opacity:0.7;";
    // Search
    TextField search = new TextField();
    search.setStyle(textFieldStyle);
    search.setMinHeight(40);
    search.setPromptText("Room number");
    search.setMinWidth(580);

    Button searchButton = new Button("Search");
    hoverOverAnimation(searchButton);
    searchButton.setAlignment(Pos.CENTER);
    searchButton.setStyle(buttonStyle);

    HBox searchBox = new HBox(search, searchButton);
    searchBox.setSpacing(20);
    searchBox.setAlignment(Pos.CENTER);
    searchBox.setPadding(new Insets(50));

    // Page name + Drop down menu
    ChoiceBox<String> menuButton = new ChoiceBox<String>();
    menuButton.getSelectionModel().select(0);
    menuButton.setCenterShape(true);
    menuButton.setMinSize(100, 40);
    menuButton.setValue("Room No");
    menuButton.setStyle(textFieldStyle);
    try {
      menuButton.getItems().addAll(dbHandeler.getListofRoomNo());
    } catch (SQLException e2) {
      e2.printStackTrace();
    }
    Pane spacer = new Pane();
    spacer.setMinWidth(20);
    spacer.setPrefWidth(80);
    Label pageName = new Label("Room details");
    pageName.setStyle("-fx-text-fill: DarkGoldenRod;-fx-font-family:'arial rounded mt bold';-fx-font-size:35;");
    HBox rightBox = new HBox(pageName, menuButton);
    rightBox.setAlignment(Pos.CENTER);
    rightBox.setPadding(new Insets(1, 1, 10, 1));
    rightBox.setSpacing(10);
    HBox leftbox = new HBox(spacer);
    BorderPane nameBox = new BorderPane();
    nameBox.setLeft(leftbox);
    nameBox.setCenter(rightBox);
    VBox topMenu = new VBox(searchBox, nameBox);
    // Room details
    VBox roomTextField = new VBox(5);
    roomTextField.setAlignment(Pos.CENTER_LEFT);
    VBox roomLabelField = new VBox(5);
    roomLabelField.setAlignment(Pos.CENTER_RIGHT);
    TextField roomNumber = new TextField();
    roomNumber.setStyle(textFieldStyle);
    roomNumber.setPromptText("The number of the room");
    roomNumber.setMinHeight(40);
    roomNumber.setMaxWidth(700);
    TextField size = new TextField();
    size.setStyle(textFieldStyle);
    size.setPromptText("The size of the room in m^2");
    size.setMinHeight(40);
    size.setMaxWidth(700);
    HBox bedBox = new HBox(5);
    bedBox.setMinHeight(40);
    bedBox.setMaxWidth(700);
    ArrayList<String> currentBedList = new ArrayList<>();
    ChoiceBox<String> BedMenu = new ChoiceBox<String>();
    BedMenu.setStyle(textFieldStyle);
    BedMenu.getSelectionModel().select(0);
    BedMenu.setCenterShape(true);
    BedMenu.setMinSize(80, 40);
    BedMenu.setValue("Single");
    BedMenu.setItems(FXCollections.observableArrayList("Single", "Double", "Extra_large"));
    BedMenu.setVisible(false);
    Button addBed = new Button("Add Bed");
    addBed.setStyle(
        " -fx-font-size:20;-fx-font-family:'arial rounded mt bold';-fx-background-color: DarkGoldenRod;-fx-text-fill:Silver;-fx-background-radius:8;");
    addBed.setMinHeight(40);
    hoverOverAnimation(addBed);
    addBed.setVisible(false);
    Button removeBed = new Button("Remove bed");
    removeBed.setStyle(
        " -fx-font-size:20;-fx-font-family:'arial rounded mt bold';-fx-background-color: DarkGoldenRod;-fx-text-fill:Silver;-fx-background-radius:8;");
    removeBed.setMinHeight(40);
    hoverOverAnimation(removeBed);
    removeBed.setVisible(false);
    Label currentBed = new Label();
    currentBed.setMinHeight(40);
    currentBed.setStyle(styles);
    bedBox.getChildren().addAll(currentBed, BedMenu, addBed, removeBed);
    currentBed.setText(makeBeds(currentBedList));
    EventHandler<ActionEvent> addBedButtonEvent = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        // clear the ArrayList
        currentBedList.clear();
        // add beds from database to currentBedList
        String[] oldBeds = currentBed.getText().split(" ");
        for (String s : oldBeds) {
          currentBedList.add(s);
        }
        // add new beds to the list
        currentBedList.add(BedMenu.getValue());
        currentBed.setText(makeBeds(currentBedList));
        Alert alertAddBed = new Alert(AlertType.INFORMATION);
        alertAddBed.setHeaderText("The " + BedMenu.getValue() + " bed has been added");
        alertAddBed.show();
      }
    };
    addBed.setOnAction(addBedButtonEvent);
    EventHandler<ActionEvent> removeBedButtonEvent = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        // update currentBedList
        currentBedList.clear();
        String[] oldBeds = currentBed.getText().split(" ");
        for (String s : oldBeds) {
          currentBedList.add(s);
        }
        // remove the bed (first occurrence)
        if (currentBedList.remove(BedMenu.getValue())) {
          currentBed.setText(makeBeds(currentBedList));
          Alert alertRemoveBed = new Alert(AlertType.INFORMATION);
          alertRemoveBed.setHeaderText("The " + BedMenu.getValue() + " bed has been removed");
          alertRemoveBed.show();
        } else {
          Alert alertRemoveBed = new Alert(AlertType.INFORMATION);
          alertRemoveBed.setHeaderText("Bed not found");
          alertRemoveBed.show();
        }
      }
    };
    removeBed.setOnAction(removeBedButtonEvent);
    TextField price = new TextField();
    price.setStyle(textFieldStyle);
    price.setPromptText("The room number");
    price.setMinHeight(40);
    price.setMaxWidth(700);
    TextField location = new TextField();
    location.setStyle(textFieldStyle);
    location.setPromptText("Room floor");
    location.setMinHeight(40);
    location.setMaxWidth(700);
    TextField comments = new TextField();
    comments.setStyle(textFieldStyle);
    comments.setPromptText("Anything to note");
    comments.setMinHeight(40);
    comments.setMaxWidth(700);
    roomTextField.getChildren().addAll(roomNumber, size, bedBox, price, location, comments);
    Label roomNumberLabel = new Label("Room number:");
    roomNumberLabel.setStyle(styles);
    roomNumberLabel.setPadding(new Insets(0, 5, 5, 85));
    Label sizeLabel = new Label("Size:");
    sizeLabel.setStyle(styles);
    sizeLabel.setPadding(new Insets(9, 5, 4, 85));
    Label bedLabel = new Label("Beds:");
    bedLabel.setStyle(styles);
    bedLabel.setPadding(new Insets(9, 5, 5, 85));
    Label priceLabel = new Label("Price:");
    priceLabel.setStyle(styles);
    priceLabel.setPadding(new Insets(9, 5, 5, 85));
    Label locationLabel = new Label("Location:");
    locationLabel.setStyle(styles);
    locationLabel.setPadding(new Insets(9, 5, 5, 85));
    Label commentLabel = new Label("Comments:");
    commentLabel.setStyle(styles);
    commentLabel.setPadding(new Insets(9, 5, 0, 85));
    roomLabelField.getChildren().addAll(roomNumberLabel, sizeLabel, bedLabel, priceLabel, locationLabel, commentLabel);
    Button backButton = new Button("Back");
    hoverOverAnimation(backButton);
    backButton.setAlignment(Pos.CENTER);
    backButton.setStyle(buttonStyle);
    EventHandler<ActionEvent> backMainMenuEvent = new EventHandler<ActionEvent>() {
      public void handle(ActionEvent e) {
        new mainMenu(root);
      }
    };
    backButton.setOnAction(backMainMenuEvent);
    Button adminOptionButton = new Button("Administrator Options");
    hoverOverAnimation(adminOptionButton);
    adminOptionButton.setAlignment(Pos.CENTER);
    adminOptionButton.setStyle(buttonStyle);
    HBox visibleButtonRow = new HBox(50);
    if (user.getAccessLevel().equals(AccessLevel.Admin)) {
      visibleButtonRow.getChildren().addAll(backButton, adminOptionButton);
    } else {
      visibleButtonRow.getChildren().addAll(backButton);
    }
    visibleButtonRow.setAlignment(Pos.TOP_CENTER);
    visibleButtonRow.setPadding(new Insets(0, 0, 50, 0));
    Button addButton = new Button("Add");
    hoverOverAnimation(addButton);
    EventHandler<ActionEvent> addRoom = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        double defaultPrice = 0;
        double defaultsize = 0;
        String defaultLocation = "";
        String defaultComments = "";
        String warning = "";
        try {
          if (isInteger(roomNumber.getText())) {
            if ((!price.getText().isEmpty()) && isNumeric(price.getText())) {
              defaultPrice = Double.parseDouble(price.getText());
            } else if ((!price.getText().isEmpty()) && !isNumeric(price.getText())) {
              warning += " Invalid input for price\n";
            }
            if ((!size.getText().isEmpty()) && isNumeric(size.getText())) {
              defaultsize = Double.parseDouble(size.getText());
            } else if ((!size.getText().isEmpty()) && !isNumeric(size.getText())) {
              warning += " Invalid input for size";
            }
            if (!location.getText().isEmpty()) {
              defaultLocation = location.getText();
            }
            if (!comments.getText().isEmpty()) {
              defaultComments = comments.getText();
            }
            if (warning.isEmpty()) {
              room = new Room(Integer.parseInt(roomNumber.getText()), defaultPrice, defaultsize, defaultLocation,
                  defaultComments);
              room.multipleBeds(currentBed.getText());
              boolean add = dbHandeler.addRoom(room);
              if (add) {
                Alert addRoom = new Alert(AlertType.INFORMATION);
                addRoom.setHeaderText("The room has been added");
                addRoom.show();
                new roomMenu(root);
              } else {
                Alert addRoom = new Alert(AlertType.INFORMATION);
                addRoom.setHeaderText("The room was not added because the room number is occupied by other room");
                addRoom.show();
              }
            } else {
              Alert invalidPriceSize = new Alert(AlertType.INFORMATION);
              invalidPriceSize.setContentText(warning);
              invalidPriceSize.setHeaderText("Invalid input");
              invalidPriceSize.show();
            }
          } else {
            Alert invalidInput = new Alert(AlertType.INFORMATION);
            invalidInput.setHeaderText("The room was not added because room number is invalid");
            invalidInput.show();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    };
    addButton.setOnAction(addRoom);
    addButton.setAlignment(Pos.CENTER);
    addButton.setStyle(buttonStyle);
    addButton.setVisible(false);
    Button removeButton = new Button("Remove");
    hoverOverAnimation(removeButton);
    EventHandler<ActionEvent> deleteroom = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          if (dbHandeler.getRoomDetail(Integer.parseInt(roomNumber.getText())) != null) {
            if (dbHandeler.checkRemovableRoom(Integer.parseInt(roomNumber.getText()))) {
              dbHandeler.removeRoom(Integer.parseInt(roomNumber.getText()));
              Alert deleteroom = new Alert(AlertType.INFORMATION);
              deleteroom.setHeaderText("The room has been deleted");
              deleteroom.show();
              new roomMenu(root);
            } else {
              Alert roomUsed = new Alert(AlertType.INFORMATION);
              roomUsed.setHeaderText("The room is booked, there must be no bookings for the room to be removable!");
              roomUsed.show();
            }
          } else {
            Alert roomNotExist = new Alert(AlertType.INFORMATION);
            roomNotExist.setHeaderText("Deletion failed");
            roomNotExist.setContentText("The room does not exist.");
            roomNotExist.show();
          }
        } catch (NumberFormatException | SQLException e) {
          Alert deleteRoomFail = new Alert(AlertType.INFORMATION);
          deleteRoomFail.setHeaderText("The room was not deleted, an error occurred");
          deleteRoomFail.show();
        }
      }
    };
    removeButton.setOnAction(deleteroom);
    removeButton.setAlignment(Pos.CENTER);
    removeButton.setStyle(buttonStyle);
    removeButton.setVisible(false);
    Button editRoom = new Button("Edit Room");
    hoverOverAnimation(editRoom);
    EventHandler<ActionEvent> editRoomEvent = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        double defaultPrice = 0;
        double defaultsize = 0;
        String defaultLocation = "";
        String defaultComments = "";
        String warning = "";
        try {
          if (isInteger(roomNumber.getText())) {
            if ((!price.getText().isEmpty()) && isNumeric(price.getText())) {
              defaultPrice = Double.parseDouble(price.getText());
            } else if ((!price.getText().isEmpty()) && !isNumeric(price.getText())) {
              warning += " Invalid input for price\n";
            }
            if ((!size.getText().isEmpty()) && isNumeric(size.getText())) {
              defaultsize = Double.parseDouble(size.getText());
            } else if ((!size.getText().isEmpty()) && !isNumeric(size.getText())) {
              warning += " Invalid input for size";
            }
            if (!location.getText().isEmpty()) {
              defaultLocation = location.getText();
            }
            if (!comments.getText().isEmpty()) {
              defaultComments = comments.getText();
            }
            if (warning.isEmpty()) {
              Room room = new Room(Integer.parseInt(roomNumber.getText()), defaultPrice, defaultsize, defaultLocation,
                  defaultComments);
              room.multipleBeds(currentBed.getText());
              if (dbHandeler.editRoomDetail(room)) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setHeaderText("Successful edit");
                alert.show();
              } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setHeaderText("Room not editable");
                alert.setContentText("The room does not exist.");
                alert.show();
              }
            } else {
              Alert invalidPriceSize = new Alert(AlertType.INFORMATION);
              invalidPriceSize.setContentText(warning);
              invalidPriceSize.setHeaderText("Invalid input");
              invalidPriceSize.show();
            }
          }
        } catch (NumberFormatException e) {
          e.printStackTrace();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    };
    editRoom.setOnAction(editRoomEvent);
    editRoom.setAlignment(Pos.CENTER);
    editRoom.setStyle(buttonStyle);
    editRoom.setVisible(false);
    EventHandler<ActionEvent> roomSearchEvent = new EventHandler<ActionEvent>() {
      public void handle(ActionEvent e) {
        if (isNumeric(search.getText())) {
          int num = Integer.parseInt(search.getText());
          try {
            room = dbHandeler.getRoomDetail(num);
            if (room != null) {
              size.setText(Double.toString(room.getSize()));
              String beds = "";
              for (Bed b : room.getBeds()) {
                if (b == Bed.SINGLE) {
                  beds += "Single ";
                } else if (b == Bed.DOUBLE) {
                  beds += "Double ";
                } else if (b == Bed.EXTRA_LARGE) {
                  beds += "Extra_large ";
                } else {
                  beds += "";
                }
              }
              roomNumber.setText(Integer.toString(room.getNumber()));
              currentBed.setText(beds);
              price.setText(Double.toString(room.getPrice()));
              location.setText(room.getLocation());
              comments.setText(room.getComment());
            } else {
              Alert alert = new Alert(AlertType.INFORMATION);
              alert.setHeaderText("Room not found");
              alert.setContentText("The room does not exist");
              alert.show();
            }
          } catch (SQLException e1) {
            e1.printStackTrace();
          }
        } else {
          Alert alert = new Alert(AlertType.INFORMATION);
          alert.setHeaderText("Invalid text entered");
          alert.show();
        }
      }
    };
    searchButton.setOnAction(roomSearchEvent);
    EventHandler<ActionEvent> comboSelectEvent = new EventHandler<ActionEvent>() {
      public void handle(ActionEvent e) {
        int num = Integer.parseInt(menuButton.getValue().replace("Room No: ", ""));
        try {
          room = dbHandeler.getRoomDetail(num);
          if (room != null) {
            size.setText(Double.toString(room.getSize()));
            String beds = "";
            for (Bed b : room.getBeds()) {
              if (b == Bed.SINGLE) {
                beds += "Single ";
              } else if (b == Bed.DOUBLE) {
                beds += "Double ";
              } else if (b == Bed.EXTRA_LARGE) {
                beds += "Extra_large ";
              } else {
                beds += "";
              }
            }
            roomNumber.setText(Integer.toString(room.getNumber()));
            currentBed.setText(beds);
            price.setText(Double.toString(room.getPrice()));
            location.setText(room.getLocation());
            comments.setText(room.getComment());
          } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText("Room not found");
            alert.setContentText("The room does not exist");
            alert.show();
          }
        } catch (SQLException e1) {
          e1.printStackTrace();
        }
      }
    };
    menuButton.setOnAction(comboSelectEvent);
    HBox adminButtonRow = new HBox(50);
    adminButtonRow.getChildren().addAll(addButton, removeButton, editRoom);
    adminButtonRow.setAlignment(Pos.TOP_CENTER);
    adminButtonRow.setPadding(new Insets(0, 0, 50, 0));
    // Some buttons are not visible, unless admin user clicks on certian button.
    adminOptionButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        addButton.setVisible(true);
        removeButton.setVisible(true);
        editRoom.setVisible(true);
        addBed.setVisible(true);
        removeBed.setVisible(true);
        BedMenu.setVisible(true);
      }
    });
    VBox buttons = new VBox();
    buttons.getChildren().addAll(visibleButtonRow, adminButtonRow);
    // Result
    root.setTop(topMenu);
    root.setLeft(roomLabelField);
    root.setCenter(roomTextField);
    root.setBottom(buttons);
  }

  /**
   * Page that is only accessed by Administrators Allow the priviliged user to
   * edit room details.
   * 
   * @param root
   * 
   */
  private String makeBeds(ArrayList<String> a) {

    String beds = "";

    for (String i : a) {
      beds += (i + " ");
    }
    return beds;
  }
}
