package com.example.hostel_application.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.hostel_application.R;
import com.example.hostel_application.models.Attendance_data;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Bottom_view extends BottomSheetDialogFragment {
    Attendance_data attendance_data;
TextView Date,Decription,absent;
int last_present,clicked_date;
LinearLayout linearLayout,Main_layout;
    View rootView;
    public Bottom_view() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         rootView = inflater.inflate(R.layout.bottom_fragment, container, false);
        initView(rootView);

        if(attendance_data!=null){
            showpresne();
        }else{
            absentData();
        }
        return rootView;
    }

    private void initView(View rootView) {
linearLayout=rootView.findViewById(R.id.bottom_presnet_layout);
Date=rootView.findViewById(R.id.bottom_date);
Decription =rootView.findViewById(R.id.bottom_description);
absent=rootView.findViewById(R.id.bottom_absent);
Main_layout=rootView.findViewById(R.id.main_layout);
    }

    public  void setData(Attendance_data attendance_data,int last_present,int clicked_date){
this.attendance_data=attendance_data;
this.last_present=last_present;
this.clicked_date=clicked_date;
}

    private void showpresne() {

        if(attendance_data.getDescription().equals("yes")){
            Main_layout.setBackground(getResources().getDrawable(R.drawable.green_background));
            Date.setText(attendance_data.getTime());
            Decription.setText("Present");
        }else {
            Main_layout.setBackground(getResources().getDrawable(R.drawable.yellow_background));
            Date.setText(attendance_data.getTime());
            Decription.setText(attendance_data.getDescription());
        }

    }

    private void absentData() {
        if(clicked_date<last_present){
            absent.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            Main_layout.setBackground(getResources().getDrawable(R.drawable.red_background));
        }

    }



}