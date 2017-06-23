package ch.redacted.ui.forum.threadList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

import ch.redacted.ui.base.BasePresenter;
import ch.redacted.data.DataManager;
import ch.redacted.data.model.ForumView;
import ch.redacted.injection.ConfigPersistent;

@ConfigPersistent public class ThreadListPresenter extends BasePresenter<ThreadListMvpView> {

	private final DataManager mDataManager;
	private CompositeDisposable mSubscription = new CompositeDisposable();

	@Inject public ThreadListPresenter(DataManager dataManager) {
		mDataManager = dataManager;
	}

	@Override public void attachView(ThreadListMvpView mvpView) {
		super.attachView(mvpView);
	}

	@Override public void detachView() {
		super.detachView();
		if (mSubscription != null) mSubscription.dispose();
	}

	public void loadThreads(int id, int page) {
		checkViewAttached();
		getMvpView().showProgress(true);

		mSubscription.add(mDataManager.getThreads(id, page)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribeWith(new DisposableSingleObserver<ForumView>() {
				@Override
				public void onSuccess(ForumView item) {
					if (item.response.threads.size() == 0){
						getMvpView().showThreadsEmpty();
						getMvpView().showProgress(false);
					} else {
						getMvpView().showThreads(item);
						getMvpView().showProgress(false);
					}
				}

				@Override
				public void onError(Throwable error) {
					getMvpView().showError();
					getMvpView().showProgress(false);
				}
			}));
	}
}



