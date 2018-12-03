package end;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.ClusterAnalysis;
import dao.DataPoint;
import dao.io;
import data.analysis;
import data.gps;

public class document {
	private static String idStop="D:/货车轨迹数据分析/id停留点";
	private static String allStop="H:/毕业论文/中期/数据/allStop.csv";
	private static String matchedData="D:/matchedData.csv";
	public static void readAllStopPoints(String in,String out){
		File file=new File(in);
		List<String> listFile=Arrays.asList(file.list());
		BufferedWriter writer=io.getWriter(out, "GBK");
		try{
			for(String path:listFile){
				BufferedReader reader=io.getReader(in+"/"+path, "GBK");
				String line="";
				while((line=reader.readLine())!=null){
					String gps=line.split(":")[0];
					writer.write(gps+"\n");
				}
				reader.close();
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void cluster(String in,String out){
		ClusterAnalysis ca = new ClusterAnalysis();
		ArrayList<String> stayCenterPoints=new ArrayList<>();
		ArrayList<DataPoint> points=new ArrayList<>();
		try{
			String line="";
			BufferedReader reader=io.getReader(in, "gbk");
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				String lat=data[0];
				String lng=data[1];
				DataPoint dataPoint=new DataPoint();
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		List<DataPoint> dps = ca.startAnalysis(points, 500, 2,stayCenterPoints);
	}
	
	public static void getWuLiuTu(String in,String out){
		BufferedWriter writer=io.getWriter(out, "gbk");
		try{
			writer.write("data=[["+"\n");
			BufferedReader reader=io.getReader(in, "gbk");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				double[] p=gps.gcj02_To_Bd09(Double.parseDouble(data[1]), Double.parseDouble(data[0]));
				String lat=""+p[0];
				String lng=""+p[1];
				writer.write("{\"coord\":["+lng+","+lat+"],\"elevation\":1},"+"\n");
			}
			reader.close();
			writer.write("]]"+"\n");
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void getTu(String xieHuo2,String top10ServiceArea,String out){
		Map<String,String> map=new HashMap<>();
		Map<String,ArrayList<String>> rs=new HashMap<>();
		try{
			BufferedReader reader=io.getReader(top10ServiceArea,"gbk");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				map.put(data[1]+","+data[2], data[0]);
			}
			reader.close();
			reader=io.getReader(xieHuo2, "gbk");
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				String lat=data[0];
				String lng=data[1];
				String id="";
				double min=Double.MAX_VALUE;
				for(String k:map.keySet()){
					String[] d=k.split(",");
					String latk=d[0];
					String lngk=d[1];
					double distance=gps.getDistance(Double.parseDouble(lng), Double.parseDouble(lat), Double.parseDouble(lngk), Double.parseDouble(latk));
					if(min>distance){
						min=distance;
						id=map.get(k);
					}
				}
				if(rs.containsKey(id)){
					ArrayList<String> list=rs.get(id);
					list.add(lat+","+lng);
					rs.put(id, list);
				}else{
					ArrayList<String> list=new ArrayList<String>();
					list.add(lat+","+lng);
					rs.put(id, list);
				}
			}
			reader.close();
			for(String id:rs.keySet()){
				String outpath=out+"/"+id+".csv";
				ArrayList<String> list=rs.get(id);
				BufferedWriter writer=io.getWriter(outpath, "gbk");
				for(String v:list){
					writer.write(v+"\n");
				}
				writer.flush();
				writer.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void getPPDistance(String in,String out){
		File file=new File(in);
		List<String> listFile=Arrays.asList(file.list());
		BufferedWriter writer=io.getWriter(out, "GBK");
		Map<Integer,Integer> mapDistance=new HashMap<>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			for(String path:listFile){
				BufferedReader reader=io.getReader(in+"/"+path, "GBK");
				String line="";
				double latPre=0.0;
				double lngPre=0.0;
				String timePre="";
				while((line=reader.readLine())!=null){
					String[] data=line.split(",",5);
					String time=data[0];
					double lat=Double.parseDouble(data[1]);
					double lng=Double.parseDouble(data[2]);
					if(!timePre.equals("")&&latPre!=0&&lngPre!=0){
//						if((sdf.parse(time).getTime()-sdf.parse(timePre).getTime())/1000>30) continue;
						double distance=gps.getDistance(lngPre, latPre, lng, lat);
						if(distance==0) continue;
						int d=(int)(distance/100*100)%100*100;
						mapDistance.put(d,mapDistance.getOrDefault(d, 0)+1);
					}
					latPre=lat;
					lngPre=lng;
					timePre=time;
				}
				reader.close();
			}
			for(int key:mapDistance.keySet()){
				writer.write(key+","+mapDistance.get(key)+"\n");
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException, ParseException{
//		readAllStopPoints(idStop,allStop);
//		String xieHuo="C:/Users/pengrui/Desktop/新建文件夹/装卸货/xieHuo.csv";
//		String xieHuoHeatMap="E:/echarts/mok/xieHuoHeatMap.json";
//		getWuLiuTu(xieHuo,xieHuoHeatMap);
		
//		String xieHuo2="C:/Users/pengrui/Desktop/新建文件夹/装卸货/xieHuo2.csv";
//		String top10ServiceArea="C:/Users/pengrui/Desktop/新建文件夹/top10ServiceArea.csv";
//		String out="H:/毕业论文/中期/数据/服务区与货场";
//		getTu(xieHuo2,top10ServiceArea,out);
		
		String id="D:/货车轨迹数据分析/经过高速的id";
		String outDistance="H:/毕业论文/中期/数据/pointDistance.csv";
		getPPDistance(id,outDistance);
	}
}
