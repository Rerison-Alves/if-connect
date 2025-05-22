package com.if_connect.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.if_connect.R;
import com.if_connect.LoginActivity;
import com.if_connect.MainActivity;
import com.if_connect.bottomsheets.BottomSheetShape;
import com.if_connect.fragments.FragmentCPEmail;
import com.if_connect.utils.TokenManager;

public class DialogConfig extends DialogFragment {

    Context context;
    MainActivity mainActivity;
    FragmentManager fragmentManager;

    FrameLayout alterarSenha, logout;
    CardView voltar;

    public DialogConfig(Context context, MainActivity mainActivity, FragmentManager fragmentManager) {
        this.context = context;
        this.mainActivity = mainActivity;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Ifconnect);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_config, container, false);

        alterarSenha = view.findViewById(R.id.alterasenha);
        voltar = view.findViewById(R.id.voltar);
        logout = view.findViewById(R.id.logout);

        voltar.setOnClickListener(view1 -> dismiss());

        logout.setOnClickListener(view1 -> logoutDialog());
        alterarSenha.setOnClickListener(view1 -> alterarSenha());
        return view;
    }

    private void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Ao sair sua conta será desconectada, e você terá de fazer login novamente!").setTitle("Deseja Sair?");
        builder.setNegativeButton(R.string.sim, (dialogInterface, i) -> {
            TokenManager.getInstance(context).deleteTokens();
            mainActivity.startActivity(new Intent(context, LoginActivity.class));
            mainActivity.finish();

        });
        builder.setPositiveButton(R.string.nao, (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void alterarSenha() {
        BottomSheetShape bottomSheetShape = new BottomSheetShape(context);
        bottomSheetShape.setFragment(new FragmentCPEmail(context, bottomSheetShape, fragmentManager));
        bottomSheetShape.show(fragmentManager, "tag");

    }
}