package com.if_connect.utils.typeadapters;

import android.content.Context;
import android.widget.ArrayAdapter;

public class SpinnerAdapter {
    public static ArrayAdapter<String> getAdapter(String[] itens, Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, itens);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
