package kr.ac.ssu.billysrecipe.RefrigerRatorDB;

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
public final class RefrigeratorDao_Impl implements RefrigeratorDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RefrigeratorData> __insertionAdapterOfRefrigeratorData;

  private final EntityDeletionOrUpdateAdapter<RefrigeratorData> __deletionAdapterOfRefrigeratorData;

  private final EntityDeletionOrUpdateAdapter<RefrigeratorData> __updateAdapterOfRefrigeratorData;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public RefrigeratorDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRefrigeratorData = new EntityInsertionAdapter<RefrigeratorData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `RefrigeratorData` (`id`,`category`,`tag`,`name`,`tagNumber`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RefrigeratorData value) {
        stmt.bindLong(1, value.getId());
        if (value.getCategory() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getCategory());
        }
        if (value.getTag() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTag());
        }
        if (value.getName() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getName());
        }
        stmt.bindLong(5, value.getTagNumber());
      }
    };
    this.__deletionAdapterOfRefrigeratorData = new EntityDeletionOrUpdateAdapter<RefrigeratorData>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `RefrigeratorData` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RefrigeratorData value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfRefrigeratorData = new EntityDeletionOrUpdateAdapter<RefrigeratorData>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `RefrigeratorData` SET `id` = ?,`category` = ?,`tag` = ?,`name` = ?,`tagNumber` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RefrigeratorData value) {
        stmt.bindLong(1, value.getId());
        if (value.getCategory() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getCategory());
        }
        if (value.getTag() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTag());
        }
        if (value.getName() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getName());
        }
        stmt.bindLong(5, value.getTagNumber());
        stmt.bindLong(6, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM refrigeratordata";
        return _query;
      }
    };
  }

  @Override
  public void insert(final RefrigeratorData data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRefrigeratorData.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final RefrigeratorData data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfRefrigeratorData.handle(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final RefrigeratorData dafa) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfRefrigeratorData.handle(dafa);
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
  public List<RefrigeratorData> getAll() {
    final String _sql = "SELECT * FROM RefrigeratorData";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfTag = CursorUtil.getColumnIndexOrThrow(_cursor, "tag");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfTagNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tagNumber");
      final List<RefrigeratorData> _result = new ArrayList<RefrigeratorData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final RefrigeratorData _item;
        _item = new RefrigeratorData();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpCategory;
        _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        _item.setCategory(_tmpCategory);
        final String _tmpTag;
        _tmpTag = _cursor.getString(_cursorIndexOfTag);
        _item.setTag(_tmpTag);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item.setName(_tmpName);
        final int _tmpTagNumber;
        _tmpTagNumber = _cursor.getInt(_cursorIndexOfTagNumber);
        _item.setTagNumber(_tmpTagNumber);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public RefrigeratorData findData(final String target) {
    final String _sql = "SELECT * FROM refrigeratordata WHERE name = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (target == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, target);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfTag = CursorUtil.getColumnIndexOrThrow(_cursor, "tag");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfTagNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tagNumber");
      final RefrigeratorData _result;
      if(_cursor.moveToFirst()) {
        _result = new RefrigeratorData();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpCategory;
        _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        _result.setCategory(_tmpCategory);
        final String _tmpTag;
        _tmpTag = _cursor.getString(_cursorIndexOfTag);
        _result.setTag(_tmpTag);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _result.setName(_tmpName);
        final int _tmpTagNumber;
        _tmpTagNumber = _cursor.getInt(_cursorIndexOfTagNumber);
        _result.setTagNumber(_tmpTagNumber);
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
  public List<RefrigeratorData> sortData() {
    final String _sql = "SELECT * FROM refrigeratordata ORDER BY category ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfTag = CursorUtil.getColumnIndexOrThrow(_cursor, "tag");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfTagNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tagNumber");
      final List<RefrigeratorData> _result = new ArrayList<RefrigeratorData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final RefrigeratorData _item;
        _item = new RefrigeratorData();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpCategory;
        _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        _item.setCategory(_tmpCategory);
        final String _tmpTag;
        _tmpTag = _cursor.getString(_cursorIndexOfTag);
        _item.setTag(_tmpTag);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item.setName(_tmpName);
        final int _tmpTagNumber;
        _tmpTagNumber = _cursor.getInt(_cursorIndexOfTagNumber);
        _item.setTagNumber(_tmpTagNumber);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
