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
import android.widget.TextView;
import android.widget.TimePicker;

import com.loop_to_infinity.Celery.R;

/*
 * This dialog opens the edit menu to set the START time
 * for the shift. 
 * Clicking the next button will open the END time menu dialog
 * 
 */

public class Edit_Start_Time extends DialogFragment {

    private static long position = 0;
    private static int hour;
    private static int minute;

    public static Edit_Start_Time newInstnace(long i) {
        position = i;


        return new Edit_Start_Time();
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

        View view = inflater.inflate(com.loop_to_infinity.Celery.R.layout.edit_start_dialog, container,
                false);

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setTypeface(roboto);

        final TimePicker tp = (TimePicker) view.findViewById(R.id.timePicker1);

        Button next = (Button) view.findViewById(R.id.nextButton2);
        next.setTypeface(roboto);
        next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Save the shifts starting time and move on to the dialog that collects the shifts ending time

                hour = tp.getCurrentHour();
                minute = tp.getCurrentMinute();

                Edit_End_time endTime = new Edit_End_time();
                endTime.newInstnace(position);
                endTime.show(getFragmentManager(), "EditEndTime");
                dismiss();

            }
        });

        return view;
    }

    public static int getHour() {
        return hour;
    }

    public static int getMinute() {
        return minute;
    }

}
