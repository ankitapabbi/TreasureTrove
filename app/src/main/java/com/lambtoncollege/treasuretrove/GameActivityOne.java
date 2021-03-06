package com.lambtoncollege.treasuretrove;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivityOne extends AppCompatActivity {

    private GameViewOne gameView;
    private TextView textViewScore,livesText;

    private boolean isGameOver;

    private boolean isSetNewTimerThreadEnabled;

    private int volumeThreshold;

    private Thread setNewTimerThread;

    private AlertDialog.Builder alertDialog;

    private MediaPlayer mediaPlayer,mediaPlayer1;

    private int gameMode;

    int life = 3;

    private GameActivityOne.AudioRecorder audioRecorder;

    private static final int TOUCH_MODE = 0x00;
    private static final int VOICE_MODE = 0x01;

    private Timer timer;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case UPDATE: {
                    if (gameView.isAlive()) {
                        isGameOver = false;
                        gameView.update();
                    } else {
                        if (isGameOver) {
                            break;
                        } else {
                            isGameOver = true;
                        }

                        if (gameMode == TOUCH_MODE) {
                            // Cancel the timer
                            timer.cancel();
                            timer.purge();
                        } else {
                            audioRecorder.isGetVoiceRun = false;
                            audioRecorder = null;
                            System.gc();
                        }

                        alertDialog = new AlertDialog.Builder(GameActivityOne.this);
                        alertDialog.setTitle("GAME OVER");
                        alertDialog.setMessage("Score: " + String.valueOf(gameView.getScore()) +
                                "\n" + "Would you like to RESTART?");
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                GameActivityOne.this.restartGame();
                                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                                intent.putExtra("Mode", "Touch");
                                startActivity(intent);
                                finish();
                            }
                        });
                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(),StartActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        alertDialog.show();
                    }

                    break;
                }

                case RESET_SCORE: {
                    textViewScore.setText("0");

                    break;
                }

                default: {
                    break;
                }
            }
        }
    };

    // The what values of the messages
    private static final int UPDATE = 0x00;
    private static final int RESET_SCORE = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_one);
        mediaPlayer1 = MediaPlayer.create(this,R.raw.aladdinhiphop);
        mediaPlayer1.setLooping(true);


        Intent intent = getIntent();
        String mode = intent.getStringExtra("Mode");

        livesText = (TextView)findViewById(R.id.lives);
        Toast.makeText(getApplicationContext(),"level 2 reached",Toast.LENGTH_LONG);
        Log.d("level","2");

        // Initialize the private views
        initViews();

        // Initialize the MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.coincollected);
        mediaPlayer.setLooping(false);


        // Get the mode of the game from the StartingActivity

            if (mode.equals("Touch")) {
                gameMode = TOUCH_MODE;
            } else {
                gameMode = VOICE_MODE;

                volumeThreshold = getIntent().getIntExtra("VolumeThreshold", 50);
            }


        // Set the Timer
        isSetNewTimerThreadEnabled = true;
        setNewTimerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Sleep for 3 seconds for the Surface to initialize
                    Thread.sleep(3000);
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    if (isSetNewTimerThreadEnabled) {
                        setNewTimer();
                    }
                }
            }
        });
        setNewTimerThread.start();

        if (gameMode == TOUCH_MODE) {
            // Jump listener
            gameView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            gameView.jump();

                            break;

                        case MotionEvent.ACTION_UP:


                            break;

                        default:
                            break;
                    }

                    return true;
                }
            });
        } else {
            audioRecorder = new GameActivityOne.AudioRecorder();
            audioRecorder.getNoiseLevel();
        }
    }

    private class AudioRecorder {

        private static final String TAG = "AudioRecord";

        int SAMPLE_RATE_IN_HZ = 8000;

        int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);

        AudioRecord mAudioRecord;

        boolean isGetVoiceRun;

        Object mLock;

        public AudioRecorder() {
            mLock = new Object();
        }

        public void getNoiseLevel() {
            if (isGetVoiceRun) {
                return;
            }
            mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
                    AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
            if (mAudioRecord == null) {
                Log.e(TAG, "mAudioRecord initialization failed.");
            }
            isGetVoiceRun = true;

            new Thread(new Runnable() {

                @Override
                public void run() {
                    mAudioRecord.startRecording();
                    short[] buffer = new short[BUFFER_SIZE];
                    while (isGetVoiceRun) {

                        int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
                        long v = 0;

                        for (int i = 0; i < buffer.length; i++) {
                            v += buffer[i] * buffer[i];
                        }

                        double mean = v / (double) r;
                        double volume = 10 * Math.log10(mean);

                        // Jump if the volume is loud enough
                        if (volume > volumeThreshold) {
                            GameActivityOne.this.gameView.jump();

                        }

                        synchronized (mLock) {
                            try {
                                mLock.wait(17);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    mAudioRecord.stop();
                    mAudioRecord.release();
                    mAudioRecord = null;
                }

            }).start();
        }
    }

    private void initViews() {
        gameView = findViewById(R.id.game_view);
        textViewScore = findViewById(R.id.text_view_score);
    }

    /**
     * Sets the Timer to update the UI of the GameView.
     */
    private void setNewTimer() {
        if (!isSetNewTimerThreadEnabled) {
            return;
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // Send the message to the handler to update the UI of the GameView
                GameActivityOne.this.handler.sendEmptyMessage(UPDATE);

                // For garbage collection
                System.gc();
            }

        }, 0, 17);
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }

        if (audioRecorder != null) {
            audioRecorder.isGetVoiceRun = false;
            audioRecorder = null;
        }

        isSetNewTimerThreadEnabled = false;

        super.onDestroy();
        mediaPlayer1.stop();
    }

    @Override
    protected void onPause() {
        isSetNewTimerThreadEnabled = false;

        super.onPause();
        mediaPlayer1.stop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mediaPlayer1.start();
    }

    /**
     * Updates the displayed score.
     *
     * @param score The new score.
     */
    public void updateScore(int score) {
        textViewScore.setText(String.valueOf(score));
    }

    public void updateLives(int lives) {
        livesText.setText(String.valueOf(lives));
        life = lives;
    }
    /**
     * Plays the music for score.
     */
    public void playScoreMusic() {
        if (gameMode == TOUCH_MODE) {
            mediaPlayer.start();

        }
    }

    /**
     * Restarts the game.
     */
    private void restartGame() {
        // Reset all the data of the over game in the GameView
        gameView.resetData();

        // Refresh the TextView for displaying the score
        new Thread(new Runnable() {

            @Override
            public void run() {
                handler.sendEmptyMessage(RESET_SCORE);
            }

        }).start();

        if (gameMode == TOUCH_MODE) {
            isSetNewTimerThreadEnabled = true;
            setNewTimerThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        // Sleep for 3 seconds
                        Thread.sleep(3000);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    } finally {
                        if (isSetNewTimerThreadEnabled) {
                            setNewTimer();
                        }
                    }
                }

            });
            setNewTimerThread.start();
        } else {
            audioRecorder = new GameActivityOne.AudioRecorder();
            audioRecorder.getNoiseLevel();
        }
    }

    private void startAgain(){

        if (gameMode == TOUCH_MODE) {
            isSetNewTimerThreadEnabled = true;
            setNewTimerThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        // Sleep for 3 seconds
                        Thread.sleep(3000);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    } finally {
                        if (isSetNewTimerThreadEnabled) {
                            setNewTimer();
                        }
                    }
                }

            });
            setNewTimerThread.start();
        } else {
            audioRecorder = new GameActivityOne.AudioRecorder();
            audioRecorder.getNoiseLevel();
        }
    }
    @Override
    public void onBackPressed() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }

        isSetNewTimerThreadEnabled = false;

        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaPlayer1.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer1.start();
    }

}
