package ch.redacted.ui.settings;

import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import ch.redacted.REDApplication;
import ch.redacted.app.R;
import ch.redacted.data.local.PreferencesHelper;

public class SettingsFragment extends PreferenceFragmentCompat {

    private PreferencesHelper mPreferenceHelper;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        mPreferenceHelper = ((REDApplication) getActivity().getApplication()).getComponent().preferencesHelper();
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

        EditTextPreference preference4 = (EditTextPreference) findPreference(PreferencesHelper.PREF_WM_USER_KEY);
        mPreferenceHelper.setWmUser(preference4.getText());

        ListPreference preference5 = (ListPreference) findPreference(PreferencesHelper.PREF_DEFAULT_DOWNLOAD_KEY);
        mPreferenceHelper.setDownloadMethod(preference5.getValue());

        CheckBoxPreference preference6 = (CheckBoxPreference) findPreference(PreferencesHelper.PREF_LOAD_IMAGES);
        mPreferenceHelper.setLoadImages(preference6.isChecked());

        ListPreference preference7 = (ListPreference) findPreference(PreferencesHelper.PREF_TOP_TORRENT_LIMIT);
        mPreferenceHelper.setTopTorrentLimit(Integer.parseInt(preference7.getValue()));
    }
}
