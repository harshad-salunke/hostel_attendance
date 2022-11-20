package com.student.hostel_application.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.student.hostel_application.R;
import com.student.hostel_application.models.EntryOutModel;
import com.student.hostel_application.models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.Calendar;

public class EntryOutFormActivity extends AppCompatActivity {
    Button send_btn;
    EditText from_date,to_date,entryout_reson,name;
    int cal_year,cal_days,cal_month;
    Calendar calendar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Student student;
    TextInputLayout to_layout,from_layout,reason_layout;
    int val_from_date,val_from_month,val_to_date,val_to_month;
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_out_form);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Admin");

        Gson gson=new Gson();
        sharedPreferences=getSharedPreferences("user_info",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        String student_info_str=sharedPreferences.getString("user","");


        student=gson.fromJson(student_info_str,Student.class);
        if(student.getUid()==null || student.getUid().equals("")){
            String uid= FirebaseAuth.getInstance().getUid();
            student.setUid(uid);
        }

        initView();

        name.setText(student.getName());
        calendar = Calendar.getInstance();
        cal_year=calendar.get(Calendar.YEAR);
        cal_days=calendar.get(Calendar.DAY_OF_MONTH);
        cal_month=calendar.get(Calendar.MONTH);

        val_from_date=calendar.get(Calendar.DATE);
        val_from_month=cal_month;
        val_to_date=val_from_date;
        val_to_month=val_from_month;


        String date=""+val_from_date+" "+getMonthStr(val_from_month)+" "+cal_year;
        from_date.setText(date);

        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(EntryOutFormActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        String date=""+day+" "+getMonthStr(month)+" "+year;
                        from_date.setText(date);
                        if(to_date.getText().toString().equals("")){
                            to_date.setText(date);
                            val_to_date=day;
                            val_to_month=month;
                        }
                        val_from_date=day;
                        val_from_month=month;
                    }
                },cal_year,cal_month,cal_days);
                datePickerDialog.show();
                if (!from_date.getText().toString().equals("")){
                    datePickerDialog.updateDate(cal_year,val_from_month,val_from_date);
                }
            }
        });

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(EntryOutFormActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        Log.d("harsahdsalunke",year+" "+getMonthStr(month)+" "+year+ " "+day);
                        String date=""+day+" "+getMonthStr(month)+" "+year;
                        to_date.setText(date);
                        if(from_date.getText().toString().equals("")){
                            from_date.setText(date);
                            val_from_date=day;
                            val_from_month=month;
                        }
                        val_to_date=day;
                        val_to_month=month;
                    }
                },cal_year,cal_month,cal_days);
                if(!from_date.getText().toString().equals("")){

                    datePickerDialog.show();
                    if (!to_date.getText().toString().equals(from_date.getText().toString())){
                        datePickerDialog.updateDate(cal_year,val_to_month,val_to_date);

                    }
                    else{
                        datePickerDialog.updateDate(cal_year,val_from_month,val_from_date);

                    }



                }

            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int current_date=cal.get(Calendar.DATE);
                int current_month=cal.get(Calendar.MONTH);


                String to_date_str=to_date.getText().toString().trim();
                String from_date_str=from_date.getText().toString().trim();
                String reason_str=entryout_reson.getText().toString();
                Log.d("harshad",current_date+"c date");
                Log.d("harshad",current_month+"c date");
                Log.d("harshad",val_from_date+"from date");
                Log.d("harshad",val_from_month+"from month");
                Log.d("harshad",val_to_date+" to  date");
                Log.d("harshad",val_to_month+" to month");
                Log.d("harshad","val from="+val_from_date +" "+val_from_month+" "+" val to"+val_to_date +" "+val_to_month);
                if (to_date_str.equals("") || to_date_str==null){
                    to_layout.setError("Date Required...");
                    return;
                }

                if (reason_str.equals("")){
                    reason_layout.setError("Required...");
                    return;
                }

                if (val_from_date<current_date && val_from_month<=current_month){
                    from_layout.setError("Please Selecte Valid Date");
                    to_layout.setError("Please Selecte Valid Date");
                    return;
                }
                else if(val_from_month>val_to_month){
                    from_layout.setError("Please Selecte Valid Date");
                    to_layout.setError("Please Selecte Valid Date");
                    return;
                }
                else   if(val_from_date>val_to_date && val_from_month==val_to_month){
                    from_layout.setError("Please Selecte Valid Date");
                    to_layout.setError("Please Selecte Valid Date");

                    return;
                }
                if(val_from_date>val_to_date && val_from_month >val_to_month){
                    from_layout.setError("Please Selecte Valid Date");
                    to_layout.setError("Please Selecte Valid Date");

                    return;}
                EntryOutModel entryOutModel=new EntryOutModel("",student.getName(),student.getUid(),student.getMsg_token(),"p",to_date_str,from_date_str,reason_str,"");

            setDataTofirebase(entryOutModel);
            }
        });
    }

    private void setDataTofirebase(EntryOutModel entryOutModel) {
        ProgressDialog dialog=ProgressDialog.show(EntryOutFormActivity.this,"Entry Out Form","Sending Please Wait.....");
        String key=databaseReference.push().getKey();
        entryOutModel.setId(key);
        databaseReference.child("entryout").child(key).setValue(entryOutModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
//                FcmNotificationsSender notificationsSender=new FcmNotificationsSender("/topics/admin","Entry Out Request",entryOutModel.getName(),getApplicationContext(),EntryOutFormActivity.this);
//                notificationsSender.SendNotifications();
                Toast.makeText(EntryOutFormActivity.this, "Succesfully Sended", Toast.LENGTH_SHORT).show();
                dialog.cancel();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EntryOutFormActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

    }


    private void initView() {
        send_btn=findViewById(R.id.entryout_send);
        from_date=findViewById(R.id.from_date);
        to_date=findViewById(R.id.to_date);
        entryout_reson=findViewById(R.id.entryout_resons);
        to_layout=findViewById(R.id.to_layout);
        from_layout=findViewById(R.id.from_layout);
        reason_layout=findViewById(R.id.reason_layout);
        name=findViewById(R.id.name);
    }

    private String getMonthStr(int month) {
        month=month+1;
        String month_str="";
        switch (month){
            case 1:
                month_str="January";
                break;
            case 2:
                month_str="February";
                break;
            case 3:
                month_str="March";
                break;
            case 4:
                month_str="April";
                break;
            case 5:
                month_str="May";
                break;
            case 6:
                month_str="June";
                break;
            case 7:
                month_str="July";
                break;
            case 8:
                month_str="August";
                break;
            case 9:
                month_str="September";
                break;
            case 10:
                month_str="October";
                break;
            case 11:
                month_str="November";
                break;
            case 12:
                month_str="December";
                break;

        }
        return  month_str;
    }

}