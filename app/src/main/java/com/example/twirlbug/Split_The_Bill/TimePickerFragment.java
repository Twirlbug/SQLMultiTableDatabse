package com.example.twirlbug.Split_The_Bill;

/**
 * Created by Twirlbug on 3/14/2016.
 */
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Nicole Geiger on 9/8/2015.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    public static final String EXTRA_HOUR= "com.example.twirlbug.Split_The_Bill.timehour";
    public static final String EXTRA_MIN= "com.example.twirlbug.Split_The_Bill.timeminute";

    private static final String ARG_DATE = "date";

    public static TimePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int resultCode, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        if(getTargetFragment() ==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_HOUR, hourOfDay);
        intent.putExtra(EXTRA_MIN, minute);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        onTimeSet(view, Activity.RESULT_OK, hourOfDay, minute);

    }

}