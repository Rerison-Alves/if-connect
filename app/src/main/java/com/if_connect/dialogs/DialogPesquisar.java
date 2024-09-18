package com.if_connect.dialogs;

import static com.if_connect.utils.typeadapters.SpinnerAdapter.getAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.LoginActivity;
import com.if_connect.MainActivity;
import com.if_connect.bottomsheets.BottomSheetShape;
import com.if_connect.fragments.FragmentCPEmail;
import com.if_connect.models.Agrupamento;
import com.if_connect.models.Curso;
import com.if_connect.models.Usuario;
import com.if_connect.recycleviews.RecyclerViewTodos;
import com.if_connect.request.Generator;
import com.if_connect.request.requestbody.Page;
import com.if_connect.request.services.AgrupamentoService;
import com.if_connect.request.services.CursoService;
import com.if_connect.utils.BounceEditText;
import com.if_connect.utils.SpeechToText;
import com.if_connect.utils.TokenManager;
import com.if_connect.utils.UsuarioManager;

import java.util.List;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogPesquisar extends DialogFragment {

    Context context;
    FragmentManager fragmentManager;
    String filtroPesquisa;
    Integer cursoId;

    String token;
    Usuario usuarioLogado;

    AgrupamentoService agrupamentoService;
    CursoService cursoService;

    List<Curso> cursosList;
    String[] nomeCursos = new String[]{};

    ImageView voltar;
    AutoCompleteTextView pesquisar;
    ImageButton speechButton;
    CheckBox filtrarPorUsuario;
    Spinner cursoSpinner;
    RecyclerView recycleAgrupamentos;

    List<Agrupamento> agrupamentoList;

    SpeechToText speechToText;

    public DialogPesquisar(Context context, FragmentManager fragmentManager, String filtroPesquisa) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.filtroPesquisa = filtroPesquisa;
    }

    public DialogPesquisar(Context context, FragmentManager fragmentManager, String filtroPesquisa, Integer cursoId) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.filtroPesquisa = filtroPesquisa;
        this.cursoId = cursoId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Ifconnect);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pesquisar, container, false);
        token = TokenManager.getInstance(context).getAccessToken();
        usuarioLogado = UsuarioManager.getUsuarioLogado();
        agrupamentoService = Generator.getRetrofitInstance().create(AgrupamentoService.class);
        cursoService = Generator.getRetrofitInstance().create(CursoService.class);

        voltar = view.findViewById(R.id.voltar);
        pesquisar = view.findViewById(R.id.pesquisar);
        speechButton = view.findViewById(R.id.btnVoice);
        filtrarPorUsuario = view.findViewById(R.id.filtrar_por_usuario);
        cursoSpinner = view.findViewById(R.id.curso_spinner);
        recycleAgrupamentos = view.findViewById(R.id.agrupamentos);

        voltar.setOnClickListener(v -> dismiss());

        speechToText =  new SpeechToText(this, s -> pesquisar.setText(s));
        speechButton.setOnClickListener(v -> speechToText.speak());

        pesquisar.setText(filtroPesquisa);

        new BounceEditText(pesquisar, 500, text -> {
            filtroPesquisa = text.toString();
            searcherAgrupamento(filtroPesquisa);
        });

        filtrarPorUsuario.setOnClickListener(v -> searcherAgrupamento(filtroPesquisa));
        cursoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searcherAgrupamento(filtroPesquisa);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        requestCursos();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        speechToText.onActivityResult(requestCode, resultCode, data);
    }

    private void requestCursos() {
        cursoService.getAllCursos().enqueue(new Callback<List<Curso>>() {
            @Override
            public void onResponse(Call<List<Curso>> call, Response<List<Curso>> response) {
                if(response.isSuccessful()){
                    cursosList = response.body();
                    if(cursosList!=null && !cursosList.isEmpty()){
                        nomeCursos = Stream.concat(
                                        Stream.of("Todos"),
                                        cursosList.stream().map(Curso::getDescricao))
                                .toArray(String[]::new);
                        cursoSpinner.setAdapter(getAdapter(nomeCursos, context));
                        if(cursoId!=null) setCurso(cursoId);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Curso>> call, Throwable t) {

            }
        });
    }

    private Curso getCurso() {
        if(cursosList==null || cursosList.isEmpty()){
            return null;
        }else {
            int position = (int)cursoSpinner.getSelectedItemId()-1;
            return position>=0?cursosList.get(position):null;
        }
    }

    private void setCurso(Integer cursoId){
        cursoSpinner.setSelection(
                cursosList.indexOf(
                        cursosList.stream()
                                .filter(curso -> curso.getId().equals(cursoId))
                                .findFirst().orElse(null)
                ) + 1
        );
    }

    private void searcherAgrupamento(String filtro) {
        Curso curso = getCurso();
        agrupamentoService.getAgrupamentosPageable(
                filtro,
                filtrarPorUsuario.isChecked()? usuarioLogado.getId().toString():"",
                curso!=null?curso.getId().toString():"",
                "nome",
                0,
                Integer.MAX_VALUE,
                token)
                .enqueue(new Callback<Page<Agrupamento>>() {
            @Override
            public void onResponse(Call<Page<Agrupamento>> call, Response<Page<Agrupamento>> response) {
                if(response.isSuccessful() && response.body() != null){
                    agrupamentoList = response.body().getContent();
                    listarAgrupamentos();
                }
            }

            @Override
            public void onFailure(Call<Page<Agrupamento>> call, Throwable t) {

            }
        });
    }

    private void listarAgrupamentos() {
        recycleAgrupamentos.setLayoutManager(new LinearLayoutManager(context));
        recycleAgrupamentos.setAdapter(new RecyclerViewTodos(context, fragmentManager, agrupamentoList));
    }


}