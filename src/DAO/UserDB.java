package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/** User database to retrieve all users. */
public class UserDB {
    /** @throws SQLException if there is an issue.
     * @return the list of users. */
    public static ObservableList<User> getAllUsers() throws SQLException {
            ObservableList<User> usersList = FXCollections.observableArrayList();

                String allUsersSQL = "SELECT * FROM users";
                PreparedStatement ps = JDBC.getConnection().prepareStatement(allUsersSQL);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int userID = rs.getInt("User_ID");
                    String username = rs.getString("User_Name");
                    String password = rs.getString("Password");

                    User user = new User (userID, username, password);
                    usersList.add(user);
                }
            return usersList;
        }
}
