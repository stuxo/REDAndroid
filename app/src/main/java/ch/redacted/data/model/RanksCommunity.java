package ch.redacted.data.model;

/**
 * Created by sxo on 17/03/17.
 */

public class RanksCommunity {
    public RanksCommunity(Profile.Community community, Profile.Ranks ranks) {
        this.community = community;
        this.ranks = ranks;
    }

    public Profile.Community community;
    public Profile.Ranks ranks;
}
