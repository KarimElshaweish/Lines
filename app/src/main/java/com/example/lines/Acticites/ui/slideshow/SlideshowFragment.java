package com.example.lines.Acticites.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lines.Acticites.Activites.NewContractActivity;
import com.example.lines.Acticites.Adapter.ContractsAdapter;
import com.example.lines.Acticites.Model.CustomerContract;
import com.example.lines.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {


    private RecyclerView rv;
    Button newContractBtn;
    List<CustomerContract>cList;
    ContractsAdapter adapter;
    SpinKitView spin_kit;
    private void getContracts(){
        spin_kit.setVisibility(View.VISIBLE);
        cList=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("CustomerContract")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        cList=new ArrayList<>();
                        for(DataSnapshot dt:dataSnapshot.getChildren()){
                            CustomerContract cc=dt.getValue(CustomerContract.class);
                            cList.add(cc);
                        }
                        spin_kit.setVisibility(View.GONE);
                        adapter=new ContractsAdapter(getContext(),cList);
                        rv.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        spin_kit=root.findViewById(R.id.spin_kit);
        cList=new ArrayList<>();
        rv=root.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new ContractsAdapter(getContext(),cList);
        rv.setAdapter(adapter);
        newContractBtn=root.findViewById(R.id.newContractBtn);
        newContractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewContractActivity.class));
            }
        });
        getContracts();
        return root;
    }
}