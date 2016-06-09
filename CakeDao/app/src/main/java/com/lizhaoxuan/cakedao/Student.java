package com.lizhaoxuan.cakedao;

import com.example.DataProperty;
import com.example.IdProperty;

/**
 * Created by lizhaoxuan on 16/5/30.
 */
public class Student {

    @IdProperty
    public long id;
    @DataProperty
    public String name;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }
}
