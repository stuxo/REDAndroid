package ch.redacted.ui.inbox;

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
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.Conversations;

/**
 * Created by sxo on 19/01/17.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.MessageViewHolder> {
    private List<Conversations.Messages> mMessages;
    private Callback mCallback;

    @Inject
    public ConversationAdapter() {
        this.mMessages = new ArrayList<>();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View headerView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
        return new MessageViewHolder(headerView);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, final int position) {

        TimeZone tz = TimeZone.getDefault();
        int offsetFromUtc = tz.getOffset(new Date().getTime());
        long now = System.currentTimeMillis() - offsetFromUtc;

        Conversations.Messages messages = mMessages.get(position);

        holder.date.setText(DateUtils.getRelativeTimeSpanString(messages.date.getTime(), now, DateUtils.FORMAT_ABBREV_ALL));
        if (messages.username.equals("")){
            holder.sender.setHtml(holder.sender.getContext().getString(R.string.system));
        } else {
            holder.sender.setHtml(messages.username);
        }
        holder.subject.setHtml(messages.subject);
        holder.setId(messages.convId);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public void setMessages(List<Conversations.Messages> list) {
        mMessages = list;
        notifyDataSetChanged();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private int id;

        public void setId(int id) {
            this.id = id;
        }

        @BindView(R.id.sender)
        public HtmlTextView sender;

        @BindView(R.id.subject)
        public HtmlTextView subject;

        @BindView(R.id.date)
        public TextView date;

        @OnClick({R.id.conversation_root, R.id.subject, R.id.sender, R.id.date})
        public void onConversationClicked(){
            mCallback.onItemClicked(id);
        }

        MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface Callback {
        void onItemClicked(int id);
    }
}