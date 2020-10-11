package ch.redacted.ui.search.artist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.redacted.app.R;
import ch.redacted.data.model.Artist;
import ch.redacted.ui.artist.ArtistActivity;
import ch.redacted.ui.base.BaseDrawerActivity;

public class ArtistSearchActivity extends BaseDrawerActivity implements ArtistSearchMvpView {

    @Inject ArtistSearchPresenter mSearchPresenter;

    @BindView(R.id.text_no_content) TextView mNoContent;
    @BindView(R.id.search_term) EditText searchTerm;
    @BindView(R.id.artist_search_view) View artistSearchView;

    @OnClick(R.id.action_search)
    public void OnSearchClick(View v) {
        mSearchPresenter.loadArtists(searchTerm.getText().toString());
    }

    /**
     * Android activity lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_artist_search);
        ButterKnife.bind(this);

        artistSearchView.setVisibility(View.VISIBLE);

        mSearchPresenter.attachView(this);

        searchTerm.setHint(getString(R.string.artist_search));
        searchTerm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent == null) {
                    return false;
                }
                if (keyEvent.getAction() != KeyEvent.KEYCODE_SEARCH) {
                    mSearchPresenter.loadArtists(searchTerm.getText().toString());
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(artistSearchView.getWindowToken(), 0);
                }

                return false;
            }
        });

        searchTerm.requestFocus();

        super.onCreateDrawer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchPresenter.detachView();
    }

    /*****
     * MVP View methods implementation
     *****/


    @Override
    public void showResults(Artist.Response artist) {
        Intent intent = new Intent(this, ArtistActivity.class);
        intent.putExtra("id", artist.id);
        startActivity(intent);
        searchTerm.requestFocus();
        mNoContent.setVisibility(View.GONE);
    }

    @Override
    public void showResultsEmpty() {
        searchTerm.requestFocus();
        mNoContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {
        Snackbar.make(findViewById(android.R.id.content).getRootView(), getString(R.string.error_empty_search), BaseTransientBottomBar.LENGTH_LONG);
    }
}
