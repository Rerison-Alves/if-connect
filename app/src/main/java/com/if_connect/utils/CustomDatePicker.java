package com.if_connect.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomDatePicker {

    private static final String myFormat = "dd/MM/yyyy";
    public static SimpleDateFormat dateFormat;

    // Inicia tela de calendário
    public static void openDatePicker(Calendar calendar, EditText editText, Context context, Date maxDate, Date minDate) {
        dateFormat = new SimpleDateFormat(myFormat, Locale.getDefault());

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);

            editText.setText("");
            editText.setText(dateFormat.format(calendar.getTime()));
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        if (maxDate!=null){
            datePickerDialog
                    .getDatePicker()
                    .setMaxDate(maxDate.getTime());
        }

        if (minDate!=null){
            datePickerDialog
                    .getDatePicker()
                    .setMinDate(minDate.getTime());
        }

        datePickerDialog.show();
    }

    public static void openDatePicker(Calendar calendar, EditText editText, Context context){
        openDatePicker(calendar, editText, context, null, null);
    }


}
