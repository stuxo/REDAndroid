package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by sxo on 23/12/16.
 */

public class Announcement {

    @SerializedName("status")
    public String status;
    @SerializedName("response")
    public Response response;

    public static class Announcements {
        @SerializedName("newsId")
        public int newsId;
        @SerializedName("title")
        public String title;
        @SerializedName("bbBody")
        public String bbBody;
        @SerializedName("body")
        public String body;
        @SerializedName("newsTime")
        public Date newsTime;


    }

    public static class BlogPosts {
        @SerializedName("blogId")
        public int blogId;
        @SerializedName("author")
        public String author;
        @SerializedName("title")
        public String title;
        @SerializedName("bbBody")
        public String bbBody;
        @SerializedName("body")
        public String body;
        @SerializedName("blogTime")
        public String blogTime;
        @SerializedName("threadId")
        public int threadId;
    }

    public static class Response {
        @SerializedName("announcements")
        public List<Announcements> announcements;
        @SerializedName("blogPosts")
        public List<BlogPosts> blogPosts;
    }

    public static final class AnnouncementBuilder {
        public String status;
        public Announcement.Response response;

        private AnnouncementBuilder() {
        }

        public static Announcement.AnnouncementBuilder aAnnouncement() {
            return new AnnouncementBuilder();
        }

        public Announcement.AnnouncementBuilder withStatus(String status) {
            this.status = status;
            return this;
        }

        public Announcement.AnnouncementBuilder withResponse(Announcement.Response response) {
            this.response = response;
            return this;
        }

        public Announcement build() {
            Announcement announcement = new Announcement();
            announcement.response = this.response;
            announcement.status= this.status;
            return announcement;
        }
    }
}
