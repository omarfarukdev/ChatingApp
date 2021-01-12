package com.example.chatingapp.Fragments;

import com.example.chatingapp.Notification.MyResponse;
import com.example.chatingapp.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAANy_x0L4:APA91bFKmbC5pNGYZfcf4sfTJQ4btlun3zlirifPIqtfYEqGlnsE-14RIkdv0OEhepq8Ud8nE7GT6bY7PDDVTEWsBiM_fwMwQMpRE8j8ymXX7P9FMJnUKh355yoY9Z_TUUcwsLvKAH0x"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
