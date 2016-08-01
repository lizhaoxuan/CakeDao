package com.example;

/**
 * Created by lizhaoxuan on 16/7/1.
 */
public enum CType {

    STRING {
        @Override
        public String getDbType() {
            return "TEXT";
        }

        @Override
        public String getSqlBind(int i, String name) {
            return "bindString(" + i + ",value." + name + ");\n";
        }

        @Override
        public String getSqlGet(String dbName) {
            return "cursor.getString(cursor.getColumnIndex(\"" + dbName + "\"));\n";
        }
    },
    INT {
        @Override
        public String getDbType() {
            return "INTEGER";
        }

        @Override
        public String getSqlBind(int i, String name) {
            return "bindLong(" + i + ",value." + name + ");\n";
        }

        @Override
        public String getSqlGet(String dbName) {
            return "cursor.getInt(cursor.getColumnIndex(\"" + dbName + "\"));\n";
        }
    },
    BOOLEAN {
        @Override
        public String getDbType() {
            return "INTEGER";
        }

        @Override
        public String getSqlBind(int i, String name) {
            return "bindLong(" + i + ",value." + name + "? 1L: 0L);\n";
        }

        @Override
        public String getSqlGet(String dbName) {
            return "cursor.getShort(cursor.getColumnIndex(\"" + dbName + "\")) != 0;\n";
        }
    },
    BYTES {
        @Override
        public String getDbType() {
            return "BLOB";
        }

        @Override
        public String getSqlBind(int i, String name) {
            return "bindBlob(" + i + ",value." + name + ");\n";
        }

        @Override
        public String getSqlGet(String dbName) {
            return "cursor.getBlob(cursor.getColumnIndex(\"" + dbName + "\"));";
        }
    },
    BYTE {
        @Override
        public String getDbType() {
            return "INTEGER";
        }

        @Override
        public String getSqlBind(int i, String name) {
            return "bindLong(" + i + ",value." + name + ");\n";
        }

        @Override
        public String getSqlGet(String dbName) {
            return "(byte) cursor.getShort(cursor.getColumnIndex(\"" + dbName + "\"));\n";
        }
    },
    DATE {
        @Override
        public String getDbType() {
            return "INTEGER";
        }

        @Override
        public String getSqlBind(int i, String name) {
            return "bindLong(" + i + ",value." + name + ".getTime());\n";
        }

        @Override
        public String getSqlGet(String dbName) {
            return "new java.util.Date(cursor.getLong(cursor.getColumnIndex(\"" + dbName + "\")));\n";
        }

        @Override
        public String getSqlPut(String name) {
            return "value." + name + ".getTime());\n";
        }
    },
    DOUBLE {
        @Override
        public String getDbType() {
            return "REAL";
        }

        @Override
        public String getSqlBind(int i, String name) {
            return "bindDouble(" + i + ",value." + name + ");\n";
        }

        @Override
        public String getSqlGet(String dbName) {
            return "cursor.getDouble(cursor.getColumnIndex(\"" + dbName + "\"));\n";
        }
    },
    FLOAT {
        @Override
        public String getDbType() {
            return "REAL";
        }

        @Override
        public String getSqlBind(int i, String name) {
            return "bindDouble(" + i + ",value." + name + ");\n";
        }

        @Override
        public String getSqlGet(String dbName) {
            return "cursor.getFloat(cursor.getColumnIndex(\"" + dbName + "\"));\n";
        }
    },
    LONG {
        @Override
        public String getDbType() {
            return "INTEGER";
        }

        @Override
        public String getSqlBind(int i, String name) {
            return "bindLong(" + i + ",value." + name + ");\n";
        }

        @Override
        public String getSqlGet(String dbName) {
            return "cursor.getLong(cursor.getColumnIndex(\"" + dbName + "\"));\n";
        }
    },
    SHORT {
        @Override
        public String getDbType() {
            return "INTEGER";
        }

        @Override
        public String getSqlBind(int i, String name) {
            return "bindLong(" + i + ",value." + name + ");\n";
        }

        @Override
        public String getSqlGet(String dbName) {
            return "cursor.getShort(cursor.getColumnIndex(\"" + dbName + "\"));\n";
        }
    };


    public abstract String getDbType();

    public abstract String getSqlBind(int i, String name);

    public String getSqlPut(String name){
        return "value." + name + ");\n";
    }

    public abstract String getSqlGet(String dbName);
}
