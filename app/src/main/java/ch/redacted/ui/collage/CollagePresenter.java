package ch.redacted.ui.collage;

import javax.inject.Inject;

import ch.redacted.data.DataManager;
import ch.redacted.data.model.Collage;
import ch.redacted.ui.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CollagePresenter extends BasePresenter<CollageMvpView> {

    private final DataManager mDataManager;
    private CompositeDisposable mSubscription = new CompositeDisposable();

    @Inject
    CollagePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    void loadCollage(int id) {
        checkViewAttached();
        getMvpView().showLoadingProgress(true);

        mSubscription.add(mDataManager.getCollage(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Collage>() {
                    @Override
                    public void onSuccess(Collage collage) {
                        getMvpView().showCollage(collage);
                        getMvpView().showProgress(false);
                    }

                    @Override
                    public void onError(Throwable error) {
                        getMvpView().showError(error.getMessage());
                        getMvpView().showProgress(false);
                    }
                }));

    }

}
