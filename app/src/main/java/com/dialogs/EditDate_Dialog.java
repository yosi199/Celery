package com.dialogs;

import android.app.DialogFragment;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.loop_to_infinity.Celery.R;

import java.sql.Date;
import java.util.Calendar;

import utilities.Utility;

/*
 * This dialog opens the edit menu to set the DATE
 * for the shift. 
 * Clicking the next button will open the 
 * StartTime dialog to set the shift's entering hour
 * 
 */

public class EditDate_Dialog extends DialogFragment {

    private static long position = 0;
    private static int yearFromPicker, dayFromPicker, monthFromPicker, year,
            day, month;


    public static EditDate_Dialog newInstnace(long i) {
        position = i;


        return new EditDate_Dialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        Typeface roboto = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Ostrich Rounded.ttf");

        View view = inflater.inflate(R.layout.edit_dialog, container, false);

        final DatePicker dPicker = (DatePicker) view
                .findViewById(R.id.datePicker1);
        dPicker.setCalendarViewShown(false);

        TextView dateTitle = (TextView) view.findViewById(R.id.dateTitle);
        dateTitle.setTypeface(roboto);

        Button next = (Button) view.findViewById(R.id.nextButton);
        next.setTypeface(roboto);
        next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Get date info and save it
                yearFromPicker = dPicker.getYear();
                monthFromPicker = dPicker.getMonth() + 1;
                dayFromPicker = dPicker.getDayOfMonth();

                // Find out which day of the week according to date .
                Date d = new Date(yearFromPicker, monthFromPicker, dayFromPicker);
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                c.set(yearFromPicker, monthFromPicker - 1, dayFromPicker);


                day = c.get(Calendar.DAY_OF_WEEK);

                // Go to the shift "start" hour dialog and dismiss this dialog
                Edit_Start_Time startTime = new Edit_Start_Time();
                // Still passing the position of the row to edit
                startTime.newInstnace(position);
                startTime.show(getFragmentManager(), "EditStartTime");
                dismiss();
            }
        });

        return view;
    }

    // static methods to get date info in order to update the shift
    public static String getDay() {

        return Utility.getDay(day);
    }

    public static int getMonth() {

        return month;
    }

    public static int getYear() {

        return year;
    }

    public static String getDate() {


        return dayFromPicker + "/" + monthFromPicker + "/"
                + yearFromPicker;
    }

}
