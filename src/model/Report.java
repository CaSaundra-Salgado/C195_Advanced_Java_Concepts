package model;


public class Report {
    private String appointmentType;
    private Long appointmentDate;
    private Integer appointmentTotal;

    public Report(String appointmentType, Long appointmentDate, int appointmentTotal) {

        this.appointmentType = appointmentType;
        this.appointmentDate = appointmentDate;
        this.appointmentTotal = appointmentTotal;
    }

    public String getAppointmentType() {

        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {

        this.appointmentType = appointmentType;
    }

    public Long getAppointmentDate() {

        return appointmentDate;
    }

    public void setAppointmentDate(Long appointmentDate) {

        this.appointmentDate = appointmentDate;
    }

    public int getAppointmentTotal() {

        return appointmentTotal;
    }

    public void setAppointmentTotal(int appointmentTotal) {

        this.appointmentTotal = appointmentTotal;
    }
}
