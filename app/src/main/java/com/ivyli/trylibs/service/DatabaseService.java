package com.ivyli.trylibs.service;


import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.ivyli.trylibs.service.json.GalleryJson;
import com.ivyli.trylibs.service.json.ImageJson;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DatabaseService{

    public static final String TAG = DatabaseService.class.getSimpleName();
    private final RetrofitApiService mApiService;

    @Inject
    public DatabaseService(RetrofitApiService galleryService){
        this.mApiService = galleryService;
    }

    GalleryJson galleryJson = new GalleryJson();

    public Observable<GalleryJson> load(final String section, final String sort){
        return Observable.create(new Observable.OnSubscribe<GalleryJson>(){
            @Override
            public void call(final Subscriber<? super GalleryJson> subscriber){

                if(galleryJson.getImages().size() <= 0){
                    Observable<GalleryJson> result = mApiService.getObservableImages(section, sort)
                            .subscribeOn(Schedulers.io());

                    result.subscribe(new Subscriber<GalleryJson>(){
                        @Override
                        public void onCompleted(){

                        }

                        @Override
                        public void onError(Throwable e){

                        }

                        @Override
                        public void onNext(GalleryJson galleryJson){
                            // I know i know this is not pretty. but I want to clear the table each time when
                            // i get new images. So I don't end up having a huge table
                            new Delete().from(ImageJson.class).execute();

                            ActiveAndroid.beginTransaction();
                            try{
                                for(int i = 0; i < galleryJson.data.size(); i++){
                                    ImageJson imageJson = galleryJson.data.get(i);
                                    if(!imageJson.is_album){
                                        galleryJson.data.get(i).save();
                                    }
                                }
                                ActiveAndroid.setTransactionSuccessful();
                            }finally{
                                ActiveAndroid.endTransaction();
                            }
                        }
                    });
                }
            }
        })
                .startWith(galleryJson) //
                .subscribeOn(Schedulers.io()) //
                .observeOn(AndroidSchedulers.mainThread());

//        Observable<GalleryJson> result = mApiService.getObservableImages(section, sort)
//                .subscribeOn(Schedulers.io());
//
//        result.subscribe(new Subscriber<GalleryJson>(){
//            @Override
//            public void onCompleted(){
//
//            }
//
//            @Override
//            public void onError(Throwable e){
//
//            }
//
//            @Override
//            public void onNext(GalleryJson galleryJson){
//                // I know i know this is not pretty. but I want to clear the table each time when
//                // i get new images. So I don't end up having a huge table
//                new Delete().from(ImageJson.class).execute();
//
//                ActiveAndroid.beginTransaction();
//                try{
//                    for(int i = 0; i < galleryJson.data.size(); i++){
//                        ImageJson imageJson = galleryJson.data.get(i);
//                        if(!imageJson.is_album){
//                            galleryJson.data.get(i).save();
//                        }
//                    }
//                    ActiveAndroid.setTransactionSuccessful();
//                }finally{
//                    ActiveAndroid.endTransaction();
//                }
//            }
//        });
//        return result;
    }

}
