package edu.psu.cto5068.like_birbs;
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
    void insertCurrentRandomEventType(int currentRandomEventType);

    @Insert
    void insertGenerationNum(int generationNum);

    @Insert
    void insertRandomEventDurationLeft(int randomEventDurationLeft);

    @Insert
    void insertLandType(int landType);

    @Query("DELETE FROM enviorment")
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
    void updateCurrentRandomEventType(int currentRandomEventType);

    @Update
    void updateRandomEventDurationLeft(int randomEventDurationLeft);
}
