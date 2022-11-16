package DAO;

import helper.JDBC;
import helper.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Report;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AppointmentDB {
    public static ObservableList<Appointment> getFilteredAppointments(int customersID) throws SQLException {
        ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customersID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String appointmentTitle = rs.getString("Title");
            String appointmentDescription = rs.getString("Description");
            String appointmentLocation = rs.getString("Location");
            String appointmentType = rs.getString("Type");
            LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");
            Appointment appointment = new Appointment(appointmentID, appointmentTitle, appointmentDescription,
                    appointmentLocation, appointmentType, appointmentStart, appointmentEnd, customerID, userID,
                    contactID);
            filteredAppointments.add(appointment);
        }
        return filteredAppointments;
    }



    /**
     * Adds a new appointment to the database,
     * @param title object for adding a new appointment.
     * @param description object for adding a new appointment.
     * @param location object for adding a new appointment.
     * @param type object for adding a new appointment.
     * @param start object for the start date and time when adding a new appointment.
     * @param end object for end date and time when adding a new appointment.
     * @param customerID object for adding a new appointment.
     * @param userID object for adding a new appointment.
     * @param contactID object adding a new appointment.
     * @throws SQLException if there is an issue.
     * @return true or false based on if the appointment could be added or not.
     */
    public static boolean addAppointment(String title, String description,
                                         String location, String type,
                                         LocalDateTime start, LocalDateTime end,
                                         int customerID, int userID, int contactID) throws SQLException {

        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, " +
                "Created_By," +
                " Customer_ID, User_ID, Contact_ID) \n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, Timestamp.valueOf(start));
        ps.setTimestamp(6, Timestamp.valueOf(end));
        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(8, Logger.user_name);
        ps.setInt(9, customerID);
        ps.setInt(10, userID);
        ps.setInt(11, contactID);
        try {
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Modifies the selected appointment in the database.
     * @param appointmentID object for updating the appointment.
     * @param title object for updating the appointment.
     * @param description object for updating the appointment.
     * @param location object for updating the appointment.
     * @param type object for updating the appointment.
     * @param start object for updating the appointment.
     * @param end object for updating the appointment.
     * @param customerID object for updating the appointment.
     * @param userID object for updating the appointment.
     * @param contactID object for updating the appointment.
     * @throws SQLException if there is an issue.
     * @return either true of false based on if the appointment could be updated.
     */
    public static boolean updateAppointment(int appointmentID, String title, String description,
                                            String location, String type, LocalDateTime start, LocalDateTime end,
                                            int customerID, int userID, int contactID) throws SQLException {
        String sql = "UPDATE appointments "
                + "SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?," +
                " Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE " +
                "Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, Timestamp.valueOf(start));
        ps.setTimestamp(6, Timestamp.valueOf(end));
        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(8, Logger.user_name);
        ps.setInt(9, customerID);
        ps.setInt(10, userID);
        ps.setInt(11, contactID);
        ps.setInt(12, appointmentID);
        try {
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Deletes the selected appointment from the database by appointment ID.
     * @param appointmentID object for deleting the appointment.
     * @throws SQLException if there is an issue.
     * @return retrieves the appointmentID of the appointment to delete.
     */
    public static int deleteAppointment(int appointmentID) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentID);
        ps.executeUpdate();
        return appointmentID;
    }


    /**
     * Retrieves all appointments in the database.
     * @throws SQLException if there is an issue.
     * @return the list of appointments.
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments";
        PreparedStatement ps = JDBC.connection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String appointmentTitle = rs.getString("Title");
            String appointmentDescription = rs.getString("Description");
            String appointmentLocation = rs.getString("Location");
            String appointmentType = rs.getString("Type");
            LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");
            Appointment appointment = new Appointment(appointmentID, appointmentTitle, appointmentDescription,
                    appointmentLocation, appointmentType, appointmentStart, appointmentEnd, customerID, userID,
                    contactID);
            appointmentsList.add(appointment);
        }
        return appointmentsList;
    }


    /**
     * Finds the contact ID based on the selected contact name.
     * @param contact object for the contact name and matching it with the contact ID.
     * @throws SQLException if there is an issue.
     * @return the ID of the contact.
     */
    public static Integer findContactID(String contact) throws SQLException {
        // take user selected name and find the FK to can add to appointments table.
        int contactID = 0;
        PreparedStatement sql = JDBC.connection().prepareStatement("SELECT Contact_ID, Contact_Name " +
                "FROM contacts WHERE Contact_Name = ?");
        sql.setString(1, contact);
        ResultSet results = sql.executeQuery();

        while (results.next()) {
            contactID = results.getInt("Contact_ID");
        }
        sql.close();
        return contactID;
    }

    public static ArrayList generateTypeMonthReport() {
        try(Connection connection = JDBC.connection()) {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("Select month(start) as month, type, count(*) as total from appointments GROUP BY month, type;");

            ArrayList<Report> typeMonthReport = new ArrayList<>();

            while(results.next()) {
                Report row = new Report(results.getString("type"), results.getLong("month"), results.getInt("total"));
                typeMonthReport.add(row);
            }

            return typeMonthReport;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return new ArrayList<>();
    }
}
