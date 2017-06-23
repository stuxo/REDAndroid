package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by sxo on 22/01/17.
 */

public class Conversations {

    @SerializedName("status")
    public String status;
    @SerializedName("response")
    public Response response;

    public static class Messages {
        @SerializedName("convId")
        public int convId;
        @SerializedName("subject")
        public String subject;
        @SerializedName("unread")
        public boolean unread;
        @SerializedName("sticky")
        public boolean sticky;
        @SerializedName("forwardedId")
        public int forwardedId;
        @SerializedName("forwardedName")
        public String forwardedName;
        @SerializedName("senderId")
        public int senderId;
        @SerializedName("username")
        public String username;
        @SerializedName("donor")
        public boolean donor;
        @SerializedName("warned")
        public boolean warned;
        @SerializedName("enabled")
        public boolean enabled;
        @SerializedName("date")
        public Date date;
    }

    public static class Response {
        @SerializedName("currentPage")
        public int currentPage;
        @SerializedName("pages")
        public int pages;
        @SerializedName("messages")
        public List<Messages> messages;
    }
}
