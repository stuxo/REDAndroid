package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by sxo on 21/02/17.
 */

public class Subscription {

	@SerializedName("status") public String status;
	@SerializedName("response") public Response response;

	public static class Threads {
		@SerializedName("forumId") public int forumId;
		@SerializedName("forumName") public String forumName;
		@SerializedName("threadId") public int threadId;
		@SerializedName("threadTitle") public String threadTitle;
		@SerializedName("postId") public int postId;
		@SerializedName("lastPostId") public int lastPostId;
		@SerializedName("locked") public boolean locked;
		@SerializedName("new") public boolean mnew;
	}

	public static class Response {
		@SerializedName("threads") public List<Threads> threads;
	}
}
