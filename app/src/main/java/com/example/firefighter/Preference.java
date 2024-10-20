package com.example.firefighter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Preference {
    Context context;
    private SharedPreferences sp;
    public String clientId,baseURL;
    public boolean  flagBox,checkMusic,photoBox;

    public Preference(Context context) {
        this.context = context;
    }

    public void savePreference() {
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("clientId",clientId);
        ed.putString("baseURL",baseURL);
        ed.putBoolean("checkMusic",checkMusic);
        ed.putBoolean("flagBox", flagBox);
        ed.putBoolean("photoBox",photoBox);
        ed.commit();
    }

    public void loadPreference() {
        sp = context.getSharedPreferences("ConfigPreference", context.MODE_PRIVATE);
        checkMusic=sp.getBoolean("checkMusic",false);
        baseURL=sp.getString("baseURL","https://dry-beach-13530.herokuapp.com/");
        clientId=sp.getString("clientId","");
        flagBox = sp.getBoolean("flagBox", false);
        photoBox=sp.getBoolean("photoBox",true);
    }
}
