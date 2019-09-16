package ch.redacted.ui.forum.thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import ch.redacted.REDApplication;
import ch.redacted.util.Emoji;
import ch.redacted.util.ImageHelper;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.ForumThread;

/**
 * Created by sxo on 23/12/16.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ForumViewHolder> {

    private List<ForumThread.Posts> mItems;

    private PostAdapter.Callback mCallback;

    private Context mContext;

    @Inject
    public PostAdapter() {
        this.mItems = new ArrayList<>();
    }

    @Override
    public PostAdapter.ForumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        mContext = parent.getContext();
        return new ForumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ForumViewHolder holder, int position) {
        final ForumThread.Posts post = mItems.get(position);

        TimeZone tz = TimeZone.getDefault();
        int offsetFromUtc = tz.getOffset(new Date().getTime());
        long now = System.currentTimeMillis() - offsetFromUtc;

        holder.setAuthorName(post.author.authorName);
        holder.setBbBody(post.bbBody);
        holder.setHtmlBody(post.body);
        holder.setUserId(post.author.authorId);
        holder.setPostId(post.postId);
        holder.time.setText(DateUtils.getRelativeTimeSpanString(post.addedTime.getTime(), now, DateUtils.FORMAT_ABBREV_ALL));
        holder.author.setHtml(post.author.authorName);
        holder.postBody.setLinkTextColor(ContextCompat.getColor(holder.postBody.getContext(), R.color.primary));
        String body = Emoji.convertEmojis(post.body);
        body = ImageHelper.replaceImageLinks(body);
        if (REDApplication.get(mContext).getComponent().dataManager().getPreferencesHelper().getLoadImages()) {
            if (post.author.avatar.contains("gif")) {
                Glide.with(mContext).load(post.author.avatar).asGif().centerCrop().into(holder.avatar);
            }
            else if (post.author.avatar.equals(""))
                    Glide.with(mContext).load(R.drawable.default_avatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(
                        holder.avatar) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.avatar.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            else {
                Glide.with(mContext).load(post.author.avatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.avatar) {
                    @Override protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.avatar.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
            holder.postBody.setHtml(body, new HtmlHttpImageGetter(holder.postBody));
        }
        else {
            holder.avatar.setVisibility(View.GONE);
            holder.postBody.setHtml(body);
        }
    }

    public void setCallback(PostAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItems(List<ForumThread.Posts> list) {
        mItems = list;
    }

    class ForumViewHolder extends RecyclerView.ViewHolder {

        private String bbBody;

        private String htmlBody;

        private String authorName;

        private int userId;

        private int postId;

        @BindView(R.id.post_time) public TextView time;

        @BindView(R.id.post_author) public HtmlTextView author;

        @BindView(R.id.post_body) public HtmlTextView postBody;

        @BindView(R.id.profile_image) public ImageView avatar;

        @OnClick(R.id.quote_post) public void onQuoteClick(){
            mCallback.onQuoteClicked(bbBody, htmlBody, authorName, postId);
        }

        @OnClick(R.id.user_root) public void onUserClick(){
            mCallback.onProfileClicked(userId);
        }

        ForumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setBbBody(String bbBody) {
            this.bbBody = bbBody;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setHtmlBody(String htmlBody) {
            this.htmlBody = htmlBody;
        }

        public void setPostId(int postId) {
            this.postId = postId;
        }
    }

    interface Callback {
        void onQuoteClicked(String bbBody, String quoteText, String quotedUser, int postId);

        void onProfileClicked(int userId);
    }
}