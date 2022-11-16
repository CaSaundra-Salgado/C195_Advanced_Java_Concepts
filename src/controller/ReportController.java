package controller;

import DAO.AppointmentDB;
import DAO.ContactDB;
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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;


/** Report controller class for all the reports.
 *
 * @author CaSaundra Salgado. */
public class ReportController implements Initializable {


    //Type table view
    @FXML private TableView<ReportType> reportsAppointmentsTypeTable;
    @FXML private TableColumn<ReportType, String> reportsAppointmentTypeColumn;
    @FXML private TableColumn<ReportType, Integer> reportsAppointmentTypeTotalColumn;


    //Month table view
    @FXML private TableView<ReportMonth> reportsAppointmentMonthTable;
    @FXML private TableColumn<ReportMonth, String> reportsAppointmentsMonthColumn;
    @FXML private TableColumn<ReportMonth, Integer> reportsAppointmentsMonthTotalColumn;

    @FXML private TableView<Appointment> reportsUserIDTable;
    @FXML private TableColumn<Appointment, Integer> userIDReportColumn;
    @FXML private TableColumn<Appointment, Integer> contactIDReportColumn;
    @FXML private TableColumn<Appointment, Integer> appointmentIDReportColumn;
    @FXML private TableColumn<Appointment, String> appointmentTypeReportColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> appointmentStartReportColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> appointmentEndReportColumn;


    @FXML private TableView<Appointment> reportsContactTable;
    @FXML private TableColumn<Appointment, Integer> reportsContactIDColumn;
    @FXML private TableColumn<Appointment, Integer> reportsContactAppointmentIDColumn;
    @FXML private TableColumn<Appointment, Integer> reportsContactCustomerIDColumn;
    @FXML private TableColumn<Appointment, String> reportsContactTitleColumn;
    @FXML private TableColumn<Appointment, String> reportsContactTypeColumn;
    @FXML private TableColumn<Appointment, String> reportsContactDescriptionColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> reportsContactStartColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> reportsContactEndColumn;

    @FXML private ComboBox<String> contactReportsSelection;
    @FXML private ComboBox<String> userReportsSelection;


    /** Initializes the controller and sets the table view, contacts, and users.
     *
     * @param url is used for the path/location.
     *
     * @param resourceBundle is used to store texts and data that are locale sensitive. */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //activates the columns for the first report tab to populate the data.
        reportsAppointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentType"));
        reportsAppointmentTypeTotalColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentTotal"));

        reportsAppointmentsMonthColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentMonth"));
        reportsAppointmentsMonthTotalColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentTotal"));

