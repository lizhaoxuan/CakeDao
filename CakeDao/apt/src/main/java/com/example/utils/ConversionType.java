package com.example.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lizhaoxuan on 16/6/9.
 */
public class ConversionType {

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    private static Map<String, String> dbType = new LinkedHashMap<>();

    static {
        dbType.put("java.lang.String", "TEXT");
        dbType.put("int", "INTEGER");
        dbType.put("boolean", "INTEGER");
        dbType.put("byte[]", "BLOB");
        dbType.put("byte", "INTEGER");
        dbType.put("java.util.Date", "INTEGER");
        dbType.put("double", "REAL");
        dbType.put("float", "REAL");
        dbType.put("long", "INTEGER");
    }

    public static String getDbType(String type) {
        return ConversionType.dbType.get(type);
    }

    public static String getSqlPutByType(String type, String name) {
        if ("java.util.Date".equals(type)) {
            return "value." + name + ".getTime());\n";
        } else {
            return "value." + name + ");\n";
        }
    }

    public static String getSqlPutByType(int i, String type, String name) {
        String sql;
        switch (type) {
            case "java.lang.String":
                sql = "bindString(" + i + ",value." + name + ");\n";
                break;
            case "int":
                sql = "bindLong(" + i + ",value." + name + ");\n";
                break;
            case "boolean":
                sql = "bindLong(" + i + ",value." + name + "? 1L: 0L);\n";
                break;
            case "byte":
                sql = "bindLong(" + i + ",value." + name + ");\n";
                break;
            case "byte[]":
                sql = "bindBlob(" + i + ",value." + name + ");\n";
                break;
            case "java.util.Date":
                sql = "bindLong(" + i + ",value." + name + ".getTime());\n";
                break;
            case "double":
                sql = "bindDouble(" + i + ",value." + name + ");\n";
                break;
            case "float":
                sql = "bindDouble(" + i + ",value." + name + ");\n";
                break;
            case "long":
                sql = "bindLong(" + i + ",value." + name + ");\n";
                break;
            case "short":
                sql = "bindLong(" + i + ",value." + name + ");\n";
                break;
            default:
                return "";
        }
        return sql;
    }

    public static String getSqlGetByType(String type, String dbName) {
        String sql;
        switch (type) {
            case "java.lang.String":
                sql = "cursor.getString(cursor.getColumnIndex(\"" + dbName + "\"));\n";
                break;
            case "int":
                sql = "cursor.getInt(cursor.getColumnIndex(\"" + dbName + "\"));\n";
                break;
            case "boolean":
                sql = "cursor.getShort(cursor.getColumnIndex(\"" + dbName + "\")) != 0;\n";
                break;
            case "byte":
                sql = "(byte) cursor.getShort(cursor.getColumnIndex(\"" + dbName + "\"));\n";
                break;
            case "byte[]":
                sql = "cursor.getBlob(cursor.getColumnIndex(\"" + dbName + "\"));";
                break;
            case "java.util.Date":
                sql = "new java.util.Date(cursor.getLong(cursor.getColumnIndex(\"" + dbName + "\")));\n";
                break;
            case "double":
                sql = "cursor.getDouble(cursor.getColumnIndex(\"" + dbName + "\"));\n";
                break;
            case "float":
                sql = "cursor.getFloat(cursor.getColumnIndex(\"" + dbName + "\"));\n";
                break;
            case "long":
                sql = "cursor.getLong(cursor.getColumnIndex(\"" + dbName + "\"));\n";
                break;
            case "short":
                sql = "cursor.getShort(cursor.getColumnIndex(\"" + dbName + "\"));\n";
                break;
            default:
                return "";
        }
        return sql;
    }


}
