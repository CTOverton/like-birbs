package edu.psu.cto5068.like_birbs;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Start_2_customize_birb extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private BirbNameGenerator names;

    // TODO: These will need to be persist locally in the activity
    private int enviormentChoice;
    private int[][] initialBirbPercents = new int[10][5];
    private String[] birbNames = new String[10];
    private int birbsMade = 0;
    // End TODO

    private SeekBar colorSeekBar;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private boolean keepMusicGoing = false;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("env", enviormentChoice);
        outState.putInt("birbsMade", birbsMade);
        outState.putStringArray("birbnames", birbNames);
        for (int i = 0; i < birbsMade; i++) {
            outState.putIntArray("birb" + i, initialBirbPercents[i]);
        }
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle inState) {
        super.onRestoreInstanceState(inState);
        birbsMade = inState.getInt("env");
        enviormentChoice = inState.getInt("birbsMade");
        birbNames = inState.getStringArray("birbnames");
        for (int i = 0; i < birbsMade; i++) {
            initialBirbPercents[i] = inState.getIntArray("birb" + i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_2_customize_birb);

        final Context mContext = this;

        mVisible = true;
        names = new BirbNameGenerator();
        enviormentChoice = getIntent().getIntExtra("env", 0);

        colorSeekBar = findViewById(R.id.seekBar_body_color);
        colorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float[] hsv = new float[3];
                hsv[0] = progress;
                hsv[1] = 0.5f;
                hsv[2] = 0.75f;

                int outputColor = Color.HSVToColor(hsv);

                ImageView img = findViewById(R.id.imageView_birb);
                VectorChildFinder vector = new VectorChildFinder(mContext, R.drawable.birb_the_perfect_birb, img);

                VectorDrawableCompat.VFullPath path1 = vector.findPathByName("bodyColor");
                path1.setFillColor(outputColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public void onClick(View view) {
        int clickId = view.getId();

        switch (clickId) {
            case(R.id.imageView_birb):
                ((TextView) findViewById(R.id.textView_birbname)).setText(names.getRandomName());
                break;
            case(R.id.exit_button):
                Intent back = new Intent(this, FullscreenActivity.class);
                keepMusicGoing = true;
                startActivity(back);
                finish();
                return;
            case(R.id.done_button):
                if (birbsMade >= 10) {
                    // the Dr. Jeremy Blum Bug Hot Fix
                    break;
                }
                initialBirbPercents[birbsMade][0] = ((SeekBar) findViewById(R.id.seekBar_strength)).getProgress();
                initialBirbPercents[birbsMade][1] = ((SeekBar) findViewById(R.id.seekBar_speed)).getProgress();
                initialBirbPercents[birbsMade][2] = ((SeekBar) findViewById(R.id.seekBar_feathers)).getProgress();
                initialBirbPercents[birbsMade][3] = ((SeekBar) findViewById(R.id.seekBar_body_color)).getProgress();
                initialBirbPercents[birbsMade][4] = ((SeekBar) findViewById(R.id.seekBar_swim)).getProgress();
                birbNames[birbsMade] = ((TextView) findViewById(R.id.textView_birbname)).getText().toString();
                Toast.makeText(this, "Birb " + birbNames[birbsMade] + " Spawned!", Toast.LENGTH_SHORT).show();

                birbsMade++;
                ((TextView) findViewById(R.id.textView_birbname)).setText(names.getRandomName());

                if (birbsMade == 10) {
                    Intent startGame = new Intent(this, Game.class);
                    keepMusicGoing = true;
                    for (int i = 0; i < 10; i++) {
                        startGame.putExtra("birb" + i, initialBirbPercents[i]);
                    }
                    startGame.putExtra("birbNames", birbNames);
                    startGame.putExtra("env", enviormentChoice);
                    startGame.putExtra("newgame", true);
                    startActivity(startGame);

                    for (int i = 0; i < 10; i++) {
                        System.out.println(birbNames[i]);
                        for (int j = 0; j < 5; j++) {
                            System.out.print(initialBirbPercents[i][j] + " ");
                        }
                        System.out.println();
                    }
                    BirbDatabase.getDatabase(this);
                    BirbDatabase.nukeAll();
                    finish();
                }
                else {
                    ((TextView) findViewById(R.id.textView_birbnum)).setText((birbsMade + 1) + "/10");
                }

            default:
                break;

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        keepMusicGoing = false;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean playMusicPreference = sharedPreferences.getBoolean("play_music", true);

        if(playMusicPreference) {
            startService(new Intent(Start_2_customize_birb.this, SoundService.class));
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        if (!keepMusicGoing) {
            stopService(new Intent(Start_2_customize_birb.this, SoundService.class));
        }
    }
}
