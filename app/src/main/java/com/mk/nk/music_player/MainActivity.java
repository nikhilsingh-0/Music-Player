package com.mk.nk.music_player;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView mSongTittle,mTime;
    Button mBackward,mPlay,mPause,mForward;
    SeekBar mSeekbar;

    double startTime = 0;
    double finalTime = 0;
    int forwardTime = 10000;
    int backwardTime = 10000;
    static int oneTimeOnly = 0;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSongTittle = findViewById(R.id.song_title);
        mTime = findViewById(R.id.time_left_text);
        mBackward = findViewById(R.id.back_btn);
        mPlay = findViewById(R.id.play_btn);
        mPause = findViewById(R.id.pause_btn);
        mForward = findViewById(R.id.forward_btn);
        mSeekbar = findViewById(R.id.seekBar);

        // media player

        mediaPlayer = MediaPlayer.create(this,R.raw.music);
        mSeekbar.setClickable(false);

        mSongTittle.setText("Srivali");

        mPlay.setOnClickListener( view -> {
            mediaPlayer.start();
            finalTime = mediaPlayer.getDuration();
            startTime = mediaPlayer.getCurrentPosition();

            if (oneTimeOnly==0){
                mSeekbar.setMax((int) finalTime);
                oneTimeOnly = 1;
            }

            mTime.setText(String.format("%d min, %d sec", TimeUnit.MICROSECONDS.toMinutes((long)finalTime),
                    TimeUnit.MICROSECONDS.toSeconds((long)finalTime)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)finalTime))));

            mSeekbar.setProgress((int) startTime);
            handler.postDelayed(updatSong,100);

        });
        mPause.setOnClickListener(view -> {
            mediaPlayer.pause();
        });
        mPause.setOnClickListener(view ->{
            mediaPlayer.pause();
        });
        mBackward.setOnClickListener(view -> {
            if(startTime-backwardTime>0){
                startTime = startTime-backwardTime;
                mediaPlayer.seekTo((int)startTime);
            }else{
                Toast.makeText(MainActivity.this,
                        "Can't Go Back!", Toast.LENGTH_SHORT).show();
            }
        });
        mForward.setOnClickListener(view -> {
            if ((startTime + forwardTime) <= finalTime){
                startTime = startTime + forwardTime;
                mediaPlayer.seekTo((int) startTime);
            }else{
                Toast.makeText(MainActivity.this,
                        "Can't Jump Forward!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private Runnable updatSong = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            mTime.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
            ));

            mSeekbar.setProgress((int)startTime);
            handler.postDelayed(this,100);
        }
    };
}