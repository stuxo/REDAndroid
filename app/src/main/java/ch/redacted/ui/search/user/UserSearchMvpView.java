package ch.redacted.ui.search.user;

import java.util.List;

import ch.redacted.data.model.UserSearch;
import ch.redacted.ui.base.MvpView;

public interface UserSearchMvpView extends MvpView {

    void showResults(List<UserSearch.Results> results);

    void showResultsEmpty();

    void showError();

    void showProgress(boolean show);
}
