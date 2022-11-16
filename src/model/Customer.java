package model;


/** Customer class for all customers. */
public class Customer {

    private int customerID;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhone;
    private String customerDivision;
    private int divisionID;
    private String customerCountry;


    /** @param customerID sets the ID of the customer.
     *  @param customerName sets the name of the customer.
     *  @param customerAddress sets the customer address.
     *  @param customerPostalCode sets the customer postal code.
     *  @param customerPhone sets the customer phone number.
     *  @param customerDivision sets the customer division based on the country selection.
     *  @param divisionID sets the customer division ID based on the division selection.
     *  @param customerCountry sets the customer country. */
    public Customer(int customerID, String customerName, String customerAddress, String customerPostalCode,
                   String customerPhone, String customerDivision, int divisionID, String customerCountry) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhone = customerPhone;
        this.customerDivision = customerDivision;
        this.divisionID = divisionID;
        this.customerCountry = customerCountry;
    }

    /**Getter for customerID,
     * @return the ID of the customer. */
    public int getCustomerID() {

        return customerID;
    }

    /** @param customerID Setter for the ID of the customer. */
    public void setCustomerID(int customerID) {

        this.customerID = customerID;
    }

    /** Getter for customerName,
     * @return the name of the customer. */
    public String getCustomerName() {

        return customerName;
    }

    /** @param customerName Setter for customerName. */
    public void setCustomerName(String customerName) {

        this.customerName = customerName;
    }

    /** Getter for customerAddress,
     * @return the address of the customer. */
    public String getCustomerAddress() {

        return customerAddress;
    }

    /** @param customerAddress Setter for customerAddress. */
    public void setCustomerAddress(String customerAddress) {

        this.customerAddress = customerAddress;
    }

    /** Getter for customerPostalCode,
     * @return the postal code of the customer. */
    public String getCustomerPostalCode() {

        return customerPostalCode;
    }

    /** @param customerPostalCode Setter for customerPostalCode. */
    public void setCustomerPostalCode(String customerPostalCode) {

        this.customerPostalCode = customerPostalCode;
    }

    /** Getter for customerPhone,
     * @return the phone number of the customer. */
    public String getCustomerPhone() {

        return customerPhone;
    }

    /** @param customerPhone Setter for customerPhone. */
    public void setCustomerPhone(String customerPhone) {

        this.customerPhone = customerPhone;
    }

    /** Getter for divisionID,
     * @return the ID of the selected division. */
    public int getDivisionID() {

        return divisionID;
    }

    /** @param divisionID Setter for divisionID. */
    public void setDivisionID(int divisionID) {

        this.divisionID = divisionID;
    }

    /** Getter for customerDivision,
     * @return the division of the selected customer. */
    public String getCustomerDivision() {

        return customerDivision;
    }

    /** @param customerDivision Setter for customerDivision. */
    public void setCustomerDivision(String customerDivision) {

        this.customerDivision = customerDivision;
    }

    /** Getter for customerCountry,
     * @return the country of the selected customer. */
    public String getCustomerCountry() {

        return customerCountry;
    }

    /** @param customerCountry Setter for customerCountry. */
    public void setCustomerCountry(String customerCountry) {

        this.customerCountry = customerCountry;
    }

}
