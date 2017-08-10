package ch.redacted.ui.top10;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import javax.inject.Inject;
import ch.redacted.app.R;
import ch.redacted.data.model.Top10;
import ch.redacted.ui.base.BaseDrawerActivity;
import ch.redacted.ui.release.ReleaseActivity;
import ch.redacted.util.ImageHelper;

public class Top10Activity extends BaseDrawerActivity implements Top10MvpView, Top10Adapter.Callback {

	@Inject Top10Presenter mTop10Presenter;
	@Inject Top10Adapter mTop10Adapter;

	private String[] details = {"day", "week", /*"month","year",*/ "overall", "snatched", "data", "seeded"};

	@BindView(R.id.recycler_view) RecyclerView mTop10Recycler;
	@BindView(R.id.swipe_refresh_container) SwipeRefreshLayout mSwipeRefreshContainer;
	@BindView(R.id.text_no_content) TextView mNoContent;
	@BindView(R.id.tab_layout) TabLayout mTabLayout;

	private ImageView img;

	private int currentTab = 0;

	/**
	 * Android activity lifecycle methods
	 */

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityComponent().inject(this);
		setContentView(R.layout.activity_top_10);
		ButterKnife.bind(this);
		getSupportActionBar().setTitle(getString(R.string.top10));
		img = ImageHelper.getRippy(mSwipeRefreshContainer);

		mTop10Presenter.attachView(this);
		mTop10Recycler.setHasFixedSize(true);
		mTop10Recycler.setAdapter(mTop10Adapter);
		mTop10Recycler.setLayoutManager(new LinearLayoutManager(this));

		mTop10Adapter.setCallback(this);

		mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				currentTab = tab.getPosition();
				mTop10Presenter.loadTopTorrents(details[currentTab]);
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});

		mSwipeRefreshContainer.setColorSchemeResources(R.color.accent);
		mSwipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mTop10Presenter.loadTopTorrents(details[currentTab]);
			}
		});
		mTop10Presenter.loadTopTorrents(details[currentTab]);
		super.onCreateDrawer();
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		mTop10Presenter.detachView();
	}

	private void animate() {
		Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
		rotation.setRepeatCount(Animation.INFINITE);
		img.startAnimation(rotation);
	}

	/***** MVP View methods implementation
	 * @param items*****/

	@Override
	public void showTop10(List<Top10.Results> items) {
		mTop10Adapter.setResults(items);
		mTop10Adapter.notifyDataSetChanged();
		mNoContent.setVisibility(View.GONE);
	}

	@Override
	public void showTop10Empty() {
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
	public void onItemClicked(int release, ImageView sharedImage) {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
			Pair<View, String> pair1 = Pair.create((View) sharedImage, sharedImage.getTransitionName());

			ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair1);
			Intent intent = new Intent(this, ReleaseActivity.class);
			intent.putExtra("id", release);

			startActivity(intent, optionsCompat.toBundle());
		}
		else {
			Intent intent = new Intent(this, ReleaseActivity.class);
			intent.putExtra("id", release);
			startActivity(intent);
		}
	}
}
