package ch.redacted.ui.top10;

import java.util.List;
import ch.redacted.data.model.Top10;
import ch.redacted.ui.base.MvpView;

public interface Top10MvpView extends MvpView {

    void showTop10(List<Top10.Results> categories);

    void showTop10Empty();

    void showError();

    void showProgress(boolean show);
}
