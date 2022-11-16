package controller;


import DAO.AppointmentDB;
import DAO.CustomerDB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static DAO.AppointmentDB.deleteAppointment;


/** Customer controller class for the customers.
 *
 * @author CaSaundra Salgado.*/
public class CustomerController extends DAO.CustomerDB implements Initializable {

    //Table and table columns
    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private TableColumn<Customer, Integer> customerIDColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;
    @FXML
    private TableColumn<Customer, String> customerAddressColumn;
    @FXML
    private TableColumn<Customer, String> customerDivisionColumn;
    @FXML
    private TableColumn<Customer, String> customerPostalCodeColumn;

    //Text fields and combo boxes
    @FXML
    private TextField addCustomerID;
    @FXML
    private TextField addCustomerName;
    @FXML
    private TextField addCustomerPhone;
    @FXML
    private ComboBox<String> addCustomerCountry;
    @FXML
    private TextField addCustomerAddress;
    @FXML
    private ComboBox<String> addCustomerDivision;
    @FXML
    private TextField addCustomerPostalCode;


    Stage stage;


    /** Initializes the controller and sets the table fields and combo boxes. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            customersTable.setItems(getAllCustomers());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("customerDivision"));
        customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));



        try {
            addCustomerCountry.setItems(CustomerDB.getAllCountries());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Lambda expression #1 (Second lambda is in appointment controller initialize method) this is a multiline
        // lambda that listens for the country combo box selection and adjusts the divisions according to the country
        // selection.
         addCustomerCountry.valueProperty().addListener(
                 (observableValue, oldValue, newValue) -> //lambda signature
         {
            if (newValue == null) {
                addCustomerDivision.getItems().clear();
                addCustomerDivision.setDisable(true);

            } else {
                addCustomerDivision.setDisable(false);
                try {
                    addCustomerDivision.setItems(CustomerDB.getFilteredDivisions(addCustomerCountry.getValue()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }


    /**Clears all the text fields so new information can be put in when adding a new user.*/
    public void clearAllFields() {
        addCustomerID.setText("");
        addCustomerName.setText("");
        addCustomerPhone.setText("");
        addCustomerCountry.setValue("");
        addCustomerAddress.setText("");
        addCustomerDivision.setValue("");
        addCustomerPostalCode.setText("");
    }


    /** Adds a new customer when the add button is pressed and there are no blank fields.
     * @throws SQLException if there is an issue. */
    public void onAddCustomerBtn() throws SQLException {
        System.out.println("Adding Customers...");

        String customerName = addCustomerName.getText();
        String customerPhone = addCustomerPhone.getText();
        String customerCountry = addCustomerCountry.getValue();
        String customerAddress = addCustomerAddress.getText();
        String customerDivision = addCustomerDivision.getValue();
        String customerPostalCode = addCustomerPostalCode.getText();


        if (customerName.isBlank() || customerPhone.isBlank() || customerCountry.isBlank() || customerAddress.isBlank() ||
                customerDivision.isBlank() || customerPostalCode.isBlank()) {

            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please ensure no fields are left blank.",
                    clickOkay);
            alert.showAndWait();
        }
        else if (!(addCustomerPostalCode.getText().matches("[a-zA-Z0-9]+$"))) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The value in this field must contain integers and/or a " +
                    "string. Please enter a valid postal code.");
            alert.setTitle("Oops...");
            alert.setHeaderText("Please check the postal code and try again...");
            alert.showAndWait();
        }
        else {
            // Add customer to Database
            boolean addNewCustomer = CustomerDB.addCustomer(customerName, customerAddress, customerPostalCode,
                    customerPhone,
                    CustomerDB.getSpecificDivisionID(customerDivision));
            // notify user we successfully added to DB, or if there was an error.
            if (addNewCustomer) {
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Customer added successfully!", clickOkay);
                alert.showAndWait();
            } else {
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.WARNING, "Failed to add Customer", clickOkay);
                alert.showAndWait();
                return;
            }
            //Updates the customer table showing that the new customer was added successfully.
            ObservableList<Customer> updateCustomersList = CustomerDB.getAllCustomers();
            customersTable.setItems(updateCustomersList);

            clearAllFields();
        }
    }


    /** Cancels the current action and returns to the main screen.
     * @param actionEvent performs an action for the stage.
     * @throws IOException if there is an issue with the path/location. */
    public void onAddCustomerBackBtn(ActionEvent actionEvent) throws IOException {
        System.out.println("Leaving...");
        Button stageCloseButton = new Button("stage.close();");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are about to exit this screen and return to the main screen. Would you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stageCloseButton.setOnAction(event -> stage.close());
        }
    }

    /** Sends the selected customer's information to the modify controller for modification.
     * @param actionEvent performs an action for the stage.
     * @throws IOException if there is an issue with the path/location.*/
    public void onModifyCustomerBtn(ActionEvent actionEvent) throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyCustomer.fxml"));
            loader.load();

            ModifyCustomerController ModController = loader.getController();
            ModController.sendCustomer(customersTable.getSelectionModel().getSelectedItem());

            Button stageCloseButton = new Button("stage.close();");
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
            stageCloseButton.setOnAction(event -> stage.close());
        }

        catch (NullPointerException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error...");
            alert.setHeaderText("No customer selected.");
            alert.setContentText("Please select a customer to modify.");
            alert.showAndWait();
        }
    }

    /** Deletes the selected customer and displays a confirmation message with the ID and name of the customer. */
    public void onDeleteModifyCustomerBtn() {
        System.out.println("Deleting Customer...");

        try {
            int deleteCustomerID = customersTable.getSelectionModel().getSelectedItem().getCustomerID();
            int deleteAppointmentID = customersTable.getSelectionModel().getSelectedItem().getCustomerID();
            String deleteCustomerName = customersTable.getSelectionModel().getSelectedItem().getCustomerName();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are about to delete the customer with the " +
                    "ID of:" + " " + deleteCustomerID + " and customer name: " + deleteCustomerName +
                    ". " + "Would you like to continue?");
            Optional<ButtonType> confirmation = alert.showAndWait();
            if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {

                deleteAppointment(AppointmentDB.deleteAppointment(deleteAppointmentID));
                deleteCustomer(CustomerDB.deleteCustomer(deleteCustomerID));

                ObservableList<Customer> customersList = getAllCustomers();
                customersTable.setItems(customersList);
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error...");
            alert.setHeaderText("No customer selected.");
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
