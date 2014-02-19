package de.hjg.hugojunkersapp.activities.vertretung;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import de.hjg.hugojunkersapp.SQLite.VertretungData;

public class SelectedTabAdapter extends FragmentPagerAdapter {

	private ArrayList<String> days = new ArrayList<String>();
	public static ArrayList<ArrayList<VertretungData>> vertretungen = new ArrayList<ArrayList<VertretungData>>();
	private DateFormat df = new SimpleDateFormat("EEEE, dd.MM.yy");

	public SelectedTabAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	public void setData(ArrayList<ArrayList<VertretungData>> vert, Context c, ArrayList<String> days) {
		
		this.days = days;
		vertretungen = vert;
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new SelectedTabFragment();
		Bundle args = new Bundle();
		args.putInt("Tab", i);
		fragment.setArguments(args);
		Log.i("TabAdapter", "set fragment");
		return fragment;
	}

	@Override
	public int getCount() {
		return days.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return days.get(position);
	}
}