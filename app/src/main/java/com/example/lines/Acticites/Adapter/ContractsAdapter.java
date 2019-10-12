package com.example.lines.Acticites.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lines.Acticites.Model.CustomerContract;
import com.example.lines.R;

import java.util.List;


public class ContractsAdapter extends RecyclerView.Adapter<ContractsAdapter.ViewHolder> {
    Context _ctx;
    List<CustomerContract>cList;
    public ContractsAdapter(Context _ctx,List<CustomerContract>cList) {
        this._ctx = _ctx;
        this.cList=cList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(_ctx).inflate(R.layout.contract_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomerContract cc=cList.get(position);
        holder.title.setText("Star Contract from School "+cc.getSchool());
        holder.supscription.setText(cc.getSupsecriptionPeriod()+" subscription");
        holder.email.setText(cc.getEmail());
        holder.place.setText(cc.getPlace());
        holder.price.setText(cc.getPrice());
        holder.fullName.setText(cc.getFullName());
        holder.phoneNumber.setText(cc.getPhoneNumber());
        if(!cc.getIsactivated()){
            holder.status.setText("In progress");
        }else{
            holder.status.setText("Activated");
        }
    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,supscription,fullName,price,phoneNumber,place,email,status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            supscription=itemView.findViewById(R.id.supscription);
            fullName=itemView.findViewById(R.id.fullName);
            price=itemView.findViewById(R.id.price);
            phoneNumber=itemView.findViewById(R.id.phoneNumber);
            place=itemView.findViewById(R.id.place);
            email=itemView.findViewById(R.id.email);
            status=itemView.findViewById(R.id.status);

        }
    }
}
