package ca.shaneforster.warfaresquare;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Venue {
	
	private LatLng location;
	private String name;
	private Mayor mayor;
	private int defence;



	public Venue() {
		this.location = new LatLng(0.0, 0.0);
		this.name = "undefined";
		this.mayor = new Mayor();
		this.defence = 0;
	}
	
	public Venue(String name, LatLng location, Mayor mayor, int defence){
		this.location = location;
		this.name = name;
		this.mayor = mayor;
		this.defence = defence;
	}

	public LatLng getLocation() {
		return location;
	}

	public void setLocation(LatLng location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Mayor getMayor() {
		return mayor;
	}

	public void setMayor(Mayor mayor) {
		this.mayor = mayor;
	}
	
	public int getDefence() {
		return defence;
	}

	public void setDefence(int defence) {
		this.defence = defence;
	}
	
	public String infoString(){
		return "Mayor: " + this.mayor.getUserName() + "\nDefence: " + this.defence;
	}
	
	public MarkerOptions genMarkerOptions(){
		MarkerOptions m = new MarkerOptions();
		m.position(this.location);
		m.icon(BitmapDescriptorFactory.fromResource(mayor.getFlagId()));
		m.title(this.name);
		m.snippet(this.toString());
		return m;
	}

}
