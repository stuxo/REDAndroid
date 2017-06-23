package ch.redacted.ui.bookmark;

import java.util.List;

import ch.redacted.data.model.TorrentBookmark;
import ch.redacted.ui.base.MvpView;

public interface BookmarkMvpView extends MvpView {

    void showBookmarks(List<TorrentBookmark.Bookmarks> bookmark);

//    void showBookmarks(ArtistBookmark bookmark);

    void showBookmarksEmpty();

    void showError();

    void showProgress(boolean show);
}
