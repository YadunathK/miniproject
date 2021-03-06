package com.example.project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class OwnerSignedIn extends AppCompatActivity
{
    EditText Amount,ModelName;
    Spinner Activity;
    Button Submit,AddImg;
    String sActivity,sAmount,sModelName,sModelImage="",sShopId;
    ImageView iimg;
    OwnerAdd ownerAdd;
    DatabaseReference reference,ref;
    String shopID1;
    Query query;
    int clicked=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_signed_in);
        this.setTitle("Owner");

        SharedPreferences sharedPreferences=getSharedPreferences("OwnerLogin",MODE_PRIVATE);
        shopID1=sharedPreferences.getString("shopID",null);

        ownerAdd=new OwnerAdd();
        SharedPreferences sharedPreference=getSharedPreferences("OwnerLogin",MODE_PRIVATE);
        sShopId=sharedPreference.getString("shopID",null);

        Activity=(Spinner)findViewById(R.id.ownerAddSpinnerActivity);
        Amount=(EditText)findViewById(R.id.ownerAddAmount);
        ModelName=(EditText)findViewById(R.id.ownerModelName);

        Submit=(Button)findViewById(R.id.OwnerAddSubmitButton);
        AddImg=(Button)findViewById(R.id.ownerBrowseImg);

        iimg=(ImageView) findViewById(R.id.ownerShowSelectedImg);

        reference= FirebaseDatabase.getInstance().getReference().child("ShopOwners").child(shopID1);


        AddImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clicked=1;
                Intent i=new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,1);
            }
        });



        Submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sActivity=Activity.getSelectedItem().toString();
                sAmount=Amount.getText().toString();
                sModelName=ModelName.getText().toString();

                if (sAmount.isEmpty())
                {
                    Amount.setError("Enter mount");
                    Amount.requestFocus();
                }
                else if (sModelName.isEmpty())
                {
                    ModelName.setError("Enter mount");
                    ModelName.requestFocus();
                }
                else if (sModelImage.isEmpty()||sModelImage.equals(""))
                {
                    if (clicked==1)
                    {
                        Toast.makeText(getApplicationContext(), "Wait for the image to load", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Select a Model Image", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    {
                        ref=FirebaseDatabase.getInstance().getReference().child("ShopOwners").child(sShopId).child("Activity");
                        Query query1=ref.orderByChild("activity").equalTo(sActivity);
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                if (dataSnapshot.exists())
                                {
                                    Toast.makeText(OwnerSignedIn.this, "This Service already Exist...\n Chose another service", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    ownerAdd.setActivity(sActivity);
                                    ownerAdd.setPrice(sAmount);
                                    ownerAdd.setModelName(sModelName);
                                    ownerAdd.setModelImg(sModelImage);
                                    ownerAdd.setShopID(sShopId);

                                    query=reference.orderByChild("shopID").equalTo(shopID1);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                        {
                                            ref= FirebaseDatabase.getInstance().getReference().child("ShopOwners").child(shopID1).child("Activity").child(sActivity);
                                            ref.setValue(ownerAdd).addOnSuccessListener(new OnSuccessListener<Void>()
                                            {
                                                @Override
                                                public void onSuccess(Void aVoid)
                                                {
                                                    Toast.makeText(getApplicationContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                                                    Amount.setText("");
                                                    ModelName.setText("");
                                                    iimg.setImageResource(R.drawable.hair);

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError)
                                        {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri fileuri = data.getData();

                StorageReference folder = FirebaseStorage.getInstance().getReference().child("ShopImage");

                String timestamp = String.valueOf(System.currentTimeMillis());

                final StorageReference filename = folder.child(timestamp + fileuri.getLastPathSegment());

                filename.putFile(fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        filename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                sModelImage = String.valueOf(uri);
                                Picasso.get().load(sModelImage).into(iimg);
                            }
                        });
                    }
                });
            }
        }
    }
}
