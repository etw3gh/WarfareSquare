package ca.shaneforster.warfaresquare;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

public class UpdateMap extends AsyncTask<Void, Void, Void>{

	@Override
	protected Void doInBackground(Void... params) {
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet("http://wfs.openciti.ca/apinearby.php?method=nearby&lat=" + MainActivity.userLocation.latitude + "&lng=" + MainActivity.userLocation.longitude + "&howmany=15&restrict=true&radius=200");
	    // Debug. Ryerson location HttpGet httpGet = new HttpGet("http://wfs.openciti.ca/apinearby.php?method=nearby&lat=43.656522&lng=-79.380437&howmany=25&restrict=true&radius=200");
	    try {
	      HttpResponse response = client.execute(httpGet);
	      StatusLine statusLine = response.getStatusLine();
	      int statusCode = statusLine.getStatusCode();
	      if (statusCode == 200) {
	        HttpEntity entity = response.getEntity();
	        InputStream content = entity.getContent();
	        JsonReader reader = new JsonReader(new InputStreamReader(content));
	        MainActivity.venues.clear();
	        try{
	        	reader.beginObject();
	        	reader.skipValue();
	        	reader.skipValue();
	        	reader.skipValue();
	        	reader.beginObject();
	        	reader.skipValue();
	        	reader.beginArray();
	        	while (reader.hasNext()){
	        		Venue v = new Venue(reader);
	        		MainActivity.venues.put(v.toString(), v);
	        	}
	        	reader.endArray();
	        	reader.skipValue();
	        	reader.skipValue();
	        	reader.endObject();
	        } finally {	        
	        	reader.close();
	        }
	        
	        
	      } else {
	        Log.e(UpdateMap.class.toString(), "Failed to download file");
	      }
	    } catch (ClientProtocolException e) {
	      Log.e("JSON", e.getLocalizedMessage());
	    } catch (IOException e) {
	    	 Log.e("JSON", e.getLocalizedMessage());
	    }
	    
	    
		return null;
	}
	

	@Override
	protected void onPostExecute(Void result){
		//UpdateMap
		MainActivity.warMap.clear();
		MainActivity.venues.putAll(MainActivity.demoVenues);
		for (Map.Entry<String, Venue> ven : MainActivity.venues.entrySet()){
			MainActivity.warMap.addMarker(ven.getValue().genMarkerOptions());
		}
	}
}