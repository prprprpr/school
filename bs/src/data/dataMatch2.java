package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import dao.io;

public class dataMatch2 {
	private static String cqPassStation="D:/货车轨迹数据分析/cqPassStation.csv";
	private static String cqTollDataOut="D:/货车轨迹数据分析/cqTollDataOut.csv";
	
	private static String outPath16Chongqing="G:/地图/收费站数据/16PoiChongqing.csv";
	private static String outPath18Chongqing="G:/地图/收费站数据/18PoiChongqing.csv";
	private static String cqStationName="G:/新建文件夹/重庆/重庆站点表.csv";
	private static String idToPlate="D:/货车轨迹数据分析/idToPlate.csv";
	
	public static void getStationIdTimeRange(String cqPassStation,Map<String,LinkedList<String>> mapStationId,Map<String,Map<String,LinkedList<String>>> mapIdTimeRange){
		Map<String,String> mapGpsStationId=map.matchGpsToStationId(outPath18Chongqing,outPath16Chongqing,cqStationName);
		try{
			BufferedReader reader=io.getReader(cqPassStation, "gbk");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(":",2);
				String id=data[0];
				String squareTrace=data[1];
				String[] squares=squareTrace.split("\\|");
				for(int i=0;i<squares.length;i++){
					String square=squares[i];
					String squareGps=square.split(",",3)[0]+";"+square.split(",",3)[1];
					String time=square.split(",",3)[2];
					if(mapGpsStationId.containsKey(squareGps)){
						String stationId=mapGpsStationId.get(squareGps).split(";",2)[1];
						if(mapStationId.containsKey(stationId)){
							LinkedList<String> listId=mapStationId.get(stationId);
							listId.add(id);
							mapStationId.put(stationId, listId);
						}else{
							LinkedList<String> listId=new LinkedList<>();
							listId.add(id);
							mapStationId.put(stationId, listId);
						}
						
						if(mapIdTimeRange.containsKey(id)){
							Map<String,LinkedList<String>> MapTimeRange=mapIdTimeRange.get(id);
							String day=time.substring(0, 10);
							if(MapTimeRange.containsKey(day)){
								LinkedList<String> listTime=MapTimeRange.get(day);
								listTime.add(time);
								MapTimeRange.put(day, listTime);
							}else{
								LinkedList<String> listTime=new LinkedList<>();
								listTime.add(time);
								MapTimeRange.put(day, listTime);
							}
							mapIdTimeRange.put(id, MapTimeRange);
						}else{
							Map<String,LinkedList<String>> MapTimeRange=new HashMap<>();
							String day=time.substring(0, 10);
							LinkedList<String> listTime=new LinkedList<>();
							listTime.add(time);
							MapTimeRange.put(day, listTime);
							mapIdTimeRange.put(id, MapTimeRange);
						}
//						if(mapStationIdTimeRange.containsKey(stationId)){
//							Map<String,ArrayList<String>> mapIdTimeRange=mapStationIdTimeRange.get(stationId);
//							if(mapIdTimeRange.containsKey(id)){
//								ArrayList<String> listTimeRange=mapIdTimeRange.get(id);
//								listTimeRange.add(time);
//								mapIdTimeRange.put(id, listTimeRange);
//							}else{
//								ArrayList<String> listTimeRange=new ArrayList<>();
//								listTimeRange.add(time);
//								mapIdTimeRange.put(id, listTimeRange);
//							}
//							mapStationIdTimeRange.put(stationId, mapIdTimeRange);
//						}else{
//							Map<String,ArrayList<String>> mapIdTimeRange=new HashMap<>();
//							ArrayList<String> listTimeRange=new ArrayList<>();
//							listTimeRange.add(time);
//							mapIdTimeRange.put(id, listTimeRange);
//							mapStationIdTimeRange.put(stationId, mapIdTimeRange);
//						}
					}
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static boolean isMatch(String inTime,String inGpsTime) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long inTimeLong=sdf.parse(inTime).getTime()/1000;
		boolean in=false;
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
		return in;
	}
	public static void addMapIdCarCount(String car,String station,String time,Map<String,LinkedList<String>> mapStationId,Map<String,Map<String,LinkedList<String>>> mapIdTimeRange,Map<String,Map<String,Integer>> mapIdCarCount) throws ParseException{
		if(mapStationId.containsKey(station)){
			LinkedList<String> listId=mapStationId.get(station);
			for(int i=0;i<listId.size();i++){
				String id=listId.get(i);
				if(mapIdTimeRange.containsKey(id)){
					Map<String,LinkedList<String>> MapTime=mapIdTimeRange.get(id);
					String day=time.substring(0,10);
					if(MapTime.containsKey(day)){
						LinkedList<String> listTime=MapTime.get(day);
						for(int j=0;j<listTime.size();j++){
							String timeGps=listTime.get(j);
							boolean flag=isMatch(time,timeGps);
							if(flag){
								if(mapIdCarCount.containsKey(id)){
									Map<String,Integer> mapCarCount=mapIdCarCount.get(id);
									if(mapCarCount.containsKey(car)){
										mapCarCount.put(car, mapCarCount.get(car)+1);
									}else{
										mapCarCount.put(car, 1);
									}
									mapIdCarCount.put(id, mapCarCount);
								}else{
									Map<String,Integer> mapCarCount=new HashMap<>();
									mapCarCount.put(car, 1);
									mapIdCarCount.put(id, mapCarCount);
								}
								break;
							}
						}
					}
				}
			}
//			Map<String,ArrayList<String>> mapIdTimeRange=mapStationIdTimeRange.get(station);
//			for(String id:mapIdTimeRange.keySet()){
//				ArrayList<String> listTimeRange=mapIdTimeRange.get(id);
//				for(int i=0;i<listTimeRange.size();i++){
//					String timeIn=listTimeRange.get(i);
//					boolean flag=isMatch(time,timeIn);
//					if(flag){
//						if(mapIdCarCount.containsKey(id)){
//							Map<String,Integer> mapCarCount=mapIdCarCount.get(id);
//							if(mapCarCount.containsKey(car)){
//								mapCarCount.put(id, mapCarCount.get(car)+1);
//							}else{
//								mapCarCount.put(car, 1);
//							}
//							mapIdCarCount.put(id, mapCarCount);
//						}else{
//							Map<String,Integer> mapCarCount=new HashMap<>();
//							mapCarCount.put(car, 1);
//							mapIdCarCount.put(id, mapCarCount);
//						}
//						break;
//					}
//				}
//			}
		}
	}
	public static void getIdCarCount(String cqTollDataOut,String cqPassStation,String out){
		Map<String,LinkedList<String>> mapStationId=new HashMap<>();
		Map<String,Map<String,LinkedList<String>>> mapIdTimeRange=new HashMap<>();
		getStationIdTimeRange(cqPassStation,mapStationId,mapIdTimeRange);
		System.out.println("getStationIdTimeRange finish!");
		
		Map<String,Map<String,Integer>> mapIdCarCount=new HashMap<>();
		try{
			BufferedReader reader=io.getReader(cqTollDataOut, "GBK");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",9);
				String plate=data[1];
				String cx=data[2];
				String inTime=data[3];
				String outTime=data[4];
				String inStation=data[5];
				String outStation=data[6];
				String car=plate+","+cx;
				addMapIdCarCount(car,inStation,inTime,mapStationId,mapIdTimeRange,mapIdCarCount);
				addMapIdCarCount(car,outStation,outTime,mapStationId,mapIdTimeRange,mapIdCarCount);
			}
			reader.close();
			BufferedWriter writer=io.getWriter(out, "GBK");
			for(String id:mapIdCarCount.keySet()){
				Map<String,Integer> mapCarCount=mapIdCarCount.get(id);
				String maxPlate="";
				int max=Integer.MIN_VALUE;
				for(String plate:mapCarCount.keySet()){
					int cs=mapCarCount.get(plate);
					if(max<cs){
						max=cs;
						maxPlate=plate;
					}
				}
				writer.write(id+","+maxPlate+"\n");
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(mapIdCarCount.size());
	}
	public static void main(String[] args){
		getIdCarCount(cqTollDataOut,cqPassStation,idToPlate);
	}
}
