package com.ivyli.trylibs.helper;

import java.io.UnsupportedEncodingException;

import retrofit.client.Response;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedByteArray;


public class StringHelper{

    public static String getBodyString(Response response){

        if(null == response){
            return "Error happened. Response null.";
        }

        try{
            TypedByteArray body = (TypedByteArray)response.getBody();
            if(null == body){
                return "Error happened. Can not parse body string";
            }
            byte[] bodyBytes = body.getBytes();
            String bodyMime = body.mimeType();
            String bodyCharset = MimeUtil.parseCharset(bodyMime);
            return new String(bodyBytes, bodyCharset);
        }catch(UnsupportedEncodingException e){
            //we just return null if we got here
        }

        return null;
    }

}
