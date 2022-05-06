package com.example.hostel_application.Login;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.example.hostel_application.MainActivity;
import com.example.hostel_application.R;
import com.example.hostel_application.models.Student;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;


public class Login_info extends AppCompatActivity {


    EditText mobile_number;
    EditText name;
    EditText room_no;
    TextInputLayout first_name_layout;
    TextInputLayout last_name_layout;
    TextInputLayout floor_layout,clg_layout,gender_layout;

    CountryCodePicker countryCodePicker;
    TextInputLayout mobile_textLayout;
    String selected_code="91";
    Button continue_btn;
    AutoCompleteTextView collage_Name;
    AutoCompleteTextView gender;
    AutoCompleteTextView floorno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_info);

        countryCodePicker=findViewById(R.id.county_code_picker);
        mobile_textLayout=findViewById(R.id.mobileText_layout);

        mobile_textLayout.setPrefixText("+"+selected_code);
        first_name_layout=findViewById(R.id.first_namelayout);
        last_name_layout=findViewById(R.id.last_namelayout);
        name=first_name_layout.getEditText();
        room_no=last_name_layout.getEditText();
        mobile_number=mobile_textLayout.getEditText();
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
               String mobile_no=mobile_number.getText().toString();
               String mname=name.getText().toString();
               String room=room_no.getText().toString();
                String gender_str=gender.getText().toString();
                String clg_str=collage_Name.getText().toString();
                String floor_str=floorno.getText().toString();
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

                Toast.makeText(Login_info.this, "done", Toast.LENGTH_SHORT).show();
                mobile_no="+"+selected_code+mobile_no;

                Student student=new Student(mobile_no,mname,floor_str,room,gender_str,"",clg_str,false);
                Gson gson=new Gson();
                String json=gson.toJson(student);
                Intent intent=new Intent(Login_info.this, LoginActivity.class);
                intent.putExtra("myJson",json);
                startActivity(intent);

            }
        });

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
}