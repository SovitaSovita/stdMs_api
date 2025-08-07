package com.example.telegram_api.common;

import com.example.telegram_api.model.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Year;

public final class CommonUtils {

    public static boolean isInteger(String str) {
        return str != null && str.matches("-?\\d+");
    }
    public static boolean isDecimal(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isValidYear(String str) {
        try {
            int year = Integer.parseInt(str);
            int currentYear = Year.now().getValue();
            return year >= 2020 && year <= currentYear;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }
    public static boolean isValidGender(String gender) {
        try {
            Gender.valueOf(gender); // throws if not M or F
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

}
