package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sxo on 16/02/17.
 */

public class RequestSearch {

    @SerializedName("status")
    public String status;
    @SerializedName("response")
    public Response response;

    public static class Results {
        @SerializedName("requestId")
        public int requestId;
        @SerializedName("requestorId")
        public int requestorId;
        @SerializedName("requestorName")
        public String requestorName;
        @SerializedName("timeAdded")
        public String timeAdded;
        @SerializedName("lastVote")
        public String lastVote;
        @SerializedName("voteCount")
        public int voteCount;
        @SerializedName("bounty")
        public long bounty;
        @SerializedName("categoryId")
        public int categoryId;
        @SerializedName("categoryName")
        public String categoryName;
        @SerializedName("artists")
        public List<List<Artists>> artists;
        @SerializedName("title")
        public String title;
        @SerializedName("year")
        public int year;
        @SerializedName("image")
        public String image;
        @SerializedName("description")
        public String description;
        @SerializedName("recordLabel")
        public String recordLabel;
        @SerializedName("catalogueNumber")
        public String catalogueNumber;
        @SerializedName("releaseType")
        public String releaseType;
        @SerializedName("bitrateList")
        public String bitrateList;
        @SerializedName("formatList")
        public String formatList;
        @SerializedName("mediaList")
        public String mediaList;
        @SerializedName("logCue")
        public String logCue;
        @SerializedName("isFilled")
        public boolean isFilled;
        @SerializedName("fillerId")
        public int fillerId;
        @SerializedName("fillerName")
        public String fillerName;
        @SerializedName("torrentId")
        public int torrentId;
        @SerializedName("timeFilled")
        public String timeFilled;
    }

    public static class Response {
        @SerializedName("currentPage")
        public int currentPage;
        @SerializedName("pages")
        public int pages;
        @SerializedName("results")
        public List<Results> results;
    }

    public static class Artists {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
    }
}
