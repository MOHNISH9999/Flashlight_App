package com.example.flashlight_app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnSwitch;
    private Animation animation;
    private boolean isFlashOn = false;
    private static final int CAMERA_REQUEST_CODE = 1;

    private CameraManager cameraManager;
    private String cameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the UI components
        btnSwitch = findViewById(R.id.btnSwitch);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);

        // Check if the device has flash
        if (!hasFlash()) {
            // Show a toast message if the device does not have flash
            Toast.makeText(MainActivity.this, "Flashlight is not available on this device", Toast.LENGTH_SHORT).show();
        }

        // Request camera permission if not granted
        if (!isCameraPermissionGranted()) {
            requestCameraPermission();
        }

        // Set up the camera manager
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        // Set button click listener
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFlashlight();
            }
        });
    }

    // Check if the device has flash
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    // Check if the camera permission is granted
    private boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    // Request camera permission
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
    }

    // Toggle the flashlight state
    private void toggleFlashlight() {
        if (isFlashOn) {
            turnOffFlashlight();
        } else {
            turnOnFlashlight();
        }
    }

    // Turn on the flashlight
    private void turnOnFlashlight() {
        try {
            if (cameraManager != null && cameraId != null) {
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                    cameraManager.setTorchMode(cameraId, true);
                }
                isFlashOn = true;
                btnSwitch.startAnimation(animation);
                btnSwitch.setImageResource(R.drawable.btn_switch_on);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // Turn off the flashlight
    private void turnOffFlashlight() {
        try {
            if (cameraManager != null && cameraId != null) {
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                    cameraManager.setTorchMode(cameraId, false);
                }
                isFlashOn = false;
                btnSwitch.startAnimation(animation);
                btnSwitch.setImageResource(R.drawable.btn_switch_off);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted
            } else {
                // Camera permission denied
                Toast.makeText(MainActivity.this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
