package com.if_connect.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MaskWatcher implements TextWatcher {
    private EditText mEditText;
    private String mMask;
    private boolean isUpdating;
    private String old = "";

    public MaskWatcher(EditText editText, String mask) {
        mEditText = editText;
        mMask = mask;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        // Não precisamos fazer nada aqui
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        String str = MaskWatcher.unmask(charSequence.toString());
        String mascara = "";
        if (isUpdating) {
            old = str;
            isUpdating = false;
            return;
        }
        int i = 0;
        for (char m : mMask.toCharArray()) {
            if (m != '#' && str.length() > old.length()) {
                mascara += m;
                continue;
            }
            try {
                mascara += str.charAt(i);
            } catch (Exception e) {
                break;
            }
            i++;
        }
        isUpdating = true;
        mEditText.setText(mascara);
        mEditText.setSelection(mascara.length());
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    public static String unmask(String s) {
        return s.replaceAll("[^0-9]*", "");
    }
}