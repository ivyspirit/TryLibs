package com.ivyli.trylibs.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ivyli.trylibs.ObjectGraphService;
import com.ivyli.trylibs.R;
import com.ivyli.trylibs.screen.ImageScreen;
import com.ivyli.trylibs.service.json.ImageJson;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class SingleImageView extends LinearLayout{

    @Inject
    ImageScreen.Presenter presenter;

    private ImageView mImageview;

    public SingleImageView(Context context, AttributeSet attrs){
        super(context, attrs);
        setOrientation(VERTICAL);
        ObjectGraphService.inject(context, this);
    }

    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        mImageview = (ImageView)findViewById(R.id.single_image);

    }

    @Override
    protected void onAttachedToWindow(){
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    public void showImage(ImageJson imageJson){
        Picasso.with(getContext()).
                load(imageJson.link).
                error(R.drawable.error_holder).
                into(mImageview);
    }

}
