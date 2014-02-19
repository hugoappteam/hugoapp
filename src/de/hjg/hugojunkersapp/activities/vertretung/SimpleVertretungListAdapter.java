package de.hjg.hugojunkersapp.activities.vertretung;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.hjg.hugojunkersapp.R;
import de.hjg.hugojunkersapp.SQLite.VertretungData;

public class SimpleVertretungListAdapter extends ArrayAdapter<VertretungData> {

	private ArrayList<VertretungData> vertretungen;
	private Context context;
	private LayoutInflater inflater;
	int layoutResId = 0;

	public SimpleVertretungListAdapter(Context context, int resid,
			ArrayList<VertretungData> vertretungen) {
		super(context, resid, vertretungen);
		this.vertretungen = vertretungen;
		this.inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		layoutResId = resid;
		Log.i("ListAdapter", "Adapter called");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		VertretungData vert = vertretungen.get(position);
		TextView tvClass = new TextView(context);
		;
		TextView tvHour = new TextView(context);
		TextView tvVertreter = new TextView(context);
		TextView tvTeacher = new TextView(context);

		convertView = inflater.inflate(layoutResId, parent, false);
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

		// First implementation of getView
		/*
		 * 
		 * if(convertView == null) { LayoutInflater inflater = (LayoutInflater)
		 * getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 * convertView = inflater.inflate(layoutResId, parent, false);
		 * Log.i("ListAdapter", "inflated View"); } TextView text = (TextView)
		 * convertView.findViewById(R.id.Head); Log.i("ListAdapter",
		 * "got TextView");
		 * 
		 * 
		 * 
		 * text.setText(vert.get(position).getKlasse() + ": " +
		 * vert.get(position).getStunde() + ". Vertretung " +
		 * vert.get(position).getVertreter() + " für " +
		 * vert.get(position).getLehrer());
		 * 
		 * if(vert.get(position).getVertreter().equals("+")) {
		 * text.setText(vert.get(position).getKlasse() + ": " +
		 * vert.get(position).getStunde() + ". selbständiges Arbeiten " +
		 * vert.get(position).getLehrer()); }
		 * if(vert.get(position).getVertreter().equals("---")) {
		 * text.setText(vert.get(position).getKlasse() + ": " +
		 * vert.get(position).getStunde() + ". Entfall " +
		 * vert.get(position).getLehrer()); }
		 * if(vert.get(position).getInfo().equals("Klausur")) {
		 * text.setText(vert.get(position).getKlasse() + ": " +
		 * vert.get(position).getStunde() + ". Klausur, Aufsicht: " +
		 * vert.get(position).getVertreter()); } Log.i("ListAdapter",
		 * "set Text");
		 * 
		 * 
		 * return convertView;
		 */
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.i("ListAdapter", "got Count" + String.valueOf(vertretungen.size()));
		return vertretungen.size();
	}

}