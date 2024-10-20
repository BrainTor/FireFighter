package com.example.firefighter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Map;

public class LogActivity extends AppCompatActivity {
    TextView AllConfig;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        AllConfig=(TextView)findViewById(R.id.allConging);
        loadConfig();
    }
    void loadConfig() {
        sp = getSharedPreferences("ConfigPreference", MODE_PRIVATE);
        Map<String,?> keys = sp.getAll();
        String str="";

        for(Map.Entry<String,?> entry : keys.entrySet()){
            str+=entry.getKey() + ": " + entry.getValue().toString()+"\n";
        }
        AllConfig.setText(str);
    }
}
