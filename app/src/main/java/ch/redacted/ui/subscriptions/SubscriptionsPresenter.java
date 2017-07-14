package ch.redacted.ui.subscriptions;

import javax.inject.Inject;

import ch.redacted.data.DataManager;
import ch.redacted.data.model.Subscription;
import ch.redacted.injection.ConfigPersistent;
import ch.redacted.ui.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@ConfigPersistent
public class SubscriptionsPresenter extends BasePresenter<SubscriptionsMvpView> {

    private final DataManager mDataManager;
    private CompositeDisposable mSubscription = new CompositeDisposable();

    @Inject
    public SubscriptionsPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(SubscriptionsMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.dispose();
    }

    public void loadSubscriptions(String type) {
        checkViewAttached();
        getMvpView().showLoadingProgress(true);

        mDataManager.loadSubscriptions(type.equals("read") ? true : false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<Subscription>() {
                    @Override
                    public void onSuccess(Subscription subs) {
                        if (subs.response.threads == null || subs.response.threads.size() == 0){
                            getMvpView().showSubscriptionsEmpty();
                        }
                        else {
                            getMvpView().showSubscriptions(subs.response.threads);
                        }
                        getMvpView().showLoadingProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e.getMessage());
                        getMvpView().showLoadingProgress(false);
                    }
                });
    }
}
