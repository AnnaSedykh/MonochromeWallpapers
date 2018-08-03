package com.annasedykh.monochromewallpapers.api;

import com.annasedykh.monochromewallpapers.photo.PhotoSearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * {@link Api} interface for working with Unsplash API using Retrofit2
 */
public interface Api {

    /**
     * GET request to search photos with the required parameters
     *
     * @param query       Search terms
     * @param page        Page number to retrieve
     * @param perPage     Number of items per page (max: 30)
     * @param orientation Filter search results by photo orientation
     * @return a single page of photo results for a query
     */
    @GET("search/photos")
    Call<PhotoSearchResult> searchPhotos(@Query("query") String query,
                                         @Query("page") int page,
                                         @Query("per_page") int perPage,
                                         @Query("orientation") String orientation);

    /**
     * GET request triggers increment of photo downloads
     *
     * @param id photo id
     * @return photo url
     */
    @GET("photos/{id}/download")
    Call<Object> incrementDownloads(@Path("id") String id);
}
