package com.annasedykh.monochromewallpapers.photo;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * {@link Photo} is a photo model class.
 * Contains information related to a single photo.
 */
public class Photo {
    public static final String ORIENTATION_PORTRAIT = "portrait";
    public static final String FULL_SIZE = "full";
    public static final String SMALL_SIZE = "small";
    public static final String DOWNLOAD_INCREMENT = "download_location";

    /** Photo id */
    private String id;
    /** Photo width */
    private int width;
    /** Photo height */
    private int height;
    /** A list of photo urls */
    @SerializedName("urls")
    private Map<String, String> photoUrls;
    /** A list of special links */
    private Map<String, String> links;

    public String getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Map<String, String> getPhotoUrls() {
        return photoUrls;
    }

    public Map<String, String> getLinks() {
        return links;
    }
}
