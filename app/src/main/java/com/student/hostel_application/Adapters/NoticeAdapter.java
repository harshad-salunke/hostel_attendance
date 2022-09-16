package com.student.hostel_application.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.student.hostel_application.NoticeBoardActivity;
import com.student.hostel_application.R;
import com.student.hostel_application.models.Notice;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>
{
    ArrayList<Notice> arrayList;
    Context context;
    public NoticeAdapter(ArrayList<Notice> arrayList, Context context)
    {
        this.arrayList = arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.announ_item, parent, false);

        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        Notice notice=arrayList.get(position);
        holder.title.setText(notice.getTitle());


        String arr[]=notice.getDate().split(" ");
        holder.date.setText(arr[0]);
        holder.month.setText(arr[1]);
        holder.admin_name.setText(notice.getOwner());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String myJson = gson.toJson(notice);
                Intent intent=new Intent(context, NoticeBoardActivity.class);
                intent.putExtra("myjson", myJson);
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }
    static class NoticeViewHolder extends RecyclerView.ViewHolder
    {

        TextView title,date,month,admin_name;
        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.notic_title);
            date=itemView.findViewById(R.id.noti_board_date);
            month=itemView.findViewById(R.id.noti_board_month);
            admin_name=itemView.findViewById(R.id.addmin_name);
        }
    }


}
