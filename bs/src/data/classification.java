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
	private static String outPath18Chongqing="G:/��ͼ/�շ�վ����/18PoiChongqing.csv";
	private static String outPath16Chongqing="G:/��ͼ/�շ�վ����/16PoiChongqing.csv";
	private static String cqStationName="G:/�½��ļ���/����/����վ���.csv";
	
	private static String cqTollCollection20170526="D:/2017short/201705short.csv";
	private static String outGpsWithStationId="G:/�����켣����/�������/20170526���/���շ�����ƥ��/gpsWithStationId.csv";
	/**
	 * ���ļ�
	 * @param in	�����ļ�·��
	 * @param encoding	�ļ������ʽ
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
	 * д�ļ�
	 * @param out ����ļ�·��
	 * @param encoding	����ļ���ʽ
	 * @return	����BufferedWriter
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
	 * ���켣�����е�ʱ��ת��Ϊ��׼��ʽ
	 * @param s	�����Ժ����ʾ���ַ���
	 * @return	�����׼��ʽʱ���ַ���
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
	 * �жϾ�γ���Ƿ���һ����Χ��
	 * @param lat	ά��
	 * @param lng	����
	 * @return �ڷ�Χ�ڷ���true�����򷵻�false
	 */
	public static boolean isInCity(double lat,double lng){
		if(lat>=28.142508&&lat<=32.193387&&lng>=105.283247&&lng<=110.183150){
			return true;
		}
		return false;
	}
	/**
	 * ��ȡԭʼ�����켣���ݣ�ɸѡ��ĳ��ʡ�ڵĹ켣����
	 * @param path	�����ļ���·��
	 * @param outPath	����ļ���·��
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
	 * ��ÿ���ļ��еĶ������·��������Ϊһ��һ���ĵ����ļ�
	 * @param path ÿһ��Ķ���ļ�����·��
	 * @param outPath	ÿһ�������ļ�·��
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
	 * ��ȡ�켣���ݷ���һϵ�е��ArrayList
	 * @param path	�켣��������·��
	 * @return	����dbscan����ĵ�ļ���
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
	 * ���dbscanͣ������
	 * @param points	����DBSCAN�㷨��Ǻ�Ĺ켣��
	 * @param path	���·��
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
	 * �ж��ٶ�Ϊ0��ͣ�����Ƿ���һ���շ�վ����Χ
	 * @param x	�켣���x����
	 * @param y	�켣���y����
	 * @param mapGrid	�շ�վ��Ӧ�����λ��
	 * @return	����켣�����շ�վ��Χ�򷵻��շ�վ��list�����û�ڷ��ؿ�
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
	 * ��gps���Ӧ����2���շѹ㳡��ѡȡ��õ������һ���շѹ㳡
	 * @param in	�켣�㾭γ�ȣ��շѹ㳡��γ�ȼ��ϵ��ַ���
	 * @return	���ع켣�����ù켣��������շѹ㳡�ľ�γ��
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
	 * �ӹ켣������ȡ������������ֹ��֮����ٶ�Ϊ0�ĵ㣬��������Ƿ�Ϊ���շ�վ��ͣ��
	 */
	public static void getZeroSpeedPoints(){
		double bLng=105.283247;
		double bLat=28.142508;
		try{
			Map<String,List<String>> mapGrid=getGrid("G:/��ͼ/�շ�վ����/18PoiChongqing.csv");
			String path="G:/�����켣����/�������/20170526���/�����켣/part-m-00000/trace19.csv";
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
	 *�ж�ƥ�䵥���켣�Ƿ񾭹��շ�վ
	 *����ʹ��DBSCAN�жϸ����켣����ֹͣ����
	 *�ų�ͣ����֮����ٶ�Ϊ0�Ĺ켣���У��ж���λ���Ƿ����շ�վ��Χ
	 *���ƥ���ϵģ������շ�վ�Ĺ켣�������շ�վ�Ĺ켣�㡢ʱ��Ρ��������շ�վgpsλ��
	 * @param trace	�켣�㼯��
	 * @param mapGrid	�շ�վ�ڵ�ͼ�ϵ�λ��
	 * @param out	����ļ���
	 * @param count	��¼ƥ���ϵĹ켣�������ܹ켣����
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
			count[1]++;//ͳ�ƹ켣����ƥ���շ�վ�Ĺ켣����
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
					writer.write(oneToll+","+map.get(key)+","+times.length+"\n");//�����շ�վ�Ĺ켣��ľ�γ�ȡ��շѹ㳡��γ�ȡ���ֹʱ��㡢�켣�����
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
	 * ��������������շ�վ��γ�ȣ������շ�վ����
	 * @param bLng	�������꾭��
	 * @param bLat	��������γ��
	 * @param lng	�շ�վ����
	 * @param lat	�շ�վγ��
	 * @return ����x��y�ַ���
	 */
	public static String getXY(double bLng,double bLat,double lng,double lat){
		double x=Math.round(gps.getDistance(bLng, lng, lat, lat)/100);
		double y=Math.round(gps.getDistance(lng, lng, bLat, lat)/100);
		return x+","+y;
	}
	/**
	 * �������ͼ�����½ǵĵ�Ϊ�������ģ����ݾ��룬����ÿ���շ�վ��Ӧ��x��y���꣬��100��Ϊ��λ����
	 * @param tollCollection
	 * @return ����ÿ���շ�վ������
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
	 * ����һ��ʡ�����й켣�������ƥ���ϵľ����շ�վ�Ĺ켣���������շ�վ��γ�ȡ�������ʱ��
	 * @param gpsPath	�켣����·��
	 * @param tollStationPath	�շ�վ��γ������
	 * @param outdir	�����·��
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
			System.out.println("�ܹ켣����:"+count[0]);
			System.out.println("ƥ���ϵĹ켣����:"+count[1]);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * ����һ���շ�վ��γ���µ����������ں�Ϊһ��
	 * �շ�վ��γ�ȣ�ʱ����ʱ�䷶Χ������㣩�������
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
	 * ��������ľ����շ�վ�Ĺ켣��ͨ����γ��ƥ���շ�վ��ţ����ƥ���ϵ�����
	 * @param in	ƥ���ϵľ����շ�վ�Ĺ켣����
	 * @param outPath18Chongqing	18���ͼ�շ�վ��γ��
	 * @param outPath16Chongqing	16��poi�շ�վ��γ�Ⱥ�����
	 * @param cqStationName			�����շ�վ���ƺ��շ�վid��Ӧ�ļ�
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
		System.out.println("�շ�վIDƥ���ϵĹ켣����"+count);
		System.out.println("�����շ�վ����2�Ĺ켣����"+count2);
		System.out.println("�ܹ켣����"+amount);
	}
	
	/**
	 * ��������ľ����շ�վ�Ĺ켣��ͨ����γ��ƥ���շ�վ��ţ����ƥ���ϵ�����,����켣���շ�վ���շ�վid�����й켣����
	 * @param in	ƥ���ϵľ����շ�վ�Ĺ켣����
	 * @param outPath18Chongqing	18���ͼ�շ�վ��γ��
	 * @param outPath16Chongqing	16��poi�շ�վ��γ�Ⱥ�����
	 * @param cqStationName			�����շ�վ���ƺ��շ�վid��Ӧ�ļ�
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
		System.out.println("�켣��������һ���շ�վ��γ��ƥ�����շ�վid�Ĺ켣����"+count);
		System.out.println("�����շ�վ����2�Ĺ켣����"+count2);
		System.out.println("�ܹ켣����"+amount);
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
		System.out.println("gps�����г�����"+count);
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
			System.out.println("��������ƥ����gps�켣�Ľ�������"+mapRs.size());
			System.out.println("������������"+countHuo);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * dbscan�������
	 */
	public static void dbscanTest(){
		try{
			String path="G:/�����켣����/�������/20170526���/�����켣/part-m-00000/trace19.csv";
			ArrayList<point> trace=readTrace(path);
			dbscan d=new dbscan(200,20);
			d.process(trace);
			String outPath="G:/�����켣����/�������/20170526���/ͣ�������/";
			writeStop(trace,outPath);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void readTest(){
		try{
			long start=System.currentTimeMillis();
			BufferedReader reader=getReader("G:/�����켣����/20170526/part-m-00000","GBK");
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
		bigFileReader.Builder builder=new bigFileReader.Builder("G:/�����켣����/20170526/part-m-00000","G:/�����켣����/�������/ceshi.csv", new IHandle(){
			
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
//		String ori="G:/�����켣����/20170526";
//		String out="G:/�����켣����/�������/20170526���/����";
//		read(ori,out);//���ĳʡ������
//		readTest();
//		readSingleTrace("G:/�����켣����/�������/20170526���/����","G:/�����켣����/�������/20170526���/�����켣");//��ȡÿ���ļ��еĵ���·�����ɵ����ļ�
//		dbscanTest();
//		getZeroSpeedPoints();
//		getGrid("G:/��ͼ/�շ�վ����/18PoiChongqing.csv");
//		getTollStationFromGps("G:/�����켣����/�������/20170526���/����",outPath18Chongqing,"G:/�����켣����/�������/20170526���/ͣ�������(��dbscan)");
		
//		readTollCollection(cqTollCollection20170526,outGpsWithStationId);
//		matchGpsTollCollection("G:/�����켣����/�������/20170526���/ͣ�������(��dbscan)",outPath18Chongqing,outPath16Chongqing,cqStationName,outGpsWithStationId);
		
		matchGpsTollCollection2("G:/�����켣����/�������/20170526���/ͣ�������(��dbscan)",outPath18Chongqing,outPath16Chongqing,cqStationName,outGpsWithStationId);
	}
}
