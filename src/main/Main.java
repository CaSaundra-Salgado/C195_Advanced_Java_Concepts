package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;



/** Main class for the application. */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        stage.setTitle("Appointment Management Application");
        stage.setScene(new Scene(root));
        stage.show();
    }


    /** Executes the application.
     * @param args an array of arguments.
     * @throws SQLException if there is an issue. */
    public static void main(String[] args) throws SQLException {
        JDBC.startConnection();
        launch(args);
        JDBC.closeConnection();
    }
}