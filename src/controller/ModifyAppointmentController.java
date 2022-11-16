package controller;

import DAO.AppointmentDB;
import DAO.ContactDB;
import DAO.CustomerDB;
import DAO.UserDB;
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
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static DAO.AppointmentDB.*;

/** Modify appointment controller when an existing appointment needs to be modified.
 *
 * @author CaSaundra Salgado. */
public class ModifyAppointmentController implements Initializable {

    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    @FXML private TableView<Appointment> modifyAppointmentCustomerTable;
    @FXML private TableColumn<Appointment, Integer> modifyAppointmentIDColumn;
    @FXML private TableColumn<Appointment, Integer> modifyCustomerIDColumn;
    @FXML private TableColumn<Appointment, Integer> modifyUserIDColumn;

    @FXML private TextField modifyAppointmentID;
    @FXML private ComboBox<Integer> modifyAppointmentCustomerID;
    @FXML private ComboBox<Integer> modifyAppointmentUserID;
    @FXML private TextField modifyAppointmentTitle;
    @FXML private TextArea modifyAppointmentDescription;
    @FXML private TextField modifyAppointmentLocation;
    @FXML private ComboBox<String> modifyAppointmentContact;
    @FXML private TextField modifyAppointmentType;
    @FXML private DatePicker modifyAppointmentDate;
    @FXML private ComboBox<LocalTime> modifyAppointmentStart;
    @FXML private ComboBox<LocalTime> modifyAppointmentEnd;


    /** Auto-Generated */
    public ModifyAppointmentController() {
    }


    /** Initializes modify appointment controller and gets all users, contacts, customers, and appointment times and
     * sets the tableviews.
     *
     * @param url is used for the path/location.
     * @param resourceBundle is used to store texts and data that are locale sensitive. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Getting a list of all contacts for the combo box
        ObservableList<Contact> contactsList;
        try {
            contactsList = ContactDB.getAllContacts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<String> allContacts = FXCollections.observableArrayList();
        contactsList.forEach(contacts -> allContacts.add(contacts.getContactName()));


        //Getting a list of all customer IDs for the combo box
        ObservableList<Customer> customersList;
        try {
            customersList = CustomerDB.getAllCustomers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Integer> allCustomers = FXCollections.observableArrayList();
        customersList.forEach(customers -> allCustomers.add(customers.getCustomerID()));


        //Getting a list of all user IDs for the combo box
        ObservableList<User> usersList;
        try {
            usersList = UserDB.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Integer> allUsers = FXCollections.observableArrayList();
        usersList.forEach(users -> allUsers.add(users.getUserID()));


        //Getting a list of all appointments times for the combo box
        ObservableList<LocalTime> appointmentTimes = FXCollections.observableArrayList();
        LocalTime startAppointmentTime = LocalTime.MIN.plusHours(0);
        LocalTime endAppointmentTime = LocalTime.MAX.minusHours(0).minusMinutes(30);

        if (!startAppointmentTime.equals(0) || !endAppointmentTime.equals(0)) {
            while (startAppointmentTime.isBefore(endAppointmentTime)) {
                appointmentTimes.add(LocalTime.parse(String.valueOf(startAppointmentTime)));
                startAppointmentTime = startAppointmentTime.plusMinutes(30);
            }
        }

        //This actually sets the values for the combo boxes (visualization)
        modifyAppointmentStart.setItems(appointmentTimes);
        modifyAppointmentEnd.setItems(appointmentTimes);
        modifyAppointmentContact.setItems(allContacts);
        modifyAppointmentCustomerID.setItems(allCustomers);
        modifyAppointmentUserID.setItems(allUsers);


        //Displays all appointments that are saved in the database
        ObservableList<Appointment> appointmentsList;
        try {
            appointmentsList = AppointmentDB.getAllAppointments();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        modifyAppointmentCustomerTable.setItems(appointmentsList);

        try {
            modifyAppointmentCustomerTable.setItems(getAllAppointments());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        modifyAppointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        modifyCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        modifyUserIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }

    /** Clears all fields after the user inputs and saves. */
    public void clearAllFields() {
        modifyAppointmentID.clear();
        modifyAppointmentCustomerID.getSelectionModel().clearSelection();
        modifyAppointmentUserID.getSelectionModel().clearSelection();
        modifyAppointmentTitle.setText("");
        modifyAppointmentDescription.setText("");
        modifyAppointmentLocation.setText("");
        modifyAppointmentContact.getSelectionModel().clearSelection();
        modifyAppointmentType.setText("");
        modifyAppointmentDate.getEditor().clear();
        modifyAppointmentStart.getSelectionModel().clearSelection();
        modifyAppointmentEnd.getSelectionModel().clearSelection();
    }

