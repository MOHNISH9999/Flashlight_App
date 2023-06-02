package com.example.flashlight_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public
class splashscreen extends AppCompatActivity {

    TextView appname;
    LottieAnimationView lottie;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_splashscreen );

        appname  = findViewById(R.id.appname);
        lottie=findViewById(R.id.lottie);

        appname.animate().translationY(-1800).setDuration(2700).setStartDelay(0);
        lottie.animate().translationX (2000).setDuration(2000).setStartDelay(2900);

        new Handler ().postDelayed ( new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), MainActivity.class); startActivity(i);
            }
        },  5000);
    }
}