package de.hjg.hugojunkersapp.activities.vertretung;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
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

public final class RepresentAllFragment extends SherlockFragment  {
	private TabAdapter mAdapter;
	private ArrayList<ArrayList<VertretungData>> vertretungen = new ArrayList<ArrayList<VertretungData>>();
	private Date oldday = null;
	private SimpleDateFormat df = new SimpleDateFormat("EEEE, dd.MM.yy");


	private boolean selected = false;
	private View view;
	
	public static RepresentAllFragment newInstance() {
		return new RepresentAllFragment();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {		
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.share_menu, menu);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getSherlockActivity().setTitle("Alle Vertretungen");
		//container.removeAllViews();
		setHasOptionsMenu(true);
		new SQLRequest(getSherlockActivity().getApplicationContext()).execute();
		reorganizeVertretungen();
		return inflater.inflate(R.layout.represent_all_activity, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.e("debugigng", "View created");

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
				.findViewById(R.id.tabs);
		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		mAdapter = new TabAdapter(getChildFragmentManager());
		mAdapter.setData(vertretungen, this.getSherlockActivity()
				.getApplicationContext());
		pager.setAdapter(mAdapter);
		tabs.setViewPager(pager);
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
		ArrayList<VertretungData> vertretung = new ArrayList<VertretungData>();
		for (VertretungData vert : VertretungDataSource.getAllVertretungen()) {
			if (oldday != null && oldday.compareTo(vert.getDatum()) != 0) {
				vertretungen.add(vertretung);
				vertretung = new ArrayList<VertretungData>();
			}
			vertretung.add(vert);
			oldday = vert.getDatum();
		}
		vertretungen.add(vertretung);
	}
}