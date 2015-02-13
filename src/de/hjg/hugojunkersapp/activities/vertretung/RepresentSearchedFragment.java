package de.hjg.hugojunkersapp.activities.vertretung;
import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

import de.hjg.hugojunkersapp.R;
import de.hjg.hugojunkersapp.SQLite.VertretungData;
import de.hjg.hugojunkersapp.SQLite.VertretungDataSource;


public class RepresentSearchedFragment extends SherlockFragment implements AdapterView.OnItemClickListener{
	
	private ArrayList<VertretungData> searchableVertretungen = new ArrayList<VertretungData>();
	private ArrayList<VertretungData> vertretungen = new ArrayList<VertretungData>();
	private SlidingUpPanelLayout UpPanel;
	private View view;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.share_menu, menu);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		container.removeAllViews();
		setHasOptionsMenu(true);
		String search = getArguments().getString("Suche");
		String [] searched = search.split(" |,|;");
		
		try
		{
			searchableVertretungen = VertretungDataSource.getVertretungenByClass(getArguments().getString("Klasse"));
		}
		catch (NullPointerException ex)
		{
			searchableVertretungen = VertretungDataSource.getAllVertretungen();
		}

		
		search(searched);
		
		if(vertretungen.size() == 0)
		{
			searchableVertretungen = VertretungDataSource.getAllVertretungen();
			search(searched);
			if(vertretungen.size() == 0)
			{
				vertretungen = VertretungDataSource.getAllVertretungen();
				Toast.makeText(getActivity(), "Für deine Suche wurden keine Ergebnisse gefunden.", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(getActivity(), "Es wurden für die ausgewählte Klasse keine Suchergebnisse gefunden.", Toast.LENGTH_SHORT).show();
			}
		}
		
		view = inflater.inflate(R.layout.represent_searched_fragment, container);
		
		this.UpPanel = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
		this.UpPanel.setBackgroundDrawable(getResources().getDrawable(R.drawable.above_shadow));
		this.UpPanel.setAnchorPoint(0.3f);
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

			@Override
			public void onPanelHidden(View panel) {
				// TODO Auto-generated method stub
				
			}
        });
		
		
		
		TextView t = (TextView) view.findViewById(R.id.pull);
        t.setMovementMethod(LinkMovementMethod.getInstance());


		StickyListHeadersListView stickyList = (StickyListHeadersListView) view.findViewById(R.id.list);
		StickyListAdapter adapter = new StickyListAdapter(getActivity(), vertretungen);
		stickyList.setAdapter(adapter);
		stickyList.setOnItemClickListener(this);
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
		
	private void search(String[] search) {

		if(search.length == 1)
		{
			String s = search[0];
			for(VertretungData vert : searchableVertretungen)
			{
				if(	contains(s, vert))
				{
					vertretungen.add(vert);
				}
			}
		}
		else
		{
			int [] ids = new int [searchableVertretungen.size()];
			
			for(String s : search)
			{
				for(VertretungData vert : searchableVertretungen)
				{
					if(contains(s, vert))
					{
						ids[vert.getID()]++;
					}
				}
			}
			
			for(int i = 0; i<ids.length; i++)
			{
				if(ids[i]<1)
				{
					vertretungen.add(VertretungDataSource.getVertretungByID(i));
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View vi, int position, long id) {
		//InfoPopup.showInfoPopup(Vertretungen.get(position), getActivity(), parent);
		
		VertretungData vert = vertretungen.get(position);
		helperShareClass.setSelection(vert.getID());
		
		InfoPopup.showData(vertretungen.get(position), view, getActivity());
		this.UpPanel.expandPanel();
		TextView t = (TextView) view.findViewById(R.id.pull);
		t.setText("Zum Schlie√üen herunterziehen.");
		
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

	public void share() {
		
		final VertretungData vert;
		vert = VertretungDataSource.getVertretungByID(helperShareClass.getSelection());
		if(!helperShareClass.isSelected()){
			Toast.makeText(getActivity(), "W√§hle zuerst ein Element aus", Toast.LENGTH_LONG).show();
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

		builder.setMessage("M√∂chtest du diese Vertretung teilen?")
	       		.setTitle("Teilen?");
		AlertDialog dialog = builder.create();
		dialog.show();
		
	}
	
	private static boolean contains (String key, VertretungData vert)
	{
		if(	containsString(vert.getKlasse(), key) ||
				containsString(vert.getFach(), key) ||
				containsString(vert.getInfo(), key) ||
				containsString(vert.getLehrer(), key) ||
				containsString(vert.getVertreter(), key) ||
				containsString(String.valueOf(vert.getStunde()), key) )
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
    private static boolean containsString( String s, String subString ) {
        
    	boolean match = s.toLowerCase().contains(subString.toLowerCase());
    	
    	
    	return match;
    }

}
