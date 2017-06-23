package ch.redacted.ui.inbox;

import java.util.List;

import ch.redacted.data.model.Conversations;
import ch.redacted.ui.base.MvpView;

public interface InboxMvpView extends MvpView {

    void showInbox(List<Conversations.Messages> messages);

    void showInboxEmpty();

    void showLoadingProgress(boolean show);

    void showError(String message);

	void showSnackbar(String message);
}