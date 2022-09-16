package com.student.hostel_application;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.student.hostel_application.models.Notice;
import com.google.gson.Gson;

public class NoticeBoardActivity extends AppCompatActivity {
   Notice notice;
   TextView noti_title,noti_msg,noti_month,noti_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        Intent intent=getIntent();
        Gson gson = new Gson();
        notice = gson.fromJson(intent.getStringExtra("myjson"), Notice.class);
        noti_title=findViewById(R.id.noti_board_title);
        noti_msg=findViewById(R.id.noti_board_msg);
        noti_month=findViewById(R.id.noti_board_month);
        noti_date=findViewById(R.id.noti_board_date);

        String arr[]=notice.getDate().split(" ");
        noti_date.setText(arr[0]);
        noti_month.setText(arr[1]);
        noti_title.setText(notice.getTitle());
        noti_msg.setText(notice.getMsg());


        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
        findViewById(R.id.hostel_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:8956109871"));
                startActivity(intent);
            }
        });

        findViewById(R.id.hostel_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:8956109871");
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(i);
            }
        });
    }
}