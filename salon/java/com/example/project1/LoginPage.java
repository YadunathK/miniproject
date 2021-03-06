package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginPage extends AppCompatActivity
{
    TextView register;
    EditText ownerShopID,password;
    String sownerShopID,spassword;
    Button Login;
    DatabaseReference reference;
    Owner owner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        this.setTitle("Login");

        Login=(Button)findViewById(R.id.loginSignInButton);
        register=(TextView)findViewById(R.id.loginRegisterTextView);
        reference= FirebaseDatabase.getInstance().getReference().child("ShopOwners");

        owner=new Owner();


        ownerShopID=(EditText)findViewById(R.id.loginUserName);
        password=(EditText)findViewById(R.id.loginUserPassword);

        Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sownerShopID=ownerShopID.getText().toString();
                spassword=password.getText().toString();

                if (sownerShopID.equals("admin")&&spassword.equals("1234"))
                {
                    Intent intent=new Intent(getApplicationContext(),AdminPage.class);
                    startActivity(intent);
                }
                else
                {

                    Query query=reference.orderByChild("shopID").equalTo(sownerShopID);
                    query.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            if (dataSnapshot.exists())
                            {
                                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                                {
                                    owner=snapshot.getValue(Owner.class);

                                    if (spassword.equalsIgnoreCase(owner.Password))
                                    {
                                        if (owner.status.equals(true))
                                        {
                                            SharedPreferences.Editor editor=getSharedPreferences("OwnerLogin",MODE_PRIVATE).edit();
                                            editor.putString("shopID",sownerShopID);
                                            editor.commit();

                                            Toast.makeText(getApplicationContext(), "Logged in successfuly", Toast.LENGTH_SHORT).show();

                                            Intent intent=new Intent(getApplicationContext(),OwnerPage.class);
                                            startActivity(intent);
                                            ownerShopID.setText("");
                                            password.setText("");
                                        }
                                        else
                                        {
                                            ownerShopID.setText("");
                                            password.setText("");
                                            Intent intent=new Intent(getApplicationContext(),OwnerPermissionCheck.class);
                                            startActivity(intent);
                                        }

                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            else
                            {
                                ownerShopID.setError("Shop does registered with this id....! Try register");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {
                            Toast.makeText(getApplicationContext(), "Error....!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(getApplicationContext(),RegisterPage.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}