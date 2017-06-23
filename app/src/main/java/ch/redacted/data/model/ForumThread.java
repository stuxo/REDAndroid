package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

/**
 * Created by sxo on 24/12/16.
 */

public class ForumThread {

    @SerializedName("status") public String status;
    @SerializedName("response") public Response response;

    public static class Answers {
        @SerializedName("answer") public String answer;
        @SerializedName("ratio") public double ratio;
        @SerializedName("percent") public double percent;
    }

    public static class Poll {
        @SerializedName("closed") public boolean closed;
        @SerializedName("featured") public String featured;
        @SerializedName("question") public String question;
        @SerializedName("maxVotes") public int maxVotes;
        @SerializedName("totalVotes") public int totalVotes;
        @SerializedName("voted") public boolean voted;
        @SerializedName("answers") public List<Answers> answers;
    }

    public static class Author {
        @SerializedName("authorId") public int authorId;
        @SerializedName("authorName") public String authorName;
        @SerializedName("paranoia") public List<String> paranoia;
        @SerializedName("artist") public boolean artist;
        @SerializedName("donor") public boolean donor;
        @SerializedName("warned") public boolean warned;
        @SerializedName("avatar") public String avatar;
        @SerializedName("enabled") public boolean enabled;
        @SerializedName("userTitle") public String userTitle;
    }

    public static class Posts {
        @SerializedName("postId") public int postId;
        @SerializedName("addedTime") public Date addedTime;
        @SerializedName("bbBody") public String bbBody;
        @SerializedName("body") public String body;
        @SerializedName("editedUserId") public int editedUserId;
        @SerializedName("editedTime") public String editedTime;
        @SerializedName("editedUsername") public String editedUsername;
        @SerializedName("author") public Author author;
    }

    public static class Response {
        @SerializedName("forumId") public int forumId;
        @SerializedName("forumName") public String forumName;
        @SerializedName("threadId") public int threadId;
        @SerializedName("threadTitle") public String threadTitle;
        @SerializedName("subscribed") public boolean subscribed;
        @SerializedName("locked") public boolean locked;
        @SerializedName("sticky") public boolean sticky;
        @SerializedName("currentPage") public int currentPage;
        @SerializedName("pages") public int pages;
        @SerializedName("poll") public Poll poll;
        @SerializedName("posts") public List<Posts> posts;
    }
}
