package ch.redacted.ui.search.artist;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

import ch.redacted.data.DataManager;
import ch.redacted.data.model.Artist;
import ch.redacted.injection.ConfigPersistent;
import ch.redacted.ui.base.BasePresenter;

@ConfigPersistent public class ArtistSearchPresenter extends BasePresenter<ArtistSearchMvpView> {

	private final DataManager mDataManager;
	private CompositeDisposable mSubscription = new CompositeDisposable();

	@Inject public ArtistSearchPresenter(DataManager dataManager) {
		mDataManager = dataManager;
	}

	@Override public void attachView(ArtistSearchMvpView mvpView) {
		super.attachView(mvpView);
	}

	@Override public void detachView() {
		super.detachView();
		if (mSubscription != null) mSubscription.dispose();
	}

	public void loadArtists(String searchTerm) {
		checkViewAttached();
		if (searchTerm.isEmpty() || searchTerm.equals("") || searchTerm.equals(null)){
			getMvpView().showError();
		}

		mSubscription.add(mDataManager.artistsSearch(searchTerm)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribeWith(new DisposableSingleObserver<Artist>() {
				@Override
				public void onSuccess(Artist artist) {
					if (artist.response == null){
						getMvpView().showResultsEmpty();
					} else {
						getMvpView().showResults(artist.response);
					}
				}

				@Override
				public void onError(Throwable error) {
					getMvpView().showResultsEmpty();
				}
			}));
	}
}
