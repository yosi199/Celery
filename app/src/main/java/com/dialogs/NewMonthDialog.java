package com.dialogs;

import android.app.DialogFragment;
import android.content.ContentResolver;
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

import com.loop_to_infinity.Celery.R;

import utilities.MyProvider;

public class NewMonthDialog extends DialogFragment {

    private Typeface roboto = null;

    public static NewMonthDialog newInstnace() {
        return new NewMonthDialog();
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

        roboto = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/JuraLight.ttf");

        View view = inflater.inflate(R.layout.new_month_dialog, container,
                false);

        TextView newMonthTV = (TextView) view.findViewById(R.id.newMonthText);
        newMonthTV.setTypeface(roboto);

        Button bt1 = (Button) view.findViewById(R.id.Yes);
        bt1.setTypeface(roboto);
        bt1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ContentResolver cr = getActivity().getContentResolver();
                cr.delete(MyProvider.SHIFTS_URI, null, null);
                dismiss();
            }
        });
        Button bt2 = (Button) view.findViewById(R.id.No);
        bt2.setTypeface(roboto);
        bt2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}