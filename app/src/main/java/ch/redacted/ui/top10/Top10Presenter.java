package ch.redacted.ui.top10;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import ch.redacted.data.DataManager;
import ch.redacted.data.model.Top10;
import ch.redacted.injection.ConfigPersistent;
import ch.redacted.ui.base.BasePresenter;

@ConfigPersistent public class Top10Presenter extends BasePresenter<Top10MvpView> {

	private final DataManager mDataManager;
	private CompositeDisposable mSubscription = new CompositeDisposable();

	@Inject public Top10Presenter(DataManager dataManager) {
		mDataManager = dataManager;
	}

	@Override public void attachView(Top10MvpView mvpView) {
		super.attachView(mvpView);
	}

	@Override public void detachView() {
		super.detachView();
		if (mSubscription != null) mSubscription.dispose();
	}

	public void loadTopTorrents(String detail) {
		checkViewAttached();
		getMvpView().showProgress(true);

		mSubscription.add(mDataManager.getTopTorrents(detail)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribeWith(new DisposableSingleObserver<Top10>() {
				@Override public void onSuccess(Top10 items) {
					if (items.response == null || items.response.size() == 0) {
						getMvpView().showTop10Empty();
						getMvpView().showProgress(false);
					} else {
						//If you use type=torrents you will always want the first one
						getMvpView().showTop10(items.response.get(0).results);
						getMvpView().showProgress(false);
					}
				}

				@Override public void onError(Throwable error) {
					getMvpView().showError();
					error.printStackTrace();
					getMvpView().showProgress(false);
				}
			}));
	}
}
