package com.if_connect.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.if_connect.R;
import com.if_connect.utils.UsuarioManager;


public class Perfil extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar o layout do fragmento
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    // Este método é chamado após a criação do layout do fragmento
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.container_perfil, getPerfilByTipoUsuario())
                .commit();
    }

    public Fragment getPerfilByTipoUsuario(){
        switch (UsuarioManager.getTipoUsuarioLogado()){
            case ALUNO:
                return new PerfilAluno();
            case PROFESSOR:
                return new PerfilProfessor();
            default:
                return null;
        }
    }
    
}