package com.if_connect.utils;

import static com.if_connect.utils.ErrorManager.showError;

import android.content.Context;

import com.if_connect.models.Usuario;
import com.if_connect.models.enums.TipoUsuario;
import com.if_connect.request.Generator;
import com.if_connect.request.services.UsuarioService;

import java.io.IOException;

import retrofit2.Response;

public class UsuarioManager {

    private static Usuario usuarioLogado;
    private static TipoUsuario tipoUsuario;

    private static final UsuarioService usuarioService = Generator.getRetrofitInstance().create(UsuarioService.class);

    public static void requestUsuario(String token, Context context) {
        try {
            Response<Usuario> usuarioResponse = usuarioService.getUsuarioLogado(token).execute();
            if (usuarioResponse.isSuccessful()) {
                usuarioLogado = usuarioResponse.body();
                if (usuarioLogado != null) {
                    tipoUsuario = usuarioLogado.getAluno()!=null?TipoUsuario.ALUNO:TipoUsuario.PROFESSOR;
                }
            }else {
                showError("", usuarioResponse, context);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static TipoUsuario getTipoUsuarioLogado() {
        return tipoUsuario;
    }
}
