package com.example.hostel_application;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>
{
    private final ArrayList<Integer> daysOfMonth;
    ArrayList<Integer> present;
    Context context;
    public CalendarAdapter(ArrayList<Integer> daysOfMonth, Context context,ArrayList<Integer> present)
    {
        this.daysOfMonth = daysOfMonth;
        this.present=present;
        this.context=context;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cakebdar_box, parent, false);

        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        int date=daysOfMonth.get(position);
        if(date==0){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        if(present.size()>0){
            int present_date=present.get(present.size()-1);
            if(date>0 && date<present_date){
                holder.cardView.setCardBackgroundColor(Color.parseColor("#E42323"));
            }
            if(present.contains(date)){
                holder.cardView.setCardBackgroundColor(Color.parseColor("#4CAF50"));
            }
        }


        String str_date;
        if(date==0){
            str_date="";
        }
        else {
            str_date=date+"";
        }
        holder.dayOfMonth.setText(str_date);

    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }
    static class CalendarViewHolder extends RecyclerView.ViewHolder
    {

        TextView dayOfMonth;
        CardView cardView;
        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfMonth=itemView.findViewById(R.id.cellDayText);
            cardView=itemView.findViewById(R.id.carView);
        }
    }


}
