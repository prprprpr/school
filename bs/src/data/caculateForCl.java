package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dao.io;

public class caculateForCl {
	private static String in="D:\\货车轨迹数据分析\\经过高速的id/";
	private static String matchId="D:/matchId.csv";
	private static String huoyueDay="C:/Users/pengrui/Desktop/每辆车在在这周内都活跃了多少天.csv";
	private static String pointCount="C:/Users/pengrui/Desktop/每辆车平均每天的轨迹点数.csv";
	private static String huoyueCar="C:/Users/pengrui/Desktop/分时段的每小时内活跃的车辆数.csv";
	public static void read(String in,String matchId){
		try{
			BufferedWriter writer1=io.getWriter(huoyueDay, "gbk");
			BufferedWriter writer2=io.getWriter(pointCount, "gbk");
			BufferedWriter writer3=io.getWriter(huoyueCar, "gbk");
			
			Map<String,String> mapIdCar=new HashMap<>();
			BufferedReader reader=io.getReader(matchId, "gbk");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",3);
				String id=data[0];
				String plate=data[1];
				String cx=data[2];
				if(Integer.parseInt(cx)<5) continue;
				mapIdCar.put(id, plate+","+cx);
			}
			reader.close();
			File file=new File(in);
			List<String> listFile=Arrays.asList(file.list());
			Map<String,Set<String>> mapTime=new HashMap<>();
			Map<Integer,Integer> mapDayCount=new HashMap<>();
			Map<Integer,Integer> mapPointCount=new HashMap<>();
			for(String p:listFile){
				String path=in+p;
				String id=p.split("\\.")[0];
				if(!mapIdCar.containsKey(id)) continue;
				String car=mapIdCar.get(id);
				reader=io.getReader(path, "gbk");
				line="";
				int countPoint=0;
				Set<String> setDay=new HashSet<>();
				Set<String> setHour=new HashSet<>();
				while((line=reader.readLine())!=null){
					String[] data=line.split(",", 5);
					String time=data[0];
					int v=Integer.valueOf(data[3]);
					++countPoint;
					String day=time.split(" ")[0];
					String hour=time.split(":")[0];
					if(v!=0){
						setHour.add(hour);
						mapTime.put(car, setHour);
					}
					setDay.add(day);
				}
				int day=setDay.size();
				mapDayCount.put(day, mapDayCount.getOrDefault(day, 0)+1);
				int pointCount=(countPoint/day)%100*100;
				mapPointCount.put(pointCount, mapPointCount.getOrDefault(pointCount, 0)+1);
				reader.close();
			}
			for(int d:mapDayCount.keySet()){
				writer1.write(d+","+mapDayCount.get(d)+"\n");
			}
			writer1.flush();
			writer1.close();
			for(int c:mapPointCount.keySet()){
				writer2.write(c+","+mapPointCount.get(c)+"\n");
			}
			writer2.flush();
			writer2.close();
			Map<String,Integer> map=new HashMap<>();
			for(String car:mapTime.keySet()){
				Set<String> set=mapTime.get(car);
				for(String s:set){
					String h=s.split(" ")[1];
					map.put(h, map.getOrDefault(h, 0)+1);
				}
			}
			for(String key:map.keySet()){
				int v=map.get(key);
				writer3.write(key+","+v+"\n");
			}
			writer3.flush();
			writer3.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void read1(String in){
		try{
			BufferedReader reader=io.getReader(in, "gbk");
			String line="";
			int[] c=new int[10];
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",2);
				int count=Integer.parseInt(data[0]);
				int countCar=Integer.parseInt(data[1]);
				c[count/1000]+=countCar;
			}
			reader.close();
			for(int i=0;i<c.length;i++){
				System.out.println(i+","+c[i]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void readTraceReLi(String in,String out){
		BufferedWriter writer=io.getWriter(out, "gbk");
		try{
			writer.write("data=[["+"\n");
			
			Map<String,String> mapIdCar=new HashMap<>();
			BufferedReader reader=io.getReader(matchId, "gbk");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",3);
				String id=data[0];
				String plate=data[1];
				String cx=data[2];
				if(Integer.parseInt(cx)<5) continue;
				mapIdCar.put(id, plate+","+cx);
			}
			reader.close();
			
			File file=new File(in);
			List<String> listFile=Arrays.asList(file.list());
			for(String p:listFile){
				String path=in+p;
				String id=p.split("\\.")[0];
				if(!mapIdCar.containsKey(id)) continue;
				reader=io.getReader(path, "gbk");
				line="";
				Set<String> set=new HashSet<>();
				while((line=reader.readLine())!=null){
					String[] data=line.split(",");
					double[] point=gps.gcj02_To_Bd09(Double.parseDouble(data[2]), Double.parseDouble(data[1]));
					String lat=""+(double)Math.round(point[0]*100)/100;
					String lng=""+(double)Math.round(point[1]*100)/100;
					set.add(lng+","+lat);
				}
				reader.close();
				int i=0;
				for(String s:set){
					if(i%300==0){
						writer.write("{\"coord\":["+s+"],\"elevation\":1},"+"\n");
					}
					i++;
				}
			}
			writer.write("]]"+"\n");
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		read(in,matchId);
//		read1(pointCount);
//		readTraceReLi(in,"E:/echarts/mok/reli.json");
	}
}
