package ch.redacted.ui.artist;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

import java.util.TreeMap;
import javax.inject.Inject;

import ch.redacted.data.DataManager;
import ch.redacted.data.model.Artist;
import ch.redacted.injection.ConfigPersistent;
import ch.redacted.ui.base.BasePresenter;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@ConfigPersistent
public class ArtistPresenter extends BasePresenter<ArtistMvpView> {

    private final DataManager mDataManager;
    private CompositeDisposable mSubscription = new CompositeDisposable();

    @Inject
    public ArtistPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ArtistMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.dispose();
    }

    public void loadArtist(int id) {
        checkViewAttached();
        getMvpView().showLoadingProgress(true);

        mSubscription.add(mDataManager.artistsSearch(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Artist>() {
                    @Override
                    public void onSuccess(Artist artist) {
                        if (artist.response.body.equals("")){
                            artist.response.body = "No description";
                        }
                        getMvpView().showArtist(artist);
                        getMvpView().showLoadingProgress(false);
                    }

                    @Override
                    public void onError(Throwable error) {
                        getMvpView().showError(error.getMessage());
                        getMvpView().showLoadingProgress(false);
                    }
                }));
    }

    public void toggleArtistBookmark(int artistId, boolean isBookmarked) {
        checkViewAttached();
        getMvpView().showLoadingProgress(true);

        if (isBookmarked){
            mSubscription.add(mDataManager.removeArtistBookmark(artistId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableSingleObserver<ResponseBody>() {
                        @Override
                        public void onSuccess(ResponseBody response) {
                            getMvpView().showBookmarked(false);
                            getMvpView().showLoadingProgress(false);
                        }

                        @Override
                        public void onError(Throwable error) {
                            if (error instanceof HttpException) {
                               HttpException httpException = ((HttpException) error);
                                if (httpException.code() == 302) {
                                    getMvpView().showBookmarked(false);
                                }
                            }
                            getMvpView().showError(error.getMessage());
                            getMvpView().showLoadingProgress(false);
                        }
                    }));
        } else {
            mSubscription.add(mDataManager.addArtistBookmark(artistId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableSingleObserver<ResponseBody>() {
                        @Override
                        public void onSuccess(ResponseBody response) {
                            getMvpView().showBookmarked(true);
                            getMvpView().showLoadingProgress(false);
                        }

                        @Override
                        public void onError(Throwable error) {
                            if (error instanceof HttpException) {
                                HttpException httpException = ((HttpException) error);
                                if (httpException.code() == 302) {
                                    getMvpView().showBookmarked(true);
                                }
                            }
                            getMvpView().showError(error.getMessage());
                            getMvpView().showLoadingProgress(false);
                        }
                    }));
        }
    }

    public void loadTorrents(List<Artist.Torrentgroup> torrentgroup) {
        Observable.fromCallable(() -> {
            TreeMap<Integer, List<Artist.Torrentgroup>> treeMap = new TreeMap<>();
            List<Object> flattenedMap = new ArrayList<>();
            for (Artist.Torrentgroup group : torrentgroup){
                if (!treeMap.containsKey(group.releaseType)) {
                    List<Artist.Torrentgroup> list = new ArrayList<>();
                    list.add(group);

                    treeMap.put(group.releaseType, list);
                } else {
                    treeMap.get(group.releaseType).add(group);
                }
            }

            for (int key : treeMap.keySet()) {
                flattenedMap.add(key);
                for (Artist.Torrentgroup group: treeMap.get(key)) {
                    flattenedMap.add(group);
                }
            }

           return flattenedMap;
        }).subscribeOn(Schedulers.computation())
                .subscribe(flattenedMap -> getMvpView().showTorrents(flattenedMap));

    }
}
