package ch.redacted.ui.bookmark;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import ch.redacted.data.DataManager;
import ch.redacted.data.model.TorrentBookmark;
import ch.redacted.injection.ConfigPersistent;
import ch.redacted.ui.base.BasePresenter;
import okhttp3.ResponseBody;

@ConfigPersistent public class BookmarkPresenter extends BasePresenter<BookmarkMvpView> {

	private final DataManager mDataManager;
	private CompositeDisposable mSubscription = new CompositeDisposable();

	@Inject public BookmarkPresenter(DataManager dataManager) {
		mDataManager = dataManager;
	}

	@Override public void attachView(BookmarkMvpView mvpView) {
		super.attachView(mvpView);
	}

	@Override public void detachView() {
		super.detachView();
		if (mSubscription != null) mSubscription.dispose();
	}

	public void loadTorrentBookmarks() {
		checkViewAttached();
		getMvpView().showProgress(true);

		mSubscription.add(mDataManager.getTorrentBookmarks()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribeWith(new DisposableSingleObserver<TorrentBookmark>() {
				@Override public void onSuccess(TorrentBookmark items) {
					if (items.response.bookmarks.size() == 0) {
						getMvpView().showBookmarksEmpty();
						getMvpView().showProgress(false);
					} else {
						getMvpView().showBookmarks(items.response.bookmarks);
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

	public void removeBookmark(int releaseId) {
		checkViewAttached();
		getMvpView().showProgress(true);

		mSubscription.add(mDataManager.removeBookmark(releaseId, "torrent")
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribeWith(new DisposableSingleObserver<ResponseBody>() {
					@Override
					public void onSuccess(ResponseBody response) {
						mDataManager.getTorrentBookmarks()
								.observeOn(AndroidSchedulers.mainThread())
								.subscribeOn(Schedulers.io())
								.subscribe(new DisposableSingleObserver<TorrentBookmark>() {
									@Override
									public void onSuccess(TorrentBookmark bookmarks) {
										getMvpView().showBookmarks(bookmarks.response.bookmarks);
										getMvpView().showProgress(false);
									}

									@Override
									public void onError(Throwable e) {
										getMvpView().showProgress(false);
									}
								});
					}

					@Override
					public void onError(Throwable error) {
						getMvpView().showProgress(false);
					}
				}));
	}
}
