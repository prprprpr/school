package data;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dao.io;

public class selection {
	private static String top10ServiceArea="C:/Users/pengrui/Desktop/新建文件夹/top10ServiceArea.csv";
	private static String outMapServiceArea="D:/毕设相关/serviceAreaWeight.csv";
	private static String outMapWuliu="D:/毕设相关/wuliuStop.csv";
	private static String outMapWuliuWeight="D:/毕设相关/wuliuWeight.csv";
	
	public static Map<String,String> readMap(String in){
		Map<String,String> map=new HashMap<>();
		try{
			BufferedReader reader=io.getReader(in, "gbk");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				map.put(data[0], data[1]+","+data[2]);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	public static Map<String,Double> readMapWeight(String in){
		Map<String,Double> map=new HashMap<>();
		try{
			BufferedReader reader=io.getReader(in, "gbk");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				map.put(data[0], Double.parseDouble(data[1]));
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	public static double satisfaction(double distance){
		double satisfaction=0;
		double rmin=20000;
		double rmax=30000;
		if(distance<=rmin){
			satisfaction=1;
		}else if(distance<rmax&&distance>rmin){
			satisfaction=1-Math.pow((distance-rmin)/(rmax-rmin), 2);
		}
//		return (double)Math.round(satisfaction*1000)/1000;
		return satisfaction;
	}
	public static double satisfaction1(double distance){
		double satisfaction=0;
		double r=20000;
		if(distance<=r){
			satisfaction=1;
		}else{
			satisfaction=1/(1+(0.000005*Math.pow(distance-r, 2)));
		}
//		return (double)Math.round(satisfaction*1000)/1000;
		return satisfaction;
	}
	public static double getSatisfaction(String serviceArea,String wuliu,String outMapWuliuWeight){
		double rs=0;
		Map<String,String> mapServiceArea=readMap(serviceArea);
		Map<String,String> mapWuliu=readMap(wuliu);
		Map<String,Double> mapWuliuWeight=readMapWeight(outMapWuliuWeight);
		Map<String,Map<String,Double>> map=new HashMap<>();
		for(String serviceAreaId:mapServiceArea.keySet()){
			String serviceAreaLat=mapServiceArea.get(serviceAreaId).split(",",2)[0];
			String serviceAreaLng=mapServiceArea.get(serviceAreaId).split(",",2)[1];
			for(String wuliuId:mapWuliu.keySet()){
				String wuliuLat=mapWuliu.get(wuliuId).split(",",2)[0];
				String wuliuLng=mapWuliu.get(wuliuId).split(",",2)[1];
				double distance=gps.getDistance(Double.parseDouble(serviceAreaLng), Double.parseDouble(serviceAreaLat), Double.parseDouble(wuliuLng), Double.parseDouble(wuliuLat));
//				System.out.println(seviceAreaId+","+wuliuId+":"+satisfaction(distance));
				double s=satisfaction1(distance);
				double weight=mapWuliuWeight.get(wuliuId);
				if(map.containsKey(wuliuId)){
					Map<String,Double> mapIn=map.get(wuliuId);
					if(s==0) continue;
					mapIn.put(serviceAreaId, Math.max(s*weight, mapIn.getOrDefault(serviceAreaId, 0.0)));
					map.put(wuliuId, mapIn);
				}else{
					Map<String,Double> mapIn=new HashMap<>();
					if(s==0) continue;
					mapIn.put(serviceAreaId, s*weight);
					map.put(wuliuId, mapIn);
				}
			}
		}
		Set<String> set=new HashSet<>();
		for(String k:map.keySet()){
			String serviceId="";
			double max=Double.MIN_VALUE;
			Map<String,Double> mapIn=map.get(k);
			for(String key:mapIn.keySet()){
				if(max<mapIn.get(key)){
					max=mapIn.get(key);
					serviceId=key;
				}
			}
			set.add(serviceId);
			System.out.println(k+":"+serviceId+","+max);
		}
		System.out.println(map.size());
		System.out.println(set.size());
		for(String serviceId:set){
			System.out.println(serviceId+","+mapServiceArea.get(serviceId));
		}
		return rs;
	}
	
	public static void main(String[] args){
		getSatisfaction(top10ServiceArea,outMapWuliu,outMapWuliuWeight);
	}
}
