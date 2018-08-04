package com.annasedykh.monochromewallpapers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

/**
 * {@link MainActivity} shows a scrolling grid of photos
 */
public class MainActivity extends AppCompatActivity {
    public static final int COLUMN_NUMBER = 3;
    private static final int MARGIN_TOP = 200;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.choose_color);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_window, null);

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

                int posX = MainActivity.this.getResources().getDisplayMetrics().widthPixels;
                popupWindow.showAtLocation(recycler, Gravity.NO_GRAVITY, posX, MARGIN_TOP);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
                return true;
            }
        });
        return true;
    }

    /**
     * Query the Unsplash API dataset and return a list of {@link Photo} objects.
     */
    private void loadData() {
        Call<PhotoSearchResult> call = api.searchPhotos("pink", 1, 30, Photo.ORIENTATION_PORTRAIT);
        //Call is executed asynchronously
        call.enqueue(new Callback<PhotoSearchResult>() {
            @Override
            public void onResponse(Call<PhotoSearchResult> call, Response<PhotoSearchResult> response) {
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "search onResponse: ");
                }
                PhotoSearchResult result = response.body();
                if (result != null && result.getTotal() > 0) {
                    adapter.setData(result.getPhotos());
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<PhotoSearchResult> call, Throwable t) {
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "search onFailure: ", t);
                }
            }
        });
    }
}
