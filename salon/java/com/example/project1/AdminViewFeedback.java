package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminViewFeedback extends AppCompatActivity
{
    DatabaseReference reference;
    ArrayList<ClassFeedback>list;
    RecyclerView recyclerView;
    AdapterFeedback adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_feedback);

        list=new ArrayList<ClassFeedback>();
        recyclerView=(RecyclerView)findViewById(R.id.rvFeedback);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference= FirebaseDatabase.getInstance().getReference().child("Feedback");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren())
                    {
                        ClassFeedback f=snapshot.getValue(ClassFeedback.class);
                        list.add(f);
                        Log.d("aaaa",f.feedback);
                    }
                    Log.d("aaaa","adapter");
                    adapter = new AdapterFeedback(AdminViewFeedback.this,list);
                    recyclerView.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(AdminViewFeedback.this, "No feedbacks yet", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
    }
}
