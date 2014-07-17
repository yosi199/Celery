package com.shifts;

import android.content.Context;

public class Shift_With_Money extends ShiftObject {

    // Initialize the shift

    public Shift_With_Money(Context ctx) {
        super(ctx);
    }

    public void insertShift() {
        super.insertShift();
        System.out.println("Shift with Money");

    }

    public void printShift() {
        super.printShift();
    }

}
