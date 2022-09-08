package com.example.hostel_application.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hostel_application.R;
import com.example.hostel_application.models.DeleteAcount;
import com.example.hostel_application.models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AcountDeleteActivity extends AppCompatActivity {
Button delete,cancle;
ImageView back_btn;
EditText delete_msg;
TextInputLayout delete_layout;
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
Student student;

SharedPreferences sharedPreferences;
SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount_delete);

        Intent intent=getIntent();
        String json_obj=intent.getStringExtra("myJson");
        Gson gson = new Gson();
        student = gson.fromJson(json_obj, Student.class);

        initView();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Admin");

        sharedPreferences=getSharedPreferences("delete",MODE_PRIVATE);
        editor=sharedPreferences.edit();



        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccount();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AcountDeleteActivity.this, "Thank You", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void deleteAccount() {
        String msg_str=delete_msg.getText().toString();
        if(msg_str.equals("")){
            delete_layout.setError("Required..");
            return;
        }
        ProgressDialog dialog = ProgressDialog.show(AcountDeleteActivity.this, "",
                "Deleting. Please wait...", true);
        DeleteAcount deleteAcount=new DeleteAcount(student.getName(),student.getRoom_No(),msg_str,student.getUid(),student.getResiter_Date(),getCurenntTime());

        databaseReference.child("delete_accounts").child(student.getUid()).setValue(deleteAcount).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                editor.putString("delete","yes");
                editor.putString("uid",student.getUid());
                editor.putString("mobile",student.getMobile_no());
                editor.commit();
                dialog.cancel();

                AlertDialog.Builder alert_builder = new AlertDialog.Builder(AcountDeleteActivity.this);
                alert_builder.setTitle("Request was Sended")
                        .setMessage("\uD83D\uDE14 Your Account will be delete within few minutes. \uD83D\uDE0A \uD83E\uDD1D  Thank You "+student.getName() +" \uD83E\uDD1D  We will meet again")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(AcountDeleteActivity.this,DeletedActivity.class);
                                startActivity(intent);
                            }
                        }).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AcountDeleteActivity.this, "Some thing is wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private String getCurenntTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm aa");
        String formattedDate = dateFormat.format(new Date()).toString();
        return  formattedDate;
    }
    private void initView() {
        delete=findViewById(R.id.delelte_acount);
        cancle=findViewById(R.id.cancel_acount);
        back_btn=findViewById(R.id.back_btn);
        delete_msg=findViewById(R.id.entryout_resons);
        delete_layout=findViewById(R.id.delete_layout);
    }
}