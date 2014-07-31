package edu.illinois.seclab.extDroid.passAttack;

public class ExtDevice {
	private String pack_name;
	private String MAC_address;
	private int connection_status;
	private String MAC_address_start;
	
	/**
	 * 
	 * @param pack_name
	 * @param MAC_address_start
	 * @param status
	 * 
	 * @author soteris demetriou
	 */
	public ExtDevice(String pack_name, String MAC_address_start, int status){
		this.pack_name = pack_name;
		this.MAC_address_start = MAC_address_start;
		this.connection_status = connection_status;
	}
	
	public void setPackName(String pack_name){
		this.pack_name = pack_name;
	}
	
	public String getPackName(){
		return this.pack_name;
	}
	
	public void setMACAddressStart(String MAC_address_start){
		this.MAC_address_start = MAC_address_start;
	}
	
	public String getMACAddressStart(){
		return this.MAC_address_start;
	}
	
	public void setMACAddress(String MAC_address){
		this.MAC_address = MAC_address;
	}
	
	public String getMACAddress(){
		return this.MAC_address;
	}
	
	public void setConStatus(int connection_status){
		this.connection_status = connection_status;
	}
	
	public int getConStatus(){
		return this.connection_status;
	}

}
