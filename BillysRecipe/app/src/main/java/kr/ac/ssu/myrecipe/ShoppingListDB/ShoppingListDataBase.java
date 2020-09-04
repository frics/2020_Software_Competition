package kr.ac.ssu.myrecipe.ShoppingListDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ShoppingListData.class},version = 1, exportSchema = false)
public abstract class ShoppingListDataBase extends RoomDatabase {
    public abstract ShoppingListDao Dao();
}
