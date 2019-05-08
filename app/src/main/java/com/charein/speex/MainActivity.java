package com.charein.speex;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.charein.speex.lib.recoder.SpeexPlayer;
import com.charein.speex.lib.recoder.SpeexRecorder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int STOPPED = 0;
    public static final int RECORDING = 1;
    private boolean hasPerms;

    // PcmRecorder recorderInstance = null;
    SpeexRecorder recorderInstance = null;

    Button startButton = null;
    Button stopButton = null;
    Button playButton = null;
    Button exitButon = null;
    TextView textView = null;
    int status = STOPPED;

    String fileName = null;
    SpeexPlayer splayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startButton = new Button(this);
        stopButton = new Button(this);
        exitButon = new Button(this);
        playButton = new Button(this);
        textView = new TextView(this);

        startButton.setText("Start");
        stopButton.setText("Stop");
        playButton.setText("Play");
        exitButon.setText("Exit");
        textView.setText("android speex");

        startButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        exitButon.setOnClickListener(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(textView);
        layout.addView(startButton);
        layout.addView(stopButton);
        layout.addView(playButton);
        layout.addView(exitButon);
        this.setContentView(layout);

        reqPermission();
    }

    @Override
    public void onClick(View v) {
        if (!hasPerms) {
            reqPermission();
            return;
        }

        if (v == startButton) {
            this.setTitle("startButton");
            fileName = "/mnt/sdcard/gauss.spx";
            //	fileName =  "/mnt/sdcard/1324966898504.spx";

            if (recorderInstance == null) {
                // recorderInstance = new PcmRecorder();

                recorderInstance = new SpeexRecorder(fileName);

                Thread th = new Thread(recorderInstance);
                th.start();
            }
            recorderInstance.setRecording(true);
        } else if (v == stopButton) {
            this.setTitle("stopButton");
            recorderInstance.setRecording(false);
        } else if (v == playButton) {
            // play here........
            this.setTitle("playButton");
            fileName = "/mnt/sdcard/gauss.spx";
            System.out.println("filename====" + fileName);
            splayer = new SpeexPlayer(fileName);
            splayer.startPlay();

        } else if (v == exitButon) {
            recorderInstance.setRecording(false);
            System.exit(0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    hasPerms = false;
                    return;
                }
            }

            hasPerms = true;
        }
    }

    private void reqPermission() {
        List<String> perms = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            perms.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            perms.add(Manifest.permission.RECORD_AUDIO);
        }

        if (perms.size() == 0) {
            hasPerms = true;
        } else {
            String[] array = new String[perms.size()];
            array = perms.toArray(array);
            ActivityCompat.requestPermissions(this, array, 1);
        }
    }
}
