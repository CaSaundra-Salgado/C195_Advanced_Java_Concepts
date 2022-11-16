package helper;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;


/** LogonSession class for tracking the user information when they attempt to log in. */
public class LogonSession {
    private static User loggedOnUser;
    private static Locale userLocale;
    private static ZoneId userTimeZone;

    /** Auto-Generated. */
    public LogonSession() {

    }

    /** Static boolean to capture the user's login attempt in the database.
     *
     * Matches the user's username to the password entered.
     * @param username object for login attempt.
     * @param password object for login attempt.
     * @throws SQLException if there is an issue.
     * @return either true or false based on the success of the login attempt.*/
    public static boolean attemptLogon(String username, String password) throws SQLException {
        Connection connection = JDBC.connection();
        PreparedStatement sqlCommand = connection.prepareStatement("SELECT * FROM users WHERE " +
                "User_Name = ? AND Password = ?");
        sqlCommand.setString(1, username);
        sqlCommand.setString(2, password);
        ResultSet result = sqlCommand.executeQuery();
        if (!result.next()) {
            sqlCommand.close();
            return false;
        }
        else {
            loggedOnUser = new User(result.getInt("User_ID"), result.getString("User_Name"));
            userLocale = Locale.getDefault();
            userTimeZone = ZoneId.systemDefault();
            sqlCommand.close();
            return true;

        }
    }

    /** Getter for loggedOnUser,
     * @return the logged on user. */
    public static User getLoggedOnUser() {

        return loggedOnUser;
    }

    /** Getter for userLocale,
     * @return the user locale. */
    public static Locale getUserLocale() {

        return userLocale;
    }

    /** Getter for the zone ID of the user,
     * @return the user's time zone. */
    public static ZoneId getUserTimeZone() {

        return userTimeZone;
    }

}
