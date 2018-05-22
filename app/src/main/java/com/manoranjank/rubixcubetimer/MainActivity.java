package com.manoranjank.rubixcubetimer;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.concurrent.TimeUnit;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Chronometer;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    // @Override

    SoundPool mSoundPool;
    int mCSoundId;

    int seconds, minutes;
    RelativeLayout mRelativeLayout;
    Button mStart;
    TextView mTextView,vTextView;
    private final int NR_OF_SIMULTANEOUS_SOUNDS = 7;
    private final float LEFT_VOLUME = 1.0f;
    private final float RIGHT_VOLUME = 1.0f;
    private final int NO_LOOP = 0;
    private final int PRIORITY = 0;
    private final float NORMAL_PLAY_RATE = 1.0f;


    Handler customHandler= new Handler();
    long startTime=0L,timeinMilliseconds=0L,timeSwapBuff=0L,updateTime=0L;

    Runnable updateTimeThread=new Runnable() {
        @Override
        public void run() {
            timeinMilliseconds=SystemClock.uptimeMillis()-startTime;
            updateTime= timeSwapBuff+timeinMilliseconds;
            int secs=(int) (updateTime/1000);
            int min=secs/60;
            secs%=60;
         //   int milliseconds=(int)(updateTime/1000);
            mTextView.setText(""+min+":"+String.format("%2d",secs));
             customHandler.postDelayed(this,0);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textView1);
        vTextView=(TextView) findViewById(R.id.textView2);
        mRelativeLayout=(RelativeLayout) findViewById(R.id.layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(NR_OF_SIMULTANEOUS_SOUNDS)
                    .build();
        } else {
            // Deprecated way of creating a SoundPool before Android API 21.
            mSoundPool = new SoundPool(NR_OF_SIMULTANEOUS_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        }

        // Get the resource IDs to identify the sounds and store them in variables
        mCSoundId = mSoundPool.load(getApplicationContext(), R.raw.note1_c, 1);


       mRelativeLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               new CountDownTimer(15000, 1000) {


                   public void onTick(long millisUntilFinished) {
                       mRelativeLayout.setBackgroundColor(Color.RED);

                       if((millisUntilFinished/1000)>3)
                       {

                       vTextView.setText("Inspection Time");
                       mTextView.setText(String.valueOf(millisUntilFinished / 1000));
                   }
                         else
                       {
                           mTextView.setText(String.valueOf(millisUntilFinished / 1000));
                           mSoundPool.play(mCSoundId, LEFT_VOLUME, RIGHT_VOLUME, 0, NO_LOOP, NORMAL_PLAY_RATE);




                   }
                   mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           cancel();
                           onFinish();
                       }
                   });

                   }

                   public void onFinish() {

                       mRelativeLayout.setBackgroundResource(R.drawable.main);

                       vTextView.setText("Start !");


                       startTime =SystemClock.uptimeMillis();
                       customHandler.postDelayed(updateTimeThread,0);
                        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                timeSwapBuff+=timeinMilliseconds;
                                customHandler.removeCallbacks(updateTimeThread);
                                vTextView.setText("Time Taken to solve is");


                            }
                        });

                   }
               }.start();
           }
       });

    }}









