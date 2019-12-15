package ch.redacted.ui.search.collage;

import java.util.List;

import ch.redacted.data.model.CollageSearch;
import ch.redacted.ui.base.MvpView;

public interface CollageSearchMvpView extends MvpView {

    void showResults(List<CollageSearch.Response> results);

    void showResultsEmpty();

    void showError();

    void showProgress(boolean show);
}
