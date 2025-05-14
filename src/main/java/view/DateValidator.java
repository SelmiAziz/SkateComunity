package view;

import exceptions.WrongFormatException;

public class DateValidator {

    public void validaDate(String data) throws WrongFormatException {
        if (data == null || !data.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
            throw new WrongFormatException("Formato non valido (deve essere dd/mm/yyyy)");
        }

        String[] parts = data.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        if (month < 1 || month > 12) {
            throw new WrongFormatException("Mese non valido: " + month);
        }

        int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        if (month == 2 && ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))) {
            daysInMonth[1] = 29;
        }

        if (day < 1 || day > daysInMonth[month - 1]) {
            throw new WrongFormatException("Giorno non valido: " + day + " per il mese " + month);
        }
    }
}
