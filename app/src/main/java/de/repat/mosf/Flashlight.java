package de.repat.mosf;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.io.IOException;

public class Flashlight extends Activity implements SurfaceHolder.Callback {

    Camera cam;
    SurfaceHolder mHolder;
    Parameters p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // portrait Mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // delete title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // start GUI
        setContentView(R.layout.activity_flashlight);

        //http://stackoverflow.com/questions/8876843/led-flashlight-on-galaxy-nexus-controllable-by-what-api/9379765#9379765
        SurfaceView preview = (SurfaceView) findViewById(R.id.PREVIEW);
        mHolder = preview.getHolder();
        mHolder.addCallback(this);

        // find Switch
        Switch s = (Switch) findViewById(R.id.switchled);

        // listen on change
        s.setOnCheckedChangeListener(new OnCheckedChangeListener() {



            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                // turn off
                if (!isChecked) {
                    if (cam != null) {
                        p.setFlashMode(Parameters.FLASH_MODE_OFF);
                        cam.setParameters(p);
                        cam.stopPreview();
                        cam.setPreviewCallback(null);
                        cam.release();
                        cam = null;
                    }

                }
                // turn on
                else {
                    if (cam == null) {
                        cam = Camera.open();
                        try {
                            cam.setPreviewDisplay(mHolder);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    p = cam.getParameters();
                    p.setFlashMode(Parameters.FLASH_MODE_TORCH);
                    cam.setParameters(p);
                    cam.startPreview();
                }
            }
        });
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;
        try {
            if (cam != null)
                cam.setPreviewDisplay(mHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (cam != null) {
            cam.stopPreview();
            cam.release();
        }
        mHolder = null;
    }
}
