package com.ntdat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateValidator {

    public static boolean Validate(String strDate) {
        if (strDate.trim().equals(""))
        {
            return false;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            try {
                LocalDate localDate = LocalDate.parse(strDate, formatter);
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }

    public static LocalDate toLocalDate(String strDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = null;
        try {
            localDate = LocalDate.parse(strDate, formatter);
        } catch (Exception e) {
        } finally {
            return localDate;
        }
    }
}
