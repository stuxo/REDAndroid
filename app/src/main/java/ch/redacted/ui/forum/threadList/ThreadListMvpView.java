package ch.redacted.ui.forum.threadList;

import ch.redacted.ui.base.MvpView;
import ch.redacted.data.model.ForumView;

public interface ThreadListMvpView extends MvpView {

    void showThreads(ForumView threads);

    void showThreadsEmpty();

    void showError();

    void showProgress(boolean show);
}
