package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by sxo on 24/05/17.
 */

public class Conversation {

    @SerializedName("status")
    public String status;
    @SerializedName("response")
    public Response response;

    public static class Messages {
        @SerializedName("messageId")
        public int messageId;
        @SerializedName("senderId")
        public int senderId;
        @SerializedName("senderName")
        public String senderName;
        @SerializedName("sentDate")
        public Date sentDate;
        @SerializedName("bbBody")
        public String bbBody;
        @SerializedName("body")
        public String body;
    }

    public static class Response {
        @SerializedName("convId")
        public int convId;
        @SerializedName("subject")
        public String subject;
        @SerializedName("sticky")
        public boolean sticky;
        @SerializedName("messages")
        public List<Messages> messages;
    }
}
