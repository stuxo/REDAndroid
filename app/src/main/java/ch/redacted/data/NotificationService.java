package ch.redacted.data;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import ch.redacted.app.R;
import ch.redacted.ui.inbox.conversation.ConversationActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import ch.redacted.REDApplication;
import ch.redacted.data.model.Conversations;
import ch.redacted.data.model.Index;
import ch.redacted.data.model.Subscription;
import ch.redacted.ui.forum.thread.ThreadActivity;

/**
 * Service to handle callbacks from the JobScheduler. Requests scheduled with the JobScheduler
 * ultimately land on this service's "onStartJob" method. It runs jobs for a specific amount of time
 * and finishes them. It keeps the activity updated with changes via a Messenger.
 */
public class NotificationService extends JobService {

    private static final String TAG = NotificationService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        REDApplication.get(getApplicationContext()).getComponent().dataManager().
                loadIndex().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<Index>() {
                    @Override
                    public void onSuccess(Index index) {
                        if (index.response.notifications != null){
                            checkNotifications(index);
                        }
                        jobFinished(params, false);
                    }

                    @Override
                    public void onError(Throwable error) {
                        jobFinished(params, false);
                    }
                });

        return true;
    }

    private void checkNotifications(Index index) {

        if (index.response.notifications.messages > 0){

            REDApplication.get(getApplicationContext()).getComponent().dataManager().
                    loadInbox("inbox").observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DisposableSingleObserver<Conversations>() {
                        @Override
                        public void onSuccess(Conversations conversations) {
                            for (Conversations.Messages message : conversations.response.messages)
                            if (message.unread){
                                createMessageNotification(1, "New message", "You have a message from " + message.username, message.convId);
                            }
                        }

                        @Override
                        public void onError(Throwable error) {
                        }
                    });
        }

        if (index.response.notifications.newSubscriptions){
            REDApplication.get(getApplicationContext()).getComponent().dataManager().
                loadSubscriptions(false).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<Subscription>() {
                    @Override
                    public void onSuccess(Subscription subscriptions) {
                        for (Subscription.Threads thread : subscriptions.response.threads)
                            if (thread.mnew){
                                createSubscriptionNotification(2, "New Subscription", "New post in " + thread.threadTitle, thread.forumId, thread.threadId, thread.lastPostId);
                            }
                    }

                    @Override
                    public void onError(Throwable error) {
                    }
                });
        }

        if (index.response.notifications.newAnnouncement){
            createNotification(3, "New Announcement", "There has been a recent site announcement!");
        }

        if (index.response.notifications.newBlog){
            //TODO open blog post view
            createNotification(4, "New Blog Post", "There has been a recent blog post!");
        }
    }

    public void createNotification(int notificationId, String title, String body){
        int icon = R.drawable.ic_launcher;

        Notification notification =
            new Notification.Builder(this).setCategory(Notification.CATEGORY_MESSAGE)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager =
            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);
    }

    public void createSubscriptionNotification(int notificationId, String title, String body, int forumId, int threadId, int postId){
        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, ThreadActivity.class);
        resultIntent.putExtra("forumId", forumId);
        resultIntent.putExtra("topicId", threadId);
        resultIntent.putExtra("lastPostId", postId);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ThreadActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
            stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
            );
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

    public void createMessageNotification(int notificationId, String title, String body, int id){
        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, ConversationActivity.class);
        resultIntent.putExtra("id", id);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ConversationActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
            stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
            );
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // Return false to drop the job.
        return false;
    }
}