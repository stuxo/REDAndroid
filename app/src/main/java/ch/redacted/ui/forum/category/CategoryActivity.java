package ch.redacted.ui.forum.category;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.redacted.app.R;
import ch.redacted.ui.base.BaseDrawerActivity;
import ch.redacted.ui.forum.threadList.ThreadListActivity;
import ch.redacted.util.ImageHelper;

public class CategoryActivity extends BaseDrawerActivity implements CategoryMvpView, CategoryAdapter.Callback {

	@Inject CategoryPresenter mCategoryPresenter;
	@Inject CategoryAdapter mCategoryAdapter;

	@BindView(R.id.recycler_view) RecyclerView mAnnouncementRecycler;
	@BindView(R.id.swipe_refresh_container) SwipeRefreshLayout mSwipeRefreshContainer;
	@BindView(R.id.text_no_content) TextView mNoThreads;
	private ImageView img;

	/**
	 * Android activity lifecycle methods
	 */

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityComponent().inject(this);
		setContentView(R.layout.activity_list);
		ButterKnife.bind(this);

		img = ImageHelper.getRippy(mSwipeRefreshContainer);

		mCategoryPresenter.attachView(this);
		mAnnouncementRecycler.setHasFixedSize(true);
		mAnnouncementRecycler.setAdapter(mCategoryAdapter);
		mAnnouncementRecycler.setLayoutManager(new LinearLayoutManager(this));

		mCategoryAdapter.setCallback(this);

		mSwipeRefreshContainer.setColorSchemeResources(R.color.accent);
		mSwipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mCategoryPresenter.loadCategories();
			}
		});
		mCategoryPresenter.loadCategories();
		super.onCreateDrawer();
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		mCategoryPresenter.detachView();
	}

	private void animate() {
		Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
		rotation.setRepeatCount(Animation.INFINITE);
		img.startAnimation(rotation);
	}

	/***** MVP View methods implementation *****/

	@Override
	public void showCategories(List<Object> items) {
		mCategoryAdapter.setItems(items);
		mCategoryAdapter.notifyDataSetChanged();
		mNoThreads.setVisibility(View.GONE);
	}

	@Override
	public void showCategoriesEmpty() {
		mNoThreads.setVisibility(View.VISIBLE);
	}

	@Override
	public void showError() {

	}

	@Override
	public void showProgress(boolean show) {
		mSwipeRefreshContainer.setRefreshing(show);
		if (show) {
			animate();
		}
	}

	@Override
	public void onCategoryClicked(int forumId) {
		Intent intent = new Intent(this, ThreadListActivity.class);
		intent.putExtra("forumId", forumId);
		startActivity(intent);
	}
}
