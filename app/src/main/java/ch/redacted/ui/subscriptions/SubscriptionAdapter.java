package ch.redacted.ui.subscriptions;

import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.Subscription;

/**
 * Created by sxo on 19/01/17.
 */

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ForumViewHolder> {

    private List<Subscription.Threads> mItems;

    public SubscriptionAdapter.Callback mCallback;

    @Inject
    public SubscriptionAdapter() {
        this.mItems = new ArrayList<>();
    }

    @Override
    public SubscriptionAdapter.ForumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subscribed_forum, parent, false);
        return new ForumViewHolder(view);
    }

    public void setCallback(SubscriptionAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(ForumViewHolder holder, int position) {

        Subscription.Threads thread = mItems.get(position);

        if (Build.VERSION.SDK_INT >= 24) {
            holder.forumName.setText(Html.fromHtml(thread.forumName, Html.FROM_HTML_MODE_LEGACY));
            holder.title.setText(Html.fromHtml(thread.threadTitle, Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.forumName.setText(Html.fromHtml(thread.forumName));
            holder.title.setText(Html.fromHtml(thread.threadTitle));
        }

        if (thread.lastPostId > 1) {
            holder.setLastPostId(thread.lastPostId);
        }

        if (thread.locked) {
            holder.imgLocked.setVisibility(View.VISIBLE);
        } else {
            holder.imgLocked.setVisibility(View.INVISIBLE);
        }

        holder.setTopicId(thread.threadId);
        holder.setPostId(thread.postId);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setSubscriptions(List<Subscription.Threads> list) {
        mItems = list;
    }


    class ForumViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.subscription_root)
        public RelativeLayout cardView;

        @OnClick(R.id.subscription_root)
        public void onSubscriptionClicked() {
            mCallback.onSubscriptionClicked(topicId, lastPostId, postId);
        }

        @OnClick(R.id.last_read_button)
        public void onLastReadClick() {
            mCallback.onSubscriptionClicked(topicId, lastPostId, postId);
        }

        public int lastPostId;

        public int topicId;

        public int postId;

        public void setTopicId(int topicId) {
            this.topicId = topicId;
        }

        public void setLastPostId(int id) {
            this.lastPostId = id;
        }


        @BindView(R.id.forum_name)
        public TextView forumName;

        @BindView(R.id.thread_name)
        public TextView title;

        @BindView(R.id.img_locked)
        public ImageView imgLocked;

        ForumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setPostId(int postId) {
            this.postId = postId;
        }
    }

    interface Callback {
        void onSubscriptionClicked(int topicId, int lastReadPage, int postId);
    }
}