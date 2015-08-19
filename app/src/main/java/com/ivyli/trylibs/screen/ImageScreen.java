package com.ivyli.trylibs.screen;


import android.os.Bundle;

import com.google.gson.annotations.Expose;
import com.ivyli.trylibs.AndroidModule;
import com.ivyli.trylibs.R;
import com.ivyli.trylibs.android.ActionBarOwner;
import com.ivyli.trylibs.mortarcore.WithModule;
import com.ivyli.trylibs.service.json.ImageJson;
import com.ivyli.trylibs.views.SingleImageView;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import flow.path.Path;
import mortar.ViewPresenter;

@Layout(R.layout.image_view)
@WithModule(ImageScreen.Module.class)
public class ImageScreen extends Path{
    @Expose
    private final ImageJson mImage;

    public ImageScreen(ImageJson imageIndex){
        this.mImage = imageIndex;
    }

    @dagger.Module(injects = {SingleImageView.class}, addsTo = AndroidModule.class, library = true)
    public class Module{
        @Provides
        ImageJson provideImage(){
            return mImage;
        }
    }

    @Singleton
    public static class Presenter extends ViewPresenter<SingleImageView>{
        private final ImageJson mImage;
        private final ActionBarOwner actionBar;


        @Inject
        public Presenter(ImageJson image, ActionBarOwner actionBar){
            this.mImage = image;
            this.actionBar = actionBar;

        }

        @Override
        public void dropView(SingleImageView view){
            super.dropView(view);
        }

        @Override
        public void onLoad(Bundle savedInstanceState){
            if(!hasView()){
                return;
            }
            getView().showImage(mImage);
        }

        @Override
        protected void onExitScope(){
        }

    }
}

