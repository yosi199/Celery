package com.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.fragments.SettingsFragment;
import com.loop_to_infinity.Celery.R;

import java.text.DecimalFormat;

import utilities.MyProvider;

public class shiftsAdapter extends CursorAdapter {

    public static ViewHolder holder;
    private Context ctx;
    private final LayoutInflater inflater;
    private Typeface roboto = null;
    private boolean typeOfShift;
    private String currencyString = null;

    public shiftsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        ctx = context;
        inflater = LayoutInflater.from(ctx);
        roboto = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/Ostrich Rounded.ttf");

        // Get type of shift - money/no money
        SharedPreferences sharedSettings = ctx.getSharedPreferences(
                SettingsFragment.PREFS, 0);
        typeOfShift = sharedSettings.getBoolean("TypeOfShift", false);
        currencyString = sharedSettings.getString("currency", "USD");

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        holder = new ViewHolder();

        holder.dateTitle = (TextView) view
                .findViewById(R.id.Info);
        holder.dateTitle.setTypeface(roboto);

        holder.durationTitle = (TextView) view
                .findViewById(R.id.Duration);
        holder.durationTitle.setTypeface(roboto);

        holder.moneyTitle = (TextView) view
                .findViewById(R.id.Money);
        holder.moneyTitle.setTypeface(roboto);

        holder.date = (TextView) view
                .findViewById(R.id.dateDisp);
        holder.date.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(MyProvider.KEY_DATE)));
        holder.date.setTypeface(roboto);

        holder.day = (TextView) view
                .findViewById(R.id.day);
        holder.day.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(MyProvider.KEY_DAY)));
        holder.day.setTypeface(roboto);

        holder.start = (TextView) view
                .findViewById(R.id.start_finish);
        holder.start.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(MyProvider.KEY_START_TEXT_VIEW))
                + " - "
                + cursor.getString(cursor
                .getColumnIndexOrThrow(MyProvider.KEY_END_TEXT_VIEW)));
        holder.start.setTypeface(roboto);

        holder.legnth = (TextView) view
                .findViewById(R.id.timeInShift);
        holder.legnth
                .setText(cursor.getString(cursor
                        .getColumnIndexOrThrow(MyProvider.KEY_TOTAL_TIME_OF_SHIFT_STRING)));
        holder.legnth.setTypeface(roboto);

        holder.money = (TextView) view
                .findViewById(R.id.shiftDisp);
        double moneyS = cursor.getDouble(cursor
                .getColumnIndexOrThrow(MyProvider.KEY_MONEY));
        DecimalFormat df = new DecimalFormat("#.##");

        if (typeOfShift) {
            holder.money.setText("Global");

        } else if (!typeOfShift) {
            holder.money.setText("" + df.format(moneyS) + " " + currencyString);
        }

        holder.money.setTypeface(roboto);

        holder.edited = (TextView) view
                .findViewById(R.id.insert);
        holder.edited.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(MyProvider.KEY_INSERT_MODE)));
        holder.edited.setTypeface(roboto);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.list_entry2,
                null);
    }

    static class ViewHolder {
        TextView dateTitle;
        TextView durationTitle;
        TextView moneyTitle;
        TextView date;
        TextView day;
        TextView start;
        TextView legnth;
        TextView money;
        TextView edited;

    }

}
