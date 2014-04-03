package slaphappy.v1;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity 
                     implements OnSharedPreferenceChangeListener{
	
	private int theme = -1;
	
	ListPreference themeItem;
	ListPreference bkColorItem;
	ListPreference veffectItem;
	ListPreference soundItem;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {       
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
		PreferenceManager.setDefaultValues(this, R.xml.preference, false);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		theme = Integer.parseInt(sharedPrefs.getString("theme", "-1"));
		
		bkColorItem = (ListPreference)findPreference("bk_color");
		veffectItem = (ListPreference)findPreference("veffect");
		soundItem = (ListPreference)findPreference("sound");
		themeItem = (ListPreference)findPreference("theme");

		if (theme != -1) {
			bkColorItem.setEnabled(false);
			veffectItem.setEnabled(false);
			soundItem.setEnabled(false);
		}
		
		themeItem.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				final String val = newValue.toString();
				int index = themeItem.findIndexOfValue(val);
				if(index != 0) {
					bkColorItem.setEnabled(false);
					veffectItem.setEnabled(false);
					soundItem.setEnabled(false);
				}
				else {
					bkColorItem.setEnabled(true);
					veffectItem.setEnabled(true);
					soundItem.setEnabled(true);
				}
				return true;
			}
		});
	}
			
	@SuppressWarnings("deprecation")
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference connectionPref = findPreference(key);	        
        connectionPref.setSummary(sharedPreferences.getString(key, ""));
	}
}