package ca.shaneforster.warfaresquare;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AttackDialogActivity extends Activity implements OnClickListener, OnItemSelectedListener {
	
	private String venue_id;
	private int attackWith;
	private Button btnAttack, btnCancel;
	private TextView tv_venue, tv_info;
	private Spinner spin_soldiers;
	private Venue v;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		v = MainActivity.venues.get(intent.getStringExtra(MainActivity.VENUE_ID));
		
		setContentView(R.layout.activity_attack_dialog);
		
		btnAttack = (Button) findViewById(R.id.attackButton);
		btnCancel = (Button) findViewById(R.id.cancelButton);
		tv_venue = (TextView) findViewById(R.id.venueName);
		tv_info = (TextView) findViewById(R.id.venue_info);
		spin_soldiers = (Spinner) findViewById(R.id.soldierSelectorSpinner);
		
		attackWith = MainActivity.user.getSoldiers();
		spin_soldiers.setOnItemSelectedListener(this);
		
		
		List<String> soldierList = new ArrayList<String>(11);
		for (int i = 0; i <= MainActivity.user.getSoldiers(); i++)
			soldierList.add(Integer.toString(i));
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, soldierList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin_soldiers.setAdapter(dataAdapter);
		spin_soldiers.setSelection(attackWith);		
		btnCancel.setOnClickListener(new OnClickListener() {
			//Close activity on cancel
			@Override
			public void onClick(View v) {
				finish();	
			}
		});
		btnAttack.setOnClickListener(this);
		
	}
	
	protected void onResume(){
		super.onResume();

		tv_venue.setText(v.getName());
		tv_info.setText(v.infoString());
		
		if (v.getMayor().getUserName().equals(MainActivity.user.getUserName())){
			//Cannot attack yourself
			btnAttack.setEnabled(false);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.attack_dialog, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		Toast.makeText(this, "Call Server for attack. Attack venue with " + attackWith + " soldiers.", Toast.LENGTH_LONG).show();
		
		//update with success/failure
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		attackWith = Integer.parseInt((String) parent.getItemAtPosition(pos));
		if (attackWith < 1 || (v.getMayor().getUserName().equals(MainActivity.user.getUserName())))
			btnAttack.setEnabled(false);
			//Cant attack with no one
		else
			btnAttack.setEnabled(true);
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
