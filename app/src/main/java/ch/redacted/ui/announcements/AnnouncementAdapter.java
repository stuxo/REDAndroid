package ch.redacted.ui.announcements;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.REDApplication;
import ch.redacted.app.R;
import ch.redacted.data.model.Announcement;
import ch.redacted.util.ImageHelper;

/**
 * Created by sxo on 23/12/16.
 */

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementHolder> {
    private List<Announcement.Announcements> mAnnouncements;

    private static final int COLLAPSED_POST_LINES = 10;

    @Inject
    public AnnouncementAdapter() {
        this.mAnnouncements = new ArrayList<>();
    }

    @Override
    public AnnouncementHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcement, parent, false);
        return new AnnouncementHolder(view);
    }

    @Override
    public void onBindViewHolder(final AnnouncementHolder holder, final int position) {
        Announcement.Announcements announcement = mAnnouncements.get(position);
        holder.title.setText(announcement.title);

        holder.showFullPost = false;

        holder.time.setText(DateUtils.getRelativeTimeSpanString(
                announcement.newsTime.getTime(), new Date().getTime(),
                DateUtils.FORMAT_ABBREV_ALL));

        if (REDApplication.get(holder.announcementImage.getContext()).getComponent().dataManager().getPreferencesHelper().getLoadImages()) {
            String firstImageUrl = ImageHelper.getFirstImageLink(announcement.body);
            if (firstImageUrl.length() > 0) {
                Glide.with(holder.announcementImage.getContext()).load(firstImageUrl).asBitmap().fitCenter().into(holder.announcementImage);
            }
        }

        holder.fullText = announcement.body;
        holder.body.setLinkTextColor(ContextCompat.getColor(holder.body.getContext(), R.color.primary));
        if (announcement.body.length() > 25000) {
            holder.body.setText(holder.body.getContext().getString(R.string.post_too_long));
        } else {
            holder.body.setHtml(announcement.body,
                    new HtmlHttpImageGetter(holder.body));
        }

        holder.body.setMaxLines(COLLAPSED_POST_LINES);

    }

    @Override
    public int getItemCount() {
        return mAnnouncements.size();
    }

    public void setAnnouncements(List<Announcement.Announcements> list) {
        mAnnouncements = list;
    }

    class AnnouncementHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_container)
        RelativeLayout layoutContainer;

        @BindView(R.id.announcement_title)
        public TextView title;

        @BindView(R.id.announcement_time)
        public TextView time;

        @BindView(R.id.announcement_body)
        HtmlTextView body;

        @BindView(R.id.announcement_image)
        ImageView announcementImage;

        public String fullText;

        @BindView(R.id.announcement_read_more)
        Button readMore;

        boolean showFullPost = false;

        @OnClick(R.id.announcement_read_more)
        void readMoreClick() {
            toggleView();
        }

        AnnouncementHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void toggleView() {
            showFullPost = !showFullPost;
            if (showFullPost) {
                body.setLines(COLLAPSED_POST_LINES);
                body.setHtml(fullText,
                        new HtmlHttpImageGetter(body));
                readMore.setText(readMore.getContext().getString(R.string.show_all));
            } else {
                readMore.setText(readMore.getContext().getString(R.string.show_less));
                body.setMaxLines(Integer.MAX_VALUE);
            }
        }
    }
}