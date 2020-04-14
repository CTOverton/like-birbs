package edu.psu.cto5068.like_birbs;
import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.lang.reflect.GenericArrayType;

@Database(entities = {Enviorment.class}, version = 1, exportSchema = false)
public abstract class EnviormentDatabase extends RoomDatabase {
    public interface EnviormentListener {

    }

    public abstract EnviormentDAO enviormentDAO();

    private static EnviormentDatabase INSTANCE;

    public static synchronized EnviormentDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EnviormentDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                               EnviormentDatabase.class, "EnviormentDB")
                               .addCallback(createEnviormentDatabaseCallback)
                               .build();
                }
            }
        }
        return INSTANCE;
    }


    private static RoomDatabase.Callback createEnviormentDatabaseCallback =
            new RoomDatabase.Callback() {
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    // something else is in the notes here, idk if i should add that tho?? it was
                    // an array storing Blum's awesome jokes, but we want ints
                }
            };

    // then the code for updates/inserts/etc go here

    public static void insertCurrentRandomEventType(int currentRandomEventType) {
        new AsyncTask<Integer, Void, Void>(){
            protected Void doInBackground(int currentRandomEventType) {
                INSTANCE.enviormentDAO().insertCurrentRandomEventType(currentRandomEventType);
                return null;
            }
        }.execute(currentRandomEventType);
    }

    public static void insertGenerationNum(int generationNum) {
        new AsyncTask<Integer, Void, Void>(){
            protected Void doInBackground(int generationNum) {
                INSTANCE.enviormentDAO().insertGenerationNum(generationNum);
                return null;
            }
        }.execute(generationNum);
    }

    public static void insertRandomEventDurationLeft(int randomEventDurationLeft) {
        new AsyncTask<Integer, Void, Void>(){
            protected Void doInBackground(int randomEventDurationLeft) {
                INSTANCE.enviormentDAO().insertRandomEventDurationLeft(randomEventDurationLeft);
                return null;
            }
        }.execute(randomEventDurationLeft);
    }

    public static void insertLandType(int landType) {
        new AsyncTask<Integer, Void, Void>(){
            protected Void doInBackground(int landType) {
                INSTANCE.enviormentDAO().insertLandType(landType);
                return null;
            }
        }.execute(landType);
    }

    public static void deleteAll() {
        new AsyncTask<Void, Void, Void>(){
            protected Void doInBackground(Void... voids) {
                INSTANCE.enviormentDAO().deleteAll();
                return null;
            }
        }.execute();
    }

    public static void updateCurrentRandomEventType(int currentRandomEventType) {
        new AsyncTask<Integer, Void, Void>(){
            protected void doInBackground(int currentRandomEventType) {
                INSTANCE.enviormentDAO().updateCurrentRandomEventType(currentRandomEventType);
            }
        }.execute(currentRandomEventType);
    }

    public static void updateRandomEventDurationLeft(int randomEventDurationLeft) {
        new AsyncTask<Integer, Void, Void>(){
            protected void doInBackground(int randomEventDurationLeft) {
                INSTANCE.enviormentDAO().updateRandomEventDurationLeft(randomEventDurationLeft);
            }
        }.execute(randomEventDurationLeft);
    }

    public static int getLandType() {
        new AsyncTask<Void, Void, Integer>(){
            protected int doInBackground(){
                return INSTANCE.enviormentDAO().getLandType();
            }
            protected void onPostExecute(int LandType){
                super.onPostExecute(LandType);
            }
        }.execute();
    }

    public static int getGenerationNum() {
        new AsyncTask<Void, Void, Integer>(){
            protected int doInBackground(){
                return INSTANCE.enviormentDAO().getGenerationNum();
            }
            protected void onPostExecute(int GenerationNum){
                super.onPostExecute(GenerationNum);
            }
        }.execute();
    }

    public static int getCurrentRandomEventType() {
        new AsyncTask<Void, Void, Integer>(){
            protected int doInBackground(){
                return INSTANCE.enviormentDAO().getCurrentRandomEventType();
            }
            protected void onPostExecute(int CurrentRandomEventType){
                super.onPostExecute(CurrentRandomEventType);
            }
        }.execute();
    }

    public static int getRandomEventDurationLeft() {
        new AsyncTask<Void, Void, Integer>(){
            protected int doInBackground(){
                return INSTANCE.enviormentDAO().getRandomEventDurationLeft();
            }
            protected void onPostExecute(int RandomEventDurationLeft){
                super.onPostExecute(RandomEventDurationLeft);
            }
        }.execute();
    }
}