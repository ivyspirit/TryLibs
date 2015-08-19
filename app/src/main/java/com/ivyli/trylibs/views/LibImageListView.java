package com.ivyli.trylibs.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.ivyli.trylibs.ObjectGraphService;
import com.ivyli.trylibs.R;
import com.ivyli.trylibs.screen.ImageListScreen;
import com.ivyli.trylibs.service.json.ImageJson;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

public class LibImageListView extends ListView{
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

    public void showImages(List<ImageJson> imageJsons){
        Adapter adapter = new Adapter(getContext(), imageJsons);

        setAdapter(adapter);
        setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                presenter.onImageSelected(position);
            }
        });
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