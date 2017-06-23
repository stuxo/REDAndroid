package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sxo on 25/01/17.
 */

public class TorrentSearch {

    @SerializedName("status")
    public String status;
    @SerializedName("response")
    public Response response;

    public static class Artists {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("aliasid")
        public int aliasid;
    }

    public static class Torrents {
        @SerializedName("torrentId")
        public int torrentId;
        @SerializedName("editionId")
        public int editionId;
        @SerializedName("artists")
        public List<Artists> artists;
        @SerializedName("remastered")
        public boolean remastered;
        @SerializedName("remasterYear")
        public int remasterYear;
        @SerializedName("remasterCatalogueNumber")
        public String remasterCatalogueNumber;
        @SerializedName("remasterTitle")
        public String remasterTitle;
        @SerializedName("media")
        public String media;
        @SerializedName("encoding")
        public String encoding;
        @SerializedName("format")
        public String format;
        @SerializedName("hasLog")
        public boolean hasLog;
        @SerializedName("logScore")
        public int logScore;
        @SerializedName("hasCue")
        public boolean hasCue;
        @SerializedName("scene")
        public boolean scene;
        @SerializedName("vanityHouse")
        public boolean vanityHouse;
        @SerializedName("fileCount")
        public int fileCount;
        @SerializedName("time")
        public String time;
        @SerializedName("size")
        public long size;
        @SerializedName("snatches")
        public int snatches;
        @SerializedName("seeders")
        public int seeders;
        @SerializedName("leechers")
        public int leechers;
        @SerializedName("isFreeleech")
        public boolean isFreeleech;
        @SerializedName("isNeutralLeech")
        public boolean isNeutralLeech;
        @SerializedName("isPersonalFreeleech")
        public boolean isPersonalFreeleech;
        @SerializedName("canUseToken")
        public boolean canUseToken;
    }

    public static class Results {
        @SerializedName("groupId")
        public int groupId;
        @SerializedName("groupName")
        public String groupName;
        @SerializedName("artist")
        public String artist;
        @SerializedName("tags")
        public List<String> tags;
        @SerializedName("bookmarked")
        public boolean bookmarked;
        @SerializedName("vanityHouse")
        public boolean vanityHouse;
        @SerializedName("groupYear")
        public int groupYear;
        @SerializedName("releaseType")
        public String releaseType;
        @SerializedName("groupTime")
        public int groupTime;
        @SerializedName("maxSize")
        public long maxSize;
        @SerializedName("totalSnatched")
        public int totalSnatched;
        @SerializedName("totalSeeders")
        public int totalSeeders;
        @SerializedName("totalLeechers")
        public int totalLeechers;
        @SerializedName("torrents")
        public List<Torrents> torrents;
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
