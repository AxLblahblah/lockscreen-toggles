package com.locktoggles.dnd;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class ToggleDndActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationManager nm = getSystemService(NotificationManager.class);
        if (!nm.isNotificationPolicyAccessGranted()) {
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            boolean enable = nm.getCurrentInterruptionFilter()
                    == NotificationManager.INTERRUPTION_FILTER_ALL;
            nm.setInterruptionFilter(enable
                    ? NotificationManager.INTERRUPTION_FILTER_PRIORITY
                    : NotificationManager.INTERRUPTION_FILTER_ALL);
        }
        finish();
    }
}
