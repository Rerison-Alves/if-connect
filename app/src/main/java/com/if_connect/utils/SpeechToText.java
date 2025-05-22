package com.if_connect.utils;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Consumer;

public class SpeechToText {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    Fragment fragment;
    Consumer<String> stringConsumer;

    public SpeechToText(Fragment fragment, Consumer<String> stringConsumer) {
        this.fragment = fragment;
        this.stringConsumer = stringConsumer;
    }

    public void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale agora...");

        try {
            fragment.startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }catch (Exception e) {
            Toast.makeText(fragment.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                stringConsumer.accept(result.get(0));
            }
        }
    }

}
