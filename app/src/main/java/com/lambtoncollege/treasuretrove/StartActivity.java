package com.lambtoncollege.treasuretrove;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;

public class StartActivity extends AppCompatActivity {

    private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 0x00;

    private int volumeThreshold;
    private AlertDialog.Builder alertDialog;
    Button info;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        info = (Button)findViewById(R.id.info);
        mediaPlayer = MediaPlayer.create(this,R.raw.aladdinhiphop);
        mediaPlayer.setLooping(true);

        // Get the volume threshold
        SharedPreferences settings = getPreferences(0);
        volumeThreshold = settings.getInt("VolumeThreshold", 50);


        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog = new AlertDialog.Builder(StartActivity.this);
                alertDialog.setTitle("Treasure Trove");
                alertDialog.setMessage("In This Game We have 3 Levels. You have to collect 10, 20, 30 points in level 1, 2, 3 respectively to reach next level. Enjoy the game. ");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });
    }



    public void goToTouchActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("Mode", "Touch");
        startActivity(intent);
    }

    public void goToVoiceActivity(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.RECORD_AUDIO },
                    RECORD_AUDIO_PERMISSION_REQUEST_CODE);
        } else {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("Mode", "Voice");
            intent.putExtra("VolumeThreshold", volumeThreshold);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RECORD_AUDIO_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted
                    Intent intent = new Intent(this, GameActivity.class);
                    intent.putExtra("Mode", "Voice");
                    startActivity(intent);
                } else {
                    // Permission denied
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("Permission Denied...");
                    alertDialog.setMessage("Without record audio permission granted, " +
                            "you cannot play with voice control.");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                        }
                    });
                    alertDialog.show();
                }
                return;
            }
        }
    }

    public void adjustVolumeThreshold(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Scroll to adjust the threshold of you voice.");
        alertDialog.setIcon(R.drawable.ic_bird);
        View alertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.alert_dialog_adjust_volume_threshold, null);
        NumberPicker numberPicker = (NumberPicker) alertDialogView.findViewById(R.id.number_picker);
        numberPicker.setMaxValue(300);
        numberPicker.setMinValue(0);
        numberPicker.setValue(volumeThreshold);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker np, int oldValue, int newValue) {
                volumeThreshold = np.getValue();
            }
        });
        alertDialog.setView(alertDialogView);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                // Save the change in the SharedPreferences
                SharedPreferences settings = StartActivity.this.getPreferences(0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("VolumeThreshold", StartActivity.this.volumeThreshold);
                editor.apply();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mediaPlayer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }
}

