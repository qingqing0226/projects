package hotelproject.windows;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class startPage extends Windows{
  
  public startPage(BorderPane root) {
    super(root);
    /* reset background image */
    setBackground(root, "background1.png");

    /* helloBox contains: hello, iv */
    VBox helloBox = new VBox(40);
    helloBox.setAlignment(Pos.TOP_CENTER);

    Label hello = new Label("WELCOME TO ALUMINUM HOTEL");
    hello.setStyle("-fx-text-fill: Silver;-fx-font-family:'Garamond';-fx-font-size:60;");
    /* logo image */
    ImageView iv = new ImageView(new Image("file:resources/logo2.png", 200, 200, true, true));
    helloBox.getChildren().addAll(iv, hello);
    root.setTop(helloBox);

    /* buttonBox contains: login */
    HBox buttonBox = new HBox();
    buttonBox.setAlignment(Pos.CENTER);

    Button login = new Button("Log in");
    hoverOverAnimation(login);
    login.setPrefWidth(200.0);
    login.setFont(Font.font("verdana", FontWeight.BOLD, 40));
    login.setStyle("-fx-text-fill: Silver;-fx-background-color: DarkGoldenRod;-fx-background-radius:10;");
    login.setPadding(new Insets(5));
    login.setOnAction(e -> new login(root));

    buttonBox.getChildren().add(login);

    root.setCenter(buttonBox);
  }
}
