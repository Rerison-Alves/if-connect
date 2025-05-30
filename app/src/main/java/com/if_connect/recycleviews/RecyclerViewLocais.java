package com.if_connect.recycleviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.bottomsheets.BottomSheetCriarEncontro;
import com.if_connect.dialogs.DialogEscolherLocal;
import com.if_connect.models.Local;
import com.if_connect.utils.ImageManager;

import java.util.List;

public class RecyclerViewLocais extends RecyclerView.Adapter<RecyclerViewLocais.ViewHolder> {
    List<Local> locais;
    Context context;
    View view;
    ViewHolder viewHolder;
    FragmentManager fragmentManager;
    DialogEscolherLocal dialogEscolherLocal;
    BottomSheetCriarEncontro bottomSheetCriarEncontro;

    public RecyclerViewLocais(Context context, FragmentManager fragmentManager, List<Local> locais, DialogEscolherLocal dialogEscolherLocal, BottomSheetCriarEncontro bottomSheetCriarEncontro) {
        this.locais = locais;
        this.fragmentManager=fragmentManager;
        this.context=context;
        this.dialogEscolherLocal=dialogEscolherLocal;
        this.bottomSheetCriarEncontro=bottomSheetCriarEncontro;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageLocal;
        public TextView nomeDoLocal;
        public FrameLayout escolher;
        public ViewHolder(View v) {
            super(v);
            imageLocal = v.findViewById(R.id.image_local);
            nomeDoLocal = v.findViewById(R.id.nome_local);
            escolher = v.findViewById(R.id.escolher);
        }
    }

    public RecyclerViewLocais.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    view = LayoutInflater.from(context).inflate(R.layout.recycle_card_local, parent, false);
    viewHolder = new ViewHolder(view);
    return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        String localName = String.format("%s - %s", locais.get(position).getNome(), locais.get(position).getLocalizacao());
        holder.nomeDoLocal.setText(localName);
        String imagebase64 = locais.get(position).getIconeBase64();
        if (imagebase64!=null) {
            holder.imageLocal.setImageBitmap(ImageManager.base64StringToBitmap(imagebase64));
        }
        holder.escolher.setOnClickListener(view -> {
            //atualizar campos
            bottomSheetCriarEncontro.localEncontro = locais.get(position);
            bottomSheetCriarEncontro.localselecionado.setText(localName);
            dialogEscolherLocal.dismiss();
        });
    }


    public int getItemCount() {
        return locais.size();
    }
}