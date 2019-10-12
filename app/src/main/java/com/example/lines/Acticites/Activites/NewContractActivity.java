package com.example.lines.Acticites.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.lines.Acticites.Model.CustomerContract;
import com.example.lines.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewContractActivity extends AppCompatActivity {

    EditText fullName,phoneNumber,price,place,email,passengers;
    Spinner Spinner,period;
    SpinKitView spin_kit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contract);
        spin_kit=findViewById(R.id.spin_kit);
        fullName=findViewById(R.id.fullName);
        phoneNumber=findViewById(R.id.phoneNumber);
        price=findViewById(R.id.price);
        Spinner=findViewById(R.id.Spinner);
        period=findViewById(R.id.period);
        place=findViewById(R.id.place);
        email=findViewById(R.id.email);
        passengers=findViewById(R.id.passengers);
        setupSpinner();
    }
    List<String>School;
    List<String>periodList;
    private void setupSpinner() {
        School=new ArrayList<>();
        School.add("New Cairo School");
        School.add("El-Mokttam School");
        School.add("Nasr-Ciry School");
        ArrayAdapter<String>schoolAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,School);
        Spinner.setAdapter(schoolAdapter);

        periodList =new ArrayList<>();
        periodList.add("1 year");
        periodList.add("2 years");
        periodList.add("3 years");
        ArrayAdapter<String>yearAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,periodList);
        period.setAdapter(yearAdapter);
    }

    public void add(final View view) {
        spin_kit.setVisibility(View.VISIBLE);
        CustomerContract customerContract=getData();
        FirebaseDatabase.getInstance().getReference("CustomerContract")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(customerContract.getId())
                .setValue(customerContract)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        spin_kit.setVisibility(View.GONE);
                        Snackbar.make(view,e.getMessage(),Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        spin_kit.setVisibility(View.GONE);
                        finish();
                    }
                });
    }

    private CustomerContract getData() {
        CustomerContract customerContract=new CustomerContract();
        customerContract.setEmail(email.getText().toString());
        customerContract.setId(Calendar.getInstance().getTime().toString());
        customerContract.setIsactivated(false);
        customerContract.setPlace(place.getText().toString());
        customerContract.setSchool(Spinner.getSelectedItem().toString());
        customerContract.setPhoneNumber(phoneNumber.getText().toString());
        customerContract.setSupsecriptionPeriod(period.getSelectedItem().toString());
        customerContract.setPassngers(Integer.parseInt(passengers.getText().toString()));
        customerContract.setFullName(fullName.getText().toString());
        return customerContract;
    }
}
