package com.entranceaptitude;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeScreen extends AppCompatActivity {

    TextView heading;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
         heading=(TextView)findViewById(R.id.welcome_heading);
         logo=(ImageView)findViewById(R.id.welcome_logo);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.heading_logo);
        heading.setAnimation(animation);
        Animation animation1=AnimationUtils.loadAnimation(this,R.anim.bounce);
        logo.setAnimation(animation1);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
              startActivity(new Intent(WelcomeScreen.this,MainActivity.class));
              finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
     }
}