package ch.redacted.app;

import io.reactivex.Single;
import ch.redacted.app.util.RxSchedulersOverrideRule;
import ch.redacted.data.DataManager;
import ch.redacted.data.model.Top10;
import ch.redacted.data.remote.ApiService;
import ch.redacted.ui.top10.Top10MvpView;
import ch.redacted.ui.top10.Top10Presenter;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Top10PresenterTest {

    @Mock Top10MvpView mMockTop10MvpView;
    @Mock DataManager mMockDataManager;
    @Mock ApiService mMockApiservice;

    private Top10Presenter mTop10Presenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        mTop10Presenter = new Top10Presenter(mMockDataManager);
        mTop10Presenter.attachView(mMockTop10MvpView);
    }

    @After
    public void tearDown() {
        mTop10Presenter.detachView();
    }

    @Test
    public void loadTop10() {

    }

    @Test
    public void loadFail() {
        when(mMockDataManager.getTopTorrents(10))
                .thenReturn(Single.<Top10>error(new RuntimeException()));
        mTop10Presenter.loadTopTorrents(10);
        verify(mMockTop10MvpView).showError();
        verify(mMockTop10MvpView, never()).showTop10Empty();
    }

    private void stubDataManagertopTorrents(Single single) {
        doReturn(single)
                .when(mMockDataManager)
                .getAnnouncements();
    }
}