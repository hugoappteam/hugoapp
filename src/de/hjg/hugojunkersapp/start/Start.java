package de.hjg.hugojunkersapp.start;

import de.hjg.hugojunkersapp.general.LogoScreen;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class Start extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences pref = getSharedPreferences(SplashScreen.PREFS_NAME,
				MODE_PRIVATE);

		if (pref.getString(SplashScreen.PREFS_USERNAME, null) == null
				|| pref.getString(SplashScreen.PREFS_PASSWORD, null) == null) {
			this.finish();
			Intent intent_splashScreen = new Intent(
					this.getApplicationContext(), SplashScreen.class);
			intent_splashScreen.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			this.overridePendingTransition(0, 0);
			this.startActivity(intent_splashScreen);
		} else {
			this.finish();
			Intent home = new Intent(this.getApplicationContext(), LogoScreen.class);
			home.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			this.overridePendingTransition(0, 0);
			this.startActivity(home);
		}
	}
}