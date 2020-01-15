package ch.redacted.injection.component;

import ch.redacted.injection.PerActivity;
import ch.redacted.injection.module.ActivityModule;
import ch.redacted.ui.announcements.AnnouncementActivity;
import ch.redacted.ui.artist.ArtistActivity;
import ch.redacted.ui.bookmark.BookmarkActivity;
import ch.redacted.ui.collage.CollageActivity;
import ch.redacted.ui.forum.category.CategoryActivity;
import ch.redacted.ui.forum.thread.ThreadActivity;
import ch.redacted.ui.forum.threadList.ThreadListActivity;
import ch.redacted.ui.inbox.InboxActivity;
import ch.redacted.ui.inbox.conversation.ConversationActivity;
import ch.redacted.ui.login.LoginActivity;
import ch.redacted.ui.profile.ProfileActivity;
import ch.redacted.ui.release.ReleaseActivity;
import ch.redacted.ui.reply.ReplyActivity;
import ch.redacted.ui.request.RequestActivity;
import ch.redacted.ui.search.artist.ArtistSearchActivity;
import ch.redacted.ui.search.collage.CollageSearchActivity;
import ch.redacted.ui.search.request.RequestSearchActivity;
import ch.redacted.ui.search.torrent.TorrentSearchActivity;
import ch.redacted.ui.search.user.UserSearchActivity;
import ch.redacted.ui.subscriptions.SubscriptionsActivity;
import ch.redacted.ui.top10.Top10Activity;
import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(ProfileActivity profileActivity);
    void inject(LoginActivity loginActivity);
    void inject(AnnouncementActivity announcementActivity);
    void inject(CategoryActivity categoryActivity);
    void inject(ThreadListActivity threadActivity);
    void inject(ThreadActivity threadActivity);
    void inject(Top10Activity top10Activity);
    void inject(ReleaseActivity releaseActivity);
    void inject(UserSearchActivity userSearchActivity);
    void inject(TorrentSearchActivity torrentSearchActivity);
    void inject(RequestSearchActivity requestSearchActivity);

    void inject(CollageSearchActivity collageSearchActivity);
    void inject(ArtistSearchActivity artistSearchActivity);
    void inject(ArtistActivity artistActivity);

    void inject(CollageActivity collageActivity);
    void inject(RequestActivity requestActivity);
    void inject(BookmarkActivity bookmarkActivity);
    void inject(InboxActivity inboxActivity);
    void inject(ConversationActivity conversationActivity);
    void inject(ReplyActivity replyActivity);
    void inject(SubscriptionsActivity subscriptionActivity);
}
