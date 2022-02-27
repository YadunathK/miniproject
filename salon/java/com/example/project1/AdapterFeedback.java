package com.example.project1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterFeedback extends RecyclerView.Adapter<AdapterFeedback.CustomerViewHolder>
{
    private ValueEventListener mCtx;
    private ArrayList<ClassFeedback> list;
    Context context;
    DatabaseReference reference;

    AdapterFeedback(Context context, ArrayList<ClassFeedback> itemList)
    {
        this.context = context;
        list = itemList;
    }

    @NonNull
    @Override
    public AdapterFeedback.CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.cardview_feedback,null);
        return new AdapterFeedback.CustomerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull  final AdapterFeedback.CustomerViewHolder holder, final int position)
    {
        holder.t1.setText(list.get(position).getDate());
        holder.t2.setText(list.get(position).getUsername());
        holder.t3.setText(list.get(position).getNumber());
        holder.t4.setText(list.get(position).getFeedback());

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder
    {
        TextView t1,t2,t3,t4;


        public CustomerViewHolder(@NonNull View ownerView)
        {
            super(ownerView);

            t1=(TextView) ownerView.findViewById(R.id.fdate);
            t2=(TextView)ownerView.findViewById(R.id.fusername);
            t3=(TextView)ownerView.findViewById(R.id.fmobile);
            t4=(TextView)ownerView.findViewById(R.id.ffeedback);
        }

    }
}
