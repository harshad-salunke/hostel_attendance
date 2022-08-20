package com.example.hostel_application;

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
import com.example.hostel_application.models.DeleteAcount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    Fragment fragment=null;
    int fragmentId=0;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        catch (Exception e){

        }

        sharedPreferences=getSharedPreferences("delete",MODE_PRIVATE);
        checkDeletedOrNot();

        FirebaseMessaging.getInstance().subscribeToTopic("notification").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });



        bottomNavigation=findViewById(R.id.meowBottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.notifications_ic));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.home_ic));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_baseline_send_24));
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