package ch.redacted.ui.release;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.TorrentGroup;
import ch.redacted.util.Calculator;

/**
 * Created by sxo on 19/01/17.
 */

public class TorrentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_VIEW_TYPE = 1;
    private static final int RELEASE_VIEW_TYPE = 2;
    private List<Object> mTorrents;
    private Callback mCallback;

    @Inject
    public TorrentsAdapter() {
        this.mTorrents = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        switch (viewHolder.getItemViewType()) {
            case HEADER_VIEW_TYPE:
                String release = mTorrents.get(position).toString();
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;

                if (release.trim().equals("0")) {
                    headerViewHolder.releaseType.setText(headerViewHolder.releaseType.getContext().getString(R.string.original_release));
                } else {
                    headerViewHolder.releaseType.setText(release);
                }
                break;
            case RELEASE_VIEW_TYPE:
                TorrentViewHolder holder = (TorrentViewHolder) viewHolder;
                TorrentGroup.Torrents torrent = (TorrentGroup.Torrents) mTorrents.get(position);
                String info = holder.title.getContext().getString(R.string.torrent_format, torrent.media, torrent.format, torrent.encoding);
                holder.title.setText(info);

                if (!torrent.freeTorrent) {
                    holder.freeleech.setVisibility(View.GONE);
                }
                if (!torrent.reported) {
                    holder.reported.setVisibility(View.GONE);
                }
                if (!torrent.scene) {
                    holder.scene.setVisibility(View.GONE);
                }

                holder.setId(torrent.id);
                holder.snatchCount.setText(String.format("%d", torrent.snatched));
                holder.seedersCount.setText(String.format("%d", torrent.seeders));
                holder.leechersCount.setText(String.format("%d", torrent.leechers));
                holder.size.setText(String.format("%s", Calculator.toHumanReadableSize(torrent.size, 0)));

                String time = DateUtils.getRelativeDateTimeString(holder.uploader.getContext(), torrent.time.getTime(), System.currentTimeMillis(), 0, DateUtils.FORMAT_ABBREV_RELATIVE).toString();

                holder.uploader.setHtml(holder.uploader.getResources().getString(R.string.uploaded_by, torrent.username, time));
                holder.fileList.setText(prettyFiles(torrent.fileList));
                break;
        }
    }


    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    private String prettyFiles(String fileList) {
        String[] files = fileList.split("\\|\\|\\|");
        String prettyFiles = "";
        for (String file: files) {
            String name = file.substring(0, file.indexOf("{"));
            String size = Calculator.toHumanReadableSize(Long.parseLong(file.substring(file.indexOf("{{{")).replaceAll("[^0-9]", "")), 0);
            prettyFiles += String.format("%s (%s) %s", Html.fromHtml(name), size, "\n");
        }
        return prettyFiles;
    }

    @Override
    public int getItemCount() {
        return mTorrents.size();
    }

    public void setTorrents(List<Object> list) {
        mTorrents = list;
    }

    @Override
    public int getItemViewType(int position) {
        int type = HEADER_VIEW_TYPE;
        //Group header
        if (mTorrents.get(position) instanceof String) {
            type = HEADER_VIEW_TYPE;
        }
        //Group item
        else if (mTorrents.get(position) instanceof TorrentGroup.Torrents) {
            type = RELEASE_VIEW_TYPE;
        }
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case HEADER_VIEW_TYPE:
                View headerView =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_header, parent, false);
                return new HeaderViewHolder(headerView);
            case RELEASE_VIEW_TYPE:
                View torrentView =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_torrent, parent, false);
                return new TorrentViewHolder(torrentView);
            default:
                View defaultView =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_torrent, parent, false);
                return new TorrentViewHolder(defaultView);
        }
    }

    class TorrentViewHolder extends RecyclerView.ViewHolder {

        private int id;

        @BindView(R.id.format_info_simple)
        public HtmlTextView title;

        @BindView(R.id.freeleech)
        public ImageView freeleech;

        @BindView(R.id.reported)
        public ImageView reported;

        @BindView(R.id.scene)
        public TextView scene;

        @BindView(R.id.snatch_count)
        public TextView snatchCount;

        @BindView(R.id.seeders_count)
        public TextView seedersCount;

        @BindView(R.id.leechers_count)
        public TextView leechersCount;

        @BindView(R.id.size)
        public TextView size;

        @BindView(R.id.expanding_layout)
        public RelativeLayout expandingLayout;

        private void setId(int id){
            this.id = id;
        }

        @OnClick(R.id.download_default)
        public void onDownloadClick(){
            mCallback.onDownloadClicked(id);
        }

        //TODO handle user link
        @BindView(R.id.uploader)
        public HtmlTextView uploader;

        @BindView(R.id.uploaded_time)
        public TextView uploadedTime;

        @BindView(R.id.file_list)
        public TextView fileList;

        TorrentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
        void onDownloadClicked(int id);
    }
}