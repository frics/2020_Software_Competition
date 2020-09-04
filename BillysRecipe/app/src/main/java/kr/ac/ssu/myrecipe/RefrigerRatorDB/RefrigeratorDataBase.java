package kr.ac.ssu.myrecipe.RefrigerRatorDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RefrigeratorData.class},version = 1, exportSchema = false)
public abstract class RefrigeratorDataBase extends RoomDatabase {
    public abstract RefrigeratorDao Dao();

}
