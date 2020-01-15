package ch.redacted.ui.collage;

import ch.redacted.data.model.Collage;
import ch.redacted.ui.base.MvpView;

public interface CollageMvpView extends MvpView {

    void showCollage(Collage collage);

    void showLoadingProgress(boolean show);

    void showError(String message);

    void showBookmarked(boolean b);

    void showProgress(boolean show);
}
