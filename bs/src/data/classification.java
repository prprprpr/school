package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dao.IHandle;
import dao.bigFileReader;
import dao.io;
import dao.point;

public class classification {
	private static String outPath18Chongqing="G:/地图/收费站数据/18PoiChongqing.csv";
	private static String outPath16Chongqing="G:/地图/收费站数据/16PoiChongqing.csv";
	private static String cqStationName="G:/新建文件夹/重庆/重庆站点表.csv";
	
	private static String cqTollCollection20170526="D:/2017short/201705short.csv";
	private static String outGpsWithStationId="G:/货车轨迹数据/输出数据/20170526输出/与收费数据匹配/gpsWithStationId.csv";
	/**
	 * 读文件
	 * @param in	输入文件路径
	 * @param encoding	文件编码格式
	 * @return	BufferedReader
	 */
	public static BufferedReader getReader(String in,String encoding){
		File file=new File(in);
		BufferedReader reader=null;
		try{
			InputStreamReader input=new InputStreamReader(new FileInputStream(file),encoding);
			reader=new BufferedReader(input);
		}catch(Exception e){
			e.printStackTrace();
		}
		return reader;
	}
	/**
	 * 写文件
	 * @param out 输出文件路径
	 * @param encoding	输出文件格式
	 * @return	返回BufferedWriter
	 */
	public static BufferedWriter getWriter(String out,String encoding){
		File file=new File(out);
		BufferedWriter writer=null;
		try{
			OutputStreamWriter output=new OutputStreamWriter(new FileOutputStream(file),encoding);
			writer=new BufferedWriter(output);
		}catch(Exception e){
			e.printStackTrace();
		}
		return writer;
	}
	/**
	 * 将轨迹数据中的时间转换为标准格式
	 * @param s	输入以毫秒表示的字符串
	 * @return	输出标准格式时间字符串
	 */
	public static String stampToDate(String s){
		String rs;
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt=new Long(s);
		Date date=new Date(lt);
		rs=simpleDateFormat.format(date);
		return rs;
	}
	/**
	 * 判断经纬度是否在一定范围内
	 * @param lat	维度
	 * @param lng	经度
	 * @return 在范围内返回true，否则返回false
	 */
	public static boolean isInCity(double lat,double lng){
		if(lat>=28.142508&&lat<=32.193387&&lng>=105.283247&&lng<=110.183150){
			return true;
		}
		return false;
	}
	/**
	 * 读取原始货车轨迹数据，筛选出某个省内的轨迹数据
	 * @param path	输入文件夹路径
	 * @param outPath	输出文件夹路径
	 */
	public static void read(String path,String outPath){
		File file=new File(path);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			String pathIn=path+"/"+list.get(i);
			BufferedReader reader=getReader(pathIn,"GBK");
			BufferedWriter writer=getWriter(outPath+"/"+list.get(i)+".csv","GBK");
			try{
				String line="";
				int index=0;
				String curId="";
				boolean flag=false;
				List<String> listLine=new LinkedList<>();
				while((line=reader.readLine())!=null){
					String[] data=line.split(",",8);
					String id=data[0];
					String time=stampToDate(data[1]);
					if(!data[2].equals("")&&!data[3].equals("")){
						double ln=Double.valueOf(data[2]);
						double la=Double.valueOf(data[3]);
						double[] p=gps.transform(la, ln);
						double lat=p[0];
						double lng=p[1];
						String v=data[4];
						String fx=data[5];
						if(!curId.equals(id)){
							if(flag){
								for(int j=0;j<listLine.size();j++){
									writer.write(listLine.get(j)+"\n");
								}
								flag=false;
							}
							listLine.clear();
							curId=id;
							index++;
						}
						if(!flag){
							flag=isInCity(lat,lng);
						}
						listLine.add(index+","+id+","+time+","+lat+","+lng+","+v+","+fx);
					}
				}
				reader.close();
				writer.flush();
				writer.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println(pathIn+" read finish!");
		}
	}
	/**
	 * 将每个文件中的多个车辆路径，划分为一个一个的单独文件
	 * @param path 每一天的多个文件输入路径
	 * @param outPath	每一天的输出文件路径
	 */
	public static void readSingleTrace(String path,String outPath){
		try{
			File file=new File(path);
			List<String> list=Arrays.asList(file.list());
			for(int i=0;i<1;i++){
				String pathIn=path+"/"+list.get(i);
				BufferedReader reader=getReader(pathIn,"GBK");
				String name=list.get(i).split("\\.")[0];
				File fileOut=new File(outPath+"/"+name);
				if(!fileOut.exists()){
					fileOut.mkdirs();
				}
				Map<String,LinkedList<String>> map=new HashMap<>();
				String line="";
				while((line=reader.readLine())!=null){
					String[] data=line.split(",",7);
					String index=data[0];
					String time=data[2];
					String lat=data[3];
					String lng=data[4];
					String v=data[5];
					String direction=data[6];
					if(map.containsKey(index)){
						LinkedList<String> listLine=map.get(index);
						listLine.add(time+","+lat+","+lng+","+v+","+direction);
						map.put(index, listLine);
					}else{
						LinkedList<String> listLine=new LinkedList<>();
						listLine.add(time+","+lat+","+lng+","+v+","+direction);
						map.put(index, listLine);
					}
				}
				reader.close();
				int index=1;
				for(String key:map.keySet()){
					BufferedWriter writer=getWriter(fileOut+"/trace"+index+".csv","GBK");
					LinkedList<String> listLine=map.get(key);
					for(int j=0;j<listLine.size();j++){
						writer.write(listLine.get(j)+"\n");
					}
					writer.flush();
					writer.close();
					index++;
				}
				System.out.println("write "+pathIn+" finish!");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 读取轨迹数据返回一系列点的ArrayList
	 * @param path	轨迹数据输入路径
	 * @return	返回dbscan输入的点的集合
	 */
	public static ArrayList<point> readTrace(String path){
		ArrayList<point> rs=new ArrayList<point>();
		try{
			BufferedReader reader=getReader(path,"GBK");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",7);
				String time=data[0];
				double lng=Double.parseDouble(data[2]);
				double lat=Double.parseDouble(data[1]);
				String v=data[3];
				String direction=data[4];
				point a=new point(lng,lat,time,v,direction);
				rs.add(a);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 * 输出dbscan停留点结果
	 * @param points	经过DBSCAN算法标记后的轨迹点
	 * @param path	输出路径
	 * @throws IOException
	 */
	public static void writeStop(ArrayList<point> points,String path) throws IOException{
		Map<Integer,ArrayList<point>> map=new HashMap<>();
		for(point p:points){
			if(p.getCluster()!=0){
				if(!map.containsKey(p.getCluster())){
					ArrayList<point> stop=new ArrayList<point>();
					stop.add(p);
					map.put(p.getCluster(), stop);
				}else{
					ArrayList<point> stop=map.get(p.getCluster());
					stop.add(p);
					map.put(p.getCluster(), stop);
				}
			}
		}
		System.out.println(map.size());
		for(int key:map.keySet()){
			ArrayList<point> stop=map.get(key);
			BufferedWriter writer=getWriter(path+"/stop"+key+".csv","GBK");
			for(point p:stop){
				writer.write(p+"\n");
			}
			writer.flush();
			writer.close();
		}
	}
	/**
	 * 判断速度为0的停留点是否在一个收费站的周围
	 * @param x	轨迹点的x坐标
	 * @param y	轨迹点的y坐标
	 * @param mapGrid	收费站对应坐标的位置
	 * @return	如果轨迹点在收费站周围则返回收费站的list，如果没在返回空
	 */
	public static List<String> isPassToll(double x,double y,Map<String,List<String>> mapGrid){
		for(double i=x-1;i<=x+1;i++){
			for(double j=y-1;j<=y+1;j++){
				if(mapGrid.containsKey(i+","+j)){
					return mapGrid.get(i+","+j);
				}
			}
		}
		return null;
	}
	/**
	 * 若gps点对应上了2个收费广场，选取离该点最近的一个收费广场
	 * @param in	轨迹点经纬度，收费广场经纬度集合的字符串
	 * @return	返回轨迹点和离该轨迹点最近的收费广场的经纬度
	 */
	public static String matchGpsToll(String in){
		String[] key=in.split("\\|",2);
		double latGps=Double.parseDouble(key[0].split(";",2)[0]);
		double lngGps=Double.parseDouble(key[0].split(";",2)[1]);
		String a=key[1].replaceAll("\\[", "");
		String b=a.replaceAll("\\]", "");
		List<String> list=Arrays.asList(b.split(","));
		if(list.size()==1){
			return key[0]+","+b;
		}else{
			String tollGps="";
			double min=Double.MAX_VALUE;
			for(int i=0;i<list.size();i++){
				double latToll=Double.parseDouble(list.get(i).split(";",2)[0]);
				double lngToll=Double.parseDouble(list.get(i).split(";",2)[1]);
				double distance=gps.getDistance(lngGps, latGps, lngToll, latToll);
				if(min>distance){
					min=distance;
					tollGps=list.get(i).trim();
				}
			}
			return key[0]+","+tollGps;
		}	
	}
	/**
	 * 从轨迹数据中取出除了最终起止点之外的速度为0的点，并检测其是否为在收费站的停留
	 */
	public static void getZeroSpeedPoints(){
		double bLng=105.283247;
		double bLat=28.142508;
		try{
			Map<String,List<String>> mapGrid=getGrid("G:/地图/收费站数据/18PoiChongqing.csv");
			String path="G:/货车轨迹数据/输出数据/20170526输出/单条轨迹/part-m-00000/trace19.csv";
			ArrayList<point> trace=readTrace(path);
			dbscan d=new dbscan(200,20);
			d.process(trace);
			Map<String,String> map=new LinkedHashMap<>();
			for(int i=0;i<trace.size();i++){
				point p=trace.get(i);
				if(p.getCluster()==0&&Double.parseDouble(p.getV())==0){
					String c=getXY(bLng,bLat,p.getX(),p.getY());
					String[] cc=c.split(",",2);
					double x=Double.parseDouble(cc[0]);
					double y=Double.parseDouble(cc[1]);
					List<String> tollStation=isPassToll(x,y,mapGrid);
					if(tollStation!=null){
						String key=p.getY()+";"+p.getX()+","+tollStation.toString();
						if(map.containsKey(key)){
							map.put(key, map.get(key)+";"+p.getTime());
						}else{
							map.put(key, p.getTime());
						}
					}
				}
			}
			for(String key:map.keySet()){
				String[] times=map.get(key).split(";");
				String oneToll=matchGpsToll(key);
				if(times.length==1){
					System.out.println(oneToll+","+map.get(key)+","+times.length);
				}else{
					System.out.println(oneToll+","+times[0]+";"+times[times.length-1]+","+times.length);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 *判断匹配单条轨迹是否经过收费站
	 *首先使用DBSCAN判断该条轨迹的起止停留点
	 *排除停留点之外的速度为0的轨迹点中，判断其位置是否在收费站周围
	 *输出匹配上的，经过收费站的轨迹、经过收费站的轨迹点、时间段、经过的收费站gps位置
	 * @param trace	轨迹点集合
	 * @param mapGrid	收费站在地图上的位置
	 * @param out	输出文件夹
	 * @param count	记录匹配上的轨迹条数和总轨迹条数
	 * @throws IOException
	 */
	public static void judgeZeroSpeedPoints(ArrayList<point> trace,Map<String,List<String>> mapGrid,String out,int[] count) throws IOException{
		double bLng=105.283247;
		double bLat=28.142508;
//		dbscan d=new dbscan(200,20);
//		d.process(trace);
		Map<String,String> map=new LinkedHashMap<>();
		for(int i=0;i<trace.size();i++){
			point p=trace.get(i);
			if(p.getCluster()==0&&Double.parseDouble(p.getV())==0){
				String c=getXY(bLng,bLat,p.getX(),p.getY());
				String[] cc=c.split(",",2);
				double x=Double.parseDouble(cc[0]);
				double y=Double.parseDouble(cc[1]);
				List<String> tollStation=isPassToll(x,y,mapGrid);
				if(tollStation!=null){
					String key=p.getY()+";"+p.getX()+"|"+tollStation.toString();
					if(map.containsKey(key)){
						map.put(key, map.get(key)+";"+p.getTime());
					}else{
						map.put(key, p.getTime());
					}
				}
			}
		}
		if(map.size()!=0){
			count[1]++;//统计轨迹数据匹配收费站的轨迹条数
			String outPath=out+"/trace"+count[1]+".csv";
			BufferedWriter writer=io.getWriter(outPath, "GBK");
			for(int i=0;i<trace.size();i++){
				point p=trace.get(i);
				writer.write(p.getWrite()+"\n");
			}
			for(String key:map.keySet()){
				String[] times=map.get(key).split(";");
				String oneToll=matchGpsToll(key);
				if(times.length==1){
					writer.write(oneToll+","+map.get(key)+","+times.length+"\n");//经过收费站的轨迹点的经纬度、收费广场经纬度、起止时间点、轨迹点个数
//					System.out.println(oneToll+","+map.get(key)+","+times.length);
				}else{
					writer.write(oneToll+","+times[0]+";"+times[times.length-1]+","+times.length+"\n");
//					System.out.println(oneToll+","+times[0]+";"+times[times.length-1]+","+times.length);
				}
			}
			writer.flush();
			writer.close();
		}
	}
	
	/**
	 * 根据中心坐标和收费站经纬度，计算收费站坐标
	 * @param bLng	中心坐标经度
	 * @param bLat	中心坐标纬度
	 * @param lng	收费站经度
	 * @param lat	收费站纬度
	 * @return 返回x，y字符串
	 */
	public static String getXY(double bLng,double bLat,double lng,double lat){
		double x=Math.round(gps.getDistance(bLng, lng, lat, lat)/100);
		double y=Math.round(gps.getDistance(lng, lng, bLat, lat)/100);
		return x+","+y;
	}
	/**
	 * 以重庆地图最左下角的点为坐标中心，根据距离，计算每个收费站对应的x，y坐标，以100米为单位坐标
	 * @param tollCollection
	 * @return 返回每个收费站的坐标
	 */
	public static Map<String,List<String>> getGrid(String tollCollection){
		double bLng=105.283247;
		double bLat=28.142508;
		Map<String,List<String>> mapGrid=new HashMap<>();
		try{
			BufferedReader reader=getReader(tollCollection,"GBK");
			reader.readLine();
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",3);
				double lng=Double.parseDouble(data[1]);
				double lat=Double.parseDouble(data[2]);
				String coordinate=getXY(bLng,bLat,lng,lat);
				if(mapGrid.containsKey(coordinate)){
					List<String> list=mapGrid.get(coordinate);
					list.add(lat+";"+lng);
					mapGrid.put(coordinate, list);
				}else{
					List<String> list=new ArrayList<>();
					list.add(lat+";"+lng);
					mapGrid.put(coordinate, list);
				}
				reader.readLine();
			}
			reader.close();
//			for(String key:mapGrid.keySet()){
//				System.out.println(key+","+mapGrid.get(key));
//			}
//			System.out.println(mapGrid.size());
		}catch(Exception e){
			e.printStackTrace();
		}
		return mapGrid;
	}
	
	/**
	 * 遍历一个省的所有轨迹数，输出匹配上的经过收费站的轨迹、经过的收费站经纬度、经过的时间
	 * @param gpsPath	轨迹输入路径
	 * @param tollStationPath	收费站经纬度输入
	 * @param outdir	输出的路径
	 */
	public static void getTollStationFromGps(String gpsPath,String tollStationPath,String outdir){
		try{
			Map<String,List<String>> mapGrid=getGrid(tollStationPath);
			File file=new File(gpsPath);
			List<String> list=Arrays.asList(file.list());
			int count[]=new int[2];;
			for(int i=0;i<list.size();i++){
				String pathIn=gpsPath+"/"+list.get(i);
				BufferedReader reader=getReader(pathIn,"GBK");
				Map<String,ArrayList<point>> mapTrace=new HashMap<>();
				String line="";
				while((line=reader.readLine())!=null){
					String[] data=line.split(",",7);
					String index=data[0];
					String time=data[2];
					double lat=Double.parseDouble(data[3]);
					double lng=Double.parseDouble(data[4]);
					String v=data[5];
					String direction=data[6];
					if(mapTrace.containsKey(index)){
						ArrayList<point> trace=mapTrace.get(index);
						trace.add(new point(lng,lat,time,v,direction));
						mapTrace.put(index, trace);
					}else{
						ArrayList<point> trace=new ArrayList<>();
						trace.add(new point(lng,lat,time,v,direction));
						mapTrace.put(index, trace);
					}
				}
				reader.close();
				String out=outdir+"/"+list.get(i).split("\\.")[0];
				File fileOut=new File(out);
				if(!fileOut.exists()){
					fileOut.mkdirs();
				}
				for(String key:mapTrace.keySet()){
					count[0]++;
					ArrayList<point> trace=mapTrace.get(key);
					judgeZeroSpeedPoints(trace,mapGrid,out,count);
				}
				System.out.println("read "+pathIn+" finish!");
			}
			System.out.println("总轨迹条数:"+count[0]);
			System.out.println("匹配上的轨迹条数:"+count[1]);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 将在一个收费站经纬度下的相邻数据融合为一条
	 * 收费站经纬度，时间点或时间范围（多个点），点个数
	 * @param map
	 * @param data
	 */
	public static LinkedList<String> bindMap(LinkedList<String> listData){
		LinkedList<String> list=new LinkedList<>();
		if(listData.size()==1){
			return listData;
		}
		int k=0;
		for(int i=0;i<listData.size();i++){
			if(list.size()==0){
				list.add(listData.get(i));
			}else{
				String[] data1=list.get(k).split(",",3);
				String[] data2=listData.get(i).split(",",3);
				String rsData="";
				if(!data1[0].equals(data2[0])){
					list.add(listData.get(i));
					k++;
				}else{
					int cs1=Integer.parseInt(data1[2]);
					int cs2=Integer.parseInt(data2[2]);
					if(cs1==1&&cs2==1){
						rsData=data1[0]+","+data1[1]+";"+data2[1]+","+(cs1+cs2);
					}else if(cs1==1&&cs2>1){
						rsData=data1[0]+","+data1[1]+";"+data2[1].split(";",2)[1]+","+(cs1+cs2);
					}else if(cs1>1&&cs2==1){
						rsData=data1[0]+","+data1[1].split(";",2)[0]+";"+data2[1]+","+(cs1+cs2);
					}else{
						rsData=data1[0]+","+data1[1].split(";",2)[0]+";"+data2[1].split(";",2)[1]+","+(cs1+cs2);
					}
					list.set(k, rsData);
				}
			}
		}
		return list;
	}
	/**
	 * 遍历输出的经过收费站的轨迹，通过经纬度匹配收费站编号，输出匹配上的数据
	 * @param in	匹配上的经过收费站的轨迹数据
	 * @param outPath18Chongqing	18年地图收费站经纬度
	 * @param outPath16Chongqing	16年poi收费站经纬度和名称
	 * @param cqStationName			重庆收费站名称和收费站id对应文件
	 * @throws IOException
	 */
	public static void matchGpsTollCollection(String in,String outPath18Chongqing,String outPath16Chongqing,String cqStationName,String outPath) throws IOException{
		BufferedWriter writer=io.getWriter(outPath, "gbk");
		Map<String,String> mapGpsToStationId=map.matchGpsToStationId(outPath18Chongqing,outPath16Chongqing,cqStationName);
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		int count=0;
		int count2=0;
		int amount=0;
		for(int i=0;i<list.size();i++){
			File fileIn=new File(in+"/"+list.get(i));
			List<String> listIn=Arrays.asList(fileIn.list());
			for(int j=0;j<listIn.size();j++){
				amount++;
				String pathIn=in+"/"+list.get(i)+"/"+listIn.get(j);
				BufferedReader reader=io.getReader(pathIn, "GBK");
				String line="";
				LinkedList<String> listData=new LinkedList<>();
				while((line=reader.readLine())!=null){
					String[] data=line.split(",");
					if(data.length==4){
						listData.add(data[1]+","+data[2]+","+data[3]);
					}
				}
				reader.close();
				LinkedList<String> listOut=bindMap(listData);
				boolean flag=true;
				for(int k=0;k<listOut.size();k++){
					String[] data=listOut.get(k).split(",",3);
					String stationGps=data[0];
					if(!mapGpsToStationId.containsKey(stationGps)){
						flag=false;
					}
				}
				if(flag){
					String out="";
					for(int k=0;k<listOut.size();k++){
						String[] data=listOut.get(k).split(",",3);
						String stationGps=data[0];
						if(out.equals("")){
							out+=mapGpsToStationId.get(stationGps)+","+listOut.get(k);
						}else{
							out+="|"+mapGpsToStationId.get(stationGps)+","+listOut.get(k);
						}
					}
//					System.out.println(listIn.get(j)+":"+out);
					String[] outData=out.split("\\|");
					if(outData.length>1){
						count2++;
						int length=outData.length;
						if(outData.length%2!=0){
							length--;
						}
						for(int l=0;l<length;l+=2){
							writer.write(outData[l]+"|"+outData[l+1]+"|"+listIn.get(j)+"\n");
						}
					}
					count++;
				}
			}
			System.out.println(in+"/"+list.get(i)+" finish!");
		}
		writer.flush();
		writer.close();
		System.out.println("收费站ID匹配上的轨迹数："+count);
		System.out.println("经过收费站大于2的轨迹数："+count2);
		System.out.println("总轨迹数："+amount);
	}
	
	/**
	 * 遍历输出的经过收费站的轨迹，通过经纬度匹配收费站编号，输出匹配上的数据,输出轨迹上收费站有收费站id的所有轨迹数据
	 * @param in	匹配上的经过收费站的轨迹数据
	 * @param outPath18Chongqing	18年地图收费站经纬度
	 * @param outPath16Chongqing	16年poi收费站经纬度和名称
	 * @param cqStationName			重庆收费站名称和收费站id对应文件
	 * @throws IOException
	 */
	public static void matchGpsTollCollection2(String in,String outPath18Chongqing,String outPath16Chongqing,String cqStationName,String outPath) throws IOException{
		BufferedWriter writer=io.getWriter(outPath, "gbk");
		Map<String,String> mapGpsToStationId=map.matchGpsToStationId(outPath18Chongqing,outPath16Chongqing,cqStationName);
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		int count=0;
		int count2=0;
		int amount=0;
		for(int i=0;i<list.size();i++){
			File fileIn=new File(in+"/"+list.get(i));
			List<String> listIn=Arrays.asList(fileIn.list());
			for(int j=0;j<listIn.size();j++){
				amount++;
				String pathIn=in+"/"+list.get(i)+"/"+listIn.get(j);
				BufferedReader reader=io.getReader(pathIn, "GBK");
				String line="";
				LinkedList<String> listData=new LinkedList<>();
				while((line=reader.readLine())!=null){
					String[] data=line.split(",");
					if(data.length==4){
						String stationGps=data[1];
						String time=data[2];
						String points=data[3];
						listData.add(stationGps+","+time+","+points);
					}
				}
				reader.close();
				LinkedList<String> listOut=bindMap(listData);
				boolean flag=false;
				for(int k=0;k<listOut.size();k++){
					String[] data=listOut.get(k).split(",",3);
					String stationGps=data[0];
					if(mapGpsToStationId.containsKey(stationGps)){
						flag=true;
					}
				}
				if(flag){
					count++;
					String out="";
					for(int k=0;k<listOut.size();k++){
						String[] data=listOut.get(k).split(",",3);
						String stationGps=data[0];
						String stationNameAndId=stationGps;
						if(mapGpsToStationId.containsKey(stationGps)){
							stationNameAndId=mapGpsToStationId.get(stationGps);
						}
						if(out.equals("")){
							out+=stationNameAndId+","+listOut.get(k);
						}else{
							out+="|"+stationNameAndId+","+listOut.get(k);
						}
					}
//					System.out.println(listIn.get(j)+":"+out);
					writer.write(out+"\n");
//					String[] outData=out.split("\\|");
//					if(outData.length>1){
//						count2++;
//						int length=outData.length;
//						if(outData.length%2!=0){
//							length--;
//						}
//						for(int l=0;l<length;l+=2){
//							writer.write(outData[l]+"|"+outData[l+1]+"|"+listIn.get(j)+"\n");
//						}
//					}
//					count++;
				}
			}
			System.out.println(in+"/"+list.get(i)+" finish!");
		}
		writer.flush();
		writer.close();
		System.out.println("轨迹中至少有一个收费站经纬度匹配上收费站id的轨迹数："+count);
		System.out.println("经过收费站大于2的轨迹数："+count2);
		System.out.println("总轨迹数："+amount);
	}
	
	public static Map<String,ArrayList<String>> readGpsStationId(String in){
		Map<String,ArrayList<String>> mapGpsStation=new HashMap<>();
		int count=0;
		try{
			BufferedReader reader=io.getReader(in, "GBK");
			String line="";
			while((line=reader.readLine())!=null){
				count++;
				String[] data=line.split("\\|",3);
				String[] inMessage=data[0].split(",",5);
				String[] outMessage=data[1].split(",",5);
				String inStationId=inMessage[1];
				String outStationId=outMessage[1];
				String inTime=inMessage[3];
				String outTime=outMessage[3];
				if(mapGpsStation.containsKey(inStationId+","+outStationId)){
					ArrayList<String> list=mapGpsStation.get(inStationId+","+outStationId);
					list.add(inTime+","+outTime);
					mapGpsStation.put(inStationId+","+outStationId, list);
				}else{
					ArrayList<String> list=new ArrayList<>();
					list.add(inTime+","+outTime);
					mapGpsStation.put(inStationId+","+outStationId, list);
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("gps高速行程数："+count);
		return mapGpsStation;
	}
	
	public static boolean isMatch(String inTime,String outTime,String inGpsTime,String outGpsTime) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long inTimeLong=sdf.parse(inTime).getTime()/1000;
		long outTimeLong=sdf.parse(outTime).getTime()/1000;
		boolean in=false,out=false;
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
		if(outGpsTime.contains(";")){
			String time1=outGpsTime.split(";",2)[0];
			String time2=outGpsTime.split(";",2)[1];
			long time1Long=sdf.parse(time1).getTime()/1000;
			long time2Long=sdf.parse(time2).getTime()/1000;
			if(outTimeLong>=time1Long-30&&outTimeLong<=time2Long+30){
				out=true;
			}
		}else{
			long time=sdf.parse(outGpsTime).getTime()/1000;
			if(Math.abs(outTimeLong-time)<=30){
				out=true;
			}
		}
		return in&&out;
	}
	public static void readTollCollection(String inTollCollection,String gpsStationPath){
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String,ArrayList<String>> mapGpsStation=readGpsStationId(gpsStationPath);
			int countHuo=0;
			BufferedReader reader=io.getReader(inTollCollection, "GBK");
			Map<String,ArrayList<String>> mapRs=new HashMap<>();
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",8);
				int cx=Integer.parseInt(data[2]);
				String inTime=data[3];
				String outTime=data[4];
				String inStation=data[5];
				String outStation=data[6];
				String inDay=inTime.split(" ",2)[0];
				String outDay=outTime.split(" ",2)[0];
				if(inDay.equals("2017-05-26")&&outDay.equals("2017-05-26")&&cx>=2){
					if(mapGpsStation.containsKey(inStation+","+outStation)){
						ArrayList<String> list=mapGpsStation.get(inStation+","+outStation);
						for(int i=0;i<list.size();i++){
							String gpsTime=list.get(i);
							String inGpsTime=gpsTime.split(",")[0];
							String outGpsTime=gpsTime.split(",")[1];
							boolean flag=isMatch(inTime,outTime,inGpsTime,outGpsTime);
							if(flag){
								System.out.println(line);
								String key=inStation+","+outStation+","+inGpsTime+","+outGpsTime;
								if(mapRs.containsKey(key)){
									ArrayList<String> listIn=mapRs.get(key);
									listIn.add(inTime+","+outTime);
									mapRs.put(key, listIn);
								}else{
									ArrayList<String> listIn=new ArrayList<>();
									listIn.add(inTime+","+outTime);
									mapRs.put(key, listIn);
								}
							}
						}
					}
					countHuo++;
				}
			}
			reader.close();
//			for(String key:mapRs.keySet()){
//				System.out.println(key+"-->"+mapRs.get(key));
//			}
			System.out.println("交易数据匹配上gps轨迹的交易数："+mapRs.size());
			System.out.println("货车交易数："+countHuo);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * dbscan输出测试
	 */
	public static void dbscanTest(){
		try{
			String path="G:/货车轨迹数据/输出数据/20170526输出/单条轨迹/part-m-00000/trace19.csv";
			ArrayList<point> trace=readTrace(path);
			dbscan d=new dbscan(200,20);
			d.process(trace);
			String outPath="G:/货车轨迹数据/输出数据/20170526输出/停留点输出/";
			writeStop(trace,outPath);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void readTest(){
		try{
			long start=System.currentTimeMillis();
			BufferedReader reader=getReader("G:/货车轨迹数据/20170526/part-m-00000","GBK");
			String line="";
			int count=0;
			while((line=reader.readLine())!=null){
				count++;
			}
			reader.close();
			long time=(System.currentTimeMillis()-start)/1000;
			System.out.println("ordinary read:"+time);
			System.out.println("all line:"+count);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void threadReadTest(){
		bigFileReader.Builder builder=new bigFileReader.Builder("G:/货车轨迹数据/20170526/part-m-00000","G:/货车轨迹数据/输出数据/ceshi.csv", new IHandle(){
			
			@Override
			public void handle(String line) throws IOException {
				// TODO Auto-generated method stub
				
			}
		});
		builder.withThreadSize(10).withCharset("gbk").withBufferSize(1024*1024);
		bigFileReader reader=builder.build();
		reader.start();
	}
	public static void main(String[] args) throws IOException, ParseException{
//		String ori="G:/货车轨迹数据/20170526";
//		String out="G:/货车轨迹数据/输出数据/20170526输出/重庆";
//		read(ori,out);//输出某省内数据
//		readTest();
//		readSingleTrace("G:/货车轨迹数据/输出数据/20170526输出/重庆","G:/货车轨迹数据/输出数据/20170526输出/单条轨迹");//读取每个文件中的单条路径生成单个文件
//		dbscanTest();
//		getZeroSpeedPoints();
//		getGrid("G:/地图/收费站数据/18PoiChongqing.csv");
//		getTollStationFromGps("G:/货车轨迹数据/输出数据/20170526输出/重庆",outPath18Chongqing,"G:/货车轨迹数据/输出数据/20170526输出/停留点输出(无dbscan)");
		
//		readTollCollection(cqTollCollection20170526,outGpsWithStationId);
//		matchGpsTollCollection("G:/货车轨迹数据/输出数据/20170526输出/停留点输出(无dbscan)",outPath18Chongqing,outPath16Chongqing,cqStationName,outGpsWithStationId);
		
		matchGpsTollCollection2("G:/货车轨迹数据/输出数据/20170526输出/停留点输出(无dbscan)",outPath18Chongqing,outPath16Chongqing,cqStationName,outGpsWithStationId);
	}
}
