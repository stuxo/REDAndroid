package ch.redacted.ui.top10;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import jp.shts.android.library.TriangleLabelView;
import ch.redacted.REDApplication;
import ch.redacted.app.R;
import ch.redacted.data.model.Top10;
import ch.redacted.util.Tags;

import org.sufficientlysecure.htmltextview.HtmlTextView;

/**
 * Created by sxo on 23/12/16.
 */

public class Top10Adapter extends RecyclerView.Adapter<Top10Adapter.Top10Holder> {
    private List<Top10.Results> mItems;
    private Callback mCallback;

    @Inject
    public Top10Adapter() {
        this.mItems = new ArrayList<>();
    }


    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public Top10Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top10, parent, false);
        return new Top10Adapter.Top10Holder(view);
    }

    @Override public void onBindViewHolder(final Top10Holder holder, int position) {
        Top10.Results results = mItems.get(position);
        holder.setId(results.groupId);
        holder.root.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.image.setTransitionName("thumbnailTransition");
                mCallback.onItemClicked(holder.id, holder.image);
            }
            else {
                mCallback.onItemClicked(holder.id, null);
            }
        });

        holder.artistName.setHtml(results.artist);
        holder.releaseName.setHtml(results.groupName);
        holder.releaseInfo.setText(String.format("[%s] - [%s/%s/%s]", results.groupYear, results.remasterTitle.equals("") ? "Original Release" : results.remasterTitle, results.format, results.encoding));
        holder.releaseTags.setText(Tags.PrettyTags(3, results.tags));
        holder.releaseSnatches.setText(String.format(Locale.getDefault(), "%d", results.snatched));
        holder.releaseSeeders.setText(String.format(Locale.getDefault(), "%d", results.seeders));
        holder.releaseLeechers.setText(String.format(Locale.getDefault(), "%d", results.leechers));
        if (REDApplication.get(holder.image.getContext()).getComponent().dataManager().getPreferencesHelper().getLoadImages()) {
            Glide.with(holder.image.getContext()).load(results.wikiImage).asBitmap().fitCenter().into(holder.image);
        }
        else {
            holder.image.setVisibility(View.GONE);
        }

        holder.top10rank.setPrimaryText(String.format("%s", position + 1));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setResults(List<Top10.Results> list) {
        mItems = list;
    }

    class Top10Holder extends RecyclerView.ViewHolder {

        private int id;

        @BindView(R.id.top_10_root) public LinearLayout root;

        @BindView(R.id.artist_name) public HtmlTextView artistName;

        @BindView(R.id.release_name) public HtmlTextView releaseName;

        @BindView(R.id.release_info) public TextView releaseInfo;

        @BindView(R.id.release_tags) public TextView releaseTags;

        @BindView(R.id.snatch_count) public TextView releaseSnatches;

        @BindView(R.id.seeders_count) public TextView releaseSeeders;

        @BindView(R.id.leechers_count) public TextView releaseLeechers;

        @BindView(R.id.album_art) public ImageView image;

        @BindView(R.id.rank) public TriangleLabelView top10rank;
        Top10Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setId(int id){
            this.id = id;
        }
    }

    interface Callback {
        void onItemClicked(int id, ImageView sharedImage);
    }
}