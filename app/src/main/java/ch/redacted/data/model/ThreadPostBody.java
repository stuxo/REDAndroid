package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sxo on 16/01/17.
 */
public class ThreadPostBody {
    @SerializedName("forumid")
    private int forumId;
    @SerializedName("topicid")
    private int topicId;
    private String body;
    private String auth;

    public ThreadPostBody(int forumId, int topicId, String text, String auth) {
        this.forumId = forumId;
        this.topicId = topicId;
        this.body = text;
        this.auth = auth;
    }
}
