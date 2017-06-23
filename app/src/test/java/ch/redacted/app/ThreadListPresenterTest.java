package ch.redacted.app;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.Single;
import ch.redacted.app.util.RxSchedulersOverrideRule;
import ch.redacted.data.DataManager;
import ch.redacted.data.model.ForumView;
import ch.redacted.data.remote.ApiService;
import ch.redacted.ui.forum.threadList.ThreadListMvpView;
import ch.redacted.ui.forum.threadList.ThreadListPresenter;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ThreadListPresenterTest {

    @Mock ThreadListMvpView mMockThreadListMvpView;
    @Mock DataManager mMockDataManager;
    @Mock ApiService mMockApiservice;

    private ThreadListPresenter mThreadListPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        mThreadListPresenter = new ThreadListPresenter(mMockDataManager);
        mThreadListPresenter.attachView(mMockThreadListMvpView);
    }

    @After
    public void tearDown() {
        mThreadListPresenter.detachView();
    }

    @Test
    public void loadMultiplePages() {

    }

    @Test
    public void loadSinglePage() {

    }

    @Test
    public void loadFail() {
        when(mMockDataManager.getThreads(1, 1))
                .thenReturn(Single.<ForumView>error(new RuntimeException()));
        mThreadListPresenter.loadThreads(1, 1);
        verify(mMockThreadListMvpView).showError();
        verify(mMockThreadListMvpView, never()).showThreadsEmpty();
    }

    private void stubDataManagerGetForumList(Single single) {
        doReturn(single)
                .when(mMockDataManager)
                .getAnnouncements();
    }
}