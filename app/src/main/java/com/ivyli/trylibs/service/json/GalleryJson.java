package com.ivyli.trylibs.service.json;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import javax.inject.Inject;

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "gallery")
public class GalleryJson extends Model{

    @Column(name = "images")
    @JsonProperty("data")
    public List<ImageJson> data;

    @Inject
    public GalleryJson(){
        super();
    }

    public List<ImageJson> getImages(){
        // get the data from db, cache it on the object
        data = new Select().from(ImageJson.class).execute();
        return data;
    }

}
