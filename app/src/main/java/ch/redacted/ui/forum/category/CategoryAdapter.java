package ch.redacted.ui.forum.category;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.ForumCategory;

/**
 * Created by sxo on 23/12/16.
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int HEADER_VIEW = 0, CONTENT_VIEW = 1;

    private List<Object> mItems;
    private Callback mCallback;

    @Inject
    public CategoryAdapter() {
        this.mItems = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case HEADER_VIEW:
                View view1 = inflater.inflate(R.layout.item_category, viewGroup, false);
                viewHolder = new CategoryViewHolder(view1);
                break;
            case CONTENT_VIEW:
                View view2 = inflater.inflate(R.layout.item_forum_section, viewGroup, false);
                viewHolder = new ForumViewHolder(view2);
                break;
        }

        return viewHolder;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()) {
            case HEADER_VIEW:
                CategoryViewHolder vh1 = (CategoryViewHolder) holder;
                configureCategoryViewHolder(vh1, position);
                break;
            case CONTENT_VIEW:
                ForumViewHolder vh2 = (ForumViewHolder) holder;
                configureForumViewHolder(vh2, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItems(List<Object> list) {
        mItems = list;
    }


    private void configureCategoryViewHolder(CategoryViewHolder holder, int position) {
        String category = (String) mItems.get(position);
        holder.name.setHtml(category);
    }

    private void configureForumViewHolder(ForumViewHolder holder, int position) {
        ForumCategory.Forums forum = (ForumCategory.Forums) mItems.get(position);
        holder.name.setText(forum.forumName);
        if (Build.VERSION.SDK_INT >= 24) {
            holder.desc.setText(Html.fromHtml(forum.forumDescription, Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.desc.setText(Html.fromHtml(forum.forumDescription));
        }
        holder.numPosts.setText("" + forum.numPosts);
        holder.numTopics.setText("" + forum.numTopics);
        if (holder.desc.length() < 1) {
            holder.desc.setVisibility(View.GONE);
        }
        holder.setId(forum.forumId);
    }


    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof String) {
            return HEADER_VIEW;
        } else if (mItems.get(position) instanceof ForumCategory.Forums) {
            return CONTENT_VIEW;
        }
        return -1;
    }

    class ForumViewHolder extends RecyclerView.ViewHolder {

        @OnClick(R.id.category_root)
        public void onCategorySelected(View view) {
            if (mCallback != null) mCallback.onCategoryClicked(id);
        }

        @BindView(R.id.category_name)
        public TextView name;

        @BindView(R.id.category_description)
        public TextView desc;

        @BindView(R.id.category_num_posts)
        public TextView numPosts;

        @BindView(R.id.category_num_threads)
        public TextView numTopics;

        public int id;

        ForumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.category)
        public HtmlTextView name;

        public CategoryViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
        }
    }

    interface Callback {
        void onCategoryClicked(int forumId);
    }
}