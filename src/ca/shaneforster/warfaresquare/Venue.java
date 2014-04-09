package ca.shaneforster.warfaresquare;

import android.util.JsonReader;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Venue {
	
	private LatLng location;
	private String name;
	private Mayor mayor;
	private int defence;
	private String id;



	public Venue() {
		this.location = new LatLng(0.0, 0.0);
		this.name = "undefined";
		this.mayor = new Mayor();
		this.defence = 0;
		this.id = null;
	}
	
	public Venue(String name, LatLng location, Mayor mayor, int defence){
		this.location = location;
		this.name = name;
		this.mayor = mayor;
		this.defence = defence;
		this.id = null;
	}
	public Venue(JsonReader reader){
		this.defence = 0;
		this.name = "undefined";
		this.mayor = new Mayor();
		
		double lat = 0.0;
		double lng = 0.0;
		try {
			reader.beginObject();
			reader.skipValue();
			reader.beginObject();
			while (reader.hasNext()){
				String name = reader.nextName();
				if (name.equals("id"))
					this.id = reader.nextString();
				else if (name.equals("name"))
					this.name = reader.nextString();
				else if (name.equals("lat"))
					lat = reader.nextDouble();
				else if (name.equals("lng"))
					lng = reader.nextDouble();
				else if (name.equals("mayor"))
					this.mayor = new Mayor(reader.nextString());
				else if (name.equals("defence"))
					this.defence = reader.nextInt();
				else
					reader.skipValue();	
			}
			reader.endObject();
			reader.endObject();
			
			this.location = new LatLng(lat, lng);
		} catch (Exception e){
			this.location = new LatLng(0.0, 0.0);
			this.name = "undefined";
			this.mayor = new Mayor();
			this.defence = 0;
			this.id = null;
		}
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
