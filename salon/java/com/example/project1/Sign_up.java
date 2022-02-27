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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Sign_up extends AppCompatActivity
{
    Button gotologin,bsignup;
    EditText eename,eephoneNo,eepasswoed;
    String sname,smobno,spassword,sphoneno;
    DatabaseReference rreference,reference;
    Customer ccustomer,customer;
    long bbackpress;
    Toast bbackToast;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Sign Up");

        gotologin=(Button)findViewById(R.id.signupButtonLogin);
        bsignup=(Button)findViewById(R.id.signupButtonsignup);
        eephoneNo=(EditText)findViewById(R.id.signupMobNo);
        eepasswoed=(EditText)findViewById(R.id.signupPassword);
        eename=(EditText)findViewById(R.id.signupName);

        ccustomer=new Customer();
        customer=new Customer();




        bsignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                smobno=eephoneNo.getText().toString();
                spassword=eepasswoed.getText().toString();
                sname=eename.getText().toString().trim();

                if (sname.isEmpty())
                {
                    eename.setError("Name is required");
                    eename.requestFocus();
                }
                else if (smobno.isEmpty())
                {
                    eephoneNo.setError("Mobile No. is required");
                    eephoneNo.requestFocus();
                }
                else if (smobno.length()<10)
                {
                    eephoneNo.setError("Mobile No. must have 10 numbers");
                    eephoneNo.requestFocus();
                }
                else if (spassword.isEmpty())
                {
                    eepasswoed.setError("Password is rquired");
                    eepasswoed.requestFocus();
                }
                else
                {
                    String code="91";
                    sphoneno="+" + code + smobno;


                    rreference= FirebaseDatabase.getInstance().getReference().child("Customer");

                    Query query=rreference.orderByChild("mobileNum").equalTo(sphoneno);
                    query.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            if (dataSnapshot.exists())
                            {
                                eephoneNo.setError("Customer already exist..! Try Login");
                                eephoneNo.requestFocus();
                            }
                            else
                            {

                                reference= FirebaseDatabase.getInstance().getReference().child("Customer").child(sphoneno);

                                customer.setName(sname);
                                customer.setMobileNum(sphoneno);
                                customer.setPassword(spassword);

                                reference.setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>()
                                {
                                    @Override
                                    public void onSuccess(Void aVoid)
                                    {
                                        Toast.makeText(getApplicationContext(),"Sucessfully Registered",Toast.LENGTH_SHORT).show();
                                    }
                                });

                                Intent intent = new Intent(getApplicationContext(), CustomerSignedIn1.class);

                                SharedPreferences.Editor editor=getSharedPreferences("UserLogin",MODE_PRIVATE).edit();
                                editor.putString("MobNo",sphoneno);
                                editor.putString("Name",sname);
                                editor.commit();

                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {
                            Toast.makeText(getApplicationContext(),"database error",Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });
        gotologin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void onBackPressed()
    {
        if (bbackpress+2000>System.currentTimeMillis())
        {
            bbackToast.cancel();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        else
        {
            bbackToast= Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT);
            bbackToast.show();
        }
        bbackpress=System.currentTimeMillis();
    }
}
