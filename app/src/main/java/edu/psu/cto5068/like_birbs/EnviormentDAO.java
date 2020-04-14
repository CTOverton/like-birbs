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
    void insertCurrentRandomEventType(Enviorment env);

    @Insert
    void insertGenerationNum(Enviorment env);

    @Insert
    void insertRandomEventDurationLeft(Enviorment env);

    @Insert
    void insertLandType(Enviorment env);

    @Query("DELETE FROM enviorment WHERE 1=1")
    void deleteAll();

    @Query("SELECT landBin FROM enviorment")
    int getLandType();

    @Query("SELECT genBin FROM enviorment")
    int getGenerationNum();

    @Query("SELECT currentRandomEventTypeBin FROM enviorment")
    int getCurrentRandomEventType();

    @Query("SELECT randomEventDurationLeftBin FROM enviorment")
    int getRandomEventDurationLeft();

    @Update
    void updateCurrentRandomEventType(Enviorment env);

    @Update
    void updateRandomEventDurationLeft(Enviorment env);
}
