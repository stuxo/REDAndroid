package ch.redacted.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Recents {

	public Response response;
	private String status;
	private String error;

	public class Response {
		public List<RecentTorrent> snatches, uploads;
	}

	public class RecentTorrent {
		@SerializedName("ID")
		public int id;
		@SerializedName("Name")
		public String name;
		@SerializedName("WikiImage")
		public String wikiImage;
		List<ArtistInfo> artists;

		//Returns the main artist
		public List<RecentArtist> getArtists(){
			return artists.get(0).artists;
		}

		public class ArtistInfo {
			@SerializedName("1")
			public List<RecentArtist> artists;
		}
	}

	public class RecentArtist {
		public String id;
		public int artistID = -1;
		public String name;
	}
}

