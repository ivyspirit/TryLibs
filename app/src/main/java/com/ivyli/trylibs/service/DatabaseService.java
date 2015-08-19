package com.ivyli.trylibs.service;


import com.ivyli.trylibs.service.json.GalleryJson;
import com.ivyli.trylibs.service.json.ImageJson;

import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;

public class DatabaseService{

    public static final String TAG = DatabaseService.class.getSimpleName();
    private final RetrofitApiService mApiService;

    @Inject
    public DatabaseService(RetrofitApiService galleryService){
        this.mApiService = galleryService;
    }

    public Observable<GalleryJson> loadImages(final String section, final String sort){

        return Observable.create(new Observable.OnSubscribe<GalleryJson>(){
                                     @Override
                                     public void call(final Subscriber<? super GalleryJson> subscriber){
                                         try{
                                             GalleryJson next = new GalleryJson();
                                             // check the local db first. if we have data, avoid
                                             // calling the imagur api.
                                             List<ImageJson> data = next.getImages();
                                             if(null == data || data.size() <= 0){
                                                 next = mApiService.getImages(section, sort);
                                                 data = next.data;
                                                 for(ImageJson imageJson : data){
                                                     if(!imageJson.is_album){
                                                         imageJson.save();
                                                     }
                                                 }
                                             }

                                             if(!subscriber.isUnsubscribed()){
                                                 subscriber.onNext(next);
                                             }
                                         }catch(RetrofitError e){
                                             if(!subscriber.isUnsubscribed()){
                                                 subscriber.onError(e);
                                             }
                                         }

                                     }
                                 }
        );

    }
}
