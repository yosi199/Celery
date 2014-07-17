package utilities;

import android.text.format.DateUtils;

import java.util.Date;

public enum Utility {
    INSTANCE;

    static int HOURS_TIME_UNIT = 24;
    static int MINUTES_TIME_UNIT = 60;

    String date = "";
    String duration = "";

    public void setDate(String s) {

        date = s;
    }

    public String getDate() {

        return this.date;

    }

    public void setDuration(String s) {

        duration = s;
    }

    public String getDuration() {

        return this.duration;

    }

    public static int getHoursTime() {
        Date date = new Date();
        return date.getHours();

    }

    public static int getMinutesTime() {
        Date date = new Date();
        int minutes = date.getMinutes();

        return minutes;

    }

    public static String formatTime(long seconds) {
        return DateUtils.formatElapsedTime(seconds);
    }

    public static String getDay(int z) {
        String day = "";

        switch (z) {

            case 1:
                day = "Sunday";
                break;
            case 2:
                day = "Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            case 7:
                day = "Saturday";
                break;

        }

        return day;
    }

    public double calcShiftMoney(double Seconds, double perHour) {

        double hours = returnHours(Seconds);
        double minutes = returnMinutes(Seconds);
        double seconds = returnSeconds(Seconds);

        // Get money for hours
        double moneyForHours = (hours * perHour);

        // Here we calculate how many percent of the amount payed per hour we will get for the minutes
        double MinutesPercentage = (minutes * 100) / 60;

        // Now get the money for minutes
        double moneyForMinutes = (MinutesPercentage * perHour) / 100;

        // Find how much money you get per second
        double moneyPerSecond = (perHour / 60) / 60;
        // Find how much money you made for all the seconds in the shift
        double moneyForSeconds = seconds * moneyPerSecond;
        // Total money for shift
        double money = moneyForHours + moneyForMinutes + moneyForSeconds;
        return money;
    }

    private double returnHours(double seconds) {
        double Hours;
        double reminder = seconds % 3600;
        double hoursOnly = seconds - reminder;
        Hours = hoursOnly / 3600;
        return Hours;
    }

    private double returnMinutes(double seconds) {
        double minutes;
        double reminder = (seconds % 3600) % 60;
        double minutesOnly = ((seconds % 3600) - reminder);
        minutes = minutesOnly / 60;
        return minutes;
    }

    private double returnSeconds(double seconds) {
        double Seconds;
        Seconds = (seconds % 3600) % 60;
        return Seconds;
    }


}
