package ch.redacted.ui.inbox.conversation;

import java.util.List;

import ch.redacted.data.model.Conversation;
import ch.redacted.ui.base.MvpView;

public interface ConversationMvpView extends MvpView {

    void showMessages(List<Conversation.Messages> messages);

    void showMessagesEmpty();

    void showLoadingProgress(boolean show);

    void showError(String message);

    void showSnackbar(String message);

    void setConversationInfo(int convId, String subject, String user);

    void showSuccess();
}