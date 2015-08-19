package com.ivyli.trylibs.service;


import com.ivyli.trylibs.service.json.GalleryJson;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface RetrofitApiService{

    @GET("/gallery/{section}/{sort}/1")
    GalleryJson getImages(@Path("section") String term,
                          @Path("sort") String sort);

    @GET("/gallery/{section}/{sort}/1")
    Observable<GalleryJson> getObservableImages(@Path("section") String term,
                          @Path("sort") String sort);

}
