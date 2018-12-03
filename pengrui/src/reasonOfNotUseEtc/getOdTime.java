package reasonOfNotUseEtc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class getOdTime {
	private static String mtcThenEtcCarK="F:/重庆/不使用ETC原因分析/车辆最先出现时间/客车/mtcThenEtcCarK.csv";
	private static String mtcThenEtcCarH="F:/重庆/不使用ETC原因分析/车辆最先出现时间/货车/mtcThenEtcCarH.csv";
	
	private static String mtcThenEtcOdTimeK="F:/重庆/不使用ETC原因分析/频次od时间分类/客车/mtcThenEtcOdTimeK.csv";
	private static String mtcThenEtcOdTimeH="F:/重庆/不使用ETC原因分析/频次od时间分类/货车/mtcThenEtcOdTimeH.csv";
	
	public static void readMap(Map<String,JSONObject> map,String in) throws IOException, JSONException{
		BufferedReader reader=getFirstEtcTime.getReader(in, "GBK");
		String line="";
		while((line=reader.readLine())!=null){
			String[] data=line.split(",",4);
			String key=data[0]+","+data[1];
			String firstMtcTime=data[2];
			String firstEtcTime=data[3];
			JSONObject ob=new JSONObject();
			ob.put("firstMtcTime", firstMtcTime);
			ob.put("firstEtcTime", firstEtcTime);
			map.put(key, ob);
		}
		reader.close();
	}
	/*
	 * 不区分出入站
	 */
	public static void addJsonOd(JSONObject od,String enStation,String exStation,long timeGap) throws JSONException{
		String key=enStation+"-"+exStation;
		String key1=exStation+"-"+enStation;
		if(od.has(key)||od.has(key1)){
			if(od.has(key)){
				String value=od.getString(key);
				String[] data=value.split("\\|",2);
				int cs=Integer.parseInt(data[0]);
				long timeGapNew=(Long.parseLong(data[1])*cs+timeGap)/(cs+1);
				od.put(key, (cs+1)+"|"+timeGapNew);
			}else{
				String value=od.getString(key1);
				String[] data=value.split("\\|",2);
				int cs=Integer.parseInt(data[0]);
				long timeGapNew=(Long.parseLong(data[1])*cs+timeGap)/(cs+1);
				od.put(key1, (cs+1)+"|"+timeGapNew);
			}
		}else{
			od.put(key, 1+"|"+timeGap);
		}
	}
	/*
	 * 区分出入站点
	 */
	public static void addJsonOd1(JSONObject od,String enStation,String exStation,long timeGap) throws JSONException{
		String key=enStation+"-"+exStation;
		DecimalFormat decimalFormat=new DecimalFormat(".00");
		if(od.has(key)){
			String value=od.getString(key);
			String[] data=value.split("\\|",2);
			int cs=Integer.parseInt(data[0]);
			float timeGapNew=(Float.parseFloat(data[1])*cs+timeGap)/(float)(cs+1);
			String persent=decimalFormat.format(timeGapNew);
			od.put(key, (cs+1)+"|"+persent);
		}else{
			od.put(key, 1+"|"+timeGap);
		}
	}
	public static void getOdTimeMap(Map<String,JSONObject> mapOdTime,String key,String enTime,String exTime,String enStation,String exStation,String isEtc,Map<String,JSONObject> mapMtcThenEtc) throws JSONException, ParseException{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat df1=new SimpleDateFormat("yyyy-MM-dd");
		Date a=df.parse(enTime);
		Date b=df.parse(exTime);
		long timeGap=(b.getTime()-a.getTime())/1000;
		enTime=enTime.substring(0,10);
		if(mapMtcThenEtc.containsKey(key)){
			JSONObject ob=mapMtcThenEtc.get(key);
			String firstMtcTime=ob.getString("firstMtcTime");
			String firstEtcTime=ob.getString("firstEtcTime");
			if(mapOdTime.containsKey(key)){
				JSONObject value=mapOdTime.get(key);
				JSONObject firstMtc=value.getJSONObject("firstMtc");
				JSONObject firstEtc=value.getJSONObject("firstEtc");
				if(df1.parse(enTime).before(df1.parse(firstEtcTime))){
					JSONObject od=firstMtc.getJSONObject("od");
					addJsonOd1(od,enStation,exStation,timeGap);
					firstMtc.put("od", od);
				}else{
					if(isEtc.equals("0")){
						JSONObject mtcOd=firstEtc.getJSONObject("mtcOd");
						addJsonOd1(mtcOd,enStation,exStation,timeGap);
						firstEtc.put("mtcOd", mtcOd);
					}else{
						JSONObject etcOd=firstEtc.getJSONObject("etcOd");
						addJsonOd1(etcOd,enStation,exStation,timeGap);
						firstEtc.put("etcOd", etcOd);
					}
				}
				value.put("firstMtc", firstMtc);
				value.put("firstEtc",firstEtc);
				mapOdTime.put(key, value);
			}else{
				JSONObject value=new JSONObject();
				JSONObject firstMtc=new JSONObject();
				firstMtc.put("firstMtcTime", firstMtcTime);
				JSONObject od=new JSONObject();
				firstMtc.put("od", od);
				
				JSONObject firstEtc=new JSONObject();
				firstEtc.put("firstEtcTime", firstEtcTime);
				JSONObject etcOd=new JSONObject();
				JSONObject mtcOd=new JSONObject();
				firstEtc.put("etcOd", etcOd);
				firstEtc.put("mtcOd", mtcOd);
				
				if(df1.parse(enTime).before(df1.parse(firstEtcTime))){
					JSONObject odIn=firstMtc.getJSONObject("od");
					odIn.put(enStation+"-"+exStation, 1+"|"+timeGap);
					firstMtc.put("od", odIn);
				}else{
					JSONObject etcOdIn=firstEtc.getJSONObject("etcOd");
					JSONObject mtcOdIn=firstEtc.getJSONObject("mtcOd");
					if(isEtc.equals("0")){
						mtcOdIn.put(enStation+"-"+exStation, 1+"|"+timeGap);
					}else{
						etcOdIn.put(enStation+"-"+exStation, 1+"|"+timeGap);
					}
					firstEtc.put("etcOd", etcOdIn);
					firstEtc.put("mtcOd", mtcOdIn);
				}
				value.put("firstMtc", firstMtc);
				value.put("firstEtc",firstEtc);
				mapOdTime.put(key, value);
			}
		}
	}
	public static void readIn(String in,Map<String,JSONObject> mapMtcThenEtcK,Map<String,JSONObject> mapMtcThenEtcH,Map<String,JSONObject> mapOdTimeK,Map<String,JSONObject> mapOdTimeH) throws NumberFormatException, IOException, JSONException, ParseException{
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
						if(cx.equals("0")||cx.equals("1")){
							getOdTimeMap(mapOdTimeK,key,enTime,exTime,enStation,exStation,isEtc,mapMtcThenEtcK);
						}else{
							getOdTimeMap(mapOdTimeH,key,enTime,exTime,enStation,exStation,isEtc,mapMtcThenEtcH);
						}
					}
				}
			}
			reader.close();
			System.out.println("read "+path+" finish!");
		}
	}
	
	public static void writeOdMapToCsv(Map<String,JSONObject> map,String out) throws JSONException, IOException{
		BufferedWriter etcAndMtcWriterOd=getFirstEtcTime.getWriter(out, "GBK");
		Set set=map.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();
			JSONObject value=(JSONObject) entries[i].getValue();
			
			JSONObject firstMtc=value.getJSONObject("firstMtc");
			String firstMtcTime=firstMtc.getString("firstMtcTime");
			JSONObject od=firstMtc.getJSONObject("od");
			JSONObject firstEtc=value.getJSONObject("firstEtc");
			String firstEtcTime=firstEtc.getString("firstEtcTime");
			JSONObject etcOd=firstEtc.getJSONObject("etcOd");
			JSONObject mtcOd=firstEtc.getJSONObject("mtcOd");
			etcAndMtcWriterOd.write(key+";"+firstMtcTime+";"+od.toString()+";"+firstEtcTime+";"+etcOd.toString()+";"+mtcOd.toString()+"\n");
			etcAndMtcWriterOd.flush();
		}
		etcAndMtcWriterOd.close();
	}
	/*
	 * 查找中途转ETC的车辆的od信息和行程时间信息
	 */
	public static void getOdTimeInfo(String in,String in2) throws IOException, JSONException, NumberFormatException, ParseException{
		Map<String,JSONObject> mapOdTimeK=new HashMap<>();
		Map<String,JSONObject> mapOdTimeH=new HashMap<>();
		
		Map<String,JSONObject> mapMtcThenEtcK=new HashMap<>();
		Map<String,JSONObject> mapMtcThenEtcH=new HashMap<>();
		readMap(mapMtcThenEtcK,mtcThenEtcCarK);
		readMap(mapMtcThenEtcH,mtcThenEtcCarH);
		System.out.println("read firstTimeMap finish!");
		readIn(in,mapMtcThenEtcK,mapMtcThenEtcH,mapOdTimeK,mapOdTimeH);
		readIn(in2,mapMtcThenEtcK,mapMtcThenEtcH,mapOdTimeK,mapOdTimeH);
		System.out.println("read mapOdTime finish");
		writeOdMapToCsv(mapOdTimeK,mtcThenEtcOdTimeK);
		writeOdMapToCsv(mapOdTimeH,mtcThenEtcOdTimeH);
	}
}
