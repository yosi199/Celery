package utilities;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ShouldMade {

    private ShouldMade() {
    }

    public static double ShouldHaveMadeInt(int monthStart, int monthlyHours,
                                           double hoursMade) {
        String s;
        int dayOfMonth = 0;
        int daysInLastMonth = 0;
        int days_Passed_Till_Current_Month = 0;
        int daysInMonth = 0;
        int days_Passed_So_Far = 0;
        double hoursPerDay = 0.0;
        double ShouldHaveSoFar = 0.0;

        // Get current date
        Calendar currentMonth = new GregorianCalendar();

        // Get last month date
        Calendar lastMonth = new GregorianCalendar(
                currentMonth.get(Calendar.YEAR),
                (currentMonth.get(Calendar.MONTH) - 1),
                currentMonth.get(Calendar.DAY_OF_MONTH));

        // Get total days in last month
        daysInLastMonth = lastMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Find out how many days have passed since the user's salary begins
        // until the month ends
        days_Passed_Till_Current_Month = daysInLastMonth - monthStart;

        // Get how many days are in this month
        daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Get the current day of the month
        dayOfMonth = currentMonth.get(Calendar.DAY_OF_MONTH);

        if (dayOfMonth < monthStart) {

            // Sum up the total days passed since user's salary month began and
            // up
            // to this day
            days_Passed_So_Far = days_Passed_Till_Current_Month + dayOfMonth;

        } else if (dayOfMonth > monthStart) {

            days_Passed_So_Far = dayOfMonth - monthStart;

        } else if (dayOfMonth == monthStart) {
            days_Passed_So_Far = 1;
        }

        // Calculate how many hours are needed per day
        hoursPerDay = (double) monthlyHours / (double) daysInMonth;

        // Find how many hours the user should have done so far
        ShouldHaveSoFar = hoursPerDay * days_Passed_So_Far;

        // Return the percentage of hours I've done so far

        // need to uncomment below to use the function

        DecimalFormat df = new DecimalFormat("#.##");
        double percentage = (hoursMade * 100) / ShouldHaveSoFar;

        s = String.valueOf(df.format(percentage));

        return Double.valueOf(s);
    }

}
