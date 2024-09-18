package com.if_connect.bottomsheets;

import static com.if_connect.utils.CustomDatePicker.openDatePicker;
import static com.if_connect.utils.CustomTimePicker.openTimePickerDialog;
import static com.if_connect.utils.ErrorManager.showError;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import com.example.if_connect.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.if_connect.dialogs.AlertDialogManager;
import com.if_connect.dialogs.DialogEscolherLocal;
import com.if_connect.dialogs.DialogGrupoAdm;
import com.if_connect.dialogs.DialogTurmaAdm;
import com.if_connect.models.Agendamento;
import com.if_connect.models.Encontro;
import com.if_connect.models.Grupo;
import com.if_connect.models.Local;
import com.if_connect.models.Turma;
import com.if_connect.request.Generator;
import com.if_connect.request.services.EncontroService;
import com.if_connect.utils.DateEditText;
import com.if_connect.utils.MaskWatcher;
import com.if_connect.utils.TokenManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetCriarEncontro extends BottomSheetDialogFragment {

    Context context;
    public TextView localselecionado;
    DateEditText data;
    EditText temaDoEncontro, descricao;
    EditText inicio, fim;
    FrameLayout btnEscolherlocal, btnConcluir;
    CardView buttonDt, buttonInicio, buttonFim;
    FragmentManager fragmentManager;
    public Local localEncontro = null;

    Calendar calendar = Calendar.getInstance();

    EncontroService encontroService;
    String token;

    DialogGrupoAdm dialogGrupoAdm;
    Grupo grupo;

    DialogTurmaAdm dialogTurmaAdm;
    Turma turma;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public BottomSheetCriarEncontro(Context context, Grupo grupo, DialogGrupoAdm dialogGrupoAdm, FragmentManager fragmentManager) {
        this.context = context;
        this.grupo = grupo;
        this.dialogGrupoAdm = dialogGrupoAdm;
        this.fragmentManager = fragmentManager;
    }

    public BottomSheetCriarEncontro(Context context, Turma turma, DialogTurmaAdm dialogTurmaAdm, FragmentManager fragmentManager) {
        this.context = context;
        this.turma = turma;
        this.dialogTurmaAdm = dialogTurmaAdm;
        this.fragmentManager = fragmentManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_criar_encontro, container, false);
        token = TokenManager.getInstance(context).getAccessToken();
        encontroService = Generator.getRetrofitInstance().create(EncontroService.class);

        temaDoEncontro = view.findViewById(R.id.nomealuno);
        data = view.findViewById(R.id.data);
        buttonDt = view.findViewById(R.id.button_dt);
        inicio = view.findViewById(R.id.datainicio);
        buttonInicio = view.findViewById(R.id.button_inicio);
        fim = view.findViewById(R.id.datafim);
        buttonFim = view.findViewById(R.id.button_fim);
        localselecionado = view.findViewById(R.id.localselecionado);
        descricao = view.findViewById(R.id.descricao);
        btnEscolherlocal = view.findViewById(R.id.btn_escolherlocal);
        btnConcluir = view.findViewById(R.id.btn_concluir);

        inicio.addTextChangedListener(new MaskWatcher(inicio, "##:##"));
        fim.addTextChangedListener(new MaskWatcher(fim, "##:##"));

        buttonDt.setOnClickListener(view1 -> openDatePicker(calendar, data, context, null, new Date()));

        buttonInicio.setOnClickListener(v -> openTimePickerDialog(calendar, inicio, context));
        buttonFim.setOnClickListener(v -> openTimePickerDialog(calendar, fim, context));

        btnEscolherlocal.setOnClickListener(view1 -> {
            escolherLocal();
        });


        btnConcluir.setOnClickListener(view12 -> concluir());
        return view;
    }

    private void escolherLocal() {
        if(validarDuracaoEncontro(true)){
            new DialogEscolherLocal(context, fragmentManager, this, getDataInicio(), getDataFim()).show(fragmentManager, "tag");
        }
    }

    private void concluir() {
        if (validarCampos()){
            encontroService.createEncontro(getEncontro(), token).enqueue(new Callback<Encontro>() {
                @Override
                public void onResponse(Call<Encontro> call, Response<Encontro> response) {
                    if (response.isSuccessful()){
                        Encontro encontro = Objects.requireNonNull(response.body());
                        Local localEncontro = encontro.getAgendamento().getLocal();
                        new AlertDialogManager(
                                context,
                                "Sucesso!",
                                "Encontro criado com sucesso!" + "\n"+
                                        "Tema: " + encontro.getTema() + "\n"+
                                        "Local: " + (localEncontro!=null?localEncontro.getNome():"Online") + "\n"+
                                        "Dia: " + data.getText() + "\n"+
                                        "Horário: " + inicio.getText().toString() + " - " + fim.getText().toString())
                                .show();
                        if(dialogGrupoAdm!=null)dialogGrupoAdm.searchEncontros();
                        else dialogTurmaAdm.searchEncontros();
                        dismiss();
                    }else {
                        showError("Não foi possível criar encontro: ", response, context);
                    }
                }

                @Override
                public void onFailure(Call<Encontro> call, Throwable t) {

                }
            });

            dismiss();
        }
    }

    public Encontro getEncontro(){
        Encontro encontro = new Encontro();
        encontro.setTema(temaDoEncontro.getText().toString());
        encontro.setDescricao(descricao.getText().toString());
        encontro.setTema(temaDoEncontro.getText().toString());
        encontro.setAgendamento(getAgendamento());
        if(grupo!=null)encontro.setGrupo(grupo);
        else encontro.setTurma(turma);
        return encontro;
    }

    private String getDataInicio() {
        return String.format("%s %s", data.getText(), inicio.getText().toString());
    }

    private String getDataFim() {
        return String.format("%s %s", data.getText(), fim.getText().toString());
    }

    private Agendamento getAgendamento() {
        Agendamento agendamento = new Agendamento();
        agendamento.setLocal(localEncontro);
        try {
            agendamento.setStartTime(sdf.parse(getDataInicio()));
            agendamento.setEndTime(sdf.parse(getDataFim()));
        } catch (ParseException ignored) {}
        return agendamento;
    }

    private boolean validarCampos() {
        boolean valida=true;
        //verifica obrigatorios
        String temaString = temaDoEncontro.getText().toString();
        String dataString = data.getText();

        if (TextUtils.isEmpty(temaString)) {
            temaDoEncontro.setError("Campo obrigatório");
            temaDoEncontro.requestFocus();
            valida = false;
        }

        if (TextUtils.isEmpty(dataString)) {
            Toast.makeText(context, "Data é obrigatório", Toast.LENGTH_LONG).show();
            data.setError("Data é obrigatório");
            data.requestFocus();
            valida = false;
        }

        return validarDuracaoEncontro(valida);
    }

    private Boolean validarDuracaoEncontro(boolean valida){

        String inicioString = inicio.getText().toString();
        String fimString = fim.getText().toString();

        if (TextUtils.isEmpty(inicioString)) {
            inicio.setError("Campo obrigatório");
            inicio.requestFocus();
            valida = false;
        }

        if (TextUtils.isEmpty(fimString)) {
            fim.setError("Campo obrigatório");
            fim.requestFocus();
            valida = false;
        }

        // Verifica se o campo de início está no formato HH:mm
        if (!TextUtils.isEmpty(inicioString) && !inicioString.matches("\\d{2}:\\d{2}")) {
            inicio.setError("Formato inválido. Use HH:mm!");
            inicio.requestFocus();
            valida = false;
        }

        // Verifica se o campo de fim está no formato HH:mm
        if (!TextUtils.isEmpty(fimString) && !fimString.matches("\\d{2}:\\d{2}")) {
            fim.setError("Formato inválido. Use HH:mm!");
            fim.requestFocus();
            valida = false;
        }

        // Validar duração máxima do encontro (4 horas)
        try{
            int horaInicio = Integer.parseInt(inicioString.split(":")[0]);
            int minInicio = Integer.parseInt(inicioString.split(":")[1]);
            int horaFim = Integer.parseInt(fimString.split(":")[0]);
            int minFim = Integer.parseInt(fimString.split(":")[1]);

            // Calculando a diferença em minutos entre o início e o fim do encontro
            int minutosInicio = horaInicio * 60 + minInicio;
            int minutosFim = horaFim * 60 + minFim;
            int duracaoMinutos = minutosFim - minutosInicio;

            if (duracaoMinutos > 240 || duracaoMinutos <= 0) { // Máximo de 4 horas (240 minutos)
                fim.setError("Duração máxima é de 4 horas!");
                fim.requestFocus();
                valida = false;
            }
        }catch (NumberFormatException ignored) {}

        return valida;
    }

}