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
public class AddCookiesInterceptor implements Interceptor {
    private Context context;

    public AddCookiesInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = REDApplication.get(context).getComponent().preferencesHelper().getCookies();
        for (String cookie : preferences) {
            if (!cookie.contains("deleted") && (!cookie.contains("redirect"))){
                builder.addHeader("Cookie", cookie);
                Log.v("OkHttp", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
            }
        }

        return chain.proceed(builder.build());
    }
}
