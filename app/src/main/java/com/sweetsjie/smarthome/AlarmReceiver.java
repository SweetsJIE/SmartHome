package com.sweetsjie.smarthome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sweets on 17/2/24.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,BackgroundService.class);
        context.startService(i);
    }
}
