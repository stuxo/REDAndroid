package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sxo on 22/01/17.
 */

public class UserSearch {

    @SerializedName("status")
    public String status;
    @SerializedName("response")
    public Response response;

    public static class Results {
        @SerializedName("userId")
        public int userId;
        @SerializedName("username")
        public String username;
        @SerializedName("donor")
        public boolean donor;
        @SerializedName("warned")
        public boolean warned;
        @SerializedName("enabled")
        public boolean enabled;
        @SerializedName("class")
        public String mclass;
    }

    public static class Response {
        @SerializedName("currentPage")
        public int currentPage;
        @SerializedName("pages")
        public int pages;
        @SerializedName("results")
        public List<Results> results;
    }
}
