package ch.redacted.ui.forum.thread;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

import ch.redacted.data.DataManager;
import ch.redacted.data.model.ForumThread;
import ch.redacted.injection.ConfigPersistent;
import ch.redacted.ui.base.BasePresenter;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@ConfigPersistent public class ThreadPresenter extends BasePresenter<ThreadMvpView> {

	private final DataManager mDataManager;
	private CompositeDisposable mSubscription = new CompositeDisposable();

	@Inject public ThreadPresenter(DataManager dataManager) {
		mDataManager = dataManager;
	}

	@Override public void attachView(ThreadMvpView mvpView) {
		super.attachView(mvpView);
	}

	@Override public void detachView() {
		super.detachView();
		if (mSubscription != null) mSubscription.dispose();
	}

	public void loadPosts(int threadId, Integer postId, Integer page, final boolean scrollToTop) {
		checkViewAttached();
		getMvpView().showProgress(true);

		mSubscription.add(mDataManager.getPosts(threadId, postId, page)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribeWith(new DisposableSingleObserver<ForumThread>() {
				@Override
				public void onSuccess(ForumThread item) {
					if (item.response == null || item.response.posts.size() == 0){
						getMvpView().showPostsEmpty();
						getMvpView().showProgress(false);
					} else {
						getMvpView().showPosts(item, scrollToTop);
						getMvpView().showProgress(false);
						getMvpView().showSubscribed(item.response.subscribed);
					}
				}

				@Override
				public void onError(Throwable error) {
					getMvpView().showError();
					getMvpView().showProgress(false);
				}
			}));
	}

	public void addPost(String text, int forumId, int topicId) {
		checkViewAttached();
		getMvpView().showProgress(true);

		mSubscription.add(mDataManager.addPost(text, forumId, topicId)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribeWith(new DisposableSingleObserver<ResponseBody>() {
					@Override
					public void onSuccess(ResponseBody value) {
						getMvpView().showProgress(false);
						getMvpView().showPostSuccessful();
					}

					@Override
					public void onError(Throwable error) {
						if (error instanceof HttpException) {
							HttpException httpException = ((HttpException) error);
							if (httpException.code() == 302) {
								getMvpView().showPostSuccessful();
							}
						}
						else {
							getMvpView().showPostFailed();
						}
						getMvpView().showProgress(false);
					}
				}));
	}

    public void toggleSubscribe(int topicId, boolean isSubscribed) {
		checkViewAttached();
//		getMvpView().sho(true);

		if (isSubscribed){
			mSubscription.add(mDataManager.removeForumSub(topicId)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribeWith(new DisposableSingleObserver<ResponseBody>() {
						@Override
						public void onSuccess(ResponseBody response) {
							getMvpView().showSubscribed(false);
//							getMvpView().showLoadingProgress(false);
						}

						@Override
						public void onError(Throwable error) {
							if (error instanceof HttpException) {
								HttpException httpException = ((HttpException) error);
								if (httpException.code() == 302) {
									getMvpView().showSubscribed(false);
								}
							}
//							getMvpView().showError(error.getMessage());
//							getMvpView().showLoadingProgress(false);
						}
					}));
		} else {
			mSubscription.add(mDataManager.addForumSub(topicId)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribeWith(new DisposableSingleObserver<ResponseBody>() {
						@Override
						public void onSuccess(ResponseBody response) {
							getMvpView().showSubscribed(true);
//							getMvpView().showLoadingProgress(false);
						}

						@Override
						public void onError(Throwable error) {
							if (error instanceof HttpException) {
								HttpException httpException = ((HttpException) error);
								if (httpException.code() == 302) {
									getMvpView().showSubscribed(true);
								}
							}
//							getMvpView().showError(error.getMessage());
//							getMvpView().showLoadingProgress(false);
						}
					}));
		}    }
}



