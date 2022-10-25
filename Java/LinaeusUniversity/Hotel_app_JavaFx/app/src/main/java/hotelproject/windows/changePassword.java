package hotelproject.windows;

import java.sql.SQLException;

import hotelproject.Secure;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.scene.control.Alert.AlertType;

public class changePassword extends Windows {

  changePassword(BorderPane root) {
    super(root);

    HBox title = new HBox();
    Label pageName = new Label("Type in your new password");
    pageName.setStyle("-fx-font-size: 45;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;");
    // pageName.setFont(Font.font("Fira Sans", FontWeight.EXTRA_LIGHT, 45));
    title.setPadding(new Insets(55, 5, 55, 5));
    title.getChildren().addAll(pageName);
    title.setAlignment(Pos.BOTTOM_CENTER);

    // Text
    Label internalPageText = new Label("Confirm your new password below");
    internalPageText.setStyle("-fx-font-size: 20;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;");

    // Text fields
    Label userName = new Label("User name: " + user.getUserName());
    userName.setStyle("-fx-font-size: 35;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: White;");

    PasswordField ogPassword = new PasswordField();
    ogPassword.setMinHeight(40);
    ogPassword.setPromptText("Password");
    ogPassword.setMaxWidth(580);

    PasswordField rpPassword = new PasswordField();
    rpPassword.setMinHeight(40);
    rpPassword.setPromptText("Repeat Password");
    rpPassword.setMaxWidth(580);

    Text errorLabel = new Text("Invalid user name.");
    errorLabel.setTextAlignment(TextAlignment.CENTER);
    errorLabel.setStyle("-fx-fill: Red;-fx-font: normal bold 18px 'serif' ");

    VBox vb = new VBox();
    vb.getChildren().addAll(internalPageText, userName, ogPassword, rpPassword);

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

    EventHandler<ActionEvent> backEvent = new EventHandler<ActionEvent>() {
      public void handle(ActionEvent e) {
        error = false;
        new mainMenu(root);
      }
    };
    cancelButton.setOnAction(backEvent);

    Button enterButton = new Button("Enter");
    enterButton.setStyle(buttonStyle);
    enterButton.setAlignment(Pos.CENTER);
    // enterButton.setFont(Font.font("century", 15));
    enterButton.setPadding(new Insets(5));
    enterButton.setDefaultButton(true);
    hoverOverAnimation(enterButton);

    EventHandler<ActionEvent> passwordEvent = new EventHandler<ActionEvent>() {
      public void handle(ActionEvent e) {
        String password1 = ogPassword.getText();
        String password2 = rpPassword.getText();
        if (password1.equals("")) {
          Alert alert = new Alert(AlertType.WARNING);
          alert.setTitle("Information Dialog");
          alert.setHeaderText("You need input some thing as password.");
          alert.initModality(Modality.APPLICATION_MODAL);
          alert.show();
        } else {
          if (password1.equals(password2)) {
            if (Secure.hash(password1).equals(user.getPassword())) {
              Alert alert = new Alert(AlertType.WARNING);
              alert.setTitle("Information Dialog");
              alert.setHeaderText("Same password as before.");
              alert.initModality(Modality.APPLICATION_MODAL);
              alert.show();
            } else {
              try {
                user.setPassword(password1);
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("You need to login again.");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.show();
                new login(root);
              } catch (SQLException e1) {
                e1.printStackTrace();
              }
            }
          } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Two passwords are different.");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.show();
            new changePassword(root);
          }
        }
      }
    };
    enterButton.setOnAction(passwordEvent);

    HBox hb = new HBox();
    hb.getChildren().addAll(cancelButton, spacerA, enterButton);
    hb.setAlignment(Pos.CENTER);
    hb.setPadding(new Insets(60, 1, 1, 1));

    VBox container = new VBox();
    container.getChildren().addAll(vb, hb);

    // BorderPane that contains several V and H boxes.
    root.setTop(title);
    root.setCenter(container);
  }
}
