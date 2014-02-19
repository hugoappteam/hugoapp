package de.hjg.hugojunkersapp.activities.vertretung;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

import de.hjg.hugojunkersapp.R;
import de.hjg.hugojunkersapp.SQLite.VertretungData;

public class TabFragment extends SherlockFragment implements
		OnItemClickListener {

	private ArrayList<VertretungData> vertretungen = new ArrayList<VertretungData>();
	private SlidingUpPanelLayout UpPanel;
	private ListView list;
	private View view;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i("tabs", "called onCreateView");

		this.view = inflater.inflate(R.layout.tab_fragment, container, false);
		
		this.UpPanel = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
		this.UpPanel.setShadowDrawable(getResources().getDrawable(R.drawable.above_shadow));
		this.UpPanel.setAnchorPoint(0.5f);
		//this.UpPanel.setSlidingEnabled(false);
		
		this.UpPanel.setPanelSlideListener(new PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelExpanded(View panel) {
         TextView t = (TextView) view.findViewById(R.id.pull);
         t.setText("Zum Schließen herunterziehen.");
            }

            @Override
            public void onPanelCollapsed(View panel) {
         TextView t = (TextView) view.findViewById(R.id.pull);
         t.setText("Für genauere Infos zur zuletzt ausgewählten Vertretung hier ziehen.");

            }

            @Override
            public void onPanelAnchored(View panel) {
         TextView t = (TextView) view.findViewById(R.id.pull);
         t.setText("Zum Schließen herunterziehen.");

            }
        });
		
		TextView t = (TextView) view.findViewById(R.id.pull);
        t.setMovementMethod(LinkMovementMethod.getInstance());
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		int tab = 0;
		Log.i("TabFragment", "created");
		super.onActivityCreated(savedInstanceState);
		Log.i("TabFragment", "got ListView");
		try{
			Log.e("TabNumber", String.valueOf(getArguments().getInt("Tab")));
			tab = getArguments().getInt("Tab");
			
		}catch (Exception e){
			tab = 0;
		}
		Log.e("test", "test message 1");
		vertretungen = TabAdapter.vertretungen.get(tab);

		Log.i("Vertretungen/day", String.valueOf(vertretungen.size()));
		list = (ListView) view.findViewById(R.id.list);
		SimpleVertretungListAdapter adapter = new SimpleVertretungListAdapter(
				getActivity().getApplicationContext(), R.layout.row,
				vertretungen);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		

		Log.i("TabFragment", "set Adapter");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View vi, int position, long id) {
		helperShareClass.setSelection(vertretungen.get(position).getID());
		
		InfoPopup.showData(vertretungen.get(position), view, getActivity());
		this.UpPanel.expandPane(0.5f);
		TextView t = (TextView) view.findViewById(R.id.pull);
		t.setText("Zum Schließen herunterziehen.");
	}
	
	
}