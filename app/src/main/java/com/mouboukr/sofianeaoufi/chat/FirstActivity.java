package com.mouboukr.sofianeaoufi.chat;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

public class FirstActivity extends AppCompatActivity {
Button log,reg;
TextView text;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        log=(Button)findViewById(R.id.log_btn);
        reg=(Button)findViewById(R.id.register_btn);
         text=(TextView)findViewById(R.id.txt);


        ObjectAnimator anim = ObjectAnimator.ofInt(text, "backgroundColor", Color.GREEN, Color.WHITE,
                R.color.myColor);
        anim.setDuration(1500);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();

    }





    public void regitration(View view) {
        Intent i=new Intent(FirstActivity.this,RegisterActivity.class);
        startActivity(i);
        return;


    }

    public void signin(View view) {
        Intent i=new Intent(FirstActivity.this,LoginActivity.class);
        startActivity(i);
        //return;


    }

    public void about(View view) {

    }






}
