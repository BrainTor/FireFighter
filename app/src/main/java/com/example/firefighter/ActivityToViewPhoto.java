package com.example.firefighter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firefighter.server.Fire;

public class ActivityToViewPhoto extends AppCompatActivity {
ImageView photo;
    byte[]photoArray;
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_to_view_photo);
        getPhoto();
    }
public boolean getPhoto(){
    photo=(ImageView)findViewById(R.id.firePhoto);
    Bundle extras = getIntent().getExtras();
    byte[] byteArray = extras.getByteArray("photo");
    Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    if(bmp!=null){
        photo.setImageBitmap(bmp);
        return true;
    }
    Toast.makeText(ActivityToViewPhoto.this, "Фото не получено с сервера", Toast.LENGTH_LONG).show();
   return false;
}
    public void bachToMiniMap(View view) {
        Intent i=new Intent(this,MainMapActivity.class);
        startActivity(i);
    }
}
