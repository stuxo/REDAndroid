package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sxo on 6/03/17.
 */

public class Request {


	@SerializedName("status")
	public String status;
	@SerializedName("response")
	public Response response;

	public static class TopContributors {
		@SerializedName("userId")
		public int userId;
		@SerializedName("userName")
		public String userName;
		@SerializedName("bounty")
		public long bounty;
	}

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

	public static class Comments {
	}

	public static class Response {
		@SerializedName("requestId")
		public int requestId;
		@SerializedName("requestorId")
		public int requestorId;
		@SerializedName("requestorName")
		public String requestorName;
		@SerializedName("isBookmarked")
		public boolean isBookmarked;
		@SerializedName("requestTax")
		public double requestTax;
		@SerializedName("timeAdded")
		public String timeAdded;
		@SerializedName("canEdit")
		public boolean canEdit;
		@SerializedName("canVote")
		public boolean canVote;
		@SerializedName("minimumVote")
		public int minimumVote;
		@SerializedName("voteCount")
		public int voteCount;
		@SerializedName("lastVote")
		public String lastVote;
		@SerializedName("topContributors")
		public List<TopContributors> topContributors;
		@SerializedName("totalBounty")
		public long totalBounty;
		@SerializedName("categoryId")
		public int categoryId;
		@SerializedName("categoryName")
		public String categoryName;
		@SerializedName("title")
		public String title;
		@SerializedName("year")
		public int year;
		@SerializedName("image")
		public String image;
		@SerializedName("bbDescription")
		public String bbDescription;
		@SerializedName("description")
		public String description;
		@SerializedName("musicInfo")
		public MusicInfo musicInfo;
		@SerializedName("catalogueNumber")
		public String catalogueNumber;
		@SerializedName("releaseType")
		public int releaseType;
		@SerializedName("releaseName")
		public String releaseName;
		@SerializedName("bitrateList")
		public List<String> bitrateList;
		@SerializedName("formatList")
		public List<String> formatList;
		@SerializedName("mediaList")
		public List<String> mediaList;
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
		@SerializedName("tags")
		public List<String> tags;
		@SerializedName("comments")
		public List<Comments> comments;
		@SerializedName("commentPage")
		public int commentPage;
		@SerializedName("commentPages")
		public int commentPages;
		@SerializedName("recordLabel")
		public String recordLabel;
		@SerializedName("oclc")
		public String oclc;
	}
}
