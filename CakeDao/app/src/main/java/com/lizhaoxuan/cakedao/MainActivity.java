package com.lizhaoxuan.cakedao;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.LoginFilter;
import android.util.Log;


import com.zhaoxuan.cakedao.AbstractCakeDao;
import com.zhaoxuan.cakedao.CakeDao;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String DB_NAME = "my_db"; //数据库名称
    private static final int version = 2; //数据库版本

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CakeDao.init(getApplicationContext(), DB_NAME, version, true);

        CakeDao.insert(Student.class, new Student("one"));
        CakeDao.insert(Student.class, new Student("two"));
        CakeDao.insert(Student.class, new Student("three"));
        CakeDao.insert(Student.class, new Student("four"));

        CakeDao.deleteById(Student.class, 1);
        Log.d("TAG","deleteById(Student.class, 1)");

        Student[] students = CakeDao.loadAllData(Student.class);
        if (students != null) {
            for (Student student : students) {
                Log.d("TAG", "ergodic：id = " + student.id + "  name = " + student.name);
            }
        }

        AbstractCakeDao<Student> studentDao = CakeDao.getCakeDao(Student.class);
        studentDao.updateById(4, new Student("4444"));
        Log.d("TAG","updateById(4, new Student(\"4444\"))");
        Student student = studentDao.loadDataById(4)[0];
        Log.d("TAG", "loadDataById(4)   id:" + student.id + "  name:" + student.name);
    }
}
