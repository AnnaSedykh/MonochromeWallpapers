package com.annasedykh.monochromewallpapers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.annasedykh.monochromewallpapers.api.Api;
import com.annasedykh.monochromewallpapers.app.App;
import com.annasedykh.monochromewallpapers.photo.Photo;
import com.annasedykh.monochromewallpapers.photo.PhotoAdapter;
import com.annasedykh.monochromewallpapers.photo.PhotoSearchResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final int COLUMN_NUMBER = 3;
    private static final String TAG = "MainActivity";

    private Api api;
    private PhotoAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        App app = (App) getApplication();
        api = app.getApi();

        setSupportActionBar(toolbar);
        adapter = new PhotoAdapter();
        recycler.setLayoutManager(new GridLayoutManager(this, COLUMN_NUMBER));
        recycler.setAdapter(adapter);

        loadData();
    }

    /**
     * Query the Unsplash API dataset and return a list of {@link Photo} objects.
     */
    private void loadData() {
        Call<PhotoSearchResult> call = api.searchPhotos("green", 1, 30, Photo.ORIENTATION_PORTRAIT);
        //Call is executed asynchronously
        call.enqueue(new Callback<PhotoSearchResult>() {
            @Override
            public void onResponse(Call<PhotoSearchResult> call, Response<PhotoSearchResult> response) {
                Log.i(TAG, "search onResponse: ");
                PhotoSearchResult result = response.body();
                if (result != null && result.getTotal() > 0) {
                    adapter.setData(result.getPhotos());
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<PhotoSearchResult> call, Throwable t) {
                Log.w(TAG, "search onFailure: ", t);
            }
        });
    }
}
