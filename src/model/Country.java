package model;

/** Country class for all countries. */
public class Country {

    private int countryID;
    private String countryName;

    /** @param countryID sets the ID of the country.
     *  @param countryName sets the name of the country.
     */
    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    /** Getter for countryID,
     * @return the ID of the country selected. */
    public int getCountryID() {

        return countryID;
    }

    /** @param countryID Setter for setting the countryID. */
    public void setCountryID(int countryID) {

        this.countryID = countryID;
    }

    /** Getter for countryName,
     * @return the name of the selected Country. */
    public String getCountryName() {

        return countryName;
    }

    /** @param countryName Setter for setting the name of the Country.*/
    public void setCountryName(String countryName) {

        this.countryName = countryName;
    }
}
