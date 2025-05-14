package viewBasic;

import exceptions.WrongFormatException;

public class DateValidatorFormatter {
    public String formatValidateDate(String month, String day, String year) throws WrongFormatException {
        int monthInt = Integer.parseInt(month);
        if (monthInt < 1 || monthInt > 12) {
            throw new WrongFormatException("Mese non valido: " + month);
        }

        int dayInt = Integer.parseInt(day);
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        int yearInt = Integer.parseInt(year);
        if (monthInt == 2 && ((yearInt % 4 == 0 && yearInt % 100 != 0) || (yearInt % 400 == 0))) {
            daysInMonth[1] = 29;
        }

        if (dayInt < 1 || dayInt > daysInMonth[monthInt - 1]) {
            throw new WrongFormatException("Giorno non valido: " + day + " per il mese " + month);
        }

        return day + "/" + month + "/20" + year;
    }
}
