package com.student.hostel_application.Login;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import com.bumptech.glide.Glide;
import com.student.hostel_application.MainActivity;
import com.student.hostel_application.R;
import com.student.hostel_application.models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Login_info extends AppCompatActivity {




    CountryCodePicker countryCodePicker;
    String selected_code="91";

    //    personal details
    TextInputLayout first_name_layout,mobile_textLayout;
    TextInputLayout parent_mob_layout,parent_name_layout,address_layout,gfm_name_layout,gfm_mobile_layout;
    EditText mobile_number,name,parent_mobile,parent_name,address,gfm_name,gfm_mobile;

//    additional details
    TextInputLayout floor_layout,clg_layout,gender_layout,clg_year_layout,clg_branch_layout,room_layout;
    EditText room_no;
    Button continue_btn,back_button;
    AutoCompleteTextView collage_Name;
    AutoCompleteTextView gender;
    AutoCompleteTextView floorno;
    AutoCompleteTextView college_year,college_branch;
    int page_number=0;
    boolean check=false;
    ImageView mylogo;
    Dialog netConectivityDialog;
TextView page_id;
    String User_msg_token;
ScrollView scrollView;
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
TextView personal_d,addition_d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_info);

        if(!CheckInternetConnectiity()){
            ShowNetWorkDilogBox();
        }
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Students").child("All_student");
        scrollView=findViewById(R.id.scrollView);
        mylogo=findViewById(R.id.imageView5);
        Glide.with(this).load(R.drawable.my_logo).into(mylogo);

        getLocationPermission();
        getDiviceToken();
        initView();




        mobile_textLayout.setPrefixText("+"+selected_code);
        first_name_layout=findViewById(R.id.first_namelayout);
        room_layout=findViewById(R.id.room_no_layout);
        name=first_name_layout.getEditText();
        room_no=room_layout.getEditText();
        mobile_number=mobile_textLayout.getEditText();
        continue_btn=findViewById(R.id.get_otp);
        back_button=findViewById(R.id.back_info_btn);
        floor_layout=findViewById(R.id.floor_layout);
        gender_layout=findViewById(R.id.gender_layout);
        clg_layout=findViewById(R.id.clg_layout);
        personal_d=findViewById(R.id.personal_details);
        addition_d=findViewById(R.id.additaion_details);
        collage_Name=findViewById(R.id.collage_name);
        String arr[]= getResources().getStringArray(R.array.clgname);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, arr);
        collage_Name.setThreshold(9);
        collage_Name.setAdapter(arrayAdapter);
        page_id=findViewById(R.id.page_id);
        floorno=findViewById(R.id.floor_no);
        String arr2[]= getResources().getStringArray(R.array.floorno);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, arr2);
        floorno.setThreshold(9);
        floorno.setAdapter(arrayAdapter2);

        gender=findViewById(R.id.gender);
        String arr3[]= getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, arr3);

        gender.setThreshold(9);
        gender.setAdapter(arrayAdapter3);


        college_year=findViewById(R.id.collage_year);
        clg_year_layout=findViewById(R.id.clg_year_layout);
        String arr4[]= getResources().getStringArray(R.array.clg_year);
        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, arr4);
        college_year.setThreshold(9);
        college_year.setAdapter(arrayAdapter4);




        college_branch=findViewById(R.id.collage_branch);
        clg_branch_layout=findViewById(R.id.clg_branch_layout);
        String arr5[]= getResources().getStringArray(R.array.clg_blanch);
        ArrayAdapter<String> arrayAdapter5 = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, arr5);
        college_branch.setThreshold(9);
        college_branch.setAdapter(arrayAdapter5);


mobile_number.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        parent_mobile.setText("");
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        parent_mobile.setText("");

    }

    @Override
    public void afterTextChanged(Editable editable) {
        parent_mobile.setText("");

    }
});

        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                selected_code=selectedCountry.getPhoneCode();
                mobile_textLayout.setPrefixText("+"+selected_code);
            }
        });

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page_number=page_number+1;

                    switch (page_number){
                        case 1:
                            hide_mobile();
                            if(check){
                                show_parent();
                                back_button.setVisibility(View.VISIBLE);
                            }else{
                                page_number=page_number-1;
                            }
                            break;
                        case 2:
                            hide_parent();
                            if(check){
                                show_addition();
                            }else{
                                page_number=page_number-1;
                            }
                            break;
                        case 3:
                            additional_checkUp();
                            if(check){
                                page_number=page_number-1;
                                sendDataToOTP();
                            }else{
                                page_number=page_number-1;
                            }
                    }

                check=false;
