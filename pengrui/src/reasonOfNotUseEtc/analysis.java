package reasonOfNotUseEtc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class analysis {
	private static String in2016="F:/重庆/原始数据";
	private static String in2017="F:/重庆/2017short";
	private static String mtcThenEtcOdTimeK="F:/重庆/不使用ETC原因分析/频次od时间分类/客车/mtcThenEtcOdTimeK.csv";
	private static String mtcThenEtcOdTimeH="F:/重庆/不使用ETC原因分析/频次od时间分类/货车/mtcThenEtcOdTimeH.csv";
	private static String odDistance="F:/重庆/odStationDistance.csv";
	
	private static String sameOd="F:/重庆/不使用ETC原因分析/sameOd.csv";
	private static String outTimeSliceTemp="F:/重庆/不使用ETC原因分析/outTimeSliceTemp.csv";
	private static String outTimeSlice="F:/重庆/不使用ETC原因分析/outTimeSlice.csv";
	private static String 渝BP0680="F:/重庆/不使用ETC原因分析/渝BP0680.csv";
	
	private static String notSameOd="F:/重庆/不使用ETC原因分析/notSameOd.csv";
	
	private static String odAllRecord="F:/重庆/不使用ETC原因分析/odAllRecord.csv";
	
	private static String small="F:/重庆/不使用ETC原因分析/small.csv";
	public static void readIn(String in,Set<String> setK) throws NumberFormatException, IOException, JSONException, ParseException{
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			String path=in+"/"+list.get(i);
			BufferedReader reader=getFirstEtcTime.getReader(path,"GBK");
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
					String key=plate+","+cx;
					if(plate.startsWith("渝")){
						if(plate.equals("渝BP0680")&&cx.equals("0")){
							setK.add(line);
						}
					}
				}
			}
			reader.close();
			System.out.println("read "+path+" finish!");
		}
	}
	
	public static JSONObject getMostOdObj(JSONObject obj,Map<String,String> mapDistance) throws NumberFormatException, JSONException{
		JSONObject rs=new JSONObject();
		Iterator it=obj.keys();
		int max=0;
		String flag="";
		while(it.hasNext()){
			String key=(String) it.next();
			String v=obj.getString(key);
			int value=Integer.parseInt(v.split("\\|",2)[0]);
			if(max<value){
				max=value;
				flag=key;
			}
		}
		if(flag!=""){
			String flag1=flag.split("-",2)[1]+"-"+flag.split("-", 2)[0];
			Iterator it2=obj.keys();
			while(it2.hasNext()){
				String key=(String)it2.next();
				if(key.equals(flag)||key.equals(flag1)){
					String value=obj.getString(key);
					if(mapDistance.containsKey(key)){
						rs.put(key, obj.getString(key)+"|"+mapDistance.get(key));
					}else{
						rs.put(key, obj.getString(key)+"|"+0);
					}
				}
			}
		}
		return rs;
	}
	
	public static void getCar(String out) throws NumberFormatException, IOException, JSONException, ParseException{
		BufferedWriter writer=getFirstEtcTime.getWriter(out, "GBK");
		Set<String> setK=new HashSet<>();
		readIn(in2016,setK);
//		readIn(in2017,setK);
		for(String v:setK){
			writer.write(v+"\n");
		}
		writer.flush();
		writer.close();
	}
	
	public static int getCount(JSONObject ob) throws JSONException{
		int count=0;
		Iterator<String> it=ob.keys();
		while(it.hasNext()){
			String key=it.next();
			String value=ob.getString(key);
			String[] v=value.split("\\|",2);
			count+=Integer.parseInt(v[0]);
		}
		return count;
	}
	/*
	 * 查看ETC、MTC跑的最多的od是不是一个od
	 */
	public static int getSameOdCount(JSONObject obEtc,JSONObject obMtc){
		int count;
		Set<String> set=new HashSet<>();
		Iterator it=obEtc.keys();
		while(it.hasNext()){
			String key=(String) it.next();
			set.add(key);
		}
		for(String key:set){
			String[] data=key.split("-");
			String key1=data[1]+"-"+data[0];
			if(obMtc.has(key1)||obMtc.has(key)){
				return 1;
			}
		}
		return 0;
	}
	/*
	 * 按频次，ETC使用率分类，加上od距离,对数据分类
	 */
	public static void classify(String in,String in2,String out) throws IOException, JSONException{
		BufferedReader reader=getFirstEtcTime.getReader(in, "GBK");
		BufferedReader readerDistance=getFirstEtcTime.getReader(in2, "GBK");
		Map<String,String> mapDistance=new HashMap<>();
		BufferedWriter writer=getFirstEtcTime.getWriter(out, "GBK");
		String lineDistance="";
		while((lineDistance=readerDistance.readLine())!=null){
			String[] data=lineDistance.split(",",2);
			String od=data[0];
			String distance=data[1];
			mapDistance.put(od, distance);
		}
		readerDistance.close();
		
		int count=0;
		int countAll=0;
		String line="";
		while((line=reader.readLine())!=null){
			String[] data=line.split(";",6);
			String key=data[0];
			String firstMtcTime=data[1];
			JSONObject od=new JSONObject(data[2]);
			String firstEtcTime=data[3];
			JSONObject etcOd=new JSONObject(data[4]);
			JSONObject mtcOd=new JSONObject(data[5]);
			int amount=getCount(od)+getCount(etcOd)+getCount(mtcOd);
			int etcCs=getCount(etcOd);
			int mtcCs=getCount(mtcOd);
			float p=(float)(etcCs)/((float)(etcCs)+(float)(mtcCs));
			if(amount<220){
				countAll++;
				JSONObject od1=getMostOdObj(od,mapDistance);
				JSONObject etcOd1=getMostOdObj(etcOd,mapDistance);
				JSONObject mtcOd1=new JSONObject();
				if(mtcOd.length()>0){
					mtcOd1=getMostOdObj(mtcOd,mapDistance);
				}
				count+=getSameOdCount(etcOd1,mtcOd1);
				if(getSameOdCount(etcOd1,mtcOd1)==1){
					writer.write(key+";"+firstMtcTime+";"+getCount(od)+";"+od1.toString()+";"+firstEtcTime+";"+getCount(etcOd)+";"+etcOd1.toString()+";"+getCount(mtcOd)+";"+mtcOd1.toString()+"\n");
				}
			}
		}
		reader.close();
		writer.flush();
		writer.close();
		System.out.println("跑ETC最多的od和跑MTCod相同的车辆数："+count);
		System.out.println("所有车辆数："+countAll);
	}
	
	
	public static int timeSlice(String enTime){
		String hour=enTime.substring(11,13);
		int h=Integer.parseInt(hour);
		return h/2;
	}
	public static void addTimeSliceToOb(JSONObject ob,String key,int timeSlice) throws JSONException{
		if(ob.has(key)){
			JSONObject obIn=ob.getJSONObject(key);
			if(obIn.has(""+timeSlice)){
				obIn.put(""+timeSlice, obIn.getInt(""+timeSlice)+1);
			}else{
				obIn.put(""+timeSlice, 1);
			}
		}
	}
	
	public static void readInTimeSlice(String in,Map<String,JSONObject> map) throws NumberFormatException, IOException, JSONException, ParseException{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			String path=in+"/"+list.get(i);
			BufferedReader reader=getFirstEtcTime.getReader(path,"GBK");
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
					String key=plate+","+cx;
					if(map.containsKey(key)){
						int t=timeSlice(enTime);
						JSONObject value=map.get(key);
						String firstEtcTime=value.getString("firstEtcTime");						
						JSONObject etcOdOb=value.getJSONObject("etcOdOb");
						JSONObject mtcOdOb=value.getJSONObject("mtcOdOb");
						if(!df.parse(enTime.substring(0, 10)).before(df.parse(firstEtcTime))){
							if(isEtc.equals("0")){
								addTimeSliceToOb(mtcOdOb,enStation+"-"+exStation,t);
							}else{
								addTimeSliceToOb(etcOdOb,enStation+"-"+exStation,t);
							}
						}
						value.put("etcOdOb", etcOdOb);
						value.put("mtcOdOb", mtcOdOb);
						map.put(key, value);
					}
				}
			}
			reader.close();
			System.out.println("read "+path+" finish!");
		}
	}
	public static void clearOd(JSONObject ob) throws JSONException{
		Iterator<String> it=ob.keys();
		while(it.hasNext()){
			String key=it.next();
			JSONObject obIn=new JSONObject();
			ob.put(key, obIn);
		}
	}
	
	public static void writeTimeSliceMap(Map<String,JSONObject> map,String out) throws JSONException, IOException{
		BufferedWriter writer=getFirstEtcTime.getWriter(out, "GBK");
		for(String key:map.keySet()){
			JSONObject ob=map.get(key);
			String firstEtcTime=ob.getString("firstEtcTime");
			String etcCs=ob.getString("etcCs");
			JSONObject etcOdOb=ob.getJSONObject("etcOdOb");
			String mtcCs=ob.getString("mtcCs");
			JSONObject mtcOdOb=ob.getJSONObject("mtcOdOb");
			writer.write(key+";"+firstEtcTime+";"+etcCs+";"+etcOdOb.toString()+";"+mtcCs+";"+mtcOdOb.toString()+"\n");
		}
		writer.flush();
		writer.close();
	}
	/*
	 * 对每月出行10次以上，并且ETC、MTC跑的最多的od是同一od的数据，分析其出行的时间片特征
	 */
	public static void getTimeSlice(String in,String in2,String in3,String out) throws IOException, JSONException, NumberFormatException, ParseException{
		BufferedReader readerCar=getFirstEtcTime.getReader(in3, "GBK");
		Map<String,JSONObject> map=new HashMap<>();
		String line="";
		while((line=readerCar.readLine())!=null){
			String[] data=line.split(";",9);
			String key=data[0];
			String cs=data[2];
			String firstEtcTime=data[4];
			String etcCs=data[5];
			String etcOd=data[6];
			String mtcCs=data[7];
			String mtcOd=data[8];
			JSONObject etcOdOb=new JSONObject(etcOd);
			JSONObject mtcOdOb=new JSONObject(mtcOd);
			clearOd(etcOdOb);
			clearOd(mtcOdOb);
			JSONObject value=new JSONObject();
			value.put("firstEtcTime", firstEtcTime);
			value.put("etcCs", etcCs);
			value.put("etcOdOb", etcOdOb);
			value.put("mtcCs", mtcCs);
			value.put("mtcOdOb", mtcOdOb);
			map.put(key, value);
		}
		readerCar.close();
		System.out.println("read map finish!");
		readInTimeSlice(in,map);
		readInTimeSlice(in2,map);
		writeTimeSliceMap(map,out);
	}
	
	public static void classifyTimeSlice(String in,String out) throws IOException, JSONException{
		BufferedReader reader=getFirstEtcTime.getReader(in, "GBK");
		BufferedWriter writer=getFirstEtcTime.getWriter(out, "GBK");
		String line="";
		while((line=reader.readLine())!=null){
			String[] data=line.split(";",6);
			String key=data[0];
			String firstEtcTime=data[1];
			String etcCs=data[2];
			String etcOd=data[3];
			String mtcCs=data[4];
			String mtcOd=data[5];
			float p=Float.parseFloat(etcCs)/(Float.parseFloat(etcCs)+Float.parseFloat(mtcCs));
			if(p>0.5&&p<=0.6){
				writer.write(key+";"+firstEtcTime+";"+etcCs+";"+etcOd+";"+mtcCs+";"+mtcOd+"\n");
			}
		}
		reader.close();
		writer.flush();
		writer.close();
	}
	
	public static void readInOdAllRecord(String in,Set<String> set) throws NumberFormatException, IOException, JSONException, ParseException{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			String path=in+"/"+list.get(i);
			BufferedReader reader=getFirstEtcTime.getReader(path,"GBK");
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
					String key=plate+","+cx;
					if((enStation+"-"+exStation).equals("4808-208")||(enStation+"-"+exStation).equals("208-4808")){
						set.add(line);
					}
				}
			}
			reader.close();
			System.out.println("read "+path+" finish!");
		}
	}
	
	public static void odAllRecord(String in,String in2,String out) throws NumberFormatException, IOException, JSONException, ParseException{
		BufferedWriter writer=getFirstEtcTime.getWriter(out, "GBK");
		Set<String> set=new HashSet<>();
//		readInOdAllRecord(in,set);
		readInOdAllRecord(in2,set);
		for(String key:set){
			writer.write(key+"\n");
		}
		writer.flush();
		writer.close();
	}
	
	public static void addDistance(JSONObject obj,Map<String,String> mapDistance) throws JSONException{
		Iterator it=obj.keys();
		while(it.hasNext()){
			String key=(String) it.next();
			if(mapDistance.containsKey(key)){
				obj.put(key, obj.getString(key)+"|"+mapDistance.get(key));
			}else{
				obj.put(key, obj.getString(key)+"|"+0);
			}
		}
	}
	/*
	 * 出行频次较少分类
	 */
	public static void classifySmall(String in,String in2,String out) throws IOException, JSONException{
		BufferedReader reader=getFirstEtcTime.getReader(in, "GBK");
		BufferedReader readerDistance=getFirstEtcTime.getReader(in2, "GBK");
		Map<String,String> mapDistance=new HashMap<>();
		BufferedWriter writer=getFirstEtcTime.getWriter(out, "GBK");
		String lineDistance="";
		while((lineDistance=readerDistance.readLine())!=null){
			String[] data=lineDistance.split(",",2);
			String od=data[0];
			String distance=data[1];
			mapDistance.put(od, distance);
		}
		readerDistance.close();
		
		int count=0;
		int countAll=0;
		String line="";
		while((line=reader.readLine())!=null){
			String[] data=line.split(";",6);
			String key=data[0];
			String firstMtcTime=data[1];
			JSONObject od=new JSONObject(data[2]);
			String firstEtcTime=data[3];
			JSONObject etcOd=new JSONObject(data[4]);
			JSONObject mtcOd=new JSONObject(data[5]);
			int amount=getCount(od)+getCount(etcOd)+getCount(mtcOd);
			int etcCs=getCount(etcOd);
			int mtcCs=getCount(mtcOd);
			float p=(float)(etcCs)/((float)(etcCs)+(float)(mtcCs));
			if(amount<110){
				countAll++;
				addDistance(od,mapDistance);
				addDistance(etcOd,mapDistance);
				if(mtcOd.length()>0){
					addDistance(mtcOd,mapDistance);
				}
				count+=getSameOdCount(etcOd,mtcOd);
				writer.write(key+";"+firstMtcTime+";"+getCount(od)+";"+od.toString()+";"+firstEtcTime+";"+getCount(etcOd)+";"+etcOd.toString()+";"+getCount(mtcOd)+";"+mtcOd.toString()+"\n");
			}
		}
		reader.close();
		writer.flush();
		writer.close();
		System.out.println("跑ETC最多的od和跑MTCod相同的车辆数："+count);
		System.out.println("所有车辆数："+countAll);
	}
	public static void main(String[] args) throws NumberFormatException, IOException, JSONException, ParseException{
		getCar(渝BP0680);
//		classify(mtcThenEtcOdTimeK,odDistance,sameOd);//ETC与MTC跑的最多的od是同一od
//		getTimeSlice(in2016,in2017,sameOd,outTimeSlice);
//		classifyTimeSlice(outTimeSlice,outTimeSliceTemp);
		
//		classify(mtcThenEtcOdTimeK,odDistance,notSameOd);//ETC与MTC跑的最多的od不是同一od
		
//		odAllRecord(in2016,in2017,odAllRecord);
		
//		classifySmall(mtcThenEtcOdTimeK,odDistance,small);
	}
}
