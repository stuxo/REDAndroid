package ch.redacted.ui.forum.threadList;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.redacted.app.R;
import ch.redacted.data.model.ForumView;
import ch.redacted.ui.base.BaseActivity;
import ch.redacted.ui.forum.thread.ThreadActivity;
import ch.redacted.ui.view.ForumNavigationView;
import ch.redacted.util.ImageHelper;

public class ThreadListActivity extends BaseActivity implements ThreadListMvpView, ThreadListAdapter.Callback {
    @Inject ThreadListPresenter mThreadPresenter;
    @Inject ThreadListAdapter mThreadAdapter;

    @BindView(R.id.forum_nav) ForumNavigationView forumNav;
    @BindView(R.id.recycler_view) RecyclerView mThreadRecycler;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout mSwipeRefreshContainer;
    @BindView(R.id.text_no_content) TextView mNoThreads;

    private int page = 1;
    private int forumId = 0;
    private ImageView img;

    /**
     * Android activity lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.forum_view);
        ButterKnife.bind(this);

        mThreadPresenter.attachView(this);
        img = ImageHelper.getRippy(mSwipeRefreshContainer);
        mThreadRecycler.setHasFixedSize(true);
        mThreadRecycler.setAdapter(mThreadAdapter);
        mThreadRecycler.setLayoutManager(new LinearLayoutManager(this));
        mThreadAdapter.setCallback(this);
        mSwipeRefreshContainer.setColorSchemeResources(R.color.accent);
        mSwipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mThreadPresenter.isViewAttached()) {
                    mThreadPresenter.loadThreads(forumId, page);
                }
            }
        });
        forumId = getIntent().getExtras().getInt("forumId");

        mThreadPresenter.loadThreads(forumId, 1);

        forumNav.setNavigationEventListener(new ForumNavigationView.OnNavigationEventListener() {
            @Override
            public void onNavigationChanged() {
                mThreadPresenter.loadThreads(forumId, forumNav.getCurrentPageNumber());
            }
        });
    }

    private void animate() {
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        img.startAnimation(rotation);
    }

    @Override
    public void showThreads(ForumView threads) {
        getSupportActionBar().setTitle(threads.response.forumName);
        forumNav.setPageCount(threads.response.pages);
        mThreadAdapter.setItems(threads.response.threads);
        mThreadAdapter.notifyDataSetChanged();
    }

    @Override
    public void showThreadsEmpty() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showProgress(boolean show) {
		mSwipeRefreshContainer.setRefreshing(show);
        if (show) {
            animate();
        }
    }

    @Override
    public void onThreadClicked(int topicId, int lastReadPage) {
        Intent intent = new Intent(this, ThreadActivity.class);
        intent.putExtra("topicId", topicId);
        intent.putExtra("lastReadPage", lastReadPage);
        intent.putExtra("forumId", forumId);
        startActivity(intent);
    }
}
