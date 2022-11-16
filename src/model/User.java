package model;

import DAO.UserDB;

/** User class for all users that extends from the user database from the User DAO package. */
public class User extends UserDB {
    private int userID;
    private String username;
    private String password;


    /** @param userID  sets the ID of the user.
     *  @param username sets the name of the user.
     *  @param password sets the password for the user.
     *  */
    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    /** Auto-Generated.
     * @param userID object for the user.
     * @param username object for the user. */
    public User(int userID, String username) {
    }

    /** Getter for userID,
     * @return the ID of the user. */
    public int getUserID() {

        return userID;
    }

    /** @param userID Setter for userID. */
    public void setUserID(int userID) {

        this.userID = userID;
    }

    /** Getter for userName,
     * @return the name of the user. */
    public String getUserName() {

        return username;
    }

    /** @param user_name Setter for username*/
    public void setUsername(String user_name) {

        this.username = username;
    }

    /** Getter for password,
     * @return the password of the user. */
    public String getPassword() {

        return password;
    }

    /** @param password Setter for password. */
    public void setPassword(String password) {

        this.password = this.password;
    }
}
