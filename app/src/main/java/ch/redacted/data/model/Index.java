package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sxo on 20/12/16.
 */

public class Index {
    @SerializedName("status")
    public String status;
    @SerializedName("response")
    public Response response;

    public static class Notifications {
        @SerializedName("messages")
        public int messages;
        @SerializedName("notifications")
        public int notifications;
        @SerializedName("newAnnouncement")
        public boolean newAnnouncement;
        @SerializedName("newBlog")
        public boolean newBlog;
        @SerializedName("newSubscriptions")
        public boolean newSubscriptions;
    }

    public static class Userstats {
        @SerializedName("uploaded")
        public String uploaded;
        @SerializedName("downloaded")
        public String downloaded;
        @SerializedName("ratio")
        public double ratio;
        @SerializedName("requiredratio")
        public double requiredratio;
        @SerializedName("class")
        public String mclass;
    }

    public static class Response {
        @SerializedName("username")
        public String username;
        @SerializedName("id")
        public int id;
        @SerializedName("authkey")
        public String authkey;
        @SerializedName("passkey")
        public String passkey;
        @SerializedName("notifications")
        public Notifications notifications;
        @SerializedName("userstats")
        public Userstats userstats;
    }
}
