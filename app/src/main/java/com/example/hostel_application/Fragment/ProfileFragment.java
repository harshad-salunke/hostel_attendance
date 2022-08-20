package com.example.hostel_application.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hostel_application.Activitys.AcountDeleteActivity;
import com.example.hostel_application.Activitys.BlockActivity;
import com.example.hostel_application.Activitys.EditProfileActivity;
import com.example.hostel_application.Login.LoginActivity;
import com.example.hostel_application.Login.Login_info;
import com.example.hostel_application.R;
import com.example.hostel_application.models.Student;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Objects;


public class ProfileFragment extends Fragment {

ImageView profile_img;
CardView payment_card,exit_card,delete_card,contact_card,edit_card;
TextView name,clg,branch,year,room;
FirebaseAuth firebaseAuth;
ScrollView scrollView;
ShimmerFrameLayout shimmerFrameLayout;
DatabaseReference databaseReference;
FirebaseDatabase firebaseDatabase;
FirebaseUser firebaseUser;
Student student;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Students");
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        scrollView = view.findViewById(R.id.scrollView2);
        shimmerFrameLayout = view.findViewById(R.id.shimmerlayout);
        shimmerFrameLayout.startShimmer();
        databaseReference.child("All_student").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                student = snapshot.getValue(Student.class);
                setValueInView(student);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        payment_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://dexpertsystems.com/requestedLinkDataProcessing?query=uRzc/aJkn5lbFbif2Iolwe9fW0wb6YVnsEYgogo9w/w=&source=portal";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });
        exit_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            getActivity().finish();
            }
        });

        delete_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson=new Gson();

                String json=gson.toJson(student);
                Intent intent=new Intent(getActivity(), AcountDeleteActivity.class);
                intent.putExtra("myJson",json);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });

        contact_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:9359978498"));
                startActivity(intent);
            }
        });
        edit_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Gson gson=new Gson();

                    String json=gson.toJson(student);
                    Intent intent=new Intent(getActivity(), EditProfileActivity.class);
                    intent.putExtra("myJson",json);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(getActivity(), ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    private void setValueInView(Student student) {
        name.setText(student.getName());
        clg.setText(student.getClg_name());
        room.setText(student.getRoom_No());
        year.setText(student.getClg_year());
        branch.setText(student.getClg_branch());
        if(student.getGender().equals("Male")){
            profile_img.setImageResource(R.drawable.college_boy);

        }else {
            profile_img.setImageResource(R.drawable.college_girl);
        }

    }

    private void initView(View view) {
        profile_img=view.findViewById(R.id.profile_img);
edit_card=view.findViewById(R.id.edit_card);
        payment_card=view.findViewById(R.id.fees_card);
        exit_card=view.findViewById(R.id.exit_card);
        delete_card=view.findViewById(R.id.delete_card);
        contact_card=view.findViewById(R.id.contact_card);
        name=view.findViewById(R.id.profile_name);
        year=view.findViewById(R.id.profile_year);
        branch=view.findViewById(R.id.profile_branch);
        clg=view.findViewById(R.id.profile_clg);
        room=view.findViewById(R.id.profile_room);
    }
}