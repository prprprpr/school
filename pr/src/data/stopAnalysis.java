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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dao.clusterAnalysis;
import dao.dataPoint;
import dao.serviceArea;
import dao.io;
import dao.point;

public class stopAnalysis {
	private static String matchedData="D:/matchedData.csv";
	private static String idStops="D:/货车轨迹数据分析/id停留点";
	private static String oneIdData="D:/货车轨迹数据分析/经过高速的id/";
	private static String firstStop="D:/货车轨迹数据分析/firstStop.csv";
	public static void readMatchedData(String matchedData){
		Map<String,String> serviceMap=serviceArea.readCqServiceArea(serviceArea.cqServiceArea);
		Map<String,Integer> map=new HashMap<>();
		int tradeCount=0;
		int passServiceCount=0;
		try{
			BufferedReader reader=io.getReader(matchedData, "gbk");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(";");
				String traces=data[1];
				String[] trace=traces.split("\\|");
				tradeCount+=trace.length;
				for(int i=0;i<trace.length;i++){
					String[] message=trace[i].split(",");
					if(message.length==7){
						passServiceCount++;
						String serviceAreaId=message[5];
						if(map.containsKey(serviceAreaId)){
							map.put(serviceAreaId, map.get(serviceAreaId)+1);
						}else{
							map.put(serviceAreaId, 1);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		ArrayList<String> list=new ArrayList<>();
		for(String key:map.keySet()){
			list.add(key);
			for(int i=list.size()-1;i>0&&map.get(list.get(i-1))<map.get(list.get(i));i--){
				String temp=list.get(i-1);
				list.set(i-1, list.get(i));
				list.set(i, temp);
			}
		}
		try{
			BufferedWriter writer=io.getWriter("C:/Users/pengrui/Desktop/新建文件夹/top10ServiceArea.csv", "gbk");
			int count10=0;
			System.out.println("服务区数量："+list.size());
			for(int i=0;i<10;i++){
				writer.write(list.get(i)+","+serviceMap.get(list.get(i))+"\n");
				if(i<10){
					count10+=map.get(list.get(i));
				}
				System.out.println(list.get(i)+","+serviceMap.get(list.get(i))+":"+map.get(list.get(i)));
			}
			System.out.println("前十服务区经过的总交易数："+count10);
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("总交易数："+tradeCount);
		System.out.println("经过服务区交易数："+passServiceCount);
	}
	public static Map<String,ArrayList<String>> getMapMatchedData(String matchedData){
		Map<String,ArrayList<String>> mapMatchedData=new HashMap<>();
		try{
			BufferedReader reader=io.getReader(matchedData, "gbk");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(";");
				String id=data[0].split(",")[0];
				String traces=data[1];
				String[] trace=traces.split("\\|");
				ArrayList<String> timeRangeList=new ArrayList<>();
				if(trace.length==1){
					String[] data1=trace[0].split(",");
					String outTime=data1[3];
					timeRangeList.add(outTime);
				}else{
					for(int i=0;i<trace.length-1;i++){
						String[] data1=trace[i].split(",");
						@SuppressWarnings("unused")
						String inTime1=data1[1];
						String outTime1=data1[3];
						String[] data2=trace[i+1].split(",");
						if(data2.length<4){
							continue;
						}
						String inTime2=data2[1];
						String outTime2=data2[3];
						if(i!=trace.length-2){
							timeRangeList.add(outTime1+","+inTime2);
						}else{
							timeRangeList.add(outTime1+","+inTime2);
							timeRangeList.add(outTime2);
						}
					}
				}
				mapMatchedData.put(id, timeRangeList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return mapMatchedData;
	}
	public static void readOneId(String id,String path,ArrayList<String> timeRangeList,String out,ArrayList<dataPoint> listStayPoints){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		@SuppressWarnings("unused")
		String outData="";
		Map<Integer,ArrayList<String>> map=new HashMap<>();
		try{
			BufferedReader reader=io.getReader(path, "gbk");
			String line="";
			int index=0;
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				String gpsTime=data[0];
				String lat=data[1];
				String lng=data[2];
				String v=data[3];
				@SuppressWarnings("unused")
				String direction=data[4];
				for(int i=index;i<timeRangeList.size();i++){
					String timeRange=timeRangeList.get(i);
					String[] times=timeRange.split(",");
					String inTime="";
					String outTime="";
					if(times.length==2){
						inTime=times[0];
						outTime=times[1];
						if(!sdf.parse(gpsTime).before(sdf.parse(inTime))&&!sdf.parse(gpsTime).after(sdf.parse(outTime))){
							index=i;
							if(map.containsKey(index)){
								ArrayList<String> points=map.get(index);
								points.add(lat+","+lng+","+v+","+gpsTime);
								map.put(index, points);
							}else{
								ArrayList<String> points=new ArrayList<>();
								points.add(lat+","+lng+","+v+","+gpsTime);
								map.put(index, points);
							}
							break;
						}
					}else{
						outTime=times[0];
						if(!sdf.parse(gpsTime).before(sdf.parse(outTime))){
							index=i;
							if(map.containsKey(index)){
								ArrayList<String> points=map.get(index);
								points.add(lat+","+lng+","+v+","+gpsTime);
								map.put(index, points);
							}else{
								ArrayList<String> points=new ArrayList<>();
								points.add(lat+","+lng+","+v+","+gpsTime);
								map.put(index, points);
							}
							break;
						}
					}
				}
			}
			reader.close();
//			for(int i=0;i<timeRangeList.size();i++){
//				System.out.println(timeRangeList.get(i));
//			}
//			for(int key:map.keySet()){
//				ArrayList<String> points=map.get(key);
//				System.out.println("index:"+key);
//				
//			}
			int a=0;//标识dataPoint的name
			ArrayList<dataPoint> pointList=new ArrayList<>();
			for(int key:map.keySet()){
				ArrayList<String> points=map.get(key);
				ArrayList<String> listGps=new ArrayList<>();
				String preV="";
				for(int i=0;i<points.size();i++){
					String[] data=points.get(i).split(",");
					String lat=data[0];
					String lng=data[1];
					String v=data[2];
					@SuppressWarnings("unused")
					String gpsTime=data[3];
					if(v.equals("0")){
						listGps.add(lat+","+lng);
						if(preV.equals("")&&i>0){
							String[] dataPre=points.get(i-1).split(",");
							String vPre=dataPre[2];
							preV=vPre;
						}
					}else{
						double avgV=0;
						if(!preV.equals("")){
							avgV=(Double.parseDouble(preV)+Double.parseDouble(v))/2;
						}
						if(listGps.size()>2&&avgV<=10){
							String latAndLng=getCenterPoint1(listGps);
							dataPoint point=new dataPoint(Double.valueOf(latAndLng.split(",")[0]),Double.valueOf(latAndLng.split(",")[1]),key,""+a);
							a++;
							pointList.add(point);
						}
						listGps.clear();
						preV="";
						listGps.add(lat+","+lng);
					}
				}
				if(listGps.size()>2){
					String latAndLng=getCenterPoint1(listGps);
					dataPoint point=new dataPoint(Double.valueOf(latAndLng.split(",")[0]),Double.valueOf(latAndLng.split(",")[1]),key,""+a);
					a++;
					pointList.add(point);
				}
				listGps.clear();
			}
			if(pointList.size()>0){
				getStopPoint(pointList,id,out,listStayPoints);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void getStopPoint(ArrayList<dataPoint> points,String id,String out,ArrayList<dataPoint> listStayPoints){
		clusterAnalysis ca = new clusterAnalysis();
		ArrayList<String> stayCenterPoints=new ArrayList<>();
		@SuppressWarnings("unused")
		List<dataPoint> dps = ca.startAnalysis(points, 500, 2,stayCenterPoints);
		try{
			if(stayCenterPoints.size()>0){
				BufferedWriter writer=io.getWriter(out+"/"+id+".csv", "gbk");
				for(int i=0;i<stayCenterPoints.size();i++){
					writer.write(stayCenterPoints.get(i)+"\n");
					String str=stayCenterPoints.get(i);
					String latAndLng=str.split(":")[0];
					double lat=Double.parseDouble(latAndLng.split(",")[0]);
					double lng=Double.parseDouble(latAndLng.split(",")[1]);
					dataPoint point=new dataPoint(lat,lng,listStayPoints.size()+"");
					listStayPoints.add(point);
				}
				writer.flush();
				writer.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void read(String oneIdData,String matchedData,String out) throws IOException{
		Map<String,ArrayList<String>> mapMatchedData=getMapMatchedData(matchedData);
		System.out.println("read matchedData finish!");
		File file=new File(oneIdData);
		List<String> listFile=Arrays.asList(file.list());
		ArrayList<dataPoint> listStayPoints=new ArrayList<>();
		for(int k=0;k<listFile.size();k++){
			String fileName=listFile.get(k);
			String path=oneIdData+fileName;
			String id=fileName.split("\\.")[0];
			if(mapMatchedData.containsKey(id)){
				readOneId(id,path,mapMatchedData.get(id),out,listStayPoints);
			}
		}
		System.out.println(listStayPoints.size());
//		cluster(listStayPoints);
	}
	
	public static void cluster(ArrayList<dataPoint> listStayPoints) throws IOException{
		clusterAnalysis ca = new clusterAnalysis();
		ArrayList<String> stayCenterPoints=new ArrayList<>();
		@SuppressWarnings("unused")
		List<dataPoint> dps = ca.startAnalysis(listStayPoints, 500, 50,stayCenterPoints);
		BufferedWriter writer=io.getWriter("C:/Users/pengrui/Desktop/新建文件夹/stop2.csv", "gbk");
		for(int i=0;i<stayCenterPoints.size();i++){
			writer.write(stayCenterPoints.get(i)+"\n");
		}
		writer.flush();
		writer.close();
	}
	public static String getCenterPoint1(ArrayList<String> str){
		int total=str.size();
		double lat=0;
		double lng=0;
		for(int i=0;i<str.size();i++){
			lat+=Double.valueOf(str.get(i).split(",")[0])*Math.PI/180;
			lng+=Double.valueOf(str.get(i).split(",")[1])*Math.PI/180;
		}
		lat/=total;
		lng/=total;
		return lat*180/Math.PI+","+lng*180/Math.PI;
	}
	public static void getZeroSpeedPoint(String oneIdData,String out) throws IOException{
		BufferedWriter writer=io.getWriter(out, "gbk");
		File file=new File(oneIdData);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			String path=oneIdData+"/"+list.get(i);
			try{
				BufferedReader reader=io.getReader(path, "gbk");
				String line="";
				ArrayList<String> listGps=new ArrayList<>();
				while((line=reader.readLine())!=null){
					String[] data=line.split(",");
					@SuppressWarnings("unused")
					String gpsTime=data[0];
					String lat=data[1];
					String lng=data[2];
					String v=data[3];
					@SuppressWarnings("unused")
					String direction=data[4];
					if(v.equals("0")){
						listGps.add(lat+","+lng);
					}else{
						if(listGps.size()>0){
							String latAndLng=getCenterPoint1(listGps);
							writer.write(latAndLng+"\n");
							listGps.clear();
						}
					}
				}
				reader.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		writer.flush();
		writer.close();
	}
	
	public static void readStop(String path){
		BufferedReader reader=io.getReader(path, "gbk");
		BufferedWriter writer=io.getWriter("C:/Users/pengrui/Desktop/新建文件夹/1/stop.csv", "gbk");
		try{
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(":");
				writer.write(data[0]+"\n");
			}
			reader.close();
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void test(){
		Map<String,ArrayList<String>> mapMatchedData=getMapMatchedData(matchedData);
		ArrayList<dataPoint> listStayPoints=new ArrayList<>();
		String id="b26a84fbe10b2070dbe7449bd2c6049e";
		String path="D:/货车轨迹数据分析/经过高速的id/b26a84fbe10b2070dbe7449bd2c6049e.csv";
		String out="C:/Users/pengrui/Desktop/新建文件夹/1";
		readOneId(id,path,mapMatchedData.get(id),out,listStayPoints);
	}

	public static void readStops(String in){
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		int count=0;
		for(String p:list){
			String path=in+"/"+p;
			try{
				BufferedReader reader=io.getReader(path, "gbk");
				@SuppressWarnings("unused")
				String line="";
				while((line=reader.readLine())!=null){
					count++;
				}
				reader.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		System.out.println(count);
	}
	public static void main(String[] args) throws IOException, ParseException{
//		readMatchedData(matchedData);
		read(oneIdData,matchedData,"D:/货车轨迹数据分析/id停留点");//得到每个id的停留点
		
//		getZeroSpeedPoint(oneIdData,firstStop);
		
//		test();
//		readStop("C:/Users/pengrui/Desktop/新建文件夹/1/b26a84fbe10b2070dbe7449bd2c6049e.csv");
	}
}
