package com.example.hostel_application.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hostel_application.Login.LoginActivity;
import com.example.hostel_application.Login.Login_info;
import com.example.hostel_application.MainActivity;
import com.example.hostel_application.R;
import com.example.hostel_application.models.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.rilixtech.widget.countrycodepicker.Country;
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
    TextInputLayout clg_year_layout,clg_branch_layout;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String uid;
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

        mobile_textLayout.setPrefixText("+"+selected_code);

        first_name_layout=findViewById(R.id.first_namelayout);
        last_name_layout=findViewById(R.id.last_namelayout);
        name=first_name_layout.getEditText();
        room_no=last_name_layout.getEditText();

        mobile_number=mobile_textLayout.getEditText();
        String mobile_str=sharedPreferences.getString("mobile","0");
        uid= FirebaseAuth.getInstance().getUid();
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
                if (room.equals("") || room.length()<3){
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
                ProgressDialog dialog = ProgressDialog.show(RestoreActivity.this, "",
                        "Restoring. Please wait...", true);


                mobile_no="+"+selected_code+mobile_no;
                String date = getCurrentDate() + " " + getCurrentMonth();

                Student student=new Student(mobile_no,mname,floor_str,room,gender_str,uid,clg_str,false,year_str,branch_str_,date);


                databaseReference.child(uid).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        editor.putString("delete","no");
                        editor.commit();
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


            }
        });

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

    @Override
    protected void onStart() {

        super.onStart();
    }}