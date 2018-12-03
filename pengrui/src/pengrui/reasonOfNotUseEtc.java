package pengrui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class reasonOfNotUseEtc {
	private static String in2016="F:/����/ԭʼ����";
	private static String in2017="F:/����/2017short";
	private static String etcAndMtcOutOdK="F:/����/����ETCǰ�����/2016-2017Ƶ�μ�od����/�ͳ�/etcAndMtcOutOdK.csv";
	private static String etcAndMtcOutOdH="F:/����/����ETCǰ�����/2016-2017Ƶ�μ�od����/����/etcAndMtcOutOdH.csv";
	
	
	private static String etcOdK="F:/����/��ʹ��ETCԭ�����/Ƶ��odʱ�����/�ͳ�/etcOdK.csv";
	private static String etcAndMtcOdK="F:/����/��ʹ��ETCԭ�����/Ƶ��odʱ�����/�ͳ�/etcAndMtcOdK.csv";
	private static String mtcOdK="F:/����/��ʹ��ETCԭ�����/Ƶ��odʱ�����/�ͳ�/mtcOdK.csv";
	private static String etcOdH="F:/����/��ʹ��ETCԭ�����/Ƶ��odʱ�����/����/etcOdH.csv";
	private static String etcAndMtcOdH="F:/����/��ʹ��ETCԭ�����/Ƶ��odʱ�����/����/etcAndMtcOdH.csv";
	private static String mtcOdH="F:/����/��ʹ��ETCԭ�����/Ƶ��odʱ�����/����/mtcOdH.csv";
	private static String odStation="F:/����/odStationDistance.csv";
	private static String avgDisPin1Per20="F:/����/��ʹ��ETCԭ�����/ƽ���г̾���/�ͳ�/����Ƶ��ÿ��һ��/avgDisPin1Per20.csv";
	private static String avgDisPin1Per80="F:/����/��ʹ��ETCԭ�����/ƽ���г̾���/�ͳ�/����Ƶ��ÿ��һ��/avgDisPin1Per80.csv";
	private static String avgDisPin5Per20="F:/����/��ʹ��ETCԭ�����/ƽ���г̾���/�ͳ�/����Ƶ��ÿ�����/avgDisPin5Per20.csv";
	private static String avgDisPin5Per80="F:/����/��ʹ��ETCԭ�����/ƽ���г̾���/�ͳ�/����Ƶ��ÿ�����/avgDisPin5Per80.csv";
	private static String avgDisPin10Per20="F:/����/��ʹ��ETCԭ�����/ƽ���г̾���/�ͳ�/����Ƶ��ÿ��ʮ��/avgDisPin10Per20.csv";
	private static String avgDisPin10Per80="F:/����/��ʹ��ETCԭ�����/ƽ���г̾���/�ͳ�/����Ƶ��ÿ��ʮ��/avgDisPin10Per80.csv";
	private static String avgDisPinB10Per20="F:/����/��ʹ��ETCԭ�����/ƽ���г̾���/�ͳ�/����Ƶ��ÿ�´���ʮ��/avgDisPinB10Per20.csv";
	private static String avgDisPinB10Per80="F:/����/��ʹ��ETCԭ�����/ƽ���г̾���/�ͳ�/����Ƶ��ÿ�´���ʮ��/avgDisPinB10Per80.csv";
	
	private static String mostOdPin1Per20="F:/����/��ʹ��ETCԭ�����/Ƶ�����od/�ͳ�/����Ƶ��ÿ��һ��/mostOdPin1Per20.csv";
	private static String mostOdPin1Per80="F:/����/��ʹ��ETCԭ�����/Ƶ�����od/�ͳ�/����Ƶ��ÿ��һ��/mostOdPin1Per80.csv";
	private static String mostOdPin5Per20="F:/����/��ʹ��ETCԭ�����/Ƶ�����od/�ͳ�/����Ƶ��ÿ�����/mostOdPin5Per20.csv";
	private static String mostOdPin5Per80="F:/����/��ʹ��ETCԭ�����/Ƶ�����od/�ͳ�/����Ƶ��ÿ�����/mostOdPin5Per80.csv";
	private static String mostOdPin10Per20="F:/����/��ʹ��ETCԭ�����/Ƶ�����od/�ͳ�/����Ƶ��ÿ��ʮ��/mostOdPin10Per20.csv";
	private static String mostOdPin10Per80="F:/����/��ʹ��ETCԭ�����/Ƶ�����od/�ͳ�/����Ƶ��ÿ��ʮ��/mostOdPin10Per80.csv";
	private static String mostOdPinB10Per20="F:/����/��ʹ��ETCԭ�����/Ƶ�����od/�ͳ�/����Ƶ��ÿ�´���ʮ��/mostOdPinB10Per20.csv";
	private static String mostOdPinB10Per80="F:/����/��ʹ��ETCԭ�����/Ƶ�����od/�ͳ�/����Ƶ��ÿ�´���ʮ��/mostOdPinB10Per80.csv";
	
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
	public static void writeMap(Map<String,JSONObject> map,String enPlate,String enCx,String enTime,String isEtc) throws JSONException{
		String key=enPlate+","+enCx;
		if(!map.containsKey(key)){
			JSONObject value=new JSONObject();
			if(isEtc.equals("0")){
				JSONObject firstMtc=new JSONObject();
				firstMtc.put("firstMtcTime", enTime.substring(0,10));
				firstMtc.put("cs", 1);
				value.put("firstMtc", firstMtc);
			}else{
				JSONObject firstEtc=new JSONObject();
				firstEtc.put("firstEtcTime", enTime.substring(0,10));
				firstEtc.put("etcCs", 1);
				firstEtc.put("mtcCs",0);
				value.put("firstEtc", firstEtc);
			}
			map.put(key, value);
		}else{
			JSONObject v=map.get(key);
			if(v.has("firstEtc")){
				JSONObject firstEtc=v.getJSONObject("firstEtc");
				if(isEtc.equals("1")){
					int etcCs=firstEtc.getInt("etcCs");
					firstEtc.put("etcCs", etcCs+1);
				}else{
					int mtcCs=firstEtc.getInt("mtcCs");
					firstEtc.put("mtcCs", mtcCs+1);
				}
				v.put("firstEtc", firstEtc);
			}else{
				if(isEtc.equals("0")){
					JSONObject firstMtc=v.getJSONObject("firstMtc");
					int cs=firstMtc.getInt("cs");
					firstMtc.put("cs", cs+1);
					v.put("firstMtc", firstMtc);
				}else{
					JSONObject firstEtc=new JSONObject();
					firstEtc.put("firstEtcTime", enTime.substring(0,10));
					firstEtc.put("etcCs",1);
					firstEtc.put("mtcCs",0);
					v.put("firstEtc", firstEtc);
				}
			}
			map.put(key, v);
		}
	}
	public static void addJsonOd(JSONObject od,String enStation,String exStation,long timeGap,String flag) throws JSONException{
		String key=enStation+"-"+exStation;
		String key1=exStation+"-"+enStation;
		if(od.has(key)||od.has(key1)){
			if(od.has(key)){
				String value=od.getString(key);
				if(value.contains("|")){
					String[] data=value.split("\\|",2);
					int cs=Integer.parseInt(data[0]);
					long timeGapNew=(Long.parseLong(data[1])*cs+timeGap)/(cs+1);
					od.put(key, (cs+1)+"|"+timeGapNew);
				}else{
					od.put(key, 1+"|"+timeGap);
				}
			}else{
				String value=od.getString(key1);
				if(value.contains("|")){
					String[] data=value.split("\\|",2);
					int cs=Integer.parseInt(data[0]);
					long timeGapNew=(Long.parseLong(data[1])*cs+timeGap)/(cs+1);
					od.put(key1, (cs+1)+"|"+timeGapNew);
				}else{
					od.put(key1, 1+"|"+timeGap);
				}
			}
		}else{
			System.out.println(flag+"err!"+" "+enStation+","+exStation+" "+od.toString());
		}
	}
	public static void writeOdMap(Map<String,JSONObject> map,String enPlate,String enCx,String enTime,String exTime,String enStation,String exStation,String isEtc) throws JSONException, ParseException{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat df1=new SimpleDateFormat("yyyy-MM-dd");
		Date a=df.parse(enTime);
		Date b=df.parse(exTime);
		long timeGap=(b.getTime()-a.getTime())/1000;
		if(map.containsKey(enPlate+","+enCx)){
			JSONObject value=map.get(enPlate+","+enCx);
			JSONObject firstEtc=value.getJSONObject("firstEtc");
			String firstEtcTime=firstEtc.getString("firstEtcTime");
			if(df1.parse(enTime.substring(0,10)).before(df1.parse(firstEtcTime))){
				JSONObject firstMtc=value.getJSONObject("firstMtc");
				JSONObject od=firstMtc.getJSONObject("od");
				addJsonOd(od,enStation,exStation,timeGap,"1");
				firstMtc.put("od", od);
				value.put("firstMtc", firstMtc);
			}else{
				if(isEtc.equals("0")){
					JSONObject mtcOd=firstEtc.getJSONObject("mtcOd");
					addJsonOd(mtcOd,enStation,exStation,timeGap,"2");
					firstEtc.put("mtcOd", mtcOd);
				}else{
					JSONObject etcOd=firstEtc.getJSONObject("etcOd");
					addJsonOd(etcOd,enStation,exStation,timeGap,"3");
					firstEtc.put("etcOd", etcOd);
				}
				value.put("firstEtc", firstEtc);
			}
			map.put(enPlate+","+enCx, value);
		}
	}
	
	public static void writeOdMapToCsv(Map<String,JSONObject> map,BufferedWriter etcWriterOd,BufferedWriter etcAndMtcWriterOd,BufferedWriter mtcWriterOd) throws JSONException, IOException{
		Set set=map.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();
			JSONObject value=(JSONObject) entries[i].getValue();
			if(value.has("firstEtc")){
				if(value.has("firstMtc")){
					JSONObject firstMtc=value.getJSONObject("firstMtc");
					String firstMtcTime=firstMtc.getString("firstMtcTime");
					String cs=firstMtc.getString("cs");
					JSONObject od=firstMtc.getJSONObject("od");
					JSONObject firstEtc=value.getJSONObject("firstEtc");
					String firstEtcTime=firstEtc.getString("firstEtcTime");
					String etcCs=firstEtc.getString("etcCs");
					JSONObject etcOd=firstEtc.getJSONObject("etcOd");
					String mtcCs=firstEtc.getString("mtcCs");
					JSONObject mtcOd=firstEtc.getJSONObject("mtcOd");
					etcAndMtcWriterOd.write(key+";"+firstMtcTime+";"+cs+";"+od.toString()+";"+firstEtcTime+";"+etcCs+";"+etcOd.toString()+";"+mtcCs+";"+mtcOd.toString()+"\n");
					etcAndMtcWriterOd.flush();
				}else{
					JSONObject firstEtc=value.getJSONObject("firstEtc");
					JSONObject etcOd=firstEtc.getJSONObject("etcOd");
					JSONObject mtcOd=firstEtc.getJSONObject("mtcOd");
					etcWriterOd.write(key+";"+etcOd.toString()+";"+mtcOd.toString()+"\n");
					etcWriterOd.flush();
				}
			}else{
				JSONObject firstMtc=value.getJSONObject("firstMtc");
				JSONObject od=firstMtc.getJSONObject("od");
				mtcWriterOd.write(key+";"+od.toString()+"\n");
				mtcWriterOd.flush();
			}
		}
		etcWriterOd.close();
		etcAndMtcWriterOd.close();
		mtcWriterOd.close();
	}
	public static void readIn(String in,Map<String,JSONObject> mapK,Map<String,JSONObject> mapH) throws NumberFormatException, IOException, JSONException, ParseException{
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			String path=in+"/"+list.get(i);
			BufferedReader reader=getReader(path,"GBK");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",8);
				if(data.length==8){
					String plate=data[1];
					String cx=data[2];
					String enTime=data[3];
					String exTime=data[4];
					String isEtc=data[7];
					String enStation=data[5];
					String exStation=data[6];
					if(plate.startsWith("��")){
						if(cx.equals("0")||cx.equals("1")){
							writeOdMap(mapK,plate,cx,enTime,exTime,enStation,exStation,isEtc);//���ͳ�ÿ��od��ͨ�д�����¼����
						}else{
							writeOdMap(mapH,plate,cx,enTime,exTime,enStation,exStation,isEtc);//������ÿ��od��ͨ�д�����¼����
						}
					}
//					writeOdMap(map,plate,cx,enTime,enStation,exStation,isEtc);
				}
			}
			reader.close();
			System.out.println("read "+path+" finish!");
		}
	}
	
	public static void readKHMap(String in,Map<String,JSONObject> map) throws IOException, JSONException{
		BufferedReader reader=getReader(in,"GBK");
		String line="";
		while((line=reader.readLine())!=null){
			String[] data=line.split(";",9);
			String firstMtcTime=data[1];
			String cs=data[2];
			JSONObject od=new JSONObject(data[3]);
			String firstEtcTime=data[4];
			String etcCs=data[5];
			JSONObject etcOd=new JSONObject(data[6]);
			String mtcCs=data[7];
			JSONObject mtcOd=new JSONObject(data[8]);
			JSONObject value=new JSONObject();
			JSONObject firstMtc=new JSONObject();
			firstMtc.put("firstMtcTime", firstMtcTime);
			firstMtc.put("cs", cs);
			firstMtc.put("od", od);
			JSONObject firstEtc=new JSONObject();
			firstEtc.put("firstEtcTime", firstEtcTime);
			firstEtc.put("etcCs", etcCs);
			firstEtc.put("etcOd", etcOd);
			firstEtc.put("mtcCs", mtcCs);
			firstEtc.put("mtcOd", mtcOd);
			value.put("firstMtc", firstMtc);
			value.put("firstEtc", firstEtc);
			map.put(data[0], value);
		}
	}
	/*
	 * ��¼ÿ������һ��ʹ��ETC��ʱ�䡣
	 *һ��ʼ����ETC�ĳ��� {���ƣ�����,{ETCod:����+��ʻʱ��},{MTCod:����+��ʻʱ��}}
	 *һ��ʼ����MTC�ĳ���{���ƣ����ͣ�{MTCod������+��ʻʱ��}}
	 *��;����ETC�ĳ���{���ƣ����ͣ�{MTCod:����+��ʻʱ��}��{ETCod������+�г�ʱ��}��{MTCod������+�г�ʱ��}
	 *��ȡetcAndMtcOutOdK.csv��etcAndMtcOutOdH.csv�����ļ�������ʱ���������
	 */
	public static void write(String in,String in2) throws IOException, JSONException, NumberFormatException, ParseException{
		Map<String,JSONObject> mapK=new HashMap<>();
		Map<String,JSONObject> mapH=new HashMap<>();
		readKHMap(etcAndMtcOutOdK,mapK);
		readKHMap(etcAndMtcOutOdH,mapH);
		System.out.println("read map finish!");
		readIn(in,mapK,mapH);
		readIn(in2,mapK,mapH);
		System.out.println("�ͳ�������"+mapK.size());
		System.out.println("����������"+mapH.size());
		
		//����¼��od������ʱ���map����д���ļ�
		BufferedWriter etcKWriter=getWriter(etcOdK,"GBK");	
		BufferedWriter etcAndMtcKWriter=getWriter(etcAndMtcOdK,"GBK");	
		BufferedWriter mtcKWriter=getWriter(mtcOdK,"GBK");	
		writeOdMapToCsv(mapK,etcKWriter,etcAndMtcKWriter,mtcKWriter);
		
		BufferedWriter etcHWriter=getWriter(etcOdH,"GBK");	
		BufferedWriter etcAndMtcHWriter=getWriter(etcAndMtcOdH,"GBK");	
		BufferedWriter mtcHWriter=getWriter(mtcOdH,"GBK");	
		writeOdMapToCsv(mapH,etcHWriter,etcAndMtcHWriter,mtcHWriter);
	}
	
	
	public static String getAvgDistance(JSONObject obj,Map<String,String> map){
		Iterator it=obj.keys();
		int count=0;
		float amount=0;
		boolean flag=false;
		while(it.hasNext()){
			String key=(String) it.next();
			if(map.containsKey(key)){
				flag=true;
				count++;
				String value=map.get(key);
				float v=Float.parseFloat(value);
				amount+=v;
			}
		}
		if(count!=0){
			float p=amount/count;
			DecimalFormat decimalFormat=new DecimalFormat(".00");
			String persent=decimalFormat.format(p);
			return persent;
		}else{
			return "no od match";
		}
	}
	/*
	 *�ҳ�����ETC��ȴ��ʹ��ETC��ԭ��
	 *�鿴��ͬ����Ƶ���°���ETC��ETCʹ���ʴ���80%��ETCʹ����С��20%���������ݵĲ�ͬ
	 *���ȿ�ƽ����ʻ����
	 *����ͳ�etcAndMtcOutOdK.csv��odStationDistance.csv
	 */
	public static void averageDistance(String path) throws IOException, JSONException{
		BufferedReader reader=getReader(path,"GBK");
		BufferedReader readerOd=getReader(odStation,"GBK");
		
		BufferedWriter writerAvgDisPin1Per20=getWriter(avgDisPin1Per20,"GBK");
		BufferedWriter writerAvgDisPin1Per80=getWriter(avgDisPin1Per80,"GBK");
		BufferedWriter writerAvgDisPin5Per20=getWriter(avgDisPin5Per20,"GBK");
		BufferedWriter writerAvgDisPin5Per80=getWriter(avgDisPin5Per80,"GBK");
		BufferedWriter writerAvgDisPin10Per20=getWriter(avgDisPin10Per20,"GBK");
		BufferedWriter writerAvgDisPin10Per80=getWriter(avgDisPin10Per80,"GBK");
		BufferedWriter writerAvgDisPinB10Per20=getWriter(avgDisPinB10Per20,"GBK");
		BufferedWriter writerAvgDisPinB10Per80=getWriter(avgDisPinB10Per80,"GBK");
		
		Map<String,String> map=new HashMap<>();
		String lineOd="";
		while((lineOd=readerOd.readLine())!=null){
			String[] data=lineOd.split(",",2);
			map.put(data[0], data[1]);
		}
		readerOd.close();
		String line="";
		while((line=reader.readLine())!=null){
			String[] data=line.split(";",9);
			String cs=data[2];
			String od=data[3];
			String etcCs=data[5];
			String etcOd=data[6];
			String mtcCs=data[7];
			String mtcOd=data[8];
			int amount=Integer.parseInt(cs)+Integer.parseInt(etcCs)+Integer.parseInt(mtcCs);
			JSONObject objOd=new JSONObject(od);
			JSONObject objEtcOd=new JSONObject(etcOd);
			JSONObject objMtcOd=new JSONObject(mtcOd);
			String avgDistanceOd=getAvgDistance(objOd,map);
			String avgDistanceEtcOd=getAvgDistance(objEtcOd,map);
			String avgDistanceMtcOd="";
			if(!mtcCs.equals("0")){
				avgDistanceMtcOd=getAvgDistance(objMtcOd,map);
			}
			float p=Float.parseFloat(etcCs)/(Float.parseFloat(etcCs)+Float.parseFloat(mtcCs));
			if(amount<=22){
				if(p<=0.2){
					writerAvgDisPin1Per20.write(data[0]+";"+data[1]+";"+cs+";"+avgDistanceOd+";"+data[4]+";"+etcCs+";"+avgDistanceEtcOd+";"+mtcCs+";"+avgDistanceMtcOd+"\n");
					writerAvgDisPin1Per20.flush();
				}
				if(p>=0.8){
					writerAvgDisPin1Per80.write(data[0]+";"+data[1]+";"+cs+";"+avgDistanceOd+";"+data[4]+";"+etcCs+";"+avgDistanceEtcOd+";"+mtcCs+";"+avgDistanceMtcOd+"\n");
					writerAvgDisPin1Per80.flush();
				}
			}else if(amount<=110){
				if(p<=0.2){
					writerAvgDisPin5Per20.write(data[0]+";"+data[1]+";"+cs+";"+avgDistanceOd+";"+data[4]+";"+etcCs+";"+avgDistanceEtcOd+";"+mtcCs+";"+avgDistanceMtcOd+"\n");
					writerAvgDisPin5Per20.flush();
				}
				if(p>=0.8){
					writerAvgDisPin5Per80.write(data[0]+";"+data[1]+";"+cs+";"+avgDistanceOd+";"+data[4]+";"+etcCs+";"+avgDistanceEtcOd+";"+mtcCs+";"+avgDistanceMtcOd+"\n");
					writerAvgDisPin5Per80.flush();
				}
			}else if(amount<=220){
				if(p<=0.2){
					writerAvgDisPin10Per20.write(data[0]+";"+data[1]+";"+cs+";"+avgDistanceOd+";"+data[4]+";"+etcCs+";"+avgDistanceEtcOd+";"+mtcCs+";"+avgDistanceMtcOd+"\n");
					writerAvgDisPin10Per20.flush();
				}
				if(p>=0.8){
					writerAvgDisPin10Per80.write(data[0]+";"+data[1]+";"+cs+";"+avgDistanceOd+";"+data[4]+";"+etcCs+";"+avgDistanceEtcOd+";"+mtcCs+";"+avgDistanceMtcOd+"\n");
					writerAvgDisPin10Per80.flush();
				}
			}else{
				if(p<=0.2){
					writerAvgDisPinB10Per20.write(data[0]+";"+data[1]+";"+cs+";"+avgDistanceOd+";"+data[4]+";"+etcCs+";"+avgDistanceEtcOd+";"+mtcCs+";"+avgDistanceMtcOd+"\n");
					writerAvgDisPinB10Per20.flush();
				}
				if(p>=0.8){
					writerAvgDisPinB10Per80.write(data[0]+";"+data[1]+";"+cs+";"+avgDistanceOd+";"+data[4]+";"+etcCs+";"+avgDistanceEtcOd+";"+mtcCs+";"+avgDistanceMtcOd+"\n");
					writerAvgDisPinB10Per80.flush();
				}
			}
		}
		reader.close();
		writerAvgDisPin1Per20.close();
		writerAvgDisPin1Per80.close();
		writerAvgDisPin5Per20.close();
		writerAvgDisPin5Per80.close();
		writerAvgDisPin10Per20.close();
		writerAvgDisPin10Per80.close();
		writerAvgDisPinB10Per20.close();
		writerAvgDisPinB10Per80.close();
	}
	
	public static JSONObject getMostOdObj(JSONObject obj) throws NumberFormatException, JSONException{
		JSONObject rs=new JSONObject();
		Iterator it=obj.keys();
		int max=0;
		while(it.hasNext()){
			String key=(String) it.next();
			int value=Integer.parseInt(obj.getString(key));
			if(max<value){
				max=value;
			}
		}
		Iterator it2=obj.keys();
		while(it2.hasNext()){
			String key=(String)it2.next();
			String value=obj.getString(key);
			if(value.equals(""+max)){
				rs.put(key, value);
			}
		}
		return rs;
	}
	/*
	 *�鿴�ܵ�����od
	 *����ͳ�etcAndMtcOutOdK.csv��odStationDistance.csv
	 */
	public static void getMostOd(String path) throws IOException, JSONException{
		BufferedReader reader=getReader(path,"GBK");
		
		BufferedWriter writerMostOdPin1Per20=getWriter(mostOdPin1Per20,"GBK");
		BufferedWriter writerMostOdPin1Per80=getWriter(mostOdPin1Per80,"GBK");
		BufferedWriter writerMostOdPin5Per20=getWriter(mostOdPin5Per20,"GBK");
		BufferedWriter writerMostOdPin5Per80=getWriter(mostOdPin5Per80,"GBK");
		BufferedWriter writerMostOdPin10Per20=getWriter(mostOdPin10Per20,"GBK");
		BufferedWriter writerMostOdPin10Per80=getWriter(mostOdPin10Per80,"GBK");
		BufferedWriter writerMostOdPinB10Per20=getWriter(mostOdPinB10Per20,"GBK");
		BufferedWriter writerMostOdPinB10Per80=getWriter(mostOdPinB10Per80,"GBK");
		
		String line="";
		while((line=reader.readLine())!=null){
			String[] data=line.split(";",9);
			String cs=data[2];
			String od=data[3];
			String etcCs=data[5];
			String etcOd=data[6];
			String mtcCs=data[7];
			String mtcOd=data[8];
			int amountCs=Integer.parseInt(cs)+Integer.parseInt(etcCs)+Integer.parseInt(mtcCs);
			float p=Float.parseFloat(etcCs)/(Float.parseFloat(etcCs)+Float.parseFloat(mtcCs));
			JSONObject objOd=new JSONObject(od);
			JSONObject objEtcOd=new JSONObject(etcOd);
			JSONObject objMtcOd=new JSONObject(mtcOd);
			JSONObject objMostOd=getMostOdObj(objOd);
			JSONObject objMostEtcOd=getMostOdObj(objEtcOd);
			JSONObject objMostMtcOd=getMostOdObj(objMtcOd);
			if(amountCs<=22){
				if(p<=0.2){
					writerMostOdPin1Per20.write(data[0]+";"+data[1]+";"+cs+";"+objMostOd.toString()+";"+data[4]+";"+etcCs+";"+objMostEtcOd.toString()+";"+mtcCs+";"+objMostMtcOd.toString()+"\n");
					writerMostOdPin1Per20.flush();
				}
				if(p>=0.8){
					writerMostOdPin1Per80.write(data[0]+";"+data[1]+";"+cs+";"+objMostOd.toString()+";"+data[4]+";"+etcCs+";"+objMostEtcOd.toString()+";"+mtcCs+";"+objMostMtcOd.toString()+"\n");
					writerMostOdPin1Per80.flush();
				}
			}else if(amountCs<=110){
				if(p<=0.2){
					writerMostOdPin5Per20.write(data[0]+";"+data[1]+";"+cs+";"+objMostOd.toString()+";"+data[4]+";"+etcCs+";"+objMostEtcOd.toString()+";"+mtcCs+";"+objMostMtcOd.toString()+"\n");
					writerMostOdPin5Per20.flush();
				}
				if(p>=0.8){
					writerMostOdPin5Per80.write(data[0]+";"+data[1]+";"+cs+";"+objMostOd.toString()+";"+data[4]+";"+etcCs+";"+objMostEtcOd.toString()+";"+mtcCs+";"+objMostMtcOd.toString()+"\n");
					writerMostOdPin5Per80.flush();
				}
			}else if(amountCs<=220){
				if(p<=0.2){
					writerMostOdPin10Per20.write(data[0]+";"+data[1]+";"+cs+";"+objMostOd.toString()+";"+data[4]+";"+etcCs+";"+objMostEtcOd.toString()+";"+mtcCs+";"+objMostMtcOd.toString()+"\n");
					writerMostOdPin10Per20.flush();
				}
				if(p>=0.8){
					writerMostOdPin10Per80.write(data[0]+";"+data[1]+";"+cs+";"+objMostOd.toString()+";"+data[4]+";"+etcCs+";"+objMostEtcOd.toString()+";"+mtcCs+";"+objMostMtcOd.toString()+"\n");
					writerMostOdPin10Per80.flush();
				}
			}else{
				if(p<=0.2){
					writerMostOdPinB10Per20.write(data[0]+";"+data[1]+";"+cs+";"+objMostOd.toString()+";"+data[4]+";"+etcCs+";"+objMostEtcOd.toString()+";"+mtcCs+";"+objMostMtcOd.toString()+"\n");
					writerMostOdPinB10Per20.flush();
				}
				if(p>=0.8){
					writerMostOdPinB10Per80.write(data[0]+";"+data[1]+";"+cs+";"+objMostOd.toString()+";"+data[4]+";"+etcCs+";"+objMostEtcOd.toString()+";"+mtcCs+";"+objMostMtcOd.toString()+"\n");
					writerMostOdPinB10Per80.flush();
				}
			}
		}
		reader.close();
		writerMostOdPin1Per20.close();
		writerMostOdPin1Per80.close();
		writerMostOdPin5Per20.close();
		writerMostOdPin5Per80.close();
		writerMostOdPin10Per20.close();
		writerMostOdPin10Per80.close();
		writerMostOdPinB10Per20.close();
		writerMostOdPinB10Per80.close();
	}
	
	public static void main(String[] args) throws IOException, JSONException, ParseException{
//		averageDistance(etcAndMtcOutOdK);
//		getMostOd(etcAndMtcOutOdK);
		write(in2016,in2017);
	}
}
