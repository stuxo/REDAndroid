package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Collage {

    @SerializedName("status")
    public String status;

    @SerializedName("response")
    public Response response;

    public static class Response {
        @SerializedName("id")
        public Integer id;

        @SerializedName("name")
        public String name;

        @SerializedName("description")
        public String description;

        @SerializedName("creatorID")
        public Integer creatorID;

        @SerializedName("deleted")
        public Boolean deleted;

        @SerializedName("collageCategoryID")
        public Integer collageCategoryID;

        @SerializedName("collageCategoryName")
        public String collageCategoryName;

        @SerializedName("locked")
        public Boolean locked;

        @SerializedName("maxGroups")
        public Integer maxGroups;

        @SerializedName("maxGroupsPerUser")
        public Integer maxGroupsPerUser;

        @SerializedName("hasBookmarked")
        public Boolean hasBookmarked;

        @SerializedName("subscriberCount")
        public Integer subscriberCount;

        @SerializedName("torrentGroupIDList")
        public List<Integer> torrentGroupIDList;

        @SerializedName("torrentgroups")
        public List<TorrentGroup> torrentGroups;


        public static class TorrentGroup {
            @SerializedName("id")
            public Integer id;

            @SerializedName("name")
            public String name;

            @SerializedName("year")
            public Integer year;

            @SerializedName("categoryId")
            public Integer categoryId;

            @SerializedName("recordLabel")
            public String recordLabel;

            @SerializedName("catalogueNumber")
            public String catalogueNumber;

            @SerializedName("vanityHouse")
            public Integer vanityHouse;

            @SerializedName("tagList")
            public String tagList;

            @SerializedName("releaseType")
            public Integer releaseType;

            @SerializedName("wikiImage")
            public String wikiImage;

            @SerializedName("musicInfo")
            ch.redacted.data.model.TorrentGroup.MusicInfo musicInfo;

            // https://stackoverflow.com/a/3236616
            private static String concatArtistsWithCommas(List<ch.redacted.data.model.TorrentGroup.Artists> artists) {
                StringBuilder wordList = new StringBuilder();
                for (ch.redacted.data.model.TorrentGroup.Artists artist : artists) {
                    wordList.append(artist.name).append(",");
                }
                return new String(wordList.deleteCharAt(wordList.length() - 1));
            }

            // @todo this should get a proper treatment
            public String getArtistName() {
                if (musicInfo.artists.size() > 0) {
                    return concatArtistsWithCommas(musicInfo.artists);
                } else {
                    return "Unknown Artist";
                }
            }
        }

    }

}
