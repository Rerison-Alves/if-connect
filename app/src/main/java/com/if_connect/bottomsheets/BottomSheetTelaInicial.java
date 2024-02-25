package com.if_connect.bottomsheets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.if_connect.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetTelaInicial extends BottomSheetDialogFragment {

    Context context;
    FragmentManager fragmentManager;

    public BottomSheetTelaInicial(Context context) {
        this.context = context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_tela_inicial, container, false);
        fragmentManager = getChildFragmentManager();
        replaceFragment(new FragmentTipoConta(context, this));

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
