package com.example;

import com.example.utils.CamelCaseUtils;

/**
 * Created by lizhaoxuan on 16/5/29.
 */
public class AttributeInfo {

    private String type;
    private String name;
    private String nameByDB;
    private String typeByDB;
    private boolean isPrimary;
    private CType cType;

    AttributeInfo(String name, String type, boolean isPrimary) throws CakeDaoException {
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
     *
     * @return true 合法 false 不合法
     */
    private boolean convertType(String type, boolean isPrimary) {
        if (isPrimary && !type.equals("long")) {
            return false;
        }
        switch (type) {
            case "java.lang.String":
                cType = CType.STRING;
                break;
            case "int":
                cType = CType.INT;
                break;
            case "boolean":
                cType = CType.BOOLEAN;
                break;
            case "byte":
                cType = CType.BYTE;
                break;
            case "byte[]":
                cType = CType.BYTES;
                break;
            case "java.util.Date":
                cType = CType.DATE;
                break;
            case "double":
                cType = CType.DOUBLE;
                break;
            case "float":
                cType = CType.FLOAT;
                break;
            case "long":
                cType = CType.LONG;
                break;
            case "short":
                cType = CType.SHORT;
                break;
            default:
                return false;
        }
        return true;
    }

    public String getSqlPut() {
        return cType.getSqlPut(name);
    }

    public String getSqlBind(int i) {
        return cType.getSqlBind(i, name);
    }

    public String getSqlGet() {
        return cType.getSqlGet(nameByDB);
    }


}
