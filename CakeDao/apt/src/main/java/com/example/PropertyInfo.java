package com.example;

import com.example.utils.CamelCaseUtils;
import com.example.utils.ConversionType;

/**
 * Created by lizhaoxuan on 16/5/29.
 */
public class PropertyInfo {

    private String type;
    private String name;
    private String nameByDB;
    private String typeByDB;
    private boolean isPrimary;

    PropertyInfo(String name, String type, boolean isPrimary) throws CakeDaoException {
        this.name = name;
        this.type = type;
        this.isPrimary = isPrimary;
        if (!convertType(this.type, isPrimary)) {
            if (isPrimary) {
                throw new CakeDaoException("Primary key type must be long :" + type);
            }
            throw new CakeDaoException("CakeDao does not support this type: " + type);
        }
    }

    String getNameByDB() {
        if (nameByDB == null || nameByDB.equals("")) {
            if (isPrimary) {
                nameByDB = "CAKEDAO_ID";
            } else {
                nameByDB = CamelCaseUtils.toUnderlineName(name);
            }
        }
        return nameByDB;
    }

    String getName() {
        return name;
    }

    String getTypeByDB() {
        return typeByDB;
    }

    /**
     * 转换为数据库需要类型
     * @return true 合法 false 不合法
     */
    private boolean convertType(String type, boolean isPrimary) {
        if (isPrimary) {
            return type.equals("long");
        }
        typeByDB = ConversionType.getDbType(type);
        return type != null;
    }

    public String getSqlPutByType() {
        return ConversionType.getSqlPutByType(type, name);
    }

    public String getSqlPutByType(int i) {
        return ConversionType.getSqlPutByType(i, type, name);
    }

    public String getSqlGetByType() {
        return ConversionType.getSqlGetByType(type, getNameByDB());
    }
}
