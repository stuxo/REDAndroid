package ch.redacted.ui.search.collage;

import javax.inject.Inject;

import ch.redacted.data.DataManager;
import ch.redacted.data.model.CollageSearch;
import ch.redacted.injection.ConfigPersistent;
import ch.redacted.ui.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@ConfigPersistent
public class CollageSearchPresenter extends BasePresenter<CollageSearchMvpView> {

    private final DataManager mDataManager;
    private CompositeDisposable mSubscription = new CompositeDisposable();

    @Inject
    CollageSearchPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(CollageSearchMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.dispose();
    }

    void loadCollages(String searchTerm) {
        checkViewAttached();
        getMvpView().showProgress(true);
        if (searchTerm.isEmpty()) {
            getMvpView().showError();
        }

        mSubscription.add(mDataManager.collageSearch(searchTerm)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<CollageSearch>() {
                    @Override
                    public void onSuccess(CollageSearch collages) {
                        if (collages.response.size() == 0) {
                            getMvpView().showResultsEmpty();
                            getMvpView().showProgress(false);
                        } else {
                            getMvpView().showResults(collages.response);
                            getMvpView().showProgress(false);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        getMvpView().showError();
                        error.printStackTrace();
                        getMvpView().showProgress(false);
                    }
                }));
    }
}
