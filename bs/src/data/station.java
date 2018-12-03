package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dao.io;
import dao.mapSquare;
import dao.mapStation;

public class station {
	private static String cChongqingMif="G:/���µ�ͼ/road2018q2/chongqing/road/Cchongqing.mif";
	private static String cChongqingMid="G:/���µ�ͼ/road2018q2/chongqing/road/Cchongqing.mid";
	
	private static String PNamechongqingMid="D:/mapinfo_ditu/��ͼ/level2/level2/chongqing/other/PNamechongqing.mid";
	private static String POIchongqingMid="D:/mapinfo_ditu/��ͼ/level2/level2/chongqing/index/POIchongqing.mid";
	
	private static String shortestPath="G:/����/djjkstra���·�����/·�����˽��/�շ�վ��̾���20180601185300.csv";
	
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
	 * ��ȡ16��pName�ļ����õ��������շ�վ��β��poiId������
	 * @param path	pName�ļ�·��
	 * @return	������poiIdΪkey���շ�վ����Ϊֵ��hashMap
	 */
	public static Map<String,String> getTollStationPoiId(String path){
		Map<String,String> poiIdToll=new HashMap<>();
		BufferedReader reader=io.getReader(path,"GBK");
		String line="";
		try{
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",10);
				String poiId=data[0].replaceAll("\"", "").trim();
				String name=data[2].replaceAll("\"", "").trim();
				if(name.endsWith("�շ�վ")){
					poiIdToll.put(poiId, name);
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return poiIdToll;
	}
	/**
	 * ��ȡpoiMid�ļ����ҵ�poiId��Ӧ���շ�վ�ľ�γ��
	 * @param pName	pName�ļ�·��
	 * @param poiMid	poiMid�ļ�·��
	 * @return	�������շѹ㳡��γ��Ϊkey���շѹ㳡�����շ�վ����Ϊֵ��hashMap
	 */
	public static Map<String,String> get16PoiGps(String pName,String poiMid){
		Map<String,String> poiIdToll=getTollStationPoiId(pName);
		BufferedReader reader=io.getReader(poiMid,"GBK");
		Map<String,String> map=new HashMap<>();
		String line="";
		try{
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",23);
				String poiId=data[7].replaceAll("\"", "").trim();
				String linkId=data[12].replaceAll("\"", "").trim();
				String lng=data[5].replaceAll("\"", "").trim();
				String lat=data[6].replaceAll("\"", "").trim();
				if(poiIdToll.containsKey(poiId)){
					String tollName=poiIdToll.get(poiId);
					map.put(lat+","+lng, tollName);
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * ����mapStation���ݣ�����û���շ�վ��ţ���1��ʼ������ֵΪÿ���շ�վ��id
	 * ÿ���շ�վ�����ж���շѹ㳡����id��������
	 * ÿ���շѹ㳡���侭γ�Ⱥ����������շ�վid
	 * ͨ���õ����շѹ㳡��hashSet��������set��ÿ��ȡ��һ��ֵ������һ��mapSquare��Ϊһ���շ�վ�ĵ�һ���շѹ㳡������һ��list�������ù㳡��ӽ�ȥ
	 * ����ʣ�µ������շѹ㳡�����������list��ÿ���շѹ㳡��ŷʽ���룬ֻҪ��һ��ŷʽ����С��2km������շѹ㳡���ڸ��շ�վ������ӵ�list�У�Ȼ��
	 * ��set��ȥ���ù㳡��ֱ���������е��շѹ㳡û�������������Ϊֹ������շ�վ�����й㳡���ҵ���
	 * һֱ�����������裬ֱ��setΪ�գ��м�������շ�վ���Ƶľ�γ�ȣ�����ӽ�ȥ
	 * @param pName	pName�ļ�·��
	 * @param poiMid	poiMid�ļ�·��
	 * @param cmid	cmid�ļ�·��
	 * @param cmif	cmif�ļ�
	 * @return �������Դ��շ�վidΪkey��mapStation�շ�վ��ϢΪֵ��hashMap
	 */
	public static Map<Integer,mapStation> getStation(String pName,String poiMid,String cmid,String cmif){
		Map<Integer,mapStation> stationList=new HashMap<>();
		ArrayList<String> squareGpsList=getSquareGpsSet(cmid,cmif);
		Map<String,String> map16poiMessage=get16PoiGps(pName,poiMid);
		
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
			
			if(map16poiMessage.containsKey(outGps)){
				String stationName=map16poiMessage.get(outGps);
				station=new mapStation(index,stationName,listSquare);
				stationList.put(index,station);
			}else{
				station=new mapStation(index,listSquare);
				stationList.put(index,station);
			}
			index++;
		}
		return stationList;
	}
	/**
	 * ��ȡ������������շѹ㳡�����̾���
	 * @param path	��̾��������ļ�
	 * @return	�����������շѹ㳡��γ��Ϊkey����̾���Ϊֵ��hashMap
	 */
	public static Map<String,Double> getShortest(String path){
		Map<String,Double> map=new HashMap<>();
		try{
			BufferedReader reader=io.getReader(path, "gbk");
			String line="";
			int amount=0;
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",3);
				String d=data[2];
				if(!d.equals("0")&&!d.equals("null")){
					amount++;
					double distance=Double.parseDouble(d);
					map.put(data[0]+","+data[1], distance);
				}
			}
			reader.close();
			System.out.println("od���벻Ϊ�գ�"+amount);
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * ���������շ�վ��ľ���
	 * ��������վ�������շѹ㳡��γ�ȣ��鿴�Ƿ�����̾���
	 * @param station1 �շ�վ1
	 * @param station2	�շ�վ2
	 * @param shortestPath	��̾���
	 * @return	����������վ����̾��룬��û�ҵ����򷵻�-1
	 */
	public static double getMinStationDistance(mapStation station1,mapStation station2,Map<String,Double> shortestPath){
		ArrayList<mapSquare> list1=station1.getListSquare();
		ArrayList<mapSquare> list2=station2.getListSquare();
		double min=Double.MAX_VALUE;
		boolean flag=false;
		for(int i=0;i<list1.size();i++){
			mapSquare square1=list1.get(i);
			for(int j=0;j<list2.size();j++){
				mapSquare square2=list2.get(j);
				String key=square1.getLng()+" "+square1.getLat()+","+square2.getLng()+" "+square2.getLat();
				if(shortestPath.containsKey(key)){
					flag=true;
					double distance=shortestPath.get(key);
					if(min>distance){
						min=distance;
					}
				}
			}
		}
		if(flag){
			return min;
		}else{
			return -1;
		}
	}
	/**
	 * ��������վ�㣬��¼վ������̾���
	 * @param stationList	�շ�վhashMap
	 * @param shortestPath	�շѹ㳡����̾���
	 * @return	�����������շ�վidΪkey����̾���Ϊֵ��hashMap
	 */
	public static Map<String,Double> getEachStationDistance(Map<Integer,mapStation> stationList,Map<String,Double> shortestPath){
		int amount=0;
		Map<String,Double> mapStationDis=new HashMap<>();
		int size=stationList.size();
		for(int i=1;i<=size;i++){
			mapStation station1=stationList.get(i);
			for(int j=i+1;j<=size;j++){
				amount++;
				mapStation station2=stationList.get(j);
				double distance1=getMinStationDistance(station1,station2,shortestPath);
				double distance2=getMinStationDistance(station2,station1,shortestPath);
				if(distance1!=-1){
					int id1=station1.getId();
					int id2=station2.getId();
					mapStationDis.put(id1+","+id2, distance1);
				}
				if(distance2!=-1){
					int id1=station1.getId();
					int id2=station2.getId();
					mapStationDis.put(id2+","+id1, distance2);
				}
			}
		}
//		System.out.println("amount:"+amount);
		return mapStationDis;
	}
	
	/**
	 * ����һ���շѹ㳡�ľ�γ�ȣ��õ����շѹ㳡�������շ�վ���
	 * @param gps	������շѹ㳡��γ��
	 * @param stationList	�շ�վHashMap
	 * @return	���ظ��շѹ㳡�������շ�վ���
	 */
	public static int getIndex(String gps,Map<Integer,mapStation> stationList){
		for(int index:stationList.keySet()){
			mapStation station=stationList.get(index);
			ArrayList<mapSquare> listSquare=station.getListSquare();
			for(int i=0;i<listSquare.size();i++){
				mapSquare square=listSquare.get(i);
				String g=square.getLat()+","+square.getLng();
				if(g.equals(gps)){
					return index;
				}
			}
		}
		return -1;
	}
	/**
	 * ��list������������ݽ��е���λ��
	 * @param list
	 * @param i
	 * @param j
	 */
	public static void swap(ArrayList<Integer> list,int i,int j){
		int temp=list.get(i);
		list.set(i, list.get(j));
		list.set(j, temp);
	}
	/**
	 * ���line
	 * @param line һ�����ϵ��շ�վ���
	 * @param stationList	�շ�վ��ϢhashMap
	 * @param outPath	���·��
	 */
	public static void writeLine(ArrayList<Integer> line,Map<Integer,mapStation> stationList,BufferedWriter writer){
		try{
			for(int i=0;i<line.size();i++){
				int ind=line.get(i);
				mapStation station=stationList.get(ind);
				if(station!=null){
					ArrayList<mapSquare> squareList=station.getListSquare();
					for(int j=0;j<squareList.size();j++){
						mapSquare square=squareList.get(j);
						writer.write(square.getStationIndex()+","+square.getLng()+","+square.getLat()+"\n");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void writeLine2(ArrayList<Integer> line,Map<Integer,mapStation> stationList,Map<String,Double> stationDis,BufferedWriter writer){
		try{
			for(int i=0;i<=line.size()-3;i++){
				int ind1=line.get(i);
				int ind2=line.get(i+1);
				int ind3=line.get(i+2);
				mapStation station1=stationList.get(ind1);
				mapStation station2=stationList.get(ind2);
				mapStation station3=stationList.get(ind3);
				double dis1=stationDis.get(ind1+","+ind2);
				double dis2=stationDis.get(ind2+","+ind3);
				double dis3=stationDis.get(ind1+","+ind3);
				String dis=dis1+","+dis2+","+dis3;
				String out1=station1.getStationName()+","+station1.getListSquare().get(0).getLat()+","+station1.getListSquare().get(0).getLng();
				String out2=station2.getStationName()+","+station2.getListSquare().get(0).getLat()+","+station2.getListSquare().get(0).getLng();
				String out3=station3.getStationName()+","+station3.getListSquare().get(0).getLat()+","+station3.getListSquare().get(0).getLng();
				writer.write(out1+";"+out2+";"+out3+";"+dis+"\n");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * �����շ�վ������list������в�������
	 * @param listNd
	 * @param stationDis
	 * @param index
	 */
	public static void sort(ArrayList<Integer> listNd,Map<String,Double> stationDis,int index){
		for(int i=1;i<listNd.size();i++){
			for(int j=i;j>0;j--){
				if(stationDis.get(index+","+listNd.get(j-1))>stationDis.get(index+","+listNd.get(j))){
					swap(listNd,j-1,j);
				}
			}
		}
	}
	/**
	 * �ݹ��㷨������line
	 * @param line
	 * @param listNd
	 * @param size
	 * @param stationDis
	 * @param index
	 * @param ndAdj
	 */
	public static void readLineRecursion(ArrayList<Integer> line,ArrayList<Integer> listNd,int size,Map<String,Double> stationDis,int[] ndAdj){
		int ori=line.size()-1;
		int oriNumber=line.get(ori);
		if(listNd.isEmpty()){
			for(int i=1;i<=size;i++){
				if(!line.contains(i)&&stationDis.containsKey(oriNumber+","+i)){
					listNd.add(i);
				}
			}
			sort(listNd,stationDis,oriNumber);
			System.out.println(listNd);
		}
		if(!listNd.isEmpty()&&ndAdj[0]<listNd.size()){
			if(line.size()>=2){
				String key1=line.get(ori-1)+","+line.get(ori);
				String key2=line.get(ori)+","+listNd.get(ndAdj[0]);
				String key3=line.get(ori-1)+","+listNd.get(ndAdj[0]);
				if(stationDis.containsKey(key1)&&stationDis.containsKey(key2)&&stationDis.containsKey(key3)){
					double dis1=stationDis.get(key1);
					double dis2=stationDis.get(key2);
					double dis3=stationDis.get(key3);
					double disDiff=Math.abs(dis3-dis1-dis2);
					if(disDiff<=3){
						line.add(listNd.get(ndAdj[0]));
						listNd.clear();
						ndAdj[0]=0;
						readLineRecursion(line,listNd,size,stationDis,ndAdj);
					}else{
						ndAdj[0]++;
						readLineRecursion(line,listNd,size,stationDis,ndAdj);
					}
				}else{
					ndAdj[0]++;
					readLineRecursion(line,listNd,size,stationDis,ndAdj);
				}
			}else{
				line.add(listNd.get(ndAdj[0]));
				listNd.clear();
				ndAdj[0]=0;
				readLineRecursion(line,listNd,size,stationDis,ndAdj);
			}
		}
	}
	/**
	 * �ǵݹ��㷨������line
	 * @param line
	 * @param listNd
	 * @param size
	 * @param stationDis
	 */
	public static void readLineNoneRecursion(ArrayList<Integer> line,ArrayList<Integer> listNd,int size,Map<String,Double> stationDis){
		int ori=0,ndAdj=0;
		System.out.println(listNd);
		while(ndAdj<listNd.size()&&!listNd.isEmpty()){
			String key1,key2,key3;
			if(line.size()>=2){
				key1=line.get(ori-1)+","+line.get(ori);
				key2=line.get(ori)+","+listNd.get(ndAdj);
				key3=line.get(ori-1)+","+listNd.get(ndAdj);
			}else{
				key1=line.get(ori)+","+listNd.get(ndAdj);
				key2=listNd.get(ndAdj)+","+listNd.get(ndAdj+1);
				key3=line.get(ori)+","+listNd.get(ndAdj+1);
			}
			if(stationDis.containsKey(key1)&&stationDis.containsKey(key2)&&stationDis.containsKey(key3)){
				double dis1=stationDis.get(key1);
				double dis2=stationDis.get(key2);
				double dis3=stationDis.get(key3);
				double disDiff=Math.abs(dis3-dis1-dis2);
				if(disDiff<=4){
					int newIndex=listNd.get(ndAdj);
					line.add(newIndex);
					ori++;
					listNd.clear();
					for(int i=1;i<=size;i++){
						if(!line.contains(i)&&stationDis.containsKey(newIndex+","+i)){
							listNd.add(i);
						}
					}
					sort(listNd,stationDis,newIndex);
					System.out.println(listNd);
					ndAdj=0;
				}else{
					ndAdj++;
				}
			}else{
				ndAdj++;
			}
		}
	}
	/**
	 * ��ȡ���ݣ�����һ�����ϵ��շ�վ����
	 * @param pName
	 * @param poiMid
	 * @param cmid
	 * @param cmif
	 * @param shortPath
	 * @param gps
	 */
	public static ArrayList<Integer> getLine(String pName,String poiMid,String cmid,String cmif,String shortPath,String gps){
		Map<Integer,mapStation> stationList=getStation(pName,poiMid,cmid,cmif);
		Map<String,Double> shortestPath=getShortest(shortPath);
		Map<String,Double> stationDis=getEachStationDistance(stationList,shortestPath);
		
		System.out.println("�շ�վ������"+stationList.size());
		System.out.println("�շ�վ֮����̾��������"+stationDis.size());
		
		for(int in:stationList.keySet()){
			System.out.println(stationList.get(in));
		}
		int size=stationList.size();//�շ�վ����
		int index=getIndex(gps,stationList);//�߽�վ�������
		System.out.println("index:"+index);
		ArrayList<Integer> line=new ArrayList<>();
		ArrayList<Integer> listNd=new ArrayList<>();
		line.add(index);
		for(int i=1;i<=size;i++){
			if(!line.contains(i)&&stationDis.containsKey(index+","+i)){
				listNd.add(i);
			}
		}
		sort(listNd,stationDis,index);
		int[] ndAdj=new int[1];//�ݹ鷽��
		readLineRecursion(line,listNd,size,stationDis,ndAdj);//�ݹ鷽��
//		readLineNoneRecursion(line,listNd,size,stationDis);//�ǵݹ鷽��
		for(int i=0;i<line.size();i++){
			System.out.println(line.get(i)+":"+stationList.get(line.get(i)));
		}
		BufferedWriter writer=io.getWriter("G:/����/�շ�վƥ��/line3.csv", "GBK");
		try{
			writeLine(line,stationList,writer);
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(line);
		return line;
	}
	
	public static ArrayList<Integer> getLine2(Map<Integer,mapStation> stationList,Map<String,Double> shortestPath,Map<String,Double> stationDis,String gps){
		
		System.out.println("�շ�վ������"+stationList.size());
		System.out.println("�շ�վ֮����̾��������"+stationDis.size());
		
		for(int in:stationList.keySet()){
			System.out.println(stationList.get(in));
		}
		int size=stationList.size();//�շ�վ����
		int index=getIndex(gps,stationList);//�߽�վ�������
		System.out.println("index:"+index);
		ArrayList<Integer> line=new ArrayList<>();
		ArrayList<Integer> listNd=new ArrayList<>();
		line.add(index);
		for(int i=1;i<=size;i++){
			if(!line.contains(i)&&stationDis.containsKey(index+","+i)){
				listNd.add(i);
			}
		}
		sort(listNd,stationDis,index);
		int[] ndAdj=new int[1];//�ݹ鷽��
		readLineRecursion(line,listNd,size,stationDis,ndAdj);//�ݹ鷽��
//		readLineNoneRecursion(line,listNd,size,stationDis);//�ǵݹ鷽��
		for(int i=0;i<line.size();i++){
			System.out.println(line.get(i)+":"+stationList.get(line.get(i)));
		}
//		writeLine(line,stationList,"G:/����/�շ�վƥ��/�����շ�վ���/line4.csv");
		System.out.println(line);
		return line;
	}
	
	public static void write3Station(String pName,String poiMid,String cmid,String cmif,String shortPath){
		String[] a={"29.38522,105.42412","28.65880,106.77425","30.78963,107.44304"};
		Map<Integer,mapStation> stationList=getStation(PNamechongqingMid,POIchongqingMid,cChongqingMid,cChongqingMif);
		Map<String,Double> shortestPath=getShortest(shortPath);
		Map<String,Double> stationDis=getEachStationDistance(stationList,shortestPath);
		try{
			BufferedWriter writer=io.getWriter("G:/����/�շ�վƥ��/�����շ�վ���/line.csv", "GBK");
			for(int i=0;i<a.length;i++){
				String gps=a[i];
				ArrayList<Integer> line=getLine2(stationList,shortestPath,stationDis,gps);
				writeLine2(line,stationList,stationDis,writer);
				writer.flush();
			}
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Integer> getRandom3PLine(Map<Integer,mapStation> stationList,Map<String,Double> stationDis,int index){
		int size=stationList.size();
		ArrayList<Integer> listNd=new ArrayList<>();
		ArrayList<Integer> line=new ArrayList<>();
		System.out.println(index);
		line.add(index);
		for(int i=1;i<=size;i++){
			if(i!=index&&stationDis.containsKey(index+","+i)){
				listNd.add(i);
			}
		}
		sort(listNd,stationDis,index);
		if(listNd.size()>0){
			line.add(listNd.get(0));
			listNd.clear();
			int k1=line.get(0);
			int k2=line.get(1);
			for(int i=1;i<=size;i++){
				if(!line.contains(i)&&stationDis.containsKey(k2+","+i)){
					listNd.add(i);
				}
			}
			sort(listNd,stationDis,k2);
			if(listNd.size()>0){
				for(int i=0;i<listNd.size();i++){
					int k3=listNd.get(i);
					if(stationDis.containsKey(k1+","+k2)&&stationDis.containsKey(k2+","+k3)&&stationDis.containsKey(k1+","+k3)){
						double d1=stationDis.get(k1+","+k2);
						double d2=stationDis.get(k2+","+k3);
						double d3=stationDis.get(k1+","+k3);
						double f=Math.abs(d3-d1-d2);
						if(f<=4){
							line.add(k3);
							break;
						}
					}
				}
			}
		}
		return line;
	}
	public static void random3PLine(String pName,String poiMid,String cmid,String cmif,String shortPath){
		Map<Integer,mapStation> stationList=getStation(pName,poiMid,cmid,cmif);
		Map<String,Double> shortestPath=getShortest(shortPath);
		Map<String,Double> stationDis=getEachStationDistance(stationList,shortestPath);
		int count=0;
		try{
			int size=stationList.size();
			BufferedWriter writer=io.getWriter("G:/����/�շ�վƥ��/����/line.csv", "GBK");
			for(int i=1;i<=size;i++){
				ArrayList<Integer> line=getRandom3PLine(stationList,stationDis,i);
				System.out.println(line);
				if(line.size()==3){
//					writeLine2(line,stationList,stationDis,writer);
					writeLine(line,stationList,writer);
				}else{
					count++;
				}
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("û���ҵ�3����һ�����ϵ��շ�վ����"+count);
	}
	public static void main(String[] args){
//		getLine(PNamechongqingMid,POIchongqingMid,cChongqingMid,cChongqingMif,shortestPath,"29.55733,106.28747");
//		write3Station(PNamechongqingMid,POIchongqingMid,cChongqingMid,cChongqingMif,shortestPath);
//		random3PLine(PNamechongqingMid,POIchongqingMid,cChongqingMid,cChongqingMif,shortestPath);
	}
}
