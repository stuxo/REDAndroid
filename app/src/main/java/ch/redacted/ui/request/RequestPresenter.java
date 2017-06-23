package ch.redacted.ui.request;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import ch.redacted.data.DataManager;
import ch.redacted.data.model.Request;
import ch.redacted.injection.ConfigPersistent;
import ch.redacted.ui.base.BasePresenter;

@ConfigPersistent
public class RequestPresenter extends BasePresenter<RequestMvpView> {

    private final DataManager mDataManager;
    private CompositeDisposable mSubscription = new CompositeDisposable();

    @Inject
    public RequestPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(RequestMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.dispose();
    }

    public void loadRequest(int id) {
        checkViewAttached();
        getMvpView().showLoadingProgress(true);

        mSubscription.add(mDataManager.getRequest(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Request>() {
                    @Override
                    public void onSuccess(Request request) {
                        getMvpView().showRequest(request);
                        getMvpView().showLoadingProgress(false);
                    }

                    @Override
                    public void onError(Throwable error) {
                        getMvpView().showError(error.getMessage());
                        getMvpView().showLoadingProgress(false);
                    }
                }));
    }
}
