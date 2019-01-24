package xyz.malkki.gtfsroutefinder.gtfs.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServiceDates {
    private String serviceId;
    private LocalDate from;
    private LocalDate to;
    private Set<DayOfWeek> days;
    private Set<LocalDate> additions;
    private Set<LocalDate> exceptions;

    public ServiceDates(String serviceId, LocalDate from, LocalDate to,
                        boolean monday, boolean tuesday, boolean wednesday,
                        boolean thursday, boolean friday, boolean saturday, boolean sunday,
                        List<LocalDate> additions, List<LocalDate> exceptions) {
        this.serviceId = serviceId;
        this.from = from;
        this.to = to;

        days = new HashSet<>(7);
        if (monday) {
            days.add(DayOfWeek.MONDAY);
        }
        if (tuesday) {
            days.add(DayOfWeek.TUESDAY);
        }
        if (wednesday) {
            days.add(DayOfWeek.WEDNESDAY);
        }
        if (thursday) {
            days.add(DayOfWeek.THURSDAY);
        }
        if (friday) {
            days.add(DayOfWeek.FRIDAY);
        }
        if (saturday) {
            days.add(DayOfWeek.SATURDAY);
        }
        if (sunday) {
            days.add(DayOfWeek.SUNDAY);
        }

        this.additions = new HashSet<>();
        this.additions.addAll(additions);

        this.exceptions = new HashSet<>();
        this.exceptions.addAll(exceptions);
    }

    private String getServiceId() {
        return serviceId;
    }

    /**
     * Checks if the service runs on the specified date
     * @param date Date
     * @return true if the service is running
     */
    public boolean runsOn(LocalDate date) {
        if (additions.contains(date)) { //If the additional dates list contains the service, it runs regardless of other rules
            return true;
        } else {
            return days.contains(date.getDayOfWeek()) && //Check if the day of week is same
                    (date.isEqual(from) || date.isAfter(from)) && //Check if the date is in correct range
                    (date.isEqual(to) || date.isBefore(to)) &&
                    !exceptions.contains(date); //Check that the date is not on the exception list
        }
    }
}
