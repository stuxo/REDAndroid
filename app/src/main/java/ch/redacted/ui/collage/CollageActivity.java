package ch.redacted.ui.collage;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.redacted.app.R;
import ch.redacted.data.model.Collage;
import ch.redacted.ui.base.BaseActivity;
import ch.redacted.ui.release.ReleaseActivity;

public class CollageActivity extends BaseActivity implements CollageMvpView, CollageAdapter.Callback {
    @Inject
    CollagePresenter mCollagePresenter;

    @Inject
    CollageAdapter mCollageAdapter;


    @BindView(R.id.swipe_refresh_container)
    SwipeRefreshLayout mSwipeRefreshContainer;

    @BindView(R.id.text_no_content)
    TextView mNoContent;

    @BindView(R.id.recycler_view)
    RecyclerView mTorrentsRecyclerView;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    private int collageId = 0;
    private ImageView img;
    private int currentTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_collage);
        ButterKnife.bind(this);
        // @todo set title of collage
        // getSupportActionBar().setTitle(getString(R.string.top10));

        mCollagePresenter.attachView(this);
        mCollageAdapter.setCallback(this);

        collageId = Objects.requireNonNull(getIntent().getExtras()).getInt("id");
        mTorrentsRecyclerView.setHasFixedSize(true);
        mTorrentsRecyclerView.setAdapter(mCollageAdapter);
        mTorrentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mCollageAdapter.setCallback(this);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                mCollagePresenter.loadCollage(collageId);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mSwipeRefreshContainer.setColorSchemeResources(R.color.accent);
        mSwipeRefreshContainer.setOnRefreshListener(() -> mCollagePresenter.loadCollage(collageId));
        mCollagePresenter.loadCollage(collageId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCollagePresenter.detachView();
    }


    @Override
    public void showCollage(Collage collage) {
        mCollageAdapter.setCollage(collage);
        mCollageAdapter.notifyDataSetChanged();
        mNoContent.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingProgress(boolean show) {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showBookmarked(boolean b) {

    }


    @Override
    public void onItemClicked(int id, ImageView sharedImage) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Pair<View, String> pair1 = Pair.create(sharedImage, sharedImage.getTransitionName());

            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair1);
            Intent intent = new Intent(this, ReleaseActivity.class);
            intent.putExtra("id", id);

            startActivity(intent, optionsCompat.toBundle());
        } else {
            Intent intent = new Intent(this, ReleaseActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }
}
