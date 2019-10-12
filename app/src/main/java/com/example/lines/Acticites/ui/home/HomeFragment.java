package com.example.lines.Acticites.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.lines.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private HomeViewModel homeViewModel;
    private GoogleMap mMap;
    CardView cvBottom,cvTop,cvBottomDriver;
    RecyclerView rv;
    AutoCompleteTextView destion;
    LinearLayout linBottom;
    RelativeLayout busFab,schoolFab;
    View viewBus,ViewSchool;
    Boolean checked=false;
    Button btn;
    TextView addressFrom,toAddress,date;
    private FusedLocationProviderClient fusedLocationClient;
    View root;
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if(root!=null){
            ViewGroup parent= (ViewGroup) root.getParent();
            if(parent!=null){
                parent.removeView(root);
            }
        }
        root = inflater.inflate(R.layout.fragment_home, container, false);
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
        HorzAdapter Adapter=new HorzAdapter(getContext());
        rv.setAdapter(Adapter);

        cvBottom=root.findViewById(R.id.cvBottom);
        cvTop=root.findViewById(R.id.cvTop);
        destion=root.findViewById(R.id.destion);
        String[] countries = getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, countries);
        destion.setAdapter(adapter);

        linBottom=root.findViewById(R.id.linBottom);
        destion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvBottom.setVisibility(View.GONE);
                cvTop.setVisibility(View.VISIBLE);
                linBottom.setVisibility(View.VISIBLE);
                toAddress.setText(destion.getText().toString());
            }
        });
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
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.mapsstyle));
            setupCurrentLocation();
            // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            if (!success) {
                Toast.makeText(getContext(), "error getting map", Toast.LENGTH_SHORT).show();
            }
        } catch (Resources.NotFoundException e) {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        // Position the map's camera near Sydney, Australia.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(-34, 151)));


    }
}