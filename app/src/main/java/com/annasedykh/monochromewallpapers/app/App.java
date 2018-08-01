package com.annasedykh.monochromewallpapers.app;

import android.app.Application;

import com.annasedykh.monochromewallpapers.BuildConfig;
import com.annasedykh.monochromewallpapers.api.Api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * {@link App} configures Retrofit2 and creates {@link Api} instance.
 */
public class App extends Application {
    private Api api;

    @Override
    public void onCreate() {
        super.onCreate();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new AuthInterceptor())
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("dd.MM.yyyy HH.mm.ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        api = retrofit.create(Api.class);
    }

    public Api getApi() {
        return api;
    }

    private class AuthInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            HttpUrl url = request.url();
            HttpUrl newUrl = url.newBuilder().addQueryParameter("client_id", BuildConfig.ACCESS_KEY).build();
            Request newRequest = request.newBuilder().url(newUrl).build();
            return chain.proceed(newRequest);
        }
    }
}
