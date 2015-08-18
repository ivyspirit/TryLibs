package com.ivyli.trylibs.android;

import android.content.Context;
import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mortar.Presenter;
import mortar.bundler.BundleService;
import rx.functions.Action0;

import static mortar.bundler.BundleService.getBundleService;

public class ActionBarOwner extends Presenter<ActionBarOwner.Activity>{
    public interface Activity {
        void setShowHomeEnabled(boolean enabled);

        void setUpButtonEnabled(boolean enabled);

        void setTitle(CharSequence title);

        void setMenu(MenuAction action);

        Context getContext();
    }

    public static class Config {
        public final boolean showHomeEnabled;
        public final boolean upButtonEnabled;
        public final CharSequence title;
        public final MenuAction action;

        public Config(boolean showHomeEnabled, boolean upButtonEnabled, CharSequence title,
                      MenuAction action) {
            this.showHomeEnabled = showHomeEnabled;
            this.upButtonEnabled = upButtonEnabled;
            this.title = title;
            this.action = action;
        }

        public Config withAction(MenuAction action) {
            return new Config(showHomeEnabled, upButtonEnabled, title, action);
        }
    }

    public static class MenuAction {
        public final CharSequence title;
        public final Action0 action;

        public MenuAction(CharSequence title, Action0 action) {
            this.title = title;
            this.action = action;
        }
    }

    private Config config;

    @Inject
    ActionBarOwner() {
    }

    @Override public void onLoad(Bundle savedInstanceState) {
        if (config != null) update();
    }

    public void setConfig(Config config) {
        this.config = config;
        update();
    }

    public Config getConfig() {
        return config;
    }

    @Override protected BundleService extractBundleService(Activity activity) {
        return getBundleService(activity.getContext());
    }

    private void update() {
        if (!hasView()) return;
        Activity activity = getView();

        activity.setShowHomeEnabled(config.showHomeEnabled);
        activity.setUpButtonEnabled(config.upButtonEnabled);
        activity.setTitle(config.title);
        activity.setMenu(config.action);
    }

    @Module(library = true)
    public static class ActionBarModule {
        @Provides
        @Singleton
        ActionBarOwner provideActionBarOwner() {
            return new ActionBarOwner();
        }
    }
}
