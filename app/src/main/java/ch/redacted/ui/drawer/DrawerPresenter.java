package ch.redacted.ui.drawer;

import android.content.Context;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

import ch.redacted.ui.base.BasePresenter;
import ch.redacted.data.DataManager;
import ch.redacted.data.model.Profile;
import ch.redacted.injection.ConfigPersistent;
import ch.redacted.util.SharedPrefsHelper;

@ConfigPersistent
public class DrawerPresenter extends BasePresenter<DrawerMvpView> {

    private final DataManager mDataManager;
    private CompositeDisposable mSubscription = new CompositeDisposable();

    @Inject public DrawerPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override public void attachView(DrawerMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.dispose();
    }

    public void loadProfile(final Context context, boolean useCache) {
        checkViewAttached();

        if (useCache) {
            Profile profile = new Profile();
            String s = SharedPrefsHelper.getInstance(context)
                    .readSharedPrefsString(SharedPrefsHelper.FILE_PROFILE, SharedPrefsHelper.KEY_PROFILE, "");
            profile = profile.deserializeFromJson(s);
            if (profile != null) {
                getMvpView().showProfileInfo(profile);
            } else {
                loadProfile(context, false);
            }
        } else {
            mDataManager.getProfile(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<Profile>() {
                    @Override public void onSuccess(Profile profile) {
                        getMvpView().showProfileInfo(profile);

                        SharedPrefsHelper.getInstance(context).
                            writeSharedPrefs(SharedPrefsHelper.FILE_PROFILE,
                                SharedPrefsHelper.KEY_PROFILE, profile.serializeToJson(), null,
                                null);
                    }

                    @Override public void onError(Throwable error) {
                        getMvpView().showError();
                        error.printStackTrace();
                    }
                });
        }

    }

    public void setupDrawer() {
        getMvpView().setupDrawer();
    }
}
