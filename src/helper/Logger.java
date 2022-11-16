package helper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/** Logger class to log user activity and store information in the designated text file. */
public class Logger {
    private static final String user_login = "user_activity.txt";

    public static String user_name;

    /** Logs the user's login attempts and displays in UTC.
     * @param username object for user login.
     * @param successBool object for user login.*/
    public static void userLogin(String username, Boolean successBool) {
        try {
            BufferedWriter logger = new BufferedWriter(new FileWriter(user_login, true));
            logger.append(String.valueOf(ZonedDateTime.now(ZoneOffset.UTC))).append(" UTC-Login Attempt-USERNAME: ").append(username).append(" LOGIN SUCCESSFUL: ").append(successBool.toString()).append("\n");
            logger.flush();
            logger.close();
        }
        catch (IOException error) {
            error.printStackTrace();
        }

    }
}
