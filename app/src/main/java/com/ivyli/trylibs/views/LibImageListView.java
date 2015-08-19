package com.ivyli.trylibs.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ivyli.trylibs.ObjectGraphService;
import com.ivyli.trylibs.R;
import com.ivyli.trylibs.screen.ImageListScreen;
import com.ivyli.trylibs.service.json.ImageJson;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

public class LibImageListView extends RelativeLayout{
    private GridView mGrid;
    private ProgressBar mProgress;

    @Inject
    ImageListScreen.Presenter presenter;

    public LibImageListView(Context context, AttributeSet attrs){
        super(context, attrs);
        ObjectGraphService.inject(context.getApplicationContext(), this);
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

    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        mProgress = (ProgressBar)findViewById(R.id.image_list_progressbar);
        mGrid = (GridView)findViewById(R.id.image_list_view);

    }

    public void onError(){
        mProgress.setVisibility(GONE);
        Toast t = Toast.makeText(getContext(),
                R.string.error_loading_image, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }

    public void showImages(List<ImageJson> imageJsons, int index){
        Adapter adapter = new Adapter(getContext(), imageJsons);
        mProgress.setVisibility(GONE);
        mGrid.setAdapter(adapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                presenter.onImageSelected(position);
            }
        });

        mGrid.setSelection(index);
    }

    private static class Adapter extends ArrayAdapter<ImageJson>{
        public Adapter(Context context, List<ImageJson> objects){
            super(context, R.layout.image_list_item, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            ImageViewHolder viewHolder;
            if(null == convertView){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_list_item, null);
                viewHolder = new ImageViewHolder();
                viewHolder.image = (ImageView)convertView.findViewById(R.id.single_image_view);
                convertView.setTag(viewHolder);
            }else{

                viewHolder= (ImageViewHolder)convertView.getTag();

            }
            Picasso.with(getContext()).load(getItem(position).link).error(R.drawable.error_holder).into
                    (viewHolder.image);

            return convertView;
        }

        static class ImageViewHolder{
            ImageView image;
        }
    }
}