package com.if_connect;

import static com.if_connect.utils.CustoAnimation.startAndEndAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.if_connect.R;
import com.if_connect.bottomsheets.BottomSheetTelaInicial;
import com.if_connect.request.Generator;
import com.if_connect.request.requestbody.AuthenticationResponse;
import com.if_connect.request.services.AuthService;
import com.if_connect.utils.TokenManager;

import java.io.IOException;

import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ImageView btnCima;
    private Animation slideUpAnimation;
    private Animation slideDownAnimation;
    private float yStart;

    FragmentManager fragmentManager;
    Context context;
    AuthService authService;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authService = Generator.getRetrofitInstance().create(AuthService.class);
        context = this;
        getSupportActionBar().hide();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String refreshtoken = TokenManager.getInstance(context).getRefreshToken();
        if(refreshtoken!=null){
            try {
                Response<AuthenticationResponse> response = authService.refreshtoken(refreshtoken).execute();
                if(response.isSuccessful()){
                    AuthenticationResponse auth = response.body();
                    if(auth!=null){
                        TokenManager.getInstance(context).saveTokens(auth.accessToken, auth.refreshToken);
                        startActivity(new Intent(context, MainActivity.class));
                        finish();
                    }
                }else {
                    TokenManager.getInstance(context).deleteTokens();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        setContentView(R.layout.activity_login);
        fragmentManager = getSupportFragmentManager();

        btnCima = findViewById(R.id.btn_cima);

        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        btnCima.setOnTouchListener(new ChoiceTouchListener());
    }

    private final class ChoiceTouchListener implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    yStart = event.getY();
                    return true;
                case MotionEvent.ACTION_UP:
                    float yEnd = event.getY();
                    if (yEnd - yStart < -100) {
                        startAndEndAnimation(btnCima, slideUpAnimation, slideDownAnimation);
                        new BottomSheetTelaInicial(context).show(fragmentManager, "tag");
                    }
                    return true;
            }
            return false;
        }
    }
}