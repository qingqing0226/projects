package hotelproject.windows;

import hotelproject.Administrator;
import hotelproject.ReceptionStaff;
import hotelproject.dbHandeler;

import java.sql.SQLException;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.control.Alert.AlertType;

public class createUser extends Windows {
  /* Interface for admin users to add new users with chosen access level. */
  createUser(BorderPane root) {
    super(root);

    /* Logo image + Page Title */
    ImageView logo = new ImageView(new Image("file:resources/round_logo.png", 200, 200, true, true));
    Label pageTitle = new Label("Create A User");
    pageTitle.setStyle("-fx-text-fill: Silver;-fx-font-family:'arial rounded mt bold';-fx-font-size:60;");

    VBox logoAndTitle = new VBox();
    logoAndTitle.setAlignment(Pos.CENTER);
    logoAndTitle.getChildren().addAll(logo, pageTitle);

    /* User inputs. */
    String labelStyle = "-fx-text-fill: Silver;-fx-font-family:'arial rounded mt bold';-fx-font-size:30;";
    Label row1 = new Label("User Name: ");
    row1.setStyle(labelStyle);
    Label row2 = new Label("Password: ");
    row2.setStyle(labelStyle);
    Label row3 = new Label("Acess Level: ");
    row3.setStyle(labelStyle);
    Label row4 = new Label("First Name: ");
    row4.setStyle(labelStyle);
    Label row5 = new Label("Last Name: ");
    row5.setStyle(labelStyle);

    String inputStyle = "-fx-background-radius: 10;-fx-opacity:0.7;-fx-font-size:20";
    TextField userName = new TextField();
    userName.setStyle(inputStyle);
    PasswordField password = new PasswordField();
    password.setStyle(inputStyle);
    ChoiceBox<String> accessLevel = new ChoiceBox<String>();
    accessLevel.getItems().addAll("Reception Staff", "Administrator");
    accessLevel.getSelectionModel().select(0);
    accessLevel.setPrefSize(280, 25);
    accessLevel.setStyle(inputStyle);
    TextField firstName = new TextField();
    firstName.setStyle(inputStyle);
    TextField lastName = new TextField();
    lastName.setStyle(inputStyle);

    GridPane userInput = new GridPane();
    userInput.setAlignment(Pos.CENTER);
    userInput.setVgap(10);
    userInput.setHgap(20);
    userInput.add(row1, 0, 0);
    userInput.add(userName, 1, 0);
    userInput.add(row2, 0, 1);
    userInput.add(password, 1, 1);
    userInput.add(row3, 0, 2);
    userInput.add(accessLevel, 1, 2);
    userInput.add(row4, 0, 3);
    userInput.add(firstName, 1, 3);
    userInput.add(row5, 0, 4);
    userInput.add(lastName, 1, 4);

    /* Button row. */
    Font font = Font.font("verdana", FontWeight.BOLD, 40);
    String style = "-fx-text-fill: Silver;-fx-background-color: DarkGoldenRod;-fx-background-radius:10;";

    Pane spacer = new Pane();
    spacer.setPrefSize(200, 200);

    Button backButton = new Button("Back");
    backButton.setPrefWidth(200.0);
    backButton.setFont(font);
    backButton.setStyle(style);
    backButton.setPadding(new Insets(5));
    hoverOverAnimation(backButton);
    backButton.setOnAction(e -> new userMenu(root));
    hoverOverAnimation(backButton);

    Button createButton = new Button("Create");
    createButton.setPrefWidth(200.0);
    createButton.setFont(font);
    createButton.setStyle(style);
    createButton.setPadding(new Insets(5));
    hoverOverAnimation(createButton);

    /* Input validation. */
    createButton.setOnAction(e -> {
      String name = userName.getText();
      String pass = password.getText();
      String right = accessLevel.getValue();
      String lName = lastName.getText();
      String fName = firstName.getText();


      Alert errorAlert = new Alert(AlertType.WARNING);
      errorAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
      errorAlert.setHeaderText("Invalid input!");
      Alert success = new Alert(AlertType.INFORMATION);
      success.setContentText("User added successfully!");
      Alert failure = new Alert(AlertType.WARNING);
      failure.setContentText("User with name: <" + name + ">  is already in our record.");

      String errors = "";
      // Users Created with 4 attribute.
      if (name.trim().isEmpty() || pass.trim().isEmpty()) {
        errors += "username/password cannot be empty\n";
      }
      try {
        // In case only ONE of first name and last name is filled in
        if(fName.trim().isEmpty() ^ lName.trim().isEmpty()) {
          errors += "firstname and lastname should be BOTH empty or BOTH filled in\n";
        }
        // check if username, firstname, lastname are allowed
        if(!fName.chars().allMatch(Character::isLetter) ) {
          errors += "firstname can contain only alphabet letters\n";
        } else if(!lName.chars().allMatch(Character::isLetter)) {
          errors += "lastname can contain only alphabet letters\n";
        } else if(!name.chars().allMatch(Character::isLetter)) {
          errors += "username can contain only alphabet letters";
        }

        // check if error message is empty
        if(errors.isEmpty()) {
          if (right.equals("Reception Staff")) {
            if (dbHandeler.addUserDetail(new ReceptionStaff(name, pass, fName, lName, false))) {
              success.show();
              new createUser(root);
            } else {
              failure.show();
            }
          } else {
            if (dbHandeler.addUserDetail(new Administrator(name, pass, fName, lName, false))) {
              success.show();
              new createUser(root);
            } else {
              failure.show();
            }
          }
        } else {
          errorAlert.setContentText(errors);
          errorAlert.show();
          errors = "";  // clear error messages
        }
        
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
      
    });

    HBox buttonBox = new HBox(50);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(backButton, spacer, createButton);

    root.setTop(logoAndTitle);
    root.setCenter(userInput);
    root.setBottom(buttonBox);
  }
}
