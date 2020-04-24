package edu.psu.cto5068.like_birbs;
import android.os.Environment;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface EnviormentDAO {
    // note: table is enviorment
    @Insert
    void insert(Enviorment env);

    @Query("DELETE FROM enviorment WHERE 1=1")
    void deleteAll();

    @Query("SELECT * FROM enviorment ORDER BY genBin DESC LIMIT 1")
    Enviorment getMostRecent();

    @Update
    void update(Enviorment env);
}
