package com.fazevaib.shushit.Activities;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fazevaib.shushit.MyReceiver;
import com.fazevaib.shushit.MyTrackingService;
import com.fazevaib.shushit.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Geocoder geocoder;
    Marker marker;
    MyReceiver myReceiver;
    Button b1;
    Intent savedPlaceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.placesFragment);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }

        b1 = (Button)findViewById(R.id.buttonAdd);
        savedPlaceIntent = new Intent(getApplicationContext(), SavedPlacesActivity.class);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.e("TAG", "Place: " + place.getAddress());

                geocoder = new Geocoder(getBaseContext());
                String placeName = (String) place.getName();
                LatLng placeLatLng = place.getLatLng();
                try {
                    List<Address> list = geocoder.getFromLocationName(placeName,1);
                    mMap.clear();
                    Address address = list.get(0);
                    double lat = placeLatLng.latitude;
                    double lng = placeLatLng.longitude;

                    goToLocation(lat, lng);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (marker == null)
                {
                    Toast.makeText(getApplicationContext(), "Select a location first", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    LatLng markedPlace = marker.getPosition();
                    String markedPlaceName = marker.getTitle();

                    Bundle args = new Bundle();
                    args.putParcelable("markedPlace", markedPlace);
                    args.putString("markedPlaceName", markedPlaceName);

                    savedPlaceIntent.putExtra("bundle", args);
                }
            }
        });


    }


    public void goToLocation(double lat, double lng) {
        String placeName = getLocation(lat, lng);
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll, 20);
        marker = mMap.addMarker(new MarkerOptions().position(ll).title(placeName));
        mMap.animateCamera(cameraUpdate);
    }

    public String getLocation(double lat, double lng) {
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            return obj.getAddressLine(0);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (marker != null){
                    marker.remove();
                    goToLocation(latLng.latitude, latLng.longitude);
                }
//                    marker.remove();
                else
                {
                    goToLocation(latLng.latitude, latLng.longitude);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuStart:
                Intent intent = new Intent(this, MyReceiver.class);
                LatLng markedPlace = marker.getPosition();
                Bundle args = new Bundle();
                args.putParcelable("markedPlace", markedPlace);
                intent.putExtra("bundle", args);

                this.sendBroadcast(intent);
                Log.i("TAG", "LAtlng sent: " + markedPlace.latitude + " " + markedPlace.longitude);
                Toast.makeText(this, "Tracking Service has Started", Toast.LENGTH_SHORT)
                        .show();
                break;

            case R.id.menuStop:
                Intent intent2 = new Intent(getApplicationContext(), MyTrackingService.class);
                getApplicationContext().stopService(intent2);
                Toast.makeText(getApplicationContext(), "Service Stopped", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuYourLocations:
                startActivity(savedPlaceIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
