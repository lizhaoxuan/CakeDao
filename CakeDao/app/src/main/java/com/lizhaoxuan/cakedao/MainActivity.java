package com.lizhaoxuan.cakedao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.GetMsg;

@GetMsg(id = 2, name = "MAIN")
public class MainActivity extends AppCompatActivity {

    @GetMsg(id = 2, name = "method")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