page_id.setText("Page "+(page_number+1));
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_number=page_number-1;
                if(page_number<0){
                    page_number=0;
                }


                switch (page_number){
                    case 0:
                        show_mobile();
                        personal_d.setVisibility(View.GONE);
                        parent_mob_layout.setVisibility(View.GONE);
                        parent_name_layout.setVisibility(View.GONE);
                        address_layout.setVisibility(View.GONE);

                        break;
                    case 1:
                        show_parent();
                        hide_addition();
                        break;
                }
                page_id.setText("Page "+(page_number+1));

            }
        });

    }

    public void hide_mobile(){

        String mobile_no=mobile_number.getText().toString();
        String mname=name.getText().toString();

        if(mobile_no.length()<10){
            mobile_textLayout.setError("Enter 10 Digit No");
            Toast.makeText(Login_info.this, "Enter 10 Digit No", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mobile_no.contains("+")){
            mobile_textLayout.setError("Enter Valid No");
            Toast.makeText(Login_info.this, "Envalid Mobile No", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mname.equals("") || !mname.contains(" ")){
            first_name_layout.setError("Enter full Name");
            Toast.makeText(Login_info.this, "Enter Full Name", Toast.LENGTH_SHORT).show();
            return;
        }

personal_d.setVisibility(View.GONE);
mobile_textLayout.setVisibility(View.GONE);
first_name_layout.setVisibility(View.GONE);
countryCodePicker.setVisibility(View.GONE);
check=true;
    }
    public  void show_mobile(){
        personal_d.setVisibility(View.VISIBLE);
        mobile_textLayout.setVisibility(View.VISIBLE);
        first_name_layout.setVisibility(View.VISIBLE);
        countryCodePicker.setVisibility(View.VISIBLE);

    }

    public  void hide_parent(){
        String mobile_no=mobile_number.getText().toString();
        String parent_n_str=parent_name.getText().toString();
        String parent_m_str=parent_mobile.getText().toString();
        String address_str=address.getText().toString();
        if(parent_n_str.equals("")){
            parent_name_layout.setError("It's Required !!");
            Toast.makeText(Login_info.this, "Parent name is Required", Toast.LENGTH_SHORT).show();

            return;
        }

        if(parent_m_str.length()<10){
            parent_mob_layout.setError("Ender 10 Digit No ");
            Toast.makeText(Login_info.this, "Enter 10 Digit No", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mobile_no.equals(parent_m_str)){
            parent_mob_layout.setError("It's Not Your Parent No !!!");
            parent_mobile.setText("");
            scrollView.setFocusableInTouchMode(true);
            scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            Toast.makeText(Login_info.this, "Invalid Parent number", Toast.LENGTH_SHORT).show();

            return;
        }

        if(address_str.equals("")){
            address_layout.setError("It is Required !!!");
            Toast.makeText(Login_info.this, "Enter Address", Toast.LENGTH_SHORT).show();

            return;
        }
        personal_d.setVisibility(View.GONE);
        parent_mob_layout.setVisibility(View.GONE);
parent_name_layout.setVisibility(View.GONE);
address_layout.setVisibility(View.GONE);
check=true;
    }


    public  void show_parent(){
        personal_d.setVisibility(View.VISIBLE);
        parent_mob_layout.setVisibility(View.VISIBLE);
        parent_name_layout.setVisibility(View.VISIBLE);
        address_layout.setVisibility(View.VISIBLE);
    }


    public void show_addition(){
        addition_d.setVisibility(View.VISIBLE);
        gfm_mobile_layout.setVisibility(View.VISIBLE);
        gfm_name_layout.setVisibility(View.VISIBLE);
        room_layout.setVisibility(View.VISIBLE);
        floor_layout.setVisibility(View.VISIBLE);
        clg_layout.setVisibility(View.VISIBLE);
        clg_year_layout.setVisibility(View.VISIBLE);
        clg_branch_layout.setVisibility(View.VISIBLE);
        gender_layout.setVisibility(View.VISIBLE);
    }
    public void hide_addition(){
        addition_d.setVisibility(View.GONE);
        gfm_mobile_layout.setVisibility(View.GONE);
        gfm_name_layout.setVisibility(View.GONE);
        room_layout.setVisibility(View.GONE);
        floor_layout.setVisibility(View.GONE);
        clg_layout.setVisibility(View.GONE);
        clg_year_layout.setVisibility(View.GONE);
        clg_branch_layout.setVisibility(View.GONE);
        gender_layout.setVisibility(View.GONE);
    }
    public  void additional_checkUp(){
        String room=room_no.getText().toString();
        String gender_str=gender.getText().toString();
        String clg_str=collage_Name.getText().toString();
        String floor_str=floorno.getText().toString();
        String year_str=college_year.getText().toString();
        String branch_str_=college_branch.getText().toString();
//        String gfm_name_str=gfm_name.getText().toString();
//        String gfm_mobile_str=gfm_mobile.getText().toString();



//        if(gfm_name_str.equals("")){
//            gfm_name_layout.setError("It's Required !!");
//            Toast.makeText(Login_info.this, "Enter GFM Name", Toast.LENGTH_SHORT).show();
//
//            return;
//        }
//
//        if(gfm_mobile_str.length()<10){
//            gfm_mobile_layout.setError("Ender 10 Digit No ");
//            Toast.makeText(Login_info.this, "Enter 10 Digit No", Toast.LENGTH_SHORT).show();
//
//            return;
//        }

        if (room.equals("") || room.length()<3){
            room_layout.setError("Enter Valid Room No");
            Toast.makeText(Login_info.this, "Enter room no", Toast.LENGTH_SHORT).show();

            return;
        }

        if(floor_str.equals("")){
            floor_layout.setError("Required");
            Toast.makeText(Login_info.this, "Enter floor no", Toast.LENGTH_SHORT).show();

            return;
        }

        if(clg_str.equals("")){
            clg_layout.setError("Required");
            Toast.makeText(Login_info.this, "Enter clg name", Toast.LENGTH_SHORT).show();

            return;
        }
        if(gender_str.equals("")){
            gender_layout.setError("Required");
            Toast.makeText(Login_info.this, "Enter gender", Toast.LENGTH_SHORT).show();

            return;
        }

        if(year_str.equals("")){
            clg_year_layout.setError("Required");
            Toast.makeText(Login_info.this, "Enter year", Toast.LENGTH_SHORT).show();

            return;
        }
        if(branch_str_.equals("")){
            clg_branch_layout.setError("Required");
            Toast.makeText(Login_info.this, "Enter branch name", Toast.LENGTH_SHORT).show();

            return;
        }
        check=true;
    }

    private void sendDataToOTP() {
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
            Toast.makeText(Login_info.this, "Enter 10 Digit No", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mobile_no.contains("+")){
            mobile_textLayout.setError("Enter Valid No");
            Toast.makeText(Login_info.this, "Envalid Mobile No", Toast.LENGTH_SHORT).show();

            return;
        }
        if (mname.equals("") || !mname.contains(" ")){
            first_name_layout.setError("Enter full Name");
            Toast.makeText(Login_info.this, "Enter Full Name", Toast.LENGTH_SHORT).show();

            return;
        }

        if(parent_n_str.equals("")){
            parent_name_layout.setError("It's Required !!");
            Toast.makeText(Login_info.this, "Parent name is Required", Toast.LENGTH_SHORT).show();

            return;
        }

        if(parent_m_str.length()<10){
            parent_mob_layout.setError("Ender 10 Digit No ");
            Toast.makeText(Login_info.this, "Enter 10 Digit No", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mobile_no.equals(parent_m_str)){
            parent_mob_layout.setError("It's Not Your Parent No !!!");
            parent_mobile.setText("");
            scrollView.setFocusableInTouchMode(true);
            scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            Toast.makeText(Login_info.this, "Invalid Parent number", Toast.LENGTH_SHORT).show();

            return;
        }

        if(address_str.equals("")){
            address_layout.setError("It is Required !!!");
            Toast.makeText(Login_info.this, "Enter Address", Toast.LENGTH_SHORT).show();

            return;
        }

//        if(gfm_name_str.equals("")){
//            gfm_name_layout.setError("It's Required !!");
//            Toast.makeText(Login_info.this, "Enter GFM Name", Toast.LENGTH_SHORT).show();
//
//            return;
//        }
//
//        if(gfm_mobile_str.length()<10){
//            gfm_mobile_layout.setError("Ender 10 Digit No ");
//            Toast.makeText(Login_info.this, "Enter 10 Digit No", Toast.LENGTH_SHORT).show();
//
//            return;
//        }

        if (room.equals("") || room.length()<3){
            room_layout.setError("Enter Valid Room No");
            Toast.makeText(Login_info.this, "Enter room no", Toast.LENGTH_SHORT).show();

            return;
        }

        if(floor_str.equals("")){
            floor_layout.setError("Required");
            Toast.makeText(Login_info.this, "Enter floor no", Toast.LENGTH_SHORT).show();

            return;
        }

        if(clg_str.equals("")){
            clg_layout.setError("Required");
            Toast.makeText(Login_info.this, "Enter clg name", Toast.LENGTH_SHORT).show();

            return;
        }
        if(gender_str.equals("")){
            gender_layout.setError("Required");
            Toast.makeText(Login_info.this, "Enter gender", Toast.LENGTH_SHORT).show();

            return;
        }

        if(year_str.equals("")){
            clg_year_layout.setError("Required");
            Toast.makeText(Login_info.this, "Enter year", Toast.LENGTH_SHORT).show();

            return;
        }
        if(branch_str_.equals("")){
            clg_branch_layout.setError("Required");
            Toast.makeText(Login_info.this, "Enter branch name", Toast.LENGTH_SHORT).show();

            return;
        }
        Log.d("harshad",parent_m_str);
        Log.d("harshad",parent_n_str);
        Log.d("harshad",address_str);
        Log.d("harshad",User_msg_token);
        mobile_no="+"+selected_code+mobile_no;
        String date = getCurrentDate() + " " + getCurrentMonth();
        String clg_name_arr[]=clg_str.split("\\.");
        if (clg_name_arr[0]!=null){
            clg_str=clg_name_arr[0];
        }
        Log.d("harshad", clg_str);
        Student student=new Student(mobile_no,mname,floor_str,room,gender_str,"",clg_str,false,year_str,branch_str_,date,parent_n_str,parent_m_str,address_str,User_msg_token,gfm_name_str,gfm_mobile_str);
        Gson gson=new Gson();
        String filter_number=mobile_no.substring(0,3)+" "+mobile_no.substring(3);
        AlertDialog.Builder alert_builder = new AlertDialog.Builder(Login_info.this);
        alert_builder.setTitle(filter_number)
                .setMessage(" Is this the Correct Number ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String json=gson.toJson(student);
                        Intent intent=new Intent(Login_info.this, LoginActivity.class);
                        intent.putExtra("myJson",json);
                        startActivity(intent);
                    }
                }).setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        show_mobile();
                        hide_addition();
                        page_number=0;
                    }
                }).show();


        //                String key=databaseReference.push().getKey();
