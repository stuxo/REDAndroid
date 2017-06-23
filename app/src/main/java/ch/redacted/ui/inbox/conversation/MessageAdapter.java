package ch.redacted.ui.inbox.conversation;

/**
 * Created by sxo on 18/02/17.
 */

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.redacted.app.R;
import ch.redacted.data.model.Conversation;
import ch.redacted.util.Emoji;

/**
 * Created by sxo on 19/01/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Conversation.Messages> mMessages;

    @Inject
    public MessageAdapter() {
        this.mMessages = new ArrayList<>();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View headerView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(headerView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, final int position) {

        TimeZone tz = TimeZone.getDefault();
        int offsetFromUtc = tz.getOffset(new Date().getTime());
        long now = System.currentTimeMillis() - offsetFromUtc;

        Conversation.Messages messages = mMessages.get(position);
        holder.body.setHtml(Emoji.convertEmojis(messages.body));
        holder.sender.setHtml(messages.senderName);
        holder.date.setText(DateUtils.getRelativeTimeSpanString(messages.sentDate.getTime(), now, DateUtils.FORMAT_ABBREV_ALL));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public void setMessages(List<Conversation.Messages> list) {
        mMessages = list;
        notifyDataSetChanged();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.body)
        public HtmlTextView body;

        @BindView(R.id.sender)
        public HtmlTextView sender;

        @BindView(R.id.date)
        public TextView date;

        MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}