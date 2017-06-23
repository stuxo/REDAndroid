package ch.redacted.ui.search.request;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.RequestSearch;
import ch.redacted.util.Calculator;

/**
 * Created by sxo on 22/01/17.
 */

public class RequestSearchAdapter extends RecyclerView.Adapter<RequestSearchAdapter.RequestsViewHolder> {
    private List<RequestSearch.Results> mRequstSearches;
    private Callback mCallback;

    @Inject
    public RequestSearchAdapter() {
        this.mRequstSearches = new ArrayList<>();
    }

    @Override
    public RequestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new RequestsViewHolder(view);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(final RequestsViewHolder holder, final int position) {
        RequestSearch.Results requestSearch = mRequstSearches.get(position);
        holder.setId(requestSearch.requestId);
        holder.artistName.setHtml(requestSearch.artists.get(0).get(0).name);
        holder.releaseName.setHtml(requestSearch.title);
        holder.year.setText(String.format("%d", requestSearch.year));
        holder.bounty.setText(Calculator.toHumanReadableSize(requestSearch.bounty, 0));
        if (requestSearch.isFilled) {
            holder.requestRoot.setBackgroundColor(ContextCompat.getColor(holder.requestRoot.getContext(), R.color.BackgroundAccent));
        }
    }

    @Override
    public int getItemCount() {
        return mRequstSearches.size();
    }

    public void setRequests(List<RequestSearch.Results> list) {
        mRequstSearches = list;
    }

    class RequestsViewHolder extends RecyclerView.ViewHolder {

        private int id;

        @BindView(R.id.artist_name)
        public HtmlTextView artistName;

        @BindView(R.id.release_name)
        public HtmlTextView releaseName;

        @BindView(R.id.year)
        public TextView year;

        @BindView(R.id.bounty)
        public TextView bounty;

        @BindView(R.id.request_root)
        public LinearLayout requestRoot;

        @OnClick(R.id.request_root)
        void OnRequestClick(View view) {
            mCallback.onItemClicked(id);
        }

        private void setId(int id){
            this.id = id;
        }

        RequestsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface Callback {
        void onItemClicked(int id);
    }
}