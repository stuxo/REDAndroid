package ch.redacted.ui.inbox;

import android.content.Intent;
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

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.redacted.app.R;
import ch.redacted.data.model.Conversations;
import ch.redacted.ui.base.BaseDrawerActivity;
import ch.redacted.ui.inbox.conversation.ConversationActivity;
import ch.redacted.util.ImageHelper;

public class InboxActivity extends BaseDrawerActivity implements InboxMvpView, ConversationAdapter.Callback {

    @Inject InboxPresenter mInboxPresenter;
    @Inject ConversationAdapter mInboxAdapter;

    @BindView(R.id.recycler_view) RecyclerView mInboxRecyclerView;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout mSwipeRefreshContainer;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;

    @BindView(R.id.snackbar_anchor) CoordinatorLayout snackbarAnchor;

    private static final String INBOX = "inbox";
    private static final String SENTBOX = "sentbox";

    ImageView img;

    String type = "inbox";

    /**
     * Android activity lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_messages);
        ButterKnife.bind(this);
        mInboxPresenter.attachView(this);

        img = ImageHelper.getRippy(mSwipeRefreshContainer);

        super.onCreateDrawer();

        mInboxAdapter.setCallback(this);
        mInboxRecyclerView.setHasFixedSize(true);
        mInboxRecyclerView.setAdapter(mInboxAdapter);
        mInboxRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mInboxRecyclerView.setNestedScrollingEnabled(false);
        mInboxRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        mInboxPresenter.loadInbox(type);
        mSwipeRefreshContainer.setColorSchemeResources(R.color.accent);

        mSwipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mInboxPresenter.loadInbox(type);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_inbox:
                                mInboxRecyclerView.setVisibility(View.VISIBLE);
                                if (!type.equals(INBOX)) {
                                    type = INBOX;
                                    mInboxPresenter.loadInbox(type);
                                }
                                break;
                            case R.id.action_sentbox:
                                mInboxRecyclerView.setVisibility(View.VISIBLE);
                                if (!type.equals(SENTBOX)) {
                                    type = SENTBOX;
                                    mInboxPresenter.loadInbox(type);
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

        mInboxPresenter.detachView();
    }

    @Override
    public void showInbox(List<Conversations.Messages> messages) {
        mInboxAdapter.setMessages(messages);
    }

    @Override
    public void showInboxEmpty() {
        mInboxAdapter.setMessages(null);
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
    public void onItemClicked(int id) {
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
