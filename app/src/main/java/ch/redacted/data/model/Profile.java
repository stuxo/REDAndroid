package ch.redacted.data.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by sxo on 18/12/16.
 */

public class Profile {

    @SerializedName("status")
    public String status;
    @SerializedName("response")
    public Response response;

    public static class Stats {
        @SerializedName("joinedDate")
        public Date joinedDate;
        @SerializedName("lastAccess")
        public String lastAccess;
        @SerializedName("uploaded")
        public Long uploaded;
        @SerializedName("downloaded")
        public Long downloaded;
        @SerializedName("ratio")
        public Double ratio;
        @SerializedName("requiredRatio")
        public Double requiredRatio;
    }

    public static class Ranks {
        @SerializedName("uploaded")
        public Integer uploaded;
        @SerializedName("downloaded")
        public Integer downloaded;
        @SerializedName("uploads")
        public Integer uploads;
        @SerializedName("requests")
        public Integer requests;
        @SerializedName("bounty")
        public Integer bounty;
        @SerializedName("posts")
        public Integer posts;
        @SerializedName("artists")
        public Integer artists;
        @SerializedName("overall")
        public Integer overall;
    }

    public static class Personal {
        @SerializedName("class")
        public String mclass;
        @SerializedName("paranoia")
        public int paranoia;
        @SerializedName("paranoiaText")
        public String paranoiaText;
        @SerializedName("donor")
        public boolean donor;
        @SerializedName("warned")
        public boolean warned;
        @SerializedName("enabled")
        public boolean enabled;
        @SerializedName("passkey")
        public String passkey;
    }

    public static class Community {
        @SerializedName("posts")
        public Integer posts;
        @SerializedName("torrentComments")
        public Integer torrentComments;
        @SerializedName("collagesStarted")
        public Integer collagesStarted;
        @SerializedName("collagesContrib")
        public Integer collagesContrib;
        @SerializedName("requestsFilled")
        public Integer requestsFilled;
        @SerializedName("requestsVoted")
        public Integer requestsVoted;
        @SerializedName("perfectFlacs")
        public Integer perfectFlacs;
        @SerializedName("uploaded")
        public Integer uploaded;
        @SerializedName("groups")
        public Integer groups;
        @SerializedName("seeding")
        public Integer seeding;
        @SerializedName("leeching")
        public Integer leeching;
        @SerializedName("snatched")
        public Integer snatched;
        @SerializedName("invited")
        public Integer invited;
    }

    public static class Response {
        @SerializedName("username")
        public String username;
        @SerializedName("avatar")
        public String avatar;
        @SerializedName("isFriend")
        public boolean isFriend;
        @SerializedName("profileText")
        public String profileText;
        @SerializedName("stats")
        public Stats stats;
        @SerializedName("ranks")
        public Ranks ranks;
        @SerializedName("personal")
        public Personal personal;
        @SerializedName("community")
        public Community community;

    }

    public static final class ProfileBuilder {
        public String status;
        public Response response;

        private ProfileBuilder() {
        }

        public static ProfileBuilder aProfile() {
            return new ProfileBuilder();
        }

        public ProfileBuilder withStatus(String status) {
            this.status = status;
            return this;
        }

        public ProfileBuilder withResponse(Response response) {
            this.response = response;
            return this;
        }

        public Profile build() {
            Profile profile = new Profile();
            profile.response = this.response;
            profile.status = this.status;
            return profile;
        }
    }

    public String serializeToJson() {
        return new Gson().toJson(this);
    }

    public Profile deserializeFromJson(String jsonString) {
        return new Gson().fromJson(jsonString, Profile.class);
    }
}
