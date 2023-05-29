package com.student.hostel_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.student.hostel_application.Activitys.AcountDeleteActivity;
import com.student.hostel_application.Activitys.DeletedActivity;
import com.student.hostel_application.Fragment.AnnouncementFragment;
import com.student.hostel_application.Fragment.EntryOutFragment;
import com.student.hostel_application.Fragment.HomeFragment;
import com.student.hostel_application.Fragment.ProfileFragment;
import com.student.hostel_application.models.Additional_Class;
import com.student.hostel_application.models.DeleteAcount;
import com.student.hostel_application.models.Student;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    Fragment fragment=null;
    int fragmentId=0;
    SharedPreferences sharedPreferences;
    SharedPreferences time_sharedPreferences;
    SharedPreferences.Editor time_editor;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public static int UPDATE_CODE=22;
    AppUpdateManager appUpdateManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        try {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }
//        catch (Exception e){
//
//        }
        inAppUpdate();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Students").child("addition_class").child("9359978498");
        sharedPreferences=getSharedPreferences("delete",MODE_PRIVATE);
        time_sharedPreferences=getSharedPreferences("attendance_time",MODE_PRIVATE);
        time_editor=time_sharedPreferences.edit();
        getAttendacne_time();
        checkDeletedOrNot();

        FirebaseMessaging.getInstance().subscribeToTopic("notification").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });



        bottomNavigation=findViewById(R.id.meowBottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.notifications_ic));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.home_ic));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_baseline_present));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.ic_baseline_account_circle_24));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case 2:
                        fragment=new HomeFragment();
                        break;
                    case 1:
                        fragment=new AnnouncementFragment();
                        break;
                    case 3:
                        fragment=new EntryOutFragment();
                        break;
                    case 4:
                        fragment=new ProfileFragment();
                        break;

                }
                loadFragment(fragment,item.getId());
            }
        });
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
            }
        });
        bottomNavigation.show(2,true);

    }

    private void inAppUpdate() {
        appUpdateManager= AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> task=appUpdateManager.getAppUpdateInfo();
        task.addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if(appUpdateInfo.updateAvailability()== UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE,MainActivity.this,UPDATE_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        appUpdateManager.registerListener(listener);
    }

    InstallStateUpdatedListener listener=installState -> {
        if(installState.installStatus()== InstallStatus.DOWNLOADED){
            popUp();
        }
    };

    private void popUp() {
        Snackbar snackbar=Snackbar.make(
                findViewById(android.R.id.content),
                "App Update Almost Done",
                Snackbar.LENGTH_INDEFINITE
        );
        snackbar.setAction("Reload", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appUpdateManager.completeUpdate();

            }
        });
        snackbar.setTextColor(Color.parseColor("#FF0000"));
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==UPDATE_CODE){
            if(resultCode!=RESULT_OK){

            }
        }
    }

    private void getAttendacne_time() {
        Gson g=new Gson();
        String time=sharedPreferences.getString("time","");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Additional_Class ad = snapshot.getValue(Additional_Class.class);
                    Gson gson = new Gson();
                    String ad_str=gson.toJson(ad);
                    time_editor.putString("time",ad_str);
                    time_editor.commit();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



    private void loadFragment(Fragment newfragment,int id) {

        if (fragmentId==0 || fragmentId!=id) {
            fragmentId=id;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();
        }

    }

    private void checkDeletedOrNot() {
        String delete=sharedPreferences.getString("delete","no");
        if(delete.equals("yes")){
            Intent intent=new Intent(MainActivity.this, DeletedActivity.class);
            startActivity(intent);
            finishAffinity();
        }
    }
}