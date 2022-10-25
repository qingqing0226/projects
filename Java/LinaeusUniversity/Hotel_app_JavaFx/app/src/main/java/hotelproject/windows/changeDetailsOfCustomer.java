package hotelproject.windows;

import java.sql.SQLException;

import com.mysql.cj.util.StringUtils;

import hotelproject.Customer;
import hotelproject.dbHandeler;
import hotelproject.Customer.PaymentMethod;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class changeDetailsOfCustomer extends Windows {
  public Customer customer;

  /**
   * User with Staff or higher privilige should be able to change the details of
   * chosen customer object.
   */
  changeDetailsOfCustomer(BorderPane root) {
    super(root);

    Font font = Font.font("verdana", FontWeight.BOLD, 40);
    String buttonStyle = "-fx-text-fill: Silver;-fx-background-color: DarkGoldenRod;-fx-background-radius:10;";
    String labelStyle = "-fx-text-fill: Silver;-fx-font-family:'arial rounded mt bold';-fx-font-size:37;";
    String inputStyle = "-fx-background-radius: 10;-fx-opacity:0.7;-fx-font-size:20";

    /* Logo image + Page Title */
    ImageView logo = new ImageView(new Image("file:resources/round_logo.png", 200, 200, true, true));
    Label pageTitle = new Label("Change customer details");
    pageTitle.setStyle("-fx-text-fill: Silver;-fx-font-family:'arial rounded mt bold';-fx-font-size:60;");

    // Search mechanism and it's addendums
    Label searchLabel = new Label("Search by phone number:");
    searchLabel.setStyle(labelStyle);

    TextField searchField = new TextField();
    searchField.setStyle(inputStyle);
    searchField.setEditable(true);
    searchField.setPromptText("Enter phone number");

    Button search = new Button("Search");
    search.setStyle(buttonStyle);
    search.setFont(Font.font("verdana", FontWeight.BOLD, 20));
    hoverOverAnimation(search);

    // Drop down menu
    ChoiceBox<String> customerList = new ChoiceBox<String>();
    customerList.setCenterShape(true);
    customerList.setMinSize(260, 40);
    customerList.setValue("Customer List");
    customerList.setStyle(inputStyle);
    try {
      customerList.getItems().addAll(dbHandeler.getListofCustomer());
    } catch (SQLException e2) {
      e2.printStackTrace();
    }

    HBox searchBox = new HBox(5);
    searchBox.setAlignment(Pos.CENTER);
    searchBox.getChildren().addAll(searchLabel, searchField, search, customerList);

    VBox HeaderAndSearch = new VBox(15);
    HeaderAndSearch.setAlignment(Pos.CENTER);
    HeaderAndSearch.getChildren().addAll(logo, pageTitle, searchBox);

    // Customer details
    Label row1 = new Label("Name: ");
    row1.setStyle(labelStyle);
    Label row2 = new Label("Address: ");
    row2.setStyle(labelStyle);
    Label row3 = new Label("Phone: ");
    row3.setStyle(labelStyle);
    Label row4 = new Label("Payment: ");
    row4.setStyle(labelStyle);

    TextField customerName = new TextField();
    customerName.setPrefSize(440, 20);
    customerName.setStyle(inputStyle);
    customerName.setEditable(false);
    TextField address = new TextField();
    address.setStyle(inputStyle);
    address.setEditable(false);
    TextField phoneNum = new TextField();
    phoneNum.setStyle(inputStyle);
    phoneNum.setEditable(false);
    ChoiceBox<String> paymentMethod = new ChoiceBox<String>();
    paymentMethod.getItems().addAll("Mobile", "MasterCard", "Visa", "Cash");
    paymentMethod.setCenterShape(true);
    paymentMethod.setMinSize(440, 25);
    paymentMethod.setStyle(inputStyle);

    GridPane userDetails = new GridPane();
    userDetails.setAlignment(Pos.CENTER);
    userDetails.setVgap(10);
    userDetails.add(row1, 0, 0);
    userDetails.add(customerName, 1, 0);
    userDetails.add(row2, 0, 1);
    userDetails.add(address, 1, 1);
    userDetails.add(row3, 0, 2);
    userDetails.add(phoneNum, 1, 2);
    userDetails.add(row4, 0, 3);
    userDetails.add(paymentMethod, 1, 3);

    // Bottom buttons
    Pane spacer = new Pane();
    spacer.setPrefSize(200, 200);

    Button backButton = new Button("Back");
    backButton.setPrefWidth(180.0);
    backButton.setFont(font);
    backButton.setStyle(buttonStyle);
    backButton.setPadding(new Insets(5));
    hoverOverAnimation(backButton);

    Button saveButton = new Button("Save");
    saveButton.setPrefWidth(180.0);
    saveButton.setFont(font);
    saveButton.setStyle(buttonStyle);
    saveButton.setPadding(new Insets(5));
    hoverOverAnimation(saveButton);

    HBox buttonBox = new HBox(50);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(backButton, spacer, saveButton);

    // Actions each button execute when pressed.
    backButton.setOnAction(e -> new addCustomer(root));

    search.setOnAction(e -> {
      Alert failure = new Alert(AlertType.WARNING);
      failure.setContentText("Customer not found!");

      try {
        customer = dbHandeler.getCustomerDetail(searchField.getText());
      } catch (SQLException e1) {
        e1.printStackTrace();
      }

      if (customer == null) {
        failure.show();
      } else {
        customerName.setEditable(true);
        address.setEditable(true);
        phoneNum.setEditable(true);

        customerName.setText(customer.getName());
        address.setText(customer.getAddress());
        phoneNum.setText(customer.getPhoneNo());
        PaymentMethod choice = customer.getPaymentMethod();
        switch (choice) {
          case Mobile:
            paymentMethod.getSelectionModel().select(0);
            break;
          case MasterCard:
            paymentMethod.getSelectionModel().select(1);
            break;
          case Visa:
            paymentMethod.getSelectionModel().select(2);
            break;
          case Cash:
            paymentMethod.getSelectionModel().select(3);
            break;
          default:
            paymentMethod.getSelectionModel().select(4);
        }
      }
    });

    customerList.setOnAction(e -> {
      String phoneNumber = customerList.getValue().replace("Phone number: ", "");
      try {
        customer = dbHandeler.getCustomerDetail(phoneNumber);
        customerName.setText(customer.getName());
        address.setText(customer.getAddress());
        phoneNum.setText(customer.getPhoneNo());
        PaymentMethod choice = customer.getPaymentMethod();
        switch (choice) {
          case Mobile:
            paymentMethod.getSelectionModel().select(0);
            break;
          case MasterCard:
            paymentMethod.getSelectionModel().select(1);
            break;
          case Visa:
            paymentMethod.getSelectionModel().select(2);
            break;
          case Cash:
            paymentMethod.getSelectionModel().select(3);
            break;
          default:
            paymentMethod.getSelectionModel().select(4);
        }
        customerName.setEditable(true);
        address.setEditable(true);
        phoneNum.setEditable(true);
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
      searchField.clear();
    });

    saveButton.setOnAction(e -> {
      Alert success = new Alert(AlertType.INFORMATION);
      success.setHeaderText("Success");
      success.setContentText("Customer info updated.");
      Alert failure = new Alert(AlertType.WARNING);
      failure.setHeaderText("Hiccup in system.");
      failure.setContentText("Error in the transaction with database.");
      Alert fillError = new Alert(AlertType.WARNING);
      fillError.setHeaderText("Attempting prohibited changes");
      fillError.setContentText("Fill in all the field correctly.");

      String nName = customerName.getText();
      String nAddress = address.getText();
      String nNumber = phoneNum.getText();
      String chosenMethod = paymentMethod.getValue();
      if (
        nName.isBlank() || 
        nAddress.isBlank() || 
        nNumber.isBlank() || 
        (chosenMethod == null) || 
        !(StringUtils.isStrictlyNumeric(nNumber)) ||
        !nName.replaceAll("\\s", "").chars().allMatch(Character::isLetter)
      ) {
        fillError.show();
      } else {
        PaymentMethod method;
        switch (chosenMethod) {
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
        Customer newCustomer = new Customer(nName, nAddress, nNumber, method);
        try {
          if (dbHandeler.deleteCustomerDetail(customer) && dbHandeler.addCustomer(newCustomer)) {
            success.show();
            dbHandeler.setPhoneNoInBooking(customer.getPhoneNo(), nNumber);
            new changeDetailsOfCustomer(root);
          } else {
            failure.show();
          }
          customer = newCustomer;
        } catch (SQLException e1) {
          e1.printStackTrace();
        }
        customerName.setEditable(false);
        address.setEditable(false);
        phoneNum.setEditable(false);
      }
    });

    // Postitioning compenents in the window.
    root.setTop(HeaderAndSearch);
    root.setCenter(userDetails);
    root.setBottom(buttonBox);
  }
}