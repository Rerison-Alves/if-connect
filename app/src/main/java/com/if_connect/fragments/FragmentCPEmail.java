package com.if_connect.fragments;

import static com.if_connect.utils.ErrorManager.showError;
import static com.if_connect.utils.VerificaDados.validarEmail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.if_connect.R;
import com.if_connect.bottomsheets.BottomSheetShape;
import com.if_connect.request.Generator;
import com.if_connect.request.services.UsuarioService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCPEmail extends Fragment {

    Context context;
    BottomSheetShape bottomSheetShape;
    FragmentManager fragmentManager;
    UsuarioService usuarioService;

    public FragmentCPEmail(Context context, BottomSheetShape bottomSheetShape, FragmentManager fragmentManager) {
        this.context = context;
        this.bottomSheetShape = bottomSheetShape;
        this.fragmentManager = fragmentManager;
    }

    EditText email;
    Button btnConcluir;
    ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cp_email, container, false);
        usuarioService = Generator.getRetrofitInstance().create(UsuarioService.class);

        email = view.findViewById(R.id.email);
        btnConcluir = view.findViewById(R.id.btn_concluir);
        progressBar = view.findViewById(R.id.progress_bar);

        btnConcluir.setOnClickListener(view1 -> concluir());

        return view;
    }

    private void concluir() {
        String emailString = email.getText().toString();
        if (validaCampo(emailString)) {
            //mandar email de alteração
            isLoading(true);
            usuarioService.changePasswordCode(emailString).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    isLoading(false);
                    if (response.isSuccessful()) {
                        bottomSheetShape.replaceFragment(new FragmentCPCode(
                                context,
                                bottomSheetShape,
                                fragmentManager,
                                emailString));
                    }else {
                        showError("Não foi possível enviar email: ", response, context);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    isLoading(false);
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void isLoading(boolean isLoading) {
        btnConcluir.setEnabled(!isLoading);
        btnConcluir.setAlpha(isLoading?0.5f:1.0f);
        progressBar.setVisibility(isLoading?View.VISIBLE:View.INVISIBLE);
    }

    private boolean validaCampo(String emailString) {
        if (!validarEmail(emailString)) {
            email.setError("Email inválido!");
            email.requestFocus();
            return false;
        }
        return true;
    }

}

