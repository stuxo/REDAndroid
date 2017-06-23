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
import ch.redacted.app.test.common.TestDataFactory;
import ch.redacted.app.util.RxSchedulersOverrideRule;
import ch.redacted.data.DataManager;
import ch.redacted.data.model.Announcement;
import ch.redacted.data.remote.ApiService;
import ch.redacted.ui.announcements.AnnouncementMvpView;
import ch.redacted.ui.announcements.AnnouncementPresenter;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserSearchSearchPresenterTest {

    @Mock AnnouncementMvpView mMockAnnouncementMvpView;
    @Mock DataManager mMockDataManager;
    @Mock ApiService mMockApiservice;

    private AnnouncementPresenter mAnnouncementPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        mAnnouncementPresenter = new AnnouncementPresenter(mMockDataManager);
        mAnnouncementPresenter.attachView(mMockAnnouncementMvpView);
    }

    @After
    public void tearDown() {
        mAnnouncementPresenter.detachView();
    }

    @Test
    public void loadAnnouncements() {

        List<Announcement.Announcements> announcements = TestDataFactory.getAnnouncements(20);
        Announcement announcement = new Announcement();
        announcement.response = new Announcement.Response();
        announcement.response.announcements = announcements;
        stubDataManagerGetAnnouncements(Single.just(announcement));

        mAnnouncementPresenter.loadAnnouncements();
        verify(mMockAnnouncementMvpView).showProgress(true);
        verify(mMockAnnouncementMvpView).showAnnouncements(announcements);
        verify(mMockAnnouncementMvpView).showProgress(false);
    }

    @Test
    public void loadAnnouncementsFail() {

        when(mMockDataManager.getAnnouncements())
                .thenReturn(Single.<Announcement>error(new RuntimeException()));

        mAnnouncementPresenter.loadAnnouncements();
        verify(mMockAnnouncementMvpView).showError();
        verify(mMockAnnouncementMvpView, never()).showAnnouncementsEmpty();
        verify(mMockAnnouncementMvpView, never()).showAnnouncements(anyListOf(Announcement.Announcements.class));

    }

    @Test
    public void loadAnnouncementsEmpty() {
        Announcement emptyList = new Announcement();
        emptyList.response = new Announcement.Response();
        emptyList.status = "";
        emptyList.response.announcements = new ArrayList<>();

        stubDataManagerGetAnnouncements(Single.just(emptyList));

        mAnnouncementPresenter.loadAnnouncements();
        verify(mMockAnnouncementMvpView).showProgress(true);
        verify(mMockAnnouncementMvpView).showProgress(false);
        verify(mMockAnnouncementMvpView).showAnnouncementsEmpty();
    }

    private void stubDataManagerGetAnnouncements(Single single) {
        doReturn(single)
                .when(mMockDataManager)
                .getAnnouncements();
    }
}