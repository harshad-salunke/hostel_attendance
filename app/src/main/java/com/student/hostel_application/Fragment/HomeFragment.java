package com.student.hostel_application.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.student.hostel_application.Activitys.AcountDeleteActivity;
import com.student.hostel_application.Activitys.BlockActivity;
import com.student.hostel_application.Activitys.DeletedActivity;
import com.student.hostel_application.Activitys.MyWerningActivity;
import com.student.hostel_application.CalendarAdapter;
import com.student.hostel_application.Login.Login_info;
import com.student.hostel_application.R;
import com.student.hostel_application.models.Additional_Class;
import com.student.hostel_application.models.Attendance_data;
import com.student.hostel_application.models.Student;
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
import com.google.firebase.crashlytics.CustomKeysAndValues;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
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
    ArrayList<Integer> present,permission_present;
    ArrayList<Attendance_data> Main_attendance_data;
    Button preMonth, nextMOnt;
    private GoogleMap mMap;
    View Mapview;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    TextView longitude;
    TextView latitude;
    CardView map_btn_card, card_map;
    Button cancle_map;
    boolean location_first = true;
    TextView location_remider_text;
    int u = 0;
    Circle circle = null;
    Circle circle2 = null;

    TextView inside_outside_maptxt;

    Dialog netConectivityDialog = null;
    Button make_attendance_btn;
    SharedPreferences sharedPreferences;
    String Present_date = "";
    TextView attendance_day_month, attendance_persentage;
    boolean isInsideZone = false;
    AlertDialog.Builder location_alert;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Student student;
    ScrollView scrollView;
    ShimmerFrameLayout shimmerFrameLayout;
    TextView student_name, student_room, student_today_date,student_clg;
    ImageView profile_image,my_logo;

    LinearLayout progress_layout,location_layout;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Students");
        student_name = view.findViewById(R.id.student_name);
        student_room = view.findViewById(R.id.student_room);
        student_clg=view.findViewById(R.id.floor);
        student_today_date = view.findViewById(R.id.today_date);
        scrollView = view.findViewById(R.id.scroll_View);
        shimmerFrameLayout = view.findViewById(R.id.shimmerlayout);
        shimmerFrameLayout.startShimmer();

        profile_image=view.findViewById(R.id.profile_image);

        databaseReference.child("All_student").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                student = snapshot.getValue(Student.class);
                if (student.isBlocked()) {
                    Intent intent = new Intent(getActivity(), BlockActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
                String date = getCurrentDate() + " " + getCurrentMonth();
                student_name.setText(student.getName());
                student_room.setText(student.getRoom_No());
                student_today_date.setText(date);
                student_clg.setText(student.getClg_name());
                if(student.getGender().equals("Male")){

                    profile_image.setImageResource(R.drawable.college_boy);
                }else {
                    profile_image.setImageResource(R.drawable.college_girl);
                }


                scrollView.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                try {
                    setFirebaseCrayshlytics(student);

                }catch (Exception e){
                    Toast.makeText(getContext(), ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        InitAllViews(view);
        if (!isGPSLocationON()) {
            showGpsAlertBox();
        }

        make_attendance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckInternetConnectiity()) {
                    ShowNetWorkDilogBox();
                    return;
                }
                if(!isAddentanceTime()){
                    return;
                }
                boolean check_autodate = true;
                try {
                    check_autodate = check_autoDateTimeOnOf();
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                if (!check_autodate) {
                    Intent intent = new Intent(getContext(), MyWerningActivity.class);
                    getContext().startActivity(intent);
                    return;
                }
                if (!isGPSLocationON()) {
                    showGpsAlertBox();
                    return;
                }
                if (!isInsideZone) {
                    Toast.makeText(getContext(), "!! You are OutSide of Zone ", Toast.LENGTH_SHORT).show();
                    return;
                }

                make_attendance_btn.setClickable(false);
                String uid = firebaseUser.getUid();
                String year_str = getCurrentYear().trim();
                String month_str = getCurrentMonth().trim();
                int cu_date = getCurrentDate();
                String currentTime=getCurenntTime();
                String msg_id=databaseReference.push().getKey();
                Attendance_data attendance_data=new Attendance_data(cu_date,"yes",currentTime,msg_id);
                databaseReference.child("attendance").child(uid).child(year_str).child(month_str).child(msg_id).setValue(attendance_data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String str = getCurrentDate() + getCurrentMonth() + getCurrentYear();
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
                        make_attendance_btn.setBackgroundColor(getResources().getColor(R.color.green));
                        make_attendance_btn.setTextColor(Color.BLACK);
                        make_attendance_btn.setClickable(false);
                        databaseReference.child("dailyattendance").child(year_str).child(month_str).child(cu_date + "").child(student.getUid()).setValue(student.getUid());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        make_attendance_btn.setClickable(true);

                    }
                });

            }

        });

        make_attendance_btn.setBackgroundColor(Color.rgb(33, 150, 243));

        if (isAlreadyPresent()) {
            make_attendance_btn.setText("Present");
            make_attendance_btn.setBackgroundColor(getResources().getColor(R.color.green));
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

    private void setFirebaseCrayshlytics(Student student) {
        CustomKeysAndValues keysAndValues = new CustomKeysAndValues.Builder()
                .putString("name", student.getName())
                .putString("clg_name", student.getClg_name())
                .putBoolean("blocked", student.isBlocked())
                .putString("uid",student.getUid())
                .putString("mobile", student.getMobile_no())
                .build();

        try {
            FirebaseCrashlytics.getInstance().setCustomKeys(keysAndValues);

        }catch (Exception e){
            Toast.makeText(getContext(), ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isAddentanceTime() {
        SharedPreferences time_shareprefrence = this.getActivity().getSharedPreferences("attendance_time", Context.MODE_PRIVATE);
        String time=time_shareprefrence.getString("time","");
        Gson gson=new Gson();
        if(time.equals("")){
            return true;
        }


        Additional_Class additional_class=gson.fromJson(time,Additional_Class.class);
        Date dt = new Date();
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("kk:mm:ss");
        System.out.println("Time in 24 hr format = "+dateFormat.format(dt));
        String curTime_str=dateFormat.format(dt);

        String timearr[]=additional_class.getAttendance_data().split("-");




        int from_time=Integer.parseInt(timearr[0].substring(0,2));

        int to_time=Integer.parseInt(timearr[1].substring(0,2));

        int cur_time=Integer.parseInt(curTime_str.substring(0,2));

        Log.d("harshad",""+from_time);
        Log.d("harshad",""+to_time);
        Log.d("harshad",""+cur_time);

        //
        if(cur_time>=from_time && cur_time<=to_time){
            return  true;
        }else {
            AlertDialog.Builder alert_builder = new AlertDialog.Builder(getContext());
            alert_builder.setTitle("Attendance Time")
                    .setMessage("Attendance Will be Start \nFrom -: "+timearr[0]+"\nTo -:"+timearr[1])
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).show();
            return  false;
        }
    }

    private String getCurenntTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm aa");
        String formattedDate = dateFormat.format(new Date()).toString();
        return  formattedDate;
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
        progress_layout=view.findViewById(R.id.progress_layout);
        location_layout=view.findViewById(R.id.location_layout);
        map_btn_card = view.findViewById(R.id.map_cardView);
        cancle_map = view.findViewById(R.id.cancel_map);
        card_map = view.findViewById(R.id.Map_view);
        attendance_day_month = view.findViewById(R.id.attendance_in_month);
        attendance_persentage = view.findViewById(R.id.attendance_persentage);
        longitude = view.findViewById(R.id.longitude);
        latitude = view.findViewById(R.id.latitude);
        ShowNetWorkDilogBox();
        MapInitialization();

        initWidgets(view);
        selectedDate = LocalDate.now();
        year = getYear(selectedDate);
        month = monthYearFromDate(selectedDate);
        setMonthView();
        preMonth = view.findViewById(R.id.previousMonth);
        nextMOnt = view.findViewById(R.id.nextMonth);
        location_remider_text = view.findViewById(R.id.Location_reminder);
        inside_outside_maptxt = view.findViewById(R.id.m_inside_out_text);
        make_attendance_btn = view.findViewById(R.id.make_attendance);
        location_alert = new AlertDialog.Builder(getContext());
        my_logo=view.findViewById(R.id.my_logo);

        Glide.with(this).load(R.drawable.my_logo).into(my_logo);

    }

    private boolean isGPSLocationON() {
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return statusOfGPS;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isAlreadyPresent() {
        Present_date = sharedPreferences.getString("date", "");
        String str = getCurrentDate() + getCurrentMonth() + getCurrentYear();
        if (Present_date.equals(str)) {
            return true;
        }
        return false;

    }


    private boolean check_autoDateTimeOnOf() throws Settings.SettingNotFoundException {
        if (Settings.Global.getInt(getActivity().getContentResolver(), Settings.Global.AUTO_TIME) == 1) {
            return true;
        } else {
            return false;
            // Disabed
        }
    }

    private int getCurrentDate() {
        Date c = Calendar.getInstance().getTime();

        return (int) c.getDate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCurrentYear() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return localDate.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCurrentMonth() {
        LocalDate localDate = LocalDate.now();
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
        String uid = firebaseUser.getUid();
        databaseReference.child("attendance").child(uid).child(year).child(month).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Main_attendance_data.size() > 0) {
                    Main_attendance_data.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Attendance_data attendance_data = dataSnapshot.getValue(Attendance_data.class);
                    Main_attendance_data.add(attendance_data);
                    int p=attendance_data.getPresent();
                    if(attendance_data.getDescription().equals("yes")){
                        if (!present.contains(p)) {
                            present.add(p);
                        }
                    }else{
                        permission_present.add(p);
                    }


                }
                Log.d("harshadsalunke", present.toString());
                int days_presnt = present.size();
                int day_in_month = getDayIinMonth(selectedDate);
                String days = days_presnt + " / " + day_in_month + " Days";
                Float persentage = getPersentageOfPresenty(days_presnt, day_in_month);
                attendance_day_month.setText(days);
                attendance_persentage.setText(persentage + " %");
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
        return percentage;
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
        Main_attendance_data=new ArrayList<>();
        permission_present=new ArrayList<>();
        calendarAdapter = new CalendarAdapter(daysInMonth, getContext(), present,Main_attendance_data,permission_present);
        calendarAdapter.setHasStableIds(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int getDayIinMonth(LocalDate date) {
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();
        return daysInMonth;
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
    private Integer getSelectedDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        String selected = date.format(formatter);
        if (selected.startsWith("0")) {
            selected = selected.replace("0", "");
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

    public void ShowNetWorkDilogBox() {
        if (netConectivityDialog == null) {
            netConectivityDialog = new Dialog(getContext());
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
                    netConectivityDialog.dismiss();
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
        Context context = getContext();
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            boolean isConnected = networkInfo != null &&
                    networkInfo.isConnectedOrConnecting();
            return isConnected;
        }
        return false;

    }

    @Override
    public void onStart() {
        boolean check_autodate = true;
        try {
            check_autodate = check_autoDateTimeOnOf();
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (!check_autodate) {
            Intent intent = new Intent(getContext(), MyWerningActivity.class);
            getContext().startActivity(intent);
            getActivity().finish();
            return;
        }


        super.onStart();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        circle = mMap.addCircle(new CircleOptions()
//                .center(new LatLng(18.4228781, 73.9040033))
                        .center(new LatLng(18.4235915,73.9038324))
                .radius(1000)
                .strokeColor(Color.RED));
        circle2 = mMap.addCircle(new CircleOptions()
                .center(new LatLng(18.4235915,73.9038324))
                .radius(500)
                .strokeColor(Color.GREEN));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(18.4228781, 73.9040033), 14));
        getLocationPermission();


    }

    private void getLocationPermission() {
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        mMap.setMyLocationEnabled(true);

                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        if (permissionDeniedResponse.isPermanentlyDenied()) {
                            AlertDialog.Builder alert_builder = new AlertDialog.Builder(getContext());
                            alert_builder.setTitle("Permission Denied")
                                    .setMessage("Permission is permanently denied. To use This app you need go to setting to allow the permission.")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getContext().getPackageName(), null));
                                            startActivity(intent);
                                        }
                                    }).show();

                        } else {
                            AlertDialog.Builder alert_builder = new AlertDialog.Builder(getContext());
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




    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d("locations are",locationResult.getLastLocation()+"");
            Location location = locationResult.getLastLocation();
            longitude.setText(""+location.getLatitude());
            latitude.setText(""+location.getLongitude());
            Log.d("harshad",""+location.getLatitude());
            Log.d("harshad",""+location.getLongitude());
            progress_layout.setVisibility(View.GONE);
            location_layout.setVisibility(View.VISIBLE);

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