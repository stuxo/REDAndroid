package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CollageSearch {
    @SerializedName("status")
    public String status;

    @SerializedName("response")
    public List<Response> response;

    public static class Response {
        @SerializedName("id")
        public int id;

        @SerializedName("name")
        public String name;

        @SerializedName("creatorID")
        public int creatorID;

        @SerializedName("collageCategoryID")
        public int collageCategoryID;

        @SerializedName("collageCategoryName")
        public String collageCategoryName;

        @SerializedName("hasBookmarked")
        public Boolean hasBookmarked;

        @SerializedName("subscriberCount")
        public int subscriberCount;

        @SerializedName("lastUpdated")
        public String lastUpdated;

        @SerializedName("tagList")
        public String tagList;
    }
}
