package edu.psu.cto5068.like_birbs;
import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Enviorment.class}, version = 1, exportSchema = false)
public abstract class EnviormentDatabase extends RoomDatabase {
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
}