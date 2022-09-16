package com.student.hostel_application.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.student.hostel_application.Adapters.NoticeAdapter;
import com.student.hostel_application.R;
import com.student.hostel_application.models.Notice;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AnnouncementFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    NoticeAdapter noticeAdapter;
    ArrayList<Notice> arrayList;
    ShimmerFrameLayout shimmerFrameLayout;
    ImageView my_logo;
    LinearLayout empty_notice;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_announcement, container, false);
        init_recyclerView(view);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Students").child("announcement");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(arrayList.size()>0){
                    arrayList.clear();
                }
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Notice notice=dataSnapshot.getValue(Notice.class);
                    arrayList.add(0,notice);
                }
                noticeAdapter.notifyDataSetChanged();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                if(arrayList.size()==0){
                    empty_notice.setVisibility(View.VISIBLE);
                }else{
                    empty_notice.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return  view;
    }

    private void init_recyclerView(View view) {
        recyclerView=view.findViewById(R.id.notic_recyclerView);
        shimmerFrameLayout=view.findViewById(R.id.notice_shimmer);
        shimmerFrameLayout.startShimmer();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList=new ArrayList<>();
        noticeAdapter=new NoticeAdapter(arrayList,getContext());
        recyclerView.setAdapter(noticeAdapter);
        empty_notice=view.findViewById(R.id.empty_notice);
        my_logo=view.findViewById(R.id.my_logo2);

        Glide.with(this).load(R.drawable.my_logo).into(my_logo);
    }


}