package ch.redacted.app.test.common;

import io.reactivex.Single;
import ch.redacted.data.model.Announcement;
import ch.redacted.data.model.Artist;
import ch.redacted.data.model.Conversation;
import ch.redacted.data.model.ForumCategory;
import ch.redacted.data.model.ForumThread;
import ch.redacted.data.model.ForumView;
import ch.redacted.data.model.Conversations;
import ch.redacted.data.model.Index;
import ch.redacted.data.model.Profile;
import ch.redacted.data.model.Recents;
import ch.redacted.data.model.Request;
import ch.redacted.data.model.RequestSearch;
import ch.redacted.data.model.Subscription;
import ch.redacted.data.model.Top10;
import ch.redacted.data.model.TorrentBookmark;
import ch.redacted.data.model.TorrentGroup;
import ch.redacted.data.model.TorrentSearch;
import ch.redacted.data.model.UserSearch;
import ch.redacted.data.remote.ApiService;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.Query;

/**
 * Created by sxo on 21/12/16.
 */

public class FailedMockApiService implements ApiService {
	private static final String TAG = "MockFailedApiService";

	@Override public Single<Response<ResponseBody>> login(@Field("username") String username,
		@Field("password") String password, @Field("keeplogged") int keepLogged) {
		return null;
	}

	@Override public Single<Index> index() {
		return null;
	}

	@Override public Single<Profile> profile(@Query("id") int userId, @Query("auth") String auth) {
		return null;
	}

	@Override
	public Single<Index> cookieCheck() {
		return null;
	}

	@Override
	public Single<Announcement> announcements() {
		return null;
	}

	@Override
	public Single<ForumCategory> forumCategories() {
		return null;
	}

	@Override
	public Single<ForumView> forumThreads(@Query("forumid") int forumId, @Query("page") int page) {
		return null;
	}

	@Override
	public Single<ForumThread> threadPosts(@Query("threadid") int threadId, @Query("postid") Integer postId, @Query("page") Integer page) {
		return null;
	}

	@Override
	public Single<Top10> top10(@Query("details") String details, @Query("limit") int limit, @Query("type") String type) {
		return null;
	}

	@Override
	public Single<TorrentGroup> release(@Query("id") int id) {
		return null;
	}

	@Override
	public Single<TorrentGroup> torrentComments(@Query("id") int id) {
		return null;
	}

	@Override
	public Single<Conversations> inbox(@Query("sort") String sort, @Query("type") String type) {
		return null;
	}

	@Override
	public Single<UserSearch> userSearch(@Query("search") String search) {
		return null;
	}

	@Override
	public Single<TorrentSearch> torrentSearch(@Query("searchstr") String search) {
		return null;
	}

	@Override
	public Single<RequestSearch> requestSearch(@Query("search") String search) {
		return null;
	}

	@Override
	public Single<Artist> artist(@Query("artistname") String name, @Query("artistrelease") boolean artistReleasesOnly) {
		return null;
	}

	@Override
	public Single<Artist> artist(@Query("id") int id, @Query("artistrelease") boolean artistReleasesOnly) {
		return null;
	}

	@Override
	public Single<ResponseBody> toggleArtistBookmark(@Query("action") String action, @Query("artistid") int artistId, @Query("auth") String auth) {
		return null;
	}

	@Override
	public Single<ResponseBody> newThreadPost(@Field("action") String action, @Field("forum") int forumId, @Field("thread") int threadId, @Field("body") String body, @Query("auth") String auth) {
		return null;
	}

	@Override
	public Single<ResponseBody> toggleBookmark(@Query("action") String action, @Query("type") String type, @Query("auth") String auth, @Query("id") int id) {
		return null;
	}

	@Override
	public Single<Subscription> subscriptions(@Query("showunread") boolean showUnread) {
		return null;
	}

	@Override
	public Single<Request> request(@Query("id") int id) {
		return null;
	}

	@Override
	public Single<Response<ResponseBody>> file(@Query("id") int id, @Query("authkey") String auth,
		@Query("torrent_pass") String pass) {
		return null;
	}

	@Override public Single<Recents> recents(@Query("userid") int id, @Query("authkey") String auth,
		@Query("limit") int limit) {
		return null;
	}

	@Override
	public Single<TorrentBookmark> torrentBookmarks() {
		return null;
	}

	@Override
	public Single<Conversations> inbox() {
		return null;
	}

	@Override
	public Single<Conversation> conversation(@Query("id") int id) {
		return null;
	}

	@Override
	public Single<ResponseBody> sendMessage(@Field("action") String action, @Field("toid") String toId, @Field("convid") String convId, @Field("subject") String subject, @Field("auth") String auth, @Field("body") String body) {
		return null;
	}
}