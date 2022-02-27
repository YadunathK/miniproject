package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class AdminPage extends AppCompatActivity
{
    Button CheckNewShop,searchEdidShop,feed;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        this.setTitle("Admin Page");


        CheckNewShop=(Button)findViewById(R.id.checkForNewShop);
        searchEdidShop=(Button)findViewById(R.id.searchshopA);
        feed=(Button)findViewById(R.id.viewfeedbackA);

        CheckNewShop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(getApplicationContext(),AdminCheckNewShopAcceptance.class);
                startActivity(intent);
            }
        });
        feed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(getApplicationContext(),AdminViewFeedback.class);
                startActivity(intent);
            }
        });
        searchEdidShop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(getApplicationContext(),SearchEditShop_Admin.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(getApplicationContext(),LoginPage.class);
        startActivity(intent);
    }
}
