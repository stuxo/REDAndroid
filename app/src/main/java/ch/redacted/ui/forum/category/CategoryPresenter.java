package ch.redacted.ui.forum.category;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

import javax.inject.Inject;

import ch.redacted.ui.base.BasePresenter;
import ch.redacted.data.DataManager;
import ch.redacted.injection.ConfigPersistent;

@ConfigPersistent public class CategoryPresenter extends BasePresenter<CategoryMvpView> {

	private final DataManager mDataManager;
	private CompositeDisposable mSubscription = new CompositeDisposable();

	@Inject public CategoryPresenter(DataManager dataManager) {
		mDataManager = dataManager;
	}

	@Override public void attachView(CategoryMvpView mvpView) {
		super.attachView(mvpView);
	}

	@Override public void detachView() {
		super.detachView();
		if (mSubscription != null) mSubscription.dispose();
	}

	public void loadCategories() {
		checkViewAttached();
		getMvpView().showProgress(true);

		mSubscription.add(mDataManager.getCategories()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribeWith(new DisposableSingleObserver<List<Object>>() {
				@Override
				public void onSuccess(List<Object> items) {
					if (items.size() == 0){
						getMvpView().showCategoriesEmpty();
						getMvpView().showProgress(false);
					} else {
						getMvpView().showCategories(items);
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
