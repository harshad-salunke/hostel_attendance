package com.student.hostel_application.Activitys;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.student.hostel_application.R;
import com.student.hostel_application.models.DeleteAcount;
import com.student.hostel_application.models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeletedActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Button cancel,close;
    ImageView image;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences.Editor editor;
    String uid;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted);
        sharedPreferences=getSharedPreferences("delete",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        uid= FirebaseAuth.getInstance().getUid();
        Toast.makeText(this, ""+uid, Toast.LENGTH_SHORT).show();
        image=findViewById(R.id.delete_account_img);
        cancel=findViewById(R.id.cancel_delete);
        close=findViewById(R.id.deleted_acount);

        Glide.with(this).load(R.drawable.sad).into(image);

        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Admin").child("delete_accounts").child(uid);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = ProgressDialog.show(DeletedActivity.this, "",
                        "Checking. Please wait...", true);

               checkPresentorNot();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void delete_by_admin() {
        AlertDialog.Builder alert_builder = new AlertDialog.Builder(DeletedActivity.this);
        alert_builder.setTitle("Your Account Deleted by Admin ")
                .setMessage("To Restore Your account please fill some imformation it take few minute only")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(DeletedActivity.this, RestoreActivity.class);
                        startActivity(intent);

                    }
                }).show();
        dialog.cancel();

    }

    private void cancel_to_delete() {
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                editor.putString("delete","no");
                editor.commit();

                AlertDialog.Builder alert_builder = new AlertDialog.Builder(DeletedActivity.this);
                alert_builder.setTitle("Account Deletion Canceled")
                        .setMessage(" \uD83D\uDE0A \uD83E\uDD1D Now you can use your app")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Intent intent=new Intent(DeletedActivity.this, SplashActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        }).show();
            }
        });
    }

    private void checkPresentorNot() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DeleteAcount deleteAcount = snapshot.getValue(DeleteAcount.class);
                if(deleteAcount!=null){
                    cancel_to_delete();
                }else {
                    delete_by_admin();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}