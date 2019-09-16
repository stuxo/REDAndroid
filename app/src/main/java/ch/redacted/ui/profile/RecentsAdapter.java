package ch.redacted.ui.profile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import ch.redacted.REDApplication;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.Recents;

/**
 * Created by sxo on 19/01/17.
 */

public class RecentsAdapter extends RecyclerView.Adapter<RecentsAdapter.RecentsViewHolder> {
    private List<Recents.RecentTorrent> mTorrents;
    private Callback mCallback;

    @Inject
    public RecentsAdapter() {
        this.mTorrents = new ArrayList<>();
    }

    @Override
    public RecentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent, parent, false);
        return new RecentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecentsViewHolder holder, final int position) {
        Recents.RecentTorrent release = mTorrents.get(position);
        holder.setId(release.id);
        holder.artist.setHtml(release.getArtists().get(0).name);
        holder.releaseName.setHtml(release.name);
        if (REDApplication.get(holder.artwork.getContext()).getComponent().dataManager().getPreferencesHelper().getLoadImages()) {
            Glide.with(holder.artwork.getContext()).load(release.wikiImage).asBitmap().fitCenter().into(holder.artwork);
        }
        else {
            holder.artwork.setVisibility(View.GONE);
        }
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public int getItemCount() {
        return mTorrents.size();
    }

    public void setRecents(List<Recents.RecentTorrent> list) {
        mTorrents = list;
        this.notifyDataSetChanged();
    }

    class RecentsViewHolder extends RecyclerView.ViewHolder {

        private int id;

        @OnClick(R.id.recent_root)
        public void onRecentItemClicked(){
            mCallback.onRecentClicked(id);
        }

        @BindView(R.id.recent_artwork)
        public ImageView artwork;

        @BindView(R.id.recent_name)
        public HtmlTextView releaseName;

        @BindView(R.id.recent_artist)
        public HtmlTextView artist;

        RecentsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public interface Callback {
        void onRecentClicked(int id);
    }
}