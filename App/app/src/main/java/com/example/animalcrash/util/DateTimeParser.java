package com.example.animalcrash.util;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Datetime parser
 *              (Min API level was too low to use LocalDate function
 *              Therefore chosen to create a self made method)
 *
 */

public class DateTimeParser {

    private String dateTime;

    /**
     * Empty constructor
     */
    public DateTimeParser() {
    }

    /**
     * Constructor
     *
     * @param dateTime String       dateTime
     */
    public DateTimeParser(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Setter
     *
     * @param dateTime String   Date time to be set
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Get full date time
     *
     * @param withMonthName boolean     Get month name
     * @return dateTime     String
     */
    public String dateTimeParser(boolean withMonthName) {
        String dateTime;

        if (withMonthName) {
            dateTime = dateParser(true) + " - " + timeParser();
        } else {
            dateTime = dateParser(false) + " - " + timeParser();
        }

        return dateTime;
    }

    /**
     * Get full date
     *
     * @param withMonthName boolean     Get month name
     * @return fullDate     String
     */
    public String dateParser(boolean withMonthName) {
        String fullDate;

        if (withMonthName) {
            fullDate = getDay() + " " + getMonth(true) + " " + getYear();
        } else {
            fullDate = getDay() + "-" + getMonth(false) + "-" + getYear();
        }

        return fullDate;
    }

    /**
     * Get time
     *
     * @return time
     */
    public String timeParser() {
        String time = getHours() + ":" + getMinutes() + ":" + getSeconds();

        return time;
    }

    /**
     * Get day
     *
     * @return day      String
     */
    public String getDay() {
        String day = dateTime.substring(8, 10);

        return day;
    }

    /**
     * Get month (name)
     *
     * @param getMonthName boolean      Get month name
     * @return month       String
     */
    public String getMonth(boolean getMonthName) {
        String month = dateTime.substring(5, 7);

        if (getMonthName) {
            switch (month) {
                case "01":
                    month = "Januari";
                    break;
                case "02":
                    month = "Februari";
                    break;
                case "03":
                    month = "Maart";
                    break;
                case "04":
                    month = "April";
                    break;
                case "05":
                    month = "Mei";
                    break;
                case "06":
                    month = "Juni";
                    break;
                case "07":
                    month = "Juli";
                    break;
                case "08":
                    month = "Augustus";
                    break;
                case "09":
                    month = "September";
                    break;
                case "10":
                    month = "Oktober";
                    break;
                case "11":
                    month = "November";
                    break;
                case "12":
                    month = "December";
                    break;
                default:
                    month = "-";
            }
        }

        return month;
    }

    /**
     * Get year
     *
     * @return year     String
     */
    public String getYear() {
        String year = dateTime.substring(0, 4);

        return year;
    }

    /**
     * Get hours
     *
     * @return hours    String
     */
    public String getHours() {
        String hours = dateTime.substring(11, 13);

        return hours;
    }

    /**
     * Get minutes
     *
     * @return minutes    String
     */
    public String getMinutes() {
        String minutes = dateTime.substring(14, 16);

        return minutes;
    }

    /**
     * Get seconds
     *
     * @return seconds    String
     */
    public String getSeconds() {
        String seconds = dateTime.substring(17, 19);

        return seconds;
    }
}
