package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by sxo on 5/01/17.
 */

public class Top10 {

	@SerializedName("status") public String status;
	@SerializedName("response") public List<Response> response;

	public static class Results {
		@SerializedName("torrentId") public int torrentId;
		@SerializedName("groupId") public int groupId;
		@SerializedName("artist") public String artist;
		@SerializedName("groupName") public String groupName;
		@SerializedName("groupCategory") public int groupCategory;
		@SerializedName("groupYear") public int groupYear;
		@SerializedName("remasterTitle") public String remasterTitle;
		@SerializedName("format") public String format;
		@SerializedName("encoding") public String encoding;
		@SerializedName("hasLog") public boolean hasLog;
		@SerializedName("hasCue") public boolean hasCue;
		@SerializedName("media") public String media;
		@SerializedName("scene") public boolean scene;
		@SerializedName("year") public int year;
		@SerializedName("tags") public List<String> tags;
		@SerializedName("snatched") public int snatched;
		@SerializedName("seeders") public int seeders;
		@SerializedName("leechers") public int leechers;
		@SerializedName("data") public String data;
		@SerializedName("wikiImage") public String wikiImage;
	}

	public static class Response {
		@SerializedName("caption") public String caption;
		@SerializedName("tag") public String tag;
		@SerializedName("limit") public int limit;
		@SerializedName("results") public List<Results> results;
	}
}
