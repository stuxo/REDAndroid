package ch.redacted.data.remote.interceptors;

import android.content.Context;

import java.io.IOException;
import java.util.HashSet;

import ch.redacted.REDApplication;
import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {
    private Context context;

    public ReceivedCookiesInterceptor(Context context) {
        this.context = context;
    } // AddCookiesInterceptor()

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                if (!header.contains("deleted") && !header.contains("redirect")) {
                    cookies.add(header);
                }
            }

            REDApplication.get(context).getComponent().preferencesHelper().putCookieSet(cookies);
        }

        return originalResponse;
    }
}