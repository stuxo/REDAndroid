package ch.redacted.ui.search.collage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.CollageSearch;

public class CollageSearchAdapter extends RecyclerView.Adapter<CollageSearchAdapter.CollagesViewHolder> {
    private List<CollageSearch.Response> mCollageSearches;
    private Callback mCallback;

    @Inject
    CollageSearchAdapter() {
        this.mCollageSearches = new ArrayList<>();
    }

    @Override
    public CollagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collage_search, parent, false);
        return new CollagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollagesViewHolder holder, int position) {
        CollageSearch.Response collageSearch = mCollageSearches.get(position);
        holder.setId(collageSearch.id);
        holder.collageName.setText(collageSearch.name);
    }

    @Override
    public int getItemCount() {
        return mCollageSearches.size();
    }

    void setCollages(List<CollageSearch.Response> results) {
        mCollageSearches = results;
    }


    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    interface Callback {
        void onItemClicked(int id);
    }

    class CollagesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.collage_name)
        public TextView collageName;
        private int id;

        CollagesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.collage_root)
        void OnCollageClick(View view) {
            mCallback.onItemClicked(id);
        }

        private void setId(int id) {
            this.id = id;
        }
    }

}
