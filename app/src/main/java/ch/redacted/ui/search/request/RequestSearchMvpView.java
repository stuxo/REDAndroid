package ch.redacted.ui.search.request;

import java.util.List;

import ch.redacted.data.model.RequestSearch;
import ch.redacted.ui.base.MvpView;

public interface RequestSearchMvpView extends MvpView {

    void showResults(List<RequestSearch.Results> results);

    void showResultsEmpty();

    void showError();

    void showProgress(boolean show);
}
