package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class signupOTP_Verification extends AppCompatActivity
{
    String verificationcode,mobno,password,name;
    ProgressBar progressBar;
    EditText editText;
    Button button;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    Customer customer;
    AlertDialog.Builder builder;
    CToast c;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_otp__verification);
        this.setTitle("OTP Verification");


        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        editText=(EditText)findViewById(R.id.signupOTP);
        button=(Button)findViewById(R.id.signupOTP_verificationButton);

        mAuth=FirebaseAuth.getInstance();
        customer=new Customer();
        c=new CToast();

        Intent intent=getIntent();
        mobno=intent.getStringExtra("signMobile");
        password=intent.getStringExtra("signPassword");
        name=intent.getStringExtra("signName");

        reference= FirebaseDatabase.getInstance().getReference().child("Customer").child(mobno);

        sendVerificationCode(mobno);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String code=editText.getText().toString().trim();
                if (code.isEmpty()||code.length()<6)
                {
                    editText.setError("Enter code...!");
                    editText.requestFocus();
                    return;
                }
                verfyCode(code);

            }
        });

    }
    public void verfyCode(String code)
    {
        editText.setText(code);
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationcode, code);
        signInWithCredential(credential);
    }
    void signInWithCredential(PhoneAuthCredential credentials)
    {
        mAuth.signInWithCredential(credentials).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {


                    customer.setName(name);
                    customer.setMobileNum(mobno);
                    customer.setPassword(password);

                    reference.setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            c.toast(getApplicationContext(),"Sucessfully Registered",1);
                        }
                    });

                    Intent intent = new Intent(getApplicationContext(), CustomerSignedIn1.class);

                    SharedPreferences.Editor editor=getSharedPreferences("UserLogin",MODE_PRIVATE).edit();
                    editor.putString("MobNo",mobno);
                    editor.putString("Name",name);
                    editor.commit();

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                {
                    c.toast(getApplicationContext(),"Wrong OTP Code",1);
                }

            }
        });
    }

    public void sendVerificationCode(String num)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(num,60, TimeUnit.SECONDS,signupOTP_Verification.this,mmCall);

    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mmCall=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential1)
        {
            String code=phoneAuthCredential1.getSmsCode();
            if (code!=null)
            {
                progressBar.setVisibility(View.VISIBLE);
                verfyCode(code);
            }

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationcode=s;
        }

        @Override
        public void onVerificationFailed(FirebaseException e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onBackPressed()
    {
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure want to LOGOUT ?") .setTitle("Cancel");

        //Setting message manually and performing action on button click
        builder.setMessage("Are you sure want to cancel ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {

                        c.toast(getApplicationContext(),"OTP Verification failed",1);

                        Intent intent=new Intent(getApplicationContext(),CustomerSignedIn1.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("LOGOUT");
        alert.show();
    }
}
