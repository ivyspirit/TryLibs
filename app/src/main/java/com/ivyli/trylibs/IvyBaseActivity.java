package com.ivyli.trylibs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class IvyBaseActivity extends AppCompatActivity{

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Perform injection so that when this call returns all dependencies will be available for use.
        ((IvyApplication) getApplication()).inject(this);
    }
}
