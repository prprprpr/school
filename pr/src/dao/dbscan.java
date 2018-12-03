package dao;

import java.util.ArrayList;

import dao.point;

public class dbscan {
	private float radius;
	private int minPts;
	
	public dbscan(float radius,int minPts){
		this.radius=radius;
		this.minPts=minPts;
	}
	
	public void process(ArrayList<point> points){
		int size=points.size();
		int idx=0;
		int cluster=1;
		while(idx<size){
			point p=points.get(idx++);
			if(!p.isVisit){
				p.setVisit(true);
				ArrayList<point> adjacentPoints=getadjacentPoints(p,points);
				if(adjacentPoints!=null&&adjacentPoints.size()<minPts){
					p.setNoised(true);
				}else{
					p.setCluster(cluster);
					for(int i=0;i<adjacentPoints.size();i++){
						point adjacentPoint=adjacentPoints.get(i);
						if(!adjacentPoint.isVisit){
							adjacentPoint.setVisit(true);
							ArrayList<point> adjacentAdjacentPoints=getadjacentPoints(adjacentPoint, points);
							if(adjacentAdjacentPoints!=null&&adjacentAdjacentPoints.size()>=minPts){
								adjacentPoints.addAll(adjacentAdjacentPoints);
							}
						}
						if(adjacentPoint.getCluster()==0){
							adjacentPoint.setCluster(cluster);
							if(adjacentPoint.isNoised()){
								adjacentPoint.setNoised(false);
							}
						}
					}
					cluster++;
				}
			}
		}
		System.out.println(cluster);
	}
	
	public ArrayList<point> getadjacentPoints(point centerPoint,ArrayList<point> points){
		ArrayList<point> adjacentPoints=new ArrayList<point>();
		for(point p:points){
			double distance=centerPoint.getDistance(p);
			if(distance<=radius){
				adjacentPoints.add(p);
			}
		}
		return adjacentPoints;
	}
	
}
