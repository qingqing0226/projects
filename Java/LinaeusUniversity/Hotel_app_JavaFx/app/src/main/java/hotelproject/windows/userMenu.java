package hotelproject.windows;

import hotelproject.User.AccessLevel;
import hotelproject.User;
import hotelproject.dbHandeler;

import java.util.*;
import java.sql.SQLException;

import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.control.Alert.AlertType;

public class userMenu extends Windows {
  /**
   * user
   */
  userMenu(BorderPane root) {
    super(root);

    String buttonStyle = "-fx-font-size:30;-fx-font-family:'arial rounded mt bold';-fx-background-color: DarkGoldenRod;-fx-text-fill:Silver;-fx-background-radius:10;";
    String labelStyle = "-fx-font-size: 45;-fx-font-family: 'arial rounded mt bold';-fx-text-fill: Silver;";

    List<User> users_List = new ArrayList<>();
    try {
      users_List = dbHandeler.getAllUser();
    } catch (SQLException e2) {
      e2.printStackTrace();
    }
    List<User> users = users_List;

    ObservableList<String> listStaff = FXCollections.observableArrayList();
    ObservableList<String> listAdmin = FXCollections.observableArrayList();
    ObservableList<String> listAllUser = FXCollections.observableArrayList();
    for (User i : users) {
      if (i.getAccessLevel().equals(User.AccessLevel.Admin)) {
        listAdmin.add(i.getUserName());
      } else {
        listStaff.add(i.getUserName());
      }
      listAllUser.add(i.getUserName());
    }

    ListView<String> listView = new ListView<String>();
    listView.setStyle("-fx-font-size: 20;");
    listView.setPlaceholder(new Label("No staff access level user"));
    listView.setItems(listAllUser);
    listView.getFocusModel().focusedItemProperty();

    ListView<String> listViewAdmin = new ListView<String>();
    listViewAdmin.setStyle("-fx-font-size: 20;");
    listViewAdmin.setPlaceholder(new Label("No admin access level user"));
    listViewAdmin.setItems(listAdmin);
    listViewAdmin.getFocusModel().focusedIndexProperty();

    // title
    Label title = new Label("User Menu");
    title.setStyle(labelStyle);
    BorderPane.setAlignment(title, Pos.CENTER);
    BorderPane.setMargin(title, new Insets(20, 0, 10, 0));

    // Users
    BorderPane userBorderPane = new BorderPane();

    Label userLabel = new Label("Users");
    userLabel.setStyle(labelStyle);
    BorderPane.setAlignment(userLabel, Pos.CENTER);
    BorderPane.setMargin(userLabel, new Insets(0, 0, 10, 0));

    Button grant = new Button(" Grant admin rights ");
    grant.setFont(Font.font("century", 15));
    grant.setStyle(buttonStyle);
    hoverOverAnimation(grant);
    grant.setOnAction(e -> {
      String name = listView.getSelectionModel().getSelectedItem();
      if (user.getUserName().equals(name)) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setHeaderText("You can't change your Access Level by your self!");
        alert.show();
      } else if (name == null) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setHeaderText("You need to select someone!");
        alert.show();
      } else if (listAdmin.indexOf(name) != -1) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setHeaderText("This user is already an Administrator!");
        alert.show();
      } else {
        // listStaff.remove(name);
        listAdmin.add(name);
        for (User i : users) {
          if (i.getUserName().equals(name)) {
            try {
              i.setAccessLevel(AccessLevel.Admin);
            } catch (SQLException e1) {
              e1.printStackTrace();
            }
          }
        }
      }
    });
    BorderPane.setAlignment(grant, Pos.CENTER);
    BorderPane.setMargin(grant, new Insets(10, 0, 0, 0));

    BorderPane.setAlignment(listView, Pos.CENTER);

    userBorderPane.setTop(userLabel);
    userBorderPane.setCenter(listView);
    userBorderPane.setBottom(grant);

    // Admins
    BorderPane adminBorderPane = new BorderPane();

    Label adminLabel = new Label("Admins");
    adminLabel.setStyle(labelStyle);
    BorderPane.setAlignment(adminLabel, Pos.CENTER);
    BorderPane.setMargin(adminLabel, new Insets(0, 0, 10, 0));

    Button remove = new Button("Remove admin rights");
    remove.setFont(Font.font("century", 15));
    remove.setStyle(buttonStyle);
    hoverOverAnimation(remove);
    remove.setOnAction(e -> {
      String name = listViewAdmin.getSelectionModel().getSelectedItem();
      if (user.getUserName().equals(name)) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setHeaderText("You can't change your Access Level by your self!");
        alert.show();
      } else if (name == null) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setHeaderText("You need to select someone!");
        alert.show();
      } else {
        listAdmin.remove(name);
        // listStaff.add(name);
        for (User i : users) {
          if (i.getUserName().equals(name)) {
            try {
              i.setAccessLevel(AccessLevel.Staff);
            } catch (SQLException e1) {
              e1.printStackTrace();
            }
          }
        }
      }
    });
    BorderPane.setAlignment(remove, Pos.CENTER);
    BorderPane.setMargin(remove, new Insets(10, 0, 0, 0));

    BorderPane.setAlignment(listViewAdmin, Pos.CENTER);

    adminBorderPane.setTop(adminLabel);
    adminBorderPane.setCenter(listViewAdmin);
    adminBorderPane.setBottom(remove);

    // add user button
    BorderPane ButtonPane = new BorderPane();

    Button createUser = new Button("Create user");
    createUser.setStyle(buttonStyle);
    hoverOverAnimation(createUser);
    createUser.setOnAction(e -> new createUser(root));
    BorderPane.setAlignment(createUser, Pos.CENTER);
    ButtonPane.setRight(createUser);

    Button removeUser = new Button("Remove user");
    removeUser.setStyle(buttonStyle);
    hoverOverAnimation(removeUser);
    removeUser.setOnAction(e -> {
      String name = listView.getSelectionModel().getSelectedItem();
      if (name == null) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setHeaderText("You need to select someone!");
        alert.show();
      } else {
        for (User i : users) {
          if (i.getUserName().equals(name)) {
            if (i.getAccessLevel().equals(AccessLevel.Admin)) {
              Alert alert = new Alert(AlertType.WARNING);
              alert.setHeaderText("You can't delete an Administrator!");
              alert.show();
            } else {
              Alert confirm = new Alert(AlertType.CONFIRMATION);
              confirm.setHeaderText("Delete User `" + i.getUserName() + "`?");
              confirm.showAndWait();
              if (confirm.getResult().equals(ButtonType.OK)) {
                i.deleteUser();
                listAllUser.remove(i.getUserName());
              }
            }
          }
        }
      }
    });
    BorderPane.setAlignment(removeUser, Pos.CENTER);
    ButtonPane.setCenter(removeUser);

    Button back = new Button("     Back     ");
    back.setStyle(buttonStyle);
    hoverOverAnimation(back);
    back.setOnAction(e -> new mainMenu(root));
    BorderPane.setAlignment(back, Pos.CENTER);
    ButtonPane.setLeft(back);

    HBox hBox1 = new HBox(100);
    hBox1.setAlignment(Pos.CENTER);
    hBox1.setPadding(new Insets(20, 0, 25, 0));
    hBox1.getChildren().addAll(userBorderPane, adminBorderPane);

    BorderPane layout = new BorderPane(hBox1, title, null, ButtonPane, null);
    BorderPane.setAlignment(hBox1, Pos.CENTER);
    BorderPane.setMargin(hBox1, new Insets(12, 12, 12, 12));

    BorderPane.setAlignment(ButtonPane, Pos.BOTTOM_CENTER);
    BorderPane.setMargin(ButtonPane, new Insets(12, 12, 12, 12));

    root.setCenter(layout);
  }
}
