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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AttackDialogActivity extends Activity implements OnClickListener, OnItemSelectedListener {
	
	private String venue_id;
	private int attackWith;
	private Button btnAttack, btnCancel;
	private TextView tv_venue, tv_info, soldierPrompt;
	private RadioGroup actionGroup;
	
	private Spinner spin_soldiers;
	private Venue v;
	private boolean pickup;
	protected Boolean attack;
	
	private List<String> soldierList;
	private ArrayAdapter<String> dataAdapter;

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
		soldierPrompt = (TextView) findViewById(R.id.textView1);
		spin_soldiers = (Spinner) findViewById(R.id.soldierSelectorSpinner);
		actionGroup = (RadioGroup) findViewById(R.id.actionGroup);
		actionGroup.setVisibility(View.INVISIBLE);
		attackWith = MainActivity.user.getSoldiers();
		spin_soldiers.setOnItemSelectedListener(this);
		
		
		soldierList = new ArrayList<String>(11);
		for (int i = 0; i <= MainActivity.user.getSoldiers(); i++)
			soldierList.add(Integer.toString(i));
		dataAdapter = new ArrayAdapter<String>(this,
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
		soldierPrompt.setText("Number of soldiers to pickup:");
		btnAttack.setText("Pickup!");
		btnAttack.setOnClickListener(this);
		
	}
	
	protected void onResume(){
		super.onResume();

		tv_venue.setText(v.getName());
		tv_info.setText(v.infoString());
		
		if (v.getMayor().getUserName().equals(MainActivity.user.getUserName())){
			actionGroup.setVisibility(View.VISIBLE);
			actionGroup.check(1);
			pickup = true;

			btnAttack.setEnabled(true);
			soldierList.clear();
			for (int i = 0; i <= v.getDefence(); i++)
				soldierList.add(Integer.toString(i));
			dataAdapter.notifyDataSetChanged();
			spin_soldiers.setSelection(0);
		} else if ("Unclaimed".equals(v.getMayor().getUserName())){
			actionGroup.setVisibility(View.INVISIBLE);
			soldierPrompt.setText("Number of soldiers to attack with:");
			btnAttack.setEnabled(true);
			btnAttack.setText("Claim!");
		} else {
			actionGroup.setVisibility(View.INVISIBLE);
			soldierPrompt.setText("Number of soldiers to attack with:");
			btnAttack.setEnabled(true);
			btnAttack.setText("Attack!");
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
		
		if (v.getMayor().getUserName().equals(MainActivity.user.getUserName())){
			if (pickup){
				v.setDefence(v.getDefence() - attackWith);
				MainActivity.user.setSoldiers(MainActivity.user.getSoldiers() + attackWith);
			} else {
				v.setDefence(v.getDefence() + attackWith);
				MainActivity.user.setSoldiers(MainActivity.user.getSoldiers() - attackWith);
			}
		} else {
			MainActivity.user.setSoldiers(MainActivity.user.getSoldiers() - attackWith);
			
			if (Math.random() >= 0.5 || "Unclaimed".equals(v.getMayor().getUserName())){
				Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
				v.setMayor(MainActivity.user);
				v.setDefence(attackWith);
				
				
			} else {
				Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show();
			}
		}
		
		//update with success/failure
		new UpdateMap().execute();

		finish();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		attackWith = Integer.parseInt((String) parent.getItemAtPosition(pos));
		if (attackWith < 1 )
			btnAttack.setEnabled(false);
			//Cant attack/pickup with no one
		else if (v.getMayor().getUserName().equals(MainActivity.user.getUserName())){
			btnAttack.setEnabled(true);
			btnAttack.setText("Pickup!");
		} else if ("Unclaimed".equals(v.getMayor().getUserName())){
			btnAttack.setEnabled(true);
			btnAttack.setText("Claim!");
		}
		else {
			btnAttack.setEnabled(true);
			btnAttack.setText("Attack");
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_reinforce:
	            if (checked){
	            	soldierPrompt.setText("Number of soldiers to leave:");
	    			btnAttack.setText("Reinforce!");
	    			soldierList.clear();
	    			for (int i = 0; i <= MainActivity.user.getSoldiers(); i++)
	    				soldierList.add(Integer.toString(i));
	    			dataAdapter.notifyDataSetChanged();
	    			pickup = false;
	            }   
	            break;
	        case R.id.radio_ninjas:
	            if (checked){
	            	soldierPrompt.setText("Number of soldiers to pickup:");
	    			btnAttack.setText("Pickup!");
	    			soldierList.clear();
	    			for (int i = 0; i <= v.getDefence(); i++)
	    				soldierList.add(Integer.toString(i));
	    			dataAdapter.notifyDataSetChanged();
	    			pickup = true;
	            }
	            break;
	    }
	}
}
