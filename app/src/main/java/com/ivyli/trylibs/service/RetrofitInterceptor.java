package com.ivyli.trylibs.service;


import com.ivyli.trylibs.IvyApplication;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.RequestInterceptor;

@Singleton
public class RetrofitInterceptor implements RequestInterceptor{

    public RetrofitInterceptor(IvyApplication application){}
    private static final String AUTHORIZATION_PREFIX = "Client-ID";

    private  String authorizationValue;

    @Inject
    public RetrofitInterceptor(@ClientId String clientId) {
        authorizationValue = AUTHORIZATION_PREFIX + " " + clientId;
    }
    @Override
    public void intercept(RequestFacade request){
        request.addHeader("Authorization", authorizationValue);

    }


}
