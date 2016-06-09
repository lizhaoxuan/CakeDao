package com.zhaoxuan.cakedao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Collection;

/**
 * Created by lizhaoxuan on 16/5/29.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Collection<AbstractCakeDao> cakeDaos;
    private AbstractClassMap abstractClassMap;
    private boolean tableIfNotExists;

    public DatabaseHelper(Context context, String dbName, int version, AbstractClassMap abstractClassMap, boolean tableIfNotExists) {
        super(context, dbName, null, version);
        // TODO Auto-generated constructor stub
        this.abstractClassMap = abstractClassMap;
        this.tableIfNotExists = tableIfNotExists;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (abstractClassMap == null){
            return;
        }
        abstractClassMap.setDb(db);
        cakeDaos = abstractClassMap.getMap().values();
        for (AbstractCakeDao cakeDao : cakeDaos) {
            db.execSQL(cakeDao.createTable(tableIfNotExists));
        }
    }

    /**
     * 当 VERSION 变化时调用此方法
     * 数据库的旧数据非保存 更新策略：每增加或修改一个表，必须在 onUpgrade 方法中增加该表的删除操作，
     * 避免同名表的重复创建。
     * db.execSQL("DROP TABLE IF EXISTS 新增表名"); // 如果存在，删除该表。
     * 为兼容旧版本，onUpgrade 方法内应存在过去所有历史表的删除操作。（无需考虑oldVersion）
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        /**如果存在，删除该表**/
        if (cakeDaos == null){
            return;
        }
        for (AbstractCakeDao cakeDao : cakeDaos) {
            db.execSQL(cakeDao.dropTable(tableIfNotExists));
        }
        /**创建新表**/
        onCreate(db);
    }
}
