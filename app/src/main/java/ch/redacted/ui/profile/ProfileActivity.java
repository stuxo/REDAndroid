package ch.redacted.ui.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.redacted.app.R;
import ch.redacted.data.model.Recents;
import ch.redacted.ui.base.BaseActivity;
import ch.redacted.ui.release.ReleaseActivity;
import ch.redacted.ui.reply.ReplyActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.inject.Inject;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import static android.view.View.GONE;

public class ProfileActivity extends BaseActivity
	implements ProfileMvpView, RecentsAdapter.Callback {

	private int USER_ID = 0;
	private static final int GRID_SPAN_COUNT = 2;

	private RecentsAdapter mSnatchesAdapter;
	private RecentsAdapter mUploadsAdapter;

	@Inject ProfilePresenter mProfilePresenter;

	@BindView(R.id.toolbar) Toolbar toolbar;
	@BindView(R.id.snackbar_anchor) CoordinatorLayout snackbarAnchor;
	@BindView(R.id.avatar) ImageView avatar;

	@BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;

	@BindView(R.id.info_layout) ViewGroup infoView;
	@BindView(R.id.stats_layout) ViewGroup statsView;
	@BindView(R.id.snatches_layout) ViewGroup snatchesView;
	@BindView(R.id.uploads_layout) ViewGroup uploadsView;
	@BindView(R.id.description_layout) ViewGroup descriptionView;

	@BindView(R.id.user_name) HtmlTextView userName;
	@BindView(R.id.user_class) TextView userClass;
	@BindView(R.id.user_joined) TextView userJoined;
	@BindView(R.id.user_last_seen) TextView userLastSeen;
	@BindView(R.id.user_ratio) TextView userRatio;
	@BindView(R.id.expandable_text) HtmlTextView userProfileText;

	@BindView(R.id.downloaded_value) TextView userDownload;
	@BindView(R.id.uploaded_value) TextView userUpload;
	@BindView(R.id.uploads_value) TextView userUploads;
	@BindView(R.id.requests_value) TextView userRequests;
	@BindView(R.id.bounty_value) TextView userBounty;
	@BindView(R.id.posts_value) TextView userPosts;
	@BindView(R.id.artists_added_value) TextView userArtists;
	@BindView(R.id.overall_value) TextView userOverall;

	private boolean mIsLoggedInUser = false;

	/**
	 * Android activity lifecycle methods
	 */

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityComponent().inject(this);
		setContentView(R.layout.activity_profile);
		ButterKnife.bind(this);
		mProfilePresenter.attachView(this);
		if (getIntent().getIntExtra("id", 0) > 0) {
			USER_ID = getIntent().getIntExtra("id", 0);
		} else if (getIntent().getDataString() != null && getIntent().getDataString()
			.contains("?id=")) {
			String url = getIntent().getDataString();
			String id = url.substring(url.indexOf("id=") + 3, url.length());
			USER_ID = Integer.parseInt(id);
		}

		bottomNavigationView.setOnNavigationItemSelectedListener(
			new BottomNavigationView.OnNavigationItemSelectedListener() {
				@Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
					switch (item.getItemId()) {
						case R.id.action_info:
							descriptionView.setVisibility(View.VISIBLE);
							statsView.setVisibility(GONE);
							snatchesView.setVisibility(GONE);
							uploadsView.setVisibility(GONE);
							break;
						case R.id.action_stats:
							descriptionView.setVisibility(GONE);
							statsView.setVisibility(View.VISIBLE);
							snatchesView.setVisibility(GONE);
							uploadsView.setVisibility(GONE);
							break;
						case R.id.action_snatches:
							descriptionView.setVisibility(GONE);
							statsView.setVisibility(GONE);
							snatchesView.setVisibility(View.VISIBLE);
							uploadsView.setVisibility(GONE);
							break;
						case R.id.action_uploads:
							descriptionView.setVisibility(GONE);
							statsView.setVisibility(GONE);
							snatchesView.setVisibility(GONE);
							uploadsView.setVisibility(View.VISIBLE);
							break;
					}
					return true;
				}
			});

		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		mSnatchesAdapter = new RecentsAdapter();
		mSnatchesAdapter.setCallback(ProfileActivity.this);

		mUploadsAdapter = new RecentsAdapter();
		mUploadsAdapter.setCallback(ProfileActivity.this);

		RecyclerView snatchesRecycler =
			(RecyclerView) snatchesView.findViewById(R.id.recycler_view);
		snatchesRecycler.setLayoutManager(
			new GridLayoutManager(ProfileActivity.this, GRID_SPAN_COUNT));
		snatchesRecycler.setAdapter(mSnatchesAdapter);

		RecyclerView uploadsRecycler = (RecyclerView) uploadsView.findViewById(R.id.recycler_view);
		uploadsRecycler.setLayoutManager(
			new GridLayoutManager(ProfileActivity.this, GRID_SPAN_COUNT));
		uploadsRecycler.setAdapter(mUploadsAdapter);

		mProfilePresenter.loadProfile(this, USER_ID, true);
		mProfilePresenter.loadRecents(USER_ID);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override protected void onDestroy() {
		super.onDestroy();

		mProfilePresenter.detachView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mIsLoggedInUser){
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.send_message_menu, menu);
			return true;
		}
		return false;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.action_new_message:
				Intent intent = new Intent(this, ReplyActivity.class);
				intent.putExtra("type", ReplyActivity.TYPE_NEW_MESSAGE);
				intent.putExtra("toid", USER_ID);
				startActivityForResult(intent, 0);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request it is that we're responding to
		if (requestCode == 0) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				mProfilePresenter.newMessage(USER_ID, data.getStringExtra("subject"), data.getStringExtra("body"));
			}
			else {
			}
		}
	}

	/*****
	 * MVP View methods implementation
	 *****/

	@Override public void showAvatar(String avatarUrl) {
		if (avatarUrl.contains("gif")) {
			Glide.with(this).load(avatarUrl).asGif().into(avatar);
		} else {
			Glide.with(this).load(avatarUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(avatar) {
				@Override
				protected void setResource(Bitmap resource) {
					RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
					circularBitmapDrawable.setCircular(true);
					avatar.setImageDrawable(circularBitmapDrawable);
				}
			});
		}
	}

	@Override public void showDefaultAvatar() {
		Glide.with(this).load(R.drawable.default_avatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(avatar) {
			@Override
			protected void setResource(Bitmap resource) {
				RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
				circularBitmapDrawable.setCircular(true);
				avatar.setImageDrawable(circularBitmapDrawable);
			}
		});
	}

	@Override
	public void showSnackbar(String message) {
		Snackbar.make(snackbarAnchor, message, Snackbar.LENGTH_LONG).show();
	}

	@Override public void showUserClass(String userClassRank) {
		userClass.setText(userClassRank);
	}

	@Override public void showUsername(String username) {
		userName.setHtml(username);
	}

	@Override public void showJoinedDate(Date date) {
		TimeZone tz = TimeZone.getDefault();
		int offsetFromUtc = tz.getOffset(new Date().getTime());
		long now = System.currentTimeMillis() - offsetFromUtc;

		userJoined.setText(getString(R.string.joined_when, DateUtils.getRelativeTimeSpanString(date.getTime(),
			now, DateUtils.FORMAT_ABBREV_MONTH).toString()));
	}

	@Override public void showUserDescription(String description) {
		userProfileText.setHtml(description, new HtmlHttpImageGetter(userProfileText));
	}

	@Override public void showUserDescriptionEmpty() {
		userProfileText.setText(getString(R.string.profile_empty));
	}

	@Override
	public void showProfileIsLoggedInUser(boolean isUser) {
		mIsLoggedInUser = isUser;
	}

	@Override public void showDownloaded(int download) {
		userDownload.setText(String.format(Locale.getDefault(), "%s%%", download));
	}

	@Override public void showDownloadedParanoid() {
		userDownload.setText(getText(R.string.redacted));
	}

	@Override public void showUploaded(int uploadedPercentile) {
		userUpload.setText(String.format(Locale.getDefault(), "%s%%", uploadedPercentile));
	}

	@Override public void showRatio(double ratio, double requiredRatio) {
		userRatio.setText(getString(R.string.user_ratio, requiredRatio, ratio));
	}

	@Override public void showRatioParanoid() {
		userRatio.setVisibility(GONE);
	}

	@Override public void showUploadedParanoid() {
		userUpload.setText(getText(R.string.redacted));
	}

	@Override public void showNumUploads(int numUploadedPercentile) {
		userUploads.setText(String.format(Locale.getDefault(), "%s%%", numUploadedPercentile));
	}

	@Override public void showNumUploadsParanoid() {
		userUploads.setText(getText(R.string.redacted));

	}

	@Override public void showRequestsFilled(int requestsPercentile) {
		userRequests.setText(String.format(Locale.getDefault(), "%s%%", requestsPercentile));
	}

	@Override public void showRequestsParanoid() {
		userRequests.setText(getText(R.string.redacted));
	}

	@Override public void showBountySpent(int spentPercentile) {
		userBounty.setText(String.format(Locale.getDefault(), "%s%%", spentPercentile));
	}

	@Override public void showBountySpentParanoid() {
		userBounty.setText(getText(R.string.redacted));
	}

	@Override public void showNumForumPosts(int postsPercentile) {
		userPosts.setText(String.format(Locale.getDefault(), "%s%%", postsPercentile));
	}

	@Override public void showNumForumPostsParanoid() {
		userPosts.setText(getText(R.string.redacted));
	}

	@Override public void showArtistsAdded(int artistsAddedPercentile) {
		userArtists.setText(String.format(Locale.getDefault(), "%s%%", artistsAddedPercentile));
	}

	@Override public void showArtistsAddedParanoid() {
		userArtists.setText(getText(R.string.redacted));
	}

	@Override public void showOverallRank(int overallPercentile) {
		userOverall.setText(String.format(Locale.getDefault(), "%s%%", overallPercentile));
	}

	@Override public void showOverallRankParanoid() {
		userOverall.setText(getText(R.string.redacted));
	}

	@Override public void showLastSeen(Date lastSeen) {
		TimeZone tz = TimeZone.getDefault();
		int offsetFromUtc = tz.getOffset(new Date().getTime());
		long now = System.currentTimeMillis() - offsetFromUtc;

		userLastSeen.setText(getString(R.string.last_seen_when,
			DateUtils.getRelativeTimeSpanString(lastSeen.getTime(),
				now, DateUtils.FORMAT_ABBREV_RELATIVE))
				.replace("0 minutes ago", "just now"));
	}

	@Override public void showLastSeenParanoid() {
		userLastSeen.setVisibility(GONE);
	}

	@Override public void showInvitedCount() {

	}

	@Override public void showInvitedCountParanoid() {

	}

	@Override public void showRecentUploads(List<Recents.RecentTorrent> uploads) {
		mUploadsAdapter.setRecents(uploads);
	}

	@Override public void showRecentUploadsEmpty() {
		//todo hide the tab
	}

	@Override public void showRecentSnatches(List<Recents.RecentTorrent> snatches) {
		mSnatchesAdapter.setRecents(snatches);
	}

	@Override public void showRecentSnatchesEmpty() {
		//todo hide the tab
	}

	@Override public void showLoadingProgress(boolean show) {

	}

	@Override public void showError(String message) {

	}

	@Override public void onRecentClicked(int id) {
		Intent intent = new Intent(ProfileActivity.this, ReleaseActivity.class);
		intent.putExtra("id", id);
		startActivity(intent);
	}
}
