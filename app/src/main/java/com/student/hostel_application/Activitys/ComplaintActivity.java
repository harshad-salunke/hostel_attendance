package com.student.hostel_application.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.student.hostel_application.Notification.FcmNotificationsSender;
import com.student.hostel_application.R;
import com.student.hostel_application.models.Complaints;
import com.student.hostel_application.models.DeleteAcount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ComplaintActivity extends AppCompatActivity {
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
Button send_data;
ImageView back_btn;
EditText complaint_text;
TextInputLayout complaint_layout;
String UserUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Admin");
        UserUid= FirebaseAuth.getInstance().getUid();
        initView();
        send_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendComplaint();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public  void initView(){
        send_data=findViewById(R.id.send_complaints);
        back_btn=findViewById(R.id.back_btn);
        complaint_text=findViewById(R.id.complaint_text);
    }

    private void sendComplaint() {
        String msg_str=complaint_text.getText().toString();
        if(msg_str.equals("")){
            complaint_layout.setError("Required..");
            return;
        }
        ProgressDialog dialog = ProgressDialog.show(ComplaintActivity.this, "",
                "Sending. Please wait...", true);
        String key=databaseReference.push().getKey();
        Complaints complaints=new Complaints(UserUid,key,msg_str,getDate());
        databaseReference.child("complaints").child(key).setValue(complaints).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.cancel();
                complaint_text.setText("");
                AlertDialog.Builder alert_builder = new AlertDialog.Builder(ComplaintActivity.this);
                alert_builder.setTitle("Complaint Sended Successfully")
                        .setMessage(" We Will Solve Your Problem Within Few Days. Thank You")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               complaint_text.setText("");
                                FcmNotificationsSender notificationsSender=new FcmNotificationsSender("/topics/admin","⚠️ Complain Alert   ️⚠️"," Complaint Received",getApplicationContext(),ComplaintActivity.this);
                                notificationsSender.SendNotifications();
                                finish();
                            }
                        }).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ComplaintActivity.this, "Some thing is wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private  String getDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        return  dtf.format(now);
    }
}