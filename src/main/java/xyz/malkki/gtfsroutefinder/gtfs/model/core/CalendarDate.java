package xyz.malkki.gtfsroutefinder.gtfs.model.core;

import xyz.malkki.gtfsroutefinder.gtfs.utils.GTFSDateParser;
import xyz.malkki.gtfsroutefinder.gtfs.utils.GTFSParser;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

//https://developers.google.com/transit/gtfs/reference/#calendar_datestxt
public class CalendarDate {
    private String serviceId;
    private LocalDate date;
    private boolean available;

    public CalendarDate(String serviceId, LocalDate date, boolean available) {
        this.serviceId = serviceId;
        this.date = date;
        this.available = available;
    }

    public String getServiceId() {
        return serviceId;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarDate that = (CalendarDate) o;
        return available == that.available &&
                Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, date, available);
    }

    @Override
    public String toString() {
        return "CalendarDate{" +
                "serviceId='" + serviceId + '\'' +
                ", date=" + date +
                ", available=" + available +
                '}';
    }

    public static List<CalendarDate> parseFromFile(String file) throws IOException {
        return GTFSParser.parseFromFile(file, record -> {
            String serviceId = record.get("service_id");
            LocalDate date = GTFSDateParser.parseDate(record.get("date"));
            boolean available = "1".equals(record.get("exception_type"));

            return new CalendarDate(serviceId, date, available);
        });
    }
}
