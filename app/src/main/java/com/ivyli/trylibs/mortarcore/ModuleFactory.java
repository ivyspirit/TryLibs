package com.ivyli.trylibs.mortarcore;

import android.content.res.Resources;

public abstract class ModuleFactory<T>{
    protected abstract Object createDaggerModule(Resources resources, T screen);
}

