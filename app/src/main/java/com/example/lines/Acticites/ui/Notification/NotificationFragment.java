package com.example.lines.Acticites.ui.Notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lines.Acticites.Adapter.NotificationAdapter;
import com.example.lines.R;

public class NotificationFragment extends Fragment {

    private ToolsViewModel toolsViewModel;
    RecyclerView rv;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notification, container, false);
       rv=root.findViewById(R.id.rv);
       rv.setHasFixedSize(true);
       rv.setLayoutManager(new LinearLayoutManager(getContext()));
        NotificationAdapter adapter=new NotificationAdapter(getContext());
        rv.setAdapter(adapter);
        return root;
    }
}