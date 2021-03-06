package xyz.malkki.gtfsroutefinder.gtfs.model.core;

import xyz.malkki.gtfsroutefinder.gtfs.utils.GTFSDateParser;
import xyz.malkki.gtfsroutefinder.gtfs.utils.GTFSParser;
import xyz.malkki.gtfsroutefinder.gtfs.utils.GTFSTimeParser;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Describes GTFS calendar type, see <a href="https://developers.google.com/transit/gtfs/reference/#calendartxt">GTFS reference</a>
 */
public class Calendar {
    private String serviceId;
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private boolean sunday;
    private LocalDate startDate;
    private LocalDate endDate;

    public Calendar(String serviceId, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday, LocalDate startDate, LocalDate endDate) {
        this.serviceId = serviceId;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getServiceId() {
        return serviceId;
    }

    public boolean isMonday() {
        return monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calendar calendar = (Calendar) o;
        return monday == calendar.monday &&
                tuesday == calendar.tuesday &&
                wednesday == calendar.wednesday &&
                thursday == calendar.thursday &&
                friday == calendar.friday &&
                saturday == calendar.saturday &&
                sunday == calendar.sunday &&
                Objects.equals(serviceId, calendar.serviceId) &&
                Objects.equals(startDate, calendar.startDate) &&
                Objects.equals(endDate, calendar.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, monday, tuesday, wednesday, thursday, friday, saturday, sunday, startDate, endDate);
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "serviceId='" + serviceId + '\'' +
                ", monday=" + monday +
                ", tuesday=" + tuesday +
                ", wednesday=" + wednesday +
                ", thursday=" + thursday +
                ", friday=" + friday +
                ", saturday=" + saturday +
                ", sunday=" + sunday +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public static List<Calendar> parseFromFile(String file) throws IOException {
        return GTFSParser.parseFromFile(file, (record, headers) -> {
            String serviceId = record.get(headers.get("service_id"));
            boolean monday = "1".equals(record.get(headers.get("monday")));
            boolean tuesday = "1".equals(record.get(headers.get("tuesday")));
            boolean wednesday = "1".equals(record.get(headers.get("wednesday")));
            boolean thursday = "1".equals(record.get(headers.get("thursday")));
            boolean friday = "1".equals(record.get(headers.get("friday")));
            boolean saturday = "1".equals(record.get(headers.get("saturday")));
            boolean sunday = "1".equals(record.get(headers.get("sunday")));
            LocalDate startDate = GTFSDateParser.parseDate(record.get(headers.get("start_date")));
            LocalDate endDate = GTFSDateParser.parseDate(record.get(headers.get("end_date")));

            return new Calendar(serviceId, monday, tuesday, wednesday, thursday, friday, saturday, sunday, startDate, endDate);
        });
    }
}
