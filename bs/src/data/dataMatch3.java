package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import dao.io;

public class dataMatch3 {
	private static String cqPassStation="D:/货车轨迹数据分析/cqPassStation.csv";
	private static String cqTollDataOut="D:/货车轨迹数据分析/cqTollDataOut.csv";
	private static String in="D:/货车轨迹数据分析";
	private static String cqAllPassStation="D:/货车轨迹数据分析/cqAllPassStation.csv";
	
	private static String outPath16Chongqing="G:/地图/收费站数据/16PoiChongqing.csv";
	private static String outPath18Chongqing="G:/地图/收费站数据/18PoiChongqing.csv";
	private static String cqStationName="G:/新建文件夹/重庆/重庆站点表.csv";
	private static String idToPlate="D:/货车轨迹数据分析/idToPlate.csv";
	
	public static void getStationIdTimeRange(String cqPassStation,Map<String,Map<String,ArrayList<String>>> mapIdStationIdTimeRange,Set<String> timeAquire){
		Map<String,String> mapGpsStationId=map.matchGpsToStationId(outPath18Chongqing,outPath16Chongqing,cqStationName);
		try{
			BufferedReader reader=io.getReader(cqPassStation, "gbk");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(":",2);
				String id=data[0];
				Map<String,ArrayList<String>> mapStationIdTimeRange=new HashMap<>();
				String squareTrace=data[1];
				String[] squares=squareTrace.split("\\|");
				for(int i=0;i<squares.length;i++){
					String square=squares[i];
					String squareGps=square.split(",",3)[0]+";"+square.split(",",3)[1];
					String time=square.split(",",3)[2];
					timeAquire.add(time.substring(0, 13));
					if(mapGpsStationId.containsKey(squareGps)){
						String stationId=mapGpsStationId.get(squareGps).split(";",2)[1];
						if(mapStationIdTimeRange.containsKey(stationId)){
							ArrayList<String> listTimeRange=mapStationIdTimeRange.get(stationId);
							listTimeRange.add(time);
							mapStationIdTimeRange.put(stationId, listTimeRange);
						}else{
							ArrayList<String> listTimeRange=new ArrayList<>();
							listTimeRange.add(time);
							mapStationIdTimeRange.put(stationId, listTimeRange);
						}
					}
				}
				mapIdStationIdTimeRange.put(id, mapStationIdTimeRange);
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
	
	public static void addMapStationIdDayTime(String car,String time,String station,Map<String,Map<String,ArrayList<String>>> mapStationIdDayTime){
		if(mapStationIdDayTime.containsKey(station)){
			Map<String,ArrayList<String>> mapDayTime=mapStationIdDayTime.get(station);
			String day=time.substring(0,10);
			if(mapDayTime.containsKey(day)){
				ArrayList<String> listTime=mapDayTime.get(day);
				listTime.add(time+";"+car);
				mapDayTime.put(day, listTime);
			}else{
				ArrayList<String> listTime=new ArrayList<>();
				listTime.add(time+";"+car);
				mapDayTime.put(day, listTime);
			}
			mapStationIdDayTime.put(station, mapDayTime);
		}else{
			Map<String,ArrayList<String>> mapDayTime=new HashMap<>();
			String day=time.substring(0,10);
			ArrayList<String> listTime=new ArrayList<>();
			listTime.add(time+";"+car);
			mapDayTime.put(day, listTime);
			mapStationIdDayTime.put(station, mapDayTime);
		}
	}
	public static void getIdCarCount(String cqTollDataOut,String cqPassStation,String out){
		Map<String,Map<String,ArrayList<String>>> mapIdStationIdTimeRange=new HashMap<>();
		Set<String> timeAquire=new HashSet<>();
		getStationIdTimeRange(cqPassStation,mapIdStationIdTimeRange,timeAquire);
		System.out.println("getStationIdTimeRange finish!");
		
		Map<String,Map<String,ArrayList<String>>> mapStationIdDayTime=new HashMap<>();
		
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
				if(cx.matches("[0-9]")&&Integer.parseInt(cx)>=2&&inTime.length()>14&&outTime.length()>14){
					String inTimeAquire=inTime.substring(0, 13);
					if(timeAquire.contains(inTimeAquire)){
						addMapStationIdDayTime(car,inTime,inStation,mapStationIdDayTime);
					}
					String outTimeAquire=outTime.substring(0, 13);
					if(timeAquire.contains(outTimeAquire)){
						addMapStationIdDayTime(car,outTime,outStation,mapStationIdDayTime);
					}
				}
			}
			reader.close();
			System.out.println("read mapStationIdDayTime finish!");
			
			for(String id:mapIdStationIdTimeRange.keySet()){
				Map<String,ArrayList<String>> mapStationIdTimeRange=mapIdStationIdTimeRange.get(id);
				for(String stationId:mapStationIdTimeRange.keySet()){
					ArrayList<String> listTimeGps=mapStationIdTimeRange.get(stationId);
					if(mapStationIdDayTime.containsKey(stationId)){
						Map<String,ArrayList<String>> mapDayTime=mapStationIdDayTime.get(stationId);
						for(int i=0;i<listTimeGps.size();i++){
							String timeGps=listTimeGps.get(i);
							String day=timeGps.substring(0,10);
							if(mapDayTime.containsKey(day)){
								ArrayList<String> listTimeAndCar=mapDayTime.get(day);
								for(int j=0;j<listTimeAndCar.size();j++){
									String timeAndCar=listTimeAndCar.get(j);
									String time=timeAndCar.split(";",2)[0];
									String car=timeAndCar.split(";",2)[1];
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
										listTimeAndCar.remove(j);
										break;
									}
								}
							}
						}
					}
				}
			}
			
			BufferedWriter writer=io.getWriter(out, "GBK");
			for(String id:mapIdCarCount.keySet()){
				Map<String,Integer> mapCarCount=mapIdCarCount.get(id);
				System.out.println(id+","+mapCarCount);
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
	
	public static void getAllCqPassStation(String in,String out) throws IOException{
		BufferedWriter writer=io.getWriter(out, "gbk");
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			String path=in+"/"+list.get(i);
			File fileIn=new File(path);
			if(fileIn.isDirectory()){
				String pathIn=path+"/"+"cqPassStation.csv";
				try{
					BufferedReader reader=io.getReader(pathIn, "gbk");
					String line="";
					while((line=reader.readLine())!=null){
						writer.write(line+"\n");
					}
					reader.close();
				}catch(Exception e){
					e.printStackTrace();
				}
				System.out.println(pathIn);
			}
		}
		writer.flush();
		writer.close();
	}
	
	public static void getAllIdMessage(String in1,String in,String out) throws IOException{
		BufferedReader reader1=io.getReader(in1, "utf-8");
		String line1="";
		Set<String> idSet=new HashSet<>();
		try{
			while((line1=reader1.readLine())!=null){
				System.out.println(line1);
				String[] data=line1.split(",",3);
				String id=data[0];
				idSet.add(id);
			}
			reader1.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			String path=in+"/"+list.get(i);
			File fileIn=new File(path);
			if(fileIn.isDirectory()){
				String pathIn=path+"/"+"重庆每条轨迹所有天";
				File f=new File(pathIn);
				List<String> fList=Arrays.asList(f.list());
				for(int j=0;j<fList.size();j++){
					String id=fList.get(j).split("\\.",2)[0];
					String pathInIn=pathIn+"/"+fList.get(j);
					if(idSet.contains(id)){
						String outPath=out+"/"+fList.get(j);
						try{
							BufferedReader reader=io.getReader(pathInIn, "gbk");
							BufferedWriter writer=io.getWriter(outPath, "gbk");
							String line="";
							while((line=reader.readLine())!=null){
								writer.write(line+"\n");
							}
							reader.close();
							writer.flush();
							writer.close();
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
				System.out.println(pathIn);
			}
		}
	}
	
	public static void main(String[] args) throws IOException{
//		getAllCqPassStation(in,cqAllPassStation);
//		getIdCarCount(cqTollDataOut,cqAllPassStation,idToPlate);
		getAllIdMessage("D:/yu_4_5_idToPlate.csv",in,"D:/45IdToPlate");
	}
}
