package model;

/** ReportMonth class for retrieving the months and total number of appointments in those months. */
public class ReportMonth {

    private String appointmentMonth;
    private int appointmentTotal;

    /** @param appointmentMonth object for the appointment month.
     * @param appointmentTotal object for the total number of appointments for the months. */
    public ReportMonth(String appointmentMonth, int appointmentTotal) {

        this.appointmentMonth = appointmentMonth;
        this.appointmentTotal = appointmentTotal;
    }

    /** Getter for appointmentMonth,
     * @return the month of the appointment. */
    public String getAppointmentMonth() {

        return appointmentMonth;
    }

    /** @param appointmentMonth Setter for appointmentMonth. */
    public void setAppointmentMonth(String appointmentMonth) {

        this.appointmentMonth = appointmentMonth;
    }

    /** Getter for appointmentTotal,
     * @return the total number of appointments for each month with an appointment. */
    public int getAppointmentTotal() {

        return appointmentTotal;
    }

    /** @param appointmentTotal Setter for appointmentTotal. */
    public void setAppointmentTotal(int appointmentTotal) {

        this.appointmentTotal = appointmentTotal;
    }

}
