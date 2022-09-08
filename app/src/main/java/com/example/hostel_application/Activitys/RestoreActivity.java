package com.example.hostel_application.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hostel_application.Login.Login_info;
import com.example.hostel_application.MainActivity;
import com.example.hostel_application.R;
import com.example.hostel_application.models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class RestoreActivity extends AppCompatActivity {
    EditText mobile_number;
    EditText name;
    EditText room_no;
    TextInputLayout first_name_layout;
    TextInputLayout last_name_layout;
    TextInputLayout floor_layout,clg_layout,gender_layout;

    TextInputLayout mobile_textLayout;
    String selected_code="91";
    Button continue_btn;
    AutoCompleteTextView collage_Name;
    AutoCompleteTextView gender;
    AutoCompleteTextView floorno;
    AutoCompleteTextView college_year,college_branch;
    TextInputLayout clg_year_layout,clg_branch_layout,gfm_mobile_layout,gfm_name_layout;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;
    String uid;


    CountryCodePicker countryCodePicker;

    //    personal details
    TextInputLayout parent_mob_layout,parent_name_layout,address_layout;
    EditText parent_mobile,parent_name,address,gfm_mobile,gfm_name;

    //    additional details
    TextInputLayout room_layout;


    ImageView mylogo;
    Dialog netConectivityDialog;

    String User_msg_token;
    Student student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore);
        getLocationPermission();
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Students").child("All_student");
        mobile_textLayout=findViewById(R.id.mobileText_layout);
        sharedPreferences=getSharedPreferences("delete",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        sharedPreferences2=getSharedPreferences("user_info",MODE_PRIVATE);

        Gson gson=new Gson();
        String student_str=sharedPreferences2.getString("user","");
        student=gson.fromJson(student_str,Student.class);
        String uid=FirebaseAuth.getInstance().getUid();
        student.setUid(uid);
        Toast.makeText(this, ""+student.getUid(), Toast.LENGTH_SHORT).show();
        editor2=sharedPreferences2.edit();

        getDiviceToken();
        mobile_textLayout.setPrefixText("+"+selected_code);
        gfm_mobile_layout=findViewById(R.id.gfm_mobile_layout);
        gfm_name_layout=findViewById(R.id.gfm_namelayout);
        parent_name=findViewById(R.id.parent_name);
        parent_mobile=findViewById(R.id.parent_mobile);
        address=findViewById(R.id.address);

        gfm_name=findViewById(R.id.gfm_name);
        gfm_mobile=findViewById(R.id.gfm_mobile);
        first_name_layout=findViewById(R.id.first_namelayout);
        last_name_layout=findViewById(R.id.room_no_layout);
        name=first_name_layout.getEditText();
        room_no=last_name_layout.getEditText();

        mobile_number=mobile_textLayout.getEditText();
        String mobile_str=sharedPreferences.getString("mobile","0");
        mobile_str=mobile_str.replace("+91","");
        mobile_number.setText(mobile_str);
        mobile_number.setEnabled(false);


        continue_btn=findViewById(R.id.get_otp);

        floor_layout=findViewById(R.id.floor_layout);
        gender_layout=findViewById(R.id.gender_layout);
        clg_layout=findViewById(R.id.clg_layout);

        collage_Name=findViewById(R.id.collage_name);
        String arr[]= getResources().getStringArray(R.array.clgname);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, arr);
        collage_Name.setThreshold(1);
        collage_Name.setAdapter(arrayAdapter);

        floorno=findViewById(R.id.floor_no);
        String arr2[]= getResources().getStringArray(R.array.floorno);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, arr2);
        floorno.setThreshold(1);
        floorno.setAdapter(arrayAdapter2);

        gender=findViewById(R.id.gender);
        String arr3[]= getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, arr3);
        gender.setThreshold(1);
        gender.setAdapter(arrayAdapter3);


        college_year=findViewById(R.id.collage_year);
        clg_year_layout=findViewById(R.id.clg_year_layout);
        String arr4[]= getResources().getStringArray(R.array.clg_year);
        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, arr4);
        college_year.setThreshold(1);
        college_year.setAdapter(arrayAdapter4);




        college_branch=findViewById(R.id.collage_branch);
        clg_branch_layout=findViewById(R.id.clg_branch_layout);
        String arr5[]= getResources().getStringArray(R.array.clg_blanch);
        ArrayAdapter<String> arrayAdapter5 = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, arr5);
        college_branch.setThreshold(1);
        college_branch.setAdapter(arrayAdapter5);


        setDataToView();

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile_no=mobile_number.getText().toString();
                String mname=name.getText().toString();
                String room=room_no.getText().toString();
                String gender_str=gender.getText().toString();
                String clg_str=collage_Name.getText().toString();
                String floor_str=floorno.getText().toString();
                String year_str=college_year.getText().toString();
                String branch_str_=college_branch.getText().toString();

                String parent_n_str=parent_name.getText().toString();
                String parent_m_str=parent_mobile.getText().toString();
                String address_str=address.getText().toString();
                String gfm_name_str=gfm_name.getText().toString();
                String gfm_mobile_str=gfm_mobile.getText().toString();

                if(mobile_no.length()<10){
                    mobile_textLayout.setError("Enter 10 Digit No");
                    return;
                }
                if(mobile_no.contains("+")){
                    mobile_textLayout.setError("Enter Valid No");
                    return;
                }
                if (mname.equals("") || !mname.contains(" ")){
                    first_name_layout.setError("Enter full Name");
                    return;
                }

                if(parent_n_str.equals("")){
                    parent_name.setError("It's Required !!");
                    return;
                }

                if(parent_m_str.length()<10){
                    mobile_number.setError("Ender 10 Digit No ");
                    return;
                }

                if(mobile_no.equals(parent_m_str)){
                    mobile_number.setError("It's Not Your Parent No !!!");
                    return;
                }

                if(address_str.equals("")){
                    address.setError("It is Required !!!");
                    return;
                }

                if(gfm_name_str.equals("")){
                    gfm_name.setError("It's Required !!");
                    return;
                }

                if(gfm_mobile_str.length()<10){
                    gfm_mobile.setError("Ender 10 Digit No ");
                    return;
                }

                if (room.equals("") || room.length()<3){
                    room_layout.setError("Enter Valid Room No");
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
                Log.d("harshad",parent_m_str);
                Log.d("harshad",parent_n_str);
                Log.d("harshad",address_str);
                Log.d("harshad",User_msg_token);
                mobile_no="+"+selected_code+mobile_no;
                String date = getCurrentDate() + " " + getCurrentMonth();
                Student edit_student=new Student(mobile_no,mname,floor_str,room,gender_str,"",clg_str,false,year_str,branch_str_,date,parent_n_str,parent_m_str,address_str,User_msg_token,gfm_name_str,gfm_mobile_str);
                Gson gson1=new Gson();
                String student_str=gson1.toJson(edit_student);
                ProgressDialog dialog = ProgressDialog.show(RestoreActivity.this, "",
                        "Restoring. Please wait...", true);



                if(student.getUid()!=null && !student.getUid().equals("")){

                    Toast.makeText(RestoreActivity.this, "uid is"+student.getUid(), Toast.LENGTH_SHORT).show();
                    databaseReference.child(student.getUid()).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            editor.putString("delete","no");
                            editor2.putString("user",student_str);
                            editor.commit();
                            editor2.commit();
                            Toast.makeText(RestoreActivity.this, "Restore Successfully ", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(RestoreActivity.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RestoreActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                }else{
                    Toast.makeText(RestoreActivity.this, "Sorry Some thing is wrong ", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    private void setDataToView() {
        name.setText(student.getName());
        parent_name.setText(student.getParent_name());
        parent_mobile.setText(student.getParent_mobile());
        gfm_name.setText(student.getGfm_name());
        gfm_mobile.setText(student.getGfm_mobile());
        room_no.setText(student.getRoom_No());
        floorno.setText(student.getFloor_no());
        collage_Name.setText(student.getClg_name());
        college_branch.setText(student.getClg_branch());
        college_year.setText(student.getClg_year());
        address.setText(student.getAdress());
        gender.setText(student.getGender());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private  String getDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        return  dtf.format(now);
    }
    private void getLocationPermission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        if (permissionDeniedResponse.isPermanentlyDenied()) {
                            AlertDialog.Builder alert_builder = new AlertDialog.Builder(RestoreActivity.this);
                            alert_builder.setTitle("Permission Denied")
                                    .setMessage("Permission is permanently denied. To use This app you need go to setting to allow the permission.")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                            startActivity(intent);
                                        }
                                    }).show();

                        } else {
                            AlertDialog.Builder alert_builder = new AlertDialog.Builder(RestoreActivity.this);
                            alert_builder.setTitle("Permission Required !")
                                    .setMessage("Please to Allow a permission to use this app. It is Required !!!")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getLocationPermission();
                                        }
                                    }).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private int getCurrentDate() {
        Date c = Calendar.getInstance().getTime();

        return (int) c.getDate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCurrentMonth() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return localDate.format(formatter);
    }

    private void getDiviceToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("harshad", "Fetching FCM registration token failed", task.getException());
                                    return;
                                }

                                // Get new FCM registration token
                                User_msg_token = task.getResult();

                                Log.d("harshad", "Fetching FCM registration token done "+User_msg_token);

                                // Log and toast
                            }
                        });
            }
        });

    }

    @Override
    protected void onStart() {

        super.onStart();
    }

}
