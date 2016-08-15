package learnenglishhou.bluebirdaward.com.learne.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import learnenglishhou.bluebirdaward.com.learne.R;

public class SettingsMenuActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);

    }





}
