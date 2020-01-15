package ch.redacted.data.remote;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import ch.redacted.REDApplication;
import ch.redacted.app.BuildConfig;
import ch.redacted.data.model.Announcement;
import ch.redacted.data.model.Artist;
import ch.redacted.data.model.Collage;
import ch.redacted.data.model.CollageSearch;
import ch.redacted.data.model.Conversation;
import ch.redacted.data.model.Conversations;
import ch.redacted.data.model.ForumCategory;
import ch.redacted.data.model.ForumThread;
import ch.redacted.data.model.ForumView;
import ch.redacted.data.model.Index;
import ch.redacted.data.model.Profile;
import ch.redacted.data.model.Recents;
import ch.redacted.data.model.RequestSearch;
import ch.redacted.data.model.Subscription;
import ch.redacted.data.model.Top10;
import ch.redacted.data.model.TorrentBookmark;
import ch.redacted.data.model.TorrentComments;
import ch.redacted.data.model.TorrentGroup;
import ch.redacted.data.model.TorrentSearch;
import ch.redacted.data.model.UserSearch;
import ch.redacted.data.remote.interceptors.AddApiKeyInterceptor;
import ch.redacted.data.remote.interceptors.AddCookiesInterceptor;
import ch.redacted.data.remote.interceptors.ReceivedCookiesInterceptor;
import io.reactivex.Single;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sxo on 6/12/16.
 */

public interface ApiService {

    String ENDPOINT = REDApplication.DEFAULT_SITE + "/";

    @FormUrlEncoded
    @POST("login.php")
    Single<Response<ResponseBody>> login(@Field("username") String username, @Field("password") String password, @Field("keeplogged") int keepLogged);

    @GET("ajax.php?action=index")
    Single<Index> index();

    @GET("ajax.php?action=user")
    Single<Profile> profile(@Query("id") int userId, @Query("auth") String auth);

    @GET("ajax.php?action=index")
    Single<Index> cookieCheck();

    @GET("ajax.php?action=announcements")
    Single<Announcement> announcements();

    @GET("ajax.php?action=forum&type=main")
    Single<ForumCategory> forumCategories();

    @GET("ajax.php?action=forum&type=viewforum")
    Single<ForumView> forumThreads(@Query("forumid") int forumId, @Query("page") int page);

    @GET("ajax.php?action=forum&type=viewthread")
    Single<ForumThread> threadPosts(@Query("threadid") int threadId, @Query("postid") Integer postId, @Query("page") Integer page);

    @GET("ajax.php?action=top10")
    Single<Top10> top10(@Query("details") String details, @Query("limit") int limit, @Query("type") String type);

    @GET("ajax.php?action=torrentgroup")
    Single<TorrentGroup> release(@Query("id") int id);

    @GET("ajax.php?action=tcomments")
    Single<TorrentComments> torrentComments(@Query("id") int id, @Query("page") int page);

    @GET("ajax.php?action=inbox")
    Single<Conversations> inbox(@Query("sort") String sort, @Query("type") String type);

    @GET("ajax.php?action=usersearch")
    Single<UserSearch> userSearch(@Query("search") String search);

    @GET("ajax.php?action=browse")
    Single<TorrentSearch> torrentSearch(@Query("searchstr") String search);

    @GET("ajax.php?action=requests")
    Single<RequestSearch> requestSearch(@Query("search") String search);

    @GET("ajax.php?action=collages")
    Single<CollageSearch> collageSearch(@Query("search") String search);

    @GET("ajax.php?action=artist")
    Single<Artist> artist(@Query("artistname") String name, @Query("artistrelease") boolean artistReleasesOnly);

    @GET("ajax.php?action=artist")
    Single<Artist> artist(@Query("id") int id, @Query("artistrelease") boolean artistReleasesOnly);

    @POST("artist.php")
    Single<ResponseBody> toggleArtistBookmark(@Query("action") String action, @Query("artistid") int artistId, @Query("auth") String auth);

    @FormUrlEncoded
    @POST("forums.php")
    Single<ResponseBody> newThreadPost(@Field("action") String action, @Field("forum") int forumId, @Field("thread") int threadId, @Field("body") String body, @Query("auth") String auth);

    @POST("bookmarks.php")
    Single<ResponseBody> toggleBookmark(@Query("action") String action, @Query("type") String type, @Query("auth") String auth, @Query("id") int id);

    @GET("ajax.php?action=subscriptions")
    Single<Subscription> subscriptions(@Query("showunread") int showUnread);

    @GET("ajax.php?action=request")
    Single<ch.redacted.data.model.Request> request(@Query("id") int id);

    @GET("ajax.php?action=collage")
    Single<Collage> collage(@Query("id") int id);

    @GET("torrents.php?action=download")
    Single<retrofit2.Response<ResponseBody>> file(@Query("id") int id, @Query("authkey") String auth, @Query("torrent_pass") String pass);

    @GET("ajax.php?action=user_recents")
    Single<Recents> recents(@Query("userid") int id, @Query("authkey") String auth, @Query("limit") int limit);

    @GET("ajax.php?action=bookmarks&type=torrents")
    Single<TorrentBookmark> torrentBookmarks();

    @GET("ajax.php?action=inbox")
    Single<Conversations> inbox();

    @GET("ajax.php?action=inbox&type=viewconv")
    Single<Conversation> conversation(@Query("id") int id);

    @FormUrlEncoded
    @POST("inbox.php")
    Single<ResponseBody> sendMessage(@Field("action") String action, @Field("toid") String toId, @Field("convid") String convId, @Field("subject") String subject, @Field("auth") String auth, @Field("body") String body);

    @POST("userhistory.php?action=thread_subscribe")
    Single<ResponseBody> toggleForumSubscription(@Query("topicid") int threadId, @Query("auth") String auth);

    /********
     * Helper class that sets up a new services
     *******/
    class Creator {

        public static ApiService newApiService(Context applicationContext) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .serializeNulls()
                    .create();
            Retrofit retrofit = new Retrofit.Builder().client(new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                            Request original = chain.request();

                            Request request = original.newBuilder()
                                    .header("User-Agent", "PTHAndroid Android " + BuildConfig.VERSION_NAME).build();

                            return chain.proceed(request);
                        }
                    })
                    .followRedirects(false)
                    .addInterceptor(new AddCookiesInterceptor(applicationContext))
                    .addInterceptor(new AddApiKeyInterceptor(applicationContext))
                    .addInterceptor(new ReceivedCookiesInterceptor(applicationContext))
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build())
                    .baseUrl(ApiService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            return retrofit.create(ApiService.class);
        }
    }
}

