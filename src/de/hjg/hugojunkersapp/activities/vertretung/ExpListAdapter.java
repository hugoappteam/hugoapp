package de.hjg.hugojunkersapp.activities.vertretung;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import de.hjg.hugojunkersapp.R;
import de.hjg.hugojunkersapp.SQLite.VertretungDataSource;

public class ExpListAdapter extends BaseExpandableListAdapter {

	public ArrayList<String> groupItem, tempChild;
	public ArrayList<Object> Childtem = new ArrayList<Object>();
	public LayoutInflater minflater;
	public Activity activity;

	public ExpListAdapter(ArrayList<String> grList, ArrayList<Object> childItem) {
		groupItem = grList;
		this.Childtem = childItem;
	}

	public void setInflater(LayoutInflater mInflater, Activity act) {
		this.minflater = mInflater;
		activity = act;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		tempChild = (ArrayList<String>) Childtem.get(groupPosition);
		TextView text = null;
		TextView number = null;

		if (convertView == null) {
			convertView = minflater.inflate(R.layout.child_row, null);
		}
		text = (TextView) convertView.findViewById(R.id.Head);
		number = (TextView) convertView.findViewById(R.id.tvNumber);
		text.setText(tempChild.get(childPosition));
		try {
		number.setText(String.valueOf(VertretungDataSource.getVertretungenByClass(tempChild.get(childPosition)).size()));
		} catch (Exception ex) {
			Log.e("data", "number: " + String.valueOf(VertretungDataSource.getVertretungenByClass(tempChild.get(childPosition)).size()));
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		if (groupPosition > 4) {
			return 0;
		}
		return ((ArrayList<String>) Childtem.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groupItem.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		CheckedTextView text = null;
		TextView tvNumber = null;

		if (convertView == null) {
			convertView = minflater.inflate(R.layout.group_row, null);
		}
		text = (CheckedTextView) convertView.findViewById(R.id.groupText);
		tvNumber = (TextView) convertView.findViewById(R.id.tvNumber);

		text.setText(groupItem.get(groupPosition));
		tvNumber.setText(String.valueOf(VertretungDataSource
				.getVertretungenByClass(groupItem.get(groupPosition)).size()));

		text.setChecked(isExpanded);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
