package reasonOfNotUseEtc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class getFirstEtcTime {
	
	private static String etcCarK="F:/重庆/不使用ETC原因分析/车辆最先出现时间/客车/etcCarK.csv";
	private static String mtcCarK="F:/重庆/不使用ETC原因分析/车辆最先出现时间/客车/mtcCarK.csv";
	private static String mtcThenEtcCarK="F:/重庆/不使用ETC原因分析/车辆最先出现时间/客车/mtcThenEtcCarK.csv";
	
	private static String etcCarH="F:/重庆/不使用ETC原因分析/车辆最先出现时间/货车/etcCarH.csv";
	private static String mtcCarH="F:/重庆/不使用ETC原因分析/车辆最先出现时间/货车/mtcCarH.csv";
	private static String mtcThenEtcCarH="F:/重庆/不使用ETC原因分析/车辆最先出现时间/货车/mtcThenEtcCarH.csv";
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
	/*
	 * 找每个车的第一次MTC时间，第一次ETC时间
	 * 如果车辆最早时间是ETC车道，则只有一个ETC时间
	 * 如果车辆先出现MTC车道，后出现ETC车道，则有一个第一次MTC时间，一个ETC时间
	 * 如果车辆一直是MTC车道，这只有一个第一次MTC时间
	 */
	public static void getFirstEtcOrMtcTime(Map<String,JSONObject> map,String plate,String cx,String enTime,String isEtc) throws JSONException, ParseException{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String key=plate+","+cx;
		String eTime=enTime.substring(0,10);
		if(map.containsKey(key)){
			JSONObject value=map.get(key);
			if(value.has("firstEtcTime")){
				if(df.parse(eTime).before(df.parse(value.getString("firstEtcTime")))){
					if(isEtc.equals("0")){
						value.put("firstMtcTime", eTime);
					}else{
						value.put("firstEtcTime", eTime);
					}
				}
			}else{
				if(isEtc.equals("0")){	
					if(df.parse(eTime).before(df.parse(value.getString("firstMtcTime")))){
						value.put("firstMtcTime", eTime);
					}
				}else{
					value.put("firstEtcTime", eTime);
				}
			}
		}else{
			JSONObject value=new JSONObject();
			if(isEtc.equals("0")){
				value.put("firstMtcTime", eTime);
			}else{
				value.put("firstEtcTime", eTime);
			}
			map.put(key, value);
		}
	}
	/*
	 * 读取文件夹下的每个文件，去除非渝籍车
	 */
	public static void readIn(String in,Map<String,JSONObject> mapK,Map<String,JSONObject> mapH) throws NumberFormatException, IOException, JSONException, ParseException{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
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
					String isEtc=data[7];
					if(plate.startsWith("渝")&&df.parse(enTime.substring(0, 10)).after(df.parse("2015-12-31"))){
						if(cx.equals("0")||cx.equals("1")){
							getFirstEtcOrMtcTime(mapK,plate,cx,enTime,isEtc);
						}else{
							getFirstEtcOrMtcTime(mapH,plate,cx,enTime,isEtc);
						}
					}
				}
			}
			reader.close();
			System.out.println("read "+path+" finish!");
		}
	}
	
	public static void writeMap(Map<String,JSONObject> map,String etcCar,String mtcCar,String mtcThenEtcCar) throws IOException, JSONException{
		BufferedWriter writerEtcCar=getWriter(etcCar,"GBK");
		BufferedWriter writerMtcCar=getWriter(mtcCar,"GBK");
		BufferedWriter writerMtcThenEtcCar=getWriter(mtcThenEtcCar,"GBK");
		Set set=map.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();
			JSONObject ob=map.get(key);
			if(ob.length()==1){
				if(ob.has("firstEtcTime")){
					writerEtcCar.write(key+","+ob.getString("firstEtcTime")+"\n");
					writerEtcCar.flush();
				}else{
					writerMtcCar.write(key+","+ob.getString("firstMtcTime")+"\n");
					writerMtcCar.flush();
				}
			}else{
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				String firstMtcTime=ob.getString("firstMtcTime");
				String firstEtcTime=ob.getString("firstEtcTime");
				if(firstMtcTime.equals(firstEtcTime)){
					writerEtcCar.write(key+","+ob.getString("firstEtcTime")+"\n");
					writerEtcCar.flush();
				}else{
					writerMtcThenEtcCar.write(key+","+ob.getString("firstMtcTime")+","+ob.getString("firstEtcTime")+"\n");
					writerMtcThenEtcCar.flush();
				}
			}
		}
		writerEtcCar.close();
		writerMtcCar.close();
		writerMtcThenEtcCar.close();
	}
	/*
	 * 找出每个车牌最先出现的跑ETC时间
	 */
	public static void write(String in,String in2) throws IOException, JSONException, NumberFormatException, ParseException{
		Map<String,JSONObject> mapTimeK=new HashMap<>();
		Map<String,JSONObject> mapTimeH=new HashMap<>();
		readIn(in,mapTimeK,mapTimeH);
		readIn(in2,mapTimeK,mapTimeH);
		System.out.println("客车数量："+mapTimeK.size());
		System.out.println("货车数量："+mapTimeH.size());
		writeMap(mapTimeK,etcCarK,mtcCarK,mtcThenEtcCarK);
		writeMap(mapTimeH,etcCarH,mtcCarH,mtcThenEtcCarH);
	}
}
