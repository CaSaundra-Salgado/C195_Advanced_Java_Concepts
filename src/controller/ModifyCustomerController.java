package controller;

import DAO.CustomerDB;
import javafx.collections.FXCollections;
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


/** Modify customer controller to modify the selected customer.
 *
 * @author CaSaundra Salgado.
 * */
public class ModifyCustomerController extends DAO.CustomerDB implements Initializable {

    private ObservableList<Customer> modifyCustomersList = FXCollections.observableArrayList();

    @FXML private TableView<Customer> customersTable;
    @FXML private TableColumn<Customer, Integer> customerIDColumn;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> customerPhoneColumn;
    @FXML private TableColumn<Customer, String> customerAddressColumn;
    @FXML private TableColumn<Customer, String> customerDivisionColumn;
    @FXML private TableColumn<Customer, String> customerPostalCodeColumn;


    //All text fields
    @FXML private TextField modifyCustomerID;
    @FXML
    private TextField modifyCustomerNameField;
    @FXML
    private TextField modifyCustomerPhoneField;
    @FXML
    private ComboBox<String> modifyCustomerCountryField;
    @FXML
    private TextField modifyCustomerAddressField;
    @FXML
    private ComboBox<String> modifyCustomerDivisionField;
    @FXML
    private TextField modifyCustomerPostalCodeField;



    /** Initializes the controller.
     * Sets the items in the table fields and the combo boxes.
     * @param url is used for the path/location.
     * @param resourceBundle is used to store texts and data that are locale sensitive. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customersTable.setItems(modifyCustomersList);

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
            modifyCustomerCountryField.setItems(CustomerDB.getAllCountries());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        modifyCustomerCountryField.valueProperty().addListener((obs, oldValue, newValue) ->  {
            if (newValue == null) {
                modifyCustomerDivisionField.getItems().clear();
                modifyCustomerDivisionField.setDisable(true);

            } else {
                modifyCustomerDivisionField.setDisable(false);
                try {
                    modifyCustomerDivisionField.setItems(CustomerDB.getFilteredDivisions(modifyCustomerCountryField.getValue()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        });

    }

    /** Clears all the fields. */
    public void clearAllFields() {
        modifyCustomerID.setText("");
        modifyCustomerNameField.setText("");
        modifyCustomerPhoneField.setText("");
        modifyCustomerCountryField.setValue("");
        modifyCustomerAddressField.setText("");
        modifyCustomerDivisionField.setValue("");
        modifyCustomerPostalCodeField.setText("");
    }

    /** Saves the modified customer if there are no empty fields and sends the user back to the customer view.
     * @param actionEvent performs an action for the stage.
     * @throws NumberFormatException issues an error if a conversion from a string to a number is not formatted
     * correctly. */
    public void onModifyCustomerSaveBtn(ActionEvent actionEvent) throws NumberFormatException {
        try {
            int customerID = Integer.parseInt(String.valueOf(modifyCustomerID.getText()));
            String customerName = modifyCustomerNameField.getText();
            String customerPhone = modifyCustomerPhoneField.getText();
            String customerCountry = modifyCustomerCountryField.getValue();
            String customerAddress = modifyCustomerAddressField.getText();
            String customerDivision = modifyCustomerDivisionField.getValue();
            String customerPostalCode = modifyCustomerPostalCodeField.getText();

            if (customerName.isEmpty() || customerPhone.isEmpty() || customerCountry.isEmpty() || customerAddress.isEmpty() ||
                    customerDivision.isEmpty() || customerPostalCode.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The value in the fields cannot be empty. Please enter" +
                        " a valid value in all fields.");
                alert.setTitle("Error...");
                alert.setHeaderText("Please check that all fields have appropriate values...");
                alert.showAndWait();
            }
            else if (!(modifyCustomerPostalCodeField.getText().matches("[a-zA-Z0-9]+$"))) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The value in this field must contain integers and/or a " +
                        "string. Please enter a valid postal code.");
                alert.setTitle("Oops...");
                alert.setHeaderText("Please check the postal code and try again...");
                alert.showAndWait();
            }
            else {
                Boolean modifyCustomer = CustomerDB.updateCustomer(customerID, customerName, customerAddress,
                        customerPostalCode,
                        customerPhone, customerDivision);

                // notify user we successfully added to DB, or if there was an error.
                if (modifyCustomer) {
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Customer updated successfully!", clickOkay);
                    alert.showAndWait();
                } else {
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Failed to update Customer", clickOkay);
                    alert.showAndWait();
                    return;
                }
                CustomerDB.updateCustomer((customerID), customerName, customerAddress, customerPostalCode,
                        customerPhone,
                        customerDivision);

                ObservableList<Customer> updateCustomersList = CustomerDB.getAllCustomers();
                customersTable.setItems(updateCustomersList);


                Button stageCloseButton = new Button("stage.close();");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are about to exit this screen and return " +
                        "to the customer screen. Would you like to continue?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Parent root = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                    stageCloseButton.setOnAction(event -> stage.close());

                    clearAllFields();
                }
            }
        }catch(Exception e){
                e.printStackTrace();
            }
        }


        /** Retrieves the selected customer to modify.
         * @param customerToModify object to modify the selected customer.
         * @throws SQLException if there is an issue. */
        public void sendCustomer (Customer customerToModify) throws SQLException {
            modifyCustomerID.setText(String.valueOf(customerToModify.getCustomerID()));
            modifyCustomerNameField.setText(customerToModify.getCustomerName());
            modifyCustomerPhoneField.setText(customerToModify.getCustomerPhone());
            modifyCustomerCountryField.setValue(customerToModify.getCustomerCountry());
            modifyCustomerAddressField.setText(customerToModify.getCustomerAddress());
            modifyCustomerDivisionField.setValue(customerToModify.getCustomerDivision());
            modifyCustomerPostalCodeField.setText(customerToModify.getCustomerPostalCode());

            modifyCustomersList.addAll(CustomerDB.getAllCustomers());
        }

        /** When the clear button is pressed it clears all the text fields and combo boxes. */
        public void onModifyCustomerClearButton() {
            clearAllFields();
     }

     /** Cancels the modify action and returns the user back to the customer screen.
      * @param actionEvent performs an action on the stage.
      * @throws IOException if there is an issue with the path/location. */
        public void onModifyCustomerCancelBtn (ActionEvent actionEvent) throws IOException {
            System.out.println("Cancelling...");

            Button stageCloseButton = new Button("stage.close();");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are about to exit this screen and return to " +
                    "the customer screen. Would you like to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Parent root = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
                stageCloseButton.setOnAction(event -> stage.close());
            }
        }

        /** Deletes the selected customer and displays the customer ID and name to be deleted for confirmation. */
    public void onModifyDeleteButton() {
        System.out.println("Deleting Customer...");

        try {
            int deleteCustomerID = customersTable.getSelectionModel().getSelectedItem().getCustomerID();
            String deleteCustomerName = customersTable.getSelectionModel().getSelectedItem().getCustomerName();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are about to delete the customer with the " +
                    "ID of:" + " " + deleteCustomerID + " and customer name: " + deleteCustomerName +
                    ". " + "Would you like to continue?");
            Optional<ButtonType> confirmation = alert.showAndWait();
            if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
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
