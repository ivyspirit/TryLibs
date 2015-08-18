package com.ivyli.trylibs;

import android.app.Activity;
import android.content.Context;

import dagger.ObjectGraph;
import mortar.MortarScope;

public class ObjectGraphService {
    public static final String SERVICE_NAME = ObjectGraphService.class.getName();

    /**
     * Create a new {@link ObjectGraph} based on the given module. The new graph will extend
     * the graph found in the parent scope (via {@link ObjectGraph#plus}), if there is one.
     */
    public static ObjectGraph create(MortarScope parent, Object... daggerModules) {
        ObjectGraph parentGraph = getObjectGraph(parent);

        return parentGraph == null ? ObjectGraph.create(daggerModules)
                : parentGraph.plus(daggerModules);
    }

    public static ObjectGraph getObjectGraph(Context context) {
        return (ObjectGraph) context.getSystemService(ObjectGraphService.SERVICE_NAME);
    }

    public static ObjectGraph getObjectGraph(MortarScope scope) {
        return scope.getService(ObjectGraphService.SERVICE_NAME);
    }

    /**
     * A convenience wrapper for {@link ObjectGraphService#getObjectGraph} to simplify dynamic
     * injection, typically for {@link Activity} and {@link android.view.View} instances that must be
     * instantiated by Android.
     */
    public static void inject(Context context, Object object) {
        getObjectGraph(context).inject(object);
    }

    /**
     * A convenience wrapper for {@link ObjectGraphService#getObjectGraph} to simplify dynamic
     * injection, typically for {@link Activity} and {@link android.view.View} instances that must be
     * instantiated by Android.
     */
    public static void inject(MortarScope scope, Object object) {
        getObjectGraph(scope).inject(object);
    }
}
