package com.map.etruck.Fragments;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.map.etruck.BaseFragmet.BaseFragment;
import com.map.etruck.DialogHelper.DialogUtility;
import com.map.etruck.R;
import com.map.etruck.UI_Helper.BitMapImage_Projector;
import com.map.etruck.UI_Helper.CustomInfoWindowAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
public class MapFragmet extends BaseFragment implements
        OnMapReadyCallback {
    private static final String TAG = MapFragmet.class.getSimpleName();
    View mapFragmentView;
    Bundle bundleSavedinstanceState;
    MapView mapView;
    private final int LocationPermissionRequestCode = 100;
    private GoogleMap googleMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    Geocoder geocoder;
    List<Address> addresses;
    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mapFragmentView = inflater.inflate(R.layout.map_fragment, container, false);
        intializeViews(savedInstanceState);
        checkPermissionGiven(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        return mapFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

    }

    private void intializeViews(Bundle bundleSavedinstanceState) {
        mapView = (MapView) mapFragmentView.findViewById(R.id.map_view_id);
        this.bundleSavedinstanceState = bundleSavedinstanceState;
    }

    private void checkPermissionGiven(Bundle savedInstanceState) {
        if (isAdded()) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                setUpMap(bundleSavedinstanceState);
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LocationPermissionRequestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LocationPermissionRequestCode:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                       setUpMap(bundleSavedinstanceState);
                } else {
                    askPermission();
                }
        }
    }

    private void askPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.LOCATION_NOT_GIVEN), Toast.LENGTH_SHORT).show();
        } else {
            new DialogUtility().showPermissionDialog(getActivity(), "Location Denied");
        }
    }
    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    double latitute=currentLocation.getLatitude();
                    double longitude=currentLocation.getLongitude();
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 30);
                    googleMap.animateCamera(cameraUpdate);
                    getAddressFromLocation(latitute,longitude);
                }
            }
        });
    }

    private void setUpMap(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap=mMap;
                mMap.setMyLocationEnabled(true);
                fetchLocation();
            }
        });
    }

    private void getAddressFromLocation(double latitude,double longitude){
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        googleMap.clear();
        setCustomInfoWindowAdapter(latitude,longitude,""+addresses.get(0).getAddressLine(0));
    }

    private void setCustomInfoWindowAdapter(double lat,double longgi,String ad){
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        MarkerOptions markerOpt = new MarkerOptions();
                        markerOpt.position(new LatLng(lat,longgi));
                        markerOpt.title(ad);
        markerOpt.icon(BitMapImage_Projector.bitmapDescriptorFromVector(getActivity(), R.drawable.ic_delivery_truck));
        CustomInfoWindowAdapter customInfoWindowAdapter=new CustomInfoWindowAdapter(getActivity());
        googleMap.setInfoWindowAdapter(customInfoWindowAdapter);
        googleMap.addMarker(markerOpt).showInfoWindow();
    }

}
