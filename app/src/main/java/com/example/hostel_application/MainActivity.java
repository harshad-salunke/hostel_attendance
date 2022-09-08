package com.example.hostel_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.hostel_application.Activitys.AcountDeleteActivity;
import com.example.hostel_application.Activitys.DeletedActivity;
import com.example.hostel_application.Fragment.AnnouncementFragment;
import com.example.hostel_application.Fragment.EntryOutFragment;
import com.example.hostel_application.Fragment.HomeFragment;
import com.example.hostel_application.Fragment.ProfileFragment;
import com.example.hostel_application.Notification.FcmNotificationsSender;
import com.example.hostel_application.models.Additional_Class;
import com.example.hostel_application.models.DeleteAcount;
import com.example.hostel_application.models.Student;
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