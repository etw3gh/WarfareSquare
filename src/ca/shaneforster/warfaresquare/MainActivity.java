package ca.shaneforster.warfaresquare;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MainActivity extends Activity implements OnMarkerClickListener{
	public static final String VENUE_ID = "ca.shaneforster.warfaresquare.VENUE_ID";
	public static final String USER_NAME = "ca.shaneforster.warfaresquare.USER_NAME";
	private MapFragment warMapFragment;
	private GoogleMap warMap;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] menuItems;
	
	private SharedPreferences prefs;
	protected static Mayor user;
	protected static HashMap<String, Venue> venues;
	protected static LatLng userLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		prefs = getPreferences(MODE_PRIVATE);
		
		user = new Mayor(prefs.getString(USER_NAME, "default"));
		
		if ("default".equalsIgnoreCase(user.getUserName())){
			//TODO launch login
	
		}
		
		//Demo code
		user = new Mayor("Shane", 8, R.drawable.mk_flag_t4);
		
		venues = new HashMap<String, Venue>();
		
		warMapFragment = MapFragment.newInstance();
		

		mTitle = mDrawerTitle = getTitle();
		menuItems = getResources().getStringArray(R.array.menu_item_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, menuItems));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		Venue v;
		v = new Venue("DMZ", new LatLng(43.656746, -79.380484), new Mayor("RichardLachman", 10, R.drawable.mk_flag_t1), 5);
		venues.put(v.toString(), v);
		
		v = new Venue("Oakham House", new LatLng(43.657780, -79.379088), new Mayor("Geoff Lachapelle", 9, R.drawable.mk_flag_t3), 2);
		venues.put(v.toString(), v);

		v = new Venue("Podium", new LatLng(43.6583927, -79.380994), user, 4);
		venues.put(v.toString(), v);
		

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons

		return super.onOptionsItemSelected(item);

	}

	@Override
	public void onResume() {
		super.onResume();
		
		warMap = warMapFragment.getMap();
		
		if (warMap == null)
			Toast.makeText(this, "No Map!", Toast.LENGTH_LONG).show();
		
		warMap.clear();
		//TODO get nearby venues load into venues
		
		
		for (Map.Entry<String, Venue> ven : venues.entrySet()){
			warMap.addMarker(ven.getValue().genMarkerOptions());
		}

		
		warMap.setMyLocationEnabled(true);
		warMap.setOnMarkerClickListener(this);

	    Location location=((LocationManager)getSystemService(LOCATION_SERVICE)).getLastKnownLocation(((LocationManager)getSystemService(LOCATION_SERVICE)).getBestProvider(new Criteria(), true));
	    userLocation = new LatLng(location.getLatitude(), location.getLongitude());
		
	    warMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
		
		
	}
	
	
	protected void onStop(){
		super.onStop();
		
		SharedPreferences.Editor e = prefs.edit();
		e.putString(USER_NAME, user.getUserName());
		e.commit();
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

	private void selectItem(int position) {
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = warMapFragment;
			break;
		case 1:
			Intent i = new Intent(this, CouponActivity.class);
			startActivity(i);
		default:
			Toast.makeText(this, "Not Implemented", Toast.LENGTH_LONG).show();
			fragment = new Fragment();

			break;
		}

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(menuItems[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	public boolean onMarkerClick(Marker m){
		Intent intent = new Intent(this, AttackDialogActivity.class);
		intent.putExtra(VENUE_ID, m.getSnippet());
		startActivity(intent);
		return true;
	}
	


}
