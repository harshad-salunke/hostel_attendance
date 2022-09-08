package com.example.hostel_application.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostel_application.R;
import com.example.hostel_application.models.Student;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class EditProfileActivity extends AppCompatActivity {

    EditText mobile_number;
    EditText name;
    EditText room_no;
    TextInputLayout first_name_layout;
    TextInputLayout last_name_layout;
    TextInputLayout floor_layout,clg_layout,gender_layout;

    Button continue_btn;
    AutoCompleteTextView collage_Name;
    AutoCompleteTextView gender;
    AutoCompleteTextView floorno;
    AutoCompleteTextView college_year,college_branch;
    TextInputLayout clg_year_layout,clg_branch_layout;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
TextView profile_name;
ImageView profile_img;
    Student student;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        sharedPreferences=getSharedPreferences("user_info",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        Intent intent=getIntent();
        String json_obj=intent.getStringExtra("myJson");
        Gson gson = new Gson();
        student = gson.fromJson(json_obj, Student.class);

        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Students").child("All_student");

        profile_img=findViewById(R.id.profile_img);
        profile_name=findViewById(R.id.profile_name);
        setValueInView(student);



        first_name_layout=findViewById(R.id.first_namelayout);
        last_name_layout=findViewById(R.id.room_no_layout);
        name=first_name_layout.getEditText();
                name.setText(student.getName());
        room_no=last_name_layout.getEditText();
                room_no.setText(student.getRoom_No());
        continue_btn=findViewById(R.id.get_otp);

        floor_layout=findViewById(R.id.floor_layout);
        gender_layout=findViewById(R.id.gender_layout);
        clg_layout=findViewById(R.id.clg_layout);

        collage_Name=findViewById(R.id.collage_name);
        String arr[]= getResources().getStringArray(R.array.clgname);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, arr);
        collage_Name.setThreshold(9);
        collage_Name.setAdapter(arrayAdapter);
                collage_Name.setText(student.getClg_name());


        floorno=findViewById(R.id.floor_no);
        String arr2[]= getResources().getStringArray(R.array.floorno);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, arr2);
        floorno.setThreshold(9);
        floorno.setAdapter(arrayAdapter2);
                floorno.setText(student.getFloor_no());


        gender=findViewById(R.id.gender);
        String arr3[]= getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, arr3);
        gender.setThreshold(9);
        gender.setAdapter(arrayAdapter3);
                gender.setText(student.getGender());

        college_year=findViewById(R.id.collage_year);
        clg_year_layout=findViewById(R.id.clg_year_layout);
        String arr4[]= getResources().getStringArray(R.array.clg_year);
        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, arr4);
        college_year.setThreshold(9);
        college_year.setAdapter(arrayAdapter4);
                college_year.setText(student.getClg_year());



        college_branch=findViewById(R.id.collage_branch);
        clg_branch_layout=findViewById(R.id.clg_branch_layout);
        String arr5[]= getResources().getStringArray(R.array.clg_blanch);
        ArrayAdapter<String> arrayAdapter5 = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, arr5);
        college_branch.setThreshold(9);
        college_branch.setAdapter(arrayAdapter5);
        college_branch.setText(student.getClg_branch());

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mname=name.getText().toString();
                String room_str=room_no.getText().toString();
                String gender_str=gender.getText().toString();
                String clg_str=collage_Name.getText().toString();
                String floor_str=floorno.getText().toString();
                String year_str=college_year.getText().toString();
                String branch_str_=college_branch.getText().toString();

                if (mname.equals("") || !mname.contains(" ")){
                    first_name_layout.setError("Enter full Name");
                    return;
                }
                if (room_str.equals("") || room_str.length()<3){
                    last_name_layout.setError("Enter Valid Room No");
                    return;
                }
                if(floor_str.equals("")){
                    floor_layout.setError("Required");
                    return;
                }

                if(clg_str.equals("")){
                    clg_layout.setError("Required");
                    return;
                }
                if(gender_str.equals("")){
                    gender_layout.setError("Required");
                    return;
                }

                if(year_str.equals("")){
                    clg_year_layout.setError("Required");
                    return;
                }
                if(branch_str_.equals("")){
                    clg_branch_layout.setError("Required");
                    return;
                }

                ProgressDialog dialog = ProgressDialog.show(EditProfileActivity.this, "",
                        "Loading. Please wait...", true);
                student.setName(mname);
                student.setRoom_No(room_str);
                student.setFloor_no(floor_str);
                student.setClg_name(clg_str);
                student.setClg_year(year_str);
                student.setClg_branch(branch_str_);
                student.setGender(gender_str);
                setValueInView(student);

                Gson gson=new Gson();
                String student_infp=   gson.toJson(student);
                if(student.getUid()!=null){
                    databaseReference.child(student.getUid()).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            editor.putString("user",student_infp);
                            editor.commit();
                            Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            dialog.cancel();

//                        finish();
                        }
                    });

                }else{
                    Toast.makeText(EditProfileActivity.this, "Some thing is wrong", Toast.LENGTH_SHORT).show();
                }


            }
        });




        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.back_btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void setValueInView(Student student) {
        profile_name.setText(student.getName());

        if(student.getGender().equals("Male")){
            profile_img.setImageResource(R.drawable.college_boy);

        }else {
            profile_img.setImageResource(R.drawable.college_girl);
        }

    }

}