package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by sxo on 20/01/17.
 */

public class TorrentComments {

    @SerializedName("status")
    public String status;
    @SerializedName("response")
    public Response response;

    public static class Userinfo {
        @SerializedName("authorId")
        public int authorId;
        @SerializedName("authorName")
        public String authorName;
        @SerializedName("artist")
        public boolean artist;
        @SerializedName("donor")
        public boolean donor;
        @SerializedName("warned")
        public boolean warned;
        @SerializedName("avatar")
        public String avatar;
        @SerializedName("enabled")
        public boolean enabled;
        @SerializedName("userTitle")
        public String userTitle;
    }

    public static class Comments {
        @SerializedName("postId")
        public int postId;
        @SerializedName("addedTime")
        public Date addedTime;
        @SerializedName("bbBody")
        public String bbBody;
        @SerializedName("body")
        public String body;
        @SerializedName("editedUserId")
        public int editedUserId;
        @SerializedName("editedTime")
        public String editedTime;
        @SerializedName("editedUsername")
        public String editedUsername;
        @SerializedName("userinfo")
        public Userinfo userinfo;
    }

    public static class Response {
        @SerializedName("page")
        public int page;
        @SerializedName("pages")
        public int pages;
        @SerializedName("comments")
        public List<Comments> comments;
    }
}
