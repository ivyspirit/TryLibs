package com.ivyli.trylibs.screen;


import android.os.Bundle;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.ivyli.trylibs.R;
import com.ivyli.trylibs.mortarcore.WithModule;
import com.ivyli.trylibs.service.DatabaseService;
import com.ivyli.trylibs.service.json.GalleryJson;
import com.ivyli.trylibs.service.json.ImageJson;
import com.ivyli.trylibs.views.LibImageListView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import flow.Flow;
import flow.path.Path;
import mortar.ViewPresenter;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;


@Layout(R.layout.root_layout)
@WithModule(ImageListScreen.Module.class)
public class ImageListScreen extends Path{
    public static String TAG = ImageListScreen.class.getSimpleName();

    @Expose
    static int imageIndex = 0;

    @dagger.Module(injects = LibImageListView.class, complete = false, library = true)
    public static class Module{
        @Provides
        List<ImageJson> provideImages(GalleryJson images){
            return images.data;
        }
    }

    @Singleton
    public static class Presenter extends ViewPresenter<LibImageListView>{
        private List<ImageJson> images;

        @Inject
        DatabaseService mDataService;

        private Subscription running = Subscriptions.empty();

        @Inject
        Presenter(List<ImageJson> chats){
            this.images = chats;
        }

        @Override
        public void onLoad(Bundle savedInstanceState){
            super.onLoad(savedInstanceState);
            if(!hasView()){
                return;
            }

            // check the cache first
            if(null == images || images.size() <= 0){
                GalleryJson galleryJson = new GalleryJson();
                images = galleryJson.getImages();
            }

            if((null != images && images.size() > 0)){
                getView().showImages(images, imageIndex);
                return;
            }

            // seems like we did not have any images in the local db yet. make api call to imagur.
            running = mDataService.loadImages("hot", "virtal")
                    .subscribeOn(Schedulers.io()). //
                            observeOn(AndroidSchedulers.mainThread()).
                            subscribe(new Observer<GalleryJson>(){
                                @Override
                                public void onCompleted(){
                                    Log.w(TAG, "Completed?? OK.");
                                    running = Subscriptions.empty();
                                }

                                @Override
                                public void onError(Throwable e){
                                    // I still want to try to see if there is any data cached we
                                    // can display. I should set an error view.
                                    List<ImageJson> list = new GalleryJson().getImages();
                                    if(null != list && list.size() > 0){
                                        getView().showImages(list, 0);
                                        return;
                                    }

                                    getView().onError();
                                    Log.w(TAG, e.getLocalizedMessage());
                                    running = Subscriptions.empty();
                                }

                                @Override
                                public void onNext(GalleryJson galleryJson){
                                    if(!hasView()){
                                        return;
                                    }
                                    images = galleryJson.getImages();
                                    getView().showImages(images, 0);
                                }
                            });

        }

        public void onImageSelected(int position){
            // want to be able to go back to the prev position when user tap back.
            imageIndex = position;
            Flow.get(getView()).set(new ImageScreen(images.get(position)));
        }
    }
}
