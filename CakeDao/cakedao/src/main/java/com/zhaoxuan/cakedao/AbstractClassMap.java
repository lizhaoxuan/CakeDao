package com.zhaoxuan.cakedao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

/**
 * Created by lizhaoxuan on 16/5/30.
 */
public abstract class AbstractClassMap {

    protected SQLiteDatabase db;

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public abstract <T> AbstractCakeDao<T> getAbstractCakeDao(Class<T> clazz);

    public abstract Map<Class, AbstractCakeDao> getMap();

}
