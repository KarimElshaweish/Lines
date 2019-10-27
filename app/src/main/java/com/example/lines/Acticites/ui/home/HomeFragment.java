package com.example.lines.Acticites.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lines.Acticites.Adapter.HorzAdapter;
import com.example.lines.Acticites.Model.CustomerContract;
import com.example.lines.Acticites.Model.Driver;
import com.example.lines.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private HomeViewModel homeViewModel;
    private GoogleMap mMap;
    CardView cvBottom,cvTop,cvBottomDriver;
    RecyclerView rv;
    Spinner destion;
    LinearLayout linBottom;
    RelativeLayout busFab,schoolFab;
    View viewBus,ViewSchool;
    Boolean checked=false;
    Button btn;
    TextView addressFrom,toAddress,date;
    private FusedLocationProviderClient fusedLocationClient;
    View root;
    List<Driver>driverList;
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    ImageView arrow;
    private void setupCurrentLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                onLocationChanged(location);
                            }
                        }
                    });
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            onLocationChanged(location);
                        }
                    }
                });
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(root!=null){
            ViewGroup parent= (ViewGroup) root.getParent();
            if(parent!=null){
                parent.removeView(root);
            }
        }else {
            root = inflater.inflate(R.layout.fragment_home, container, false);
        }
        date=root.findViewById(R.id.date);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        date.setText(formattedDate);
        addressFrom=root.findViewById(R.id.addressFrom);
        toAddress=root.findViewById(R.id.toAddress);
        cvBottomDriver=root.findViewById(R.id.cvBottomDriver);
        btn=root.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvBottomDriver.setVisibility(View.VISIBLE);
            }
        });
        viewBus=root.findViewById(R.id.viewBus);
        busFab=root.findViewById(R.id.bus);
        busFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked=!checked;
                viewBus.setBackgroundColor(getResources().getColor(R.color.yallow));
                busFab.setBackground(getResources().getDrawable(R.drawable.oval_yallow));

            }
        });
        ViewSchool=root.findViewById(R.id.ViewSchool);
        schoolFab=root.findViewById(R.id.school);
        schoolFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checked) {
                    ViewSchool.setBackgroundColor(getResources().getColor(R.color.yallow));
                    schoolFab.setBackground(getResources().getDrawable(R.drawable.oval_yallow));
                }
            }
        });
        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);

        rv=root.findViewById(R.id.rv);
        rv.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        getDrivers();


        cvBottom=root.findViewById(R.id.cvBottom);
        cvTop=root.findViewById(R.id.cvTop);
        destion=root.findViewById(R.id.destion);
        getSchools();
        String[] countries = getResources().getStringArray(R.array.countries_array);

        arrow=root.findViewById(R.id.arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvBottom.setVisibility(View.GONE);
                cvTop.setVisibility(View.VISIBLE);
                linBottom.setVisibility(View.VISIBLE);
                toAddress.setText(destion.getSelectedItem().toString());
                Toast.makeText(getContext(), toAddress.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        linBottom=root.findViewById(R.id.linBottom);
//        destion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

//                subscribeToUpdates();
//            }
//        });
        setupCurrentLocation();
        return root;
    }
    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Locale locale = new Locale("en");
        Geocoder geoCoder = new Geocoder(getActivity(), locale);
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            String finalAddress = address.get(0).getLocality();
            addressFrom.setText(finalAddress);
            LatLng current = new LatLng(lat, lng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current,19));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        subscribeToUpdates();
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.mapsstyle));
            if (!success) {
                Toast.makeText(getContext(), "error getting map", Toast.LENGTH_SHORT).show();
            }
        } catch (Resources.NotFoundException e) {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
    private void getDrivers(){
        FirebaseDatabase.getInstance().getReference("Drivers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        driverList=new ArrayList<>();
                        for(DataSnapshot dt:dataSnapshot.getChildren()){
                                driverList.add(dt.getValue(Driver.class));

                        }
                        HorzAdapter Adapter=new HorzAdapter(getContext(),driverList);
                        rv.setAdapter(Adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void subscribeToUpdates() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("locations");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d(TAG, "Failed to read value.", error.toException());
            }

        });
    }
    private void setMarker(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
        double lat = Double.parseDouble(value.get("latitude").toString());
        double lng = Double.parseDouble(value.get("longitude").toString());
        LatLng location = new LatLng(lat, lng);
        if (!mMarkers.containsKey(key)) {
            int height = 150;
            int width = 100;
            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.bus_marker);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .position(location)));
        } else {
            mMarkers.get(key).setPosition(location);
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mMarkers.values()) {
            builder.include(marker.getPosition());
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 3));
    }
    List<String>schoolList;
    private void getSchools(){
        FirebaseDatabase.getInstance().getReference("CustomerContract").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        schoolList=new ArrayList<>();
                        for(DataSnapshot dt:dataSnapshot.getChildren()){
                            CustomerContract contract=dt.getValue(CustomerContract.class);
                            if(contract.getIsactivated()){
                                schoolList.add(contract.getSchool());
                            }
                        }
                        ArrayAdapter<String> adapter =
                                    new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, schoolList);
                        destion.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}