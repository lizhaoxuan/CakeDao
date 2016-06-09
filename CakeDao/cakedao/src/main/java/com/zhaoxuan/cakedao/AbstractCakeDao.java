package com.zhaoxuan.cakedao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lizhaoxuan on 16/5/29.
 */
public abstract class AbstractCakeDao<T> {

    protected SQLiteDatabase db = null;

    public AbstractCakeDao(SQLiteDatabase db) {
        this.db = db;
    }

    public abstract String createTable(boolean ifNotExists);

    public abstract String dropTable(boolean ifExists);

    public abstract long insert(T value);

    public abstract long deleteAllData();

    public abstract long deleteById(T value);

    public abstract long deleteById(long id);

    public abstract long updateById(long id, T value);

    public abstract long update(T value);

    public abstract T[] loadDataById(long id);

    public abstract T[] loadAllData();
}
