package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sxo on 30/04/17.
 */

public class TorrentBookmark {

    @SerializedName("status")
    public String status;
    @SerializedName("response")
    public Response response;

    public static class Torrents {
        @SerializedName("id")
        public int id;
        @SerializedName("groupId")
        public int groupId;
        @SerializedName("media")
        public String media;
        @SerializedName("format")
        public String format;
        @SerializedName("encoding")
        public String encoding;
        @SerializedName("remasterYear")
        public int remasterYear;
        @SerializedName("remastered")
        public boolean remastered;
        @SerializedName("remasterTitle")
        public String remasterTitle;
        @SerializedName("remasterRecordLabel")
        public String remasterRecordLabel;
        @SerializedName("remasterCatalogueNumber")
        public String remasterCatalogueNumber;
        @SerializedName("scene")
        public boolean scene;
        @SerializedName("hasLog")
        public boolean hasLog;
        @SerializedName("hasCue")
        public boolean hasCue;
        @SerializedName("logScore")
        public int logScore;
        @SerializedName("fileCount")
        public int fileCount;
        @SerializedName("freeTorrent")
        public boolean freeTorrent;
        @SerializedName("size")
        public long size;
        @SerializedName("leechers")
        public int leechers;
        @SerializedName("seeders")
        public int seeders;
        @SerializedName("snatched")
        public int snatched;
        @SerializedName("time")
        public String time;
        @SerializedName("hasFile")
        public int hasFile;
    }

    public static class Bookmarks {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("year")
        public int year;
        @SerializedName("recordLabel")
        public String recordLabel;
        @SerializedName("catalogueNumber")
        public String catalogueNumber;
        @SerializedName("tagList")
        public String tagList;
        @SerializedName("releaseType")
        public String releaseType;
        @SerializedName("vanityHouse")
        public boolean vanityHouse;
        @SerializedName("image")
        public String image;
        @SerializedName("torrents")
        public List<Torrents> torrents;
    }

    public static class Response {
        @SerializedName("bookmarks")
        public List<Bookmarks> bookmarks;
    }
}
