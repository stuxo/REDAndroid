package ch.redacted.ui.artist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.Artist;
import ch.redacted.ui.base.BaseActivity;
import ch.redacted.ui.release.ReleaseActivity;
import ch.redacted.util.Tags;

public class ArtistActivity extends BaseActivity implements ArtistMvpView, TorrentGroupAdapter.Callback {

    private boolean showAllDesc = false;
    private boolean isBookmarked;

    @Inject ArtistPresenter mArtistPresenter;
    @Inject TorrentGroupAdapter mTorrentsAdapter;

    @BindView(R.id.release_image) ImageView artistImage;
    @BindView(R.id.release_title) HtmlTextView releaseTitle;
    @BindView(R.id.release_artist) HtmlTextView releaseArtist;
    @BindView(R.id.release_info) TextView artistInfo;
    @BindView(R.id.release_tags) TextView artistTags;
    @BindView(R.id.release_description) HtmlTextView artistDescription;
    @BindView(R.id.recycler_view) RecyclerView mTorrentsRecyclerView;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar) AppBarLayout mAppBarLayout;
    @BindView(R.id.read_more_button) Button mReadMore;
    @BindView(R.id.fab) FloatingActionButton bookmarkFab;

    @OnClick(R.id.fab)
    public void OnBookMarkToggle(View v) {
        mArtistPresenter.toggleArtistBookmark(ARTIST_ID, isBookmarked);
    }

    @OnClick(R.id.read_more_button)
    public void OnReadMoreClick(View v) {
        showAllDesc = !showAllDesc;

        if (showAllDesc) {
            mReadMore.setText(getString(R.string.hide_description));
            artistDescription.setVisibility(View.VISIBLE);
        } else {
            mReadMore.setText(getString(R.string.show_description));
            artistDescription.setVisibility(View.GONE);
        }
    }

    private int ARTIST_ID = 0;

    /**
     * Android activity lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_artist);
        ButterKnife.bind(this);
        mArtistPresenter.attachView(this);

        if (getIntent().getExtras().getInt("id") > 0) {
            ARTIST_ID = getIntent().getExtras().getInt("id");
        } else  if (getIntent().getDataString().contains("?id=")){
            String url = getIntent().getDataString();
            String id = url.substring(url.indexOf("id=") + 3, url.length());
            ARTIST_ID = Integer.parseInt(id);
        }

        mTorrentsAdapter.setCallback(this);
        mTorrentsRecyclerView.setHasFixedSize(true);
        mTorrentsRecyclerView.setAdapter(mTorrentsAdapter);
        mTorrentsRecyclerView.addItemDecoration(new DividerItemDecoration(mTorrentsRecyclerView.getContext(),
            LinearLayoutManager.VERTICAL));
        mTorrentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTorrentsRecyclerView.setNestedScrollingEnabled(false);
        mArtistPresenter.loadArtist(ARTIST_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mArtistPresenter.detachView();
    }

    /*****
     * MVP View methods implementation
     *****/

    @Override
    public void showLoadingProgress(boolean show) {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
        if (isBookmarked) {
            bookmarkFab.setImageResource(R.drawable.ic_notifications_active_48px);
        } else {
            bookmarkFab.setImageResource(R.drawable.ic_notifications_disabled_48px);
        }
    }

    @Override
    public void showTorrents(List<Object> list) {
        mTorrentsAdapter.setTorrents(list);
        mTorrentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showArtist(final Artist artist) {
        Glide.with(this).load(artist.response.image).asBitmap().fitCenter().into(artistImage);
        releaseArtist.setText(artist.response.name);
        artistTags.setText(Tags.PrettyArtistTags(3, artist.response.tags));
        isBookmarked = artist.response.notificationsEnabled;

        if (showAllDesc) {
            mReadMore.setText(getString(R.string.hide_description));
            artistDescription.setVisibility(View.VISIBLE);
        } else {
            mReadMore.setText(getString(R.string.show_description));
            artistDescription.setVisibility(View.GONE);
        }

        if (isBookmarked) {
            bookmarkFab.setImageResource(R.drawable.ic_notifications_active_48px);
        } else {
            bookmarkFab.setImageResource(R.drawable.ic_notifications_disabled_48px);
        }

        artistDescription.setHtml(artist.response.body);

        mArtistPresenter.loadTorrents(artist.response.torrentgroup);

        mToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mToolbarLayout.setTitle(artist.response.name);
                    artistImage.setFitsSystemWindows(false);
                    isShow = true;
                    bookmarkFab.hide();

                } else if (isShow) {
                    mToolbarLayout.setTitle(" ");
                    artistImage.setFitsSystemWindows(true);
                    isShow = false;
                    bookmarkFab.show();
                }
            }
        });
    }

    @Override
    public void onItemClicked(int id) {
        Intent intent = new Intent(this, ReleaseActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
