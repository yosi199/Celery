package com.shifts;

import android.content.Context;

import com.fragments.MainScreenFragment;

public class ShiftObjectFactory {

    private boolean type = false;
    private Context ctx2;

    public ShiftObjectFactory(boolean type) {
        this.type = type;
        ctx2 = MainScreenFragment.getContext();
    }

    public ShiftObject Shift(Context ctx) {
        if (type) {
            return new Shift_No_Money(ctx2);
        } else if (!type) {
            return new Shift_With_Money(ctx2);
        } else return null;


    }


}
