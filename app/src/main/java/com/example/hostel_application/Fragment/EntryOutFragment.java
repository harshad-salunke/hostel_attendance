package com.example.hostel_application.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hostel_application.Activitys.EntryOutFormActivity;
import com.example.hostel_application.Adapters.EntryOutAdapter;
import com.example.hostel_application.Adapters.NoticeAdapter;
import com.example.hostel_application.R;
import com.example.hostel_application.models.EntryOutModel;
import com.example.hostel_application.models.Notice;
import com.example.hostel_application.models.Student;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;


public class EntryOutFragment extends Fragment {

FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
    RecyclerView recyclerView;
    EntryOutAdapter entryOutAdapter;
    ArrayList<EntryOutModel> arrayList;
    ShimmerFrameLayout shimmerFrameLayout;
    Student student;
ImageView my_logo;
LinearLayout empty_form;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_entry_out, container, false);
        init_recyclerView(view);
        SharedPreferences sharedPreferences;
        String user_str;
        sharedPreferences=getContext().getSharedPreferences("user_info",MODE_PRIVATE);
        user_str=sharedPreferences.getString("user","");
        Gson geson=new Gson();

        student=geson.fromJson(user_str,Student.class);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Admin");

        databaseReference.child("entryout").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (arrayList.size()>0){
                    arrayList.clear();
                }
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    EntryOutModel entryOutModel=dataSnapshot.getValue(EntryOutModel.class);
                    if (entryOutModel.getUser_id().equals(student.getUid())){
                    arrayList.add(0,entryOutModel);}
                }
                entryOutAdapter.notifyDataSetChanged();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if (arrayList.size()==0){
                    empty_form.setVisibility(View.VISIBLE);
                }else {
                    empty_form.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        view.findViewById(R.id.floatin_entryout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), EntryOutFormActivity.class);
                startActivity(intent);
            }
        });
        return  view;
    }

    private void init_recyclerView(View view) {
        recyclerView=view.findViewById(R.id.recylcer_view);
        shimmerFrameLayout=view.findViewById(R.id.notice_shimmer);
        shimmerFrameLayout.startShimmer();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList=new ArrayList<>();
        entryOutAdapter=new EntryOutAdapter(getContext(),arrayList);
        recyclerView.setAdapter(entryOutAdapter);
        my_logo=view.findViewById(R.id.my_logo2);
        empty_form=view.findViewById(R.id.empty_notice);

        Glide.with(this).load(R.drawable.my_logo).into(my_logo);
    }

}