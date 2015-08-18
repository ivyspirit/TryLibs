package com.ivyli.trylibs.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ivyli.trylibs.R;

import flow.Flow;
import flow.path.Path;
import flow.path.PathContainer;
import flow.path.PathContainerView;

/**
 * Created by IvyLi on 8/16/15.
 */
 public class FramePathContainerView extends FrameLayout
        implements IHandlesBack, PathContainerView{
    private final PathContainer container;
    private boolean disabled;

    @SuppressWarnings("UnusedDeclaration") // Used by layout inflation, of course!
    public FramePathContainerView(Context context, AttributeSet attrs) {
        this(context, attrs, new SimplePathContainer(R.id.screen_switcher_tag, Path.contextFactory()));
    }

    /**
     * Allows subclasses to use custom {@link flow.path.PathContainer} implementations. Allows the use
     * of more sophisticated transition schemes, and customized context wrappers.
     */
    protected FramePathContainerView(Context context, AttributeSet attrs, PathContainer container) {
        super(context, attrs);
        this.container = container;
    }

    @Override public boolean dispatchTouchEvent(MotionEvent ev) {
        return !disabled && super.dispatchTouchEvent(ev);
    }

    @Override public ViewGroup getContainerView() {
        return this;
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override public void dispatch(Flow.Traversal traversal, final Flow.TraversalCallback callback) {
        disabled = true;
        container.executeTraversal(this, traversal, new Flow.TraversalCallback() {
            @Override public void onTraversalCompleted() {
                callback.onTraversalCompleted();
                disabled = false;
            }
        });
    }

    @Override public boolean onBackPressed() {
        if (getCurrentChild() instanceof IHandlesBack) {
            if (((IHandlesBack) getCurrentChild()).onBackPressed()) {
                return true;
            }
        }
        return Flow.get(getCurrentChild()).goBack();
    }

    @Override public ViewGroup getCurrentChild() {
        return (ViewGroup) getContainerView().getChildAt(0);
    }
}
