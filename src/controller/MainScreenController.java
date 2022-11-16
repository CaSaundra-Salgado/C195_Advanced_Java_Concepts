package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/** Main screen controller for the logged on user.
 *
 * @author CaSaundra Salgado. */
public class MainScreenController {

    /** Takes the user to the customer view.
     * @param actionEvent performs an action for the stage.
     * @throws IOException is there is an issue with the path/location. */
    public void onFirstScreenCustomerBtn(ActionEvent actionEvent) throws IOException {

            Button stageCloseButton = new Button("stage.close();");
            Parent root = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
            Scene scene = new Scene(root, 900, 500);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stageCloseButton.setOnAction(event -> stage.close());
    }

    /** Takes the user to the appointment view.
     * @param actionEvent performs an action for the stage.
     * @throws IOException if there is an issue with the path/location. */
    public void onFirstScreenAppointmentBtn(ActionEvent actionEvent) throws IOException {

        Button stageCloseButton = new Button("stage.close();");
        Parent root = FXMLLoader.load(getClass().getResource("/view/Appointment.fxml"));
        Scene scene = new Scene(root, 1000, 600);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        stageCloseButton.setOnAction(event -> stage.close());
    }

    /** Takes the user to the report view.
     * @param actionEvent performs an action for the stage.
     * @throws IOException if there is an issue with the path/location. */
    public void onFirstScreenReportsBtn(ActionEvent actionEvent) throws IOException {

        Button stageCloseButton = new Button("stage.close();");
        Parent root = FXMLLoader.load(getClass().getResource("/view/Report.fxml"));
        Scene scene = new Scene(root, 900, 500);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        stageCloseButton.setOnAction(event -> stage.close());
    }

    /** Takes the user back to the login screen.
     * @param actionEvent performs an action for the stage.
     * @throws IOException if there is an issue with the path/location. */
    public void onFirstScreenExitBtn(ActionEvent actionEvent) throws IOException {
        System.out.println("Exiting...");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are about to exit this screen and be redirected to the LoginController screen. Would you like to continue?");

        Button stageCloseButton = new Button("stage.close();");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Scene scene = new Scene(root, 600, 400);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stageCloseButton.setOnAction(event -> stage.close());
        }
    }
}
