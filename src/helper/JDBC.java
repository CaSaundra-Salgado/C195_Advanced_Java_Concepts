package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** JDBC class for the database. */
public abstract class JDBC {
    private static PreparedStatement preparedStatement;
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String username = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  //Connection Interface


    /** Starts the connection to the database and prints if the connection is successful.
     * @return the connection.*/
    public static Connection startConnection() {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, username, password); // Reference Connection object
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e)    //If driver cannot be found.
        {
            e.printStackTrace();
        }
        return connection;
    }


    /** Closes the connection to the database and prints a message. */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }


    /** Getter for the connection.
     * @return the connection.*/
    public static Connection getConnection() {

        return connection;
    }

    /** @return the connection. */
    public static Connection connection() {

        return connection;
    }

    /** Setter for preparedStatement.
     * @param connection object for the prepared statement.
     * @param sqlStatement object for the prepared statement.
     * @throws SQLException if there is an issue. */
    public static void setPreparedStatement(Connection connection, String sqlStatement) throws SQLException {

        preparedStatement = connection.prepareStatement(sqlStatement);
    }

    /** Getter for preparedStatement,
     * @return the prepared statement. */
    public static PreparedStatement getPreparedStatement() {

        return preparedStatement;
    }
}