    /** Modifies the selected appointment when the modify button is pressed and no text fields and combo boxes are empty.
     * @param actionEvent performs an action for the stage. */
    public void onModifyAppointmentBtn(ActionEvent actionEvent) {
//            Appointment appointment = (Appointment) modifyAppointmentCustomerTable.getSelectionModel().getSelectedItem();

        try {

            int appointmentID = Integer.parseInt(String.valueOf(Integer.valueOf(modifyAppointmentID.getText())));
            String appointmentTitle = modifyAppointmentTitle.getText();
            String appointmentDescription = modifyAppointmentDescription.getText();
            String appointmentLocation = modifyAppointmentLocation.getText();
            String appointmentContact = modifyAppointmentContact.getValue();
            String appointmentType = modifyAppointmentType.getText();
            LocalDate appointmentDate = modifyAppointmentDate.getValue();
            LocalTime appointmentStart = modifyAppointmentStart.getValue();
            LocalTime appointmentEnd = modifyAppointmentEnd.getValue();
            int customerID = modifyAppointmentCustomerID.getValue();
            int userID = modifyAppointmentUserID.getValue();

            if (appointmentTitle.isEmpty() || appointmentDescription.isEmpty() || appointmentLocation.isEmpty()
                    || appointmentContact.isEmpty() || appointmentType.isEmpty() || appointmentDate.toString().isEmpty() ||
                    appointmentStart.toString().isEmpty() || appointmentEnd.toString().isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Oops...");
                alert.setHeaderText("An appointment field is empty.");
                alert.setContentText("Please ensure that all fields contain a value and try again.");
                alert.showAndWait();
            }
            else {

                boolean updateSuccessful = AppointmentDB.updateAppointment(appointmentID, appointmentTitle,
                        appointmentDescription,
                        appointmentLocation,
                        appointmentType, LocalDateTime.of(appointmentDate, appointmentStart),
                        LocalDateTime.of(appointmentDate, appointmentEnd), customerID, userID,
                        findContactID(appointmentContact));

                // notify user we successfully added to DB, or if there was an error.
                if (updateSuccessful) {
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment updated successfully!",
                            clickOkay);
                    alert.showAndWait();
                } else {
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Failed to update Appointment", clickOkay);
                    alert.showAndWait();
                    return;
                }

                AppointmentDB.updateAppointment(appointmentID, appointmentTitle, appointmentDescription,
                        appointmentLocation,
                        appointmentType, LocalDateTime.of(appointmentDate, appointmentStart),
                    LocalDateTime.of(appointmentDate, appointmentEnd), customerID, userID,
                           findContactID(appointmentContact));

                System.out.println("Updating appointment...");
                clearAllFields();

                ObservableList<Appointment> updateAppointmentsList = AppointmentDB.getAllAppointments();
                modifyAppointmentCustomerTable.setItems(updateAppointmentsList);

                Button stageCloseButton = new Button("stage.close();");

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are about to exit this screen and return " +
                        "to the appointment screen. Would you like to continue?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Parent root = FXMLLoader.load(getClass().getResource("/view/Appointment.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();

                    stageCloseButton.setOnAction(event -> stage.close());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /** Retrieves the appointment to be modified from the appointments table.
     * @param appointmentToModify object to modify the selected appointment.
     * @throws SQLException if there is an issue. */
    public void sendAppointment (Appointment appointmentToModify) throws SQLException {
        ObservableList<Contact> contactList = ContactDB.getAllContacts();
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        String contactName = " ";

        contactList.forEach(contacts -> contactNames.add(contacts.getContactName()));
        modifyAppointmentContact.setItems(contactNames);

        for (Contact contact : contactList) {
            if (appointmentToModify.getContactID() == contact.getContactID()) {
                contactName = contact.getContactName();
            }

            modifyAppointmentID.setText(String.valueOf(appointmentToModify.getAppointmentID()));
            modifyAppointmentCustomerID.setValue(appointmentToModify.getCustomerID());
            modifyAppointmentUserID.setValue(appointmentToModify.getUserID());
            modifyAppointmentTitle.setText(appointmentToModify.getAppointmentTitle());
            modifyAppointmentDescription.setText(appointmentToModify.getAppointmentDescription());
            modifyAppointmentLocation.setText(appointmentToModify.getAppointmentLocation());
            modifyAppointmentType.setText(appointmentToModify.getAppointmentType());
            modifyAppointmentDate.setValue(LocalDate.from(appointmentToModify.getAppointmentStartDateTime()));
            modifyAppointmentStart.setValue(LocalTime.from(appointmentToModify.getAppointmentStartDateTime()));
            modifyAppointmentEnd.setValue(LocalTime.from(appointmentToModify.getAppointmentEndDateTime()));
            modifyAppointmentContact.setValue(contactName);
            appointmentList.addAll(getAllAppointments());
        }
    }


    /** Cancels modifying the selected appointment and returns to the appointment screen.
     * @param actionEvent performs an action for the stage.
     * @throws IOException if there is an issue with the path/location. */
    public void onBackModifyAppointmentBtn(ActionEvent actionEvent) throws IOException {
        System.out.println("Cancelling...");
        Button stageCloseButton = new Button("stage.close();");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are about to exit this screen and return to the " +
                "appointment" +
                " screen. Would you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Appointment.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            stageCloseButton.setOnAction(event -> stage.close());
        }
    }
}
