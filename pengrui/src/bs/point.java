package bs;

public class point {
	private float x;
	private float y;
	private String time;
	public boolean isVisit;
	private int cluster;
	private boolean isNoised;
	private static final double EARTH_RADIUS = 6378137;  
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public point(float x,float y,String time){
		this.x=x;
		this.y=y;
		this.time=time;
		this.isVisit=false;
		this.cluster=0;
		this.isNoised=false;
	}
	private float rad(float d){  
	    return (float) (d * Math.PI / 180.0);  
	}  
	public float getDistance(point p){
		float lng1=x;
		float lat1=y;
		float lng2=p.x;
		float lat2=p.y;
		float radLat1 = rad(lat1);  
		float radLat2 = rad(lat2);  
		float a = radLat1 - radLat2;  
		float b = rad(lng1) - rad(lng2);  
		float s = (float) (2 * Math.asin(  
	        Math.sqrt(  
	            Math.pow(Math.sin(a/2),2)   
	            + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)  
	        )  
        ));  
        s = (float) (s * EARTH_RADIUS);  
        s = Math.round(s * 10000) / 10000;  
        return s;  
	}
	public boolean isVisit() {
		return isVisit;
	}
	public void setVisit(boolean isVisit) {
		this.isVisit = isVisit;
	}
	public int getCluster() {
		return cluster;
	}
	public void setCluster(int cluster) {
		this.cluster = cluster;
	}
	public boolean isNoised() {
		return isNoised;
	}
	public void setNoised(boolean isNoised) {
		this.isNoised = isNoised;
	}
	public String toString(){
		return x+","+y+","+cluster+","+(isNoised?1:0)+","+time;
	}
}
