package edu.psu.cto5068.like_birbs;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.psu.cto5068.like_birbs.game.DisplayBirbs;
import edu.psu.cto5068.like_birbs.game.DisplayEventDialog;
import edu.psu.cto5068.like_birbs.game.DisplayLogDialog;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Game extends AppCompatActivity
        implements DisplayEventDialog.EventDialogListener,
        DisplayLogDialog.LogDialogListener, DisplayBirbs.LogDialogListener

{

    private String TAG = "Game";
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
    private ArrayList<Birb> temp;
    private String[] initialBirbsNames;
    private Enviorment env;
    private int[] birbImages;
    private boolean keepMusicGoing = false;
    private int currentEvent = 0;
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        EnviormentDatabase.insert(env);
        outState.putBoolean("newGame", false);
        outState.putInt("currentEvent", currentEvent);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle inState) {
        super.onRestoreInstanceState(inState);
        if (!inState.getBoolean("newGame", true)) {
            BirbDatabase.getDatabase(this);
            BirbDatabase.getAllBirbs(new BirbDatabase.AllBirbsListener() {
                @Override
                public void onBirbsReturn(List<Birb> birbs) {
                    temp = new ArrayList<>(birbs);
                }
            });
            EnviormentDatabase.getDatabase(this);
            EnviormentDatabase.getMostRecent(new EnviormentDatabase.EnviormentListener() {
                @Override
                public void onEnviormentReturn(Enviorment myEnv) {
                    env = myEnv;
                    env.setBirbs(temp);
                }
            });

            currentEvent = inState.getInt("currentEvent");
            ((TextView) findViewById(R.id.gencounter)).setText(env.getGenerationNum());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        if (savedInstanceState == null) {
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

                float[] hsv = new float[3];
                hsv[0] = initialBirbs[i][3];
                hsv[1] = 0.5f;
                hsv[2] = 0.75f;

                int outputColor = Color.HSVToColor(hsv);
                ImageView img = findViewById(birbImages[i]);
                VectorChildFinder vector = new VectorChildFinder(this, R.drawable.birb_the_perfect_birb, img);

                VectorDrawableCompat.VFullPath path1 = vector.findPathByName("bodyColor");
                path1.setFillColor(outputColor);


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
            switch (getIntent().getIntExtra("env", -1)) {
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
        }

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
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        switch (id) {
            case (R.id.view_birbs):
                v.vibrate(100);
                popLogDialog(env.outAllBirbs(), null);
                break;
            case (R.id.view_birth_logs):
                v.vibrate(100);
                popLogDialog(env.getLogs().getBirths(), false);
                break;
            case (R.id.view_death_logs):
                v.vibrate(100);
                popLogDialog(env.getLogs().getDeaths(), true);
                break;
            case (R.id.next_gen):
                // code view go to next gen
                // Todo things happen on environment
                // Todo Show dialog based on random event
                v.vibrate(100);
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
                this.currentEvent = randomEvent;

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

                //EnviormentDatabase.insert(env);

                System.out.println(tempBirbs.size());
                if (tempBirbs.size() == 0) {
                    Intent gameOver = new Intent(this, Game_over.class);
                    Bundle extras = new Bundle();
                    extras.putInt("totalGens", env.getGenerationNum());
                    extras.putInt("env", env.getLandType());
                    keepMusicGoing = true;
                    gameOver.putExtras(extras);
                    startActivity(gameOver);
                    finish();
                    return;
                }

                for (int i = 0; i < 14; i++) {
                    if (i <= tempBirbs.size() - 1) {
                        findViewById(birbImages[i]).setVisibility(View.VISIBLE);

                        float[] hsv = new float[3];
                        hsv[0] = (float) ((tempBirbs.get(i).getColorDecimal() * 1.0) / 32767) * 360;
                        hsv[1] = 0.5f;
                        hsv[2] = 0.75f;

                        int outputColor = Color.HSVToColor(hsv);

                        ImageView img = findViewById(birbImages[i]);
                        VectorChildFinder vector = new VectorChildFinder(this, R.drawable.birb_the_perfect_birb, img);

                        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("bodyColor");
                        path1.setFillColor(outputColor);

                        // call animateBirbs
                        animateBirbs((ImageView)findViewById(birbImages[i]));
                    }
                    else {
                        findViewById(birbImages[i]).setVisibility(View.INVISIBLE);
                    }
                }

                ((TextView) findViewById(R.id.gencounter)).setText(String.valueOf(env.getGenerationNum()));

                break;
            case (R.id.main_menu):
                // launch dialog asking to save / no save and quit
                v.vibrate(100);
                Intent nextScreenIntent = new Intent(this, Game_over.class);
                Bundle extras = new Bundle();
                extras.putInt("totalGens", env.getGenerationNum());
                nextScreenIntent.putExtras(extras);
                startActivity(nextScreenIntent);
                nextScreenIntent.putExtras(extras);
                keepMusicGoing = true;
                finish();
                break;
            default:
                // error
                break;
        }
    }

    // make birbs do animations
    public void animateBirbs(ImageView birb) {
        int whichAnimation;
        Random rand = new Random();
        whichAnimation = rand.nextInt(5);
        switch (whichAnimation) {
            case 0:
                // no animation, dont do anything
                break;
            case 1:
                // jump animation
                Animation birbbounce = AnimationUtils.loadAnimation(this, R.anim.birbbounce);
                birb.startAnimation(birbbounce);
                break;
            case 2:
                // backflip animation
                Animation birbspin = AnimationUtils.loadAnimation(this, R.anim.birbspin);
                birb.startAnimation(birbspin);
                break;
            case 3:
                // frontflip animation
                Animation birbspin2 = AnimationUtils.loadAnimation(this, R.anim.birbspin2);
                birb.startAnimation(birbspin2);
                break;
            case 4:
                // bend knees animation
                Animation birbbounce2 = AnimationUtils.loadAnimation(this, R.anim.birbbounce2);
                birb.startAnimation(birbbounce2);
                break;
        }
    }


    public void popLogDialog(ArrayList<String> logs, Boolean isDeaths) {
        Bundle arguments = new Bundle();
        if (isDeaths == null) {
            arguments.putStringArrayList("logs", logs);
            DialogFragment df = new DisplayBirbs();
            df.setArguments(arguments);
            df.show(getSupportFragmentManager(), "birbStats");
        }
        else {
            arguments.putBoolean("deathLogs", isDeaths);
            arguments.putStringArrayList("logs", logs);

            DialogFragment df = new DisplayLogDialog();
            df.setArguments(arguments);
            df.show(getSupportFragmentManager(), isDeaths ? "logDialogDeaths" : "logDialogBirths");
        }
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (this.currentEvent == Enviorment.CRASHED_SHIP) {
            int event = Enviorment.CRASHED_SHIP;
            while (event == Enviorment.CRASHED_SHIP) {
                event = (int) (Math.random() * 12) + 1;
            }
            Toast.makeText(this, "Ship Contained: " +
                    getResources().getStringArray(R.array.eventTitles)[event], Toast.LENGTH_LONG).show();
            env.setRandomEvent(event, (int) (Math.random() * 5) + 1);
            this.currentEvent = event;
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        if (this.currentEvent == Enviorment.CRASHED_SHIP) {
            Toast.makeText(this, "Birbs ignore the ship...", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onLogDialogPositiveClick(DialogFragment dialog) {
        switch (dialog.getTag()) {
            case "logDialogDeaths":
                popLogDialog(env.getLogs().getBirths(), false);
                break;
            case "logDialogBirths":
                popLogDialog(env.getLogs().getDeaths(), true);
                break;
        }
    }

    @Override
    public void onLogDialogNegativeClick(DialogFragment dialog) {
        // Todo: Handle negative action
    }
    @Override
    public void onResume() {
        super.onResume();
        keepMusicGoing = false;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean playMusicPreference = sharedPreferences.getBoolean("play_music", true);

        if(playMusicPreference) {
            startService(new Intent(Game.this, SoundService.class));
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        if (!keepMusicGoing) {
            stopService(new Intent(Game.this, SoundService.class));
        }
    }
}
