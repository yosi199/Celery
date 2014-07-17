
/*
@author Yosi Mizrachi
@version 1.0.2
*/


package com.fragments;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.loop_to_infinity.Celery.R;
import com.shifts.ShiftObject;
import com.shifts.ShiftObjectFactory;
import com.views.HoloCircularProgressBar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import utilities.MyProvider;
import utilities.ShouldMade;
import utilities.Utility;

public class MainScreenFragment extends android.app.Fragment implements
        LoaderCallbacks<Cursor> {

    private static String TAG = "Main Fragment";
    private static Context ctx;
    public static Activity act;
    private Chronometer timer;
    private int sal;
    private int startHour, startMinute,
            visibility = 4, newMonth, hoursPerMonth, chronoVisibility = 8;
    private String startString, lastShift,
            monthly_Hours_Completed_String = "% Out of monthly hours.",
            completed_Till_Now_String = "% of the hours needed by now.",
            DateInString, monthlyPercentageString = "0",
            completedTillNowPercentage = "0", checkedStatus,
            hours_Sum_String;
    private long timeStampWhenIn, timeStampWhenOut, chronoBaseTime;
    private boolean clicked = false, typeOfShift, wasShift = false, isChronoRunning = false;
    private TextView lastShiftTV = null,
            hoursSum = null;
    private Animation rotate = null, clickAnim = null;
    private Vibrator vib = null;
    private double timeShouldHaveMade = 0.0, monthlyPercentageDouble = 0.0;
    private Handler mHandler = null;
    private ShouldMade sm = null;
    DecimalFormat df = new DecimalFormat("#.##");

    public MainScreenFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Toast.makeText(getActivity(), "onCreateView", Toast.LENGTH_SHORT).show();


        View view = inflater.inflate(R.layout.main_page, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        System.out.println("In onCreateView");
        lastShift = "N/A";
        hours_Sum_String = "N/A";

        // ******* get some resources ****** //

        ctx = getActivity();
        act = (Activity) ctx;

        restoreLastState();
        // ***** Set Animations ******* //

        final MainScreenFragment m = this;


        clickAnim = AnimationUtils.loadAnimation(ctx, R.anim.click_anim);
        rotate = AnimationUtils.loadAnimation(ctx, R.anim.rotate);

        // ******** Set Fonts ********* //
        Typeface roboto, roboto2;
        roboto2 = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Chunk.ttf");
        roboto = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Ostrich Rounded.ttf");

        // ******* Initiailize views ***** //
        final String last_shift_string = getString(R.string.Last_Shift_String);
        lastShiftTV = (TextView) view.findViewById(R.id.show_Last_shift);
        lastShiftTV.setTypeface(roboto);
        lastShiftTV.setText(last_shift_string + " " + lastShift);

        final String hours_Summary = getString(R.string.hoursSum);
        hoursSum = (TextView) view.findViewById(R.id.hoursSUM);
        hoursSum.setTypeface(roboto);
        hoursSum.setText(hours_Summary + " " + hours_Sum_String);

        TextView subTitle = (TextView) view.findViewById(R.id.subtitle2);
        subTitle.setTypeface(roboto);

        final TextView completedMonthly = (TextView) view
                .findViewById(R.id.monthlyHoursTV);
        completedMonthly.setTypeface(roboto);
        completedMonthly.setText(monthlyPercentageString
                + monthly_Hours_Completed_String);
        completedMonthly.setTextColor(Color.WHITE);

        final TextView completedTillNow = (TextView) view
                .findViewById(R.id.completedTillNowTV);
        completedTillNow.setText(completedTillNowPercentage
                + completed_Till_Now_String);
        completedTillNow.setTypeface(roboto);
        completedTillNow.setTextColor(Color.WHITE);

        final TextView subText = (TextView) view
                .findViewById(R.id.buttonSubText);
        subText.setTypeface(roboto);
        subText.setVisibility(visibility);

        final TextView subText2 = (TextView) view
                .findViewById(R.id.buttonSubText2);
        subText2.setTypeface(roboto);
        subText2.setVisibility(visibility);

        final HoloCircularProgressBar pb = (HoloCircularProgressBar) view
                .findViewById(R.id.holoCircularProgressBarOuter);
        pb.setMarkerEnabled(true);

        final HoloCircularProgressBar pb2 = (HoloCircularProgressBar) view
                .findViewById(R.id.holoCircularProgressBarInner);
        pb2.setMarkerEnabled(true);

        timer = (Chronometer) view.findViewById(R.id.chronometer);
        timer.setVisibility(chronoVisibility);
        timer.setTypeface(roboto2);

        // Main Button
        final Button bt = (Button) view.findViewById(R.id.button1);
        bt.setTypeface(roboto2);
        bt.setText(checkedStatus);
        bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(clickAnim);

                // ******* CHECK IN ********* //
                if (!clicked && !wasShift) {
                    // Get and save all data for the check in
                    chronoBaseTime = SystemClock.elapsedRealtime();
                    timer.setBase(chronoBaseTime);
                    timer.start();
                    timer.setVisibility(View.VISIBLE);
                    isChronoRunning = true;
                    chronoVisibility = View.VISIBLE;


                    // Get date
                    Calendar calendar = Calendar.getInstance();
                    final Date today = Calendar.getInstance().getTime();
                    SimpleDateFormat DF = new SimpleDateFormat("d/M/yyyy");
                    DateInString = DF.format(today);

                    // Get the shifts starting time - save date and time values.
                    timeStampWhenIn = calendar.getTimeInMillis();
                    System.out.println("In CheckIn Button " + timeStampWhenIn);
                    startHour = calendar.get(Calendar.HOUR_OF_DAY);
                    startMinute = calendar.get(Calendar.MINUTE);
                    startString = String.format("%02d", startHour) + ":"
                            + String.format("%02d", startMinute);


                    // Set Button behaviour
                    subText.setVisibility(visibility);
                    subText2.setVisibility(visibility);
                    checkedStatus = getString(R.string.CheckOut);
                    bt.setText(checkedStatus);

                    // Set Flag
                    clicked = true;

                }

                // ******* CHECK OUT ********* //
                else if (clicked) {
                    // Get all data for the check Out
                    timer.stop();
                    isChronoRunning = false;
                    timer.setVisibility(View.INVISIBLE);

                    Calendar calendar2 = Calendar.getInstance();
                    timeStampWhenOut = calendar2.getTimeInMillis();

                    // Change button text and conditional test
                    checkedStatus = "Add";
                    bt.setText(checkedStatus);
                    subText.startAnimation(clickAnim);
                    subText2.startAnimation(clickAnim);
                    visibility = 0;
                    subText.setVisibility(visibility);
                    subText2.setVisibility(visibility);

                    // Set Flag
                    clicked = false;
                    wasShift = true;

                }

                // ******* CANCEL ********* //
                else if (!clicked && wasShift) {
                    visibility = 4;
                    subText.setVisibility(visibility);
                    subText2.setVisibility(visibility);
                    checkedStatus = getResources().getString(R.string.CheckIn);
                    bt.setText(checkedStatus);
                    // Set Flag
                    clicked = false;
                    wasShift = false;

                }
            }
        });

        // ******* ADD SHIFT TO DB ********* //

        bt.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                if (wasShift) {


					/*
                     * We create a shift based on the type of shift -
					 * "global salary" will not account for money and salary per
					 * hour and the "Regular shift" will calculate the money
					 * made during that shift based on user settings.
					 * 
					 * All will be entered to DB.
					 */
                    ShiftObjectFactory SOF = new ShiftObjectFactory(typeOfShift);
                    ShiftObject S1 = SOF.Shift(getActivity());

                    // Call the shift methods
                    S1.enterShift(timeStampWhenIn, startHour,
                            startMinute, DateInString);
                    S1.setPerHour(sal);
                    S1.exitShift(timeStampWhenOut);
                    S1.insertShift();
                    S1.printShift();

                    // change UI stuff
                    checkedStatus = getResources().getString(R.string.CheckIn);
                    bt.setText(checkedStatus);
                    visibility = 4;
                    subText.setVisibility(visibility);
                    subText2.setVisibility(visibility);
                    // call a method to run calculation on the shifts
                    // calculate();

                    // Update UI thread with changes
                    mHandler.obtainMessage(1).sendToTarget();
                    vib = (Vibrator) getContext().getSystemService(
                            getContext().VIBRATOR_SERVICE);
                    vib.vibrate(50);
                    vib = null;
                    // set flags
                    wasShift = false;

                    getLoaderManager().restartLoader(0, null, m);

                }

                // If user long-pressed the button while not exiting the shift,
                // the button will shake as a message to alert something is
                // wrong.
                else {
                    v.startAnimation(rotate);
                }

                return true;
            }
        });

        // Update views in UI thread
        mHandler = new Handler() {

            public void handleMessage(Message msg) {
                pb.setProgress((float) timeShouldHaveMade * 0.01f);
                pb2.setProgress((float) monthlyPercentageDouble * 0.01f);
                completedTillNow.setText(completedTillNowPercentage
                        + completed_Till_Now_String);
                completedMonthly.setText(monthlyPercentageString
                        + monthly_Hours_Completed_String);
                lastShiftTV.setText(last_shift_string + " " + lastShift);
                hoursSum.setText(hours_Summary + " " + hours_Sum_String);
            }
        };

        return view;

    }

    public double calculate(double totalTime) {


        return totalTime / 3600.00;
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("In onStart");


    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);

        if (isChronoRunning) {
            timer.setBase(chronoBaseTime);
            System.out.println("chronoBaseTime - " + chronoBaseTime);
            System.out.println("elapsedRealtime() - " + SystemClock.elapsedRealtime());
            timer.start();
            timer.setVisibility(chronoVisibility);
            System.out.println("Timer - " + timer.getText());
        }

        System.out.println("On Resume");
