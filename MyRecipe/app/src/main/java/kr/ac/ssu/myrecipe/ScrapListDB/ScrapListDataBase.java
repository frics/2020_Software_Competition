package kr.ac.ssu.myrecipe.ScrapListDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ScrapListData.class},version = 1, exportSchema = false)
public abstract class ScrapListDataBase extends RoomDatabase {
    public abstract ScrapListDao Dao();
}
