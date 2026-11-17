package com.locktoggles.torch;

import android.app.Activity;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;

public class ToggleFlashlightActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraManager cm = getSystemService(CameraManager.class);
        try {
            for (String id : cm.getCameraIdList()) {
                CameraCharacteristics ch = cm.getCameraCharacteristics(id);
                if (ch.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == Boolean.TRUE) {
                    boolean on = ch.get(CameraCharacteristics.FLASH_STATE)
                            == CameraCharacteristics.FLASH_STATE_TORCH;
                    cm.setTorchMode(id, !on);
                    break;
                }
            }
        } catch (CameraAccessException ignored) {
        }
        finish();
    }
}
