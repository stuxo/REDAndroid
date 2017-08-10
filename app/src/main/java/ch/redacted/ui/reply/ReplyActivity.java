package ch.redacted.ui.reply;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.redacted.app.R;
import ch.redacted.data.model.Conversation;
import ch.redacted.ui.base.BaseActivity;
import ch.redacted.ui.inbox.conversation.MessageAdapter;
import ch.redacted.util.Emoji;
import ch.redacted.util.ImageHelper;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class ReplyActivity extends BaseActivity implements ReplyMvpView {

    @Inject ReplyPresenter mReplyPresenter;
    private String quote;
    private int type;
    private String bbcode;

    //replying to a message
    public static int TYPE_MESSAGE = 1;
    //new thread post
    public static int TYPE_THREAD = 2;
    //replying to a thread post
    public static int TYPE_POST = 3;
    //start of new conv
    public static int TYPE_NEW_MESSAGE = 4;

    @BindView(R.id.quoted_text) HtmlTextView replyingTo;
    @BindView(R.id.compose) EditText messageText;
    @BindView(R.id.subject_edit) EditText subjectEdit;
    @BindView(R.id.replying_to) TextView heading;


    /**
     * Android activity lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.reply_view);
        ButterKnife.bind(this);
        mReplyPresenter.attachView(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        quote = getIntent().getStringExtra("quote");
        type = getIntent().getExtras().getInt("type");
        bbcode = getIntent().getExtras().getString("bbcode");
        String user = getIntent().getStringExtra("user");
        int postId = getIntent().getIntExtra("postId", 0);

        if (type == TYPE_NEW_MESSAGE) {
            replyingTo.setVisibility(View.GONE);
            subjectEdit.setVisibility(View.VISIBLE);
            messageText.setHint(R.string.compose_message);
            heading.setText(R.string.new_message_to);
        } else {
            quote = Emoji.convertEmojis(quote);
            quote = ImageHelper.replaceImageLinks(quote);
            replyingTo.setMovementMethod(new ScrollingMovementMethod());
            if (type == TYPE_MESSAGE) {
                replyingTo.setHtml(quote);
            } else if (type == TYPE_THREAD) {
                replyingTo.setHtml(quote);
            } else if (type == TYPE_POST) {
                messageText.setText(String.format(Locale.getDefault(), "[quote=%s|%d]%s[/quote]", user, postId, bbcode));
                replyingTo.setHtml(String.format(Locale.getDefault(), "<i>%s said: </i>%s", user, quote));
                messageText.append("");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReplyPresenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reply_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_send:
                if (messageText != null && messageText.length() > 0){
                    showSuccess();
                }
                else {
                    showFailure();
                }
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*****
     * MVP View methods implementation
     *****/

    @Override
    public void onBackPressed() {
        showFailure();
        super.onBackPressed();
    }

    @Override public void showSuccess() {
        if (type == TYPE_NEW_MESSAGE) {
            Intent intent = new Intent();
            intent.putExtra("body", messageText.getText().toString());
            intent.putExtra("subject", subjectEdit.getText().toString());
            setResult(RESULT_OK, intent);

        } else {
            setResult(RESULT_OK, new Intent().putExtra("body", messageText.getText().toString()));
        }
        finish();
    }

    @Override public void showFailure() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
