package ch.redacted.ui.subscriptions;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.redacted.app.R;
import ch.redacted.data.model.Subscription;
import ch.redacted.ui.base.BaseDrawerActivity;
import ch.redacted.ui.forum.thread.ThreadActivity;
import ch.redacted.util.ImageHelper;

public class SubscriptionsActivity extends BaseDrawerActivity implements SubscriptionsMvpView, SubscriptionAdapter.Callback {

    private static final String UNREAD = "unread";
    private static final String READ = "read";

    @Inject SubscriptionsPresenter mSubscriptionPresenter;
    @Inject SubscriptionAdapter mSubscriptionsAdapter;

    @BindView(R.id.recycler_view) RecyclerView mSubscriptionRecyclerView;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout mSwipeRefreshContainer;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.text_no_content) TextView mNoContentView;
    @BindView(R.id.snackbar_anchor) CoordinatorLayout snackbarAnchor;

    ImageView img;

    String type = "unread";

    /**
     * Android activity lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_subscriptions);
        ButterKnife.bind(this);
        mSubscriptionPresenter.attachView(this);

        img = ImageHelper.getRippy(mSwipeRefreshContainer);

        super.onCreateDrawer();

        mSubscriptionsAdapter.setCallback(this);
        mSubscriptionRecyclerView.setHasFixedSize(true);
        mSubscriptionRecyclerView.setAdapter(mSubscriptionsAdapter);
        mSubscriptionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSubscriptionRecyclerView.setNestedScrollingEnabled(false);
        mSubscriptionRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        mSubscriptionPresenter.loadSubscriptions(type);
        mSwipeRefreshContainer.setColorSchemeResources(R.color.accent);

        mSwipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSubscriptionPresenter.loadSubscriptions(type);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_unread:
                                mSubscriptionRecyclerView.setVisibility(View.VISIBLE);
                                if (!type.equals(UNREAD)) {
                                    type = UNREAD;
                                    mSubscriptionPresenter.loadSubscriptions(type);
                                }
                                break;
                            case R.id.action_read:
                                mSubscriptionRecyclerView.setVisibility(View.VISIBLE);
                                if (!type.equals(READ)) {
                                    type = READ;
                                    mSubscriptionPresenter.loadSubscriptions(type);
                                }
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptionPresenter.detachView();
    }

    @Override
    public void showSubscriptionsEmpty() {
        mSubscriptionsAdapter.setSubscriptions(new ArrayList<Subscription.Threads>());
        mNoContentView.setVisibility(View.VISIBLE);
    }

    private void animate() {
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        img.startAnimation(rotation);
    }

    /*****
     * MVP View methods implementation
     *****/

    @Override
    public void showLoadingProgress(boolean show) {
        mSwipeRefreshContainer.setRefreshing(show);
        if (show) {
            animate();
        }
    }

    @Override
    public void showError(String message) {

    }

    @Override public void showSnackbar(String message) {
        Snackbar.make(snackbarAnchor, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSubscriptions(List<Subscription.Threads> threads) {
        mNoContentView.setVisibility(View.GONE);
        mSubscriptionsAdapter.setSubscriptions(threads);
    }


    @Override
    public void onSubscriptionClicked(int topicId, int lastPostId, int postId) {
        Intent intent = new Intent(this, ThreadActivity.class);
        intent.putExtra("topicId", topicId);
        intent.putExtra("lastPostId", lastPostId);
        startActivity(intent);
    }
}
