package ch.redacted.ui.search.collage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.CollageSearch;
import ch.redacted.ui.base.BaseDrawerActivity;
import ch.redacted.ui.collage.CollageActivity;
import ch.redacted.util.ImageHelper;

public class CollageSearchActivity extends BaseDrawerActivity implements CollageSearchMvpView, CollageSearchAdapter.Callback {

    @Inject
    CollageSearchPresenter mSearchPresenter;
    @Inject
    CollageSearchAdapter mCollageSearchAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mCollageSearchRecycler;

    @BindView(R.id.swipe_refresh_container)
    SwipeRefreshLayout mSwipeRefreshContainer;

    @BindView(R.id.text_no_content)
    TextView mNoContent;

    @BindView(R.id.search_term)
    EditText searchTerm;

    @BindView(R.id.collage_search_view)
    View collageSearchView;

    private ImageView img;

    @OnClick(R.id.action_search)
    public void OnSearchClick(View v) {
        mSearchPresenter.loadCollages(searchTerm.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_collage_search);
        ButterKnife.bind(this);

        collageSearchView.setVisibility(View.VISIBLE);

        img = ImageHelper.getRippy(mSwipeRefreshContainer);

        mSearchPresenter.attachView(this);
        mCollageSearchRecycler.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mCollageSearchRecycler.setLayoutManager(mLayoutManager);
        mCollageSearchRecycler.addItemDecoration(new DividerItemDecoration(mCollageSearchRecycler.getContext(), mLayoutManager
                .getOrientation()));

        mCollageSearchAdapter.setCallback(this);

        searchTerm.setHint(getString(R.string.collage_search));


        searchTerm.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (keyEvent == null) {
                return false;
            }
            if (keyEvent.getAction() != KeyEvent.KEYCODE_SEARCH) {
                mSearchPresenter.loadCollages(searchTerm.getText().toString());
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(collageSearchView.getWindowToken(), 0);
            }

            return false;
        });
        mSwipeRefreshContainer.setColorSchemeResources(R.color.accent);
        mSwipeRefreshContainer.setOnRefreshListener(() -> {
            mSearchPresenter.loadCollages(searchTerm.getText().toString());
        });

        super.onCreateDrawer();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchPresenter.detachView();
    }

    private void animate() {
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        img.startAnimation(rotation);
    }

    @Override
    public void showResults(List<CollageSearch.Response> results) {
        mCollageSearchRecycler.setAdapter(mCollageSearchAdapter);
        mCollageSearchAdapter.setCollages(results);
        mCollageSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress(boolean show) {
        mSwipeRefreshContainer.setRefreshing(show);
        if (show) {
            animate();
        }
    }


    @Override
    public void onItemClicked(int id) {
        Intent intent = new Intent(this, CollageActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);

    }

    @Override
    public void showResultsEmpty() {
        mNoContent.setVisibility(View.VISIBLE);
    }
}
