package kr.ac.ssu.billysrecipe.ScrapListDB;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ScrapListDao_Impl implements ScrapListDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ScrapListData> __insertionAdapterOfScrapListData;

  private final EntityDeletionOrUpdateAdapter<ScrapListData> __deletionAdapterOfScrapListData;

  private final EntityDeletionOrUpdateAdapter<ScrapListData> __updateAdapterOfScrapListData;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public ScrapListDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfScrapListData = new EntityInsertionAdapter<ScrapListData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `ScrapListData` (`id`,`totalNum`,`Scraped`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ScrapListData value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getTotalNum());
        stmt.bindLong(3, value.getScraped());
      }
    };
    this.__deletionAdapterOfScrapListData = new EntityDeletionOrUpdateAdapter<ScrapListData>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `ScrapListData` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ScrapListData value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfScrapListData = new EntityDeletionOrUpdateAdapter<ScrapListData>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `ScrapListData` SET `id` = ?,`totalNum` = ?,`Scraped` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ScrapListData value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getTotalNum());
        stmt.bindLong(3, value.getScraped());
        stmt.bindLong(4, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM ScrapListData";
        return _query;
      }
    };
  }

  @Override
  public void insert(final ScrapListData data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfScrapListData.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final ScrapListData data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfScrapListData.handle(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final ScrapListData data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfScrapListData.handle(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<ScrapListData> getAll() {
    final String _sql = "SELECT * FROM ScrapListData";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTotalNum = CursorUtil.getColumnIndexOrThrow(_cursor, "totalNum");
      final int _cursorIndexOfScraped = CursorUtil.getColumnIndexOrThrow(_cursor, "Scraped");
      final List<ScrapListData> _result = new ArrayList<ScrapListData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ScrapListData _item;
        _item = new ScrapListData();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpTotalNum;
        _tmpTotalNum = _cursor.getInt(_cursorIndexOfTotalNum);
        _item.setTotalNum(_tmpTotalNum);
        final int _tmpScraped;
        _tmpScraped = _cursor.getInt(_cursorIndexOfScraped);
        _item.setScraped(_tmpScraped);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public ScrapListData findData(final int target) {
    final String _sql = "SELECT * FROM ScrapListData WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, target);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTotalNum = CursorUtil.getColumnIndexOrThrow(_cursor, "totalNum");
      final int _cursorIndexOfScraped = CursorUtil.getColumnIndexOrThrow(_cursor, "Scraped");
      final ScrapListData _result;
      if(_cursor.moveToFirst()) {
        _result = new ScrapListData();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpTotalNum;
        _tmpTotalNum = _cursor.getInt(_cursorIndexOfTotalNum);
        _result.setTotalNum(_tmpTotalNum);
        final int _tmpScraped;
        _tmpScraped = _cursor.getInt(_cursorIndexOfScraped);
        _result.setScraped(_tmpScraped);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ScrapListData> sortData() {
    final String _sql = "SELECT * FROM ScrapListData ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTotalNum = CursorUtil.getColumnIndexOrThrow(_cursor, "totalNum");
      final int _cursorIndexOfScraped = CursorUtil.getColumnIndexOrThrow(_cursor, "Scraped");
      final List<ScrapListData> _result = new ArrayList<ScrapListData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ScrapListData _item;
        _item = new ScrapListData();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpTotalNum;
        _tmpTotalNum = _cursor.getInt(_cursorIndexOfTotalNum);
        _item.setTotalNum(_tmpTotalNum);
        final int _tmpScraped;
        _tmpScraped = _cursor.getInt(_cursorIndexOfScraped);
        _item.setScraped(_tmpScraped);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
