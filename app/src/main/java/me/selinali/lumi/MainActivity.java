package me.selinali.lumi;

import android.graphics.Color;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    Camera camera;
    Boolean isLightOn;
    Switch lightSwitch;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isLightOn = false;
        lightSwitch = (Switch) findViewById(R.id.light_switch);
        view = findViewById(R.id.background);

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    camera = Camera.open();
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    camera.startPreview();
                    isLightOn = true;
                } else {
                    camera.stopPreview();
                    camera.release();
                    view.setBackgroundColor(getResources().getColor(R.color.gray));
                    isLightOn = false;
                }
            }
        });
    }
}
