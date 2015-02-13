package de.hjg.hugojunkersapp.general;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import de.hjg.hugojunkersapp.R;
import de.hjg.hugojunkersapp.SQLite.VertretungDataSource;
import de.hjg.hugojunkersapp.activities.vertretung.CustomArrayAdapter;
import de.hjg.hugojunkersapp.activities.vertretung.ExpListAdapter;
import de.hjg.hugojunkersapp.activities.vertretung.RepresentAllFragment;
import de.hjg.hugojunkersapp.activities.vertretung.RepresentSearchedFragment;
import de.hjg.hugojunkersapp.activities.vertretung.RepresentSelectedFragment;
import de.hjg.hugojunkersapp.activities.vertretung.SQLRequest;
import de.hjg.hugojunkersapp.start.SplashScreen;

public class LogoScreen extends SherlockFragmentActivity {

	private String[] lItems = {"Alle Vertretungen", "Meine Vertretungen",
			"Einstellungen", "Ausloggen" };
	private DrawerLayout lDrawerLayout;
	private ListView lDrawerList;
	private ActionBarDrawerToggle lDrawerToggle;
	private String lDrawerTitle = "Navigation";
	private Context con = this;
	
	private LinearLayout drawerView;

	private static ExpandableListView rDrawerList;
	private static ExpListAdapter expListAdapter;

	private static String[][] Klassen = new String[][] {
			{ "5", "5a", "5b", "5c" }, { "6", "6a", "6b", "6c" },
			{ "7", "7a", "7b", "7c" }, { "8", "8a", "8b", "8c" },
			{ "9", "9a", "9b", "9c" }, { "EF" }, { "Q1" }, { "Q2" } };
	private ArrayList<String> groupItem = new ArrayList<String>();
	private ArrayList<Object> childItem = new ArrayList<Object>();
	private static String selectedClass = "";
	private static int selectedItem = -1;

	private String Title;

	// Build menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Loading data from mysql server
		VertretungDataSource verDS = new VertretungDataSource(this);
		verDS.open();

		update.start();

		// prepare left-hand sidenavigation
		lDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		drawerView = (LinearLayout) findViewById(R.id.drawer_view);
		
		lDrawerList = (ListView) findViewById(R.id.left_drawer);

		lDrawerList.setAdapter(new CustomArrayAdapter(this,
				R.layout.drawer_list_item, lItems));

		lDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		// left-hand sidenavigation eventhandler
		lDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		lDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {

				if (((String) getActionBar().getTitle()).equals(lDrawerTitle)) {
					getActionBar().setTitle(Title);
				}
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				Title = (String) getActionBar().getTitle();
				getActionBar().setTitle(lDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		lDrawerLayout.setDrawerListener(lDrawerToggle);

		// prepare data for right-hand sidenavigation
		setGroupData();
		setSubData();
		// prepare right-hand sidenavigation
		rDrawerList = (ExpandableListView) findViewById(R.id.right_drawer);

		expListAdapter = new ExpListAdapter(groupItem, childItem);
		expListAdapter.setInflater(getLayoutInflater(), this);
		rDrawerList.setAdapter(expListAdapter);
		// eventhandler for class selection
		rDrawerList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				SherlockFragment fr = new RepresentSelectedFragment();
				Bundle args = new Bundle();
				args.putString("Klasse",
						Klassen[groupPosition][childPosition + 1]);
				fr.setArguments(args);

				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fr).commit();

				setTitle(Klassen[groupPosition][childPosition + 1]);
				selectedClass = Klassen[groupPosition][childPosition + 1];
				lDrawerLayout.closeDrawer(rDrawerList);
				return false;
			}
		});
		rDrawerList.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if (groupPosition > 4) {
					SherlockFragment fr = new RepresentSelectedFragment();
					Bundle args = new Bundle();
					args.putString("Klasse", Klassen[groupPosition][0]);
					fr.setArguments(args);

					FragmentManager fragmentManager = getSupportFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.content_frame, fr).commit();

					setTitle(Klassen[groupPosition][0]);
					selectedClass = Klassen[groupPosition][0];
					lDrawerLayout.closeDrawer(rDrawerList);
				}
				return false;
			}
		});

		rDrawerList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP
						&& ExpandableListView.getPackedPositionGroup(id) < 5) {

					SherlockFragment fr = new RepresentSelectedFragment();
					Bundle args = new Bundle();
					args.putString("Klasse", Klassen[ExpandableListView
							.getPackedPositionGroup(id)][0]);
					fr.setArguments(args);

					FragmentManager fragmentManager = getSupportFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.content_frame, fr).commit();

					setTitle(Klassen[ExpandableListView
							.getPackedPositionGroup(id)][0]);
					selectedClass = Klassen[ExpandableListView
							.getPackedPositionGroup(id)][0];
					lDrawerLayout.closeDrawer(rDrawerList);
				}

				return false;
			}

		});

		// check intent
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);

			SherlockFragment fr = new RepresentSearchedFragment();
			Bundle args = new Bundle();
			args.putString("Suche", query);
			fr.setArguments(args);

			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fr)
					.commit();

			setTitle("Suche: " + query);
		} else {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			selectItem(Integer.parseInt(sharedPreferences.getString(
					"pref_start_view", "0")));
		}
		
		
	}

	// detect search intents delivered by singleTop
	@Override
	protected void onNewIntent(Intent intent) {

		if (intent.getAction().equals("android.intent.action.SEARCH")) {
			String query = intent.getStringExtra(SearchManager.QUERY);

			SherlockFragment fr = new RepresentSearchedFragment();
			Bundle args = new Bundle();
			args.putString("Suche", query);
			if (!selectedClass.equals("")) {
				args.putString("Klasse", selectedClass);
			}
			fr.setArguments(args);

			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fr)
					.commit();

			setTitle("Suche: " + query);
		} else if (intent.getAction().equals(
				"android.intent.action.NOTIFICATION")) {
			selectItem(1);
		} else {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			selectItem(Integer.parseInt(sharedPreferences.getString(
					"pref_start_view", "0")));
		}

		super.onNewIntent(intent);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		lDrawerLayout.isDrawerOpen(drawerView);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		switch (item.getItemId()) {
		case R.id.action_search:
			onSearchRequested();
			return true;
		case R.id.action_refresh:
			update.interrupt();
			return true;
		case R.id.action_share:
			return false;

		}
		Log.e("OnOptionsItemCalled", (String) item.getTitle());
		// Close Right Drawer when left drawer is toggled
		if (lDrawerToggle.onOptionsItemSelected(getMenuItem(item))) {
			if(lDrawerLayout.isDrawerVisible(rDrawerList) != false) {
				lDrawerLayout.closeDrawer(rDrawerList);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);

		}
	}

	// select Item from sidenavigation
	private void selectItem(int position) {
		Log.e("data", "item" + position);
		FragmentManager fragmentManager;
		SherlockFragment fr;
		if (position == selectedItem) {
			lDrawerLayout.closeDrawer(this.drawerView);
			return;
		}

		switch (position) {
		case 0:
			fr = new RepresentAllFragment();
			// fr = new PageSlidingTabStripFragment();
			fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fr)
					.commit();
			// update selected item and title, then close the drawer
			lDrawerList.setItemChecked(position, true);
			setTitle("Vertretungen");
			lDrawerLayout.closeDrawer(this.drawerView);
			selectedItem = position;
			selectedClass = "";
			Log.e("debugging", "case 0 - ende");
			break;

		case 1:

			SharedPreferences sharedPref = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			if (sharedPref.getString("pref_Klasse", null) != null) {
				selectedClass = sharedPref.getString("pref_Klasse", null);
				fr = new RepresentSelectedFragment();
				Bundle args = new Bundle();
				fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fr).commit();
				args.putString("Klasse", selectedClass);
				fr.setArguments(args);
			} else {
				selectItem(2);

				Toast.makeText(this, "Du hast noch keine Klasse eingestellt.",
						Toast.LENGTH_LONG).show();
			}

			// update selected item and title, then close the drawer
			lDrawerList.setItemChecked(position, true);
			setTitle("Vertretungen");
			lDrawerLayout.closeDrawer(this.drawerView);
			selectedItem = position;
			break;

		case 2:


			Intent settings = new Intent(this.getApplicationContext(),
					SettingsActivity.class);
			this.startActivity(settings);

			// update selected item and title, then close the drawer
			lDrawerList.setItemChecked(position, false);
			lDrawerLayout.closeDrawer(this.drawerView);
			selectedClass = "";
			break;

		case 3:

			this.finish();
			Intent intent_splashScreen = new Intent(
					this.getApplicationContext(), SplashScreen.class);
			intent_splashScreen.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			this.overridePendingTransition(0, 0);
			this.startActivity(intent_splashScreen);
			break;

		default:
		}

	}

	@Override
	public void setTitle(CharSequence title) {
		getActionBar().setTitle(title);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		lDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		lDrawerToggle.onConfigurationChanged(newConfig);
	}

	public static int getSelectedItem() {
		return selectedItem;
	}

	public static String getSelectedClass() {
		return selectedClass;
	}

	public static ExpandableListView getExpListView() {
		return rDrawerList;
	}

	public static ExpandableListAdapter getExpandableListAdapter() {
		return expListAdapter;
	}

	public void setGroupData() {
		// Groups
		for (int i = 0; i < 8; i++)
			groupItem.add(Klassen[i][0]);

	}

	public void setSubData() {
		// SubData
		ArrayList<String> child = new ArrayList<String>();
		// 5
		for (int i = 1; i < 4; i++) {
			child.add(Klassen[0][i]);
		}
		childItem.add(child);
		// 6
		child = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			child.add(Klassen[1][i]);
		}
		childItem.add(child);
		// 7
		child = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			child.add(Klassen[2][i]);
		}
		childItem.add(child);
		// 8
		child = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			child.add(Klassen[3][i]);
		}
		childItem.add(child);
		// 9
		child = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			child.add(Klassen[4][i]);
		}
		childItem.add(child);
	}

	// this method is used because DrawerToggle doesn't support the sherlock
	// MenuItem
	private android.view.MenuItem getMenuItem(final MenuItem item) {
		return new android.view.MenuItem() {
			@Override
			public int getItemId() {
				return item.getItemId();
			}

			public boolean isEnabled() {
				return true;
			}

			@Override
			public boolean collapseActionView() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean expandActionView() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public ActionProvider getActionProvider() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public View getActionView() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public char getAlphabeticShortcut() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getGroupId() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Drawable getIcon() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Intent getIntent() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ContextMenuInfo getMenuInfo() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public char getNumericShortcut() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getOrder() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public SubMenu getSubMenu() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CharSequence getTitle() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CharSequence getTitleCondensed() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean hasSubMenu() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isActionViewExpanded() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isCheckable() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isChecked() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isVisible() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public android.view.MenuItem setActionProvider(
					ActionProvider actionProvider) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setActionView(View view) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setActionView(int resId) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setAlphabeticShortcut(char alphaChar) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setCheckable(boolean checkable) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setChecked(boolean checked) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setEnabled(boolean enabled) {
				// TODO Auto-generateditle("Suche: " + query);
				return null;
			}

			@Override
			public android.view.MenuItem setIcon(Drawable icon) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setIcon(int iconRes) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setIntent(Intent intent) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setNumericShortcut(char numericChar) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setOnActionExpandListener(
					OnActionExpandListener listener) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setOnMenuItemClickListener(
					OnMenuItemClickListener menuItemClickListener) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setShortcut(char numericChar,
					char alphaChar) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setShowAsAction(int actionEnum) {
				// TODO Auto-generated method stub

			}

			@Override
			public android.view.MenuItem setShowAsActionFlags(int actionEnum) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setTitle(CharSequence title) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setTitle(int title) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setTitleCondensed(CharSequence title) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setVisible(boolean visible) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	// thread for data update
	Thread update = new Thread(new Runnable() {

		@Override
		public void run() {

			ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo mWifi = connManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			while (true) {

				SharedPreferences sharedPref = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
				if (mWifi.isConnected()
						|| !sharedPref.getBoolean("pref_wlan_only", false)) {
					new SQLRequest(con).execute();
				}

				try {
					Thread.sleep(Integer.parseInt(sharedPref.getString(
							"pref_update_period", "60")) * 60 * 1000);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					Log.e("Update", "aktualisiere Daten...");
				}
			}
		}
	});

}
