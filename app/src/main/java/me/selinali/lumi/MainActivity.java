package me.selinali.lumi;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    CameraManager cameraManager;
    String[] camId;
    Camera camera;
    Boolean isLightOn;
    Switch lightSwitch;
    View view;
    boolean isMarshmallow;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isLightOn = false;
        lightSwitch = (Switch) findViewById(R.id.light_switch);
        view = findViewById(R.id.background);
        isMarshmallow = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

        if (isMarshmallow) {
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                camId = cameraManager.getCameraIdList();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.lights_on_copy);

                    if (isMarshmallow) {
                        try {
                            cameraManager.setTorchMode(camId[0], true);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        camera = Camera.open();
                        Camera.Parameters parameters = camera.getParameters();
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(parameters);
                        camera.startPreview();
                        isLightOn = true;
                    }

                } else {
                    view.setBackgroundResource(R.drawable.lights_off_copy);

                    if (isMarshmallow) {
                        try {
                            cameraManager.setTorchMode(camId[0], false);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        camera.stopPreview();
                        camera.release();
                        isLightOn = false;
                    }
                }
            }
        });
    }
}
