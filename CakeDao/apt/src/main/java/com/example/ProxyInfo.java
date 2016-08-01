package com.example;

import com.example.utils.CamelCaseUtils;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;

/**
 * Created by lizhaoxuan on 16/5/29.
 */
public class ProxyInfo {

    private String packageName;
    private String targetClassName;
    private String fullClassName;
    private String proxyClassName;

    private TypeElement typeElement;
    private AttributeInfo idProperty;
    private List<AttributeInfo> propertyInfos;

    public static final String PROXY = "CAKEDAO";

    ProxyInfo(String packageName, String className) {
        this.packageName = packageName;
        this.targetClassName = className;
        this.fullClassName = packageName + "." + targetClassName;
        this.proxyClassName = className + "$$" + PROXY;
    }

    String getProxyClassFullName() {
        return packageName + "." + proxyClassName;
    }

    String getTargetClassName() {
        return targetClassName.replace("$", ".");
    }

    String getFullClassName() {
        return fullClassName;
    }

    private String getTableName() {
        return CamelCaseUtils.toUnderlineName(getTargetClassName());
    }

    void setTypeElement(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    TypeElement getTypeElement() {
        return typeElement;
    }

    void addPropertyInfos(AttributeInfo info) {
        if (propertyInfos == null) {
            propertyInfos = new ArrayList<>();
        }
        propertyInfos.add(info);
    }

    void setIdProperty(AttributeInfo info) {
        idProperty = info;
    }

    String generateJavaCode() throws CakeDaoException {
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code from CakeDao. Do not modify!\n");
        builder.append("package ").append(packageName).append(";\n\n");

        builder.append("import android.content.ContentValues;\n");
        builder.append("import android.database.Cursor;\n");
        builder.append("import com.zhaoxuan.cakedao.AbstractCakeDao;\n");
        builder.append("import android.database.sqlite.SQLiteDatabase;\n");
        builder.append("import android.database.sqlite.SQLiteStatement;\n");
        builder.append('\n');

        builder.append("public class ").append(proxyClassName);
        builder.append(" extends AbstractCakeDao<").append(getTargetClassName()).append(">");
        builder.append(" {\n");

        builder.append("public ")
                .append(proxyClassName)
                .append("(SQLiteDatabase db) { \n")
                .append("super(db);\n")
                .append("}\n");
        //创建表
        generateCreateTableCode(builder);
        //删除表
        generateDropTable(builder);
        //删除所有数据
        generateDeleteAllData(builder);
        //根据Id删除数据
        generateDeleteById(builder);
        //根据Id删除数据
        generateDeleteById_(builder);
        //根据Id加载数据
        generateLoadDataById(builder);
        //加载所有数据
        generateLoadAllData(builder);
        //插入数据
        generateInsert(builder);
        //根据Id更新
        generateUpdateById(builder);
        //更新
        generateUpdate(builder);

        generateConvertToValue(builder);

        builder.append("\n}");
        return builder.toString();
    }

    private void generateCreateTableCode(StringBuilder builder) {
        builder.append("public String createTable(boolean ifNotExists) { \n")
                .append("String constraint = ifNotExists ? \"IF NOT EXISTS \" : \"\";\n")
                .append("String sql = \"CREATE TABLE \" + constraint +  \"")
                .append(getTableName())
                .append(" (\" + \n")
                .append("\"CAKEDAO_ID INTEGER PRIMARY KEY AUTOINCREMENT");
        for (AttributeInfo info : propertyInfos) {
            builder.append(",    ")
                    .append(info.getNameByDB())
                    .append(" ")
                    .append(info.getTypeByDB());
        }
        builder.append(");\"; \n");
        builder.append("return sql; \n }\n");
    }

    private void generateDropTable(StringBuilder builder) {
        builder.append("public String dropTable(boolean ifExists) { \n")
                .append("String sql = \"DROP TABLE \" + (ifExists ? \"IF EXISTS \" : \"\") + \"")
                .append(getTableName())
                .append("\";\n")
                .append("return sql; \n")
                .append("} \n");
    }

    private void generateDeleteAllData(StringBuilder builder) {
        builder.append("public long deleteAllData() { \n")
                .append("return db.delete(\"")
                .append(getTableName())
                .append("\", null, null);\n")
                .append("}\n");
    }

    private void generateDeleteById(StringBuilder builder) {
        builder.append("public long deleteById(long id) { \n")
                .append("return db.delete(\"")
                .append(getTableName())
                .append("\",")
                .append("\"CAKEDAO_ID = \" + id, null);\n")
                .append("}\n");
    }

    private void generateDeleteById_(StringBuilder builder) {
        builder.append("public long deleteById(")
                .append(getTargetClassName())
                .append(" value){ \n")
                .append("return db.delete(\"")
                .append(getTableName())
                .append("\",")
                .append("\"CAKEDAO_ID = \" + value.")
                .append(idProperty.getName())
                .append(", null);\n")
                .append("}\n");
    }

    private void generateLoadDataById(StringBuilder builder) {
        builder.append("public ")
                .append(getTargetClassName())
                .append("[] loadDataById(long id) {\n")
                .append("Cursor results = db.query(\"")
                .append(getTableName())
                .append("\", null,")
                .append("\"CAKEDAO_ID = ?\", new String[]{String.valueOf(id)}, null, null, null,null);\n")
                .append("return convertToValue(results); \n } \n");
    }

    private void generateLoadAllData(StringBuilder builder) {
        builder.append("public ")
                .append(getTargetClassName())
                .append("[] loadAllData() { \n")
                .append("Cursor results = db.query(\"")
                .append(getTableName())
                .append("\", null,")
                .append("null, null, null, null, null);\n")
                .append("return convertToValue(results); \n } \n");
    }

    private void generateInsert(StringBuilder builder) {
        builder.append("public long insert(")
                .append(getTargetClassName())
                .append(" value){ \n")
                .append("String sql = \"INSERT INTO ")
                .append(getTableName())
                .append("(");
        for (AttributeInfo info : propertyInfos) {
            builder.append(info.getNameByDB())
                    .append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(") VALUES(");
        for (int i = 1, l = propertyInfos.size(); i <= l; i++) {
            builder.append("?,");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(")\";\n");
        builder.append("SQLiteStatement statement=db.compileStatement(sql);");
        for (int i = 1, l = propertyInfos.size(); i <= l; i++) {
            builder.append("statement.")
                    .append(propertyInfos.get(i - 1).getSqlBind(i));
        }

        builder.append("return statement.executeInsert();\n }\n");
    }

    private void generateUpdateById(StringBuilder builder) {
        builder.append(" public long updateById(long id,")
                .append(getTargetClassName())
                .append(" value) { \n")
                .append("ContentValues contentValues = new ContentValues();\n");

        for (AttributeInfo info : propertyInfos) {
            builder.append("contentValues.put(\"")
                    .append(info.getNameByDB())
                    .append("\",")
                    .append(info.getSqlPut());
        }
        builder.append("return db.update(\"")
                .append(getTableName())
                .append("\", contentValues, \"CAKEDAO_ID = \" + id, null); \n } \n");
    }

    private void generateUpdate(StringBuilder builder) {
        builder.append(" public long update(")
                .append(getTargetClassName())
                .append(" value) { \n")
                .append("ContentValues contentValues = new ContentValues();\n");
        for (AttributeInfo info : propertyInfos) {
            builder.append("contentValues.put(\"")
                    .append(info.getNameByDB())
                    .append("\",")
                    .append(info.getSqlPut());
        }
        builder.append("return db.update(\"")
                .append(getTableName())
                .append("\", contentValues, \"CAKEDAO_ID = \" +value.")
                .append(idProperty.getName())
                .append(", null); \n } \n");
    }

    private void generateConvertToValue(StringBuilder builder) {
        builder.append("private ")
                .append(getTargetClassName())
                .append("[] convertToValue(Cursor cursor) {")
                .append("int count = cursor.getCount();\n")
                .append("if (count == 0 || !cursor.moveToFirst()) { \n")
                .append("return null;\n } \n")
                .append(getTargetClassName())
                .append("[]values = new ").append(getTargetClassName()).append("[count];\n")
                .append("for (int i = 0; i < count; i++) {\n")
                .append("values[i] = new ").append(getTargetClassName()).append("();\n");
        builder.append("values[i].").append(idProperty.getName())
                .append("= cursor.getInt(cursor.getColumnIndex(\"CAKEDAO_ID\"));");
        for (AttributeInfo info : propertyInfos) {
            builder.append("values[i].").append(info.getName())
                    .append("= ")
                    .append(info.getSqlGet());
        }
        builder.append("cursor.moveToNext();\n }")
                .append("cursor.close(); \n")
                .append("return values; \n } \n");
    }


}
