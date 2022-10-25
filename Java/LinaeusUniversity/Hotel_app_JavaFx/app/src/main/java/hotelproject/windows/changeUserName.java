package hotelproject.windows;

import java.sql.SQLException;

import hotelproject.User;
import hotelproject.dbHandeler;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.scene.control.Alert.AlertType;

public class changeUserName extends Windows {

  changeUserName(BorderPane root) {
    super(root);

    HBox title = new HBox();
    Label pageName = new Label("Type in your new user name");
    pageName.setStyle("-fx-font-size: 45;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;");
    // pageName.setFont(Font.font("Fira Sans", FontWeight.EXTRA_LIGHT, 45));
    title.setPadding(new Insets(55, 5, 55, 5));
    title.getChildren().addAll(pageName);
    title.setAlignment(Pos.BOTTOM_CENTER);

    // Text
    // HBox internalText = new HBox();
    Label internalPageText = new Label("Type your new user name below");
    internalPageText.setStyle("-fx-font-size: 20;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;");
    // internalPageText.setFont(Font.font("Fira Sans", FontWeight.EXTRA_LIGHT, 20));

    // Text fields
    Label oldUserName = new Label("Old user name: " + user.getUserName());
    oldUserName.setStyle("-fx-font-size: 35;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: White;");

    TextField userName = new TextField();
    userName.setMinHeight(40);
    userName.setPromptText("New user name");
    userName.setMaxWidth(580);

    VBox vb = new VBox();
    vb.getChildren().addAll(internalPageText, oldUserName, userName);
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

    EventHandler<ActionEvent> userNameEvent = new EventHandler<ActionEvent>() {
      public void handle(ActionEvent e) {
        String userName1 = userName.getText();
        try {
          for (User oneUser: dbHandeler.getAllUser()) {
            if (oneUser.getUserName().equals(userName1)) {
              userName1 = user.getUserName();
            }
          }
        } catch (SQLException e2) {
          e2.printStackTrace();
        }
        if (userName1.equals("")) {
          Alert alert = new Alert(AlertType.WARNING);
          alert.setTitle("Information Dialog");
          alert.setHeaderText("You need input some thing as user name.");
          alert.initModality(Modality.APPLICATION_MODAL);
          alert.show();
        } else if (userName1.equals(user.getUserName())) {
          Alert alert = new Alert(AlertType.WARNING);
          alert.setTitle("Information Dialog");
          alert.setHeaderText("Duplication of name or same name as before.");
          alert.initModality(Modality.APPLICATION_MODAL);
          alert.show();
        } else {
          try {
            user.setUserName(userName1);
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
      }
    };
    enterButton.setOnAction(userNameEvent);

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
