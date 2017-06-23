package ch.redacted.ui.announcements;

import java.util.List;

import ch.redacted.ui.base.MvpView;
import ch.redacted.data.model.Announcement;

public interface AnnouncementMvpView extends MvpView {

    void showAnnouncements(List<Announcement.Announcements> announcements);

    void showAnnouncementsEmpty();

    void showError();

    void showProgress(boolean show);
}
