package ch.redacted.ui.inbox;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import ch.redacted.data.DataManager;
import ch.redacted.data.model.Conversations;
import ch.redacted.injection.ConfigPersistent;
import ch.redacted.ui.base.BasePresenter;
import retrofit2.HttpException;

@ConfigPersistent
public class InboxPresenter extends BasePresenter<InboxMvpView> {

    private final DataManager mDataManager;
    private CompositeDisposable mSubscription = new CompositeDisposable();

    @Inject
    public InboxPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(InboxMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.dispose();
    }

    public void loadInbox(String type) {
        checkViewAttached();
        getMvpView().showLoadingProgress(true);

        mSubscription.add(mDataManager.loadInbox(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Conversations>() {
                    @Override
                    public void onSuccess(Conversations conversations) {
                        if (conversations.response.messages.size() == 0){
                            getMvpView().showInboxEmpty();
                            getMvpView().showLoadingProgress(false);
                        } else {
                            getMvpView().showInbox(conversations.response.messages);
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
}
