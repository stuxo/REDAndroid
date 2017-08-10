package ch.redacted.ui.inbox.conversation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import ch.redacted.ui.reply.ReplyActivity;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.redacted.app.R;
import ch.redacted.data.model.Conversation;
import ch.redacted.ui.base.BaseActivity;
import ch.redacted.util.ImageHelper;

import static ch.redacted.ui.reply.ReplyActivity.TYPE_MESSAGE;

public class ConversationActivity extends BaseActivity implements ConversationMvpView {

    @Inject ConversationPresenter mConversationPresenter;
    @Inject MessageAdapter mMessageAdapter;
    private int CONVERSATION_ID;

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.recycler_view) RecyclerView mConversationRecycler;
    @BindView(R.id.snackbar_anchor) CoordinatorLayout snackbarAnchor;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout mSwipeRefreshContainer;

    private String mSubject;
    private String mLastMessage;
    private int mConversationId;
    private String mUser;
    private ImageView img;

    /**
     * Android activity lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_conversation);
        ButterKnife.bind(this);
        mConversationPresenter.attachView(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.hide();
                Intent intent = new Intent(ConversationActivity.this, ReplyActivity.class);
                intent.putExtra("quote", mLastMessage);
                intent.putExtra("type", TYPE_MESSAGE);
                startActivityForResult(intent, 0);
            }
        });

        img = ImageHelper.getRippy(mSwipeRefreshContainer);

        mConversationRecycler.setHasFixedSize(true);
        mConversationRecycler.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });
        mConversationRecycler.setAdapter(mMessageAdapter);
        mConversationRecycler.setLayoutManager(new LinearLayoutManager(this));
        mConversationRecycler.setNestedScrollingEnabled(false);

        mConversationRecycler.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        mSwipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mConversationPresenter.loadConversation(CONVERSATION_ID);
            }
        });
        CONVERSATION_ID = getIntent().getExtras().getInt("id");

        mConversationPresenter.loadConversation(CONVERSATION_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mConversationPresenter.detachView();
    }

    /*****
     * MVP View methods implementation
     *****/

    @Override
    public void showMessages(List<Conversation.Messages> messages) {
        mMessageAdapter.setMessages(messages);
        mLastMessage = messages.get(messages.size() - 1).body;
    }

    @Override
    public void showMessagesEmpty() {
        mMessageAdapter.setMessages(null);
    }

    private void animate() {
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        img.startAnimation(rotation);
    }

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

    @Override
    public void showSnackbar(String message) {
        Snackbar.make(snackbarAnchor, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setConversationInfo(int convId, String subject, String user) {
        mConversationId = convId;
        mSubject = subject;
        mUser = user;
    }

    @Override
    public void showSuccess() {
        mConversationPresenter.loadConversation(CONVERSATION_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == 0) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                mConversationPresenter.replyMessage(mUser, mSubject, CONVERSATION_ID, data.getStringExtra("body"));
            }
            else {
            }
        }
        fab.show();
    }
}
