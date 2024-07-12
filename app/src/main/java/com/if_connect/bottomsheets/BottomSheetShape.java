package com.if_connect.bottomsheets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.if_connect.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetShape extends BottomSheetDialogFragment {

    Context context;
    Fragment fragment;

    public BottomSheetShape(Context context) {
        this.context = context;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_shape, container, false);
        replaceFragment(fragment);
        return view;
    }

    public void replaceFragment(Fragment fragment){
        getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_slide_in, R.anim.fragment_slide_out)
                .replace(R.id.frameLayoutContainer, fragment)
                .commit();
    }

}
