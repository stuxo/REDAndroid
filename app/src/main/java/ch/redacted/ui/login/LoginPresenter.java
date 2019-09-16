package ch.redacted.ui.login;

import javax.inject.Inject;

import ch.redacted.data.DataManager;
import ch.redacted.data.local.PreferencesHelper;
import ch.redacted.data.model.Index;
import ch.redacted.injection.ConfigPersistent;
import ch.redacted.ui.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@ConfigPersistent
public class LoginPresenter extends BasePresenter<LoginMvpView> {

    private final DataManager mDataManager;
    private final PreferencesHelper mPreferenceHelper;
    private CompositeDisposable mSubscription = new CompositeDisposable();

    @Inject
    public LoginPresenter(DataManager dataManager, PreferencesHelper preferencesHelper) {
        mDataManager = dataManager;
        mPreferenceHelper = preferencesHelper;
    }

    @Override
    public void attachView(LoginMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.dispose();
    }

    public void login(String username, String password) {
        checkViewAttached();
        getMvpView().showLoadingProgress(true);

        mSubscription.add(mDataManager.login(username, password, 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(Boolean success) {
                        if (success) {
                            loginWithCookie();
                        } else {
                            getMvpView().showError("Login Failed");
                            getMvpView().showLoadingProgress(false);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        getMvpView().showError(error.getMessage());
                        getMvpView().showLoadingProgress(false);
                    }
                }));
    }

    public void loginWithCookie() {
        checkViewAttached();

        getMvpView().showLoadingProgress(true);
        mSubscription.add(mDataManager.loginWithCookie()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Index>() {
                    @Override
                    public void onSuccess(Index value) {
                        getMvpView().showLoginSuccess();
                        getMvpView().showLoadingProgress(false);
                    }

                    @Override
                    public void onError(Throwable error) {
                        getMvpView().showError(error.getMessage());
                        getMvpView().showLoadingProgress(false);
                    }
                }));
    }

    public boolean hasCookie() {
        return mPreferenceHelper.getCookies().size() > 0;
    }

    public boolean hasApiKey() {
        return !mPreferenceHelper.getApiKey().isEmpty();
    }

    public String getApiKey() {
        return mPreferenceHelper.getApiKey();
    }

    public void loginWithApiKey(String key) {
        getMvpView().showLoadingProgress(true);
        mPreferenceHelper.setApiKey(key);
        mSubscription.add(mDataManager.loginWithApiKey()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Index>() {
                    @Override
                    public void onSuccess(Index value) {
                        getMvpView().showLoginSuccess();
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
