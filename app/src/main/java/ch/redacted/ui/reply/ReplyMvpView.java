package ch.redacted.ui.reply;

import ch.redacted.ui.base.MvpView;

public interface ReplyMvpView extends MvpView {

    void showSuccess();

    void showFailure();
}