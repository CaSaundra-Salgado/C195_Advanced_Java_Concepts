package controller;

import DAO.AppointmentDB;
import helper.Logger;
import helper.LogonSession;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import static helper.Logger.userLogin;
import static java.util.logging.Logger.getLogger;


/** Login class to check the username and password and to see if they have an appointment in the next 15 minutes. */
public class LoginController implements Initializable {
    ObservableList<User> usersList;
    //all labels for the controller
    @FXML private Label headerLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;

    //text fields
    @FXML private PasswordField passwordTextField;
    @FXML private TextField usernameTextField;
    @FXML private  TextField locationTextField;


    //buttons
    @FXML private Button loginBtn;
    @FXML private Button cancelLoginBtn;

    //used to show a message when the user enters the wrong username and/or password in either English or French.
    private String failedLogin;
    private String headerText;


    /**Clears the input fields. I had to comment out clearing the location field because I want it to show even when
     *
     * there is a failed login. */
    public void clearAllFields() {
        usernameTextField.setText("");
        passwordTextField.setText("");
        //locationTextField.setText("");
    }

    /** Checks to see if the logged on user has an appointment in the next 15 minutes.
     * @param actionEvent performs an action for the stage.
     * @throws IOException if there is an issue with the path/location.
     * @throws SQLException if there is an issue.*/
    public void onLoginBtn(ActionEvent actionEvent) throws IOException, SQLException {
        ObservableList<Appointment> getAllAppointments = AppointmentDB.getAllAppointments();
        LocalDateTime minus15MinutesFromTime = LocalDateTime.now().minusMinutes(15);
        LocalDateTime add15MinutesToTime = LocalDateTime.now().plusMinutes(15);
        LocalDateTime startTime;
        int getAppointmentID = 0;
        LocalDateTime appointmentStartingTime = null;
        boolean appointmentIn15Minutes = false;

        ResourceBundle rb = ResourceBundle.getBundle("language/login", Locale.getDefault());


        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        java.util.logging.Logger log = getLogger("user_activity.txt");

        try {
            FileHandler filehandler = new FileHandler("user_activity.txt", true);
            SimpleFormatter simpleformatter = new SimpleFormatter();
            filehandler.setFormatter(simpleformatter);
            log.addHandler(filehandler);

        } catch (IOException | SecurityException ex) {
            getLogger(Logger.class.getName()).log(Level.INFO, "This shows user activity information.");
        }

        boolean logon = LogonSession.attemptLogon(username, password);

        userLogin(username, logon);
        boolean match = false;
        for (User u : usersList) {
            if (u.getUserName().equals(username) && u.getPassword().equals(password)) {
                match = true;
                break;
            }
        }
        if (match) {
            Logger.user_name = username;

                Button stageCloseButton = new Button("stage.close();");
                Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
                stageCloseButton.setOnAction(event -> stage.close());


            for (Appointment appointment: getAllAppointments) {
                startTime = appointment.getAppointmentStartDateTime();
                if ((startTime.isAfter(minus15MinutesFromTime) || startTime.isEqual(minus15MinutesFromTime)) && (startTime.isBefore(add15MinutesToTime) || (startTime.isEqual(add15MinutesToTime)))) {
                    getAppointmentID = appointment.getAppointmentID();
                    appointmentStartingTime = startTime;
                    appointmentIn15Minutes = true;
                }
            }
            if (appointmentIn15Minutes) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "There is an appointment within 15 minutes with an appointment ID of: " + getAppointmentID +
                                " and a start time of: " + appointmentStartingTime);
                Optional<ButtonType> confirmation = alert.showAndWait();
                System.out.println("There is an appointment within 15 minutes");
            }

            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "No upcoming appointments.");
                alert.setTitle("No upcoming appointments...");
                alert.setHeaderText("No appointments.");
                alert.setContentText("There are no upcoming appointment in the next 15 minutes.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error...");
            alert.setHeaderText(headerText);
            alert.setContentText(failedLogin);
            alert.showAndWait();
        }
        clearAllFields();
    }


    /** If the user does not want to log in they press the cancel button and are taken back to the main screen. */
    public void onCancelLoginBtn() {
        System.out.println("Exiting...");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are about to exit the application. Would you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            {
                System.exit(0);
            }
        }
    }


    /** Initializes the controller and sets the language depending on the user's language settings.
     *
     * Also retrieves all users.
     *
     * @param url is used for the path/location.
     *
     * @param resourceBundle is used to store texts and data that are locale sensitive.
     * */
    //Had to add the last line so that it shows the zone id on the login page.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            usersList = User.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResourceBundle rb = ResourceBundle.getBundle("language/login", Locale.getDefault());
        headerLabel.setText(rb.getString("headerLabel"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginBtn.setText(rb.getString("loginButton"));
        cancelLoginBtn.setText(rb.getString("cancelButton"));
        failedLogin = rb.getString("failedLogin");
        headerText = rb.getString("headerText");
        locationTextField.setText(ZoneId.systemDefault().toString());
    }
}
