package ch.redacted.ui.search.user;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.UserSearch;

/**
 * Created by sxo on 22/01/17.
 */

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.UserViewHolder> {
    private List<UserSearch.Results> mUserSearches;
    private Callback mCallback;

    @Inject
    public UserSearchAdapter() {
        this.mUserSearches = new ArrayList<>();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, final int position) {
        UserSearch.Results userSearch = mUserSearches.get(position);
        holder.userName.setHtml(userSearch.username);
        holder.userClass.setText(userSearch.mclass);
        holder.setId(userSearch.userId);

        if (!userSearch.donor) {
            holder.donor.setVisibility(View.GONE);
        }

        if (!userSearch.warned) {
            holder.warned.setVisibility(View.GONE);
        }

        if (userSearch.enabled) {
            holder.disabled.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mUserSearches.size();
    }

    public void setUsers(List<UserSearch.Results> list) {
        mUserSearches = list;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private int id;

        @BindView(R.id.user_name)
        HtmlTextView userName;

        @BindView(R.id.icon_donor)
        public ImageView donor;

        @BindView(R.id.icon_warned)
        public ImageView warned;

        @BindView(R.id.icon_disabled)
        public ImageView disabled;

        @BindView(R.id.user_class)
        public TextView userClass;

        @OnClick(R.id.user_root)
        void OnUserClick(View view) {
            mCallback.onItemClicked(id);
        }

        private void setId(int id){
            this.id = id;
        }

        UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface Callback {
        void onItemClicked(int id);
    }
}