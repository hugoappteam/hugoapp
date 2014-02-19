package de.hjg.hugojunkersapp.activities.vertretung;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import de.hjg.hugojunkersapp.R;
import de.hjg.hugojunkersapp.SQLite.VertretungData;
import de.hjg.hugojunkersapp.SQLite.VertretungDataSource;


public class RepresentSearchedActivity extends SherlockFragment implements AdapterView.OnItemClickListener{
	
	private ArrayList<VertretungData> searchableVertretungen = new ArrayList<VertretungData>();
	private ArrayList<VertretungData> vertretungen = new ArrayList<VertretungData>();
	private DateFormat df = new SimpleDateFormat("dd.MM.yy");
	private View view;
	private int selection;
	private boolean selected = false;
	
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


		StickyListHeadersListView stickyList = (StickyListHeadersListView) view.findViewById(R.id.list);
		StickyListAdapter adapter = new StickyListAdapter(getActivity(), vertretungen);
		if(stickyList == null) {
			Log.e("debug", "is null!!!");
		} else {
			Log.e("debug", "is not null!!!");
		}
		stickyList.setAdapter(adapter);
		stickyList.setOnItemClickListener(this);
		Log.e("debug,", "bis hier");
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
		selection = position;
		selected = true;
		
		InfoPopup.showData(vert, view, getActivity());	
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
		vert = vertretungen.get(selection);
		if(!selected){
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
