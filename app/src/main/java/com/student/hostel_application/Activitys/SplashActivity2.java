package com.student.hostel_application.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.student.hostel_application.Login.Login_info;
import com.student.hostel_application.R;

public class SplashActivity2 extends AppCompatActivity {
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        imageView=findViewById(R.id.loading_image2);



        Glide.with(this).load(R.drawable.loading_gear).into(imageView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity2.this, Login_info.class);
                startActivity(intent);
                finish();
            }
        },1000);
    }
}