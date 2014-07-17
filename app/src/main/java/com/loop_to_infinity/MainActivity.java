package com.loop_to_infinity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;

import com.fragments.MainScreenFragment;
import com.fragments.SettingsFragment;
import com.fragments.ShiftsFragment;
import com.loop_to_infinity.Celery.R;

import utilities.MyProvider;

public class MainActivity extends Activity {
    RadioButton rb1, rb2, rb3, rb4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_ui_shell);


        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
                .penaltyLog().penaltyDeath().build());


        // ******** Set Fonts *********//

        Typeface roboto2 = Typeface.createFromAsset(this.getAssets(),
                "fonts/Ostrich Rounded.ttf");

        // Add first fragment to its container

        final FragmentManager fm = getFragmentManager();

        if (savedInstanceState == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.Frame, new MainScreenFragment(), "main");
            ft.commit();
        }

        // Set top navigation bar
        rb1 = (RadioButton) findViewById(R.id.radioButton1);
        rb1.setTypeface(roboto2) ;
        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb2.setTypeface(roboto2);
        rb3 = (RadioButton) findViewById(R.id.radioButton3);
        rb3.setTypeface(roboto2);
        rb4 = (RadioButton) findViewById(R.id.radioButton4);
        rb4.setTypeface(roboto2);
        rb1.setChecked(true); 


        // Replace fragment in the container upon clicking the radio button.
        rb1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.Frame, new MainScreenFragment());
                ft.commit();
                getFragmentManager().executePendingTransactions();
            }
        });

        rb2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.Frame, new ShiftsFragment());
                ft.commit();
                getFragmentManager().executePendingTransactions();
            }
        });

        rb3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.Frame, new SettingsFragment());
                ft.commit();
                getFragmentManager().executePendingTransactions();
            }
        });

        rb4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                openOptionsMenu();

            }
        });

    }

    // Need to implement a menu system. Below code is unreachable for now.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case (R.id.menu_settings):
                finish();
                return true;

            case (R.id.menu_delete_all):
                getContentResolver().delete(MyProvider.SHIFTS_URI, null, null);
                return true;
        }

        return false;

    }

    // Kill the app on back pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Runtime.getRuntime().exit(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        rb1.setChecked(true);
    }


}
