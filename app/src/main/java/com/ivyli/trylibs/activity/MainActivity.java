package com.ivyli.trylibs.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.ivyli.trylibs.IvyBaseActivity;
import com.ivyli.trylibs.R;
import com.ivyli.trylibs.android.ActionBarOwner;
import com.ivyli.trylibs.screen.GsonParceler;
import com.ivyli.trylibs.screen.ImageListScreen;

import java.util.Random;

import javax.inject.Inject;

import flow.Flow;
import flow.FlowDelegate;
import flow.History;
import flow.path.Path;
import flow.path.PathContainerView;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import rx.functions.Action0;

import static mortar.bundler.BundleServiceRunner.getBundleServiceRunner;

public class MainActivity extends IvyBaseActivity
        implements ActionBarOwner.Activity, Flow.Dispatcher{

    public static final String TAG = MainActivity.class.getSimpleName();

    private MortarScope activityScope;
    private FlowDelegate flowDelegate;
    private PathContainerView container;

    @Inject
    ActionBarOwner actionBarOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_main);
        if(activityScope == null){
            int random = new Random().nextInt();
            String scopeName = getLocalClassName() + "-ivyLi-" + getTaskId() + random;
            MortarScope parentScope = MortarScope.getScope(getApplication());

            activityScope = parentScope.buildChild()
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .build(scopeName);
        }

        getBundleServiceRunner(activityScope).onCreate(savedInstanceState);

        actionBarOwner.takeView(this);
        GsonParceler parceler = new GsonParceler(new Gson());
        FlowDelegate.NonConfigurationInstance nonConfig =
                (FlowDelegate.NonConfigurationInstance)getLastNonConfigurationInstance();
        container = (PathContainerView)findViewById(R.id.container);
        flowDelegate = FlowDelegate.onCreate(nonConfig, getIntent(), savedInstanceState, parceler,
                History.single(new ImageListScreen()), this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == android.R.id.home){
            return Flow.get(this).goBack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getContext(){
        return this;
    }

    @Override
    public void dispatch(Flow.Traversal traversal, Flow.TraversalCallback callback){
        Path newScreen = traversal.destination.top();
        String title = newScreen.getClass().getSimpleName();
        ActionBarOwner.MenuAction menu = new ActionBarOwner.MenuAction("Images", new Action0(){
            @Override
            public void call(){
                Flow.get(MainActivity.this).set(new ImageListScreen());
            }
        });
        actionBarOwner.setConfig(
                new ActionBarOwner.Config(false, !(newScreen instanceof ImageListScreen), title, menu));

        container.dispatch(traversal, callback);
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        flowDelegate.onNewIntent(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();
        flowDelegate.onResume();
    }

    @Override
    protected void onPause(){
        flowDelegate.onPause();
        super.onPause();
    }


    @Override
    public Object getSystemService(String name){
        if(flowDelegate != null){
            Object flowService = flowDelegate.getSystemService(name);
            if(flowService != null) return flowService;
        }

        return activityScope != null && activityScope.hasService(name) ? activityScope.getService(name)
                : super.getSystemService(name);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        getBundleServiceRunner(this).
                onSaveInstanceState(outState);
        flowDelegate.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed(){
        if(Flow.get(this).goBack()){
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy(){
        actionBarOwner.dropView(this);
        actionBarOwner.setConfig(null);

        // activityScope may be null in case isWrongInstance() returned true in onCreate()
        if(isFinishing() && activityScope != null){
            activityScope.destroy();
            activityScope = null;
        }

        super.onDestroy();
    }

    @Override
    public void setShowHomeEnabled(boolean enabled){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
    }

    @Override
    public void setUpButtonEnabled(boolean enabled){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(enabled);
        actionBar.setHomeButtonEnabled(enabled);
    }

    @Override
    public void setTitle(CharSequence title){
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setMenu(ActionBarOwner.MenuAction action){
        // don't want to do any thing here....
    }


}
