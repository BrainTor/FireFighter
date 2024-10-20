package com.example.firefighter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ConfigActivity extends AppCompatActivity {
    CheckBox flagBox;
    CheckBox checkMusic;
    CheckBox photoBox;
    EditText clientId;
    EditText baseURL;
   Preference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        preference=new Preference(this);
        checkMusic=(CheckBox)findViewById(R.id.checkMusic);
        flagBox=(CheckBox)findViewById(R.id.flagBox) ;
        clientId =(EditText)findViewById(R.id.clientId);
        baseURL=(EditText)findViewById(R.id.baseURL);
        photoBox=(CheckBox)findViewById(R.id.photoBox);
        loadConfig();
    }


    void loadConfig() {
      preference.loadPreference();
        photoBox.setChecked(preference.photoBox);
        checkMusic.setChecked(preference.checkMusic);
        clientId.setText(preference.clientId);
        baseURL.setText(preference.baseURL);
        flagBox.setChecked(preference.flagBox);
        Toast.makeText(ConfigActivity.this, "Настройки Загружены", Toast.LENGTH_SHORT).show();
    }
    public void goSave(View view) {
        saveConfig();
    }
    void saveConfig() {
        preference.checkMusic=checkMusic.isChecked();
        preference.clientId= clientId.getText().toString();
        preference.baseURL=baseURL.getText().toString();
        preference.flagBox=flagBox.isChecked();
        preference.photoBox=photoBox.isChecked();
        preference.savePreference();
        Toast.makeText(this, getResources().getText(R.string.config_saved), Toast.LENGTH_SHORT).show();
    }

    public void optionActivity(View view) {
        Intent i=new Intent (this,LogActivity.class);
        startActivity(i);

    }


}
