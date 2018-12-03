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
	private static String cChongqingMif="G:/���µ�ͼ/road2018q2/chongqing/road/Cchongqing.mif";
	private static String cChongqingMid="G:/���µ�ͼ/road2018q2/chongqing/road/Cchongqing.mid";
	private static String allStation="G:/�½��ļ���/����/allStation_0608.txt";
	/**
	 * ��ȡcmid�ļ���ȡ��������Ϊ�շѹ㳡�ĵ����������·���ţ�ÿһ��Ϊһ������Ϣ������ȡ�����к�Ϊkey
	 * @param cmid cmid�ļ�·��
	 * @return	�������к�Ϊkey�����������·��������·����������Ϊֵ��hashMap
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
	 * ��ȡcmif�ļ���ͨ���ж��к�,���ж��Ƿ�Ϊ�շѹ㳡���õ��շѹ㳡�ľ�γ�ȣ������
	 * @param cmid cmid�ļ�
	 * @param cmif cmif�ļ�
	 * @return	�����շѹ㳡��γ�ȵ�hashSet
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
		System.out.println("�շ�վ��γ�ȸ���:"+tollSquare.size());
		return tollSquare;
	}
	/**
	 * ����mapStation���ݣ�����û���շ�վ��ţ���1��ʼ������ֵΪÿ���շ�վ��id
	 * ÿ���շ�վ�����ж���շѹ㳡����id��������
	 * ÿ���շѹ㳡���侭γ�Ⱥ����������շ�վid
	 * ͨ���õ����շѹ㳡��hashSet��������set��ÿ��ȡ��һ��ֵ������һ��mapSquare��Ϊһ���շ�վ�ĵ�һ���շѹ㳡������һ��list�������ù㳡��ӽ�ȥ
	 * ����ʣ�µ������շѹ㳡�����������list��ÿ���շѹ㳡��ŷʽ���룬ֻҪ��һ��ŷʽ����С��2km������շѹ㳡���ڸ��շ�վ������ӵ�list�У�Ȼ��
	 * ��set��ȥ���ù㳡��ֱ���������е��շѹ㳡û�������������Ϊֹ������շ�վ�����й㳡���ҵ���
	 * һֱ�����������裬ֱ��setΪ�գ��м�������շ�վ���Ƶľ�γ�ȣ�����ӽ�ȥ
	 * @param cmid	cmid�ļ�·��
	 * @param cmif	cmif�ļ�
	 * @return �������Դ��շ�վidΪkey��mapStation�շ�վ��ϢΪֵ��hashMap
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
//			BufferedWriter writer=io.getWriter("C:/Users/pengrui/Desktop/�½��ļ��� (2)/stationId.csv", "gbk");
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
//		BufferedWriter writer=io.getWriter("C:/Users/pengrui/Desktop/�½��ļ��� (2)/stationId1.csv", "gbk");
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
