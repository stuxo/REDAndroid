package ch.redacted.ui.release;

import android.content.Context;

import java.io.File;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

import ch.redacted.data.DataManager;
import ch.redacted.data.model.TorrentGroup;
import ch.redacted.injection.ConfigPersistent;
import ch.redacted.ui.base.BasePresenter;
import okhttp3.ResponseBody;
import retrofit2.Response;

@ConfigPersistent
public class ReleasePresenter extends BasePresenter<ReleaseMvpView> {

    private final DataManager mDataManager;
    private CompositeDisposable mSubscription = new CompositeDisposable();

    @Inject
    public ReleasePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ReleaseMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.dispose();
    }

    public void loadRelease(int id) {
        checkViewAttached();
        getMvpView().showLoadingProgress(true);

        mSubscription.add(mDataManager.getRelease(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<TorrentGroup>() {
                    @Override
                    public void onSuccess(TorrentGroup torrentGroup) {
                        getMvpView().showRelease(torrentGroup);
                        flattenTorrents(torrentGroup.response.torrents);
                        getMvpView().showLoadingProgress(false);
                    }

                    @Override
                    public void onError(Throwable error) {
                        getMvpView().showError(error.getMessage());
                        getMvpView().showLoadingProgress(false);
                    }
                }));
    }

    private void flattenTorrents(List<TorrentGroup.Torrents> torrents) {
        mDataManager.flattenTorrents(torrents)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object group) {
                        getMvpView().showTorrents(group);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        getMvpView().showLoadingProgress(false);
                    }
                });
    }

    public void toggleBookmark(int releaseId, boolean isBookmarked) {
        checkViewAttached();
        getMvpView().showLoadingProgress(true);

        if (isBookmarked){
            mSubscription.add(mDataManager.removeBookmark(releaseId, "torrent")
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
                            getMvpView().showError(error.getMessage());
                            getMvpView().showLoadingProgress(false);
                        }
                    }));
        } else {
            mSubscription.add(mDataManager.addBookmark(releaseId, "torrent")
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
                            getMvpView().showError(error.getMessage());
                            getMvpView().showLoadingProgress(false);
                        }
                    }));
        }
    }

    public void downloadRelease(int id, Context context) {
        checkViewAttached();
        String downloadMethod = mDataManager.getPreferencesHelper().getDownloadMethod();

        if (downloadMethod.equals("Send to WhatManager")) {
            getMvpView().showMessage("Sent download request");
            mSubscription.add(mDataManager.downloadWmRelease(id, context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Response>() {
                    @Override public void onSuccess(Response value) {
                        getMvpView().showSendToServerComplete();
                    }

                    @Override public void onError(Throwable error) {
                        getMvpView().showError("Could not download file: " + error.getMessage());
                    }
                }));
        } else if (downloadMethod.equals("Send to PyWhatAuto")) {
            getMvpView().showMessage("Sent download request");
            mSubscription.add(mDataManager.downloadPywaRelease(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Response>() {
                    @Override public void onSuccess(Response value) {
                        //fml... really?
                        if (value.body().toString().contains("Incorrect password")){
                            getMvpView().showError("Could not download file: " + value.body().toString());
                        } else {
                            getMvpView().showSendToServerComplete();
                        }
                    }

                    @Override public void onError(Throwable error) {
                        getMvpView().showError("Could not download file: " + error.getMessage());
                    }
                }));
        } else {
            mSubscription.add(mDataManager.downloadRelease(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<File>() {
                    @Override public void onSuccess(File file) {
                        getMvpView().showDownloadComplete(file);
                    }

                    @Override public void onError(Throwable error) {
                        getMvpView().showError("Could not download file: " + error.getMessage());
                    }
                }));
        }
    }
}
