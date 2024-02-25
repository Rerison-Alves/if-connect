package com.if_connect.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.if_connect.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateEditText extends LinearLayout {

    private EditText dayEditText;
    private EditText monthEditText;
    private EditText yearEditText;

    public DateEditText(Context context) {
        super(context);
        init(context);
    }

    public DateEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DateEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_date_edit_text_group, this, true);
        dayEditText = view.findViewById(R.id.dayEditText);
        monthEditText = view.findViewById(R.id.monthEditText);
        yearEditText = view.findViewById(R.id.yearEditText);

        setupTextWatchers();
        setupKeyListeners();
    }

    private void setupTextWatchers() {
        TextWatcher dayWatcher = createTextWatcher(dayEditText, monthEditText);
        TextWatcher monthWatcher = createTextWatcher(monthEditText, yearEditText);

        dayEditText.addTextChangedListener(dayWatcher);
        monthEditText.addTextChangedListener(monthWatcher);
    }

    private void setupKeyListeners() {
        setupKeyListener(dayEditText, null, monthEditText);
        setupKeyListener(monthEditText, dayEditText, yearEditText);
        setupKeyListener(yearEditText, monthEditText, null);
    }

    private void setupKeyListener(final EditText currentEditText, final EditText prevEditText, final EditText nextEditText) {
        currentEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN && currentEditText.getText().length() == 0) {
                    if (prevEditText != null) {
                        prevEditText.requestFocus();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private TextWatcher createTextWatcher(final EditText currentEditText, final EditText nextEditText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2 && nextEditText != null) {
                    nextEditText.requestFocus();
                }
            }
        };
    }

    public void setText(String date) {
        String[] parts = date.split("/");
        if (parts.length == 3) {
            dayEditText.setText(parts[0]);
            monthEditText.setText(parts[1]);
            yearEditText.setText(parts[2]);
        }
    }

    public String getText() {
        String day = dayEditText.getText().toString();
        String month = monthEditText.getText().toString();
        String year = yearEditText.getText().toString();
        return day + "/" + month + "/" + year;
    }

    public Date getDate(){
        SimpleDateFormat formata = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            return formata.parse(getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}




