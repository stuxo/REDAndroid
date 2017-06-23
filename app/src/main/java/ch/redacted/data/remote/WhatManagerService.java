package ch.redacted.data.remote;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import io.reactivex.Single;
import java.io.IOException;

import ch.redacted.app.BuildConfig;
import ch.redacted.data.remote.interceptors.AddWmCookiesInterceptor;
import ch.redacted.data.remote.interceptors.ReceivedWmCookiesInterceptor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by sxo on 6/12/16.
 */

public interface WhatManagerService {

    @FormUrlEncoded
    @POST("/user/login")
    Single<Response<ResponseBody>> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/json/add_torrent")
    Single<Response<ResponseBody>> addTorrent(@Field("id") int id);

    /********
     * Helper class that sets up a new services
     *******/
    class Creator {

        public static WhatManagerService newWMApiService(String url, Context context) {

            Retrofit retrofit = new Retrofit.Builder().client(
                new OkHttpClient.Builder().followRedirects(false)
                    .addInterceptor(new Interceptor() {
                        @Override public okhttp3.Response intercept(Interceptor.Chain chain)
                            throws IOException {
                            Request original = chain.request();

                            Request request = original.newBuilder()
                                .header("User-Agent",
                                    "PTHAndroid Android " + BuildConfig.VERSION_NAME)
                                .build();

                            return chain.proceed(request);
                        }
                    })
                    .addInterceptor(new AddWmCookiesInterceptor(context))
                    .addInterceptor(new ReceivedWmCookiesInterceptor(context))
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build())
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
            return retrofit.create(WhatManagerService.class);
        }
    }
}

