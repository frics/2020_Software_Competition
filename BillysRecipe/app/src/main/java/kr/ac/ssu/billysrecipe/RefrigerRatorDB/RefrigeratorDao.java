package kr.ac.ssu.billysrecipe.RefrigerRatorDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RefrigeratorDao {
    @Query("SELECT * FROM RefrigeratorData")
    List<RefrigeratorData> getAll();

    @Query("SELECT * FROM refrigeratordata WHERE name = :target")
    RefrigeratorData findData(String target);

    @Query("SELECT * FROM refrigeratordata WHERE category = :target")
    List<RefrigeratorData> findDataByTag(String target);

    @Query("SELECT * FROM refrigeratordata ORDER BY category ASC")
    List<RefrigeratorData> sortData();

    @Query("DELETE FROM refrigeratordata")
    void deleteAll();

    @Insert
    void insert(RefrigeratorData data);

    @Update
    void update(RefrigeratorData dafa);

    @Delete
    void delete(RefrigeratorData data);
}
