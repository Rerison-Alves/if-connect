package com.if_connect.dialogs;

import static android.app.Activity.RESULT_OK;

import static com.if_connect.utils.ErrorManager.showError;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.example.if_connect.R;
import com.if_connect.request.Generator;
import com.if_connect.request.services.UsuarioService;
import com.if_connect.utils.ImageManager;
import com.if_connect.utils.TokenManager;
import com.if_connect.utils.UsuarioManager;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogAlterarImagemPerfil extends DialogFragment {

    Context context;
    Activity activity;
    ImageView imagemPerfilUsuario;
    String imagemPerfil;
    UsuarioService usuarioService;
    String token;

    Bitmap novaImagemBitmap;

    public DialogAlterarImagemPerfil(Context context, ImageView imagemPerfilUsuario, String imagemPerfil) {
        this.context = context;
        activity = (Activity) context;
        this.imagemPerfilUsuario = imagemPerfilUsuario;
        this.imagemPerfil = imagemPerfil;
    }

    private static final int PICK_IMAGE_REQUEST = 1;

    CardView profileCardView, cancelar, confirmar;
    ImageView profileImageView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_alterar_imagem, null);
        builder.setView(v);
        activity = (Activity) context;
        usuarioService = Generator.getRetrofitInstance().create(UsuarioService.class);
        token = TokenManager.getInstance(context).getAccessToken();

        profileCardView = v.findViewById(R.id.profileCardView);
        profileImageView = v.findViewById(R.id.profileImageView);

        cancelar = v.findViewById(R.id.cancelar);
        confirmar = v.findViewById(R.id.confirmar);

        Bitmap image = ImageManager.base64StringToBitmap(imagemPerfil);
        if(image!=null) profileImageView.setImageBitmap(image);

        profileCardView.setOnClickListener(view -> selectImage());

        cancelar.setOnClickListener(view -> dismiss());
        confirmar.setOnClickListener(view -> alterarImagem());

        return builder.create();
    }

    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            startUCrop(uri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            handleUCropResult(data);
        }
    }

    private void startUCrop(Uri sourceUri) {
        UCrop.Options options = new UCrop.Options();
        options.setStatusBarColor(Color.DKGRAY);
        options.setActiveControlsWidgetColor(Color.parseColor("#a1a1a1"));

        Uri destinationUri = Uri.fromFile(new File(context.getCacheDir(), "IMG_" + System.currentTimeMillis()));
        UCrop.of(sourceUri, destinationUri)
        .withAspectRatio(1, 1) // Define a proporção desejada (1:1 para um quadrado)
        .withMaxResultSize(512, 512) // Define o tamanho máximo da imagem resultante
        .withOptions(options)
        .start(context, this, UCrop.REQUEST_CROP);
    }

    private void handleUCropResult(Intent data) {
        final Uri resultUri = UCrop.getOutput(data);
        if (resultUri != null) {
            try {
                novaImagemBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), resultUri);
                profileImageView.setImageBitmap(novaImagemBitmap);
            } catch (IOException ignored) {}
        }
    }

    private void alterarImagem(){
        if(novaImagemBitmap!=null){
            confirmar.setClickable(false);
            imagemPerfil = ImageManager.bitmapToBase64String(novaImagemBitmap);
            usuarioService.changeProfileImage(imagemPerfil, token).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    confirmar.setClickable(true);
                    if(response.isSuccessful()){
                        new AlertDialogManager(context, "Imagem do perfil alterada!", "A imagem do perfil foi alterada com sucesso!").show();
                        dismiss();
                        UsuarioManager.requestUsuario(token, context);
                        imagemPerfilUsuario.setImageBitmap(novaImagemBitmap);
                    }else {
                        showError("Não foi possível alterar imagem: ", response, context);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    confirmar.setClickable(true);
                }
            });
        }else {
            Toast.makeText(context, "Nenhuma imagem selecionada!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Window dialog = getDialog().getWindow();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.setLayout(width, height);
            dialog.setGravity(Gravity.CENTER);
        }
    }
}