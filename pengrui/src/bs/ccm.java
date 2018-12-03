package bs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ccm {
	private static final double EARTH_RADIUS = 6378137;  
//	private static final double bh_Lng = 116.35347;  
//	private static final double bh_Lat = 39.986069;
	
	private static float rad(float d){  
	    return (float) (d * Math.PI / 180.0);  
	}  
	public static float getDistance(point p1,point p2){
		float lng1=p1.getX();
		float lat1=p1.getY();
		float lng2=p2.getX();
		float lat2=p2.getY();
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
	
	 public static point MillierConvertion(point p)  
	    {  
		 float L = (float) (6381372 * Math.PI * 2);//地球周长  
		 float W=L;// 平面展开后，x轴等于周长  
		 float H=L/2;// y轴约等于周长一半  
		 float mill=2.3f;// 米勒投影中的一个常数，范围大约在正负2.3之间  
		 float x = (float) (p.getX() * Math.PI / 180);// 将经度从度数转换为弧度  
		 float y = (float) (p.getY() * Math.PI / 180);// 将纬度从度数转换为弧度  
	         y=(float) (1.25 * Math.log( Math.tan( 0.25 * Math.PI + 0.4 * y ) ));// 米勒投影的转换  
	         // 弧度转为实际距离  
	         x = (float) (( W / 2 ) + ( W / (2 * Math.PI) ) * x);  
	         y = ( H / 2 ) - ( H / ( 2 * mill ) ) * y;  
	         point result=new point(x,y,p.getTime());  
	         return result;  
	    }  
	
	public ArrayList<point> getKeySet(ArrayList<point> points){
		ArrayList<point> keyList=new ArrayList<point>();
		keyList.add(points.get(0));
		ArrayList<point> neighborPoints=new ArrayList<point>();
		for(int i=1;i<points.size()-1;i++){
			neighborPoints.add(MillierConvertion(points.get(i-1)));
			neighborPoints.add(MillierConvertion(points.get(i)));
			neighborPoints.add(MillierConvertion(points.get(i+1)));
//			neighborPoints.add(points.get(i-1));
//			neighborPoints.add(points.get(i));
//			neighborPoints.add(points.get(i+1));
			pearsonCorrelationScore score = new pearsonCorrelationScore(neighborPoints);
//			Double b=(double)score.getPearsonCorrelationScore();
			if(Math.abs(score.getPearsonCorrelationScore())<0.55){
				keyList.add(points.get(i));
			}
			neighborPoints.clear();
		}
		keyList.add(points.get(points.size()-1));
		return keyList;
	}
	
	public ArrayList<ArrayList<point>> splitKeySet(ArrayList<point> keySet,float d){
		ArrayList<ArrayList<point>> splitKeySet=new ArrayList<ArrayList<point>>();
		ArrayList<point> subKeySet=new ArrayList<point>();
		for(int i=0;i<keySet.size()-1;i++){
			if(getDistance(keySet.get(i),keySet.get(i+1))<=d){
				subKeySet.add(keySet.get(i));
			}else{
				subKeySet.add(keySet.get(i));
				splitKeySet.add(subKeySet);
				subKeySet=new ArrayList<point>();
			}
		}
		return splitKeySet;
	}
	
	public ArrayList<point> getNeighbors(ArrayList<point> trace,ArrayList<point> keySet){
		ArrayList<point> rs=new ArrayList<point>();
		for(int i=0;i<trace.size();i++){
			if(keySet.contains(trace.get(i))){
				if(i-1>=0&&!keySet.contains(trace.get(i-1))&&!rs.contains(trace.get(i-1))){
					rs.add(trace.get(i-1));
				}
				if(i+1<=trace.size()-1&&!keySet.contains(trace.get(i+1))&&!rs.contains(trace.get(i+1))){
					rs.add(trace.get(i+1));
				}
			}
		}
		return rs;
	}
	
	public ArrayList<point> seekExt(ArrayList<point> trace,ArrayList<point> keySet){
		ArrayList<point> rs=new ArrayList<point>();
		circle c=new circle();
		c.setPoints(keySet);
		c.min_cover_circle();
		float r=c.getR();
//		System.out.println("banjing:"+r);
		point central=c.getCentral();
		ArrayList<point> neighbor=getNeighbors(trace,keySet);
		ccm a=new ccm();
		for(int i=0;i<trace.size();i++){
			if(keySet.contains(trace.get(i))){
				rs.add(trace.get(i));
			}else if(neighbor.contains(trace.get(i))){
				if(getDistance(central,trace.get(i))<=r){
					rs.add(trace.get(i));
				}else if(i-1>=0&&i+1<=trace.size()-1){
					ArrayList<point> neighborPoints=new ArrayList<point>();
					neighborPoints.add(MillierConvertion(trace.get(i-1)));
					neighborPoints.add(MillierConvertion(trace.get(i)));
					neighborPoints.add(MillierConvertion(trace.get(i+1)));
//					neighborPoints.add(trace.get(i-1));
//					neighborPoints.add(trace.get(i));
//					neighborPoints.add(trace.get(i+1));
					pearsonCorrelationScore score = new pearsonCorrelationScore(neighborPoints);
					if(score.getPearsonCorrelationScore()<0.55){
						rs.add(trace.get(i));
					}
				}
			}
		}
		return rs;
	}
	
	public float getLength(ArrayList<point> trace){
		float rs=0;
		for(int i=0;i<trace.size()-1;i++){
			rs+=getDistance(trace.get(i),trace.get(i+1));
		}
		return rs;
	}
	
	public long getTraceTime(ArrayList<point> trace) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		point a=trace.get(0);
		point b=trace.get(trace.size()-1);
		long da=sdf.parse(a.getTime()).getTime();
		long db=sdf.parse(b.getTime()).getTime();
		return (db-da)/1000;
	}
	public ArrayList<point> judgeStop(ArrayList<point> trace,ArrayList<point> keySet) throws ParseException{
		ArrayList<point> subTrace=seekExt(trace,keySet);
		circle c=new circle();
		c.setPoints(subTrace);
		c.min_cover_circle();
		float r=c.getR();
		point central=c.getCentral();
		float len=getLength(subTrace);
		if(len<5.5*r||getTraceTime(subTrace)<150){
			return null;
		}else{
			return subTrace;
		}
	}
	
	public ArrayList<ArrayList<point>> getStops(ArrayList<point> trace) throws ParseException{
		ArrayList<ArrayList<point>> stopSet=new ArrayList<ArrayList<point>>();
		ccm c=new ccm();
		ArrayList<point> keyList=c.getKeySet(trace);
		ArrayList<point> keySet=c.getKeySet(keyList);
		ArrayList<ArrayList<point>> splitKeySet=c.splitKeySet(keySet, 75);
		for(int i=0;i<splitKeySet.size();i++){
			ArrayList<point> arr=splitKeySet.get(i);
//			if(arr.size()>3){
//				System.out.println("arr:"+arr.size());
//				ArrayList<point> stop=judgeStop(trace,arr);
//				if(stop!=null){
//					stopSet.add(stop);
//				}
//			}
			ArrayList<point> stop=judgeStop(trace,arr);
			if(stop!=null){
				stopSet.add(stop);
			}
		}
		return stopSet;
	}
	
	public void writePoints(ArrayList<point> points,String path){
		try{
			BufferedWriter writer=readCCM.getWriter(path,"GBK");
			for(int i=0;i<points.size();i++){
				writer.write(points.get(i).toString()+"\n");
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws ParseException{
		readCCM read=new readCCM();
		String path="F:/货车轨迹数据/单条轨迹/trace2.csv";
		ArrayList<point> tracePoints=read.readTrace(path);
		ccm c=new ccm();
		ArrayList<ArrayList<point>> stops=c.getStops(tracePoints);
		System.out.println("停留点数："+stops.size());
		String outDir="F:/货车轨迹数据/测试结果/"+path.split("\\/")[3].split("\\.")[0]+"的停留点";
		File file=new File(outDir);
		if(!file.exists()){
			file.mkdirs();
		}
		for(int i=0;i<stops.size();i++){
			ArrayList<point> arr=stops.get(i);
			c.writePoints(arr, outDir+"/stop"+i+".csv");
			System.out.println("停留点"+i+":"+arr.size());
			for(int j=0;j<arr.size();j++){
				System.out.println(arr.get(j).toString());
			}
		}
	}
}
