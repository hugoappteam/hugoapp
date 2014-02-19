package de.hjg.hugojunkersapp.general;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

import de.hjg.hugojunkersapp.R;

public class SettingsActivity extends SherlockPreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.layout.settings);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			/*
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.create(this)
					.addNextIntentWithParentStack(upIntent)
					.startActivities();
			} else {
				NavUtils.navigateUpTo(this, upIntent);
			}
			
			return true;
			
		}
		*/
			onBackPressed();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
