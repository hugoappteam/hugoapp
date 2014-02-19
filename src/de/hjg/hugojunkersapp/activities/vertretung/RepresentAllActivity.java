package de.hjg.hugojunkersapp.activities.vertretung;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.astuetz.PagerSlidingTabStrip;

import de.hjg.hugojunkersapp.R;
import de.hjg.hugojunkersapp.SQLite.VertretungData;
import de.hjg.hugojunkersapp.SQLite.VertretungDataSource;

public final class RepresentAllActivity extends SherlockFragment {
	private TabAdapter mAdapter;
	private ArrayList<ArrayList<VertretungData>> vertretungen = new ArrayList<ArrayList<VertretungData>>();
	private Date oldday = null;
	private SimpleDateFormat df = new SimpleDateFormat("EEEE, dd.MM.yy");

	public static RepresentAllActivity newInstance() {
		return new RepresentAllActivity();
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
		// container.removeAllViews();
		// setHasOptionsMenu(true);
		new SQLRequest(getSherlockActivity().getApplicationContext()).execute();
		reorganizeVertretungen();

		Log.e("debugigng", "View creating");
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