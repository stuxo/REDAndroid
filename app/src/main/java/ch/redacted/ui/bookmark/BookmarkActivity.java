package ch.redacted.ui.bookmark;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import ch.redacted.data.model.TorrentBookmark;
import ch.redacted.ui.base.BaseDrawerActivity;
import ch.redacted.ui.release.ReleaseActivity;
import ch.redacted.util.ImageHelper;

public class BookmarkActivity extends BaseDrawerActivity implements BookmarkMvpView, BookmarkAdapter.Callback {

	@Inject BookmarkPresenter mBookmarkPresenter;
	@Inject BookmarkAdapter mBookmarkAdapter;

	@BindView(R.id.recycler_view) RecyclerView mBookmarksRecycler;
	@BindView(R.id.swipe_refresh_container) SwipeRefreshLayout mSwipeRefreshContainer;
	@BindView(R.id.text_no_content) TextView mNoContent;
	private ImageView img;

	/**
	 * Android activity lifecycle methods
	 */

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityComponent().inject(this);
		setContentView(R.layout.activity_list);
		ButterKnife.bind(this);

		getSupportActionBar().setTitle(getString(R.string.bookmarks));

		img = ImageHelper.getRippy(mSwipeRefreshContainer);

		mBookmarkPresenter.attachView(this);
		mBookmarksRecycler.setHasFixedSize(true);
		mBookmarksRecycler.setAdapter(mBookmarkAdapter);
		mBookmarksRecycler.setLayoutManager(new LinearLayoutManager(this));

		mBookmarkAdapter.setCallback(this);

		mSwipeRefreshContainer.setColorSchemeResources(R.color.accent);
		mSwipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mBookmarkPresenter.loadTorrentBookmarks();
			}
		});
		mBookmarkPresenter.loadTorrentBookmarks(); //can load artists too
		super.onCreateDrawer();
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		mBookmarkPresenter.detachView();
	}

	private void animate() {
		Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
		rotation.setRepeatCount(Animation.INFINITE);
		img.startAnimation(rotation);
	}

	/***** MVP View methods implementation
	 * @param items*****/

	@Override
	public void showBookmarks(List<TorrentBookmark.Bookmarks> items) {
		mBookmarkAdapter.setResults(items);
		mBookmarkAdapter.notifyDataSetChanged();
		mNoContent.setVisibility(View.GONE);
	}

	@Override
	public void showBookmarksEmpty() {
		mNoContent.setVisibility(View.VISIBLE);
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
	public void onItemClicked(int release) {
		Intent intent = new Intent(this, ReleaseActivity.class);
		intent.putExtra("id", release);
		startActivity(intent);
	}

	@Override
	public void onRemoveClicked(int id) {
		mBookmarkPresenter.removeBookmark(id);
	}

	@Override
	public void onDownloadClicked(int id) {
		Snackbar.make(findViewById(android.R.id.content).getRootView(), "Not implemented yet", Snackbar.LENGTH_LONG).show();
	}
}
