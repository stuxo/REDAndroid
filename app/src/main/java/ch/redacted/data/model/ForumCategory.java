package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sxo on 24/12/16.
 */

public class ForumCategory {

    @SerializedName("status")
    public String status;
    @SerializedName("response")
    public Response response;

    public static class Forums {
        @SerializedName("forumId")
        public int forumId;
        @SerializedName("forumName")
        public String forumName;
        @SerializedName("forumDescription")
        public String forumDescription;
        @SerializedName("numTopics")
        public int numTopics;
        @SerializedName("numPosts")
        public int numPosts;
        @SerializedName("lastPostId")
        public int lastPostId;
        @SerializedName("lastAuthorId")
        public int lastAuthorId;
        @SerializedName("lastPostAuthorName")
        public String lastPostAuthorName;
        @SerializedName("lastTopicId")
        public int lastTopicId;
        @SerializedName("lastTime")
        public String lastTime;
        @SerializedName("lastTopic")
        public String lastTopic;
        @SerializedName("read")
        public boolean read;
        @SerializedName("locked")
        public boolean locked;
        @SerializedName("sticky")
        public boolean sticky;
    }

    public static class Categories {
        @SerializedName("categoryID")
        public int categoryID;
        @SerializedName("categoryName")
        public String categoryName;
        @SerializedName("forums")
        public List<Forums> forums;
    }

    public static class Response {
        @SerializedName("categories")
        public List<Categories> categories;
    }
}
