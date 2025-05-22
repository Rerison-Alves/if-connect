package com.if_connect.recycleviews;

import static com.if_connect.utils.ErrorManager.showError;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.bottomsheets.BottomSheetEditarTurma;
import com.if_connect.dialogs.DialogTurmaAdm;
import com.if_connect.fragments.PerfilProfessor;
import com.if_connect.models.Turma;
import com.if_connect.models.Usuario;
import com.if_connect.request.services.TurmaService;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewPerfilProfessor extends RecyclerView.Adapter<RecyclerViewPerfilProfessor.ViewHolder> {
    List<Turma> turmas;
    Context context;
    View view;
    ViewHolder viewHolder;
    FragmentManager fragmentManager;
    TurmaService turmaService;
    PerfilProfessor perfilProfessor;
    String token;
    Usuario admin;

    public RecyclerViewPerfilProfessor(List<Turma> turmas, PerfilProfessor perfilProfessor, Context context, FragmentManager fragmentManager, TurmaService turmaService, String token, Usuario admin) {
        this.turmas = turmas;
        this.perfilProfessor = perfilProfessor;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.turmaService = turmaService;
        this.token = token;
        this.admin = admin;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nomeTurma, disciplina, turno;
        public ImageButton edit, excluir;
        public FrameLayout consulta;
        public ViewHolder(View v) {
            super(v);
            nomeTurma = v.findViewById(R.id.nome_turma);
            disciplina = v.findViewById(R.id.disciplina);
            turno = v.findViewById(R.id.turno);
            edit = v.findViewById(R.id.edit);
            excluir= v.findViewById(R.id.delete);
            consulta=v.findViewById(R.id.consulta);
        }
    }

    public RecyclerViewPerfilProfessor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    view = LayoutInflater.from(context).inflate(R.layout.recycle_turmas_perfil, parent, false);
    viewHolder = new ViewHolder(view);
    return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nomeTurma.setText(turmas.get(position).getNome());
        holder.disciplina.setText(turmas.get(position).getDisciplina());
        holder.turno.setText(turmas.get(position).getTurno().name());
        holder.consulta.setOnClickListener(view -> {
            new DialogTurmaAdm(turmas.get(position), context, fragmentManager).show(fragmentManager, "tag");
        });
        holder.edit.setOnClickListener(v -> editTurma(turmas.get(position)));
        holder.excluir.setOnClickListener(view -> confirmaExcluirTurma(turmas.get(position)));

    }

    private void editTurma(Turma turma) {
        new BottomSheetEditarTurma(context, admin, fragmentManager, perfilProfessor, turma).show(fragmentManager, "tag");
    }

    private void confirmaExcluirTurma(Turma turma) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Ao fazer isso sua turma será excluida permanentemente!")
                .setTitle("Deseja excluir "+ turma.getNome()+ "?");

        builder.setNegativeButton(R.string.sim, (dialog, id) -> excluirTurma(turma.getId()));
        builder.setPositiveButton(R.string.nao, (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }

    private void excluirTurma(Integer turmaId) {
        turmaService.deleteTurma(turmaId, token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Turma excluida com sucesso!", Toast.LENGTH_SHORT).show();
                }else {
                    showError("Não foi possível excluir turma: ", response, context);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getItemCount() {
        return turmas.size();
    }
}