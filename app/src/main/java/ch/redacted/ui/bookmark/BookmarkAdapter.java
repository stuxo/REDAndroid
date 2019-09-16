package ch.redacted.ui.bookmark;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.TorrentBookmark;
import ch.redacted.util.ReleaseTypes;
import ch.redacted.util.Tags;

/**
 * Created by sxo on 23/12/16.
 */

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkHolder> {
    private List<TorrentBookmark.Bookmarks> mItems;
    private Callback mCallback;

    @Inject
    public BookmarkAdapter() {
        this.mItems = new ArrayList<>();
    }


    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public BookmarkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_torrent_bookmark, parent, false);
        return new BookmarkHolder(view);
    }

    @Override public void onBindViewHolder(final BookmarkHolder holder, int position) {
        TorrentBookmark.Bookmarks results = mItems.get(position);
        holder.setId(results.id);
        holder.root.setOnClickListener(view -> mCallback.onItemClicked(holder.id));

        Glide.with(holder.image.getContext()).load(results.image).asBitmap().fitCenter().into(holder.image);

        holder.releaseName.setHtml(results.name);
        holder.releaseYear.setText(String.format(Locale.getDefault(), "%d", results.year));
        holder.releaseTags.setText(Tags.PrettyTags(3, results.tagList.split(" ")));
        holder.releaseType.setText(ReleaseTypes.getReleaseType(Integer.parseInt(results.releaseType)));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setResults(List<TorrentBookmark.Bookmarks> list) {
        mItems = list;
    }

    class BookmarkHolder extends RecyclerView.ViewHolder {

        private int id;

        @BindView(R.id.torrent_bookmark_root) public RelativeLayout root;

        @BindView(R.id.release_name) public HtmlTextView releaseName;

        @BindView(R.id.release_tags) public TextView releaseTags;

        @BindView(R.id.release_type) public TextView releaseType;

        @BindView(R.id.year) public TextView releaseYear;

        @BindView(R.id.album_art) public ImageView image;

        @OnClick(R.id.remove_bookmark)
        public void onRemoveBookmark(){
            mCallback.onRemoveClicked(id);
        }

        @OnClick(R.id.download_default)
        public void onDownloadBookmark(){
            mCallback.onDownloadClicked(id);
        }

        public BookmarkHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setId(int id){
            this.id = id;
        }
    }

    interface Callback {
        void onItemClicked(int id);
        void onRemoveClicked(int id);
        void onDownloadClicked(int id);
    }
}