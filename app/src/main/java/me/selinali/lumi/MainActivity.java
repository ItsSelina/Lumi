package me.selinali.lumi;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    CameraManager cameraManager;
    String[] camId;
    Camera camera;
    Boolean isLightOn;
    Switch lightSwitch;
    ImageView imageView;
    boolean isMarshmallow;
    ViewGroup parentLayout;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Drawable lightsOn = getDrawable(R.drawable.lights_on);
        final Drawable lightsOff = getDrawable(R.drawable.lights_off);

        isLightOn = false;
        lightSwitch = (Switch) findViewById(R.id.light_switch);
        imageView = (ImageView) findViewById(R.id.lumi_imageview);
        parentLayout = (ViewGroup) findViewById(R.id.parent_layout);
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
                    imageView.setImageDrawable(lightsOn);

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
                    imageView.setImageDrawable(lightsOff);

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
