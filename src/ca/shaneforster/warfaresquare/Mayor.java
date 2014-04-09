package ca.shaneforster.warfaresquare;

public class Mayor {
	private String userName;
	private int soldiers;
	private int flagId;
	
	public Mayor(){
		this.userName = "Unclaimed";
		this.soldiers = 0;
		this.flagId = R.drawable.mk_flag_unclaimed;
	}
	
	public Mayor(String name){
		this.userName = name;
		this.soldiers = -1;
		this.flagId = R.drawable.mk_flag_unclaimed;
	}
	
	public Mayor(String name, int soldiers, int flagId){
		this.userName = name;
		this.soldiers = soldiers;
		this.flagId = flagId;
		
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getSoldiers() {
		return soldiers;
	}
	public void setSoldiers(int soldiers) {
		this.soldiers = soldiers;
	}
	public int getFlagId() {
		return flagId;
	}
	public void setFlagId(int flagId) {
		this.flagId = flagId;
	}
	
	
}
