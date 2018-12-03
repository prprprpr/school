package dao;

public class DataPoint {
    private String name; // ��������
    private int index;
    private double lat; // �������ά��
    private double lng;
    private double coreDistance; //���ľ��룬����õ㲻�Ǻ��Ķ��������Ϊ-1
    private double reachableDistance; //�ɴ����

    public DataPoint(){
    }

    public DataPoint(DataPoint e){
        this.name=e.name;
        this.index=e.index;
        this.lat=e.lat;
        this.lng=e.lng;
        this.coreDistance=e.coreDistance;
        this.reachableDistance=e.reachableDistance;
    }

    
    public DataPoint(double lat,double lng,String name){
        this.name=name;
        this.index=-1;
        this.lat=lat;
        this.lng=lng;
        this.coreDistance=-1;
        this.reachableDistance=-1;
    }
    public DataPoint(double lat,double lng,int index,String name){
        this.name=name;
        this.index=index;
        this.lat=lat;
        this.lng=lng;
        this.coreDistance=-1;
        this.reachableDistance=-1;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getCoreDistance() {
        return coreDistance;
    }
    public void setCoreDistance(double coreDistance) {
        this.coreDistance = coreDistance;
    }
    public double getReachableDistance() {
        return reachableDistance;
    }
    public void setReachableDistance(double reachableDistance) {
        this.reachableDistance = reachableDistance;
    }
}
