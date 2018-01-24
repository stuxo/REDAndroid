package ch.redacted.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import ch.redacted.REDApplication;
import ch.redacted.app.R;
import ch.redacted.ui.base.BaseActivity;
import net.rdrei.android.dirchooser.DirectoryChooserActivity;

/**
 * Created by sxo on 27/02/17.
 */

public class SettingsActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 0) {
			if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
				REDApplication.get(this).getComponent().preferencesHelper().setDefaultDownloadLocation(data
					.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR));
			} else {
				// Nothing selected
			}
		}
	}
}