package com.dialogs;

import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fragments.ShiftsFragment;
import com.loop_to_infinity.Celery.R;

import interfaces.mListener;
import utilities.MyProvider;

/*
 * This dialog opens the menu to DELETE or EDIT
 *  a shift. Upon clicking the next button it will call the
 *  EditDate Dialog class
 * 
 */

public class ShiftsDialog extends DialogFragment {
    private Typeface roboto = null;
    private static long position = 0;
    private ContentResolver cr = null;
    private static mListener listener;


    public static ShiftsDialog newInstnace(long i) {
        position = i;
        listener = ShiftsFragment.sf;
        return new ShiftsDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cr = getActivity().getBaseContext().getContentResolver();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set Dialog look
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

        roboto = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Ostrich Rounded.ttf");

        // Create the views
        View view = inflater.inflate(R.layout.dialog, container, false);

        TextView tv = (TextView) view.findViewById(R.id.options);
        tv.setTypeface(roboto);

        Button delete = (Button) view.findViewById(R.id.deleteBtn);
        delete.setTypeface(roboto);

        // OnClick listener for delete function
        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                Uri rowURI = ContentUris.withAppendedId(MyProvider.SHIFTS_URI,
                        position);
                cr.delete(rowURI, null, null);
                listener.reload();
                dismiss();

            }
        });

        Button edit = (Button) view.findViewById(R.id.editBtn);
        edit.setTypeface(roboto);

        // OnClick listener for editing a shift
        edit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // Here we call the edit dialog with the position of the row we
                // want to edit.
                EditDate_Dialog ed = new EditDate_Dialog();
                ed.newInstnace(position);
                ed.show(getFragmentManager(), "edit_dialog");

                dismiss();

            }
        });

        return view;

    }

}
