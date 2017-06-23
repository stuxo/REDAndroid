package ch.redacted.ui.request;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import javax.inject.Inject;
import ch.redacted.app.R;
import ch.redacted.data.model.Request;
import ch.redacted.ui.artist.ArtistActivity;
import ch.redacted.ui.base.BaseActivity;
import ch.redacted.util.Calculator;
import ch.redacted.util.ReleaseTypes;
import ch.redacted.util.Tags;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class RequestActivity extends BaseActivity implements RequestMvpView {

    private int REQUEST_ID = 0;
    private int ARTIST_ID = 0;

    private boolean isBookmarked;

    @Inject RequestPresenter mRequestPresenter;
    @BindView(R.id.release_image) ImageView releaseImage;
    @BindView(R.id.release_title) HtmlTextView releaseTitle;
    @BindView(R.id.release_artist) Button releaseArtist;
    @BindView(R.id.release_info) TextView releaseInfo;
    @BindView(R.id.release_tags) TextView releaseTags;
    @BindView(R.id.acceptable_bitrates) TextView acceptableBitrates;
    @BindView(R.id.acceptable_formats) TextView acceptableFormats;
    @BindView(R.id.acceptable_media) TextView acceptableMedia;
    @BindView(R.id.bounty) TextView bounty;
    @BindView(R.id.release_description) HtmlTextView releaseDescription;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar) AppBarLayout mAppBarLayout;
    @BindView(R.id.fab) FloatingActionButton bookmarkFab;

    @OnClick(R.id.release_artist)
    public void OnArtistClick(View v) {
        Intent intent = new Intent(this, ArtistActivity.class);
        intent.putExtra("id", ARTIST_ID);
        startActivity(intent);
    }

    @OnClick(R.id.fab)
    public void OnBookMarkToggle(View v) {
//        mRequestPresenter.toggleBookmark(REQUEST_ID, isBookmarked);
    }

    /**
     * Android activity lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_request);
        ButterKnife.bind(this);
        mRequestPresenter.attachView(this);

        REQUEST_ID = getIntent().getExtras().getInt("id");

        mRequestPresenter.loadRequest(REQUEST_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRequestPresenter.detachView();
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
            bookmarkFab.setImageResource(R.drawable.ic_bookmark_24dp);
        }
        else {
            bookmarkFab.setImageResource(R.drawable.ic_bookmark_border_24dp);
        }
    }

    @Override
    public void showRequest(Request request) {
        Glide.with(this).load(request.response.image).asBitmap().fitCenter().into(releaseImage);
        final String info = getString(R.string.release_info, request.response.musicInfo.artists.get(0).name, request.response.releaseName, request.response.year, ReleaseTypes.getReleaseType(request.response.releaseType));
        String releaseInfoSimple = getString(R.string.release_info_simple, request.response.year, ReleaseTypes.getReleaseType(request.response.releaseType), 0);
        releaseArtist.setText(request.response.musicInfo.artists.get(0).name);
        releaseTags.setText(Tags.PrettyTags(3, request.response.tags));
        releaseInfo.setText(releaseInfoSimple);
        releaseTitle.setHtml(request.response.releaseName);
        acceptableBitrates.setText(String.format("%s: %s", getString(R.string.accept_bitrates), request.response.bitrateList.toString()));
        acceptableMedia.setText(String.format("%s: %s", getString(R.string.accept_media), request.response.mediaList.toString()));
        acceptableFormats.setText(String.format("%s: %s", getString(R.string.accept_formats), request.response.formatList.toString()));
        bounty.setText(String.format("%s: %s", getString(R.string.bounty), Calculator.toHumanReadableSize(request.response.totalBounty, 0)));
        ARTIST_ID = request.response.musicInfo.artists.get(0).id;

        isBookmarked = request.response.isBookmarked;

        if (isBookmarked) {
            bookmarkFab.setImageResource(R.drawable.ic_bookmark_24dp);
        } else {
            bookmarkFab.setImageResource(R.drawable.ic_bookmark_border_24dp);
        }

        releaseDescription.setHtml(request.response.description);

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
                    mToolbarLayout.setTitle(info);
                    releaseImage.setFitsSystemWindows(false);
                    isShow = true;
                    bookmarkFab.hide();

                } else if (isShow) {
                    mToolbarLayout.setTitle(" ");
                    releaseImage.setFitsSystemWindows(true);
                    isShow = false;
                    bookmarkFab.show();
                }
            }
        });
    }
}
