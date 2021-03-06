package ga.hnbenterprises.like_birbs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BirbDAO {
    @Query("SELECT * FROM birbs ORDER BY gens")
    LiveData<List<Birb>> getAll();

    @Query("SELECT * FROM birbs WHERE rowid = :id")
    Birb getById(int id);

    @Insert
    void insert(Birb... birbs);

    @Update
    void updateBirbStatus(Birb birb);

    @Query("DELETE FROM birbs WHERE id = :id")
    void delete(int id);
}
