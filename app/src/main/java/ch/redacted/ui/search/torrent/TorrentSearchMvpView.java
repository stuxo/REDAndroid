package ch.redacted.ui.search.torrent;

import java.util.List;

import ch.redacted.data.model.TorrentSearch;
import ch.redacted.ui.base.MvpView;

public interface TorrentSearchMvpView extends MvpView {

    void showResults(List<TorrentSearch.Results> results);

    void showResultsEmpty();

    void showError();

    void showProgress(boolean show);
}
