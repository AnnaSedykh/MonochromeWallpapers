package com.annasedykh.monochromewallpapers.photo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.annasedykh.monochromewallpapers.MainActivity;
import com.annasedykh.monochromewallpapers.PreviewActivity;
import com.annasedykh.monochromewallpapers.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link PhotoAdapter} displays a scrolling grid of {@link Photo} photos using RecyclerView.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private List<Photo> data = new ArrayList<>();

    /**
     * Create new views (invoked by the layout manager)
     */
    @NonNull
    @Override
    public PhotoAdapter.PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo, parent, false);
        return new PhotoViewHolder(view);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.PhotoViewHolder holder, int position) {
        Photo photo = data.get(position);
        holder.bind(photo);
    }

    /**
     * Return the size of photo's dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Photo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<Photo> getData() {
        return data;
    }

    /**
     * {@link PhotoViewHolder} provide a reference to the views for each photo
     */
    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private static final double THUMB_SIDE_RATIO = 0.5625; // 1080 / 1920

        @BindView(R.id.thumbnail)
        ImageView thumbnail;
        private Context context;

        /**
         * {@link PhotoViewHolder} constructor
         */
        PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        /**
         * Binds view with photo
         */
        void bind(final Photo photo) {
            String downloadThumbnailUrl = photo.getPhotoUrls().get(Photo.SMALL_SIZE);
            final String downloadFullUrl = photo.getPhotoUrls().get(Photo.FULL_SIZE);
            String incrementLink = photo.getLinks().get(Photo.DOWNLOAD_INCREMENT);

            // Count thumbnail size
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            double thumbWidth = screenWidth / MainActivity.COLUMN_NUMBER;
            double thumbHeight = thumbWidth / THUMB_SIDE_RATIO;

            try {
                Glide.with(context)
                        .load(downloadThumbnailUrl)
                        .apply(new RequestOptions()
                                .centerCrop()
                                .override((int) thumbWidth, (int) thumbHeight))
                        .into(thumbnail);
            } catch (Exception e) {
                e.printStackTrace();
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!downloadFullUrl.isEmpty()) {
                        Intent preview = new Intent(context, PreviewActivity.class);
                        preview.putExtra("fullUrl", downloadFullUrl);
                        preview.putExtra("id", photo.getId());
                        context.startActivity(preview);
                    }
                }
            });
        }
    }
}

