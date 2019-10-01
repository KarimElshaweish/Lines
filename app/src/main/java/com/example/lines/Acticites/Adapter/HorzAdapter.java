package com.example.lines.Acticites.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lines.Acticites.Activites.DriverProfile;
import com.example.lines.R;

import java.sql.Driver;

public class HorzAdapter extends RecyclerView.Adapter<HorzAdapter.ViewHolder> {
    @NonNull
    Context _ctx;

    public HorzAdapter(@NonNull Context _ctx) {
        this._ctx = _ctx;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)    {
        View view= LayoutInflater.from(_ctx).inflate(R.layout.rv_item_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _ctx.startActivity(new Intent(_ctx, DriverProfile.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv= itemView.findViewById(R.id.cv);
        }
    }
}
