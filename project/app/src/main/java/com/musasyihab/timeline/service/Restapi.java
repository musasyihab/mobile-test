package com.musasyihab.timeline.service;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;


public interface Restapi {
    @GET("/timelines")
    void getTimeline(RestCallback<ResponseData> callback);
}