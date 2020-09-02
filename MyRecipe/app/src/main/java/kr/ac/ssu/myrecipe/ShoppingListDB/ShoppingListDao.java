package kr.ac.ssu.myrecipe.ShoppingListDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShoppingListDao {
    @Query("SELECT * FROM ShoppingListData")
    List<ShoppingListData> getAll();

    @Query("SELECT * FROM ShoppingListData WHERE name = :target")
    ShoppingListData findData(String target);

    @Query("SELECT * FROM ShoppingListData ORDER BY id ASC")
    List<ShoppingListData> sortData();

    @Query("DELETE FROM ShoppingListData")
    void deleteAll();

    @Insert
    void insert(ShoppingListData data);

    @Update
    void update(ShoppingListData dafa);

    @Delete
    void delete(ShoppingListData data);
}
