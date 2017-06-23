package ch.redacted.ui.artist;

import java.util.List;

import ch.redacted.data.model.Artist;
import ch.redacted.ui.base.MvpView;

public interface ArtistMvpView extends MvpView {

    void showArtist(Artist artist);

    void showLoadingProgress(boolean show);

    void showError(String message);

    void showBookmarked(boolean b);

    void showTorrents(List<Object> list);
}