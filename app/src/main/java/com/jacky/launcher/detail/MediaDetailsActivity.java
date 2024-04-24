package com.jacky.launcher.detail;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.jacky.launcher.R;

public class MediaDetailsActivity extends FragmentActivity {

    public static final String MEDIA = "Media";
    public static final String SHARED_ELEMENT_NAME = "hero";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }
}