package ch.redacted.ui.login;

import ch.redacted.ui.base.MvpView;

public interface LoginMvpView extends MvpView {

    void showLoginSuccess();

    void showCookieExpired();

    void onCookieFound();

    void showLoadingProgress(boolean show);

    void showError(String message);
}
