package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sxo on 16/02/17.
 */

public class Artist {

    @SerializedName("status")
    public String status;
    @SerializedName("response")
    public Response response;

    public static class Tags implements Comparable<Tags> {
        @SerializedName("name")
        public String name;
        @SerializedName("count")
        public int count;

        @Override
        public int compareTo(Tags tags) {
            if (this.count > tags.count)
                return -1;
            else if (this.count == tags.count){
                return 0;
            }
            else {
                return 1;
            }
        }
    }

    public static class SimilarArtists {
    }

    public static class Statistics {
        @SerializedName("numGroups")
        public int numGroups;
        @SerializedName("numTorrents")
        public int numTorrents;
        @SerializedName("numSeeders")
        public int numSeeders;
        @SerializedName("numLeechers")
        public int numLeechers;
        @SerializedName("numSnatches")
        public int numSnatches;
    }

    public static class Torrent {
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

    public static class Torrentgroup {
        @SerializedName("groupId")
        public int groupId;
        @SerializedName("groupName")
        public String groupName;
        @SerializedName("groupYear")
        public int groupYear;
        @SerializedName("groupRecordLabel")
        public String groupRecordLabel;
        @SerializedName("groupCatalogueNumber")
        public String groupCatalogueNumber;
        @SerializedName("tags")
        public List<String> tags;
        @SerializedName("releaseType")
        public int releaseType;
        @SerializedName("groupVanityHouse")
        public boolean groupVanityHouse;
        @SerializedName("hasBookmarked")
        public boolean hasBookmarked;
        @SerializedName("torrent")
        public List<Torrent> torrent;
    }

    public static class Requests {
        @SerializedName("requestId")
        public int requestId;
        @SerializedName("categoryId")
        public int categoryId;
        @SerializedName("title")
        public String title;
        @SerializedName("year")
        public int year;
        @SerializedName("timeAdded")
        public String timeAdded;
        @SerializedName("votes")
        public int votes;
        @SerializedName("bounty")
        public long bounty;
    }

    public static class Response {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("notificationsEnabled")
        public boolean notificationsEnabled;
        @SerializedName("hasBookmarked")
        public boolean hasBookmarked;
        @SerializedName("image")
        public String image;
        @SerializedName("body")
        public String body;
        @SerializedName("vanityHouse")
        public boolean vanityHouse;
        @SerializedName("tags")
        public List<Tags> tags;
        @SerializedName("similarArtists")
        public List<SimilarArtists> similarArtists;
        @SerializedName("statistics")
        public Statistics statistics;
        @SerializedName("torrentgroup")
        public List<Torrentgroup> torrentgroup;
        @SerializedName("requests")
        public List<Requests> requests;
    }
}
