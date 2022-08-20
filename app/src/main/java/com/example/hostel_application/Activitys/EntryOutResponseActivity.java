package com.example.hostel_application.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.hostel_application.R;

public class EntryOutResponseActivity extends AppCompatActivity {
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_out_response);
        imageView=findViewById(R.id.review_img);
        Glide.with(this).load(R.drawable.successfuly).into(imageView);
    }
}