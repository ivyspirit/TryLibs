package com.ivyli.trylibs;

import com.ivyli.trylibs.activity.MainActivity;
import com.ivyli.trylibs.screen.ImageListScreen;
import com.ivyli.trylibs.service.ClientId;
import com.ivyli.trylibs.service.DatabaseService;
import com.ivyli.trylibs.service.RetrofitApiService;
import com.ivyli.trylibs.service.RetrofitInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

@Module(
        injects = {
                IvyApplication.class,
                MainActivity.class
        },
        includes = {
                ImageListScreen.Module.class
        },
        library = true,
        complete = false
)


public class AndroidModule{

    private IvyApplication app;

    private static final String CLIENT_ID = "3436c108ccc17d3";
    private final String endPoint = "https://api.imgur.com/3";

    public AndroidModule(IvyApplication app){
        this.app = app;
    }


    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    @ForApplication
    IvyApplication provideApplicationContext(){
        return app;
    }


    @Provides
    @Singleton
    RestAdapter provideRestAdapter(RetrofitInterceptor headers){
        return new RestAdapter.Builder() //
                .setEndpoint(endPoint) //
                .setRequestInterceptor(headers) //
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    @Provides
    @Singleton
    RetrofitApiService provideApiService(RestAdapter restAdapter){
        return restAdapter.create(RetrofitApiService.class);
    }

    @Provides
    @Singleton
    DatabaseService providDatabaseService(RetrofitApiService apiService){
        return new DatabaseService(apiService);
    }


    @Provides
    @Singleton
    @ClientId
    String provideClientId(){
        return CLIENT_ID;
    }

}
