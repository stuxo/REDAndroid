package ch.redacted.ui.forum.category;

import java.util.List;

import ch.redacted.ui.base.MvpView;

public interface CategoryMvpView extends MvpView {

    void showCategories(List<Object> categories);

    void showCategoriesEmpty();

    void showError();

    void showProgress(boolean show);
}
