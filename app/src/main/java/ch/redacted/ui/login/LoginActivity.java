package ch.redacted.ui.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import ch.redacted.app.R;
import ch.redacted.ui.base.BaseActivity;
import ch.redacted.ui.announcements.AnnouncementActivity;
import ch.redacted.util.SharedPrefsHelper;

public class LoginActivity extends BaseActivity implements LoginMvpView {

	@Inject LoginPresenter mLoginPresenter;
	@BindView(R.id.username_input) public TextView username;
	@BindView(R.id.password_input) public TextView password;
	@BindView(R.id.api_key_input) public TextView apiKey;
	@BindView(R.id.logo) public ImageView logo;

	ProgressDialog alertDialog;

	@OnClick(R.id.login_button) public void submit(View view) {
		if (apiKey.getText().length() > 0){
			mLoginPresenter.loginWithApiKey(apiKey.getText().toString());
		} else if (username.getText().length() > 0 && password.getText().length() > 0) {
			mLoginPresenter.login(username.getText().toString(), password.getText().toString());
		}
	}

	/**
	 * Android activity lifecycle methods
	 */

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityComponent().inject(this);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);

		SharedPrefsHelper.getInstance(this).
				writeSharedPrefs(SharedPrefsHelper.FILE_PROFILE,
						SharedPrefsHelper.KEY_PROFILE,
						"", null, null);

		int imageId = 1 + (int)(Math.random() * ((3 - 1) + 1));

		switch (imageId){
			case 1:
				logo.setImageResource(R.drawable.redacted_logo_1);
				break;
			case 2:
				logo.setImageResource(R.drawable.redacted_logo_2);
				break;
			case 3:
				logo.setImageResource(R.drawable.redacted_logo_3);
				break;
			default:
				logo.setImageResource(R.drawable.redacted_logo_1);
				break;
		}

		mLoginPresenter.attachView(this);

		if (mLoginPresenter.hasApiKey()) {
			mLoginPresenter.loginWithApiKey(mLoginPresenter.getApiKey());
		} else if (mLoginPresenter.hasCookie()) {
			mLoginPresenter.loginWithCookie();
		}
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		if (alertDialog != null) {
			alertDialog.cancel();
		}

		mLoginPresenter.detachView();
	}

	/***** MVP View methods implementation *****/

	@Override public void showLoginSuccess() {
		Intent result = new Intent();
		setResult(Activity.RESULT_OK, result);
		LoginActivity.this.finish();

		Intent intent = new Intent(LoginActivity.this, AnnouncementActivity.class);
		startActivity(intent);
	}

	@Override public void showCookieExpired() {
		Snackbar.make(findViewById(android.R.id.content).getRootView(), getString(R.string.error_cookie_expired),
			Snackbar.LENGTH_LONG).show();
	}

	@Override public void onCookieFound() {
		mLoginPresenter.loginWithCookie();
	}

	@Override
	public void showLoadingProgress(boolean show) {
		if (show) {
			alertDialog = new ProgressDialog(this);
			alertDialog.setMessage(getResources().getString(R.string.logging_in));
			alertDialog.setCancelable(false);
			alertDialog.show();
		}
		else {
			alertDialog.dismiss();
		}
	}

	@Override public void showError(String message) {
		Snackbar.make(findViewById(android.R.id.content).getRootView(), message,
				Snackbar.LENGTH_LONG).show();
	}
}
