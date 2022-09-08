package com.example.hostel_application.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hostel_application.R;

public class PrivacyPolicyActivity extends AppCompatActivity {
TextView email;
ImageView back_btn,my_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        email=findViewById(R.id.email);
        back_btn=findViewById(R.id.back_btn);
        my_logo=findViewById(R.id.my_logo);
        Glide.with(this).load(R.drawable.my_logo).into(my_logo);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_SENDTO);
//                intent.setData(Uri.parse("mailto:harshadsalunke9696@gmail.com")); // only email apps should handle this
//                intent.putExtra(Intent.EXTRA_SUBJECT, );
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(intent);
//                }

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "harshadsalunke9696@gmail.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "I have some issue with privacy policy");

                startActivity(Intent.createChooser(emailIntent, "Harshad salunke"));

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}