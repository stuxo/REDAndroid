package ch.redacted.ui.search.request;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
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
import ch.redacted.data.model.RequestSearch;
import ch.redacted.ui.base.BaseDrawerActivity;
import ch.redacted.ui.request.RequestActivity;
import ch.redacted.util.ImageHelper;

public class RequestSearchActivity extends BaseDrawerActivity implements RequestSearchMvpView, RequestSearchAdapter.Callback {

    @Inject RequestSearchPresenter mSearchPresenter;
    @Inject RequestSearchAdapter mRequestSearchAdapter;

    @BindView(R.id.recycler_view) RecyclerView mRequestSearchRecycler;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout mSwipeRefreshContainer;
    @BindView(R.id.text_no_content) TextView mNoContent;
    @BindView(R.id.search_term) EditText searchTerm;
    @BindView(R.id.request_search_view) View requestSearchView;
    private ImageView img;

    @OnClick(R.id.action_search)
    public void OnSearchClick(View v) {
        mSearchPresenter.loadRequests(searchTerm.getText().toString());
    }

    /**
     * Android activity lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_request_search);
        ButterKnife.bind(this);

        requestSearchView.setVisibility(View.VISIBLE);

        img = ImageHelper.getRippy(mSwipeRefreshContainer);

        mSearchPresenter.attachView(this);
        mRequestSearchRecycler.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRequestSearchRecycler.setLayoutManager(mLayoutManager);
        mRequestSearchRecycler.addItemDecoration(new DividerItemDecoration(mRequestSearchRecycler.getContext(), mLayoutManager
                .getOrientation()));

        mRequestSearchAdapter.setCallback(this);

        searchTerm.setHint(getString(R.string.request_search));

        searchTerm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent == null) {
                    return false;
                }
                if (keyEvent.getAction() != KeyEvent.KEYCODE_SEARCH) {
                    mSearchPresenter.loadRequests(searchTerm.getText().toString());
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(requestSearchView.getWindowToken(), 0);
                }

                return false;
            }
        });
        mSwipeRefreshContainer.setColorSchemeResources(R.color.accent);
        mSwipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSearchPresenter.loadRequests(searchTerm.getText().toString());
            }
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

    /*****
     * MVP View methods implementation
     *****/

    @Override
    public void showResults(List<RequestSearch.Results> results) {
        mRequestSearchRecycler.setAdapter(mRequestSearchAdapter);
        mRequestSearchAdapter.setRequests(results);
        mRequestSearchAdapter.notifyDataSetChanged();
        mNoContent.setVisibility(View.GONE);
    }

    @Override
    public void showResultsEmpty() {
        mNoContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {
        Snackbar.make(findViewById(android.R.id.content).getRootView(), getString(R.string.error_empty_search), BaseTransientBottomBar.LENGTH_LONG);
    }

    @Override
    public void showProgress(boolean show) {
        mSwipeRefreshContainer.setRefreshing(show);
        if (show) {
            animate();
        }
    }

    @Override
    public void onItemClicked(int requestId) {
        Intent intent = new Intent(this, RequestActivity.class);
        intent.putExtra("id", requestId);
        startActivity(intent);
    }
}
