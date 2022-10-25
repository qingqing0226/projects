package hotelproject.windows;

import hotelproject.User;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class mainMenu extends Windows {

  mainMenu(BorderPane root) {
    super(root);

    /* helloBox contains: hello, iv */
    HBox helloBox = new HBox(800);
    helloBox.setAlignment(Pos.CENTER_LEFT);
    helloBox.setPadding(new Insets(30, 10, 10, 100));
    /* logo image */
    ImageView iv = new ImageView(new Image("file:resources/round_logo.png", 150, 150, true, true));
    // comment out this block for testing, will recover it soon

    String userName = "";
    if (user.getFirstName().equals("NULL")) {
      userName = user.getUserName();
    } else {
      userName = user.getFirstName();
    }

    Label hello = new Label("Hello " + userName);
    hello.setStyle("-fx-font-size: 35;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: DarkGoldenRod;");
    helloBox.getChildren().addAll(hello, iv);

    VBox menuBox = new VBox(10);
    menuBox.setAlignment(Pos.CENTER_LEFT);
    menuBox.setPadding(new Insets(10, 10, 10, 300));
    String buttonStyle = "-fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;-fx-font-size: 35;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;";

    if (user.getAccessLevel().equals(User.AccessLevel.Admin)) {
      Button userPriorities = new Button("User menu");
      userPriorities.setStyle(buttonStyle);
      hoverOverAnimation(userPriorities);
      userPriorities.setOnAction(e -> new userMenu(root));

      menuBox.getChildren().add(userPriorities);
    }

    Button roomMenu = new Button("Room menu");
    roomMenu.setStyle(buttonStyle);
    hoverOverAnimation(roomMenu);
    roomMenu.setOnAction(e -> new roomMenu(root));

    Button bookings = new Button("Bookings");
    bookings.setStyle(buttonStyle);
    hoverOverAnimation(bookings);
    bookings.setOnAction(e -> new bookingSearch(root));

    Button customerMenu = new Button("Add/Edit Customer");
    customerMenu.setStyle(buttonStyle);
    hoverOverAnimation(customerMenu);
    customerMenu.setOnAction(e -> new addCustomer(root));

    Button changeLogin = new Button("Change username/password");
    changeLogin.setStyle(buttonStyle);
    hoverOverAnimation(changeLogin);
    changeLogin.setOnAction(e -> new changeUserNamepasswordPage(root));

    menuBox.getChildren().addAll(roomMenu, bookings, customerMenu, changeLogin);

    HBox logoutBox = new HBox(10);
    logoutBox.setAlignment(Pos.CENTER_RIGHT);
    logoutBox.setPadding(new Insets(10, 50, 50, 0));

    Button logout = new Button("Log out");
    hoverOverAnimation(logout);
    logout.setOnAction(e -> new startPage(root));
    logout.setStyle(
        "-fx-font-size:35;-fx-font-family:'arial rounded mt bold';-fx-background-color: DarkGoldenRod;-fx-text-fill:Silver;-fx-background-radius:10;");

    logoutBox.getChildren().add(logout);

    root.setTop(helloBox);
    root.setCenter(menuBox);
    root.setBottom(logoutBox);
  }
}