//                student.setUid(key);
//                databaseReference.child(key).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
////                Intent intent=new Intent(Login_info.this, Login_info.class);
////                startActivity(intent);
////                        finish();
//                        Toast.makeText(Login_info.this, "done", Toast.LENGTH_SHORT).show();
//                    }
//                });

    }

    private void initView() {

        countryCodePicker=findViewById(R.id.county_code_picker);
        mobile_textLayout=findViewById(R.id.mobileText_layout);

        parent_name_layout=findViewById(R.id.parent_namelayout);
        parent_name=findViewById(R.id.parent_name);

        parent_mob_layout=findViewById(R.id.mobile_parent_layout);
        parent_mobile=findViewById(R.id.parent_mobile);

        address_layout=findViewById(R.id.address_namelayout);
        address=findViewById(R.id.address);

        gfm_mobile_layout=findViewById(R.id.gfm_mobile_layout);
        gfm_name_layout=findViewById(R.id.gfm_namelayout);

        gfm_name=findViewById(R.id.gfm_name);
        gfm_mobile=findViewById(R.id.gfm_mobile);


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
                            AlertDialog.Builder alert_builder = new AlertDialog.Builder(Login_info.this);
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
                            AlertDialog.Builder alert_builder = new AlertDialog.Builder(Login_info.this);
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
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            Intent intent=new Intent(Login_info.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        super.onStart();
    }

    public void ShowNetWorkDilogBox() {
        if (netConectivityDialog == null) {
            netConectivityDialog = new Dialog(this);
            netConectivityDialog.setContentView(R.layout.internet_alert_dailog);
            netConectivityDialog.setCanceledOnTouchOutside(false);
            netConectivityDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            netConectivityDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            netConectivityDialog.getWindow().getAttributes().windowAnimations= android.R.style.Animation_Dialog;
            Button tryAgain_btn = netConectivityDialog.findViewById(R.id.block_ok);
            tryAgain_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CheckInternetConnectiity()) {
                        ShowNetWorkDilogBox();
                        return;
                    }else {
                        netConectivityDialog.dismiss();

                    }

                }
            });
        }
        if (!CheckInternetConnectiity()) {
            netConectivityDialog.show();
        } else {
            netConectivityDialog.dismiss();
        }

    }

    public Boolean CheckInternetConnectiity() {
        Context context = this;
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            boolean isConnected = networkInfo != null &&
                    networkInfo.isConnectedOrConnecting();
            return isConnected;
        }
        return false;

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


}