package ch.redacted.ui.forum.thread;

import ch.redacted.data.model.ForumThread;
import ch.redacted.ui.base.MvpView;

public interface ThreadMvpView extends MvpView {

    void showSubscribed(boolean subscribed);

    void showPosts(ForumThread threads, boolean scrollToTop);

    void showPostsEmpty();

    void showError();

    void showProgress(boolean show);

    void showPostSuccessful();

    void showPostFailed();
}
