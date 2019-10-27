package com.example.lines.Acticites.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.lines.Acticites.Model.Driver;
import com.example.lines.Acticites.Model.ProfilePicture;
import com.example.lines.Acticites.Model.Vechicel;
import com.example.lines.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverProfile extends AppCompatActivity {

    TextView fullName,licences;
    CircleImageView pc1,pc2,pc3,avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        String id=getIntent().getStringExtra("ID");
        String fullNameSt=getIntent().getStringExtra("Name");
        fullName=findViewById(R.id.fullName);
        fullName.setText(fullNameSt);
        licences=findViewById(R.id.licences);
        pc1=findViewById(R.id.pc1);
        pc2=findViewById(R.id.pc2);
        pc3=findViewById(R.id.pc3);
        avatar=findViewById(R.id.avatar);
        getProfileData(id);
        getProfileImage(id);
    }
    private void getProfileData(String ID){
        FirebaseDatabase.getInstance().getReference("Vechicle").child(ID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Vechicel vechicel=dataSnapshot.getValue(Vechicel.class);
                        updateUI(vechicel);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void updateUI(Vechicel driver) {
        licences.setText(driver.getLicences());
        switch (driver.getUriList().size()){
            case 1:
                Picasso.get().load(driver.getUriList().get(0)).into(pc1);
                break;
            case 2:
                Picasso.get().load(driver.getUriList().get(0)).into(pc1);
                Picasso.get().load(driver.getUriList().get(1)).into(pc2);
                break;
            case 3:
                Picasso.get().load(driver.getUriList().get(0)).into(pc1);
                Picasso.get().load(driver.getUriList().get(1)).into(pc2);
                Picasso.get().load(driver.getUriList().get(2)).into(pc3);
                break;
        }
    }

    public void finish(View view) {
        finish();
    }
    private void getProfileImage(String id){
        FirebaseDatabase.getInstance().getReference("Profiles").child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ProfilePicture profilePicture=dataSnapshot.getValue(ProfilePicture.class);
                        Picasso.get().load(profilePicture.getUrl()).placeholder(R.drawable.placholder).into(avatar);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
