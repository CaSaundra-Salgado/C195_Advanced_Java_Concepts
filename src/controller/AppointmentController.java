package controller;

import DAO.AppointmentDB;
import DAO.ContactDB;
import DAO.CustomerDB;
import DAO.UserDB;
import helper.TimeManager;
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
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static DAO.AppointmentDB.*;

public class AppointmentController implements Initializable {
    Stage stage;


    //Table views and columns
    @FXML
    private TableView<Appointment> allAppointmentsTable;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIDColumn;
    @FXML
    private TableColumn<Appointment, Integer> customerIDColumn;
    @FXML
    private TableColumn<Appointment, Integer> userIDColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentTitleColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentDescriptionColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentLocationColumn;
    @FXML
    private TableColumn<Appointment, Integer> appointmentContactIDColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentTypeColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentStartColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentEndColumn;


    //Text fields/area and choice boxes
    @FXML
    private TextField appointmentID;
    @FXML
    private ComboBox<Integer> appointmentCustomerID;
    @FXML
    private ComboBox<Integer> appointmentUserID;
    @FXML
    private TextField appointmentTitle;
    @FXML
    private TextArea appointmentDescription;
    @FXML
    private TextField appointmentLocation;
    @FXML
    private ComboBox<String> appointmentContact;
    @FXML
    private ComboBox<String> appointmentType;
    @FXML
    private DatePicker appointmentStartDate;
    @FXML
    private DatePicker appointmentEndDate;
    @FXML
    private ComboBox<LocalTime> appointmentStartTime;
    @FXML
    private ComboBox<LocalTime> appointmentEndTime;

    //Toggle group
    @FXML
    private ToggleGroup appointments;

    //Buttons
    @FXML
    private RadioButton weeklyAppointmentRadioBtn;
    @FXML
    private RadioButton monthlyAppointmentRadioBtn;
    @FXML
    private RadioButton allAppointmentRadioBtn;


    /**
     * Allows for a new appointment to be created.
     */
    @FXML
    public static Appointment appointment = new Appointment();


    /**
     * Initializing the controller and populating contacts, types, customers, appointment times, and user IDs in their
     * respective combo boxes.
     *
     * @param url            is used for the path/location.
     * @param resourceBundle is used to store texts and data that are locale sensitive.
     */

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


        //Getting all pre-populated types.
        ObservableList<String> allTypes = FXCollections.observableArrayList("Planning Session", "De-Briefing",
                "Other");
        appointmentType.setItems(allTypes);


