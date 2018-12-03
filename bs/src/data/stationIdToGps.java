package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dao.io;
import dao.mapSquare;
import dao.mapStation;
import dao.passStation;

public class stationIdToGps {
	private static String cChongqingMif="G:/最新地图/road2018q2/chongqing/road/Cchongqing.mif";
	private static String cChongqingMid="G:/最新地图/road2018q2/chongqing/road/Cchongqing.mid";
	private static String allStation="G:/新建文件夹/重庆/allStation_0608.txt";
	/**
	 * 读取cmid文件，取出点类型为收费广场的点和其所属的路链号，每一行为一个点信息，所以取自增行号为key
	 * @param cmid cmid文件路径
	 * @return	返回以行号为key，点所属入口路链，出口路链，点类型为值的hashMap
	 */
	public static Map<Integer,String> readCMid(String cmid){
		Map<Integer,String> idWithPointMessage =new HashMap<>();
		BufferedReader reader=io.getReader(cmid,"GBK");
		String line="";
		int lineId=0;
		try{
			while((line=reader.readLine())!=null){
				lineId++;
				String[] data=line.split(",",10);
				String inLinkId=data[3].replaceAll("\"", "").trim();
				String outLinkId=data[4].replaceAll("\"", "").trim();
				String CondType=data[5].replaceAll("\"", "").trim();
				if(CondType.equals("3")){
					idWithPointMessage.put(lineId, inLinkId+";"+outLinkId);
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return idWithPointMessage;
	}
	/**
	 * 读取cmif文件，通过判断行号,来判断是否为收费广场，得到收费广场的经纬度，并输出
	 * @param cmid cmid文件
	 * @param cmif cmif文件
	 * @return	返回收费广场经纬度的hashSet
	 */
	public static ArrayList<String> getSquareGpsSet(String cmid,String cmif){
		Map<Integer,String> idWithPointMessage=readCMid(cmid);
		BufferedReader reader=io.getReader(cmif,"GBK");
		String line="";
		int lineId=0;
		try{
			while((line=reader.readLine())!=null){
				if(line.startsWith("Point")){
					String f="";
					lineId++;
					if(idWithPointMessage.containsKey(lineId)){
						String[] data=line.split(" ",3);
						f+=data[2]+","+data[1];
						String value=idWithPointMessage.get(lineId);
						value=value+";"+f;
						idWithPointMessage.put(lineId, value);
					}
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		ArrayList<String> tollSquare=new ArrayList<>();
		Set setPoint=idWithPointMessage.entrySet();
		Map.Entry[] entriesPoint = (Map.Entry[])setPoint.toArray(new Map.Entry[setPoint.size()]);
		for(int i=0;i<entriesPoint.length;i++){
			String value=entriesPoint[i].getValue().toString();
			String[] v=value.split(";",3);
			String inLinkId=v[0];
			String outLinkId=v[1];
			String gps=v[2];
			tollSquare.add(gps);
		}
		System.out.println("收费站经纬度个数:"+tollSquare.size());
		return tollSquare;
	}
	/**
	 * 创建mapStation数据，由于没有收费站编号，以1开始的自增值为每个收费站的id
	 * 每个收费站可以有多个收费广场，有id，有名称
	 * 每个收费广场有其经纬度和其所属的收费站id
	 * 通过得到的收费广场的hashSet，遍历该set，每次取第一个值，建立一个mapSquare作为一个收费站的第一个收费广场，建立一个list，并将该广场添加进去
	 * 遍历剩下的所有收费广场，计算其与该list中每个收费广场的欧式距离，只要有一个欧式距离小于2km，则该收费广场属于该收费站，并添加到list中，然后
	 * 从set里去掉该广场，直到余下所有的收费广场没有满足距离条件为止，则该收费站的所有广场都找到了
	 * 一直进行上述步骤，直到set为空，中间如果有收费站名称的经纬度，则添加进去
	 * @param cmid	cmid文件路径
	 * @param cmif	cmif文件
	 * @return 返回以自创收费站id为key，mapStation收费站信息为值的hashMap
	 */
	public static Map<Integer,mapStation> getStation(String cmid,String cmif){
		Map<Integer,mapStation> stationList=new HashMap<>();
		ArrayList<String> squareGpsList=getSquareGpsSet(cmid,cmif);
		
		int index=1;
		while(!squareGpsList.isEmpty()){
			String outGps=squareGpsList.get(0);
			squareGpsList.remove(0);
			String latString=outGps.split(",",2)[0];
			String lngString=outGps.split(",",2)[1];
			double lat=Double.parseDouble(latString);
			double lng=Double.parseDouble(lngString);
			mapSquare square1=new mapSquare(lngString,latString,index);
			ArrayList<mapSquare> listSquare=new ArrayList<>();
			listSquare.add(square1);
			mapStation station;
			boolean flag=true;
			while(flag){
				flag=false;
				for(int j=0;j<listSquare.size();j++){
					mapSquare square=listSquare.get(j);
					double lat1=Double.parseDouble(square.getLat());
					double lng1=Double.parseDouble(square.getLng());
					for(int i=0;i<squareGpsList.size();i++){
						String inGps=squareGpsList.get(i);
						String lat2String=inGps.split(",",2)[0];
						String lng2String=inGps.split(",",2)[1];
						double lat2=Double.parseDouble(lat2String);
						double lng2=Double.parseDouble(lng2String);
						double distance=gps.getDistance(lng1, lat1, lng2, lat2);
						if(distance<=2000){
							flag=true;
							mapSquare squareNew=new mapSquare(lng2String,lat2String,index);
							listSquare.add(squareNew);
							squareGpsList.remove(i);
							break;
						}
					}
				}
			}
			station=new mapStation(index,listSquare);
			stationList.put(index,station);
			stationList.put(index,station);
			index++;
		}
		return stationList;
	}
	public static Map<String,String> readAllStation(String allStation){
		Map<String,String> mapAllStation=new HashMap<>();
		try{
//			BufferedWriter writer=io.getWriter("C:/Users/pengrui/Desktop/新建文件夹 (2)/stationId.csv", "gbk");
			String line="";
			BufferedReader reader=io.getReader(allStation, "GBK");
			while((line=reader.readLine())!=null){
				String[] data=line.split(":",2);
				String stationId=data[0].replaceAll("\"", "");
				String lng=data[1].split(",",3)[0].replaceAll("\\[", "");
				String lat=data[1].split(",",3)[1].replaceAll("\\]", "");
				mapAllStation.put(stationId, lat+","+lng);
//				writer.write(stationId+","+lat+","+lng+"\n");
			}
			reader.close();
//			writer.flush();
//			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return mapAllStation;
	}
	
	public static String isInStation(String xy,Map<String,String> mapCordinateToStationId){
		double x=Double.valueOf(xy.split(",",2)[0]);
		double y=Double.valueOf(xy.split(",",2)[1]);
		for(double i=x-1;i<=x+1;i++){
			for(double j=y-1;j<=y+1;j++){
				if(mapCordinateToStationId.containsKey(i+","+j)){
					return mapCordinateToStationId.get(i+","+j);
				}
			}
		}
		return "";
	}
	public static Map<String,String> getStationIdToGps(String cmid,String cmif,String allStation) throws IOException{
//		BufferedWriter writer=io.getWriter("C:/Users/pengrui/Desktop/新建文件夹 (2)/stationId1.csv", "gbk");
		Map<String,String> gpsToStationId=new HashMap<>();
		ArrayList<String> squareList=getSquareGpsSet(cmid,cmif);
		Map<String,String> mapAllStation=readAllStation(allStation);
		Map<String,String> mapCordinateToStationId=new HashMap<>();
		for(String stationId:mapAllStation.keySet()){
			String gps=mapAllStation.get(stationId);
			double lat=Double.parseDouble(gps.split(",")[0]);
			double lng=Double.parseDouble(gps.split(",")[1]);
			String xy=passStation.getXY(lng, lat,500);
			mapCordinateToStationId.put(xy, stationId);
		}
		int count=0;
		for(int i=0;i<squareList.size();i++){
			String gps=squareList.get(i);
			double lat=Double.parseDouble(gps.split(",")[0]);
			double lng=Double.parseDouble(gps.split(",")[1]);
			String xy=passStation.getXY(lng, lat,500);
			String stationId=isInStation(xy,mapCordinateToStationId);
			if(!stationId.equals("")){
				count++;
//				System.out.println(gps+":"+stationId);
				gpsToStationId.put(gps.split(",")[0]+","+gps.split(",")[1],stationId);
//				writer.write(stationId+","+lat+","+lng+"\n");
			}
		}
//		writer.flush();
//		writer.close();
		System.out.println(mapAllStation.size()+","+mapCordinateToStationId.size());
		System.out.println(squareList.size()+","+count);
		return gpsToStationId;
	}
	public static void main(String[] args) throws IOException{
//		readAllStation(allStation);
		getStationIdToGps(cChongqingMid,cChongqingMif,allStation);
	}
}
