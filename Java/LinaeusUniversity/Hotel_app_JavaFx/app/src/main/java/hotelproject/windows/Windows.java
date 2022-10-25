package hotelproject.windows;

import hotelproject.User;
import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class Windows extends BorderPane {
  public static User user;
  protected static String startDateString = "";
  protected static String endDateString = "";
  protected static String selectRoomNumber = "";

  Boolean error = false;
  String password = "";

  Windows(BorderPane root) {
    root.getChildren().clear();
    setBackground(root, "background2.jpg");
  }

  /* this method adds a background image to a page */
  protected void setBackground(BorderPane root, String fileName) {
    String fullPath = "file:resources/" + fileName;
    BackgroundImage bg = new BackgroundImage(new Image(fullPath), null, null, null,
        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
    root.setBackground(new Background(bg));
  }

  protected void hoverOverAnimation(Button b) {
    final FadeTransition fadeIn = new FadeTransition(Duration.millis(100));
    fadeIn.setNode(b);
    fadeIn.setToValue(0.5);
    b.setOnMouseEntered(e -> fadeIn.playFromStart());

    final FadeTransition fadeOut = new FadeTransition(Duration.millis(100));
    fadeOut.setNode(b);
    fadeOut.setToValue(1);
    b.setOnMouseExited(e -> fadeOut.playFromStart());
  }

  protected boolean isNumeric(String text) {
    try {
      Double.parseDouble(text);
    } catch (NumberFormatException e) {
      return false;
    } catch (NullPointerException e) {
      return false;
    }
    return true;
  }

  protected static boolean isInteger(String s) {
    try {
      Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return false;
    } catch (NullPointerException e) {
      return false;
    }
    // only got here if we didn't return false
    return true;
  }
}
