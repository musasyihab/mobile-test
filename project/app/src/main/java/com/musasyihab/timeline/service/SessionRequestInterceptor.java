package com.musasyihab.timeline.service;

import retrofit.RequestInterceptor;

public class SessionRequestInterceptor implements RequestInterceptor {
    private static final String TAG = SessionRequestInterceptor.class.getSimpleName();

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("Content-Type", "application/json");//you can add header here if you need in your api
    }
}
