package de.hjg.hugojunkersapp.start;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import de.hjg.hugojunkersapp.R;
import de.hjg.hugojunkersapp.general.Utils;

public class Register extends Activity implements OnClickListener {

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private final String LOGIN_URL = "http://hjg.pf-control.de/JSONRegister.php";

	private EditText username;
	private EditText password;
	private EditText password_retry;
	private EditText email;
	private Spinner studentClass;
	private EditText PersonName;
	private Button register_button;
	private TextView info;
	private ProgressBar bar;

	private String username_string;
	private String password_string;
	private String password_retry_string;
	private String email_string;
	private String studentClass_string;
	private String PersonName_string;

	private JSONParser jsonParser = new JSONParser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		username = (EditText) findViewById(R.id.register_username);
		password = (EditText) findViewById(R.id.register_password);
		password_retry = (EditText) findViewById(R.id.register_password_again);
		email = (EditText) findViewById(R.id.register_email);
		PersonName = (EditText) findViewById(R.id.register_personName);
		studentClass = (Spinner) findViewById(R.id.register_class);
		register_button = (Button) findViewById(R.id.button_register);
		register_button.setOnClickListener(this);
		info = (TextView) findViewById(R.id.register_info);
		info.setTextColor(Color.RED);
		bar = (ProgressBar) findViewById(R.id.register_progressBar);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button_register) {
			if (checkForm())
				showDialog();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	// Checks the register form for false data like email without
	// an @ or someting else
	private boolean checkForm() {
		username_string = username.getText().toString();
		password_string = password.getText().toString();
		password_retry_string = password_retry.getText().toString();
		email_string = email.getText().toString();
		studentClass_string = studentClass.getSelectedItem().toString();
		PersonName_string = PersonName.getText().toString();

		// Check for empty field
		if (username_string.matches("") || password_string.matches("")
				|| password_retry_string.matches("")
				|| email_string.matches("") || studentClass_string.matches("")
				|| PersonName_string.matches("")) {
			info.setText(R.string.fields_empty);
			return false;

		} else if (!PersonName_string.contains(" ")) {
			info.setText(R.string.personName_no_space);
			return false;

		} else if (!password_string.equals(password_retry_string)) {
			info.setText(R.string.passwords_not_equal);
			return false;
		} // else if(!email_string.contains("@")) {
			// else if(!email_string.contains("@") ||
			// (email_string.substring(email_string.length() - 1).equals("@")))
			// {
		else if (!Patterns.EMAIL_ADDRESS.matcher((CharSequence) email_string)
				.matches()) {
			info.setText(R.string.email_invalid);
			return false;
		} else if (password_string.length() < 5) {
			info.setText(R.string.password_too_short);
			return false;
		} else if (!Utils.CheckStringForName(PersonName_string)
				|| PersonName_string.matches(".*\\d.*")) {
			info.setText(R.string.personName_not_valid);
			return false;
		}

		else {
			// All offline requirements are okay
			info.setText("");
			return true;
		}
	}

	private void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Html
				.fromHtml("<b>Sind die Daten korrekt?</b><br><br>" + "<b>"
						+ getString(R.string.username) + ": </b>"
						+ this.username_string + "<br>" + "<b>"
						+ getString(R.string.name) + ": </b>"
						+ this.PersonName_string + "<br>" + "<b>"
						+ getString(R.string.email_hint) + ": </b>"
						+ this.email_string + "<br>" + "<b>"
						+ getString(R.string.course) + ": </b>"
						+ this.studentClass_string + "<br>" + "<b>"
						+ getString(R.string.password_hint) + ": </b>"
						+ Utils.GivePasswordAsPoints(this.password_string)));
		builder.setPositiveButton(getString(R.string.register_splash),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						register_button.setEnabled(false);
						bar.setVisibility(View.VISIBLE);
						new AttemptRegister().execute();
					}
				});
		builder.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void showDialogAfterRegister() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Html
				.fromHtml("<b>Erfolgreich registriert!</b><br>In deinem E-Mail-Postfach liegt nun eine Bestätigungsemail. Klicke darin auf den Link um deine E-Mail-Adresse zu bestätigen. Danke"));
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
				Intent splash = new Intent(getApplicationContext(),
						SplashScreen.class);
				startActivity(splash);
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	class AttemptRegister extends AsyncTask<String, String, String> {
		int success;
		String Names[] = PersonName_string.split(" ");
		private JSONObject json;
		private String message;

		@Override
		protected java.lang.String doInBackground(java.lang.String... params) {
			// TODO Auto-generated method stub

			try {
				List<NameValuePair> JsonParams = new ArrayList<NameValuePair>();
				JsonParams.add(new BasicNameValuePair("username",
						username_string));
				JsonParams.add(new BasicNameValuePair("password", Utils
						.ComputeMD5Hash(password_string)));
				JsonParams.add(new BasicNameValuePair("mail", email_string));
				JsonParams.add(new BasicNameValuePair("grade",
						studentClass_string));
				JsonParams.add(new BasicNameValuePair("surname", Names[1]
						.toString()));
				JsonParams.add(new BasicNameValuePair("forename", Names[0]
						.toString()));
				json = jsonParser
						.makeHttpRequest(LOGIN_URL, "POST", JsonParams);
				success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					Log.e("data", String.valueOf(success));
					Log.e("data", "Message: " + json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);
				} else {
					Log.e("data", "Message: " + json.getString(TAG_MESSAGE));
					message = json.getString(TAG_MESSAGE);
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
			if (success == 1) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Registrierung erfolgreich", Toast.LENGTH_LONG);
				toast.show();
				bar.setVisibility(View.INVISIBLE);
				showDialogAfterRegister();

			} else {
				Toast toast = Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG);
				toast.show();
				info.setText(message);
				register_button.setEnabled(true);
				bar.setVisibility(View.INVISIBLE);
			}
		}
	}
}