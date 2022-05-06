package com.example.hostel_application.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostel_application.Activitys.MyWerningActivity;
import com.example.hostel_application.CalendarAdapter;
import com.example.hostel_application.R;
import com.example.hostel_application.models.Student;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class HomeFragment extends Fragment implements OnMapReadyCallback {


    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    CalendarAdapter calendarAdapter;
    String year;
    String month;
    Geocoder geocoder;

    ArrayList<Integer> daysInMonth;
    ArrayList<Integer> present;
    Button preMonth, nextMOnt;
    private GoogleMap mMap;
    View Mapview;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    int ACCESSS_LOCATION_REQUEST_CODE = 1000;
    TextView longitude;
    TextView latitude;
    CardView map_btn_card,card_map;
    Button cancle_map;
    boolean location_first=true;
    TextView location_remider_text;
    int u=0;
    Circle circle=null;
    TextView inside_outside_maptxt;

    Dialog netConectivityDialog=null;
    Button make_attendance_btn;
    SharedPreferences sharedPreferences;
    String Present_date="";
    TextView attendance_day_month,attendance_persentage;
    boolean isInsideZone=false;
    AlertDialog.Builder location_alert;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Student student;
    ScrollView scrollView;
    ShimmerFrameLayout shimmerFrameLayout;
    TextView student_name,student_room,student_today_date;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity());
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Students");
        student_name=view.findViewById(R.id.student_name);
        student_room=view.findViewById(R.id.student_room);
        student_today_date=view.findViewById(R.id.today_date);
        scrollView=view.findViewById(R.id.scroll_View);
        shimmerFrameLayout=view.findViewById(R.id.shimmerlayout);
        shimmerFrameLayout.startShimmer();
        databaseReference.child("All_student").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 student=snapshot.getValue(Student.class);
                 String date=getCurrentDate()+" "+getCurrentMonth();
                 student_name.setText(student.getName());
                 student_room.setText(student.getRoom_No());
                 student_today_date.setText(date);
                 scrollView.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        InitAllViews(view);
        if(!isGPSLocationON()){
            showGpsAlertBox();
        }

        make_attendance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternetConnectiity()){
                    ShowNetWorkDilogBox();
                    return;
                }
                boolean check_autodate=true;
                try {
                   check_autodate= check_autoDateTimeOnOf();
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                if(!check_autodate){
                    Intent intent=new Intent(getContext(), MyWerningActivity.class);
                    getContext().startActivity(intent);
                    return;
                }
                if(!isGPSLocationON()){
                    showGpsAlertBox();
                    return;
                }
                if(!isInsideZone){
                    Toast.makeText(getContext(), "!! You are OutSide of Zone ", Toast.LENGTH_SHORT).show();
                    return;
                }


            String uid=firebaseUser.getUid();
                String year_str=getCurrentYear();
                String month_str=getCurrentMonth();
                int cu_date=getCurrentDate();
                databaseReference.child("attendance").child(uid).child(year_str).child(month_str).push().setValue(cu_date).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        String str=getCurrentDate()+getCurrentMonth()+getCurrentYear();
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("date", str);
                        myEdit.commit();

                        selectedDate = LocalDate.now();
                        year = getYear(selectedDate);
                        month = monthYearFromDate(selectedDate);
                        getFirebaseData(year, month);
                        setMonthView();

//                        making attendance button invisible

                        make_attendance_btn.setText("Present");
                        make_attendance_btn.setBackgroundColor(Color.GREEN);
                        make_attendance_btn.setTextColor(Color.BLACK);
                        make_attendance_btn.setClickable(false);
                        databaseReference.child("dailyattendance").child(student.getUid()).setValue(student);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

//                for auto date selected date
//                setFirebaseDatabase(year,month,getSelectedDate(selectedDate));
            }

        });
        make_attendance_btn.setBackgroundColor(Color.rgb(33, 150, 243));
        if(isAlreadyPresent()){
            make_attendance_btn.setText("Present");
            make_attendance_btn.setBackgroundColor(Color.GREEN);
            make_attendance_btn.setClickable(false);

        }
        map_btn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_map.setVisibility(View.VISIBLE);
                card_map.setTranslationX(800);

                card_map.setAlpha(u);
                card_map.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(200).start();
            }
        });

        cancle_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_map.setTranslationX(0);

                card_map.animate().translationX(1000).alpha(0).setDuration(800).setStartDelay(200).start();
                new CountDownTimer(800, 100) {

                    public void onTick(long millisUntilFinished) {
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                                        card_map.setVisibility(View.GONE);

                    }

                }.start();
            }
        });
        preMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousMonthAction(view);
            }
        });
        nextMOnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonthAction(view);
            }
        });
        getFirebaseData(year, month);
        return view;
    }

    private void showGpsAlertBox() {
        location_alert.setTitle("Enable GPS")
                .setMessage("GPS Location is Disable Please Go To Setting and Enable it !")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getContext().startActivity(callGPSSettingIntent);
                        dialog.cancel();
                    }
                }).create().show();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void InitAllViews(View view) {

        map_btn_card=view.findViewById(R.id.map_cardView);
        cancle_map=view.findViewById(R.id.cancel_map);
        card_map=view.findViewById(R.id.Map_view);
        attendance_day_month=view.findViewById(R.id.attendance_in_month);
        attendance_persentage=view.findViewById(R.id.attendance_persentage);
        longitude=view.findViewById(R.id.longitude);
        latitude=view.findViewById(R.id.latitude);
        ShowNetWorkDilogBox();
        MapInitialization();

        initWidgets(view);
        selectedDate = LocalDate.now();
        year = getYear(selectedDate);
        month = monthYearFromDate(selectedDate);
        setMonthView();
        preMonth = view.findViewById(R.id.previousMonth);
        nextMOnt = view.findViewById(R.id.nextMonth);
        location_remider_text=view.findViewById(R.id.Location_reminder);
        inside_outside_maptxt=view.findViewById(R.id.m_inside_out_text);
        make_attendance_btn=view.findViewById(R.id.make_attendance);
        location_alert = new AlertDialog.Builder(getContext());

    }

    private boolean isGPSLocationON() {
        LocationManager manager = (LocationManager)getActivity(). getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return  statusOfGPS;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isAlreadyPresent() {
        Present_date=sharedPreferences.getString("date","");
        String str=getCurrentDate()+getCurrentMonth()+getCurrentYear();
        if(Present_date.equals(str)){
            Toast.makeText(getContext(), "equals", Toast.LENGTH_SHORT).show();
            return  true;
        }
       return  false;

    }


    private boolean check_autoDateTimeOnOf() throws Settings.SettingNotFoundException {
        if(Settings.Global.getInt(getActivity().getContentResolver(), Settings.Global.AUTO_TIME) == 1)
        {
            return true;
        }
        else
        {
            return  false;
            // Disabed
        }
    }

    private int getCurrentDate()  {
        Date c = Calendar.getInstance().getTime();

        return (int) c.getDate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCurrentYear() {
        LocalDate localDate=LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return localDate.format(formatter);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private  String getCurrentMonth(){
        LocalDate localDate=LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return localDate.format(formatter);
    }


    private void MapInitialization() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.map, fragment);
        transaction.commit();
        fragment.getMapAsync(this);

        Mapview = fragment.getView();
        geocoder = new Geocoder(getContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fragment.getMapAsync(this);
    }




    public void getFirebaseData(String year, String month) {
        String uid=firebaseUser.getUid();
        databaseReference.child("attendance").child(uid).child(year).child(month).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (present.size() > 0) {
                    present.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int p = dataSnapshot.getValue(Integer.class);
                    if(!present.contains(p)){
                        present.add(p);
                    }

                }
                Log.d("harshadsalunke",present.toString());
                int days_presnt=present.size();
                int day_in_month=getDayIinMonth(selectedDate);
                String days=days_presnt+" / "+day_in_month+" Days";
               Float persentage= getPersentageOfPresenty(days_presnt,day_in_month);
                attendance_day_month.setText(days);
                attendance_persentage.setText(persentage+" %");
                calendarAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Float getPersentageOfPresenty(int days_presnt, int day_in_month) {
        int total, score;
        float percentage;

        total = day_in_month;
        score = days_presnt;

        percentage = (score * 100 / total);
        return  percentage;
    }


    private void initWidgets(View view) {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);

        monthYearText = view.findViewById(R.id.monthYearTV);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        daysInMonth = daysInMonthArray(selectedDate);
        present = new ArrayList<>();
        calendarAdapter = new CalendarAdapter(daysInMonth, getContext(), present);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }
@RequiresApi(api = Build.VERSION_CODES.O)
private  int getDayIinMonth(LocalDate date){
    YearMonth yearMonth = YearMonth.from(date);

    int daysInMonth = yearMonth.lengthOfMonth();
    return  daysInMonth;
}

    @RequiresApi(api = Build.VERSION_CODES.O)

    private ArrayList<Integer> daysInMonthArray(LocalDate date) {
        ArrayList<Integer> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add(0);
            } else {
                daysInMonthArray.add(i - dayOfWeek);
            }
        }
        return daysInMonthArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Integer getSelectedDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        String selected=date.format(formatter);
        if(selected.startsWith("0")){
            selected=selected.replace("0","");
        }
        return Integer.parseInt(selected);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getYear(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view) {
        selectedDate = selectedDate.minusMonths(1);
        year = getYear(selectedDate);
        month = monthYearFromDate(selectedDate);
        getFirebaseData(year, month);

        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        year = getYear(selectedDate);
        month = monthYearFromDate(selectedDate);
        getFirebaseData(year, month);
        setMonthView();
    }

    public void ShowNetWorkDilogBox(){
        if(netConectivityDialog==null){
            netConectivityDialog=new Dialog(getContext());
            netConectivityDialog.setContentView(R.layout.internet_alert_dailog);
            netConectivityDialog.setCanceledOnTouchOutside(false);
            netConectivityDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            netConectivityDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            netConectivityDialog.getWindow().getAttributes().windowAnimations= android.R.style.Animation_Dialog;
            Button tryAgain_btn=netConectivityDialog.findViewById(R.id.warning_btn);
            tryAgain_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    netConectivityDialog.dismiss();
                }
            });
        }
        if(!CheckInternetConnectiity()){
            netConectivityDialog.show();
        }
        else{
            netConectivityDialog.dismiss();
        }

    }
    public Boolean CheckInternetConnectiity(){
        Context context=getContext();
        if(context!=null){
            ConnectivityManager  connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo  networkInfo=connectivityManager.getActiveNetworkInfo();
            boolean isConnected = networkInfo != null &&
                    networkInfo.isConnectedOrConnecting();
            return  isConnected;
        }
       return  false;

    }
    @Override
    public void onStart() {
        boolean check_autodate=true;
        try {
            check_autodate= check_autoDateTimeOnOf();
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if(!check_autodate){
            Intent intent=new Intent(getContext(), MyWerningActivity.class);
            getContext().startActivity(intent);
            getActivity().finish();
            return;
        }

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            startedLocationUpdate();
        } else {
            requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESSS_LOCATION_REQUEST_CODE);
        }
        super.onStart();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(18.4228781, 73.9040033))
                .radius(1000)
                .strokeColor(Color.RED));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(18.4228781,73.9040033),14));
        mMap.setMyLocationEnabled(true);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d("locations are",locationResult.getLastLocation()+"");
            Location location = locationResult.getLastLocation();
            longitude.setText(""+location.getLatitude());
            latitude.setText(""+location.getLongitude());

            if (location_first){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),17));
                location_first=false;
            }
            if(circle!=null){
                float[] distance = new float[2];

                Location.distanceBetween( location.getLatitude(), location.getLongitude(),
                        circle.getCenter().latitude, circle.getCenter().longitude, distance);

                if( distance[0] > circle.getRadius()  ){
                    location_remider_text.setText("You are OutSide of Zone !.");
                    location_remider_text.setTextColor(Color.rgb(247, 20, 20));

                    inside_outside_maptxt.setText("You are Outside of zone");
                    inside_outside_maptxt.setTextColor(Color.rgb(247, 20, 20));

                    isInsideZone=false;

                } else {
                    location_remider_text.setText("Inside zone");
                    location_remider_text.setTextColor(Color.rgb(76, 175, 80));


                    inside_outside_maptxt.setText("You are Inside of zone");
                    location_remider_text.setTextColor(Color.rgb(76, 175, 80));

                    isInsideZone=true;

                }
            }



        }
    };
}