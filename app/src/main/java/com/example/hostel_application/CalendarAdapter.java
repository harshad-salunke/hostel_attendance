package com.example.hostel_application;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hostel_application.Fragment.Bottom_view;
import com.example.hostel_application.models.Attendance_data;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>
{
    private final ArrayList<Integer> daysOfMonth;
    ArrayList<Integer> present;
    ArrayList<Attendance_data> attendance_data;
    ArrayList<Integer> permission;
    Context context;
    int present_date;

    public CalendarAdapter(ArrayList<Integer> daysOfMonth, Context context, ArrayList<Integer> present, ArrayList<Attendance_data> attendance_data, ArrayList<Integer> permission)
    {
        this.daysOfMonth = daysOfMonth;
        this.present=present;
        this.context=context;
        this.attendance_data=attendance_data;
        this.permission=permission;
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
             present_date=present.get(present.size()-1);
            if(date>0 && date<present_date){
                holder.cardView.setCardBackgroundColor(Color.parseColor("#E42323"));
            }
            if(present.contains(date)){
                holder.cardView.setCardBackgroundColor(Color.parseColor("#4CAF50"));
            }


        }

        if(permission.size()>0){
            if(permission.contains(date)){
                holder.cardView.setCardBackgroundColor(Color.parseColor("#FFC107"));

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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Attendance_data record=  getClickedData(date);

                  Bottom_view bottomSheetDialogFragment = new Bottom_view();

                  bottomSheetDialogFragment.show(((FragmentActivity)context).getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                bottomSheetDialogFragment.setData(record,present_date,date);

            }
        });
    }

    private Attendance_data getClickedData(int date) {
        for(Attendance_data a:attendance_data){
            Log.d("calender","data"+a.getPresent()+"=="+date);
            if(date==a.getPresent()){

                return  a;
            }
        }
        return  null;
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
