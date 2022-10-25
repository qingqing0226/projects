package hotelproject;

import hotelproject.windows.SQLprompt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HotelProject extends Application {

  @Override
  public void start(Stage primaryStage) {
    BorderPane root = new BorderPane();
    new SQLprompt(root);

    primaryStage.getIcons().add(new Image("file:resources/logoRescaled.png"));
    primaryStage.setScene(new Scene(root));
    primaryStage.setMaximized(true);
    primaryStage.setTitle("Aluminum Hotel");
    primaryStage.show();
  }
}