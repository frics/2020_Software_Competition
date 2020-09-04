package kr.ac.ssu.billysrecipe.ShoppingListDB;

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
public final class ShoppingListDao_Impl implements ShoppingListDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ShoppingListData> __insertionAdapterOfShoppingListData;

  private final EntityDeletionOrUpdateAdapter<ShoppingListData> __deletionAdapterOfShoppingListData;

  private final EntityDeletionOrUpdateAdapter<ShoppingListData> __updateAdapterOfShoppingListData;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public ShoppingListDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfShoppingListData = new EntityInsertionAdapter<ShoppingListData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `ShoppingListData` (`id`,`tag`,`name`,`tagNumber`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ShoppingListData value) {
        stmt.bindLong(1, value.getId());
        if (value.getTag() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTag());
        }
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        stmt.bindLong(4, value.getTagNumber());
      }
    };
    this.__deletionAdapterOfShoppingListData = new EntityDeletionOrUpdateAdapter<ShoppingListData>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `ShoppingListData` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ShoppingListData value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfShoppingListData = new EntityDeletionOrUpdateAdapter<ShoppingListData>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `ShoppingListData` SET `id` = ?,`tag` = ?,`name` = ?,`tagNumber` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ShoppingListData value) {
        stmt.bindLong(1, value.getId());
        if (value.getTag() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTag());
        }
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        stmt.bindLong(4, value.getTagNumber());
        stmt.bindLong(5, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM ShoppingListData";
        return _query;
      }
    };
  }

  @Override
  public void insert(final ShoppingListData data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfShoppingListData.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final ShoppingListData data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfShoppingListData.handle(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final ShoppingListData dafa) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfShoppingListData.handle(dafa);
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
  public List<ShoppingListData> getAll() {
    final String _sql = "SELECT * FROM ShoppingListData";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTag = CursorUtil.getColumnIndexOrThrow(_cursor, "tag");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfTagNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tagNumber");
      final List<ShoppingListData> _result = new ArrayList<ShoppingListData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ShoppingListData _item;
        _item = new ShoppingListData();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
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
  public ShoppingListData findData(final String target) {
    final String _sql = "SELECT * FROM ShoppingListData WHERE name = ?";
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
      final int _cursorIndexOfTag = CursorUtil.getColumnIndexOrThrow(_cursor, "tag");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfTagNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tagNumber");
      final ShoppingListData _result;
      if(_cursor.moveToFirst()) {
        _result = new ShoppingListData();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
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
  public List<ShoppingListData> sortData() {
    final String _sql = "SELECT * FROM ShoppingListData ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTag = CursorUtil.getColumnIndexOrThrow(_cursor, "tag");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfTagNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tagNumber");
      final List<ShoppingListData> _result = new ArrayList<ShoppingListData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ShoppingListData _item;
        _item = new ShoppingListData();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
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
