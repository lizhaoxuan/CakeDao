package com.zhaoxuan.cakedao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by lizhaoxuan on 16/5/29.
 */
public class CakeDao {
    private static final String TAG = "CakeDao";

    private static SQLiteDatabase db;

    public AbstractClassMap classMap;

    public static CakeDao instance;

    private CakeDao(Context context, String dbName, int version, boolean tableIfNotExists) {
        try {
            Class<?> clazz = Class.forName("com.zhaoxuan.cakedao.ClassMap");
            classMap = (AbstractClassMap) clazz.newInstance();
            DatabaseHelper database = new DatabaseHelper(context, dbName, version,
                    classMap, tableIfNotExists);
            db = database.getWritableDatabase();
            classMap.setDb(db);
        } catch (Exception e) {
            Log.e("CakeDao", "AbstractClassMap not fount", e);
        }
    }

    public static CakeDao init(Context context, String dbName, int version) {
        return init(context, dbName, version, false);
    }

    public static CakeDao init(Context context, String dbName, int version, boolean ifTableIfNotExists) {
        instance = new CakeDao(context, dbName, version, ifTableIfNotExists);
        return instance;
    }

    private static <T> void check(Class<T> clazz) {
        if (instance == null) {
            throw new Error("CakeDao mast be init");
        }
        if (instance.classMap.getAbstractCakeDao(clazz) == null){
            throw new Error("Not found "+ clazz.toString() + " Table");
        }
    }

    public static <T> AbstractCakeDao<T> getCakeDao(Class<T> clazz){
        check(clazz);
        return instance.classMap.getAbstractCakeDao(clazz);
    }

    public static <T> long insert(Class<T> clazz, T value) {
        check(clazz);
        return instance.classMap.getAbstractCakeDao(clazz).insert(value);
    }

    public static <T> long deleteAllData(Class<T> clazz) {
        check(clazz);
        return instance.classMap.getAbstractCakeDao(clazz).deleteAllData();
    }

    public static <T> long deleteById(Class<T> clazz, T value) {
        check(clazz);
        return instance.classMap.getAbstractCakeDao(clazz).deleteById(value);
    }

    public static <T> long deleteById(Class<T> clazz, long id) {
        check(clazz);
        return instance.classMap.getAbstractCakeDao(clazz).deleteById(id);
    }

    public static <T> long updateById(Class<T> clazz, long id, T value) {
        check(clazz);
        return instance.classMap.getAbstractCakeDao(clazz).updateById(id, value);
    }

    public static <T> long update(Class<T> clazz, T value) {
        check(clazz);
        return instance.classMap.getAbstractCakeDao(clazz).update(value);
    }

    public static <T> T[] loadDataById(Class<T> clazz, long id) {
        check(clazz);
        return instance.classMap.getAbstractCakeDao(clazz).loadDataById(id);
    }

    public static <T> T[] loadAllData(Class<T> clazz) {
        check(clazz);
        return instance.classMap.getAbstractCakeDao(clazz).loadAllData();
    }


}
