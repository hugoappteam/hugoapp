package de.hjg.hugojunkersapp.activities.vertretung;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.astuetz.PagerSlidingTabStrip;

import de.hjg.hugojunkersapp.R;
import de.hjg.hugojunkersapp.SQLite.VertretungData;
import de.hjg.hugojunkersapp.SQLite.VertretungDataSource;
import de.hjg.hugojunkersapp.general.SettingsActivity;

public class RepresentSelectedFragment extends SherlockFragment /*
																 * implements
																 * AdapterView.
																 * OnItemClickListener
																 */{

	private String klasse;
	private SelectedTabAdapter mAdapter;
	private DateFormat df = new SimpleDateFormat("EEEE, dd.MM.yy");
	private ArrayList<String> days = new ArrayList<String>();
	// private static ArrayList<VertretungData> vertretungen = new
	// ArrayList<VertretungData>();
	private ArrayList<ArrayList<VertretungData>> vertretungen = new ArrayList<ArrayList<VertretungData>>();
	
	@Override 
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) { 
		inflater.inflate(R.menu.share_menu, menu);
		super.onCreateOptionsMenu(menu, inflater); 
	}
	 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		Log.e("data", "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e("data", "onCreateView");
		container.removeAllViews();
		setHasOptionsMenu(true);
		this.klasse = getArguments().getString("Klasse");
		reorganizeVertretungen();
		// Add matching VertretungDatax
		return inflater.inflate(R.layout.represent_all_activity, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.e("data", "onViewCreated");
		Log.e("debugigng", "View created");
		super.onViewCreated(view, savedInstanceState);

		if(days.size() != 0) {
			PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
			ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
			
			mAdapter = new SelectedTabAdapter(getChildFragmentManager());
			mAdapter.setData(vertretungen, this.getSherlockActivity()
					.getApplicationContext(), days);
			pager.setAdapter(mAdapter);
			tabs.setViewPager(pager);
		} else {
			Toast.makeText(this.getSherlockActivity(), "Es liegen keine Vertretungen vor", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.action_share:
				share();
				Log.e("FragmentOptionsCalled", (String) item.getTitle());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
			SharedPreferences sharedPref = PreferenceManager
					.getDefaultSharedPreferences(getSherlockActivity().getApplicationContext());
			if (sharedPref.getString("pref_Klasse", null) != null) {
				this.klasse = sharedPref.getString("pref_Klasse", null);
				reorganizeVertretungen();
			} else {
				Toast.makeText(getSherlockActivity().getApplicationContext(), "Du hast noch keine Klasse eingestellt.",
						Toast.LENGTH_LONG).show();
				
				Intent settings = new Intent(getSherlockActivity().getApplicationContext(),	SettingsActivity.class);
				this.startActivity(settings);
			}
			
			if(days.size() != 0) {
				PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) this.getView().findViewById(R.id.tabs);
				ViewPager pager = (ViewPager) this.getView().findViewById(R.id.pager);
				
				mAdapter = new SelectedTabAdapter(getChildFragmentManager());
				mAdapter.setData(vertretungen, this.getSherlockActivity()
						.getApplicationContext(), days);
				pager.setAdapter(mAdapter);
				tabs.setViewPager(pager);
			} else {
				Toast.makeText(this.getSherlockActivity(), "Es liegen keine Vertretungen vor", Toast.LENGTH_LONG).show();
			}

		
		
	}

	private void share() {
		
		final VertretungData vert;
		vert = VertretungDataSource.getVertretungByID(helperShareClass.getSelection());
		if(!helperShareClass.isSelected()){
			Toast.makeText(getActivity(), "Wähle zuerst ein Element aus", Toast.LENGTH_LONG).show();
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setPositiveButton("Ja", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
	
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, InfoPopup.getShareSubject(vert));
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, InfoPopup.getShareBody(vert));
				startActivity(Intent.createChooser(sharingIntent, "Teilen via"));
				
			}
		});
		builder.setNegativeButton("Nein", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
	
		builder.setMessage("Möchtest du diese Vertretung teilen?")
	       		.setTitle("Teilen?");
		AlertDialog dialog = builder.create();
		dialog.show();
			
	}	

	private void reorganizeVertretungen() {
		Date oldday = null;
		days = new ArrayList<String>();
		ArrayList<VertretungData> vertretung = new ArrayList<VertretungData>();
		for (VertretungData vert : VertretungDataSource
				.getVertretungenByClass(this.klasse)) {
			try{
				if (oldday.compareTo(vert.getDatum()) != 0 && oldday != null) {
					
					vertretungen.add(vertretung);				
					vertretung = new ArrayList<VertretungData>();
					
					days.add(df.format(vert.getDatum()));
				}
			}catch (NullPointerException e){
				try {
				days.add(df.format(vert.getDatum()));
				} catch (Exception ex) {
					Log.e("adta", "ExMessage: " + ex.getMessage());
				}
			}

			vertretung.add(vert);
			oldday = vert.getDatum();
		}
		vertretungen.add(vertretung);
	}
}
