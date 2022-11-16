package model;

/** Contact class for all contacts. */
public class Contact {

    private int contactID;
    private String contactName;
    private String contactEmail;

    /** @param contactID sets the contact ID.
     * @param contactName sets the contact.
     * @param contactEmail sets the contact email.
     * */
    public Contact(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /** Getter for contactID,
     * @return the ID of the contact.*/
    public int getContactID() {

        return contactID;
    }

    /** @param contactID Setter for contactID. */
    public void setContactID(int contactID) {

        this.contactID = contactID;
    }

    /** Getter for contactName,
     * @return the first and last name of the Contact. */
    public String getContactName() {

        return contactName;
    }

    /** @param contactName Setter for contactName. */
    public void setContactName(String contactName) {

        this.contactName = contactName;
    }

    /** Getter for contactEmail,
     * @return the email address for contactName. */
    public String getContactEmail() {

        return contactEmail;
    }

    /** @param contactEmail Setter for contactEmail. */
    public void setContactEmail(String contactEmail) {

        this.contactEmail = contactEmail;
    }
}
