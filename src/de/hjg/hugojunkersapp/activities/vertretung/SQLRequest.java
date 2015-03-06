package de.hjg.hugojunkersapp.activities.vertretung;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import de.hjg.hugojunkersapp.R;
import de.hjg.hugojunkersapp.SQLite.VertretungDataSource;
import de.hjg.hugojunkersapp.general.LogoScreen;
import de.hjg.hugojunkersapp.general.SendNotification;
import de.hjg.hugojunkersapp.general.Utils;

@SuppressLint("NewApi")
public class SQLRequest extends AsyncTask<Context, Integer, Boolean> {
	private static String URL;
	private boolean success;
	private String result = null;
	private Context context = null;
	private SendNotification notify = null;
	SharedPreferences sharedPref;

	public SQLRequest(Context con) {
		context = con;
		URL = con.getString(R.string.JSONAddress);
		sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		
	}

	@Override
	protected void onPreExecute() {

		if (sharedPref.getString("pref_Klasse", null) != null) {
			notify = new SendNotification(context,
					VertretungDataSource.getVertretungenByClass(sharedPref
							.getString("pref_Klasse", null)));
		}

		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(Context... con) {
		
		if(!Utils.checkForInternetConnectivity(this.context)) {
			result = "Keine Internetverbindung - Nur Lokale Daten verfügbar";
			return false;
		}
		
		success = true;

		List<NameValuePair> pair = new ArrayList<NameValuePair>();
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(URL);
		InputStream is = null;

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(pair));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();

			is = httpEntity.getContent();
		} catch (Exception e) {
			// HTTP Error
			Log.e("log_tag", "Error in http connection " + e.toString());
			success = false;

		}

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();

		} catch (Exception e) {
			// Error converting result
			Log.e("log_tag", "Error converting result " + e.toString());
			success = false;
		}

		if (success) {
			VertretungDataSource.fillDatabase(VertretungDataSource
					.getVertretungenFromJSON(result));
		}

		return success;
	}

	// refresh UI
	@Override
	protected void onPostExecute(Boolean re) {
		Log.e("tag_success", "data loaded");
		if(!success) {
			Toast toast = Toast.makeText(context, result, Toast.LENGTH_LONG);
			toast.show();
		} else {
			LogoScreen.getExpListView().setAdapter(LogoScreen.getExpandableListAdapter());
			
			if (notify != null) {
				notify.setNew(VertretungDataSource
						.getVertretungenByClass(sharedPref.getString("pref_Klasse",
								null)));
			}
			super.onPostExecute(re);
		}
		/*
		 * try { if(LogoScreen.getSelectedClass() == "") {
		 * if(LogoScreen.getSelectedItem() == 0) { SherlockFragment fr = new
		 * RepresentAllActivity(); FragmentManager fragmentManager =
		 * ((SherlockActivity) context).getSupportFragmentManager();
		 * fragmentManager.beginTransaction().replace(R.id.content_frame,
		 * fr).commit(); } if(LogoScreen.getSelectedItem() == 2) {
		 * SherlockPreferenceActivity fr = new SettingsActivity();
		 * 
		 * Intent settings = new Intent(this, SettingsActivity.class);
		 * 
		 * 
		 * FragmentManager fragmentManager = ((Activity)
		 * context).getFragmentManager();
		 * fragmentManager.beginTransaction().replace(R.id.content_frame,
		 * fr).commit(); } } else { SherlockFragment fr = new
		 * RepresentSelectedActivity(); Bundle args = new Bundle();
		 * args.putString("Klasse", LogoScreen.getSelectedClass());
		 * fr.setArguments(args);
		 * 
		 * FragmentManager fragmentManager = ((Activity)
		 * context).getFragmentManager();
		 * fragmentManager.beginTransaction().replace(R.id.content_frame,
		 * fr).commit();
		 * 
		 * } } catch (IllegalStateException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */
		
	}

	protected void onProgressUpdate() {
		Toast toast = Toast.makeText(context, "fetched new from mysql server",
				Toast.LENGTH_LONG);
		toast.show();
	}
	
	
	private boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if(netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

}
