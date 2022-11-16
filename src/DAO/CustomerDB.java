package DAO;

import helper.JDBC;
import helper.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;


/** Customer database class for the customers in the database. */
public class CustomerDB {

    /** Adding a new customer to the database,
     * @param customerName object for adding a new customer.
     * @param customerAddress object for adding a new customer.
     * @param customerPostalCode object for adding a new customer.
     * @param customerPhone object for adding a new customer.
     * @param divisionID object for adding a new customer.
     * @throws SQLException if there is an issue.
     * @return either true or false based on if the customer could be added or not.*/
            public static boolean addCustomer (String customerName, String customerAddress, String customerPostalCode,
                                               String customerPhone, int divisionID) throws SQLException {
                String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, " +
                        "Division_ID) \n" +
                        "VALUES(?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                ps.setString(1, customerName);
                ps.setString(2, customerAddress);
                ps.setString(3, customerPostalCode);
                ps.setString(4, customerPhone);
                ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(6, Logger.user_name);
                ps.setInt(7, divisionID);

                try {
                    ps.executeUpdate();
                    return true;
                }
                catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }


        /** Modifying the selected customer in the database.
         * @param customerID object for updating a customer
         * @param customerName object for updating a customer.
         * @param customerAddress object for updating a customer.
         * @param customerPostalCode object for updating a customer.
         * @param customerPhone object for updating a customer.
         * @param customerDivision object for updating a customer.
         * @throws SQLException if there is an issue.
         * @return either true or false based on if the customer could be updated or not.*/
        public static Boolean updateCustomer (int customerID, String customerName, String customerAddress,
                                              String customerPostalCode, String customerPhone, String customerDivision)
                                              throws SQLException {
            String sql = "UPDATE customers "
                + "SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?," +
                " Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, customerName);
            ps.setString(2, customerAddress);
            ps.setString(3, customerPostalCode);
            ps.setString(4, customerPhone);
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(6, Logger.user_name);
            ps.setInt(7, CustomerDB.getSpecificDivisionID(customerDivision));
            ps.setInt(8, customerID);
            //ps.executeUpdate();
            try {
                ps.executeUpdate();
                return true;
            }
            catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        /** Deleting the selected customer in the database.
         * @param customerID object for deleting the appointment.
         * @throws SQLException if there is an issue.
         * @return retrieves the customerID to be deleted. */
        public static int deleteCustomer (int customerID) throws SQLException {
            String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customerID);
            ps.executeUpdate();
            return customerID;
        }


        /** Retrieving all customers in the database.
         * @throws SQLException if there is an issue.
         * @return the list of customers. */
        public static ObservableList<Customer> getAllCustomers() throws SQLException {
            ObservableList<Customer> customersList = FXCollections.observableArrayList();

            PreparedStatement sql = JDBC.connection().prepareStatement(
                    "SELECT cx.Customer_ID, cx.Customer_Name, cx.Address, cx.Postal_Code, cx.Phone, cx.Division_ID, " +
                            "f.Division, f.COUNTRY_ID, co.Country FROM customers as cx INNER JOIN first_level_divisions " +
                            "as f on cx.Division_ID = f.Division_ID INNER JOIN countries as co ON f.COUNTRY_ID = co.Country_ID");
            ResultSet results = sql.executeQuery();


            while (results.next()) {
                int customerID = results.getInt("Customer_ID");
                String customerName = results.getString("Customer_Name");
                String customerAddress = results.getString("Address");
                String customerPostalCode = results.getString("Postal_Code");
                String customerPhoneNum = results.getString("Phone");
                String customerDivision = results.getString("Division");
                int divisionID = results.getInt("Division_ID");
                String customerCountry = results.getString("Country");

                Customer newCustomer = new Customer(customerID, customerName, customerAddress, customerPostalCode,
                        customerPhoneNum, customerDivision,
                        divisionID, customerCountry);

                customersList.add(newCustomer);
            }
            return customersList;
        }


        /** Retrieving all countries stored in the database.
         * @throws SQLException if there is an issue.
         * @return the list of countries.*/
        public static ObservableList<String> getAllCountries() throws SQLException {

        ObservableList<String> allCountries = FXCollections.observableArrayList();
        PreparedStatement sql = JDBC.connection().prepareStatement("SELECT DISTINCT Country FROM countries");
        ResultSet results = sql.executeQuery();

        while (results.next()) {
            allCountries.add(results.getString("Country"));
        }
        sql.close();
        return allCountries;
    }

        /** Filtering the divisions based on the selected country.
         * @param country object for filtering through countries.
         * @throws SQLException if there is an issue.
         * @return the filtered divisions.*/
        public static ObservableList<String> getFilteredDivisions(String country) throws SQLException {

        ObservableList<String> filteredDivisions = FXCollections.observableArrayList();
        PreparedStatement sql = JDBC.connection().prepareStatement(
                "SELECT c.Country, c.Country_ID,  d.Division_ID, d.Division FROM countries as c RIGHT OUTER JOIN " +
                        "first_level_divisions AS d ON c.Country_ID = d.Country_ID WHERE c.Country = ?");

        sql.setString(1, country);
        ResultSet results = sql.executeQuery();

        while (results.next()) {
            filteredDivisions.add(results.getString("Division"));
        }
        return filteredDivisions;
    }


        /** Getting the division ID based on the selected first level division.
         * @param division object for retrieving the division name to display that division ID.
         * @throws SQLException if there is an issue.
         * @return the division ID based on the division name.*/
        public static Integer getSpecificDivisionID(String division) throws SQLException {
            int divisionID = 0;
            PreparedStatement sql = JDBC.connection().prepareStatement("SELECT Division_ID FROM " +
                    "first_level_divisions WHERE Division = ?");

            sql.setString(1, division);

            ResultSet result = sql.executeQuery();

            while ( result.next() ) {
                divisionID = result.getInt("Division_ID");
            }
            return divisionID;

        }
}