        //Retrieves all appointments from the database and displays them on the respective tables.
        try {
            reportsContactTable.setItems(AppointmentDB.getAllAppointments());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            reportsUserIDTable.setItems(AppointmentDB.getAllAppointments());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        //activates the columns for the second and third report tabs to populate the data.
        reportsContactIDColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        reportsContactAppointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        reportsContactCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        reportsContactTitleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        reportsContactTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        reportsContactDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        reportsContactStartColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStartDateTime"));
        reportsContactEndColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentEndDateTime"));

        userIDReportColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        contactIDReportColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        appointmentIDReportColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTypeReportColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentStartReportColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStartDateTime"));
        appointmentEndReportColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentEndDateTime"));

        //Displays all contacts, users, and types for the reports tabs.
        ObservableList<Contact> contactsList;
        try {
            contactsList = ContactDB.getAllContacts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<String> allContactsNames = FXCollections.observableArrayList();
        contactsList.forEach(contacts -> allContactsNames.add(contacts.getContactName()));
        contactReportsSelection.setItems(allContactsNames);


        ObservableList<User> usersList;
        try {
            usersList = UserDB.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<String> allUsersNames = FXCollections.observableArrayList();
        usersList.forEach(user -> allUsersNames.add(String.valueOf(user.getUserName())));
        userReportsSelection.setItems(allUsersNames);
    }

    /** When the tab for selecting reports by contact is selected, the table view will populate all appointments
     * associated with the selected contact and the total number of appointments per contact. */
    public void onContactReportsSelection() {

        //Get selected value from combo box
        try {

            int contactID = 0;

            ObservableList<Appointment> allAppointments = AppointmentDB.getAllAppointments();
            ObservableList<Appointment> allAppointmentInfo = FXCollections.observableArrayList();
            ObservableList<Contact> getAllContacts = ContactDB.getAllContacts();

            Appointment contactAppointmentInfo;

            String contactName = contactReportsSelection.getSelectionModel().getSelectedItem();

            for (Contact contact : getAllContacts) {
                if (contactName.equals(contact.getContactName())) {
                    contactID = contact.getContactID();
                }
            }

            for (Appointment appointment : allAppointments) {
                if (appointment.getContactID() == contactID) {
                    contactAppointmentInfo = appointment;
                    allAppointmentInfo.add(contactAppointmentInfo);
                }
            }
            reportsContactTable.setItems(allAppointmentInfo);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /** When the tab for selecting reports by type and month is selected, the table view will populate all appointment
     * types with a total and all appointments by month and the total number of appointments for that month. */
    public void onAppointmentsByTypeMonth() {

        try {
            ObservableList<Appointment> getAllAppointments = AppointmentDB.getAllAppointments();
            ObservableList<Month> appointmentMonths = FXCollections.observableArrayList();
            ObservableList<Month> monthOfAppointments = FXCollections.observableArrayList();

            ObservableList<String> appointmentType = FXCollections.observableArrayList();
            ObservableList<String> uniqueAppointment = FXCollections.observableArrayList();

            ObservableList<ReportType> reportType = FXCollections.observableArrayList();
            ObservableList<ReportMonth> reportMonths = FXCollections.observableArrayList();

            //One line Lambda expression for appointments by type.
            getAllAppointments.forEach(appointments -> appointmentType.add(appointments.getAppointmentType()));

            getAllAppointments.stream().map(appointment -> appointment.getAppointmentStartDateTime().getMonth()).forEach(appointmentMonths::add);

            appointmentMonths.stream().filter(month -> !monthOfAppointments.contains(month)).forEach(monthOfAppointments::add);

            for (Appointment appointments: getAllAppointments) {
                String appointmentsAppointmentType = appointments.getAppointmentType();
                if (!uniqueAppointment.contains(appointmentsAppointmentType)) {
                    uniqueAppointment.add(appointmentsAppointmentType);
                }
            }

            for (Month month : monthOfAppointments) {
                int totalMonth = Collections.frequency(appointmentMonths, month);
                String monthName = month.name();
                ReportMonth appointmentMonth = new ReportMonth(monthName, totalMonth);
                reportMonths.add(appointmentMonth);
            }
            reportsAppointmentMonthTable.setItems(reportMonths);


            for (String type: uniqueAppointment) {
                int typeTotal = Collections.frequency(appointmentType, type);
                ReportType appointmentTypes = new ReportType(type, typeTotal);
                reportType.add(appointmentTypes);
            }
            reportsAppointmentsTypeTable.setItems(reportType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /** When the tab for selecting reports by user is selected, the table view will populate all appointments
     * by user ID and the total number of appointments per user. */
    public void onUserReportsSelection() {
        //Get selected value from combo box
        try {

            int userID = 0;

            ObservableList<Appointment> allAppointments = AppointmentDB.getAllAppointments();
            ObservableList<Appointment> allAppointmentInfo = FXCollections.observableArrayList();
            ObservableList<User> getAllUsers = UserDB.getAllUsers();

            Appointment userAppointmentInfo;

            String userInfo = userReportsSelection.getSelectionModel().getSelectedItem();

            for (User user : getAllUsers) {
                if (userInfo.equals(user.getUserName())) {
                    userID = user.getUserID();
                }
            }

            for (Appointment appointment : allAppointments) {
                if (appointment.getUserID() == userID) {
                    userAppointmentInfo = appointment;
                    allAppointmentInfo.add(userAppointmentInfo);
                }
            }
            reportsUserIDTable.setItems(allAppointmentInfo);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /** Cancels the current action and redirects the user to the main screen. */
    public void onReportBackButton(ActionEvent actionEvent) throws IOException {
        System.out.println("Going back to the main screen...");

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
}

