package model;


/** First Level Division class for the divisions. */
public class FirstLevelDivision  {

    private int divisionID;

    private String division;

    private int countryID;

    /** @param divisionID sets the division ID based on the division selection.
     * @param division sets the division based on the country selection.
     * @param countryID sets the country ID. */
    public FirstLevelDivision(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }

    /** Getter for divisionID,
     * @return the ID of the division. */
    public int getDivisionID() {

        return divisionID;
    }

    /** @param divisionID Setter for divisionID. */
    public void setDivisionID(int divisionID) {

        this.divisionID = divisionID;
    }

    /** Getter for division,
     * @return the name of the division. */
    public  String getDivision() {

        return division;
    }

    /** @param division Setter for the name of the division. */
    public void setDivision(String division) {

        this.division = division;
    }

    /** Getter for countryID,
     * @return the ID of the country. */
    public int getCountryID() {

        return countryID;
    }

    /** @param countryID Setter for countryID. */
    public void setCountryID(int countryID) {

        this.countryID = countryID;
    }
}
