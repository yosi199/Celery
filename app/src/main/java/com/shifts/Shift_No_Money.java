package com.shifts;

import android.content.Context;

public class Shift_No_Money extends ShiftObject {


    // Initialize the shift
    public Shift_No_Money(Context ctx) {
        super(ctx);

    }

    public void insertShift() {
        super.insertShift();
        System.out.println("Shift no Money");
    }

    public void printShift() {
        super.printShift();
    }

}
