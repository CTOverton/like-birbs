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
        void onEnviormentReturn(int val);
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
            protected Void doInBackground(Integer... currentEvent) {
                INSTANCE.enviormentDAO().insertCurrentRandomEventType(currentEvent[0]);
                return null;
            }
        }.execute(currentRandomEventType);
    }

    public static void insertGenerationNum(int generationNum) {
        new AsyncTask<Integer, Void, Void>(){
            protected Void doInBackground(Integer... generationNum) {
                INSTANCE.enviormentDAO().insertGenerationNum(generationNum[0]);
                return null;
            }
        }.execute(generationNum);
    }

    public static void insertRandomEventDurationLeft(int randomEventDurationLeft) {
        new AsyncTask<Integer, Void, Void>(){
            protected Void doInBackground(Integer... randomEventDurationLeft) {
                INSTANCE.enviormentDAO().insertRandomEventDurationLeft(randomEventDurationLeft[0]);
                return null;
            }
        }.execute(randomEventDurationLeft);
    }

    public static void insertLandType(int landType) {
        new AsyncTask<Integer, Void, Void>(){
            protected Void doInBackground(Integer... landType) {
                INSTANCE.enviormentDAO().insertLandType(landType[0]);
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
            protected Void doInBackground(Integer... currentRandomEventType) {
                INSTANCE.enviormentDAO().updateCurrentRandomEventType(currentRandomEventType[0]);
                return null;
            }
        }.execute(currentRandomEventType);
    }

    public static void updateRandomEventDurationLeft(int randomEventDurationLeft) {
        new AsyncTask<Integer, Void, Void>(){
            protected Void doInBackground(Integer... randomEventDurationLeft) {
                INSTANCE.enviormentDAO().updateRandomEventDurationLeft(randomEventDurationLeft[0]);
                return null;
            }
        }.execute(randomEventDurationLeft);
    }

    public static void getLandType(final EnviormentListener listener) {
        new AsyncTask<Void, Void, Integer>(){
            protected Integer doInBackground(Void... voids){
                return INSTANCE.enviormentDAO().getLandType();
            }
            protected void onPostExecute(Integer LandType){
                super.onPostExecute(LandType);
                listener.onEnviormentReturn(LandType);
            }
        }.execute();
    }

    public static void getGenerationNum(final EnviormentListener listener) {
        new AsyncTask<Void, Void, Integer>(){
            protected Integer doInBackground(Void... voids){
                return INSTANCE.enviormentDAO().getGenerationNum();
            }
            protected void onPostExecute(int GenerationNum){
                super.onPostExecute(GenerationNum);
                listener.onEnviormentReturn(GenerationNum);
            }
        }.execute();
    }

    public static void getCurrentRandomEventType(final EnviormentListener listener) {
        new AsyncTask<Void, Void, Integer>(){
            protected Integer doInBackground(Void... voids){
                return INSTANCE.enviormentDAO().getCurrentRandomEventType();
            }
            protected void onPostExecute(int CurrentRandomEventType){
                super.onPostExecute(CurrentRandomEventType);
                listener.onEnviormentReturn(CurrentRandomEventType);
            }
        }.execute();
    }

    public static void getRandomEventDurationLeft(final EnviormentListener listener) {
        new AsyncTask<Void, Void, Integer>(){
            protected Integer doInBackground(Void... voids){
                return INSTANCE.enviormentDAO().getRandomEventDurationLeft();
            }
            protected void onPostExecute(int RandomEventDurationLeft){
                super.onPostExecute(RandomEventDurationLeft);
                listener.onEnviormentReturn(RandomEventDurationLeft);
            }
        }.execute();
    }
}