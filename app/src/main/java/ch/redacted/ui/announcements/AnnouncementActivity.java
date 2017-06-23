package ch.redacted.ui.announcements;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.redacted.app.R;
import ch.redacted.data.model.Announcement;
import ch.redacted.ui.base.BaseDrawerActivity;
import ch.redacted.util.ImageHelper;

public class AnnouncementActivity extends BaseDrawerActivity implements AnnouncementMvpView {

    @Inject AnnouncementPresenter mAnnouncementPresenter;
    @Inject AnnouncementAdapter mAnnouncementAdapter;


    @BindView(R.id.recycler_view) RecyclerView mAnnouncementRecycler;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout mSwipeRefreshContainer;
    @BindView(R.id.text_no_content) TextView mNoAnnouncements;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageView img;

    /**
     * Android activity lifecycle methods
     */

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        mAnnouncementPresenter.attachView(this);
        mAnnouncementRecycler.setHasFixedSize(true);
        mAnnouncementRecycler.setAdapter(mAnnouncementAdapter);
        mAnnouncementRecycler.setLayoutManager(new LinearLayoutManager(this));

        img = ImageHelper.getRippy(mSwipeRefreshContainer);

        mSwipeRefreshContainer.setColorSchemeResources(R.color.accent);
        mSwipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAnnouncementPresenter.loadAnnouncements();
            }
        });
        mAnnouncementPresenter.loadAnnouncements();

        super.onCreateDrawer();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mAnnouncementPresenter.detachView();
    }

    private void animate() {
        Drawable drawable = img.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    /*****
     * MVP View methods implementation
     *****/

    @Override
    public void showAnnouncements(List<Announcement.Announcements> announcements) {
        mAnnouncementAdapter.setAnnouncements(announcements);
        mAnnouncementAdapter.notifyDataSetChanged();
        mNoAnnouncements.setVisibility(View.GONE);
    }

    @Override
    public void showAnnouncementsEmpty() {
        mNoAnnouncements.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {

    }

    @Override
    public void showProgress(boolean show) {
        mSwipeRefreshContainer.setRefreshing(show);
        if (show){
            animate();
        }
    }
}
