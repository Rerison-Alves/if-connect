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
import com.if_connect.bottomsheets.BottomSheetEditarGrupo;
import com.if_connect.dialogs.DialogGrupoAdm;
import com.if_connect.fragments.PerfilAluno;
import com.if_connect.models.Grupo;
import com.if_connect.models.Usuario;
import com.if_connect.request.services.GrupoService;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewPerfilAluno extends RecyclerView.Adapter<RecyclerViewPerfilAluno.ViewHolder> {
    List<Grupo> grupos;
    Context context;
    View view;
    ViewHolder viewHolder;
    FragmentManager fragmentManager;
    GrupoService grupoService;
    PerfilAluno perfilAluno;
    String token;
    Usuario admin;

    public RecyclerViewPerfilAluno(List<Grupo> grupos, PerfilAluno perfilAluno, Context context, FragmentManager fragmentManager, GrupoService grupoService, String token, Usuario admin) {
        this.grupos = grupos;
        this.perfilAluno = perfilAluno;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.grupoService = grupoService;
        this.token = token;
        this.admin = admin;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nomedogrupo, areadogrupo;
        public ImageButton edit, excluir;
        public FrameLayout consulta;
        public ViewHolder(View v){
            super(v);
            nomedogrupo = v.findViewById(R.id.textviewPrincipal);
            areadogrupo = v.findViewById(R.id.areadogrupo);
            edit = v.findViewById(R.id.edit);
            excluir= v.findViewById(R.id.delete);
            consulta=v.findViewById(R.id.consulta);
        }
    }

    public RecyclerViewPerfilAluno.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
    view = LayoutInflater.from(context).inflate(R.layout.recycle_grupos_perfil, parent, false);
    viewHolder = new ViewHolder(view);
    return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position){
        holder.nomedogrupo.setText(grupos.get(position).getNome());
        holder.areadogrupo.setText(grupos.get(position).getAreadeEstudo());
        holder.consulta.setOnClickListener(view -> {
            new DialogGrupoAdm(grupos.get(position), context, fragmentManager).show(fragmentManager, "tag");
        });
        holder.edit.setOnClickListener(v -> editGrupo(grupos.get(position)));
        holder.excluir.setOnClickListener(view -> confirmaExcluirGrupo(grupos.get(position)));

    }

    private void editGrupo(Grupo grupo) {
        new BottomSheetEditarGrupo(context, admin, fragmentManager, perfilAluno, grupo).show(fragmentManager, "tag");
    }

    private void confirmaExcluirGrupo(Grupo grupo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Ao fazer isso seu grupo será excluido permanentemente!")
                .setTitle("Deseja excluir "+ grupo.getNome()+ "?");

        builder.setNegativeButton(R.string.sim, (dialog, id) -> excluirGrupo(grupo.getId()));
        builder.setPositiveButton(R.string.nao, (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }

    private void excluirGrupo(Integer grupoId) {
        grupoService.deleteGrupo(grupoId, token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context, "Grupo excluido com sucesso!", Toast.LENGTH_SHORT).show();
                }else {
                    showError("Não foi possível excluir grupo: ", response, context);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getItemCount(){
        return grupos.size();
    }
}