//        Toast.makeText(getActivity(), "onResume", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("In onResume");

//        Toast.makeText(getActivity(), "onCreate", Toast.LENGTH_SHORT).show();

    }

    public static Context getContext() {

        return ctx;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveLastState();
//        Toast.makeText(getActivity(), "onPause", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("On Destroy");
//        Toast.makeText(getActivity(), "onDestroy", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        getLoaderManager().initLoader(0, null, this);
//        Toast.makeText(getActivity(), "onActivityCreated", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(getActivity());
        getLoaderManager().initLoader(0, null, this);
//        Toast.makeText(getActivity(), "onAttach", Toast.LENGTH_SHORT).show();
    }

    public void restoreLastState() {
        SharedPreferences sharedSettings = this.getActivity()
                .getSharedPreferences(SettingsFragment.PREFS, 0);
        typeOfShift = sharedSettings.getBoolean("TypeOfShift", false);
        clicked = sharedSettings.getBoolean("Clicked", false);
        wasShift = sharedSettings.getBoolean("WasShift", false);
        monthlyPercentageString = sharedSettings.getString("monthlyPercent",
                "0.0");
        completedTillNowPercentage = sharedSettings.getString("TillNowPercent",
                "0.0");
        DateInString = sharedSettings.getString("DateString", null);
        timeStampWhenIn = sharedSettings.getLong("timeS_IN", 0);
        startHour = sharedSettings.getInt("startHOUR", 0);
        startMinute = sharedSettings.getInt("startMINUTE", 0);
        startString = sharedSettings.getString("startStrin", null);
        checkedStatus = sharedSettings.getString("checkedSTATUS",
                getString(R.string.CheckIn));
        visibility = sharedSettings.getInt("Visibility", 4);
        sal = sharedSettings.getInt("paidHourly", 20);
        newMonth = sharedSettings.getInt("newMonth", 10);
        hoursPerMonth = sharedSettings.getInt("hoursPerMonth", 180);
        chronoBaseTime = sharedSettings.getLong("base", 0);
        isChronoRunning = sharedSettings.getBoolean("isRunning", false);
        chronoVisibility = sharedSettings.getInt("chronoVisible", TextView.VISIBLE);


    }

    private void saveLastState() {
        SharedPreferences sharedSettings = this.getActivity()
                .getSharedPreferences(SettingsFragment.PREFS, 0);
        SharedPreferences.Editor editor = sharedSettings.edit();
        editor.putBoolean("TypeOfShift", typeOfShift);
        editor.putBoolean("WasShift", wasShift);
        editor.putString("monthlyPercent", monthlyPercentageString);
        editor.putString("TillNowPercent", completedTillNowPercentage);
        editor.putString("DateString", DateInString);
        editor.putLong("timeS_IN", timeStampWhenIn);
        editor.putInt("startHOUR", startHour);
        editor.putInt("startMINUTE", startMinute);
        editor.putString("startStrin", startString);
        editor.putString("checkedSTATUS", checkedStatus);
        editor.putInt("Visibility", visibility);
        editor.putBoolean("Clicked", clicked);
        editor.putBoolean("first", false);
        editor.putLong("base", chronoBaseTime);
        editor.putBoolean("isRunning", isChronoRunning);
        editor.putInt("chronoVisible", timer.getVisibility());
        editor.apply();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        return new CursorLoader(getActivity()
                .getApplicationContext(), MyProvider.SHIFTS_URI, null, null,
                null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {

        // Find out how many hours are there
        completedTillNowPercentage = "0.0";
        monthlyPercentageString = "0.0";
        mHandler.obtainMessage(1).sendToTarget();

        cursor.moveToFirst();
        if (cursor.isFirst()) {
            double totalTime = 0.0;
            int getSeconds = 0;
            int rows = cursor.getCount();

            if (rows != 0) {
                while (cursor.getPosition() <= rows) {
                    getSeconds = Integer
                            .valueOf(cursor.getString(cursor
                                    .getColumnIndexOrThrow(MyProvider.KEY_LENGTH_IN_SECONDS)));

                    totalTime += getSeconds;

                    if (cursor.isLast()) {
                        double getHours = calculate(totalTime);
                        timeShouldHaveMade = sm.ShouldHaveMadeInt(newMonth,
                                hoursPerMonth, getHours);

                        monthlyPercentageDouble = (getHours * 100)
                                / hoursPerMonth;

                        completedTillNowPercentage = "" + timeShouldHaveMade;

                        monthlyPercentageString = String.valueOf(df
                                .format((getHours * 100) / hoursPerMonth));

                        lastShift = Utility.formatTime(getSeconds);
                        hours_Sum_String = Utility.formatTime((long) totalTime);

                        mHandler.obtainMessage(1).sendToTarget();

                        break;
                    }

                    cursor.moveToNext();
                }

            }

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {

    }


}
