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
import de.hjg.hugojunkersapp.SQLite.VertretungDataSource;

public class TabAdapter extends FragmentPagerAdapter {

	private ArrayList<String> days = new ArrayList<String>();
	public static ArrayList<ArrayList<VertretungData>> vertretungen = new ArrayList<ArrayList<VertretungData>>();
	// public static ArrayList<VertretungData> vertretungen;
	private DateFormat df = new SimpleDateFormat("EEEE, dd.MM.yy");

	public TabAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	public void setData(ArrayList<ArrayList<VertretungData>> vert, Context c) {
		days = VertretungDataSource.GetDaysAsStrings("EEEE, dd.MM.yy");
		vertretungen = vert;
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new TabFragment();
		Bundle args = new Bundle();
		Log.e("TabNumber", String.valueOf(i));
		args.putInt("Tab", i);
		fragment.setArguments(args);
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