package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import dao.io;
import dao.point;
import dao.Trace;
import dao.Trip;
import dao.dbscan;

public class reParseData {
	private static String cqTollDataOut="D:/货车轨迹数据分析/cqTollDataOut.csv";
	private static String matchedId="D:/matchId.csv";
	private static String matchedData="D:/matchedData.csv";
	private static String idStop="D:/货车轨迹数据分析/id停留点";
	private static String top10ServiceArea="C:/Users/pengrui/Desktop/新建文件夹/top10ServiceArea.csv";
	
	public static Set<String> getCar(String matchedId){
		Set<String> carSet=new HashSet<>();
		try{
			BufferedReader reader=io.getReader(matchedId, "gbk");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				String plate=data[1];
				String cx=data[2];
				carSet.add(plate+","+cx);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return carSet;
	}
	public static Map<String,Integer> getCarOdCount(String cqTollDataOut,String matchedId,Map<String,Integer> mapCarMinWeight){
		Map<String,Integer> map=new HashMap<>();
		Set<String> carSet=getCar(matchedId);
		Map<String,Map<String,String>> mapCarOdWeightCount=new HashMap<>();
		try{
			BufferedReader reader=io.getReader(cqTollDataOut, "gbk");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				String plate=data[1];
				String cx=data[2];
				String car=plate+","+cx;
				if(carSet.contains(car)){
					String inStation=data[5];
					String outStation=data[6];
					String od=inStation+","+outStation;
					int weight=Integer.valueOf(data[7]);
					if(mapCarMinWeight.containsKey(car)){
						int oldWeight=mapCarMinWeight.get(car);
						if(oldWeight>weight){
							mapCarMinWeight.put(car, weight);
						}
					}else{
						mapCarMinWeight.put(car, weight);
					}
					
					if(mapCarOdWeightCount.containsKey(car)){
						Map<String,String> mapOdWeight=mapCarOdWeightCount.get(car);
						if(mapOdWeight.containsKey(od)){
							String weightAndCount=mapOdWeight.get(od);
							int newWeight=Integer.valueOf(weightAndCount.split(",")[0])+weight;
							int newCount=Integer.valueOf(weightAndCount.split(",")[1])+1;
							mapOdWeight.put(od, newWeight+","+newCount);
						}else{
							mapOdWeight.put(od, weight+","+1);
						}
						mapCarOdWeightCount.put(car, mapOdWeight);
					}else{
						Map<String,String> mapOdWeight=new HashMap<>();
						mapOdWeight.put(od, weight+","+1);
						mapCarOdWeightCount.put(car, mapOdWeight);
					}
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		Map<String,Map<String,Integer>> mapCarOdContribution=new HashMap<>();
		Map<String,Integer> mapCarContribution=new HashMap<>();
		for(String car:mapCarOdWeightCount.keySet()){
			Map<String,String> mapOdWeight=mapCarOdWeightCount.get(car);
			Map<String,Integer> mapOdContribution=new HashMap<>();
			int minWeight=mapCarMinWeight.get(car);
			int carContribution=0;
			for(String od:mapOdWeight.keySet()){
				String weightAndCount=mapOdWeight.get(od);
				int weight=Integer.valueOf(weightAndCount.split(",")[0]);
				int count=Integer.valueOf(weightAndCount.split(",")[1]);
				int contribution=weight-minWeight*count;
				carContribution+=contribution;
				mapOdContribution.put(od, contribution);
			}
			mapCarContribution.put(car, carContribution);
			mapCarOdContribution.put(car, mapOdContribution);
		}
		List<Map.Entry<String,Integer>> listCarContribution=new ArrayList<>();
		listCarContribution.addAll(mapCarContribution.entrySet());
		Collections.sort(listCarContribution,new Comparator<Map.Entry<String, Integer>>(){
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				// TODO Auto-generated method stub
				if(o1.getValue()<o2.getValue()){
					return 1;
				}else if(o1.getValue()>o2.getValue()){
					return -1;
				}else{
					return 0;
				}
			}			
		});
		double amount=0;
		for(int i=0;i<listCarContribution.size();i++){
			Map.Entry<String,Integer> entry=listCarContribution.get(i);
			String car=entry.getKey();
			int weight=entry.getValue();
			amount+=weight;
		}
		double countWeight=0;
		int countCar=0;
		for(int i=0;i<listCarContribution.size();i++){
			Map.Entry<String,Integer> entry=listCarContribution.get(i);
			String car=entry.getKey();
			int weight=entry.getValue();
			countWeight+=weight;
			if(countWeight/amount<0.8){
				countCar++;
				map.put(car, mapCarMinWeight.get(car));
			}
		}
		System.out.println("总重占前80%的车数："+countCar);
		return map;
	}
	
	public static ArrayList<String> readTop10ServiceArea(String in){
		ArrayList<String> list=new ArrayList<>();
		try{
			BufferedReader reader=io.getReader(in, "gbk");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				list.add(data[0]);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	public static Map<String,Map<Integer,ArrayList<String>>> readIdStop(String idStop){
		Map<String,Map<Integer,ArrayList<String>>> mapIdStop=new HashMap<>();
		try{
			File file=new File(idStop);
			List<String> listFile=Arrays.asList(file.list());
			for(int i=0;i<listFile.size();i++){
				String id=listFile.get(i).split("\\.")[0];
				String path=idStop+"/"+listFile.get(i);
				Map<Integer,ArrayList<String>> mapIndexStop=new HashMap<>();
				BufferedReader reader=io.getReader(path, "gbk");
				String line="";
				while((line=reader.readLine())!=null){
					String[] data=line.split(":");
					String gps=data[0];
					String indexs=data[1];
					String[] index=indexs.split(",");
					for(int j=0;j<index.length;j++){
						if(mapIndexStop.containsKey(Integer.valueOf(index[j]))){
							ArrayList<String> listStop=mapIndexStop.get(Integer.valueOf(index[j]));
							listStop.add(gps);
							mapIndexStop.put(Integer.valueOf(index[j]), listStop);
						}else{
							ArrayList<String> listStop=new ArrayList<>();
							listStop.add(gps);
							mapIndexStop.put(Integer.valueOf(index[j]), listStop);
						}
					}
				}
				reader.close();
				mapIdStop.put(id, mapIndexStop);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return mapIdStop;
	}
	public static void readMatchedData(String matchedData,String cqTollDataOut,String matchedId,String top10ServiceArea,String idStop) throws IOException{
		Map<String,Integer> mapCarMinWeight=new HashMap<>();
		Map<String,Integer> mapCar=getCarOdCount(cqTollDataOut,matchedId,mapCarMinWeight);
		ArrayList<String> listServiceArea=readTop10ServiceArea(top10ServiceArea);
		Map<String,Map<Integer,ArrayList<String>>> mapIdStop=readIdStop(idStop);
		
		ArrayList<Trip> trips=new ArrayList<>();
		int amount=0;
		try{
			BufferedReader reader=io.getReader(matchedData, "gbk");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(";");
				String idAndCar=data[0];
				String id=idAndCar.split(",")[0];
				String car=idAndCar.split(",")[1]+","+idAndCar.split(",")[2];
				if(mapCar.containsKey(car)&&mapIdStop.containsKey(id)){
					Map<Integer,ArrayList<String>> mapIndexStop=mapIdStop.get(id);
					String traces=data[1];
					String[] trace=traces.split("\\|");
					ArrayList<Trace> listTrace=new ArrayList<>();
					for(int i=0;i<trace.length;i++){
						String[] message=trace[i].split(",");
						if(message.length==7){
							Trace t=new Trace(message[0],message[1],message[2],message[3],Integer.valueOf(message[4]),message[5],message[6]);
							listTrace.add(t);
						}else if(message.length==5){
							Trace t=new Trace(message[0],message[1],message[2],message[3],Integer.valueOf(message[4]));
							listTrace.add(t);
						}
					}
					for(int i=0;i<listTrace.size()-1;i++){
						Trace t=listTrace.get(i);
						if(t.isPassServiceArea()){
							amount++;
							if(mapIndexStop.containsKey(i)){
								int index=i-1;
								while(index>0&&!mapIndexStop.containsKey(index)){
									index--;
								}
								if(index>0){
									Trace t1=listTrace.get(i+1);
									Trip trip=new Trip(id,mapIndexStop.get(index),t.getServiceArea(),mapIndexStop.get(i),t1.getWeight()-t.getWeight());
									trips.add(trip);
								}
							}
						}
					}
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("总行程数："+amount);
		System.out.println("行程数："+trips.size());
		int count=0;
//		BufferedWriter writer=io.getWriter("C:/Users/pengrui/Desktop/新建文件夹/装卸货/xieHuo.csv", "gbk");
		ArrayList<point> listPoints=new ArrayList<>();
		Map<Integer,ArrayList<String>> mapWuliu=new HashMap<>();//物流园包含的停留点
		Map<String,ArrayList<Integer>> mapServiceArea=new HashMap<>();
		for(Trip t:trips){
			if(listServiceArea.contains(t.getServiceArea())){
				count++;
				if(t.getWeight()<-10000){
					ArrayList<String> list=t.getEndGps();
					for(String gps:list){
						String[] data=gps.split(",");
						String lat=data[0];
						String lng=data[1];
						point p=new point(Double.parseDouble(lat),Double.parseDouble(lng));
						listPoints.add(p);
					}
					String serviceAreaId=t.getServiceArea();
					int weight=t.getWeight();
					if(mapServiceArea.containsKey(serviceAreaId)){
						ArrayList<Integer> listWeight=mapServiceArea.get(serviceAreaId);
						listWeight.add(weight);
						mapServiceArea.put(serviceAreaId, listWeight);
					}else{
						ArrayList<Integer> listWeight=new ArrayList<>();
						listWeight.add(weight);
						mapServiceArea.put(serviceAreaId, listWeight);
					}
				}
			}
			System.out.println(t);
		}
		dbscan d=new dbscan(500,20);
		d.process(listPoints);
		for(point p:listPoints){
			if(!p.isNoised()){
				if(mapWuliu.containsKey(p.getCluster())){
					ArrayList<String> listGps=mapWuliu.get(p.getCluster());
					listGps.add(p.getX()+","+p.getY());
					mapWuliu.put(p.getCluster(), listGps);
				}else{
					ArrayList<String> listGps=new ArrayList<>();
					listGps.add(p.getX()+","+p.getY());
					mapWuliu.put(p.getCluster(), listGps);
				}
			}
		}
		Map<Integer,ArrayList<Integer>> mapWuliuWeight=new HashMap<>();//物流园包含的总货物量
		for(Trip t:trips){
			if(listServiceArea.contains(t.getServiceArea())){
				count++;
				if(t.getWeight()<-10000){
					ArrayList<String> gpsList=t.getEndGps();
					int weight=t.getWeight();
					here:
					for(String gps:gpsList){
						for(Integer k:mapWuliu.keySet()){
							ArrayList<String> value=mapWuliu.get(k);
							if(value.contains(gps)){
								if(mapWuliuWeight.containsKey(k)){
									ArrayList<Integer> listWeight=mapWuliuWeight.get(k);
									listWeight.add(weight);
									mapWuliuWeight.put(k, listWeight);
								}else{
									ArrayList<Integer> listWeight=new ArrayList<>();
									listWeight.add(weight);
									mapWuliuWeight.put(k, listWeight);
								}
								break here;
							}
						}
					}
				}
			}
			System.out.println(t);
		}
		System.out.println("前10服务区行程数："+count);
		String outMapServiceArea="D:/毕设相关/serviceAreaWeight.csv";
		String outMapWuliu="D:/毕设相关/wuliuStop.csv";
		String outMapWuliuWeight="D:/毕设相关/wuliuWeight.csv";
		writeMapServiceArea(mapServiceArea,outMapServiceArea);
		writeMapWuliu(mapWuliu,outMapWuliu);
		writeMapWuliuWeight(mapWuliuWeight,outMapWuliuWeight);
//		secondDbscan("C:/Users/pengrui/Desktop/新建文件夹/装卸货/xieHuo.csv","C:/Users/pengrui/Desktop/新建文件夹/装卸货/xieHuo2.csv");
	}
	public static void writeMapServiceArea(Map<String,ArrayList<Integer>> mapServiceArea,String out){
		try{
			BufferedWriter writer=io.getWriter(out,"gbk");
			for(String k:mapServiceArea.keySet()){
				ArrayList<Integer> list=mapServiceArea.get(k);
				int sum=0;
				for(int v:list){
					sum+=Math.abs(v);
				}
				int avg=sum/list.size();
				writer.write(k+","+avg+"\n");
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void writeMapWuliu(Map<Integer,ArrayList<String>> mapWuliu,String out){
		try{
			BufferedWriter writer=io.getWriter(out,"gbk");
			for(int k:mapWuliu.keySet()){
				ArrayList<String> list=mapWuliu.get(k);
				writer.write(k+","+getCenterPoint(list)+"\n");
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void writeMapWuliuWeight(Map<Integer,ArrayList<Integer>> mapWuliuWeight,String out){
		try{
			BufferedWriter writer=io.getWriter(out,"gbk");
			for(int k:mapWuliuWeight.keySet()){
				ArrayList<Integer> list=mapWuliuWeight.get(k);
				int sum=0;
				for(int v:list){
					sum+=Math.abs(v);
				}
				int avg=sum/list.size();
				writer.write(k+","+avg+"\n");
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static String getCenterPoint(ArrayList<String> points){
		Set<Integer> set=new HashSet<>();
		int total=points.size();
		double lat=0;
		double lng=0;
		for(int i=0;i<points.size();i++){
			lat+=Double.parseDouble(points.get(i).split(",")[0])*Math.PI/180;
			lng+=Double.parseDouble(points.get(i).split(",")[1])*Math.PI/180;
		}
		lat/=total;
		lng/=total;
		String index="";
		for(int ind:set){
			index+=ind+",";
		}
		return lat*180/Math.PI+","+lng*180/Math.PI;
	}
	public static void secondDbscan(String in,String out){
		BufferedReader reader=io.getReader(in, "gbk");
		try{
			String line="";
			ArrayList<point> list=new ArrayList<>();
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				String lat=data[0];
				String lng=data[1];
				point p=new point(Double.parseDouble(lat),Double.parseDouble(lng));
				list.add(p);
			}
			reader.close();
			dbscan d=new dbscan(500,20);
			d.process(list);
			Map<Integer,ArrayList<String>> map=new HashMap<>();
			for(point p:list){
				if(!p.isNoised()){
					if(map.containsKey(p.getCluster())){
						ArrayList<String> listGps=map.get(p.getCluster());
						listGps.add(p.getX()+","+p.getY());
						map.put(p.getCluster(), listGps);
					}else{
						ArrayList<String> listGps=new ArrayList<>();
						listGps.add(p.getX()+","+p.getY());
						map.put(p.getCluster(), listGps);
					}
				}
			}
			BufferedWriter writer=io.getWriter(out, "gbk");
			for(int key:map.keySet()){
				ArrayList<String> listGps=map.get(key);
				for(String gps:listGps){
					writer.write(gps+"\n");
				}
				System.out.println(key+","+listGps);
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException{
		readMatchedData(matchedData,cqTollDataOut,matchedId,top10ServiceArea,idStop);
	}
}
