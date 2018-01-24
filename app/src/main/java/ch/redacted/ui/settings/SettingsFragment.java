package ch.redacted.ui.settings;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import ch.redacted.REDApplication;
import ch.redacted.app.BuildConfig;
import ch.redacted.app.R;
import ch.redacted.data.local.PreferencesHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.functions.Consumer;
import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

public class SettingsFragment extends PreferenceFragmentCompat {

    private PreferencesHelper mPreferenceHelper;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        mPreferenceHelper = ((REDApplication) getActivity().getApplication()).getComponent().preferencesHelper();
        Preference versionPreference = findPreference("pref_verion_name");
        versionPreference.setTitle(BuildConfig.VERSION_NAME);

        Preference downloadLocation = findPreference(PreferencesHelper.PREF_DEFAULT_DOWNLOAD_LOCATION);

        downloadLocation.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override public boolean onPreferenceClick(Preference preference) {

                new RxPermissions(getActivity())
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            if (granted) {
                                final Intent chooserIntent = new Intent(getActivity(), DirectoryChooserActivity.class);

                                final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                                    .allowReadOnlyDirectory(true)
                                    .newDirectoryName("NewDir")
                                    .allowNewDirectoryNameModification(false)
                                    .build();

                                chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
                                getActivity().startActivityForResult(chooserIntent, 0);
                            }
                        }
                    });
                return false;
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        EditTextPreference preference = (EditTextPreference) findPreference(PreferencesHelper.PREF_WM_HOST_KEY);
        mPreferenceHelper.setWmHost(preference.getText());

        EditTextPreference preference2 = (EditTextPreference) findPreference(PreferencesHelper.PREF_WM_PASSWORD_KEY);
        mPreferenceHelper.setWmPassword(preference2.getText());

        EditTextPreference preference3 = (EditTextPreference) findPreference(PreferencesHelper.PREF_WM_PORT_KEY);
        mPreferenceHelper.setWmPort(preference3.getText());

        //todo rename these to something useful, ugh
        //todo error checking on url input

        //todo return an error if host or url is null
        EditTextPreference preference4 = (EditTextPreference) findPreference(PreferencesHelper.PREF_WM_USER_KEY);
        mPreferenceHelper.setWmUser(preference4.getText());

        ListPreference preference5 = (ListPreference) findPreference(PreferencesHelper.PREF_DEFAULT_DOWNLOAD_KEY);
        mPreferenceHelper.setDownloadMethod(preference5.getValue());

        CheckBoxPreference preference6 = (CheckBoxPreference) findPreference(PreferencesHelper.PREF_LOAD_IMAGES);
        mPreferenceHelper.setLoadImages(preference6.isChecked());

        ListPreference preference7 = (ListPreference) findPreference(PreferencesHelper.PREF_TOP_TORRENT_LIMIT);
        mPreferenceHelper.setTopTorrentLimit(Integer.parseInt(preference7.getValue()));

        EditTextPreference preference8 = (EditTextPreference) findPreference(PreferencesHelper.PREF_PYWA_HOST_KEY);
        mPreferenceHelper.setPywaHost(preference8.getText());

        EditTextPreference preference9 = (EditTextPreference) findPreference(
            PreferencesHelper.PREF_PYWA_PASSWORD_KEY);
        mPreferenceHelper.setPywaPassword(preference9.getText());

        EditTextPreference preference10 = (EditTextPreference) findPreference(
            PreferencesHelper.PREF_PYWA_PORT_KEY);
        mPreferenceHelper.setPywaPort(preference10.getText());

    }
}
