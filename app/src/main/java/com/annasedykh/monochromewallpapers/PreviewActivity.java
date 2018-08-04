package com.annasedykh.monochromewallpapers;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.annasedykh.monochromewallpapers.api.Api;
import com.annasedykh.monochromewallpapers.app.App;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * {@link PreviewActivity} shows the selected photo with the ability to download or set as wallpaper
 */
public class PreviewActivity extends AppCompatActivity {
    private static final String TAG = "PreviewActivity";
    private String photoId;
    private Bitmap image;
    private Api api;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.photo)
    ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        App app = (App) getApplication();
        api = app.getApi();

        photoId = getIntent().getStringExtra("id");
        String fullUrl = getIntent().getStringExtra("fullUrl");
        if (!fullUrl.isEmpty()) {
            Glide.with(this)
                    .asBitmap()
                    .load(fullUrl)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(photo);
        }
        setToolbar();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_set)
    public void setPhotoAsWallpaper() {
        getImageAsBitmap();
        WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            wallManager.setBitmap(image);
            Toast.makeText(this, "SETTING WALLPAPER SUCCESSFULLY", Toast.LENGTH_SHORT).show();
            incrementPhotoDownloads();
        } catch (IOException e) {
            Toast.makeText(this, "SETTING WALLPAPER FAILED", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_download)
    public void downloadPhoto() {
        getImageAsBitmap();
        saveImage(image);
        incrementPhotoDownloads();
    }

    /**
     * Query the Unsplash API to trigger increment of photo downloads
     */
    private void incrementPhotoDownloads() {
        Call<Object> call =  api.incrementDownloads(photoId);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(BuildConfig.DEBUG) {
                    Log.i(TAG, "search onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if(BuildConfig.DEBUG) {
                    Log.w(TAG, "search onFailure: ", t);
                }
            }
        });
    }

    private void getImageAsBitmap() {
        if(image == null) {
            photo.buildDrawingCache();
            image = photo.getDrawingCache();
        }
    }

    private String saveImage(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = "JPEG_" + photoId + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/MonochromeWallpapers");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            Toast.makeText(this, "PHOTO SAVED TO GALLERY", Toast.LENGTH_SHORT).show();
        }
        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }
}
