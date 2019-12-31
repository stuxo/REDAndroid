package ch.redacted.ui.collage;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.redacted.REDApplication;
import ch.redacted.app.R;
import ch.redacted.data.model.Collage;

public class CollageAdapter extends RecyclerView.Adapter<CollageAdapter.CollageHolder> {
    private Collage mCollage;
    private Callback mCallback;
    private List<Collage.Response.TorrentGroup> mTorrentGroups;

    @Inject
    CollageAdapter() {
        this.mCollage = new Collage();
        this.mTorrentGroups = new ArrayList<>();
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public CollageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collage, parent, false);
        return new CollageAdapter.CollageHolder(view);
    }

    @Override
    public void onBindViewHolder(final CollageHolder holder, int position) {
        Collage.Response.TorrentGroup torrentGroup = mCollage.response.torrentGroups.get(position);
        holder.setId(torrentGroup.id);
        holder.root.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.image.setTransitionName("thumbnailTransition");
                mCallback.onItemClicked(holder.id, holder.image);
            } else {
                mCallback.onItemClicked(holder.id, null);
            }
        });

        holder.artistName.setHtml(torrentGroup.getArtistName());
        holder.releaseName.setHtml(torrentGroup.name);
        holder.releaseTags.setText(torrentGroup.tagList);
        String releaseInfo = "[" + torrentGroup.year.toString() + "]";
        if (!torrentGroup.recordLabel.equals("") || !torrentGroup.catalogueNumber.equals(""))
            releaseInfo += " [" + torrentGroup.recordLabel + " - " + torrentGroup.catalogueNumber + "]";
        holder.releaseInfo.setText(releaseInfo);

        if (REDApplication.get(holder.image.getContext()).getComponent().dataManager().getPreferencesHelper().getLoadImages()) {
            Glide.with(holder.image.getContext()).load(torrentGroup.wikiImage).asBitmap().fitCenter().into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mTorrentGroups.size();
    }

    public void setCollage(Collage collage) {
        mCollage = collage;
        // we do this hack because otherwise we get a null pointer exception
        // if we want to access the non existing torrent groups during getItemCount
        mTorrentGroups = mCollage.response.torrentGroups;
    }

    interface Callback {
        void onItemClicked(int id, ImageView sharedImage);
    }

    class CollageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.top_10_root)
        public LinearLayout root;

        @BindView(R.id.album_art)
        public ImageView image;

        @BindView(R.id.artist_name)
        public HtmlTextView artistName;

        @BindView(R.id.release_name)
        public HtmlTextView releaseName;

        @BindView(R.id.release_tags)
        public TextView releaseTags;

        @BindView(R.id.release_info)
        public TextView releaseInfo;

        private int id;


        CollageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setId(int id) {
            this.id = id;
        }
    }
}
