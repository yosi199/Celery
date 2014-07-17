package com.fragments;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.adapters.shiftsAdapter;
import com.dialogs.ShiftsDialog;
import com.loop_to_infinity.Celery.R;

import interfaces.mListener;
import utilities.MyProvider;

public class ShiftsFragment extends android.app.Fragment implements
        LoaderCallbacks<Cursor>, mListener {

    private Typeface roboto;
    private ContentResolver cr;
    public shiftsAdapter sa = null;
    public static ShiftsDialog sd = null;
    static int e = 0;
    private static String TAG = "ShiftsFragment";
    public static ShiftsFragment sf;

    public static ShiftsFragment newInstance(String title) {


        return new ShiftsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cr = getActivity().getBaseContext().getContentResolver();
        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shifts, container, false);

        sf = this;

        roboto = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/JuraLight.ttf");

        ListView ls = (ListView) view.findViewById(android.R.id.list);

        sa = new shiftsAdapter(getActivity().getBaseContext(), null, 0);
        ls.setAdapter(sa);

        ls.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int lineNum, long arg3) {
                sd = new ShiftsDialog();
                sd.newInstnace(arg3);
                sd.show(getFragmentManager(), "shifts_dialog");

                return false;
            }
        });

        return view;

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity()
                .getApplicationContext(), MyProvider.SHIFTS_URI, null, null,
                null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {

        sa.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void reload() {
        getLoaderManager().restartLoader(0, null, this);

    }

}
