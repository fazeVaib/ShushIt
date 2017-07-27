package com.fazevaib.shushit.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.fazevaib.shushit.R;
import com.google.android.gms.maps.model.LatLng;

public class SavedPlacesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places);

        TextView tvName = (TextView)findViewById(R.id.tvTrial);
        Bundle bundle = getIntent().getParcelableExtra("bundle");
        LatLng latLng = bundle.getParcelable("markedPlace");
        String name = bundle.getString("markedPlaceName");

        tvName.setText(name);
    }
}
