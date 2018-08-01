package com.annasedykh.monochromewallpapers.photo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * {@link PhotoSearchResult} model class for search result.
 */
public class PhotoSearchResult {

    /** Total photo quantity */
    private int total;

    /** Total pages quantity */
    @SerializedName("total_pages")
    private int pages;

    /** List of {@link Photo} objects*/
    @SerializedName("results")
    private List<Photo> photos;

    public int getTotal() {
        return total;
    }

    public int getPages() {
        return pages;
    }

    public List<Photo> getPhotos() {
        return photos;
    }
}
