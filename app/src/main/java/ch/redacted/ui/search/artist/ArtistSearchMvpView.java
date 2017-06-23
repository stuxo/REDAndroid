package ch.redacted.ui.search.artist;

import ch.redacted.data.model.Artist;
import ch.redacted.ui.base.MvpView;

public interface ArtistSearchMvpView extends MvpView {

    void showResults(Artist.Response artist);

    void showResultsEmpty();

    void showError();
}
