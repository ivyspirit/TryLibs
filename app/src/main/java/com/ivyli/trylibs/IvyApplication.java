package com.ivyli.trylibs;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;
import mortar.MortarScope;


public class IvyApplication extends Application{

    private ObjectGraph applicationGraph;

    private MortarScope rootScope;


    @Override
    public void onCreate(){
        super.onCreate();

        applicationGraph = ObjectGraph.create(getModules().toArray());
        ActiveAndroid.initialize(this);
    }

    protected List<Object> getModules(){
        return Arrays.<Object>asList(new AndroidModule(this));
    }

    public void inject(Object object){
        applicationGraph.inject(object);
    }

    @Override
    public Object getSystemService(String name){
        if(rootScope == null){
            rootScope = MortarScope.buildRootScope()
                    .withService(ObjectGraphService.SERVICE_NAME, ObjectGraph.create(new
                            AndroidModule(this)))
                    .build("Root");
        }
        ActiveAndroid.initialize(this);
        if(rootScope.hasService(name)){
            return rootScope.getService(name);
        }

        return super.getSystemService(name);
    }

}
