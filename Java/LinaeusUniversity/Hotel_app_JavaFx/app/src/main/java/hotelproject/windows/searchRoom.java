package hotelproject.windows;

import java.util.List;

import hotelproject.Booking;
import hotelproject.dbHandeler;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.util.Callback;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.skin.DatePickerSkin;

public class searchRoom extends Windows {

  searchRoom(BorderPane root) {
    super(root);

    String buttonStyle = "-fx-text-fill: Silver;-fx-background-color: DarkGoldenRod;-fx-background-radius:10;-fx-font-size:20;";
    String inputStyle = "-fx-background-radius: 10;-fx-opacity:0.7;-fx-font-size:20;";
    /* Logo image + Page Title */
    ImageView logo = new ImageView(new Image("file:resources/round_logo.png", 150, 150, true, true));
    Label pageTitle = new Label("Search available dates of rooms");
    pageTitle.setStyle("-fx-text-fill: Silver;-fx-font-family:'arial rounded mt bold';-fx-font-size:60;");
    VBox logoAndTitle = new VBox();
    logoAndTitle.setAlignment(Pos.CENTER);
    logoAndTitle.getChildren().addAll(logo, pageTitle);
    // Hbox for adding search field
    TextField searchField = new TextField();
    searchField.setStyle(inputStyle);
    searchField.setEditable(true);
    searchField.setPromptText("Enter room number");
    // Hbox for adding search button
    Button searchButton = new Button("Search");
    hoverOverAnimation(searchButton);
    searchButton.setAlignment(Pos.CENTER);
    searchButton.setStyle(buttonStyle);

    HBox searchBox = new HBox(searchField, searchButton);
    searchBox.setSpacing(20);
    searchBox.setAlignment(Pos.CENTER);
    searchBox.setPadding(new Insets(50));

    VBox mainBox = new VBox();
    mainBox.setSpacing(20);
    mainBox.setAlignment(Pos.CENTER);
    mainBox.getChildren().addAll(logo, pageTitle);
    mainBox.getChildren().addAll(searchBox);
    DatePicker datePicker = new DatePicker();
    datePicker.setValue(LocalDate.now());
    datePicker.setShowWeekNumbers(true);
    DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
    Node popupContent = datePickerSkin.getPopupContent();
    mainBox.getChildren().addAll(popupContent);
    root.setCenter(mainBox);

    // back button
    Button backButton = new Button("Back");
    backButton.setAlignment(Pos.CENTER);
    backButton.setStyle(buttonStyle);
    mainBox.getChildren().addAll(backButton);
    hoverOverAnimation(backButton);

    StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    Class<?> callerClass = walker.getCallerClass();
    backButton.setOnAction(e -> {
      if (callerClass.toString().equals("class hotelproject.windows.bookingSearch")) {
        new bookingSearch(root);
      } else {
        new addBooking(root);
      }
    });

    searchButton.setOnAction(e -> {
      try {
        Integer flag = 0;
        Callback<DatePicker, DateCell> dayCellFactory = null;
        List<Booking> c = dbHandeler.allBookings();
        // a 2d arraylist that holds a list of arraylists of LocalDate
        ArrayList<ArrayList<LocalDate>> bookedDates = new ArrayList<>();
        // an arraylist that holds a start date and an end date
        ArrayList<LocalDate> startEnd = new ArrayList<>();
        int roomNum = -1;
        if (!searchField.getText().isEmpty() && isNumeric(searchField.getText())) {
          roomNum = Integer.parseInt(searchField.getText());

          for (Booking booking : c) {

            if (booking.getRoom().getNumber() == roomNum) {
              LocalDate sDate = LocalDate.of(booking.getStartDate().getYear(), booking.getStartDate().getMonth(),
                  booking.getStartDate().getDayOfMonth());
              LocalDate eDate = LocalDate.of(booking.getEndDate().getYear(), booking.getEndDate().getMonth(),
                  booking.getEndDate().getDayOfMonth());
              startEnd.add(sDate);
              startEnd.add(eDate);
              bookedDates.add(startEnd);

              startEnd = new ArrayList<>();
              flag = 1;

            }
          }
        } else {
          Alert alert = new Alert(AlertType.INFORMATION);
          alert.setHeaderText("Please enter a number");
          alert.show();
          flag = 1;
        }
        if (flag == 0) {
          Alert alert = new Alert(AlertType.INFORMATION);
          alert.setHeaderText("Room number does not have any bookings");
          alert.show();
        }

        dayCellFactory = this.getDayCellFactory(bookedDates);
        datePicker.setDayCellFactory(dayCellFactory);
        DatePickerSkin datePickerSkin2 = new DatePickerSkin(datePicker);
        Node popupContent2 = datePickerSkin2.getPopupContent();
        mainBox.getChildren().clear();
        mainBox.getChildren().addAll(logo, pageTitle, searchBox, popupContent2, backButton);
      } catch (SQLException e1) {
        e1.printStackTrace();
      }

    });
  }

  private Callback<DatePicker, DateCell> getDayCellFactory(ArrayList<ArrayList<LocalDate>> dates) {
    final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
      @Override
      public DateCell call(final DatePicker datePicker) {
        return new DateCell() {
          @Override
          public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            for (int i = 0; i < dates.size(); i++) {
              LocalDate startDate = dates.get(i).get(0);
              LocalDate endDate = dates.get(i).get(1);
              LocalDate tempDate = startDate.plusDays(1);

              // Disable start Dates
              if (item.equals(startDate)) {
                setDisable(true);
                setStyle("-fx-background-color: #C0C0C0;");
              }
              // Diaable Dates between start Dates and end Dates
              else if (item.isAfter(startDate) && item.isBefore(endDate)) {
                // System.out.println("Temp Date: " + tempDate);
                setDisable(true);
                setStyle("-fx-background-color: #C0C0C0;");
                tempDate = tempDate.plusDays(1);
              }
              // Disable end Dates
              else if (item.equals(endDate)) {
                setDisable(true);
                setStyle("-fx-background-color: #C0C0C0;");
              }
            }
          }
        };
      }
    };
    return dayCellFactory;
  }

}
