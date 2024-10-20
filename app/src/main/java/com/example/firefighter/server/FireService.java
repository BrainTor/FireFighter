package com.example.firefighter.server;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface FireService {
    @POST("/fire/get")
    Call<ArrayList<Fire>> getFire(@Query("hashName") String hashName, @Query("timeFrom")Date timeFrom);
}
