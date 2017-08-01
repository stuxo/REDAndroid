package ch.redacted.data.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sxo on 6/12/16.
 */

public interface PyWhatAutoService {

    @GET("/dl.pywa")
    Single<Response<ResponseBody>> addTorrent(@Query("pass") String pass, @Query("site") String site, @Query("id") int id);

    /********
     * Helper class that sets up a new services
     *******/
    class Creator {

        public static PyWhatAutoService newPywaService(String url) {

            Retrofit retrofit = new Retrofit.Builder().client(
                new OkHttpClient.Builder().followRedirects(false)
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build())
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
            return retrofit.create(PyWhatAutoService.class);
        }
    }
}

