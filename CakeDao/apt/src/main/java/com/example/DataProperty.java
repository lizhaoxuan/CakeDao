package com.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表名需要本地持久化的类成员。
 * 仅支持boolean byteArray byte double float long int
 * 及Date类型。
 * Created by lizhaoxuan on 16/5/29.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface DataProperty {

}
