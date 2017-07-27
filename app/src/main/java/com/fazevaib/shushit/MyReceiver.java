package com.fazevaib.shushit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Log.e("TAG", "onReceive: Receiving broadcast" );
        Intent i = new Intent(context, MyTrackingService.class);
        context.startService(i);
    }
}
