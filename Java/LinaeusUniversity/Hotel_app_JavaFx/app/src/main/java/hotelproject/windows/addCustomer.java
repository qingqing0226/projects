package hotelproject.windows;

import hotelproject.dbHandeler;
import hotelproject.Customer;
import hotelproject.Customer.PaymentMethod;

import com.mysql.cj.util.StringUtils;

import java.sql.SQLException;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;

public class addCustomer extends Windows {

  addCustomer(BorderPane root) {
    super(root);
    StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    Class<?> callerClass = walker.getCallerClass();

    /* Logo image + Page Title */
    ImageView logo = new ImageView(new Image("file:resources/round_logo.png", 200, 200, true, true));
    Label pageTitle = new Label("Add Customer's Information");
    pageTitle.setStyle("-fx-text-fill: Silver;-fx-font-family:'arial rounded mt bold';-fx-font-size:60;");

    VBox logoAndTitle = new VBox();
    logoAndTitle.setAlignment(Pos.CENTER);
    logoAndTitle.getChildren().addAll(logo, pageTitle);

    /* User inputs. */
    String labelStyle = "-fx-text-fill: Silver;-fx-font-family:'arial rounded mt bold';-fx-font-size:37;";
    Label row1 = new Label("Customer Name: ");
    row1.setStyle(labelStyle);
    Label row2 = new Label("Address: ");
    row2.setStyle(labelStyle);
    Label row3 = new Label("Phone: ");
    row3.setStyle(labelStyle);
    Label row4 = new Label("Payment method: ");
    row4.setStyle(labelStyle);

    VBox leftBox = new VBox(10);
    leftBox.setAlignment(Pos.CENTER_RIGHT);
    leftBox.getChildren().addAll(row1, row2, row3, row4);

    // rightBox
    String inputStyle = "-fx-background-radius: 10;-fx-opacity:0.7;-fx-font-size:20";
    TextField customerName = new TextField();
    customerName.setStyle(inputStyle);
    customerName.setPromptText("Full name of customer");
    customerName.setAlignment(Pos.CENTER_LEFT);

    TextField address = new TextField();
    address.setStyle(inputStyle);
    address.setPromptText("Current living place");
    address.setAlignment(Pos.CENTER_LEFT);

    TextField phoneNum = new TextField();
    phoneNum.setEditable(true);
    phoneNum.setPromptText("Phone number");
    phoneNum.setStyle(inputStyle);
    phoneNum.setAlignment(Pos.CENTER_LEFT);

    ComboBox<String> paymentMethod = new ComboBox<String>();
    paymentMethod.setPromptText("Add payment here");
    paymentMethod.setCenterShape(true);
    paymentMethod.setMinSize(280, 25);
    paymentMethod.setStyle(inputStyle);
    paymentMethod.setItems(FXCollections.observableArrayList("MasterCard", "Visa", "Mobile", "Cash"));

    VBox rightBox = new VBox(10);
    rightBox.setAlignment(Pos.CENTER_LEFT);
    rightBox.getChildren().addAll(customerName, address, phoneNum, paymentMethod);

    HBox userInput = new HBox();
    userInput.setAlignment(Pos.CENTER);
    userInput.getChildren().addAll(leftBox, rightBox);

    // Button
    /* Button row. */
    Font font = Font.font("verdana", FontWeight.BOLD, 40);
    String style = "-fx-text-fill: Silver;-fx-background-color: DarkGoldenRod;-fx-background-radius:10;";

    Pane spacer = new Pane();
    spacer.setPrefSize(0, 200);

    Button backButton = new Button("Back");
    backButton.setPrefWidth(200.0);
    backButton.setFont(font);
    backButton.setStyle(style);
    backButton.setPadding(new Insets(5));
    hoverOverAnimation(backButton);

    Button createButton = new Button("Create");
    createButton.setPrefWidth(200.0);
    createButton.setFont(font);
    createButton.setStyle(style);
    createButton.setPadding(new Insets(5));
    hoverOverAnimation(createButton);

    Button editButton = new Button("Edit");
    editButton.setPrefWidth(200.0);
    editButton.setFont(font);
    editButton.setStyle(style);
    editButton.setPadding(new Insets(5));
    editButton.setOnAction(e -> new changeDetailsOfCustomer(root));
    hoverOverAnimation(editButton);

    if (callerClass.toString().equals("class hotelproject.windows.addBooking$1")) {
      editButton.setVisible(false);
      editButton.setManaged(false);
    }

    backButton.setOnAction(e -> {
      if (callerClass.toString().equals("class hotelproject.windows.addBooking$1")) {
        new addBooking(root);
      } else {
        new mainMenu(root);
      }
    });
    hoverOverAnimation(backButton);

    createButton.setOnAction(e -> {
      String phoneNumber_s = phoneNum.getText();
      String name_s = customerName.getText();
      String address_s = address.getText();
      String paymentMethod_s = paymentMethod.getValue();

      Alert fillAlert = new Alert(AlertType.WARNING);
      fillAlert.setContentText("Fill in required fields.");
      Alert typeAlert = new Alert(AlertType.WARNING);
      typeAlert.setContentText("Illegal character usage detected!");
      typeAlert.setHeaderText("Wrong type of input");
      Alert success = new Alert(AlertType.INFORMATION);
      success.setContentText("Customer <" + name_s + "> added successfully!");
      Alert failure = new Alert(AlertType.WARNING);
      failure.setHeaderText("Error! Customer already exists.");
      failure.setContentText("Customer with Phone number: <" + phoneNumber_s + ">  is already in our record.");

      if (phoneNumber_s.trim().isEmpty() || name_s.trim().isEmpty() || address_s.trim().isEmpty()
          || paymentMethod_s == null) {
        fillAlert.show();
      } else if (!StringUtils.isStrictlyNumeric(phoneNumber_s)
          || !name_s.replaceAll("\\s", "").chars().allMatch(Character::isLetter)) {
        typeAlert.show();
      } else {
        PaymentMethod method;
        switch (paymentMethod_s) {
          case "Mobile":
            method = PaymentMethod.Mobile;
            break;
          case "MasterCard":
            method = PaymentMethod.MasterCard;
            break;
          case "Visa":
            method = PaymentMethod.Visa;
            break;
          case "Cash":
            method = PaymentMethod.Cash;
            break;
          default:
            method = PaymentMethod.None;
        }

        try {
          if (dbHandeler.addCustomer(new Customer(name_s, address_s, phoneNumber_s, method))) {
            if (callerClass.toString().equals("class hotelproject.windows.addBooking$1")) {
              success.show();
              new addBooking(root);
            } else {
              success.show();
              new addCustomer(root);
            }
          } else {
            failure.show();
          }
        } catch (SQLException e1) {
          e1.printStackTrace();
        }
      }
    });

    HBox buttonBox = new HBox(50);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(spacer, backButton, createButton, editButton);

    root.setTop(logoAndTitle);
    root.setCenter(userInput);
    root.setBottom(buttonBox);
  }

}
