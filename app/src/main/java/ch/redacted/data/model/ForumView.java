package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by sxo on 24/12/16.
 */

public class ForumView
{
    @SerializedName("status")
    public String status;
    @SerializedName("response")
    public Response response;

    public static class SpecificRules {
    }

    public static class Threads {
        @SerializedName("topicId")
        public int topicId;
        @SerializedName("title")
        public String title;
        @SerializedName("authorId")
        public int authorId;
        @SerializedName("authorName")
        public String authorName;
        @SerializedName("locked")
        public boolean locked;
        @SerializedName("sticky")
        public boolean sticky;
        @SerializedName("postCount")
        public int postCount;
        @SerializedName("lastID")
        public int lastID;
        @SerializedName("lastTime")
        public Date lastTime;
        @SerializedName("lastAuthorId")
        public int lastAuthorId;
        @SerializedName("lastAuthorName")
        public String lastAuthorName;
        @SerializedName("lastReadPage")
        public int lastReadPage;
        @SerializedName("lastReadPostId")
        public int lastReadPostId;
        @SerializedName("read")
        public boolean read;
    }

    public static class Response {
        @SerializedName("forumName")
        public String forumName;
        @SerializedName("specificRules")
        public List<SpecificRules> specificRules;
        @SerializedName("currentPage")
        public int currentPage;
        @SerializedName("pages")
        public int pages;
        @SerializedName("threads")
        public List<Threads> threads;
    }
}
