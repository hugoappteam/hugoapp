package de.hjg.hugojunkersapp.activities.vertretung;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import de.hjg.hugojunkersapp.R;
import de.hjg.hugojunkersapp.SQLite.VertretungData;

public class StickyListAdapter extends BaseAdapter  implements
		StickyListHeadersAdapter, SectionIndexer {


	private Context context;
	private ArrayList<VertretungData> vertretungen;
	private LayoutInflater inflater;
	private int[] mSectionIndices;

	private ArrayList<String> days = new ArrayList<String>();
	private Date oldday = null;
	private DateFormat df = new SimpleDateFormat("EEEE, dd.MM.yy");

	public StickyListAdapter(Context context,
			ArrayList<VertretungData> vertretungen) {
		this.context = context;
		this.vertretungen = vertretungen;
		inflater = LayoutInflater.from(context);
		mSectionIndices = getIndices();
	}

	private int[] getIndices() {
		ArrayList<Integer> sectionsList = new ArrayList<Integer>();
		int i = 0;
		for (VertretungData vert : vertretungen) {

			if (oldday == null || oldday.compareTo(vert.getDatum()) != 0) {
				days.add(df.format(vert.getDatum()));
				sectionsList.add(i);
			}
			i++;
		}

		int[] sections = new int[sectionsList.size()];
		for (int a = 0; a < sectionsList.size(); a++) {
			sections[a] = sectionsList.get(a);
		}
		return sections;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return vertretungen.size();
	}

	@Override
	public VertretungData getItem(int position) {
		// TODO Auto-generated method stub
		return vertretungen.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		VertretungData vert = vertretungen.get(position);
		TextView tvClass = null;
		TextView tvHour = null;
		TextView tvVertreter = null;
		TextView tvTeacher = null;

		tvClass = new TextView(context);
		tvHour = new TextView(context);
		tvVertreter = new TextView(context);
		tvTeacher = new TextView(context);
		convertView = inflater.inflate(R.layout.sticky_row, parent, false);
		tvClass = (TextView) convertView.findViewById(R.id.tvClass);
		tvHour = (TextView) convertView.findViewById(R.id.tvHour);
		tvVertreter = (TextView) convertView.findViewById(R.id.tvVertreter);
		tvTeacher = (TextView) convertView.findViewById(R.id.tvTeacher);

		convertView.setTag(tvClass);
		convertView.setTag(tvHour);
		convertView.setTag(tvVertreter);
		convertView.setTag(tvTeacher);

		tvClass.setText(vert.getKlasse() + ":");
		tvHour.setText(vert.getStunde() + " St.");
		tvVertreter.setText(vert.getVertreter());
		tvTeacher.setText(vert.getLehrer());
		if (vert.getInfo().equals("Klausur")
				|| vert.getInfo().equals("Klassenarbeit")) {
			tvVertreter.setText(vert.getInfo());
			tvTeacher.setText("Aufsicht: " + vert.getVertreter());
		}

		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		TextView holder = new TextView(context);
		//TextView number = new TextView(context);
		if (convertView == null) {
			holder = new TextView(context);
			convertView = inflater.inflate(R.layout.child_row, parent, false);
			holder = (TextView) convertView.findViewById(R.id.Head);
			//number = (TextView) convertView.findViewById(R.id.tvNumber);
			convertView.setTag(holder);
			//convertView.setTag(number);
		} else {
			holder = (TextView) convertView.getTag();
			//number = (TextView) convertView.getTag();
		}
		
		if(holder == null) {
			Log.e("debug", "ist null");
		} else {
			Log.e("debug", "ist nicht null: " + vertretungen.get(position).getDatum());
		}
		holder.setText(df.format(vertretungen.get(position).getDatum()));
		//number.setText(String.valueOf(mSectionIndices[getSectionForPosition(position)+1]));

		return convertView;
	}

	@Override
	public long getHeaderId(int position) {

		int i = 0;
		for (String day : days) {
			if (day.equalsIgnoreCase(df.format(vertretungen.get(position).getDatum()))) {
				return i;
			}
			i++;

		}

		return 0;
	}

	@Override
	public int getPositionForSection(int section) {
		if (section >= mSectionIndices.length) {
			section = mSectionIndices.length - 1;
		} else if (section < 0) {
			section = 0;
		}
		return mSectionIndices[section];
	}

	@Override
	public int getSectionForPosition(int position) {
		for (int i = 0; i < mSectionIndices.length; i++) {
			if (position < mSectionIndices[i]) {
				return i - 1;
			}
		}
		return mSectionIndices.length - 1;
	}

	@Override
	public Object[] getSections() {

		Object[] obj = new Object[days.size()];
		for (int i = 0; i < days.size(); i++) {
			obj[i] = days.get(i);
		}

		return obj;
	}

}
