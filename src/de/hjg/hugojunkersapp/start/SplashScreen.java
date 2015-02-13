package de.hjg.hugojunkersapp.start;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import de.hjg.hugojunkersapp.R;
import de.hjg.hugojunkersapp.general.LogoScreen;
import de.hjg.hugojunkersapp.general.Utils;

public class SplashScreen extends Activity implements AnimationListener,
		OnClickListener {
	private Animation animSplash;
	private EditText username;
	private EditText password;
	private TextView registerLink;
	private TextView errorText;
	private Button login_button;
	private AlphaAnimation controlAlphaAnimation;
	// private final String LOGIN_URL = getString(R.string.JSONLoginAddress);
	private final String LOGIN_URL = "http://hjg.pf-control.de/JSONlogin.php";
	// private static final String LOGIN_URL = change to a ressource string
	private JSONParser jsonParser = new JSONParser();
	private ProgressBar bar;

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	public static final String PREFS_NAME = "LoginPrefs";
	public static final String PREFS_USERNAME = "username";
	public static final String PREFS_PASSWORD = "password";
	public static final String PREFS_LAST_USERNAME = "last_username";

	private SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);
		username = (EditText) findViewById(R.id.splash_username);
		password = (EditText) findViewById(R.id.splash_password);
		registerLink = (TextView) findViewById(R.id.register_link);
		registerLink.setOnClickListener(this);
		login_button = (Button) findViewById(R.id.button_login);
		login_button.setOnClickListener(this);
		errorText = (TextView) findViewById(R.id.login_info);
		errorText.setTextColor(ColorStateList.valueOf(Color.RED));
		bar = (ProgressBar) findViewById(R.id.splashScreen_progressBar);

		controlAlphaAnimation = new AlphaAnimation(0.1f, 1.0f);
		controlAlphaAnimation.setDuration(250);
		controlAlphaAnimation.setFillAfter(true);

		pref = getSharedPreferences(SplashScreen.PREFS_NAME, MODE_PRIVATE);
		// On start from SplashScreen: clear the fields in the Preferences
		pref.edit().putString(PREFS_USERNAME, null)
				.putString(PREFS_PASSWORD, null).commit();
		if (pref.getString(SplashScreen.PREFS_LAST_USERNAME, null) != null) {
			username.setText(pref.getString(SplashScreen.PREFS_LAST_USERNAME,
					null));
		}

		startAnimation();

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button_login) {
			// login_button.setClickable(false);
			login_button.setEnabled(false);
			errorText.setVisibility(View.INVISIBLE);
			bar.setVisibility(View.VISIBLE);
			new AttemptLogin().execute();
		} else if (v.getId() == R.id.register_link) {
			Intent intent_register = new Intent(this.getApplicationContext(),
					Register.class);
			this.overridePendingTransition(0, 0);
			this.startActivity(intent_register);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	public void startAnimation() {
		ImageView imageToMove = (ImageView) findViewById(R.id.hugo_logo);
		animSplash = AnimationUtils
				.loadAnimation(this, R.anim.anim_logo_splash);
		animSplash.setAnimationListener(this);
		imageToMove.startAnimation(animSplash);
	}

	public void onAnimationStart(Animation animation) {
		// do nothing
	}

	public void onAnimationRepeat(Animation animation) {
		// do nothing
	}

	public void onAnimationEnd(Animation animation) {
		username.setVisibility(View.VISIBLE);
		password.setVisibility(View.VISIBLE);
		login_button.setVisibility(View.VISIBLE);
		username.startAnimation(controlAlphaAnimation);
		password.startAnimation(controlAlphaAnimation);
		login_button.startAnimation(controlAlphaAnimation);
	}

	class AttemptLogin extends AsyncTask<String, String, String> {
		private int success;
		private String message;
		private String user;
		private String pass;

		@Override
		protected java.lang.String doInBackground(java.lang.String... params) {
			// TODO Auto-generated method stub
			user = username.getText().toString();
			pass = password.getText().toString();
			getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
					.putString(PREFS_LAST_USERNAME, user).commit();
			

			try {
				List<NameValuePair> JsonParams = new ArrayList<NameValuePair>();
				JsonParams.add(new BasicNameValuePair("username", user));
				JsonParams.add(new BasicNameValuePair("password",
						Utils.ComputeMD5Hash(pass)));
				
				JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
						JsonParams);
				Log.i("json", json.toString());
				Log.e("data", "request done");
				this.success = json.getInt(TAG_SUCCESS);
				this.message = json.getString(TAG_MESSAGE);
				Log.e("data", String.valueOf(success));
				Log.e("data", Utils.ComputeMD5Hash(pass) + user);

				if (this.success == 1) {
					Log.e("data", String.valueOf(this.success));
					Log.e("data", "Message: " + json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);
				} else {
					Log.e("data", "Message: " + json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);
				}
			} catch (JSONException e) {
				Log.e("data", "failure by requesting http");
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			// dismiss the dialog once product deleted

			

			Log.e("data", "done.");
			if (success == 1) {
				getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
				.putString(PREFS_USERNAME, user)
				.putString(PREFS_PASSWORD, pass).commit();
				Toast toast = Toast.makeText(getApplicationContext(),
						"Login erfolgreich", Toast.LENGTH_LONG);
				toast.show();
				finish();
				Intent home = new Intent(getApplicationContext(),
						LogoScreen.class);
				startActivity(home);

			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Login gescheitert", Toast.LENGTH_LONG);
				//toast.show();
				//errorText.setText(getString(R.string.login_failed));
				errorText.setText(this.message);
				errorText.setVisibility(View.VISIBLE);
				// login_button.setClickable(true);
				login_button.setEnabled(true);
				bar.setVisibility(View.INVISIBLE);
				// TODO: implementing AlertDialog
				/*
				 * AlertDialog.Builder builder = new
				 * AlertDialog.Builder(getApplication());
				 * builder.setMessage(R.string
				 * .dialog_failure_login).setTitle(R.string
				 * .dialog_failure_login_title); AlertDialog dialog =
				 * builder.create(); dialog.show();
				 */
			}
		}
	}
}