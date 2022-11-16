package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.*;

/** Time manager class to create a list of appointment times using a for loop for the start and end time combo boxes. */
public class TimeManager {
    public static ObservableList<LocalTime> getTimes(int offset, int max) {
        ObservableList<LocalTime> businessHours = FXCollections.observableArrayList();
        LocalDateTime now = LocalDateTime.of(LocalDate.now(),LocalTime.of(8,0));
        ZoneId zId = ZoneId.of("America/New_York");
        ZonedDateTime estZDT = ZonedDateTime.of(now, zId);
        ZoneId localZId = ZoneId.systemDefault();
        ZonedDateTime localZDT = ZonedDateTime.ofInstant(estZDT.toInstant(),localZId);
        //System.out.println(localZDT);
        int startTime = localZDT.getHour();
        int midnight = 0;
        for (int i = offset+startTime; i < startTime + max; i++) {
            if (i < 24) {
                businessHours.add(LocalTime.of(i, 0));
            }
            else {
                businessHours.add(LocalTime.of(midnight, 0));
                midnight += 1;
            }
        }
        return businessHours;
    }

}
