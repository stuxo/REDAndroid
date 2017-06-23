package ch.redacted.ui.drawer;

import ch.redacted.data.model.Profile;
import ch.redacted.ui.base.MvpView;

public interface DrawerMvpView extends MvpView {

    void showError();

    void setupDrawer();

    void showProgress(boolean show);

    void showProfileInfo(Profile profile);
}
