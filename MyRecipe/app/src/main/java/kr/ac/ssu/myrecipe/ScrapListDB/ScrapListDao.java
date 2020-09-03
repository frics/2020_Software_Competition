package kr.ac.ssu.myrecipe.ScrapListDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ScrapListDao {
    @Query("SELECT * FROM ScrapListData")
    List<ScrapListData> getAll();

    @Query("SELECT * FROM ScrapListData WHERE id = :target")
    ScrapListData findData(int target);

    @Query("SELECT * FROM ScrapListData ORDER BY id ASC")
    List<ScrapListData> sortData();

    @Query("DELETE FROM ScrapListData")
    void deleteAll();

    @Insert
    void insert(ScrapListData data);

    @Update
    void update(ScrapListData data);

    @Delete
    void delete(ScrapListData data);
}
