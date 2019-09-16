package ch.redacted.data.remote.interceptors;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;

import ch.redacted.REDApplication;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This interceptor put all the Cookies in Preferences in the Request.
 * Your implementation on how to get the Preferences may ary, but this will work 99% of the time.
 */
public class AddApiKeyInterceptor implements Interceptor {
    private Context context;

    public AddApiKeyInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String key = REDApplication.get(context).getComponent().preferencesHelper().getApiKey();

        if (!key.isEmpty()){
            builder.addHeader("Authorization", key);
        }

        return chain.proceed(builder.build());
    }
}
