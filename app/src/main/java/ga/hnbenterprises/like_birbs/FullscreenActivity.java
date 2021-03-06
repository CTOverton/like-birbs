package ga.hnbenterprises.like_birbs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
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
    private boolean keepMusicGoing = false;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean playMusicPreference = sharedPreferences.getBoolean("play_music", true);

        if(playMusicPreference) {
            startService(new Intent(FullscreenActivity.this, SoundService.class));
        }
        else {
            stopService(new Intent(FullscreenActivity.this, SoundService.class));
        }
        setContentView(R.layout.activity_fullscreen);

        mVisible = true;

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
        mVisible = true;

    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public void gameClick(View view) {
        int id = view.getId();
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        switch (id) {
            case (R.id.exitButton):
                // code to exit game
                v.vibrate(100);
                System.exit(0);
                break;
            case (R.id.goToScoresButton):
                // code to see scores
                v.vibrate(100);
                keepMusicGoing = true;
                Intent nextScreenIntent = new Intent(this, Leaderbirb.class);
                startActivity(nextScreenIntent);
                break;
            case (R.id.goToCredits):
                v.vibrate(100);
                keepMusicGoing = true;
                Intent creditsScreenIntent = new Intent(this, credits.class);
                startActivity(creditsScreenIntent);
                break;
            case (R.id.playButton):
                // code to start game
                v.vibrate(100);
                keepMusicGoing = true;
                Intent playScreenIntent = new Intent(this, Start_1_selection.class);
                startActivity(playScreenIntent);
                break;
            case (R.id.settingsButton):
                v.vibrate(100);
                keepMusicGoing = true;
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
            default:
                // error
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
            startService(new Intent(FullscreenActivity.this, SoundService.class));
        }

        int[] birbImages = new int[] {
                R.id.imageView,
                R.id.imageView2,
                R.id.imageView3,
                R.id.imageView4,
                R.id.imageView5,
        };

        for (int i = 0; i < birbImages.length; i++) {
            float[] hsv = new float[3];
            hsv[0] = (float) (Math.random() * 360) + 1;
            hsv[1] = 0.5f;
            hsv[2] = 0.75f;

            int outputColor = Color.HSVToColor(hsv);

            ImageView img = findViewById(birbImages[i]);
            VectorChildFinder vector = new VectorChildFinder(this, R.drawable.birb_the_perfect_birb, img);

            VectorDrawableCompat.VFullPath path1 = vector.findPathByName("bodyColor");
            path1.setFillColor(outputColor);
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        if (!keepMusicGoing) {
            stopService(new Intent(FullscreenActivity.this, SoundService.class));
        }
    }
}
