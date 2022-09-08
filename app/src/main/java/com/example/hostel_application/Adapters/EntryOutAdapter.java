package com.example.hostel_application.Adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.example.hostel_application.Activitys.EntryOutResponseActivity;
import com.example.hostel_application.R;
import com.example.hostel_application.models.EntryOutModel;
import com.example.hostel_application.models.Student;
import com.google.gson.Gson;

import java.util.ArrayList;

public class EntryOutAdapter extends RecyclerView.Adapter<EntryOutAdapter.ViewHolder> {
    Context context;
    ArrayList<EntryOutModel> arrayList;

    public EntryOutAdapter(Context context, ArrayList<EntryOutModel> arrayList) {
        this.context=context;
        this.arrayList=arrayList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.entryout_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EntryOutModel entryOutModel=arrayList.get(position);

        if(entryOutModel.getAccepted().equals("t")){
            Glide.with(context).load(R.drawable.successfuly).into(holder.imageView);
            holder.layout.setBackgroundResource(R.drawable.success_bg);
            holder.pending_text.setText("Accepted");
        }
       else if (entryOutModel.getAccepted().equals("f")){
            Glide.with(context).load(R.drawable.rejected).into(holder.imageView);
            holder.layout.setBackgroundResource(R.drawable.cancle_bg);
            holder.pending_text.setText("Rejected");

        }else{
            Glide.with(context).load(R.drawable.pending).into(holder.imageView);
            holder.layout.setBackgroundResource(R.drawable.pending_bg);
            holder.pending_text.setText("Under Review");

        }

        holder.from_date.setText(entryOutModel.getFrom_date());
        holder.to_date.setText(entryOutModel.getTo_date());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson=new Gson();
                String obj=gson.toJson(entryOutModel);
                Intent intent=new Intent(context, EntryOutResponseActivity.class);
                intent.putExtra("obj",obj);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView from_date,to_date,pending_text;
        ImageView imageView;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            from_date=itemView.findViewById(R.id.from_date);
            to_date=itemView.findViewById(R.id.to_date);
            pending_text=itemView.findViewById(R.id.pending_text);
            imageView=itemView.findViewById(R.id.pending_img);
            layout=itemView.findViewById(R.id.layout_item);
        }
    }
}
