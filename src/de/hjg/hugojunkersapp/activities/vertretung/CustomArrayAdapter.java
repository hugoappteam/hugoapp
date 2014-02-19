package de.hjg.hugojunkersapp.activities.vertretung;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.hjg.hugojunkersapp.R;

public class CustomArrayAdapter extends ArrayAdapter<String> {

	private Context context;
	private String[] items;
	private int layoutResource;

	public CustomArrayAdapter(Context context, int resource, String[] items) {
		super(context, resource, items);
		this.context = context;
		this.items = items;
		this.layoutResource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		Holder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResource, parent, false);

			holder = new Holder();
			holder.imgIcon = (ImageView) row.findViewById(R.id.icon);
			holder.txtTitle = (TextView) row.findViewById(R.id.text1);

			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		holder.txtTitle.setText(items[position]);
		switch (position) {
		case 0:
			holder.imgIcon
					.setImageResource(R.drawable.ic_action_calendar_month);
			break;
		case 1:
			holder.imgIcon.setImageResource(R.drawable.ic_action_calendar_day);
			break;
		case 2:
			holder.imgIcon.setImageResource(R.drawable.ic_action_settings);
			break;
		case 3:
			holder.imgIcon.setImageResource(R.drawable.ic_action_lock_open);
			break;
		}
		return row;

	}

	static class Holder {
		ImageView imgIcon;
		TextView txtTitle;
	}

}