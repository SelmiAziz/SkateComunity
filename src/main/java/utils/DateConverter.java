package utils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateConverter {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public  String localDateToString(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(formatter);
    }


    public LocalDate stringToLocalDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateStr, formatter);
    }
}
