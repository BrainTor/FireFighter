package com.example.firefighter.server;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.example.firefighter.ActivityToViewPhoto;
import com.example.firefighter.MainMapActivity;
import com.example.firefighter.Preference;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Connect {
    public LatLng location;
    Coordinates coordinates;
    Fire fire;
    public byte[] photo;
    private static Date lastDate;
    public int fireRate;
    Preference preference;
    ArrayList<Coordinates> answerCoordinate;
    ArrayList<Fire> answerFire;
    Context context;
    CoordinateService serviceCoordinate;
    FireService serviceFire;
    private Thread fromMainActivity;

    // Using Android's base64 libraries. This can be replaced with any base64 library.
    private  class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Base64.decode(json.getAsString(), Base64.DEFAULT);
        }
@Override
        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64.encodeToString(src, Base64.DEFAULT));
        }
    }

    public Connect(Context context) {
        this.context = context;
        if(lastDate==null){
            lastDate=new Date(System.currentTimeMillis()-3600000);//Время на час назад 60*60*1000
        }
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeHierarchyAdapter(byte[].class,new ByteArrayToBase64TypeAdapter()).create();
        Gson gson = builder.create();
        preference = new Preference(context);
        preference.loadPreference();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(preference.baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        serviceCoordinate = retrofit.create(CoordinateService.class);
        serviceFire = retrofit.create(FireService.class);
    }

    public void getCoordinate() {
        CoordinateThread coordinateThread = new CoordinateThread();
        coordinateThread.execute();
        Log.d("firefighter", "get coordinates from server");
    }

    public void getFire() {
        FireThread fireAsyncTask = new FireThread();
        fireAsyncTask.execute();
        Log.d("firefighter", "get coordinates from server");
    }

    public class CoordinateThread extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            location = null;
            preference = new Preference(context);
            preference.loadPreference();
            Call<ArrayList<Coordinates>> call = serviceCoordinate.getCoordinate(preference.clientId,lastDate);
            lastDate=new Date();
            try {
                Response<ArrayList<Coordinates>> userResponse = call.execute();
                answerCoordinate = userResponse.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (answerCoordinate != null && answerCoordinate.size() > 0) {
                location = new LatLng(answerCoordinate.get(answerCoordinate.size() - 1).getLatitude(),
                        answerCoordinate.get(answerCoordinate.size() - 1).getLongtitude());
                ((MainMapActivity) context).call();
            }
        }
    }


    public class FireThread extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            location = null;
            preference = new Preference(context);
            preference.loadPreference();
            Call<ArrayList<Fire>> call = serviceFire.getFire(preference.clientId, lastDate);
            lastDate = new Date();
            try {
                Response<ArrayList<Fire>> userResponse = call.execute();
                answerFire = userResponse.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (answerFire != null && answerFire.size() > 0) {
                fireRate = answerFire.get(answerFire.size() - 1).getFireRate();
                location = new LatLng(answerFire.get(answerFire.size() - 1).getLatitude(), answerFire.get(answerFire.size() - 1).getLongtitude());
                byte[]  photoNew = answerFire.get(answerFire.size() - 1).getPhoto();
                if (photoNew != null && photoNew.length != 0) {
                    File pictureFileDir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                    String date = dateFormat.format(new Date());
                    String photoFile = "Firefighter" + "_" + date + ".jpg";
                    String filename = pictureFileDir.getPath() + File.separator + photoFile;

                    try {
                        FileOutputStream fos = new FileOutputStream(filename);
                        fos.write(photoNew);
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ((MainMapActivity) context).call();
                    photo=photoNew;
                }
            }


        }
    }
}