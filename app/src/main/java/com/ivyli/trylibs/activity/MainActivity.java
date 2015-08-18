package com.ivyli.trylibs.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ivyli.trylibs.IvyBaseActivity;
import com.ivyli.trylibs.R;
import com.ivyli.trylibs.android.ActionBarOwner;
import com.ivyli.trylibs.fragment.NavigationDrawerFragment;
import com.ivyli.trylibs.screen.GsonParceler;
import com.ivyli.trylibs.screen.ImageListScreen;
import com.ivyli.trylibs.views.FramePathContainerView;

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
        implements ActionBarOwner.Activity, Flow.Dispatcher, NavigationDrawerFragment.NavigationDrawerCallbacks{

    public static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
//    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private MortarScope activityScope;
    private FlowDelegate flowDelegate;
    private PathContainerView container;

    @Inject
    ActionBarOwner actionBarOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.new_activity_main);

//        mNavigationDrawerFragment = (NavigationDrawerFragment)
//                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        if(activityScope == null){
            String scopeName = getLocalClassName() + "-ivyLi-" + getTaskId();
            MortarScope parentScope = MortarScope.getScope(getApplication());

            activityScope = parentScope.buildChild()
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .build(scopeName);
        }

        getBundleServiceRunner(activityScope).onCreate(savedInstanceState);

        actionBarOwner.takeView(this);
        GsonParceler parceler = new GsonParceler(new Gson());
        FlowDelegate.NonConfigurationInstance nonConfig =
                (FlowDelegate.NonConfigurationInstance) getLastNonConfigurationInstance();
        container = (PathContainerView) findViewById(R.id.container);
        flowDelegate = FlowDelegate.onCreate(nonConfig, getIntent(), savedInstanceState, parceler,
                History.single(new ImageListScreen()), this);
        // Set up the drawer.
//        mNavigationDrawerFragment.setUp(
//                R.id.navigation_drawer,
//                (DrawerLayout)findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position){
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number){
        switch(number){
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
        }
    }

    public void restoreActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
//        if(!mNavigationDrawerFragment.isDrawerOpen()){
//            // Only show items in the action bar relevant to this screen
//            // if the drawer is not showing. Otherwise, let the drawer
//            // decide what to show in the action bar.
//            getMenuInflater().inflate(R.menu.main, menu);
//            restoreActionBar();
//            return true;
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber){
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment(){
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity){
            super.onAttach(activity);
            ((MainActivity)activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


    @Override
    public Context getContext(){
        return this;
    }

    @Override public void dispatch(Flow.Traversal traversal, Flow.TraversalCallback callback) {
        Path newScreen = traversal.destination.top();
        String title = newScreen.getClass().getSimpleName();
        ActionBarOwner.MenuAction menu = new ActionBarOwner.MenuAction("Images", new Action0() {
            @Override public void call() {
                Flow.get(MainActivity.this).set(new ImageListScreen());
            }
        });
        actionBarOwner.setConfig(
                new ActionBarOwner.Config(false, !(newScreen instanceof ImageListScreen), title, menu));

        container.dispatch(traversal, callback);
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        flowDelegate.onNewIntent(intent);
    }

    @Override protected void onResume() {
        super.onResume();
        flowDelegate.onResume();
    }

    @Override protected void onPause() {
        flowDelegate.onPause();
        super.onPause();
    }

//    @SuppressWarnings("deprecation") // https://code.google.com/p/android/issues/detail?id=151346
//    @Override public Object onRetainNonConfigurationInstance() {
//        return flowDelegate.onRetainNonConfigurationInstance();
//    }

    @Override public Object getSystemService(String name) {
        if (flowDelegate != null) {
            Object flowService = flowDelegate.getSystemService(name);
            if (flowService != null) return flowService;
        }

        return activityScope != null && activityScope.hasService(name) ? activityScope.getService(name)
                : super.getSystemService(name);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        flowDelegate.onSaveInstanceState(outState);
        getBundleServiceRunner(this).
                onSaveInstanceState(outState);
    }

    /** Inform the view about back events. */
    @Override public void onBackPressed() {
        if (!((FramePathContainerView)container).onBackPressed()) super.onBackPressed();
    }

    @Override protected void onDestroy() {
        actionBarOwner.dropView(this);
        actionBarOwner.setConfig(null);

        // activityScope may be null in case isWrongInstance() returned true in onCreate()
        if (isFinishing() && activityScope != null) {
            activityScope.destroy();
            activityScope = null;
        }

        super.onDestroy();
    }

    @Override public void setShowHomeEnabled(boolean enabled) {
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowHomeEnabled(false);
    }

    @Override public void setUpButtonEnabled(boolean enabled) {
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(enabled);
//        actionBar.setHomeButtonEnabled(enabled);
    }

    @Override public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override public void setMenu(ActionBarOwner.MenuAction action) {
//        if (action != actionBarMenuAction) {
//            actionBarMenuAction = action;
//            invalidateOptionsMenu();
//        }
    }


}
