package com.example.lines.Acticites.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lines.Acticites.Activites.DriverProfile;
import com.example.lines.Acticites.Model.Driver;
import com.example.lines.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HorzAdapter extends RecyclerView.Adapter<HorzAdapter.ViewHolder> {
    @NonNull
    Context _ctx;
    List<Driver>driverList;
    private void getProfilePicture(final CircleImageView circleImageView, int position){
        FirebaseDatabase.getInstance().getReference("Profiles").child(driverList.get(position).getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ProfilePicture profilePicture=dataSnapshot.getValue(ProfilePicture.class);
                        if(profilePicture!=null)
                        Picasso.get().load(profilePicture.getUrl()).placeholder(R.drawable.placholder)
                                .into(circleImageView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    public HorzAdapter(@NonNull Context _ctx,List<Driver>driverList) {
        this._ctx = _ctx;
        this.driverList=driverList;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)    {
        View view= LayoutInflater.from(_ctx).inflate(R.layout.rv_item_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(_ctx, DriverProfile.class);
                intent.putExtra("ID",driverList.get(position).getId());
                intent.putExtra("Name",driverList.get(position).getFullName());
                _ctx.startActivity(intent);
            }
        });
        holder.fullName.setText(driverList.get(position).getFullName());
        getProfilePicture(holder.avatar,position);
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView fullName,tab;
        CircleImageView avatar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv= itemView.findViewById(R.id.cv);
            fullName=itemView.findViewById(R.id.fullName);
            tab=itemView.findViewById(R.id.tab);
            avatar=itemView.findViewById(R.id.avatar);
        }
    }
}
