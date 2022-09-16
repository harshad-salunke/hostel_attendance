package com.student.hostel_application.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.student.hostel_application.MainActivity;
import com.student.hostel_application.R;
import com.student.hostel_application.models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    String mverificationId;
    FirebaseAuth mfirebaseaut;
    PhoneAuthProvider.ForceResendingToken mforceResendingToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    String Mobile_No;
    String Name;
    OtpView otpView;
//    firebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressBar veri_progressBar;
    Button verify_btn;
    TextView otpresend;
    boolean enter_otp=false;
    TextView mobile_text;
    Student student;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog dialog;
    String User_Uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = ProgressDialog.show(LoginActivity.this, "",
                "Verifying. Please wait...", true);

        sharedPreferences=getSharedPreferences("user_info",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        Intent intent=getIntent();
        String json_obj=intent.getStringExtra("myJson");
        Gson gson = new Gson();
        student = gson.fromJson(json_obj, Student.class);
        Mobile_No=student.getMobile_no();
        setContentView(R.layout.activity_login);
        mfirebaseaut=FirebaseAuth.getInstance();
        otpView=findViewById(R.id.otp_view);
        mobile_text=findViewById(R.id.veri_mobile);
        veri_progressBar=findViewById(R.id.veri_progressBar);
        verify_btn=findViewById(R.id.verification_btn);
        otpresend=findViewById(R.id.otp_send);
       mobile_text=findViewById(R.id.veri_mobile);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Students");

        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted( PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed( FirebaseException e) {
                Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(s, token);

                mverificationId=s;
                mforceResendingToken=token;
                Toast.makeText(LoginActivity.this, "OTP send sucssesfuly", Toast.LENGTH_SHORT).show();
                verify_btn.setEnabled(true);
                veri_progressBar.setVisibility(View.GONE);
                dialog.cancel();
                otpresend.setEnabled(true);

            }
        };



        if(Mobile_No!=null){
            User_Uid=Mobile_No;
            sendotp(Mobile_No);
            mobile_text.setText(Mobile_No);
            Toast.makeText(this, Mobile_No, Toast.LENGTH_SHORT).show();
        }


        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                enter_otp=true;
                veri_progressBar.setVisibility(View.VISIBLE);
                dialog.show();
                verify_btn.setEnabled(false);
                PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(mverificationId,otp.trim());
                singInWithAuth(phoneAuthCredential);

            }
        });


        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!enter_otp){
                    Toast.makeText(LoginActivity.this, "Please Enter 6 digit code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        otpresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Mobile_No!=null){
                    otpresend.setEnabled(false);
                    Toast.makeText(LoginActivity.this, "Sending Code Again", Toast.LENGTH_SHORT).show();
                    sendotp(Mobile_No);

                }
            }
        });

    }

    private void singInWithAuth(PhoneAuthCredential phoneAuthCredential) {

        mfirebaseaut.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            SaveDataInDB(student);

                        }
                        else{
                            enter_otp=false;
                            verify_btn.setEnabled(false);
                            veri_progressBar.setVisibility(View.GONE);
                            dialog.cancel();
                            Toast.makeText(LoginActivity.this, "Please Enter Valid Code", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void SaveDataInDB(Student s) {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
         User_Uid=firebaseAuth.getCurrentUser().getUid();
        s.setUid(User_Uid);
        Toast.makeText(this, ""+student.getUid(), Toast.LENGTH_SHORT).show();
        Gson gson=new Gson();
        editor.putString("user",gson.toJson(s));
        editor.commit();
        databaseReference.child("back_up").child(User_Uid).setValue(s);
        databaseReference.child("All_student").child(User_Uid).setValue(s).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure( Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });


    }


    @Override
    protected void onStart() {
        super.onStart();

    }
    public void sendotp(String PhoneNumber){
        verify_btn.setEnabled(false);
        veri_progressBar.setVisibility(View.VISIBLE);
        dialog.show();
        PhoneAuthOptions phoneAuthOptions=PhoneAuthOptions.newBuilder(mfirebaseaut)
                .setPhoneNumber(PhoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(LoginActivity.this)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);

    }
}