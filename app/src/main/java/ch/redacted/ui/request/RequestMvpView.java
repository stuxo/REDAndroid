package ch.redacted.ui.request;

import ch.redacted.data.model.Request;
import ch.redacted.ui.base.MvpView;

public interface RequestMvpView extends MvpView {

    void showRequest(Request request);

    void showLoadingProgress(boolean show);

    void showError(String message);

    void showBookmarked(boolean b);
}