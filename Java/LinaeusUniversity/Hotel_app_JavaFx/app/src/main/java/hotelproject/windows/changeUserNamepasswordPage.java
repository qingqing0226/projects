package hotelproject.windows;

import hotelproject.Secure;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Modality;

public class changeUserNamepasswordPage extends Windows {

  changeUserNamepasswordPage(BorderPane root) {
    super(root);

    HBox title = new HBox();
    Label pageName = new Label("Verify username and password");
    pageName.setStyle("-fx-font-size: 45;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;");
    // pageName.setFont(Font.font("Fira Sans", FontWeight.EXTRA_LIGHT, 45));
    title.setPadding(new Insets(55, 5, 55, 5));
    title.getChildren().addAll(pageName);
    title.setAlignment(Pos.BOTTOM_CENTER);

    // Text
    // HBox internalText = new HBox();
    Label internalPageText = new Label("Select Password to change password or User Name to change user name");
    internalPageText.setStyle("-fx-font-size: 20;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;");
    // internalPageText.setFont(Font.font("Fira Sans", FontWeight.EXTRA_LIGHT, 20));

    // Text fields
    TextField userName = new TextField();
    userName.setMinHeight(40);
    userName.setPromptText("Username");
    userName.setMaxWidth(580);

    PasswordField ogPassword = new PasswordField();
    ogPassword.setMinHeight(40);
    ogPassword.setPromptText("Password");
    ogPassword.setMaxWidth(580);

    Text errorLabel = new Text("Invalid user name or password.");
    errorLabel.setTextAlignment(TextAlignment.CENTER);
    errorLabel.setStyle("-fx-fill: yellow;-fx-font-family:'arial rounded mt bold';-fx-font-size:35 ");

    VBox vb = new VBox();
    vb.getChildren().addAll(internalPageText, userName, ogPassword);

    vb.setSpacing(25);
    vb.setPadding(new Insets(15, 5, 5, 5));
    vb.setAlignment(Pos.CENTER);

    // Buttons
    String buttonStyle = " -fx-font-size:30; " + "-fx-font-family:'arial rounded mt bold';"
        + "-fx-background-color: DarkGoldenRod;" + "-fx-text-fill:Silver;" + "-fx-background-radius:10;";
    Pane spacerA = new Pane();
    spacerA.setMinSize(10, 20);
    spacerA.setPrefSize(80, 40);

    Pane spacerB = new Pane();
    spacerB.setMinSize(10, 20);
    spacerB.setPrefSize(80, 40);

    Button cancelButton = new Button("Back");
    cancelButton.setStyle(buttonStyle);
    cancelButton.setAlignment(Pos.CENTER);
    // cancelButton.setFont(Font.font("century", 15));
    cancelButton.setPadding(new Insets(5));
    hoverOverAnimation(cancelButton);

    cancelButton.setOnAction(e -> {new mainMenu(root);});

    Button passwordButton = new Button("Password");
    passwordButton.setStyle(buttonStyle);
    passwordButton.setAlignment(Pos.CENTER);
    // enterButton.setFont(Font.font("century", 15));
    passwordButton.setPadding(new Insets(5));
    passwordButton.setDefaultButton(true);
    hoverOverAnimation(passwordButton);

    passwordButton.setOnAction(e -> {
      String userName1 = userName.getText();
      String password = ogPassword.getText();
      if (user.getPassword().equals(Secure.hash(password)) && user.getUserName().equals(userName1)) {
        new changePassword(root);
      } else {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Wrong password or user name.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.show();
        new changeUserNamepasswordPage(root);
      }
    });

    Button userNameButton = new Button("User Name");
    userNameButton.setStyle(buttonStyle);
    userNameButton.setAlignment(Pos.CENTER);
    // enterButton.setFont(Font.font("century", 15));
    userNameButton.setPadding(new Insets(5));
    userNameButton.setDefaultButton(true);
    hoverOverAnimation(userNameButton);

    userNameButton.setOnAction(e -> {
      String userName1 = userName.getText();
      String password = ogPassword.getText();
      if (user.getPassword().equals(Secure.hash(password)) && user.getUserName().equals(userName1)) {
        new changeUserName(root);
      } else {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Wrong password or user name.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.show();
        new changeUserNamepasswordPage(root);
      }
    });

    HBox hb = new HBox();
    hb.getChildren().addAll(cancelButton, spacerA, userNameButton, spacerB, passwordButton);
    hb.setAlignment(Pos.CENTER);
    hb.setPadding(new Insets(60, 1, 1, 1));

    VBox container = new VBox();
    container.getChildren().addAll(vb, hb);

    // BorderPane that contains several V and H boxes.
    root.setTop(title);
    root.setCenter(container);
  }
}
