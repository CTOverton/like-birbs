package ga.hnbenterprises.like_birbs;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Birb.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class BirbDatabase extends RoomDatabase {
    public interface BirbListener {
        void onBirbReturn(Birb birb);
        void onBirbDeath(Birb ripBirb, int deathID);
    }
    public abstract BirbDAO birbDAO();
    private static BirbDatabase INSTANCE;

    public static synchronized BirbDatabase getDatabse(final Context context)  {
        if (INSTANCE == null) {
            synchronized (BirbDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                           BirbDatabase.class, "BirbsDB")
                           .addCallback(createBirbDatabaseCallback).build();
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback createBirbDatabaseCallback =
            new RoomDatabase.Callback() {
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    // No need to add default birbs
                }
            };


    public static void insert(Birb birb) {
        new AsyncTask<Birb, Void, Void>(){
            protected Void doInBackground(Birb... birbs) {
                INSTANCE.birbDAO().insert(birbs);
                return null;
            }
        }.execute(birb);
    }

    public static void getBirb(int id, final BirbListener listener) {
        new AsyncTask<Integer, Void, Birb>() {
            protected Birb doInBackground(Integer... ids) {
                return INSTANCE.birbDAO().getById((int) ids[0]);
            }
            protected void onPostExecute(Birb birb) {
                super.onPostExecute(birb);
                listener.onBirbReturn(birb);
            }
        }.execute(id);
    }

    // Deleting birbs one at a time to allow for a notification for all birbs
    // who are deleted.
    public static void delete(int birbID, final int deathID, final BirbListener listener) {
        // DeathID used for alerts on death
        new AsyncTask<Integer, Void, Birb>() {
            protected Birb doInBackground(Integer... ids) {
                Birb retBirb = INSTANCE.birbDAO().getById((int) ids[0]);
                INSTANCE.birbDAO().delete((int) ids[0]);
                return retBirb;
            }

            protected void onPostExecute(Birb birb) {
                super.onPostExecute(birb);
                listener.onBirbDeath(birb, deathID);
            }
        }.execute(birbID);
    }

    public static void updateStatus(Birb... birbs) {
        new AsyncTask<Birb, Void, Void>() {
            protected Void doInBackground(Birb... birbs) {
                for (Birb birb : birbs) {
                    INSTANCE.birbDAO().updateBirbStatus(birb);
                }
                return null;
            }
        }.execute(birbs);
    }
}
