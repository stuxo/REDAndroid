package ch.redacted.ui.forum.threadList;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.ForumView;

/**
 * Created by sxo on 23/12/16.
 */

public class ThreadListAdapter extends RecyclerView.Adapter<ThreadListAdapter.ForumViewHolder> {

    private List<ForumView.Threads> mItems;

    public ThreadListAdapter.Callback mCallback;

    @Inject
    public ThreadListAdapter() {
        this.mItems = new ArrayList<>();
    }

    @Override
    public ThreadListAdapter.ForumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum, parent, false);
        return new ForumViewHolder(view);
    }

    public void setCallback(ThreadListAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(ForumViewHolder holder, int position) {

        ForumView.Threads thread = mItems.get(position);

        TimeZone tz = TimeZone.getDefault();
        int offsetFromUtc = tz.getOffset(new Date().getTime());
        long now = System.currentTimeMillis() - offsetFromUtc;

        holder.lastPostBy.setText(holder.lastPostBy.getContext().getString(R.string.last_post_by, thread.lastAuthorName, DateUtils.getRelativeTimeSpanString(thread.lastTime.getTime(), now, DateUtils.FORMAT_ABBREV_ALL)));
        holder.title.setText(mItems.get(position).title);
        if (Build.VERSION.SDK_INT >= 24) {
            holder.author.setText(Html.fromHtml(thread.authorName, Html.FROM_HTML_MODE_LEGACY));
            holder.title.setText(Html.fromHtml(thread.title, Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.author.setText(Html.fromHtml(thread.authorName));
            holder.title.setText(Html.fromHtml(thread.title));
        }

        if (thread.lastReadPage > 1){
            holder.setLastReadPage(thread.lastReadPage);
        } else {
            holder.setLastReadPage(0);
        }

        if (thread.read){
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.cardView.getContext(), R.color.BackgroundAccentDark));
        } else {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.cardView.getContext(), R.color.Background));
        }

        if (thread.locked) {
            holder.imgLocked.setVisibility(View.VISIBLE);
        } else {
            holder.imgLocked.setVisibility(View.INVISIBLE);
        }
        if (thread.sticky) {
            holder.imgSticky.setVisibility(View.VISIBLE);
        } else {
            holder.imgSticky.setVisibility(View.INVISIBLE);
        }

        holder.setTopicId(thread.topicId);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItems(List<ForumView.Threads> list) {
        mItems = list;
    }


    class ForumViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thread_root)
        public CardView cardView;

        @OnClick(R.id.thread_root)
        public void onThreadClick() {
            mCallback.onThreadClicked(topicId, 1);
        }

        @OnClick(R.id.last_read_button)
        public void onLastReadClick() {
            mCallback.onThreadClicked(topicId, lastReadPage);
        }

        public int lastReadPage;

        public int topicId;

        public void setTopicId(int topicId) {
            this.topicId = topicId;
        }

        public void setLastReadPage(int lastReadPage) {
            this.lastReadPage = lastReadPage;
        }

        @BindView(R.id.thread_author)
        public TextView author;

        @BindView(R.id.thread_name)
        public TextView title;

        @BindView(R.id.thread_last_post_by)
        public TextView lastPostBy;

        @BindView(R.id.img_sticky)
        public ImageView imgSticky;

        @BindView(R.id.img_locked)
        public ImageView imgLocked;

        ForumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface Callback {
        void onThreadClicked(int topicId, int lastReadPage);
    }
}