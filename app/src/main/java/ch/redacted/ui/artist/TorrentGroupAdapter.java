package ch.redacted.ui.artist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.redacted.util.Tags;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.Artist;
import ch.redacted.util.ReleaseTypes;

/**
 * Created by sxo on 19/01/17.
 */

public class TorrentGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> mTorrents;
    private Callback mCallback;
    private int HEADER_VIEW_TYPE = 0;
    private int GROUP_VIEW_TYPE = 1;

    @Inject
    public TorrentGroupAdapter() {
        this.mTorrents = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 0:
                View headerView =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_header, parent, false);
                return new HeaderViewHolder(headerView);
            case 1:
                View torrentView =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_torrent_group, parent, false);
                return new TorrentGroupViewHolder(torrentView);
            default:
                View defaultView =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_header, parent, false);
                return new HeaderViewHolder(defaultView);
        }
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()) {
            case 0:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.releaseType.setText(ReleaseTypes.getReleaseType( (Integer) mTorrents.get(position)));
                break;
            case 1:
                TorrentGroupViewHolder torrentGroupViewHolder = (TorrentGroupViewHolder) holder;
                Artist.Torrentgroup group = (Artist.Torrentgroup) mTorrents.get(position);
                torrentGroupViewHolder.setId(group.groupId);
                torrentGroupViewHolder.releaseTags.setText(Tags.PrettyTags(3, group.tags));
                torrentGroupViewHolder.title.setHtml(torrentGroupViewHolder.title.getContext().getString(R.string.release_title, group.groupYear, group.groupName));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mTorrents.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = HEADER_VIEW_TYPE;
        //Group header
        if (mTorrents.get(position) instanceof Integer) {
            type = HEADER_VIEW_TYPE;
        }
        //Group item
        else if (mTorrents.get(position) instanceof Artist.Torrentgroup) {
            type = GROUP_VIEW_TYPE;
        }
        return type;
    }

    public void setTorrents(List<Object> list) {
        mTorrents = list;
    }

    class TorrentGroupViewHolder extends RecyclerView.ViewHolder {

        private int releaseId;

        @BindView(R.id.release_title)
        public HtmlTextView title;

        @BindView(R.id.release_tags)
        public TextView releaseTags;

        @OnClick(R.id.torrent_group_root)
        public void OnTorrentItemClick(){
            mCallback.onItemClicked(releaseId);
        }

        TorrentGroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setId(int id){
            this.releaseId = id;
        }

    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.release_type)
        public TextView releaseType;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface Callback {
        void onItemClicked(int id);
    }
}