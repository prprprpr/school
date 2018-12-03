package data;

import java.io.BufferedReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dao.io;

public class dataMatch {
	private static String cqPassStation="D:/货车轨迹数据分析/cqAllPassStation.csv";
	private static String cqTollDataOut="D:/货车轨迹数据分析/cqTollDataOut.csv";
	
	private static String outPath16Chongqing="G:/地图/收费站数据/16PoiChongqing.csv";
	private static String outPath18Chongqing="G:/地图/收费站数据/18PoiChongqing.csv";
	private static String cqStationName="G:/新建文件夹/重庆/重庆站点表.csv";
	
	public static void readCqPassStation(String cqPassStation,Map<String,String> mapTraceIdGpsTrace,Map<String,ArrayList<String>> mapTraceStationAndTime){
		Map<String,String> mapGpsStationId=map.matchGpsToStationId(outPath18Chongqing,outPath16Chongqing,cqStationName);
		try{
			BufferedReader reader=io.getReader(cqPassStation, "gbk");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(":",2);
				String id=data[0];
				String squareTrace=data[1];
				mapTraceIdGpsTrace.put(id, squareTrace);
				
				String[] squares=squareTrace.split("\\|");
				
				if(squares.length>1){
					int length=squares.length;
					int i=0;
					while(i+1<length){
						String square1=squares[i];
						String square2=squares[i+1];
						String station1Gps=square1.split(",",3)[0]+";"+square1.split(",",3)[1];
						String station2Gps=square2.split(",",3)[0]+";"+square2.split(",",3)[1];
						String station1=station1Gps;
						String station2=station2Gps;
						if(mapGpsStationId.containsKey(station1Gps)){
							station1=mapGpsStationId.get(station1Gps).split(";",2)[1];
						}
						if(mapGpsStationId.containsKey(station2Gps)){
							station2=mapGpsStationId.get(station2Gps).split(";",2)[1];
						}
						String time1=square1.split(",",3)[2];
						String time2=square2.split(",",3)[2];
						if(!station1.equals(station2)){
							if(mapTraceStationAndTime.containsKey(station1+","+station2)){
								ArrayList<String> list=mapTraceStationAndTime.get(station1+","+station2);
								list.add(time1+","+time2+","+id);
								mapTraceStationAndTime.put(station1+","+station2, list);
							}else{
								ArrayList<String> list=new ArrayList<>();
								list.add(time1+","+time2+","+id);
								mapTraceStationAndTime.put(station1+","+station2, list);
							}
							i+=2;
						}else{
							i+=1;
						}
					}
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static boolean isMatch(String inTime,String outTime,String inGpsTime,String outGpsTime) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long inTimeLong=sdf.parse(inTime).getTime()/1000;
		long outTimeLong=sdf.parse(outTime).getTime()/1000;
		boolean in=false,out=false;
		if(inGpsTime.contains(";")){
			String time1=inGpsTime.split(";",2)[0];
			String time2=inGpsTime.split(";",2)[1];
			long time1Long=sdf.parse(time1).getTime()/1000;
			long time2Long=sdf.parse(time2).getTime()/1000;
			if(inTimeLong>=time1Long-30&&inTimeLong<=time2Long+30){
				in=true;
			}
		}else{
			long time=sdf.parse(inGpsTime).getTime()/1000;
			if(Math.abs(inTimeLong-time)<=30){
				in=true;
			}
		}
		if(outGpsTime.contains(";")){
			String time1=outGpsTime.split(";",2)[0];
			String time2=outGpsTime.split(";",2)[1];
			long time1Long=sdf.parse(time1).getTime()/1000;
			long time2Long=sdf.parse(time2).getTime()/1000;
			if(outTimeLong>=time1Long-30&&outTimeLong<=time2Long+30){
				out=true;
			}
		}else{
			long time=sdf.parse(outGpsTime).getTime()/1000;
			if(Math.abs(outTimeLong-time)<=30){
				out=true;
			}
		}
		return in&&out;
	}
	
	public static void readTollDataOnce(String inTollCollection,String cqPassStation,Map<String,String> mapTraceIdGpsTrace,Map<String,ArrayList<String>> mapTraceStationAndTime,Map<String,Map<String,Integer>> mapTraceIdCar,Set<String> matchedCar){
		try{
			BufferedReader reader=io.getReader(inTollCollection, "GBK");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",9);
				String plate=data[1];
				String cx=data[2];
				String inTime=data[3];
				String outTime=data[4];
				String inStation=data[5];
				String outStation=data[6];
				if(mapTraceStationAndTime.containsKey(inStation+","+outStation)){
					ArrayList<String> list=mapTraceStationAndTime.get(inStation+","+outStation);
					for(int i=0;i<list.size();i++){
						String gpsTime=list.get(i);
						String[] gpsData=gpsTime.split(",",3);
						String inGpsTime=gpsData[0];
						String outGpsTime=gpsData[1];
						String traceId=gpsData[2];
						boolean flag=isMatch(inTime,outTime,inGpsTime,outGpsTime);
						if(flag){
							matchedCar.add(plate+","+cx);
							if(mapTraceIdCar.containsKey(traceId)){
								Map<String,Integer> listCar=mapTraceIdCar.get(traceId);
								if(listCar.containsKey(plate+","+cx)){
									listCar.put(plate+","+cx, listCar.get(plate+","+cx)+1);
								}else{
									listCar.put(plate+","+cx,1);
								}
								mapTraceIdCar.put(traceId, listCar);
							}else{
								Map<String,Integer> listCar=new HashMap<>();
								listCar.put(plate+","+cx,1);
								mapTraceIdCar.put(traceId, listCar);
							}
//							mapTraceIdCar.put(traceId, plate+","+cx);//一个traceId多个车辆？
						}
					}
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void readTollCollectionTwice(String inTollCollection,String cqPassStation){
		Map<String,String> mapTraceIdGpsTrace=new HashMap<>();//traceId和经过收费站的轨迹数据
		Map<String,ArrayList<String>> mapTraceStationAndTime=new HashMap<>();//记录匹配上收费站id的，收费站id对和经过的时间
		readCqPassStation(cqPassStation,mapTraceIdGpsTrace,mapTraceStationAndTime);//读取上述两个map
		System.out.println("readCqPassStation finish!");
		
		Map<String,Map<String,Integer>> mapTraceIdCar=new HashMap<>();//记录traceId匹配上的一个或多个车辆
		Set<String> matchedCar=new HashSet<>();//记录匹配上的车牌号
		readTollDataOnce(inTollCollection,cqPassStation,mapTraceIdGpsTrace,mapTraceStationAndTime,mapTraceIdCar,matchedCar);
		System.out.println("readTollDataOnce finish!");
		
		for(String key:mapTraceIdCar.keySet()){
			System.out.println(key+":"+mapTraceIdCar.get(key));
		}
		System.out.println(mapTraceIdCar.size());
		
		Map<String,String> mapCarToll=new HashMap<>();
		try{
			BufferedReader reader=io.getReader(inTollCollection, "GBK");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",9);
				String plate=data[1];
				String cx=data[2];
				String inTime=data[3];
				String outTime=data[4];
				String inStation=data[5];
				String outStation=data[6];
				if(matchedCar.contains(plate+","+cx)){
					if(mapCarToll.containsKey(plate+","+cx)){
						String value=mapCarToll.get(plate+","+cx);
						value+="|"+inStation+","+inTime+"|"+outStation+","+outTime;
						mapCarToll.put(plate+","+cx,value);
					}else{
						mapCarToll.put(plate+","+cx,inStation+","+inTime+"|"+outStation+","+outTime);
					}
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
//		for(String traceId:mapTraceIdCar.keySet()){
//			ArrayList<String> listCar=mapTraceIdCar.get(traceId);
//			String gpsTrace=mapTraceIdGpsTrace.get(traceId);
//			System.out.println(traceId+":"+gpsTrace);
//			for(int i=0;i<listCar.size();i++){
//				String car=listCar.get(i);
//				String toll=mapCarToll.get(car);
//				System.out.println(car+":"+toll);
//			}
//			System.out.println();
//		}
	}
	public static void main(String[] args){
//		readTollCollectionTwice(cqTollDataOut,cqPassStation);
		char[] c={'a','b','c'};
		c[0]='1';
		StringBuilder a=new StringBuilder("a");
		char b=a.charAt(0);
		System.out.println(	);
	}
}
