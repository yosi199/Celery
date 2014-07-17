package com.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fragments.ShiftsFragment;
import com.loop_to_infinity.Celery.R;

import java.util.concurrent.TimeUnit;

import interfaces.mListener;
import utilities.MyProvider;
import utilities.Utility;

/*
 * This dialog opens the edit menu to set the END time
 * for the shift. 
 * Clicking the next button will save all data and UPDATE the DB with the new manually inserted shift
 * 
 */

public class Edit_End_time extends DialogFragment {
    private static long position = 0;
    private ContentResolver cr = null;
    private int endHour, endMinute;

    private double perHour;
    public static final String PREFS = "savedSettings";
    private static mListener listener;

    public static Edit_End_time newInstnace(long i) {
        position = i;
        listener = ShiftsFragment.sf;

        return new Edit_End_time();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cr = getActivity().getBaseContext().getContentResolver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set dialog look and behaviour and create views.
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(5));
        getDialog().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        Typeface roboto;

        SharedPreferences sharedSettings = getActivity().getApplicationContext()
                .getSharedPreferences(PREFS, 0);

        perHour = sharedSettings.getInt("paidHourly", 40);


        roboto = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Ostrich Rounded.ttf");

        View view = inflater.inflate(R.layout.edit_finish_dialog, container,
                false);

        TextView title = (TextView) view.findViewById(R.id.title2);
        title.setTypeface(roboto);

        final TimePicker tp = (TimePicker) view.findViewById(R.id.timePicker2);

        Button next = (Button) view.findViewById(R.id.nextButton3);
        next.setTypeface(roboto);
        next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Create and Validate logic

                // Collect starting time.
                int startHour = Edit_Start_Time.getHour();
                int startMinute = Edit_Start_Time.getMinute();

                // Collect ending time
                endHour = tp.getCurrentHour();
                endMinute = tp.getCurrentMinute();

                // Save the original ending time so we can manipulate them.
                int originalEndHour = endHour;
                int originalEndMinute = endMinute;

                // Test different conditions for hours
                if (startHour > endHour) {
                    endHour = endHour + 24;
                }

                // Test different conditions for minutes
                if (startMinute > endMinute) {
                    endMinute = endMinute + 60;
                }

                // Sum totals - Ints & Strings
                int totalHours = endHour - startHour;
                int totalMinutes = endMinute - startMinute;

                int totalSeconds = (int) (TimeUnit.HOURS.toSeconds(totalHours) + TimeUnit.MINUTES
                        .toSeconds(totalMinutes));
                String totalTimeOfShift = Utility.formatTime(totalSeconds);

                String startTExt = String.format("%02d", startHour) + ":"
                        + String.format("%02d", startMinute);

                String endTExt = String.format("%02d", originalEndHour) + ":"
                        + String.format("%02d", originalEndMinute);

                // Get money from the utility class
                double money = Utility.INSTANCE.calcShiftMoney(totalSeconds, perHour);

                // Insert these values instead of the old shift

                ContentValues cv = new ContentValues();
                cv.put(MyProvider.KEY_TIMESTAMP_IN, "");
                cv.put(MyProvider.KEY_TIMESTAMP_OUT, "");
                cv.put(MyProvider.KEY_STARTING_HOUR_STRING,
                        String.valueOf(startHour));
                cv.put(MyProvider.KEY_STARTING_HOUR_INT, startHour);
                cv.put(MyProvider.KEY_STARTING_MINUTES_STRING,
                        String.valueOf(startMinute));
                cv.put(MyProvider.KEY_STARTING_MINUTES_INT, startMinute);
                cv.put(MyProvider.KEY_FINISHING_HOUR_STRING,
                        String.valueOf(endHour));
                cv.put(MyProvider.KEY_FINISHING_HOUR_INT, endHour);
                cv.put(MyProvider.KEY_FINISHIN_MINUTES_STRING,
                        String.valueOf(endMinute));
                cv.put(MyProvider.KEY_FINISHIN_MINUTES_INT, endMinute);
                cv.put(MyProvider.KEY_TOTAL_TIME_OF_SHIFT_STRING,
                        totalTimeOfShift);
                cv.put(MyProvider.KEY_MONTH_STRING,
                        String.valueOf(EditDate_Dialog.getMonth()));
                cv.put(MyProvider.KEY_DATE, EditDate_Dialog.getDate());
                cv.put(MyProvider.KEY_DAY, EditDate_Dialog.getDay());
                cv.put(MyProvider.KEY_INSERT_MODE, "Edited");
                cv.put(MyProvider.KEY_MONEY, money);
                cv.put(MyProvider.KEY_START_TEXT_VIEW, startTExt);
                cv.put(MyProvider.KEY_END_TEXT_VIEW, endTExt);
                cv.put(MyProvider.KEY_LENGTH_IN_SECONDS, totalSeconds);

                Uri rowUri = ContentUris.withAppendedId(MyProvider.SHIFTS_URI,
                        position);

                cr.update(rowUri, cv, null, null);

                if (listener != null) {
                    listener.reload();
                }

                dismiss();
            }
        });

        return view;
    }

}
