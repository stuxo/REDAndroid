package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by sxo on 19/01/17.
 */

public class TorrentGroup {

    @SerializedName("status")
    public String status;
    @SerializedName("response")
    public Response response;

    public static class Composers {
    }

    public static class Dj {
    }

    public static class Artists {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
    }

    public static class With {
    }

    public static class Conductor {
    }

    public static class RemixedBy {
    }

    public static class Producer {
    }

    public static class MusicInfo {
        @SerializedName("composers")
        public List<Composers> composers;
        @SerializedName("dj")
        public List<Dj> dj;
        @SerializedName("artists")
        public List<Artists> artists;
        @SerializedName("with")
        public List<With> with;
        @SerializedName("conductor")
        public List<Conductor> conductor;
        @SerializedName("remixedBy")
        public List<RemixedBy> remixedBy;
        @SerializedName("producer")
        public List<Producer> producer;
    }

    public static class Group {
        @SerializedName("wikiBody")
        public String wikiBody;
        @SerializedName("wikiImage")
        public String wikiImage;
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
        @SerializedName("releaseType")
        public int releaseType;
        @SerializedName("categoryId")
        public int categoryId;
        @SerializedName("categoryName")
        public String categoryName;
        @SerializedName("time")
        public String time;
        @SerializedName("vanityHouse")
        public boolean vanityHouse;
        @SerializedName("isBookmarked")
        public boolean isBookmarked;
        @SerializedName("musicInfo")
        public MusicInfo musicInfo;
        @SerializedName("tags")
        public List<String> tags;
    }

    public static class Torrents {
        @SerializedName("id")
        public int id;
        @SerializedName("media")
        public String media;
        @SerializedName("format")
        public String format;
        @SerializedName("encoding")
        public String encoding;
        @SerializedName("remastered")
        public boolean remastered;
        @SerializedName("remasterYear")
        public int remasterYear;
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
        @SerializedName("size")
        public long size;
        @SerializedName("seeders")
        public int seeders;
        @SerializedName("leechers")
        public int leechers;
        @SerializedName("snatched")
        public int snatched;
        @SerializedName("freeTorrent")
        public boolean freeTorrent;
        @SerializedName("reported")
        public boolean reported;
        @SerializedName("time")
        public Date time;
        @SerializedName("description")
        public String description;
        @SerializedName("fileList")
        public String fileList;
        @SerializedName("filePath")
        public String filePath;
        @SerializedName("userId")
        public int userId;
        @SerializedName("username")
        public String username;
    }

    public static class Response {
        @SerializedName("group")
        public Group group;
        @SerializedName("torrents")
        public List<Torrents> torrents;
    }
}