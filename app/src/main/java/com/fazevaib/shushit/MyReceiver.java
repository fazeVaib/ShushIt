package com.fazevaib.shushit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Log.e("TAG", "onReceive: Receiving broadcast" );
        Bundle bundle = intent.getParcelableExtra("bundle");
        LatLng latLng = bundle.getParcelable("markedPlace");

        if (latLng != null) {
            Log.i("TAG", "LAtlng received in myReceiver: " + latLng.latitude + " " + latLng.longitude);
        }

        Intent i = new Intent(context, MyTrackingService.class);

        Bundle args = new Bundle();
        args.putParcelable("markedPlace", latLng);
        i.putExtra("bundle", args);
        context.startService(i);
    }
}
