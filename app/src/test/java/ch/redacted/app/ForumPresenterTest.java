package ch.redacted.app;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import ch.redacted.app.util.RxSchedulersOverrideRule;
import ch.redacted.data.DataManager;
import ch.redacted.data.remote.ApiService;
import ch.redacted.ui.forum.category.CategoryMvpView;
import ch.redacted.ui.forum.category.CategoryPresenter;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ForumPresenterTest {

    @Mock CategoryMvpView mMockCategoryMvpView;
    @Mock DataManager mMockDataManager;
    @Mock ApiService mMockApiservice;

    private CategoryPresenter mCategoryPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        mCategoryPresenter = new CategoryPresenter(mMockDataManager);
        mCategoryPresenter.attachView(mMockCategoryMvpView);
    }

    @After
    public void tearDown() {
        mCategoryPresenter.detachView();
    }

    @Test
    public void loadCategoriesFail() {

        when(mMockDataManager.getCategories())
                .thenReturn(Single.<List<Object>>error(new RuntimeException()));

        mCategoryPresenter.loadCategories();
        verify(mMockCategoryMvpView).showError();
        verify(mMockCategoryMvpView, never()).showCategoriesEmpty();
        verify(mMockCategoryMvpView, never()).showCategories(anyListOf(Object.class));

    }

    @Test
    public void loadCategoriesEmpty() {
        List<Object> emptyList = new ArrayList<>();

        stubDataManagerGetCategories(Single.just(emptyList));

        mCategoryPresenter.loadCategories();
        verify(mMockCategoryMvpView).showProgress(true);
        verify(mMockCategoryMvpView).showProgress(false);
        verify(mMockCategoryMvpView).showCategoriesEmpty();
    }

    private void stubDataManagerGetCategories(Single single) {
        doReturn(single)
                .when(mMockDataManager)
                .getCategories();
    }
}