package com.locktoggles.torch;

import android.app.Activity;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ToggleFlashlightActivity extends Activity {

    private static final CountDownLatch sLatch = new CountDownLatch(1);
    private static volatile Boolean sTorchOn;
    private static String sCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraManager cm = getSystemService(CameraManager.class);
        ensureState(cm);
        boolean on = sTorchOn != null && sTorchOn;
        if (sCameraId != null) {
            try {
                cm.setTorchMode(sCameraId, !on);
            } catch (CameraAccessException ignored) {
            }
        }
        finish();
    }

    private static synchronized void ensureState(CameraManager cm) {
        if (sTorchOn != null) {
            return;
        }
        try {
            for (String id : cm.getCameraIdList()) {
                if (cm.getCameraCharacteristics(id)
                        .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == Boolean.TRUE) {
                    sCameraId = id;
                    break;
                }
            }
        } catch (CameraAccessException ignored) {
        }
        if (sCameraId == null) {
            return;
        }
        cm.registerTorchCallback(new CameraManager.TorchCallback() {
            @Override
            public void onTorchModeChanged(String id, boolean enabled) {
                if (id.equals(sCameraId)) {
                    sTorchOn = enabled;
                    sLatch.countDown();
                }
            }
        }, null);
        try {
            sLatch.await(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ignored) {
        }
    }
}
