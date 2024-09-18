package com.if_connect.utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Locale;

public class CustomTimePicker {

    public static final String HOURFORMAT = "%02d:%02d";

    public static void openTimePickerDialog(Calendar calendar, EditText editText, Context context) {
        // Obtém a hora atual
        int horaAtual = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        int minutoAtual = calendar.get(java.util.Calendar.MINUTE);

        // Cria e exibe o TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                (view, horaSelecionada, minutoSelecionado) -> {
                    // Atualiza o TextView com a hora selecionada
                    String horaFormatada = String.format(Locale.getDefault(), HOURFORMAT, horaSelecionada, minutoSelecionado);
                    editText.setText(horaFormatada);
                    }, horaAtual, minutoAtual, true); // true para exibir formato 24 horas

        timePickerDialog.show();
    }
}
