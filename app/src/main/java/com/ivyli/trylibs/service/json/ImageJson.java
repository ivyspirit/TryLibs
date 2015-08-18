package com.ivyli.trylibs.service.json;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.inject.Inject;

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "image")
public class ImageJson extends Model{

    @Inject
    public ImageJson(){}

    @JsonProperty("link")
    @Column(name = "link")
    public  String link;

    @JsonProperty("title")
    @Column(name = "title")
    public  String title;

    @JsonProperty("width")
    @Column(name = "width")
    public  int width;

    @JsonProperty("height")
    @Column(name = "height")
    public  int height;

    @JsonProperty("datetime")
    @Column(name = "datetime")
    public  long datetime;

    @JsonProperty("views")
    @Column(name = "views")
    public  int views;

    @JsonProperty("is_album")
    @Column(name = "is_album")
    public  boolean is_album;
}
