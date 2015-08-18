package com.ivyli.trylibs.views;

import android.content.Context;
import android.util.AttributeSet;

import com.ivyli.trylibs.R;
import com.ivyli.trylibs.mortarcore.MortarContextFactory;

import flow.path.Path;

/**
 * Created by IvyLi on 8/16/15.
 */
public class ScreenSwitcherFrame extends FramePathContainerView {
    public ScreenSwitcherFrame(Context context, AttributeSet attrs) {
        super(context, attrs, new SimplePathContainer(R.id.screen_switcher_tag,
                Path.contextFactory(new MortarContextFactory())));
    }
}
