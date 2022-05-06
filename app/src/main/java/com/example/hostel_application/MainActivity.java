package com.example.hostel_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.hostel_application.Fragment.HomeFragment;
import com.example.hostel_application.Fragment.NotificationFragment;
import com.example.hostel_application.Fragment.SettingFragment;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    Fragment fragment=null;
    int fragmentId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation=findViewById(R.id.meowBottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.notifications_ic));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.home_ic));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.settings_ic));
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case 2:
                        fragment=new HomeFragment();
                        break;
                    case 1:
                        fragment=new NotificationFragment();
                        break;
                    case 3:
                        fragment=new SettingFragment();
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

    }}