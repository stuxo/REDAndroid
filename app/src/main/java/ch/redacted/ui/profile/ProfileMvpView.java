package ch.redacted.ui.profile;

import java.util.Date;
import java.util.List;

import ch.redacted.data.model.Profile;
import ch.redacted.data.model.RanksCommunity;
import ch.redacted.data.model.Recents;
import ch.redacted.ui.base.MvpView;

public interface ProfileMvpView extends MvpView {

    //these fields cannot be hidden by paranoia
    void showUsername(String username);
    void showAvatar(String avatarUrl);
    void showUserClass(String userClass);
    void showJoinedDate(Date date);
    void showUserDescription(String description);
    void showUserDescriptionEmpty();

    void showProfileIsLoggedInUser(boolean isUser);

    void showLastSeen(Date lastSeen);
    void showLastSeenParanoid();

    void showRatio(double ratio, double requiredRatio);
    void showRatioParanoid();

    void showDownloaded(int downloadPercentile);
	void showDownloadedParanoid();

    void showUploaded(int uploadedPercentile);
    void showUploadedParanoid();

    void showNumUploads(int numUploadedPercentile);
    void showNumUploadsParanoid();

    void showRequestsFilled(int requestsPercentile);
    void showRequestsParanoid();

    void showBountySpent(int spentPercentile);
    void showBountySpentParanoid();

    void showNumForumPosts(int postsPercentile);
    void showNumForumPostsParanoid();

    void showArtistsAdded(int artistsAddedPercentile);
    void showArtistsAddedParanoid();

    void showOverallRank(int overallPercentile);
    void showOverallRankParanoid();

    void showInvitedCount();
    void showInvitedCountParanoid();

    void showRecentUploads(List<Recents.RecentTorrent> uploads);
    void showRecentUploadsEmpty(); //Paranoid

    void showRecentSnatches(List<Recents.RecentTorrent> snatches);
    void showRecentSnatchesEmpty(); //Paranoid

    void showLoadingProgress(boolean show);

    void showError(String message);

    void showDefaultAvatar();

    void showSnackbar(String message);
}