package DAO;

import model.FirstLevelDivision;


/** First level division class for database that extends from the first level division model. */
public class FirstLevelDivisionDB extends FirstLevelDivision {

    /** @param divisionID object for the divisions.
     *  @param division object for the divisions.
     *  @param countryID object for the divisions. */
    public FirstLevelDivisionDB(int divisionID, String division, int countryID) {
        super(divisionID, division, countryID);
    }
}
