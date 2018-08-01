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
    /** A list of photo urls */
    @SerializedName("urls")
    private Map<String, String> photoUrls;
    /** A list of special links */
    private Map<String, String> links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(Map<String, String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
}
