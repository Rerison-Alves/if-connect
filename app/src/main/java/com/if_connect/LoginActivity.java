package com.if_connect;

import static com.if_connect.utils.CustoAnimation.startAndEndAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    private ImageView btnCima;
    private Animation slideUpAnimation;
    private Animation slideDownAnimation;
    private float yStart;

    FragmentManager fragmentManager;
    Context context;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        fragmentManager = getSupportFragmentManager();
        getSupportActionBar().hide();

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