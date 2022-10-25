package hotelproject.windows;

import hotelproject.Secure;
import hotelproject.dbHandeler;

import java.sql.SQLException;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class login extends Windows {

  login(BorderPane root) {
    super(root);

    // Creating a Grid Pane
    GridPane gridPane = new GridPane();
    // VBox gridPane = new VBox();
    // creating label email
    Label text1 = new Label("Username:");
    text1.setStyle("-fx-font-size: 35;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;");

    // creating label password
    Label text2 = new Label("Password:");
    text2.setStyle("-fx-font-size: 35;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;");

    // Creating Text Filed for email
    TextField textField1 = new TextField();

    // Creating Text Filed for password
    PasswordField textField2 = new PasswordField();

    EventHandler<ActionEvent> loginEvent = new EventHandler<ActionEvent>() {
      public void handle(ActionEvent e) {
        String userName = textField1.getText();
        String password = textField2.getText();
        try {
          user = dbHandeler.getUserDetail(userName);
        } catch (SQLException e1) {
          e1.printStackTrace();
        }
        if (user.getPassword().equals(Secure.hash(password))) {
          error = false;
          new mainMenu(root);
        } else {
          error = true;
          Alert alert = new Alert(AlertType.INFORMATION);
          alert.setContentText("Invalid Username or Password, please try again");
          alert.setHeaderText("Invalid input");
          alert.show();
          root.getChildren().clear();
          new login(root);
        }
      }
    };


    // Creating Buttons
    Button button1 = new Button("Enter");
    hoverOverAnimation(button1);
    // button1.setAlignment(Pos.CENTER);
    button1.setPrefWidth(200.0);
    button1.setFont(Font.font("verdana", FontWeight.BOLD, 40));
    button1.setStyle("-fx-text-fill: Silver;-fx-background-color: DarkGoldenRod;-fx-background-radius:10;");
    button1.setPadding(new Insets(5));
    button1.setOnAction(loginEvent);

    // Setting size for the pane
    gridPane.setMinSize(800, 400);

    // Setting the padding
    gridPane.setPadding(new Insets(10, 10, 10, 10));

    // Setting the vertical and horizontal gaps between the columns
    gridPane.setVgap(5);
    gridPane.setHgap(5);

    // Setting the Grid alignment
    gridPane.setAlignment(Pos.CENTER);

    // Arranging all the nodes in the grid
    gridPane.add(text1, 0, 0);
    gridPane.add(textField1, 1, 0);
    gridPane.add(text2, 0, 1);
    gridPane.add(textField2, 1, 1);
    gridPane.add(button1, 1, 14);

    root.setCenter(gridPane);
  }
}

