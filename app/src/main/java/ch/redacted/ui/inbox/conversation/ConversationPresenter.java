package ch.redacted.ui.inbox.conversation;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import ch.redacted.data.DataManager;
import ch.redacted.data.model.Conversation;
import ch.redacted.injection.ConfigPersistent;
import ch.redacted.ui.base.BasePresenter;
import retrofit2.HttpException;

@ConfigPersistent
public class ConversationPresenter extends BasePresenter<ConversationMvpView> {

    private final DataManager mDataManager;
    private CompositeDisposable mSubscription = new CompositeDisposable();

    @Inject
    public ConversationPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ConversationMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.dispose();
    }

    public void loadConversation(int id) {
        checkViewAttached();
        getMvpView().showLoadingProgress(true);

        mSubscription.add(mDataManager.getConversation(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Conversation>() {
                    @Override
                    public void onSuccess(Conversation conversation) {
                        if (conversation.response.messages.size() == 0){
                            getMvpView().showMessagesEmpty();
                            getMvpView().showLoadingProgress(false);
                        } else {
                            String toId = "";
                            //hacky way to get the other user's id, I can't see it being offered anywhere else. This will fail if you're replying to a conv that hasn't had a response
                            for (Conversation.Messages message : conversation.response.messages) {
                                if (message.senderId != mDataManager.getPreferencesHelper().getUserId()){
                                    toId = String.valueOf(message.senderId);
                                    break;
                                }
                            }
                            getMvpView().showMessages(conversation.response.messages);
                            getMvpView().setConversationInfo(conversation.response.convId, conversation.response.subject, toId);
                            getMvpView().showLoadingProgress(false);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        getMvpView().showError(error.getMessage());
                        getMvpView().showLoadingProgress(false);
                    }
                }));
    }

    public void replyMessage(String mUser, final String subject, final int conversationId, final String body) {
        mDataManager.sendMessage(mUser, String.valueOf(conversationId), subject, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new SingleObserver<Object>() {
                    @Override public void onSubscribe(Disposable d) {

                    }

                    @Override public void onSuccess(Object value) {
                    }

                    @Override public void onError(Throwable error) {
                        if (error instanceof HttpException) {
                            HttpException httpException = ((HttpException) error);
                            if (httpException.code() == 302) {
                                getMvpView().showSnackbar("Message sent");
                                getMvpView().showSuccess();
                            }
                        }
                        else if (error.getMessage().equals("User not found")){
                            getMvpView().showSnackbar("User not found");
                        }
                        else {
                            getMvpView().showSnackbar(error.getMessage());
                        }
                    }
                });
    }
}
