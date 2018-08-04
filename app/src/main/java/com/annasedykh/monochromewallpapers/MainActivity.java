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
    private static final String TAG = "MainActivity";
    public static final int COLUMN_NUMBER = 3;
    private static final int MARGIN_TOP = 200;

    private Api api;
    private PhotoAdapter adapter;
    private PopupWindow popupWindow;

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

        loadPhotos("red");
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
                popupWindow = new PopupWindow(popupView, width, height, true);

                int posX = MainActivity.this.getResources().getDisplayMetrics().widthPixels;
                popupWindow.showAtLocation(recycler, Gravity.NO_GRAVITY, posX, MARGIN_TOP);

                initColorViews(popupView);

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
     * Query the Unsplash API dataset and return a list of {@link Photo} objects of chosen color.
     */
    private void loadPhotos(String color) {
        Call<PhotoSearchResult> call = api.searchPhotos(color, 1, 30, Photo.ORIENTATION_PORTRAIT);
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

    /**
     * Find color views in popupView and set OnColorClickListener
     */
    private void initColorViews(View popupView) {
        View beigeView = popupView.findViewById(R.id.color_beige);
        beigeView.setOnClickListener(new OnColorClickListener(getString(R.string.beige)));

        View blackView = popupView.findViewById(R.id.color_black);
        blackView.setOnClickListener(new OnColorClickListener(getString(R.string.black)));

        View blueView = popupView.findViewById(R.id.color_blue);
        blueView.setOnClickListener(new OnColorClickListener(getString(R.string.blue)));

        View brownView = popupView.findViewById(R.id.color_brown);
        brownView.setOnClickListener(new OnColorClickListener(getString(R.string.brown)));

        View darkGreenView = popupView.findViewById(R.id.color_dark_green);
        darkGreenView.setOnClickListener(new OnColorClickListener(getString(R.string.dark_green)));

        View darkRedView = popupView.findViewById(R.id.color_dark_red);
        darkRedView.setOnClickListener(new OnColorClickListener(getString(R.string.dark_red)));

        View greenView = popupView.findViewById(R.id.color_green);
        greenView.setOnClickListener(new OnColorClickListener(getString(R.string.green)));

        View greyView = popupView.findViewById(R.id.color_grey);
        greyView.setOnClickListener(new OnColorClickListener(getString(R.string.grey)));

        View navyView = popupView.findViewById(R.id.color_navy);
        navyView.setOnClickListener(new OnColorClickListener(getString(R.string.navy)));

        View orangeView = popupView.findViewById(R.id.color_orange);
        orangeView.setOnClickListener(new OnColorClickListener(getString(R.string.orange)));

        View pinkView = popupView.findViewById(R.id.color_pink);
        pinkView.setOnClickListener(new OnColorClickListener(getString(R.string.pink)));

        View purpleView = popupView.findViewById(R.id.color_purple);
        purpleView.setOnClickListener(new OnColorClickListener(getString(R.string.purple)));

        View redView = popupView.findViewById(R.id.color_red);
        redView.setOnClickListener(new OnColorClickListener(getString(R.string.red)));

        View turquoiseView = popupView.findViewById(R.id.color_turquoise);
        turquoiseView.setOnClickListener(new OnColorClickListener(getString(R.string.turquoise)));

        View whiteView = popupView.findViewById(R.id.color_white);
        whiteView.setOnClickListener(new OnColorClickListener(getString(R.string.white)));

        View yellowView = popupView.findViewById(R.id.color_yellow);
        yellowView.setOnClickListener(new OnColorClickListener(getString(R.string.yellow)));
    }


    private class OnColorClickListener implements View.OnClickListener{
        private String color;

        OnColorClickListener(String color) {
            this.color = color;
        }

        @Override
        public void onClick(View v) {
            loadPhotos(color);
            popupWindow.dismiss();
        }
    }
}
