package com.if_connect.utils;

import android.annotation.SuppressLint;
import android.widget.EditText;
import com.jakewharton.rxbinding4.widget.RxTextView;
import java.util.concurrent.TimeUnit;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import java.util.function.Consumer;

public class BounceEditText {

    @SuppressLint("CheckResult")
    public BounceEditText(EditText editText, int delayMiliSeconds, Consumer<CharSequence> onTextDebounce) {
        RxTextView.textChanges(editText)
                .debounce(delayMiliSeconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onTextDebounce::accept);
    }

}