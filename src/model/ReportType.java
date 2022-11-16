package model;

/** ReportType class for retrieving the types and total number of appointments with each type. */
public class ReportType {

    private String appointmentType;
    private int appointmentTotal;


    /** @param appointmentType object for the type of appointment.
     * @param appointmentTotal object for the total number of appointments for each type of appointment. */
    public ReportType(String appointmentType, int appointmentTotal) {

        this.appointmentType = appointmentType;
        this.appointmentTotal = appointmentTotal;
    }

    /** Getter for appointmentType,
     * @return the type of appointment. */
    public String getAppointmentType() {

        return appointmentType;
    }

    /** @param appointmentType Setter for the appointmentType. */
    public void setAppointmentType(String appointmentType) {

        this.appointmentType = appointmentType;
    }

    /** Getter for appointmentTotal,
     * @return the total number of appointments for each type.*/
    public int getAppointmentTotal() {

        return appointmentTotal;
    }

    /** @param appointmentTotal Setter for appointmentTotal. */
    public void setAppointmentTotal(int appointmentTotal) {

        this.appointmentTotal = appointmentTotal;
    }
}
