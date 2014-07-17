package com.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loop_to_infinity.Celery.R;

public class SettingsFragment extends android.app.Fragment {

    public static final String PREFS = "savedSettings";
    private Typeface roboto;
    private boolean GlobalSal = false;
    private String currencyString = "";
    private int newMonthInteger = 1, hoursPerMonthString = 180,
            paidHourly = 20;

    public static SettingsFragment newInstance(String title) {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ******** Set Fonts *********//
        roboto = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Ostrich Rounded.ttf");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings2, container, false);
        restoreLastState();

        // Get references to views and set the fonts

        final Animation click = AnimationUtils.loadAnimation(getActivity()
                .getApplicationContext(), R.anim.click_anim);
        final Animation fadeIn = AnimationUtils.loadAnimation(getActivity()
                .getApplicationContext(), R.anim.fade_in);

        final CheckBox globalCheckBox = (CheckBox) view
                .findViewById(R.id.GlobalSal);
        globalCheckBox.setTypeface(roboto);
        globalCheckBox.setChecked(GlobalSal);
        globalCheckBox.requestFocus();

        TextView salaryLabel = (TextView) view.findViewById(R.id.salaryLabel);
        salaryLabel.setTypeface(roboto);

        final EditText enterSal = (EditText) view
                .findViewById(R.id.enterSalary);
        enterSal.setTypeface(roboto);
        enterSal.setText(String.valueOf(paidHourly));
        enterSal.startAnimation(fadeIn);

        TextView currency = (TextView) view.findViewById(R.id.currency);
        currency.setTypeface(roboto);

        final EditText enterCur = (EditText) view
                .findViewById(R.id.enterCurrency);
        enterCur.setTypeface(roboto);
        enterCur.setText(String.valueOf(currencyString));
        enterCur.startAnimation(fadeIn);

        final TextView newMonth = (TextView) view
                .findViewById(R.id.newMonthDate);
        newMonth.setTypeface(roboto);

        final EditText newMonthEditBox = (EditText) view
                .findViewById(R.id.newMonthEditBox);
        newMonthEditBox.setTypeface(roboto);
        newMonthEditBox.setText(String.valueOf(newMonthInteger));
        newMonthEditBox.startAnimation(fadeIn);

        TextView monthlyHours = (TextView) view
                .findViewById(R.id.monthlyNeededHoursText);
        monthlyHours.setTypeface(roboto);

        final EditText monthlyBox = (EditText) view
                .findViewById(R.id.monthlyNeededHoursEditText);
        monthlyBox.setTypeface(roboto);
        monthlyBox.setText(String.valueOf(hoursPerMonthString));
        monthlyBox.startAnimation(fadeIn);

        TextView note = (TextView) view.findViewById(R.id.SettingsNote);
        note.setTypeface(roboto);

        TextView by = (TextView) view.findViewById(R.id.By);
        by.setTypeface(roboto);

        TextView version = (TextView) view.findViewById(R.id.Version);
        version.setTypeface(roboto);

        Button save = (Button) view.findViewById(R.id.SaveSettings);
        save.setTypeface(roboto);

        Button cancel = (Button) view.findViewById(R.id.CancelSettings);
        cancel.setTypeface(roboto);

        // ****** Upon saving ******* //

        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String toastText = "Settings Saved. ";
                v.startAnimation(click);

                try {
                    hoursPerMonthString = Integer.valueOf(monthlyBox.getText()
                            .toString());
                    GlobalSal = globalCheckBox.isChecked();
                    paidHourly = Integer.valueOf(enterSal.getText().toString());
                    currencyString = enterCur.getText().toString();
                    newMonthInteger = Integer.valueOf(newMonthEditBox.getText()
                            .toString());
                } catch (Exception e) {
                    toastText = "Error entering data, please review.";
                    newMonthInteger = 1;
                    currencyString = "USD";
                    paidHourly = 20;

                }
                saveCurrentState();

                Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT)
                        .show();

            }
        });

        // ****** Upon canceling ******* //

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(click);
                getFragmentManager().popBackStackImmediate();

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        restoreLastState();

    }


    @Override
    public void onStop() {
        super.onStop();

        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

    }

    public void saveCurrentState() {
        SharedPreferences sharedSettings = this.getActivity()
                .getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = sharedSettings.edit();
        editor.putBoolean("TypeOfShift", GlobalSal);
        editor.putInt("paidHourly", paidHourly);
        editor.putString("currency", currencyString);
        editor.putInt("newMonth", newMonthInteger);
        editor.putInt("hoursPerMonth", hoursPerMonthString);

        editor.apply();
    }

    public void restoreLastState() {
        SharedPreferences sharedSettings = this.getActivity()
                .getSharedPreferences(PREFS, 0);

        GlobalSal = sharedSettings.getBoolean("TypeOfShift", false);
        paidHourly = sharedSettings.getInt("paidHourly", 40);
        currencyString = sharedSettings.getString("currency", "USD");
        newMonthInteger = sharedSettings.getInt("newMonth", 1);
        hoursPerMonthString = sharedSettings.getInt("hoursPerMonth", 180);

    }

    public int getMonthStart() {
        return newMonthInteger;
    }

    public int getMonthlyHours() {
        return hoursPerMonthString;
    }
}
