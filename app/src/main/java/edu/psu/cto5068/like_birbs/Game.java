package edu.psu.cto5068.like_birbs;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import edu.psu.cto5068.like_birbs.game.DisplayEventDialog;
import edu.psu.cto5068.like_birbs.game.DisplayLogDialog;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Game extends AppCompatActivity
        implements DisplayEventDialog.EventDialogListener,
        DisplayLogDialog.LogDialogListener

{
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

    // Jacob's Stuff
    private int[][] initialBirbs = new int[10][6];
    private String[] initialBirbsNames;
    private Enviorment env;
    private int[] birbImages;
    //

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
        Log.wtf("Help", "OnCreate Called");
        setContentView(R.layout.activity_game);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        for (int i = 0; i < 10; i++) {
            initialBirbs[i] = getIntent().getIntArrayExtra("birb" + i);
        }
        initialBirbsNames = getIntent().getStringArrayExtra("birbNames");

        ArrayList<Birb> initBirbs = new ArrayList<>();
        BirbDatabase.getDatabase(this);
        EnviormentDatabase.getDatabase(this);

        for (int i = 0; i < initialBirbs.length; i++) {
            int dummyStr = (initialBirbs[i][0] * 65_536) / 100;
            int dummySpd = (initialBirbs[i][1] * 65_536) / 100;
            int dummyFth = (initialBirbs[i][2] * 65_536) / 100;
            int dummyCol = (initialBirbs[i][3] * 32_767) / 100;
            int dummySwm = (initialBirbs[i][4] * 65_536) / 100;
            System.out.println(dummyStr + " " + dummySpd + " " + dummyFth + " " + dummyCol + " " + dummySwm);

            String dummyName = initialBirbsNames[i];

            // there HAS to be a better way to do this
            int dummyId = (int) (Math.random() * Integer.MAX_VALUE);

            initBirbs.add(new Birb(dummyId, dummyStr, dummySpd, dummyFth, dummyCol, dummySwm, new boolean[]{false, false}, dummyName));
            System.out.println(i);
            BirbDatabase.insert(initBirbs.get(i));
        }
        if (getIntent().getIntExtra("env", -1) != -1) {
            env = new Enviorment(initBirbs, getIntent().getIntExtra("env", -1));
            EnviormentDatabase.insert(env);
        }
        switch (getIntent().getIntExtra("env", -1)){
            case 1:
                (findViewById(R.id.bg)).setBackground(ContextCompat.getDrawable(this, R.drawable.bg_meadow));
                break;
            case 2:
                (findViewById(R.id.bg)).setBackground(ContextCompat.getDrawable(this, R.drawable.bg_desert));
                break;
            case 3:
                (findViewById(R.id.bg)).setBackground(ContextCompat.getDrawable(this, R.drawable.bg_snow));
                break;
            case 4:
                (findViewById(R.id.bg)).setBackground(ContextCompat.getDrawable(this, R.drawable.bg_island));
                break;
            default:
                (findViewById(R.id.bg)).setBackground(ContextCompat.getDrawable(this, R.drawable.bg_rip));
                break;
        }
        birbImages = new int[]{
                R.id.birb0,
                R.id.birb1,
                R.id.birb2,
                R.id.birb3,
                R.id.birb4,
                R.id.birb5,
                R.id.birb6,
                R.id.birb7,
                R.id.birb8,
                R.id.birb9,
                R.id.birb10,
                R.id.birb11,
                R.id.birb12,
                R.id.birb13,
        };
        findViewById(R.id.birb10).setVisibility(View.INVISIBLE);
        findViewById(R.id.birb11).setVisibility(View.INVISIBLE);
        findViewById(R.id.birb12).setVisibility(View.INVISIBLE);
        findViewById(R.id.birb13).setVisibility(View.INVISIBLE);

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

    public void gameClick(View view) {
        int id = view.getId();

        switch (id) {
            case (R.id.view_logs):
                ArrayList<String> deaths = env.getLogs().getDeaths();
                ArrayList<String> births = env.getLogs().getBirths();
                // TODO: Here's 2 ArrayLists, they hold strings of logs
                // TODO: Each Log Holds 1 line of "Birb X has died because of Y after Z generations"
                // TODO: Make dialog box that pops up with single button to close
                // TODO: Output logs in a scrolly way because there could be a lot of logs per generation

                break;
            case (R.id.next_gen):
                // code view go to next gen
                // Todo things happen on environment
                // Todo Show dialog based on random event
                env.clearLogs();
                env.birbsEat();
                env.strongBirbsTakeFood();
                env.birbsStarve();
                env.predatorsEat();
                env.birbsTemperature();
                env.birbsDrown();

                if (env.enoughToReproduce()) {
                    env.birbsShuffle();
                    env.birbsReproduce();
                }
                env.increaseAliveTime();
                env.incrementGeneration();

                int randomEvent = env.randomEvent();

                if (randomEvent != Enviorment.NO_EVENT) {
                    Bundle args = new Bundle();
                    args.putString("title", getResources().getStringArray(R.array.eventTitles)[randomEvent]);
                    args.putString("message", getResources().getStringArray(R.array.eventMessages)[randomEvent]);

                    args.putString("posButton", getResources().getStringArray(R.array.eventPositive)[randomEvent]);
                    args.putString("negButton", getResources().getStringArray(R.array.eventNegative)[randomEvent]);

                    DialogFragment d = new DisplayEventDialog();
                    d.setArguments(args);
                    d.show(getSupportFragmentManager(), "eventDialog");
                    env.setRandomEvent(randomEvent, (int) (Math.random() * 5) + 1);
                }

                BirbDatabase.nukeAll();
                ArrayList<Birb> tempBirbs = env.getBirbs();

                for (Birb birb : tempBirbs) {
                    BirbDatabase.insert(birb);
                }

                EnviormentDatabase.insert(env);

                System.out.println(tempBirbs.size());
                if (tempBirbs.size() == 0) {
                    Intent gameOver = new Intent(this, Game_over.class);
                    gameOver.putExtra("totalGens", env.getGenerationNum());
                    startActivity(gameOver);
                }
                env.outAllBirbs();
                for (int i = 0; i < 14; i++) {
                    if (i < tempBirbs.size() - 1) {
                        findViewById(birbImages[i]).setVisibility(View.VISIBLE);
                    }
                    else {
                        findViewById(birbImages[i]).setVisibility(View.INVISIBLE);
                    }
                }

                break;
            case (R.id.main_menu):
                // launch dialog asking to save / no save and quit
                Intent nextScreenIntent = new Intent(this, FullscreenActivity.class);
                startActivity(nextScreenIntent);
                break;
            default:
                // error
                break;
        }
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // Todo: Handle positive action
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Todo: Handle negative action
    }

    @Override
    public void onLogDialogPositiveClick(DialogFragment dialog) {
        // Todo: Handle positive action
    }

    @Override
    public void onLogDialogNegativeClick(DialogFragment dialog) {
        // Todo: Handle negative action
    }
}
