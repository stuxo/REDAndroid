package ch.redacted.ui.forum.thread;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.OnClick;
import ch.redacted.ui.reply.ReplyActivity;
import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.redacted.app.R;
import ch.redacted.data.model.ForumThread;
import ch.redacted.ui.base.BaseActivity;
import ch.redacted.ui.profile.ProfileActivity;
import ch.redacted.ui.view.ForumNavigationView;
import ch.redacted.util.ImageHelper;

import static ch.redacted.ui.reply.ReplyActivity.TYPE_MESSAGE;
import static ch.redacted.ui.reply.ReplyActivity.TYPE_POST;
import static ch.redacted.ui.reply.ReplyActivity.TYPE_THREAD;

/**
 * Created by sxo on 27/12/16.
 */

public class ThreadActivity extends BaseActivity implements ThreadMvpView, PostAdapter.Callback {

    @Inject ThreadPresenter mThreadPresenter;
    @Inject PostAdapter mPostAdapter;

    @BindView(R.id.recycler_view) RecyclerView mPostRecycler;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout mSwipeRefreshContainer;
    @BindView(R.id.text_no_content) TextView mNoContent;
    @BindView(R.id.forum_nav) ForumNavigationView forumNav;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.fab_subscribe) FloatingActionButton fabSubscribe;

    private int topicId = 0;
    private int lastPageId = 1;
    private int lastPostId = 0;
    private int forumId = 0;

    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout;
    private ImageView img;
    private String threadTitle;
    private boolean isSubscribed;

    /**
     * Android activity lifecycle methods
     */


    @OnClick(R.id.fab_subscribe)
    public void OnBookMarkToggle(View v) {
        mThreadPresenter.toggleSubscribe(topicId, isSubscribed);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_forum);
        ButterKnife.bind(this);

        toolbarLayout = findViewById(R.id.toolbar_layout);
        toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        toolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        img = ImageHelper.getRippy(mSwipeRefreshContainer);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThreadActivity.this, ReplyActivity.class);
                intent.putExtra("quote", threadTitle);
                intent.putExtra("type", TYPE_THREAD);
                startActivityForResult(intent, 0);
                fab.hide();
            }
        });
        fabSubscribe.animate().translationY(-getResources().getDimension(R.dimen.standard_55));

        mThreadPresenter.attachView(this);
        mPostRecycler.setHasFixedSize(true);
        mPostRecycler.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0) {
                    fabSubscribe.hide();
                    fab.hide();
                } else if (dy < 0) {
                    fabSubscribe.show();
                    fab.show();
                }
            }
        });
        mPostAdapter.setCallback(this);
        mPostRecycler.setAdapter(mPostAdapter);
        mPostRecycler.setLayoutManager(new LinearLayoutManager(this));

        forumId = getIntent().getExtras().getInt("forumId");
        topicId = getIntent().getExtras().getInt("topicId");
        lastPageId = getIntent().getExtras().getInt("lastReadPage");
        lastPostId = getIntent().getExtras().getInt("lastPostId");

        mSwipeRefreshContainer.setColorSchemeResources(R.color.accent);
        mSwipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mThreadPresenter.loadPosts(topicId, null, forumNav.getCurrentPageNumber(), false);
            }
        });

        forumNav.setNavigationEventListener(new ForumNavigationView.OnNavigationEventListener() {
            @Override
            public void onNavigationChanged() {
                mThreadPresenter.loadPosts(topicId, null, forumNav.getCurrentPageNumber(), true);
            }
        });

        if (lastPostId > 0) {
            mThreadPresenter.loadPosts(topicId, lastPostId, null, false);
        } else {
            mThreadPresenter.loadPosts(topicId, null, lastPageId, false);
        }
    }

    @Override
    public void showSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
        if (isSubscribed) {
            fabSubscribe.setImageResource(R.drawable.ic_notifications_active_48px);
        } else {
            fabSubscribe.setImageResource(R.drawable.ic_notifications_disabled_48px);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mThreadPresenter.detachView();
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
    public void showPosts(ForumThread threads, boolean scrollToTop) {
        threadTitle = threads.response.threadTitle;
        if (Build.VERSION.SDK_INT >= 24) {
            toolbarLayout.setTitle(Html.fromHtml(threads.response.threadTitle, Html.FROM_HTML_MODE_LEGACY));
        } else {
            toolbarLayout.setTitle(Html.fromHtml(threads.response.threadTitle));
        }

        if (scrollToTop){
            mPostRecycler.scrollToPosition(0);
        }

        mPostAdapter.setItems(threads.response.posts);
        mPostAdapter.notifyDataSetChanged();
        mNoContent.setVisibility(View.GONE);

        forumNav.setPageCount(threads.response.pages);
        forumNav.updateCurrentPageNumber(threads.response.currentPage);
    }

    @Override
    public void showPostsEmpty() {

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

    @Override
    public void showPostSuccessful() {
        mThreadPresenter.loadPosts(topicId, null, forumNav.getCurrentPageNumber(), false);
    }

    @Override
    public void showPostFailed() {

    }

    @Override
    public void onQuoteClicked(String bbBody, String quoteText, String quotedUser, int postId) {
        Intent intent = new Intent(this, ReplyActivity.class);
        intent.putExtra("quote", quoteText);
        intent.putExtra("bbcode", bbBody);
        intent.putExtra("user", quotedUser);
        intent.putExtra("type", TYPE_POST);
        intent.putExtra("postId", postId);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onProfileClicked(int userId) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("id", userId);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == 0) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                mThreadPresenter.addPost(data.getStringExtra("body"), forumId, topicId);
            }
            else {
            }
        }
    }
}
