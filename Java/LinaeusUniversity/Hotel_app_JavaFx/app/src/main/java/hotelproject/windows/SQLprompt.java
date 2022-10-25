package hotelproject.windows;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import hotelproject.dbHandeler;

import java.sql.SQLException;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.control.Alert.AlertType;


public class SQLprompt extends Windows {

  public SQLprompt(BorderPane root) {
    super(root);

    // styles
    String labelStyle = "-fx-font-size: 35;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;";

    // outerbox and title
    VBox outerBox = new VBox(30);
    outerBox.setAlignment(Pos.CENTER);
    Label title = new Label("Set Database Connection");
    title.setStyle("-fx-font-size: 45;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;");
    
    // Creating a Grid Pane
    GridPane gridPane = new GridPane();

    Label text1 = new Label("Username:");
    text1.setStyle(labelStyle);

    // creating label password
    Label text2 = new Label("Password:");
    text2.setStyle(labelStyle);

    // creating label password
    Label text3 = new Label("Port:");
    text3.setStyle(labelStyle);

    // Creating Text Filed for username
    TextField textField1 = new TextField();

    // Creating Text Filed for password
    PasswordField textField2 = new PasswordField();

    // Creating Text Filed for port
    TextField port = new TextField();

    EventHandler<ActionEvent> connectEvent = new EventHandler<ActionEvent>() {
      public void handle(ActionEvent e) {
        String userName = textField1.getText();
        String password = textField2.getText();
        String portNo = port.getText();
        // dbHandeler Initilize;
        dbHandeler dbh = new dbHandeler(userName, password, portNo);
        if(dbh.connected) {
          try {
            dbh.initilizeDatabase();
          } catch (ClassNotFoundException err) {
            err.printStackTrace();
          } catch (SQLException err) {
            err.printStackTrace();
          }
          new startPage(root);
        } else {
          Alert alert = new Alert(AlertType.INFORMATION);
          alert.setContentText("Invalid Username/Password/port, please try again");
          alert.setHeaderText("Connection failed");
          alert.show();
          root.getChildren().clear();
          new SQLprompt(root);
        }
      }
    };


    // Creating Buttons
    Button connecButton= new Button("Connect Database");
    hoverOverAnimation(connecButton);
    connecButton.setFont(Font.font("verdana", FontWeight.BOLD, 30));
    connecButton.setStyle("-fx-text-fill: Silver;-fx-background-color: DarkGoldenRod;-fx-background-radius:10;");
    connecButton.setPadding(new Insets(5));
    connecButton.setOnAction(connectEvent);

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
    gridPane.add(text3, 0, 2);
    gridPane.add(port, 1, 2);
    gridPane.add(connecButton, 1, 14);

    outerBox.getChildren().addAll(title, gridPane);
    root.setCenter(outerBox);
  }
  
}
