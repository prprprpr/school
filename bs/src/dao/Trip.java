package dao;

import java.util.ArrayList;

public class Trip{
	private String id;
	private ArrayList<String> startGps;
	private String serviceArea;
	private ArrayList<String> endGps;
	private int weight;//卸货为负数，装货为正数
	public Trip(String id,ArrayList<String> startGps,String serviceArea,ArrayList<String> endGps,int weight){
		this.id=id;
		this.startGps=startGps;
		this.serviceArea=serviceArea;
		this.endGps=endGps;
		this.weight=weight;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<String> getStartGps() {
		return startGps;
	}
	public void setStartGps(ArrayList<String> startGps) {
		this.startGps = startGps;
	}
	public String getServiceArea() {
		return serviceArea;
	}
	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}
	public ArrayList<String> getEndGps() {
		return endGps;
	}
	public void setEndGps(ArrayList<String> endGps) {
		this.endGps = endGps;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public String toString(){
		return id+","+startGps+","+serviceArea+","+endGps+","+weight;
	}
}
