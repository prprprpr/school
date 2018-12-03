package dao;

import java.util.ArrayList;
//暂时没用到
public class Trace {
	private String inStation;
	private String inTime;
	private String outStation;
	private String outTime;
	private int weight;
	private String serviceArea;
	private String passServiceAreaTime;
	public Trace(String inStation,String inTime,String outStation,String outTime,int weight){
		this.inStation=inStation;
		this.inTime=inTime;
		this.outStation=outStation;
		this.outTime=outTime;
		this.weight=weight;
		this.serviceArea=null;
		this.passServiceAreaTime=null;
	}
	public Trace(String inStation,String inTime,String outStation,String outTime,int weight,String serviceArea,String passServiceAreaTime){
		this.inStation=inStation;
		this.inTime=inTime;
		this.outStation=outStation;
		this.outTime=outTime;
		this.weight=weight;
		this.serviceArea=serviceArea;
		this.passServiceAreaTime=passServiceAreaTime;
	}
	public String getInStation() {
		return inStation;
	}
	public void setInStation(String inStation) {
		this.inStation = inStation;
	}
	public String getInTime() {
		return inTime;
	}
	public void setInTime(String inTime) {
		this.inTime = inTime;
	}
	public String getOutStation() {
		return outStation;
	}
	public void setOutStation(String outStation) {
		this.outStation = outStation;
	}
	public String getOutTime() {
		return outTime;
	}
	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public String getServiceArea() {
		return serviceArea;
	}
	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}
	public String getPassServiceAreaTime() {
		return passServiceAreaTime;
	}
	public void setPassServiceAreaTime(String passServiceAreaTime) {
		this.passServiceAreaTime = passServiceAreaTime;
	}
	
	public boolean isPassServiceArea(){
		return this.serviceArea!=null;
	}
}
