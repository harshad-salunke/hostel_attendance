package com.student.hostel_application.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.student.hostel_application.R;
import com.student.hostel_application.models.EntryOutModel;
import com.student.hostel_application.models.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class EntryOutResponseActivity extends AppCompatActivity {
ImageView imageView,profile_img;
EntryOutModel entryOutModel;
Student student;

TextView from_data,to_date,name,room ,branch,year,accpted,valid;
ScrollView scrollView;
LinearLayout linearLayout2;
ImageButton delete_pass;
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_out_response);
        Gson gson=new Gson();
        String data=getIntent().getStringExtra("obj");
        entryOutModel=gson.fromJson(data,EntryOutModel.class);
        sharedPreferences=getSharedPreferences("user_info",MODE_PRIVATE);
        String student_str=sharedPreferences.getString("user","");
        Gson gson1=new Gson();
        student=gson1.fromJson(student_str,Student.class);

        if(student.getUid()==null || student.getUid().equals("")){
            String uid= FirebaseAuth.getInstance().getUid();
            student.setUid(uid);
        }

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Admin").child("entryout").child(entryOutModel.getId());
        initView();
        setDataToView();
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        delete_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert_builder = new AlertDialog.Builder(
                        EntryOutResponseActivity.this);
                alert_builder.setTitle("Are you sure for  Delete ?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface di, int which) {
                            ProgressDialog    dialog = ProgressDialog.show(EntryOutResponseActivity.this, "",
                                        "Deleting. Please wait...", true);
                            databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(EntryOutResponseActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EntryOutResponseActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.cancel();
                                finish();
                            }
                        }).setNegativeButton("No",null).show();
            }
        });
    }

    private void setDataToView() {
        name.setText(student.getName());
        from_data.setText(entryOutModel.getFrom_date());
        to_date.setText(entryOutModel.getTo_date());
        room.setText(student.getRoom_No());
        branch.setText(student.getClg_branch());
        year.setText(student.getClg_year());

        if(student.getGender().equals("Male")){
            profile_img.setImageResource(R.drawable.college_boy);

        }else {
            profile_img.setImageResource(R.drawable.college_girl);
        }

        if(entryOutModel.getAccepted().equals("t")){
            Glide.with(this).load(R.drawable.successfuly).into(imageView);
            scrollView.setBackgroundResource(R.drawable.success_bg);
            accpted.setText("   Accepted ");
            valid.setText("Valid UpTo -: "+entryOutModel.getTo_date());
            linearLayout2.setBackgroundResource(R.drawable.success_bg);
        }
        else if (entryOutModel.getAccepted().equals("f")){
            Glide.with(this).load(R.drawable.rejected).into(imageView);
            scrollView.setBackgroundResource(R.drawable.cancle_bg);
            accpted.setText("   Rejected ");
            linearLayout2.setBackgroundResource(R.drawable.cancle_bg);


        }else{
            Glide.with(this).load(R.drawable.pending).into(imageView);
            scrollView.setBackgroundResource(R.drawable.pending_bg);
            accpted.setText("   Under Review ");
            linearLayout2.setBackgroundResource(R.drawable.pending_bg);


        }
    }

    private void initView() {
        from_data=findViewById(R.id.from_date);
        to_date=findViewById(R.id.to_date);
        name=findViewById(R.id.name);
        room=findViewById(R.id.room);
        branch=findViewById(R.id.branch);
        year=findViewById(R.id.year);
        accpted=findViewById(R.id.accepted);
        valid=findViewById(R.id.valid_text);
        imageView=findViewById(R.id.review_img);
        scrollView=findViewById(R.id.scrollview);
        linearLayout2=findViewById(R.id.linearLayout2);
        profile_img=findViewById(R.id.profile_img);
        delete_pass=findViewById(R.id.delete_pass);
    }
}