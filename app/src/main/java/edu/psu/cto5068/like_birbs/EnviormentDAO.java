package edu.psu.cto5068.like_birbs;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface EnviormentDAO {
    @Insert
    void insertCurrentRandomEventType(int currentRandomEventType);

    @Insert
    void insertGenerationNum(int generationNum);

    @Insert
    void insertRandomEventDurationLeft(int randomEventDurationLeft);

    @Insert
    void insertLandType(int landType);

    //@Query("SELECT * FROM enviorment WHERE 1==1")
    //int
}
