package com.shifts;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import utilities.MyProvider;
import utilities.Utility;

public class ShiftObject {
    String totalTimeOfShift, startHourString, finishHourString,
            startMinuteString, finishMinuteString, DateInString,
            startTEXTString, finishTEXTString,
            insertMode, day;
    double madeForShift;
    long timeStampWhenIn, timeStampWhenOut, time;
    int startHourINT, finishHourINT, startMinuteINT, finishMinuteINT, month;
    float perHour;
    Context ThisCtx;
    ContentResolver cr;
    public static final String PREFS = "savedSettings";

    public ShiftObject(Context ctx) {
        ThisCtx = ctx;
        cr = ThisCtx.getContentResolver();

    }

    public void enterShift(long timeStampWhenIn,
                           int startHour, int startMinute, String DateInString) {

        SharedPreferences sharedSettings = ThisCtx.getApplicationContext()
                .getSharedPreferences(PREFS, 0);

        perHour = sharedSettings.getInt("paidHourly", 40);
        this.DateInString = DateInString;

        // Get time stamps
        this.timeStampWhenIn = timeStampWhenIn;
        this.timeStampWhenOut = timeStampWhenOut;

        // Get hour in String and Ints
        this.startHourINT = startHour;
        this.startHourString = String.valueOf(startHourINT);

        // Get minutes in String and Ints
        this.startMinuteINT = startMinute;
        this.startMinuteString = String.valueOf(startMinuteINT);

        this.startTEXTString = String.format("%02d", startHourINT) + ":"
                + String.format("%02d", startMinuteINT);

    }

    public void exitShift(long mTimeStampWhenOut) {
        Calendar calendar2 = Calendar.getInstance();
        this.timeStampWhenOut = mTimeStampWhenOut;

        // Get hour in String and Ints
        finishHourINT = calendar2.get(Calendar.HOUR_OF_DAY);
        finishHourString = String.valueOf(finishHourINT);

        // Get minutes in String and Ints
        finishMinuteINT = calendar2.get(Calendar.MINUTE);
        finishMinuteString = String.valueOf(finishMinuteINT);

        finishTEXTString = String.format("%02d", finishHourINT) + ":"
                + String.format("%02d", finishMinuteINT);
        totalTimeOfShift = totalTimeOfShiftString(timeStampWhenIn,
                timeStampWhenOut);

        // Get day and month and set insert mode (Edited shift or not)
        GregorianCalendar GC = new GregorianCalendar();
        month = calendar2.get(Calendar.MONTH);
        day = Utility.getDay(GC.get(Calendar.DAY_OF_WEEK));
        insertMode = "";

    }

    // *** Get And Set salary per hour
    public float getPerHour() {
        return this.perHour;
    }

    public void setPerHour(float perHour) {
        this.perHour = perHour;
    }


    public void insertShift() {

        ContentValues values = new ContentValues();

        values.put(MyProvider.KEY_TIMESTAMP_IN, timeStampWhenIn);
        values.put(MyProvider.KEY_TIMESTAMP_OUT, timeStampWhenOut);
        values.put(MyProvider.KEY_STARTING_HOUR_STRING, startHourString);
        values.put(MyProvider.KEY_FINISHING_HOUR_STRING, finishHourString);
        values.put(MyProvider.KEY_STARTING_MINUTES_STRING, startMinuteString);
        values.put(MyProvider.KEY_FINISHIN_MINUTES_STRING, finishMinuteString);
        values.put(MyProvider.KEY_STARTING_HOUR_INT, startHourINT);
        values.put(MyProvider.KEY_FINISHING_HOUR_INT, finishHourINT);
        values.put(MyProvider.KEY_STARTING_MINUTES_INT, startMinuteINT);
        values.put(MyProvider.KEY_FINISHIN_MINUTES_INT, finishMinuteINT);
        values.put(MyProvider.KEY_TOTAL_TIME_OF_SHIFT_STRING, totalTimeOfShift);
        values.put(MyProvider.KEY_MONTH_STRING, month);
        values.put(MyProvider.KEY_DATE, DateInString);
        values.put(MyProvider.KEY_DAY, day);
        values.put(MyProvider.KEY_INSERT_MODE, insertMode);
        values.put(MyProvider.KEY_MONEY, madeForShift);
        values.put(MyProvider.KEY_START_TEXT_VIEW, startTEXTString);
        values.put(MyProvider.KEY_END_TEXT_VIEW, finishTEXTString);
        values.put(MyProvider.KEY_LENGTH_IN_SECONDS, time);

        cr.insert(MyProvider.SHIFTS_URI, values);
    }

    // Returns total time of shift in String
    @SuppressLint("NewApi")
    public String totalTimeOfShiftString(long In, long Out) {
        long sum = 0;
        String timeOfShift;

        if (In < Out) {
            sum = Out - In;
        } else if (Out < In) {
            sum = (Out + 86400000) - In;
        }

        time = TimeUnit.MILLISECONDS.toSeconds(sum);
        timeOfShift = Utility.formatTime(time);

        // Get money from the utility class
        madeForShift = Utility.INSTANCE.calcShiftMoney(time, perHour);

        System.out.println("timeOfShift " + timeOfShift);
        System.out.println("Time in Shift " + time + " Seconds");

        return timeOfShift;
    }


    public void printShift() {

        // Log.i("TimeStampIn", "" + timeStampWhenIn);
        // Log.i("TimeStampOut", "" + timeStampWhenOut);
        // Log.i("StartHour", "" + startHourINT);
        // Log.i("FinishHour", "" + finishHourINT);
        // Log.i("StartMinute", "" + startMinuteINT);
        // Log.i("FinishMinute", "" + finishMinuteINT);
        // Log.i("DateInString", DateInString);

    }


}