        //Getting a list of all customer IDs for the combo box.
        ObservableList<Customer> customersList;
        try {
            customersList = CustomerDB.getAllCustomers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Integer> allCustomers = FXCollections.observableArrayList();

        //Lambda expression #1 to show all customer IDs from customers list.
        //This is helpful because instead of a for loop going through customer IDs, it does a foreach and puts code
        // on one line instead of multiple lines.
        customersList.forEach(customers -> allCustomers.add(customers.getCustomerID()));


        //Getting a list of all usernames to populate in the combo box and converting the values to IDs for the
        // tableview.
        ObservableList<User> usersList;
        try {
            usersList = UserDB.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Integer> allUsers = FXCollections.observableArrayList();

        usersList.forEach(users -> allUsers.add(users.getUserID()));


        //Getting a list of all appointment times for the combo box in one hour increments based on the system timezone.
        appointmentStartTime.setItems(TimeManager.getTimes(0, 14));
        appointmentEndTime.setItems(TimeManager.getTimes(1, 15));

        appointmentContact.setItems(allContacts);
        appointmentCustomerID.setItems(allCustomers);
        appointmentUserID.setItems(allUsers);


        //Displays all appointments that are saved in the database and displays them on the table.
        try {
            allAppointmentsTable.setItems(getAllAppointments());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Displays all column fields in the table
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStartDateTime"));
        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentEndDateTime"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        appointmentContactIDColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
    }


    /**
     * Clears all fields; combo boxes, text fields and date pickers.
     */
    public void clearAllFields() {
        appointmentID.clear();
        appointmentCustomerID.getSelectionModel().clearSelection();
        appointmentUserID.getSelectionModel().clearSelection();
        appointmentTitle.setText("");
        appointmentDescription.setText("");
        appointmentLocation.setText("");
        appointmentContact.getSelectionModel().clearSelection();
        appointmentType.setValue("");
        appointmentStartDate.getEditor().clear();
        appointmentEndDate.getEditor().clear();
        appointmentStartTime.getSelectionModel().clearSelection();
        appointmentEndTime.getSelectionModel().clearSelection();
    }


    /**
     * When the add appointment button is pressed, there is a check to make sure that there are no empty fields,
     * there are no overlapping appointments, and the appointment being scheduled is within business hours.
     * There is also some time conversion taking place.
     *
     */
    public void onAddAppointmentBtn() throws NumberFormatException, DateTimeException, NullPointerException {
        boolean overlapping = false;
        try {

            LocalDateTime startDT = null;
            LocalDateTime endDT = null;


            if (appointmentTitle.getText().isEmpty() || appointmentDescription.getText().isEmpty() || appointmentLocation.getText().isEmpty()
                    || appointmentType.getValue().isEmpty() || appointmentStartDate.getValue().toString().isEmpty() || appointmentEndDate.getValue().toString().isEmpty() ||
                    appointmentStartTime.getSelectionModel().isEmpty() || appointmentEndTime.getSelectionModel().isEmpty()
                    || appointmentCustomerID.getSelectionModel().isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Oops...");
                alert.setHeaderText("There is an empty appointment field.");
                alert.setContentText("Please ensure that all fields contain a value and try again.");
                alert.showAndWait();
            } else {
                //Creating local date, time, and datetime variables for the combo boxes.
                LocalDate startDate = appointmentStartDate.getValue();
                LocalTime startTime = appointmentStartTime.getValue();
                startDT = LocalDateTime.of(startDate, startTime);

                LocalDate endDate = appointmentEndDate.getValue();
                LocalTime endTime = appointmentEndTime.getValue();
                endDT = LocalDateTime.of(endDate, endTime);
            }

            //assert endDT != null;
            if (startDT.isAfter(endDT) || startDT.isEqual(endDT)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Oops...");
                alert.setHeaderText("The start and end date times need to be checked.");
                alert.setContentText("The start time cannot be after the end time and the start and end time cannot " +
                        "start and end at the same times.");
                alert.show();
                return;
            }

            int customersID = appointmentCustomerID.getValue();

            ObservableList<Appointment> appointmentsToday = AppointmentDB.getFilteredAppointments(customersID);
            for (Appointment appointment : appointmentsToday) {
                if (startDT.isBefore(appointment.getAppointmentEndDateTime()) && endDT.isAfter(appointment.getAppointmentStartDateTime())) {
                    overlapping = true;
                    break;
                }
                if (endDT.isAfter(appointment.getAppointmentStartDateTime()) && startDT.isBefore(appointment.getAppointmentEndDateTime())) {
                    overlapping = true;
                    break;
                }
                if (startDT.isBefore(appointment.getAppointmentStartDateTime()) && endDT.isAfter(appointment.getAppointmentEndDateTime())) {
                    overlapping = true;
                    break;
                }
                if (startDT.isEqual(appointment.getAppointmentStartDateTime()) || endDT.isEqual(appointment.getAppointmentEndDateTime())) {
                    overlapping = true;
                    break;
                }
            }
            if (overlapping) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Oops...");
                alert.setContentText("Appointment times overlap with an existing appointment.");
                alert.show();
                return;
            }
                String title = appointmentTitle.getText();
                String description = appointmentDescription.getText();
                String location = appointmentLocation.getText();
                String contact = appointmentContact.getValue();
                String type = appointmentType.getValue();
                LocalDate startDate = appointmentStartDate.getValue();
                LocalDate endDate = appointmentEndDate.getValue();
                LocalTime startTime = appointmentStartTime.getValue();
                LocalTime endTime = appointmentEndTime.getValue();
                int customerID = appointmentCustomerID.getValue();
                int userID = appointmentUserID.getValue();


                AppointmentDB.addAppointment(title, description, location, type,
                        LocalDateTime.of(startDate, startTime), LocalDateTime.of(endDate, endTime), customerID, userID,
                        findContactID(contact));

                ObservableList<Appointment> updateAppointmentsList = AppointmentDB.getAllAppointments();
                allAppointmentsTable.setItems(updateAppointmentsList);

                System.out.println("Appointment added successfully!");

                clearAllFields();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /** When the clear button is pressed it clears all the input from the text fields and combo boxes. */
    public void onClearAppointmentBtn() {

        clearAllFields();
    }



    /** When the modify button is pressed it sends the information of the selected appointment to modify
     * controller. If no appointment was selected to modify there will be an error displayed.
     * @param actionEvent performs an action for the stage.
     * @throws IOException if there is an issue with the path.*/
    public void onModifyAppointmentBtn(ActionEvent actionEvent) throws IOException {
        try {

            Button stageCloseButton = new Button("stage.close();");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyAppointment.fxml"));
            loader.load();

            ModifyAppointmentController ModController = loader.getController();
            ModController.sendAppointment(allAppointmentsTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
            stageCloseButton.setOnAction(event -> stage.close());
        }

        catch (NullPointerException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error...");
            alert.setHeaderText("No appointment selected.");
            alert.setContentText("Please select an appointment to modify.");
            alert.showAndWait();
        }
    }


    /** When the weekly button is pressed the application will only show appointments with the current week and
     * disables the other buttons.*/
    public void onWeeklyAppointmentRadioBtn() {
        allAppointmentRadioBtn.setSelected(false);
        monthlyAppointmentRadioBtn.setSelected(false);

        try {
            ObservableList<Appointment> allAppointmentsList = AppointmentDB.getAllAppointments();
            ObservableList<Appointment> appointmentsByWeek = FXCollections.observableArrayList();

            LocalDateTime weekStart = LocalDateTime.now().minusWeeks(1);
            LocalDateTime weekEnd = LocalDateTime.now().plusDays(7);


            //Lambda expression to get appointments by week using a for each rather than a filtered data list.
            allAppointmentsList.forEach(appointment -> {
                if (appointment.getAppointmentEndDateTime().isAfter(weekStart) && appointment.getAppointmentEndDateTime().isBefore(weekEnd)) {
                    appointmentsByWeek.add(appointment);
                }
                allAppointmentsTable.setItems(appointmentsByWeek);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /** When the monthly button is pressed the application will only show appointments with the current month and
     * disables the other buttons.*/
    public void onMonthlyAppointmentRadioBtn() {
        allAppointmentRadioBtn.setSelected(false);
        weeklyAppointmentRadioBtn.setSelected(false);

        try {
            ObservableList<Appointment> allAppointmentsList = AppointmentDB.getAllAppointments();
            ObservableList<Appointment> appointmentsByMonth = FXCollections.observableArrayList();

            LocalDateTime monthStart = LocalDateTime.now().minusMonths(1);
            LocalDateTime monthEnd = LocalDateTime.now().plusMonths(1);


            allAppointmentsList.forEach(appointment -> {
                if (appointment.getAppointmentStartDateTime().isAfter(monthStart) && appointment.getAppointmentEndDateTime().isBefore(monthEnd)) {
                    appointmentsByMonth.add(appointment);
                }
                allAppointmentsTable.setItems(appointmentsByMonth);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /** When the all button is pressed the application will show all appointments on the table view. */
    public void onAllAppointmentRadioBtn() {
        weeklyAppointmentRadioBtn.setSelected(false);

        try {
            ObservableList<Appointment> allAppointmentsList = getAllAppointments();

            allAppointmentsList.forEach(appointment -> allAppointmentsTable.setItems(allAppointmentsList));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //Cancels the appointment that the customer has scheduled and sends a message
    /** When the cancel button is pressed, there will be a message that lets the user know that they are about to
     * cancel an existing appointment when a given appointment ID and type.
     * Then it will cancel the appointment and delete it from the table.
     * If no appointment is selected there will be an error message for the user to select an appointment to cancel. */
    public void onDeleteAppointmentBtn() {
        try {
            int cancelAppointmentID = allAppointmentsTable.getSelectionModel().getSelectedItem().getAppointmentID();
            String cancelAppointmentType = allAppointmentsTable.getSelectionModel().getSelectedItem().getAppointmentType();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are about to cancel the appointment with the " +
                    "ID of:" + " " + cancelAppointmentID + " and appointment type: " + cancelAppointmentType +
                    ". " + "Would you like to continue?");
            Optional<ButtonType> confirmation = alert.showAndWait();
            if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
                deleteAppointment(deleteAppointment(cancelAppointmentID));

                ObservableList<Appointment> allAppointmentsList = getAllAppointments();
                allAppointmentsTable.setItems(allAppointmentsList);

                System.out.println("Deleting appointment...");
            }

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error...");
            alert.setHeaderText("No appointment selected.");
            alert.setContentText("Please select an appointment to cancel.");
            alert.showAndWait();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    //Takes the user back to the main screen
    /** Displays a message confirming that the user would like to exit the current screen and go back to the main
     * screen. If the user confirms they will be taken to the main screen.
     * @param actionEvent performs an action for the stage.
     * @throws IOException if there is an issue with the path. */
    public void onBackModifyAppointmentBtn(ActionEvent actionEvent) throws IOException {
        System.out.println("Exiting...");
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

    /** Appointment toggle for the appointment radio buttons. */
    public void RadioToggleGroup() {

        ToggleGroup AppointmentToggle = new ToggleGroup();

        allAppointmentRadioBtn.setToggleGroup(AppointmentToggle);
        weeklyAppointmentRadioBtn.setToggleGroup(AppointmentToggle);
        monthlyAppointmentRadioBtn.setToggleGroup(AppointmentToggle);
    }
}
