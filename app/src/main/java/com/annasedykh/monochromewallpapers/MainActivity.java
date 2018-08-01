package com.annasedykh.monochromewallpapers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.annasedykh.monochromewallpapers.api.Api;
import com.annasedykh.monochromewallpapers.photo.PhotoAdapter;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
public static final int COLUMN_NUMBER = 3;
private Api api;
private PhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
