package com.student;

public enum YearLevel {
    FIRST_YEAR,
    SECOND_YEAR,
    THIRD_YEAR,
    FOURTH_YEAR;

    @Override
    public String toString() {
        switch (this) {
            case FIRST_YEAR:  return "1st Year";
            case SECOND_YEAR: return "2nd Year";
            case THIRD_YEAR:  return "3rd Year";
            case FOURTH_YEAR: return "4th Year";
            default:          return "";
        }
    }

    /**
     * Converts a display string (e.g. "1st Year") back to its enum constant.
     * Returns null if not found.
     */
    public static YearLevel fromString(String text) {
        for (YearLevel y : values()) {
            if (y.toString().equals(text)) return y;
        }
        return null;
    }
}