package ch.redacted.ui.comments;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.TorrentComments;
import ch.redacted.data.model.TorrentGroup;
import ch.redacted.util.Calculator;

/**
 * Created by sxo on 19/01/17.
 */

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TorrentComments.Comments> mComments;

    @Inject
    public CommentsAdapter() {
        this.mComments = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        TimeZone tz = TimeZone.getDefault();
        int offsetFromUtc = tz.getOffset(new Date().getTime());
        long now = System.currentTimeMillis() - offsetFromUtc;

        TorrentComments.Comments comment = mComments.get(position);
        CommentViewHolder holder = (CommentViewHolder) viewHolder;
        holder.body.setHtml(comment.body);
        holder.author.setHtml(comment.userinfo.authorName);
        holder.postTime.setText(DateUtils.getRelativeTimeSpanString(comment.addedTime.getTime(), now, DateUtils.FORMAT_ABBREV_ALL));
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View defaultView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(defaultView);
    }

    public void addComments(List<TorrentComments.Comments> comments) {
        int originalSize = mComments.size();
        mComments.addAll(comments);
        notifyItemRangeInserted(originalSize, comments.size());
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        //todo could add image

        @BindView(R.id.post_author)
        public HtmlTextView author;

        @BindView(R.id.post_time)
        public TextView postTime;

        @BindView(R.id.post_body)
        public HtmlTextView body;

        CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}