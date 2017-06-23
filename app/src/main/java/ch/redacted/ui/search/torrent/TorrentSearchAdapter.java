package ch.redacted.ui.search.torrent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.TorrentSearch;
import ch.redacted.util.Tags;

/**
 * Created by sxo on 25/01/17.
 */

public class TorrentSearchAdapter extends RecyclerView.Adapter<TorrentSearchAdapter.TorrentSearchViewHolder> {
    private List<TorrentSearch.Results> mTorrentResults;
    private Callback mCallback;

    @Inject
    public TorrentSearchAdapter() {
        this.mTorrentResults = new ArrayList<>();
    }

    @Override
    public TorrentSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_torrent_search, parent, false);
        return new TorrentSearchViewHolder(view);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(final TorrentSearchViewHolder holder, final int position) {
        TorrentSearch.Results torrentSearch = mTorrentResults.get(position);
        holder.setId(torrentSearch.groupId);
        holder.year.setText(String.format("%d", torrentSearch.groupYear));
        holder.release_name.setHtml(torrentSearch.groupName);
        //artist is null for sheet music
        if (torrentSearch.artist != null) {
            holder.artist.setHtml(torrentSearch.artist);
        }
        holder.tags.setText(Tags.PrettyTags(3, torrentSearch.tags));
    }

    @Override
    public int getItemCount() {
        return mTorrentResults.size();
    }

    public void setTorrentResults(List<TorrentSearch.Results> list) {
        mTorrentResults = list;
    }

    class TorrentSearchViewHolder extends RecyclerView.ViewHolder {

        private int torrentId;

        @BindView(R.id.tags)
        public TextView tags;

        @BindView(R.id.year)
        public TextView year;

        @BindView(R.id.artist)
        public HtmlTextView artist;

        @BindView(R.id.release_name)
        public HtmlTextView release_name;

        @OnClick(R.id.torrent_search_view_root)
        void OnTorrentClick(View view) {
            mCallback.onItemClicked(torrentId);
        }

        TorrentSearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setId(int id) {
            this.torrentId = id;
        }
    }

    interface Callback {
        void onItemClicked(int id);
    }
}
