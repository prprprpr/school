package bs;

import java.util.ArrayList;
import java.util.Collections;

public class circle {
	private float eps=(float) 1e-8;
	private point central;
	private float r;
	private ArrayList<point> points;
	
	public circle(){};
	public circle(ArrayList<point> points){
		this.points=points;
	}
	public point getCentral() {
		return central;
	}

	public void setCentral(point central) {
		this.central = central;
	}

	public float getR() {
		return r;
	}

	public void setR(float r) {
		this.r = r;
	}

	public float dis(point a,point b){
		return (float) Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY()));
	}
	
	public point circumCenter(point a,point b,point c){
		float a1=b.getX()-a.getX(),b1=b.getY()-a.getY(),c1=(a1*a1+b1*b1)/2;
		float a2=c.getX()-a.getX(),b2=c.getY()-a.getY(),c2=(a2*a2+b2*b2)/2;
		float d=a1*b2-a2*b1;
		float x=a.getX()+(c1*b2-c2*b1)/d;
		float y=a.getY()+(a1*c2-a2*c1)/d;
		point rs=new point(x,y,"r");
		return rs;
	}
	
	public void min_cover_circle(){
//		Collections.shuffle(points);
		central=new point(points.get(0).getX(),points.get(0).getY(),points.get(0).getTime());
		int i,j,k;
		r=0;
		for(i=1;i<points.size();i++){
			if(dis(central,points.get(i))+eps>r){
				central.setX(points.get(i).getX());
				central.setY(points.get(i).getY());
				central.setTime(points.get(i).getTime());
				r=0;
				for(j=0;j<i;j++){
					if(dis(central,points.get(j))+eps>r){
						central.setX((points.get(i).getX()+points.get(j).getX())/2);
						central.setY((points.get(i).getY()+points.get(j).getY())/2);
						r=dis(central,points.get(j));
						for(k=0;k<j;k++){
							if(dis(central,points.get(k))+eps>r){
								central=circumCenter(points.get(i),points.get(j),points.get(k));
								r=dis(central,points.get(k));
							}
						}
					}
				}
			}
		}
	}

	public ArrayList<point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<point> points) {
		this.points = points;
	}
}
