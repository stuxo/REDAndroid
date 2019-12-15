package ch.redacted.data;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import ch.redacted.data.local.PreferencesHelper;
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
import ch.redacted.data.model.Request;
import ch.redacted.data.model.RequestSearch;
import ch.redacted.data.model.Subscription;
import ch.redacted.data.model.Top10;
import ch.redacted.data.model.TorrentBookmark;
import ch.redacted.data.model.TorrentComments;
import ch.redacted.data.model.TorrentGroup;
import ch.redacted.data.model.TorrentSearch;
import ch.redacted.data.model.UserSearch;
import ch.redacted.data.remote.ApiService;
import ch.redacted.data.remote.PyWhatAutoService;
import ch.redacted.data.remote.WhatManagerService;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;

@Singleton
public class DataManager {

    private final ApiService mApiService;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(ApiService apiService, PreferencesHelper preferencesHelper) {
        mApiService = apiService;
        mPreferencesHelper = preferencesHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }


    public Single<Profile> getProfile(int id) {
        if (id == 0) {
            id = mPreferencesHelper.getUserId();
        }

        return mApiService.profile(id, mPreferencesHelper.getAuth());
    }

    public Single<Boolean> login(String username, String password, int stayLoggedIn) {
        return mApiService.login(username, password, stayLoggedIn)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Response<ResponseBody>, Single<? extends Boolean>>() {
                    @Override public Single<? extends Boolean> apply(
                        Response<ResponseBody> responseBodyResponse) {
                        String header = responseBodyResponse.raw().header("Set-Cookie", "");
                        if (header.contains("session")
                            && !header.contains("deleted")
                            && !header.contains("redirect")) {
                            return Single.just(true);
                        } else {
                            getPreferencesHelper().clearCookies();
                            return Single.just(false);
                        }
                    }
                });
    }

    public Single<Index> loadIndex() {
        return mApiService.index().doOnSuccess(new Consumer<Index>() {
            @Override public void accept(Index index) throws Exception {
                mPreferencesHelper.setUserId(index.response.id);
                mPreferencesHelper.setAuth(index.response.authkey);
                mPreferencesHelper.setPass(index.response.passkey);
            }
        });
    }

    public Single<Index> validateCookie() {
        return mApiService.cookieCheck();
    }

    public Single<Index> loginWithCookie() {
        return loadIndex();
    }

    public Single<Announcement> getAnnouncements() {
        return mApiService.announcements().flatMap(new Function<Announcement, Single<? extends Announcement>>() {
            @Override public Single<? extends Announcement> apply(Announcement announcement) {
                for (Announcement.Announcements announcements : announcement.response.announcements) {
                    announcements.body = announcements.body.replace("<blockquote>", "");
                    announcements.body = announcements.body.replace("</blockquote>", "");
                    //todo replace bbcode on click with a clickable span for shows
                    //todo handle the href links in app
                }

                return Single.just(announcement);
            }
        });
    }

    public Single<List<Object>> getCategories() {
        return mApiService.forumCategories().flatMap(new Function<ForumCategory, SingleSource<? extends List<Object>>>() {
            @Override public SingleSource<? extends List<Object>> apply(ForumCategory forumCategory) {
                ArrayList<Object> items = new ArrayList<>();
                for (ForumCategory.Categories categories : forumCategory.response.categories) {
                    items.add(categories.categoryName);
                    for (ForumCategory.Forums forum : categories.forums) {
                        items.add(forum);
                    }
                }
                return Single.just(items);
            }
        });
    }

    public Single<ForumView> getThreads(int id, int page) {
        return mApiService.forumThreads(id, page);
    }

    public Single<ForumThread> getPosts(int threadId, Integer postId, Integer page) {
        return mApiService.threadPosts(threadId, postId, page);
    }

    public Single<Top10> getTopTorrents(String details) {
        return mApiService.top10(details, getPreferencesHelper().getTopTorrentLimit(), "torrents");
    }

    public Single<ResponseBody> addPost(String text, int forumId, int topicId) {
        return mApiService.newThreadPost("reply", forumId, topicId, text, mPreferencesHelper.getAuth());
    }

    public Single<TorrentGroup> getRelease(int id) {
        return mApiService.release(id);
    }

    public Single<Conversations> loadInbox(String type) {
        return mApiService.inbox("unread", type);
    }

    public Single<UserSearch> userSearch(String term) {
        return mApiService.userSearch(term);
    }

    public Single<TorrentSearch> torrentSearch(String term) {
        return mApiService.torrentSearch(term);
    }

    public Single<ResponseBody> addBookmark(int releaseId, String type) {
        return mApiService.toggleBookmark("add", type, mPreferencesHelper.getAuth(), releaseId);
    }

    public Single<ResponseBody> removeBookmark(int releaseId, String type) {
        return mApiService.toggleBookmark("remove", type, mPreferencesHelper.getAuth(), releaseId);
    }

    public Single<ResponseBody> addArtistBookmark(int artistId) {
        return mApiService.toggleArtistBookmark("notify", artistId, mPreferencesHelper.getAuth());
    }

    public Single<ResponseBody> removeArtistBookmark(int artistId) {
        return mApiService.toggleArtistBookmark("notifyremove", artistId, mPreferencesHelper.getAuth());
    }

    public Single<RequestSearch> requestSearch(String term) {
        return mApiService.requestSearch(term);
    }

    public Single<CollageSearch> collageSearch(String term) {
        return mApiService.collageSearch(term);
    }

    //this will only return direct matches
    public Single<Artist> artistsSearch(String name) {
        return mApiService.artist(name, false);
    }

    //this will only return direct matches
    public Single<Artist> artistsSearch(int id) {
        return mApiService.artist(id, false);
    }

    public Single<Subscription> loadSubscriptions(boolean read) {
        return mApiService.subscriptions(read ? 0 : 1); //1 means show unread, 0 means read
    }

    public Single<File> downloadRelease(final int id) {

        String path = mPreferencesHelper.getDefaultDownloadLocation();
        if (path == null){
            path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getAbsoluteFile().getPath();
        }
    
        final String finalPath = path;
        return mApiService.file(id, mPreferencesHelper.getAuth(), mPreferencesHelper.getPass())
                .flatMap(new Function<Response<ResponseBody>, SingleSource<? extends File>>() {
                    @Override
                    public SingleSource<? extends File> apply(Response<ResponseBody> response) throws Exception {
                        String header = response.headers().get("Content-Disposition");
                        String fileName = header.replace("attachment; filename=", "").replace("\"", "");
                        //todo allow users to select directory
                        File file = new File(finalPath,
                                fileName.replaceAll("\"", ""));

                        BufferedSink sink;
                        try {
                            sink = Okio.buffer(Okio.sink(file));
                            sink.writeAll(response.body().source());
                            sink.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return Single.just(file);
                    }
                });
    }

    public Single<Response<ResponseBody>> downloadWmRelease(final int id, Context context) {

        try {
            //todo: gross, we are pushing context all the way down here.
            final WhatManagerService wm =
                WhatManagerService.Creator.newWMApiService(mPreferencesHelper.getWmHost(), context);
            return wm.login(mPreferencesHelper.getWmUser(), mPreferencesHelper.getWmPassword())
                .flatMap(new Function<Response<ResponseBody>, Single<Response<ResponseBody>>>() {
                    @Override
                    public Single<Response<ResponseBody>> apply(Response<ResponseBody> response)
                        throws Exception {
                        return wm.addTorrent(id);
                    }
                });
        } catch (IllegalArgumentException e) {
            return Single.error(new Exception("WM is not configured correctly"));
        }
    }

    public Single<Response<ResponseBody>> downloadPywaRelease(final int id) {

        final PyWhatAutoService pywa =
            PyWhatAutoService.Creator.newPywaService(mPreferencesHelper.getPywaHost());

        return pywa.addTorrent(mPreferencesHelper.getPywaPassword(), "redacted", id);
    }


    public Single<Request> getRequest(int id) {
        return mApiService.request(id);
    }

    public Single<Collage> getCollage(int id) {
        return mApiService.collage(id);
    }

    public Single<Recents> getRecents(int id) {
        if (id == 0) {
            id = mPreferencesHelper.getUserId();
        }
        return mApiService.recents(id, mPreferencesHelper.getAuth(), 20);
    }

    public Single<Integer> getUserId(final String username) {
        return userSearch(username).map(new Function<UserSearch, Integer>() {
            @Override public Integer apply(UserSearch userSearch) throws Exception {
                if (userSearch.response.results.size() == 0){
                    return -1;
                } else if (userSearch.response.results.size() == 1){
                    if (userSearch.response.results.get(0).equals(username)) {
                        return userSearch.response.results.get(0).userId;
                    }
                    else {
                        return -1;
                    }
                }
                else {
                    for (UserSearch.Results name : userSearch.response.results) {
                        if (name.username.equals(username)) {
                            return name.userId;
                        }
                    }
                    return -1;
                }
            }
        });
    }

    public Single<TorrentBookmark> getTorrentBookmarks() {
        return mApiService.torrentBookmarks();
    }

    public Single<Conversation> getConversation(int id) {
        return mApiService.conversation(id);
    }

    public Single<ResponseBody> sendMessage(String mUser, String conversationId, String subject, String body) {
        return mApiService.sendMessage("takecompose", mUser, conversationId, subject, mPreferencesHelper.getAuth(), body);
    }

    public Observable<Object> flattenTorrents(final List<TorrentGroup.Torrents> torrents) {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                TreeMap<String, List<TorrentGroup.Torrents>> treeMap = new TreeMap<>();
                List<Object> flattenedMap = new ArrayList<>();
                for (TorrentGroup.Torrents group : torrents) {
                    if (!treeMap.containsKey(String.format("%s %s %s", group.remasterYear, group.remasterTitle, group.remasterRecordLabel))) {
                        List<TorrentGroup.Torrents> list = new ArrayList<>();
                        list.add(group);
                        treeMap.put(String.format(String.format("%s %s %s", group.remasterYear, group.remasterTitle, group.remasterRecordLabel)), list);
                    } else {
                        treeMap.get(String.format("%s %s %s", group.remasterYear, group.remasterTitle, group.remasterRecordLabel)).add(group);
                    }
                }

                for (String key : treeMap.keySet()) {
                    flattenedMap.add(key);
                    emitter.onNext(key);
                    for (TorrentGroup.Torrents group : treeMap.get(key)) {
                        flattenedMap.add(group);
                        emitter.onNext(group);
                    }
                }
                emitter.onComplete();
            }
        });
    }

    public Single<ResponseBody> removeForumSub(int topicId) {
        return mApiService.toggleForumSubscription(topicId, mPreferencesHelper.getAuth());
    }

    public Single<ResponseBody> addForumSub(int topicId) {
        return mApiService.toggleForumSubscription(topicId, mPreferencesHelper.getAuth());
    }

    public Single<TorrentComments> fetchTorrentComments(int id, int page) {
        return mApiService.torrentComments(id, page);
    }

    public Single<Index> loginWithApiKey() {
        return loadIndex();
    }
}

