package ch.redacted.ui.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.local.DrawerItem;
import ch.redacted.data.model.Profile;
import ch.redacted.ui.announcements.AnnouncementActivity;
import ch.redacted.ui.bookmark.BookmarkActivity;
import ch.redacted.ui.drawer.DrawerAdapter;
import ch.redacted.ui.drawer.DrawerMvpView;
import ch.redacted.ui.drawer.DrawerPresenter;
import ch.redacted.ui.forum.category.CategoryActivity;
import ch.redacted.ui.inbox.InboxActivity;
import ch.redacted.ui.profile.ProfileActivity;
import ch.redacted.ui.search.artist.ArtistSearchActivity;
import ch.redacted.ui.search.collage.CollageSearchActivity;
import ch.redacted.ui.search.request.RequestSearchActivity;
import ch.redacted.ui.search.torrent.TorrentSearchActivity;
import ch.redacted.ui.search.user.UserSearchActivity;
import ch.redacted.ui.settings.SettingsActivity;
import ch.redacted.ui.subscriptions.SubscriptionsActivity;
import ch.redacted.ui.top10.Top10Activity;
import ch.redacted.util.Calculator;

public class BaseDrawerActivity extends BaseActivity implements DrawerMvpView {

    @Inject DrawerPresenter mDrawerPresenter;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.drawer_header) RelativeLayout mDrawerHeader;
    @BindView(R.id.drawer_items) ListView mDrawerItems;
    @BindView(R.id.profile_image) ImageView mProfileImage;
    @BindView(R.id.user_ratio) TextView mRatioText;
    @BindView(R.id.user_buffer) TextView mBufferText;
    @BindView(R.id.user_name) TextView mUsernameText;
    @BindView(R.id.user_upload) TextView mUploadText;
    @BindView(R.id.user_download) TextView mDownloadText;

    @OnClick(R.id.settings) public void onSettingsClick (View v){
        Intent intent = new Intent(BaseDrawerActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private ActionBarDrawerToggle mDrawerToggle;
    private Intent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onCreateDrawer() {
        ButterKnife.bind(this);
        mDrawerPresenter.attachView(this);
        mDrawerPresenter.setupDrawer();
        mDrawerPresenter.loadProfile(this, true);
    }

    @Override public void showError() {

    }

    @Override public void setupDrawer() {
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {

                public void onDrawerClosed(View view) {
                    supportInvalidateOptionsMenu();

                    if (pendingIntent != null) {
                        startActivity(pendingIntent);
                        overridePendingTransition(R.anim.common_fade_in, R.anim.common_fade_out);
                        pendingIntent = null;
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                finish();
                            }
                        }, 1000);
                    }
                }

                public void onDrawerOpened(View drawerView) {
                    supportInvalidateOptionsMenu();
                }
            };

            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.addDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
        }

        mDrawerItems.setAdapter(new DrawerAdapter(this));

        mDrawerItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DrawerItem item = (DrawerItem) adapterView.getItemAtPosition(i);
                switch (item) {
                    case Announcements:
                        pendingIntent = new Intent(BaseDrawerActivity.this, AnnouncementActivity.class);
                        break;
                    case Profile:
                        pendingIntent = new Intent(BaseDrawerActivity.this, ch.redacted.ui.profile.ProfileActivity.class);
                        startActivity(pendingIntent);
                        pendingIntent = null;
                        break;
                    case Forums:
                        pendingIntent = new Intent(BaseDrawerActivity.this, CategoryActivity.class);
                        break;
                    case Top10:
                        pendingIntent = new Intent(BaseDrawerActivity.this, Top10Activity.class);
                        break;
                    case Torrents:
                        pendingIntent = new Intent(BaseDrawerActivity.this, TorrentSearchActivity.class);
                        break;
                    case Users:
                        pendingIntent = new Intent(BaseDrawerActivity.this, UserSearchActivity.class);
                        break;
                    case Artists:
                        pendingIntent = new Intent(BaseDrawerActivity.this, ArtistSearchActivity.class);
                        break;
                    case Requests:
                        pendingIntent = new Intent(BaseDrawerActivity.this, RequestSearchActivity.class);
                        break;
                    case Bookmarks:
                        pendingIntent = new Intent(BaseDrawerActivity.this, BookmarkActivity.class);
                        break;
                    case Inbox:
                        pendingIntent = new Intent(BaseDrawerActivity.this, InboxActivity.class);
                        break;
                    case Subscriptions:
                        pendingIntent = new Intent(BaseDrawerActivity.this, SubscriptionsActivity.class);
                        break;
                    case Collages:
                        pendingIntent = new Intent(BaseDrawerActivity.this, CollageSearchActivity.class);
                }

                mDrawerLayout.closeDrawers();
            }
        });
    }

    @Override public void showProgress(boolean show) {

    }

    @Override public void showProfileInfo(Profile profile) {
        mUsernameText.setText(profile.response.username);
        mBufferText.setText(Calculator.getBuffer(profile) + " GB");
        mRatioText.setText(String.valueOf(profile.response.stats.ratio));
        mUploadText.setText(Calculator.toHumanReadableSize(profile.response.stats.uploaded, 0));
        mDownloadText.setText(Calculator.toHumanReadableSize(profile.response.stats.downloaded, 0));

        if (profile.response.avatar.equals("")) {
            Glide.with(this).load(R.drawable.default_avatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(mProfileImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    mProfileImage.setImageDrawable(circularBitmapDrawable);
                }
            });
        } else {
            Glide.with(this).load(profile.response.avatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(mProfileImage) {
                @Override protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    mProfileImage.setImageDrawable(circularBitmapDrawable);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // This is ASYNCHRONOUS
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M) public void onGenerated(Palette p) {
                                mDrawerHeader.setBackgroundColor(p.getDarkVibrantColor(
                                    getResources().getColor(R.color.secondary_text, getTheme())));
                            }
                        });
                    }
                }
            });
        }
    }

    @OnClick(R.id.drawer_header) void drawerHeaderClick() {
        startActivity(new Intent(BaseDrawerActivity.this, ProfileActivity.class));
        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null) mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }
}
