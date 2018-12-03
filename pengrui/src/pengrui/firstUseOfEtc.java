package pengrui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;


public class firstUseOfEtc {
	private static String in="F:/重庆/原始数据/201601.csv";
	private static String d="D:/2017年重庆数据";
	private static String f="F:/重庆/原始数据";
	private static String etcOut="F:/重庆/办理ETC前后分析/频次分类/etcOutResult.csv";
	private static String etcAndMtcOut="F:/重庆/办理ETC前后分析/频次分类/etcAndMtcOutResult.csv";
	private static String mtcOut="F:/重庆/办理ETC前后分析/频次分类/mtcOutResult.csv";
	
	private static String etcOutOd="F:/重庆/办理ETC前后分析/频次加od分类/etcOutResultOd.csv";
	private static String etcAndMtcOutOd="F:/重庆/办理ETC前后分析/频次加od分类/etcAndMtcOutResultOd.csv";
	private static String mtcOutOd="F:/重庆/办理ETC前后分析/频次加od分类/mtcOutResultOd.csv";
	
	private static String etcOutOdK="F:/重庆/办理ETC前后分析/2016-2017频次加od分类/客车/etcOutOdK.csv";
	private static String etcAndMtcOutOdK="F:/重庆/办理ETC前后分析/2016-2017频次加od分类/客车/etcAndMtcOutOdK.csv";
	private static String mtcOutOdK="F:/重庆/办理ETC前后分析/2016-2017频次加od分类/客车/mtcOutOdK.csv";
	
	private static String etcAndMtcOutMostOdK="F:/重庆/办理ETC前后分析/2016-2017频次加od分类/客车/etcAndMtcOutMostOdK.csv";
	
	private static String etcOutOdH="F:/重庆/办理ETC前后分析/2016-2017频次加od分类/货车/etcOutOdH.csv";
	private static String etcAndMtcOutOdH="F:/重庆/办理ETC前后分析/2016-2017频次加od分类/货车/etcAndMtcOutOdH.csv";
	private static String mtcOutOdH="F:/重庆/办理ETC前后分析/2016-2017频次加od分类/货车/mtcOutOdH.csv";
	
	private static String etcOutK="F:/重庆/办理ETC前后分析/频次分类/客车/etcOutK.csv";
	private static String etcAndMtcOutK="F:/重庆/办理ETC前后分析/频次分类/客车/etcAndMtcOutK.csv";
	private static String mtcOutK="F:/重庆/办理ETC前后分析/频次分类/客车/mtcOutK.csv";
	
	private static String etcOutH="F:/重庆/办理ETC前后分析/频次分类/货车/etcOutH.csv";
	private static String etcAndMtcOutH="F:/重庆/办理ETC前后分析/频次分类/货车/etcAndMtcOutH.csv";
	private static String mtcOutH="F:/重庆/办理ETC前后分析/频次分类/货车/mtcOutH.csv";
	
	private static String etc0K="F:/重庆/办理ETC前后分析/2016-2017频次分类/客车/0型客车/etcOut0K.csv";
	private static String etcAndMtc0K="F:/重庆/办理ETC前后分析/2016-2017频次分类/客车/0型客车/etcAndMtcOut0K.csv";
	private static String mtc0K="F:/重庆/办理ETC前后分析/2016-2017频次分类/客车/0型客车/mtcOut0K.csv";
	private static String etc1K="F:/重庆/办理ETC前后分析/2016-2017频次分类/客车/1型客车/etcOut1K.csv";
	private static String etcAndMtc1K="F:/重庆/办理ETC前后分析/2016-2017频次分类/客车/1型客车/etcAndMtcOut1K.csv";
	private static String mtc1K="F:/重庆/办理ETC前后分析/2016-2017频次分类/客车/1型客车/mtcOut1K.csv";
	
	private static String etc2H="F:/重庆/办理ETC前后分析/2016-2017频次分类/货车/2型货车/etcOut2H.csv";
	private static String etcAndMtc2H="F:/重庆/办理ETC前后分析/2016-2017频次分类/货车/2型货车/etcAndMtcOut2H.csv";
	private static String mtc2H="F:/重庆/办理ETC前后分析/2016-2017频次分类/货车/2型货车/mtcOut2H.csv";
	private static String etcH="F:/重庆/办理ETC前后分析/2016-2017频次分类/货车/2型以上货车/etcOutH.csv";
	private static String etcAndMtcH="F:/重庆/办理ETC前后分析/2016-2017频次分类/货车/2型以上货车/etcAndMtcOutH.csv";
	private static String mtcH="F:/重庆/办理ETC前后分析/2016-2017频次分类/货车/2型以上货车/mtcOutH.csv";
	
	private static String station="G:/新建文件夹/重庆/全路网距离梯形图(2016-04-13).csv";
	private static String odStation="F:/重庆/odStationDistance.csv";
	private static String etcAndMtcOutOdDistance="F:/重庆/办理ETC前后分析/etcAndMtcOutOdDistance.csv";
	private static String etcAndMtcOutMostOd="F:/重庆/办理ETC前后分析/etcAndMtcOutMostOd.csv";
	private static String etcOutMostOd="F:/重庆/办理ETC前后分析/etcOutMostOd.csv";
	
	private static String etcLessMtcMore="F:/重庆/办理ETC前后分析/etcLessMtcMore.csv";
	private static String etcLessMtcMoreDistance="F:/重庆/办理ETC前后分析/etcLessMtcMoreDistance.csv";
	
	private static String s10s90="F:/重庆/办理ETC前后分析/频次加od分类/具体分类/s10s90.csv";
	private static String s10b90="F:/重庆/办理ETC前后分析/频次加od分类/具体分类/s10b90.csv";
	private static String b10s90="F:/重庆/办理ETC前后分析/频次加od分类/具体分类/b10s90.csv";
	private static String b10b90="F:/重庆/办理ETC前后分析/频次加od分类/具体分类/b10b90.csv";
	/**
	 * 返回车辆的车型 
	 * @param khFlag 客货标志位
	 * @param vehClass 车型代码
	 * @return 0（小客车） 1（大中型客车）    2（1型货车）   3（2型货车） 4（3型货车） 5（4型货车） 6（5型货车）  
	 */
	public static int getVehicleType(String khFlag,String vehClass){
		if(khFlag.equals("0")&&Integer.valueOf(vehClass)<=1)return 0;
		if(khFlag.equals("0")&&Integer.valueOf(vehClass)>1)return 1;
		if(!khFlag.equals("0")&&Integer.valueOf(vehClass)<=1)return 2;
		if(!khFlag.equals("0")&&Integer.valueOf(vehClass)==2)return 3;
		if(!khFlag.equals("0")&&Integer.valueOf(vehClass)==3)return 4;
		if(!khFlag.equals("0")&&Integer.valueOf(vehClass)==4)return 5;
		if(!khFlag.equals("0")&&Integer.valueOf(vehClass)==5)return 6;
		return -1;
	}
	
	public static String correctTimeCq(String time) throws ParseException{
		if(time.length()<19){
			return "0";
		}else{
			String a=time.substring(0, 19);
			DateFormat df=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date=df.parse(a);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String outTime=sdf.format(date);
			return outTime;
		}
	}
	
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
	
	public static void readCq(String in,String out) throws JSONException{
		BufferedReader reader=getReader(in,"GBK");
		BufferedWriter writer=getWriter(out,"GBK");
		String line="";
		try{
			while((line=reader.readLine())!=null){
				String[] data=line.split(";",51);
				if(data.length==51){
					String cardId=data[15].trim();
					String exStation=data[0].trim();
					String enStation=data[9].trim();
					String exTime=correctTimeCq(data[2].trim());
					String enTime=correctTimeCq(data[10].trim());
					String enPlate=data[20].trim();
					String exPlate=data[21].trim();
					String exClass=data[11].trim();
					String exFlag=data[28].trim();
					String enClass=data[18].trim();
					String enFlag=data[29].trim();
					String distance=data[36].trim();
					String weight=data[41].trim();
					if(enFlag.length()<10&&enClass.length()<10&&exFlag.length()<10&&exClass.length()<10){
						String enCx=""+getVehicleType(enFlag,enClass);
						String exCx=""+getVehicleType(exFlag,exClass);
						String isEtc=data[48].trim();
						if(enPlate.equals(exPlate)&&enCx.equals(exCx)&&enPlate.length()>5){
							writer.write(cardId+","+enPlate+","+enCx+","+enTime+","+exTime+","+enStation+","+exStation+","+weight+","+isEtc+"\n");
							writer.flush();
						}
						
					}
				}
			}
			reader.close();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void readFile(String in) throws JSONException{
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		int count=0;
		for(int i=0;i<list.size();i++){
			File fileIn=new File(in+"/"+list.get(i));
			List<String> listIn=Arrays.asList(fileIn.list());
			for(int j=0;j<listIn.size();j++){
				if(listIn.get(j).startsWith("a")){
					count++;
					String inPath=in+"/"+list.get(i)+"/"+listIn.get(j);
					String outPath="";
					if(count<10){
						outPath="D:/2016short/20160"+count+"short.csv";
					}else{
						outPath="D:/2016short/2016"+count+"short.csv";
					}
					readCq(inPath,outPath);
					System.out.println(outPath+" finish!");
				}
			}
		}
	}
	
	public static void readCq2017(String in,String out) throws JSONException{
		BufferedReader reader=getReader(in,"GBK");
		BufferedWriter writer=getWriter(out,"GBK");
		String line="";
		try{
			reader.readLine();
			while((line=reader.readLine())!=null){
				String[] data=line.split(";",52);
				if(data.length==52){
					String cardId=data[15].trim();
					String exStation=data[0].trim();
					String enStation=data[9].trim();
					String exTime=correctTimeCq(data[2].trim());
					String enTime=correctTimeCq(data[10].trim());
					String enPlate=data[20].trim();
					String exPlate=data[21].trim();
					String exClass=data[11].trim();
					String exFlag=data[28].trim();
					String enClass=data[18].trim();
					String enFlag=data[29].trim();
					if(enFlag.length()<10&&enClass.length()<10&&exFlag.length()<10&&exClass.length()<10){
						String enCx=""+getVehicleType(enFlag,enClass);
						String exCx=""+getVehicleType(exFlag,exClass);
						String isEtc=data[48].trim();
						if(enPlate.equals(exPlate)&&enCx.equals(exCx)&&enPlate.length()>5){
							writer.write(cardId+","+enPlate+","+enCx+","+enTime+","+exTime+","+enStation+","+exStation+","+isEtc+"\n");
							writer.flush();
						}
						
					}
				}
			}
			reader.close();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void read2017File(String in) throws JSONException{
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		int count=0;
		for(int i=0;i<list.size();i++){
			File fileIn=new File(in+"/"+list.get(i));
			String inPath=in+"/"+list.get(i);
			String[] a=list.get(i).split("\\.");
			String outPath="F:/重庆/2017short/"+a[0]+"short.csv";
			readCq2017(inPath,outPath);
			System.out.println(outPath+" finish!");
		}
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
	
	public static void writeMapToCsv(Map<String,JSONObject> map,BufferedWriter etcWriter,BufferedWriter etcAndMtcWriter,BufferedWriter mtcWriter) throws JSONException, IOException{
		Set set=map.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();
			JSONObject value=(JSONObject) entries[i].getValue();
			if(value.has("firstEtc")){
				if(value.has("firstMtc")){
					JSONObject firstEtc=value.getJSONObject("firstEtc");
					JSONObject firstMtc=value.getJSONObject("firstMtc");
					String firstMtcTime=firstMtc.getString("firstMtcTime");
					String cs=firstMtc.getString("cs");
					String firstEtcTime=firstEtc.getString("firstEtcTime");
					String etcCs=firstEtc.getString("etcCs");
					String mtcCs=firstEtc.getString("mtcCs");
					if(firstEtcTime.equals(firstMtcTime)){
						mtcCs+=cs;
						etcWriter.write(key+","+firstEtcTime+","+etcCs+","+mtcCs+"\n");
						etcWriter.flush();
					}else{
						etcAndMtcWriter.write(key+","+firstMtcTime+","+cs+","+firstEtcTime+","+etcCs+","+mtcCs+"\n");
						etcAndMtcWriter.flush();
					}
				}else{
					JSONObject firstEtc=value.getJSONObject("firstEtc");
					String firstEtcTime=firstEtc.getString("firstEtcTime");
					String etcCs=firstEtc.getString("etcCs");
					String mtcCs=firstEtc.getString("mtcCs");
					etcWriter.write(key+","+firstEtcTime+","+etcCs+","+mtcCs+"\n");
					etcWriter.flush();
				}
			}else{
				JSONObject firstMtc=value.getJSONObject("firstMtc");
				String firstMtcTime=firstMtc.getString("firstMtcTime");
				String cs=firstMtc.getString("cs");
				mtcWriter.write(key+","+firstMtcTime+","+cs+"\n");
				mtcWriter.flush();
			}
		}
		etcWriter.close();
		etcAndMtcWriter.close();
		mtcWriter.close();
	}
	public static void addJsonOd(JSONObject od,String enStation,String exStation) throws JSONException{
		String key=enStation+"-"+exStation;
		String key1=exStation+"-"+enStation;
		if(od.has(key)||od.has(key1)){
			if(od.has(key)){
				od.put(key, od.getInt(key)+1);
			}else{
				od.put(key1, od.getInt(key1)+1);
			}
		}else{
			od.put(key,1);
		}
	}
	public static void writeOdMap(Map<String,JSONObject> map,String enPlate,String enCx,String enTime,String enStation,String exStation,String isEtc) throws JSONException{
		String key=enPlate+","+enCx;
		if(!map.containsKey(key)){
			JSONObject value=new JSONObject();
			if(isEtc.equals("0")){
				JSONObject firstMtc=new JSONObject();
				firstMtc.put("firstMtcTime", enTime.substring(0,10));
				firstMtc.put("cs", 1);
				JSONObject od=new JSONObject();
				od.put(enStation+"-"+exStation, 1);
				firstMtc.put("od", od);
				value.put("firstMtc", firstMtc);
			}else{
				JSONObject firstEtc=new JSONObject();
				JSONObject etcOd=new JSONObject();
				JSONObject mtcOd=new JSONObject();
				firstEtc.put("firstEtcTime", enTime.substring(0,10));
				firstEtc.put("etcCs", 1);
				etcOd.put(enStation+"-"+exStation, 1);
				firstEtc.put("etcOd", etcOd);
				firstEtc.put("mtcCs",0);
				firstEtc.put("mtcOd", mtcOd);
				value.put("firstEtc", firstEtc);
			}
			map.put(key, value);
		}else{
			JSONObject v=map.get(key);
			if(v.has("firstEtc")){
				JSONObject firstEtc=v.getJSONObject("firstEtc");
				if(isEtc.equals("1")){
					JSONObject etcOd=firstEtc.getJSONObject("etcOd");
					int etcCs=firstEtc.getInt("etcCs");
					firstEtc.put("etcCs", etcCs+1);
					addJsonOd(etcOd,enStation,exStation);
					firstEtc.put("etcOd", etcOd);
				}else{
					JSONObject mtcOd=firstEtc.getJSONObject("mtcOd");
					int mtcCs=firstEtc.getInt("mtcCs");
					firstEtc.put("mtcCs", mtcCs+1);
					addJsonOd(mtcOd,enStation,exStation);
					firstEtc.put("mtcOd", mtcOd);
				}
				v.put("firstEtc", firstEtc);
			}else{
				if(isEtc.equals("0")){
					JSONObject firstMtc=v.getJSONObject("firstMtc");
					JSONObject od=firstMtc.getJSONObject("od");
					int cs=firstMtc.getInt("cs");
					firstMtc.put("cs", cs+1);
					addJsonOd(od,enStation,exStation);
					firstMtc.put("od", od);
					v.put("firstMtc", firstMtc);
				}else{
					JSONObject firstEtc=new JSONObject();
					JSONObject etcOd=new JSONObject();
					JSONObject mtcOd=new JSONObject();
					firstEtc.put("firstEtcTime", enTime.substring(0,10));
					firstEtc.put("etcCs",1);
					etcOd.put(enStation+"-"+exStation, 1);
					firstEtc.put("etcOd", etcOd);
					firstEtc.put("mtcCs",0);
					firstEtc.put("mtcOd", mtcOd);
					v.put("firstEtc", firstEtc);
				}
			}
			map.put(key, v);
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
					JSONObject firstEtc=value.getJSONObject("firstEtc");
					JSONObject firstMtc=value.getJSONObject("firstMtc");
					String firstMtcTime=firstMtc.getString("firstMtcTime");
					String cs=firstMtc.getString("cs");
					JSONObject od=firstMtc.getJSONObject("od");
					String firstEtcTime=firstEtc.getString("firstEtcTime");
					String etcCs=firstEtc.getString("etcCs");
					String mtcCs=firstEtc.getString("mtcCs");
					JSONObject etcOd=firstEtc.getJSONObject("etcOd");
					JSONObject mtcOd=firstEtc.getJSONObject("mtcOd");
					if(firstEtcTime.equals(firstMtcTime)){
						mtcCs+=cs;
						Iterator<String> it=od.keys();
						while(it.hasNext()){
							String keyIn=it.next();
							int valueIn=od.getInt(keyIn);
							if(mtcOd.has(keyIn)){
								mtcOd.put(keyIn, od.getInt(keyIn)+valueIn);
							}else{
								mtcOd.put(keyIn, valueIn);
							}
						}
						etcWriterOd.write(key+";"+firstEtcTime+";"+etcCs+";"+etcOd.toString()+";"+mtcCs+";"+mtcOd.toString()+"\n");
						etcWriterOd.flush();
					}else{
						etcAndMtcWriterOd.write(key+";"+firstMtcTime+";"+cs+";"+od.toString()+";"+firstEtcTime+";"+etcCs+";"+etcOd.toString()+";"+mtcCs+";"+mtcOd.toString()+"\n");
						etcAndMtcWriterOd.flush();
					}
				}else{
					JSONObject firstEtc=value.getJSONObject("firstEtc");
					String firstEtcTime=firstEtc.getString("firstEtcTime");
					String etcCs=firstEtc.getString("etcCs");
					String mtcCs=firstEtc.getString("mtcCs");
					JSONObject etcOd=firstEtc.getJSONObject("etcOd");
					JSONObject mtcOd=firstEtc.getJSONObject("mtcOd");
					etcWriterOd.write(key+";"+firstEtcTime+";"+etcCs+";"+etcOd.toString()+";"+mtcCs+";"+mtcOd.toString()+"\n");
					etcWriterOd.flush();
				}
			}else{
				JSONObject firstMtc=value.getJSONObject("firstMtc");
				String firstMtcTime=firstMtc.getString("firstMtcTime");
				String cs=firstMtc.getString("cs");
				JSONObject od=firstMtc.getJSONObject("od");
				mtcWriterOd.write(key+";"+firstMtcTime+";"+cs+";"+od.toString()+"\n");
				mtcWriterOd.flush();
			}
		}
		etcWriterOd.close();
		etcAndMtcWriterOd.close();
		mtcWriterOd.close();
	}
	
	public static void readIn(String in,Map<String,JSONObject> map0K,Map<String,JSONObject> map1K,Map<String,JSONObject> map2H,Map<String,JSONObject> mapH) throws NumberFormatException, IOException, JSONException{
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
					String enStation=data[5];
					String exStation=data[6];
					if(plate.startsWith("渝")){
						if(cx.equals("0")){
							writeMap(map0K,plate,cx,enTime,isEtc);
//							writeOdMap(mapK,plate,cx,enTime,enStation,exStation,isEtc);//将客车每个od的通行次数记录下来
						}else if(cx.equals("1")){
							writeMap(map1K,plate,cx,enTime,isEtc);
//							writeOdMap(mapH,plate,cx,enTime,enStation,exStation,isEtc);//将货车每个od的通行次数记录下来
						}else if(cx.equals("2")){
							writeMap(map2H,plate,cx,enTime,isEtc);
						}else{
							writeMap(mapH,plate,cx,enTime,isEtc);
						}
					}
//					writeOdMap(map,plate,cx,enTime,enStation,exStation,isEtc);
				}
			}
			reader.close();
			System.out.println("read "+path+" finish!");
		}
	}
	public static void write(String in,String in2) throws IOException, JSONException{
		Map<String,JSONObject> map0K=new HashMap<>();
		Map<String,JSONObject> map1K=new HashMap<>();
		Map<String,JSONObject> map2H=new HashMap<>();
		Map<String,JSONObject> mapH=new HashMap<>();
		readIn(in,map0K,map1K,map2H,mapH);
		readIn(in2,map0K,map1K,map2H,mapH);
		System.out.println("0型客车数量："+map0K.size());
		System.out.println("1型客车数量："+map1K.size());
		System.out.println("2型货车数量："+map2H.size());
		System.out.println("2型以上货车数量："+mapH.size());
		BufferedWriter etc0KWriter=getWriter(etc0K,"GBK");	
		BufferedWriter etcAndMtc0KWriter=getWriter(etcAndMtc0K,"GBK");	
		BufferedWriter mtc0KWriter=getWriter(mtc0K,"GBK");	
		writeMapToCsv(map0K,etc0KWriter,etcAndMtc0KWriter,mtc0KWriter);
		
		BufferedWriter etc1KWriter=getWriter(etc1K,"GBK");	
		BufferedWriter etcAndMtc1KWriter=getWriter(etcAndMtc1K,"GBK");	
		BufferedWriter mtc1KWriter=getWriter(mtc1K,"GBK");	
		writeMapToCsv(map1K,etc1KWriter,etcAndMtc1KWriter,mtc1KWriter);
		
		BufferedWriter etc2HWriter=getWriter(etc2H,"GBK");	
		BufferedWriter etcAndMtc2HWriter=getWriter(etcAndMtc2H,"GBK");	
		BufferedWriter mtc2HWriter=getWriter(mtc2H,"GBK");	
		writeMapToCsv(map2H,etc2HWriter,etcAndMtc2HWriter,mtc2HWriter);
		
		BufferedWriter etcHWriter=getWriter(etcH,"GBK");	
		BufferedWriter etcAndMtcHWriter=getWriter(etcAndMtcH,"GBK");	
		BufferedWriter mtcHWriter=getWriter(mtcH,"GBK");	
		writeMapToCsv(mapH,etcHWriter,etcAndMtcHWriter,mtcHWriter);
		
//		//将记录着od次数的map分类写入文件
//		BufferedWriter etcKWriter=getWriter(etcOutOdK,"GBK");	
//		BufferedWriter etcAndMtcKWriter=getWriter(etcAndMtcOutOdK,"GBK");	
//		BufferedWriter mtcKWriter=getWriter(mtcOutOdK,"GBK");	
//		writeOdMapToCsv(mapK,etcKWriter,etcAndMtcKWriter,mtcKWriter);
//		
//		BufferedWriter etcHWriter=getWriter(etcOutOdH,"GBK");	
//		BufferedWriter etcAndMtcHWriter=getWriter(etcAndMtcOutOdH,"GBK");	
//		BufferedWriter mtcHWriter=getWriter(mtcOutOdH,"GBK");	
//		writeOdMapToCsv(mapH,etcHWriter,etcAndMtcHWriter,mtcHWriter);
	}
	
	public static void write2017(String in) throws IOException, JSONException{
		Set<String> etcPlate2016=new HashSet<>();
		Set<String> mtcPlate2016=new HashSet<>();
		BufferedReader readerEtcAndMtc=getReader(etcAndMtcOut,"GBK");
		BufferedReader readerEtc=getReader(etcOut,"GBK");
		BufferedReader readerMtc=getReader(mtcOut,"GBK");
		String lineEtcAndMtc="";
		while((lineEtcAndMtc=readerEtcAndMtc.readLine())!=null){
			String[] data=lineEtcAndMtc.split(",",7);
			String plate=data[0];
			String cx=data[1];
			etcPlate2016.add(plate+","+cx);
		}
		readerEtcAndMtc.close();
		
		String lineEtc="";
		while((lineEtc=readerEtc.readLine())!=null){
			String[] data=lineEtc.split(",",5);
			String plate=data[0];
			String cx=data[1];
			etcPlate2016.add(plate+","+cx);
		}
		readerEtc.close();
		
		String lineMtc="";
		while((lineMtc=readerMtc.readLine())!=null){
			String[] data=lineMtc.split(",",4);
			String plate=data[0];
			String cx=data[1];
			mtcPlate2016.add(plate+","+cx);
		}
		readerMtc.close();
		
		Map<String,JSONObject> mapK=new HashMap<>();
		Map<String,JSONObject> mapH=new HashMap<>();
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
					String enStation=data[5];
					String exStation=data[6];
					if(plate.startsWith("渝")){
						if(Integer.parseInt(cx)<2){
							if(etcPlate2016.contains(plate+","+cx)||mtcPlate2016.contains(plate+","+cx)){
								if(etcPlate2016.contains(plate+","+cx)){
									
								}else{
									
								}
							}else{
								writeMap(mapK,plate,cx,enTime,isEtc);
							}
						}else{
							if(etcPlate2016.contains(plate+","+cx)||mtcPlate2016.contains(plate+","+cx)){
								if(etcPlate2016.contains(plate+","+cx)){
									
								}else{
									
								}
							}else{
								writeMap(mapH,plate,cx,enTime,isEtc);
							}
						}
					}
//					writeOdMap(map,plate,cx,enTime,enStation,exStation,isEtc);
				}
			}
			reader.close();
			System.out.println("read "+path+" finish!");
		}
		System.out.println("客车数量："+mapK.size());
		System.out.println("货车数量："+mapH.size());
		BufferedWriter etcKWriter=getWriter(etcOutK,"GBK");	
		BufferedWriter etcAndMtcKWriter=getWriter(etcAndMtcOutK,"GBK");	
		BufferedWriter mtcKWriter=getWriter(mtcOutK,"GBK");	
		writeMapToCsv(mapK,etcKWriter,etcAndMtcKWriter,mtcKWriter);
		
		BufferedWriter etcHWriter=getWriter(etcOutH,"GBK");	
		BufferedWriter etcAndMtcHWriter=getWriter(etcAndMtcOutH,"GBK");	
		BufferedWriter mtcHWriter=getWriter(mtcOutH,"GBK");	
		writeMapToCsv(mapH,etcHWriter,etcAndMtcHWriter,mtcHWriter);
		
//		BufferedWriter etcWriterOd=getWriter(etcOutOd,"GBK");	
//		BufferedWriter etcAndMtcWriterOd=getWriter(etcAndMtcOutOd,"GBK");	
//		BufferedWriter mtcWriterOd=getWriter(mtcOutOd,"GBK");	
//		writeOdMapToCsv(map,etcWriterOd,etcAndMtcWriterOd,mtcWriterOd);
	}
	
	public static void write(String in,String etcOut,String etcAndMtcOut,String mtcOut) throws IOException, JSONException{
		Map<String,JSONObject> map=new HashMap<>();
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
					String enStation=data[5];
					String exStation=data[6];
					writeMap(map,plate,cx,enTime,isEtc);
				}
			}
			reader.close();
			System.out.println("read "+path+" finish!");
		}
		System.out.println(map.size());
//		BufferedWriter etcWriter=getWriter(etcOut,"GBK");	
//		BufferedWriter etcAndMtcWriter=getWriter(etcAndMtcOut,"GBK");	
//		BufferedWriter mtcWriter=getWriter(mtcOut,"GBK");	
//		writeMapToCsv(map,etcWriter,etcAndMtcWriter,mtcWriter);
	}
	
	public static void initMap(Map<String,Integer> map){
		int a=-10,b=0;
		for(int i=1;i<=10;i++){
			int c=i*10;
			String p1=""+(a+c)+"%";
			String p2=""+(b+c)+"%";
			map.put(p1+"-"+p2, 0);
		}
	}
	public static void getPersent(Map<String,Integer> map,String etcCs,String mtcCs){
		float p=Float.parseFloat(etcCs)/(Float.parseFloat(etcCs)+Float.parseFloat(mtcCs))*100;
		String persent="";
		if(p>0&&p<=10){
			persent="0%-10%";
		}else if(p<=20){
			persent="10%-20%";
		}else if(p<=30){
			persent="20%-30%";
		}else if(p<=40){
			persent="30%-40%";
		}else if(p<=50){
			persent="40%-50%";
		}else if(p<=60){
			persent="50%-60%";
		}else if(p<=70){
			persent="60%-70%";
		}else if(p<=80){
			persent="70%-80%";
		}else if(p<=90){
			persent="80%-90%";                                   
		}else if(p<=100){
			persent="90%-100%";
		}
		map.put(persent, map.get(persent)+1);
	}
	
	public static void writePersent(Map<String,Integer> map,BufferedWriter writer) throws IOException{
		for(String key:map.keySet()){
			String value=map.get(key).toString();
			writer.write(key+","+value+"\n");
			writer.flush();
		}
		writer.close();
	}
	
	public static void addMapToMap(Map<String,List<Integer>> mapO,Map<String,Integer> map){
		for(String key:map.keySet()){
			String value=map.get(key).toString();
			if(mapO.containsKey(key)){
				List<Integer> list=mapO.get(key);
				list.add(Integer.parseInt(value));
				mapO.put(key, list);
			}else{
				List<Integer> list=new ArrayList<>();
				list.add(Integer.parseInt(value));
				mapO.put(key, list);
			}
		}
	}
	
	public static void writePersent(Map<String,Integer> map1,Map<String,Integer> map2,Map<String,Integer> map3,Map<String,Integer> map4,BufferedWriter writer) throws IOException{
		writer.write("ETC行程所占比例,平均每月出行1次,平均每月出行1-5次,平均每月出行5-10次,平均每月出行10次以上"+"\n");
		writer.flush();
		Map<String,List<Integer>> mapOut=new HashMap<>();
		addMapToMap(mapOut,map1);
		addMapToMap(mapOut,map2);
		addMapToMap(mapOut,map3);
		addMapToMap(mapOut,map4);
		for(String key:mapOut.keySet()){
			String line=key+",";
			List<Integer> list=mapOut.get(key);
			for(int i=0;i<list.size();i++){
				if(i!=list.size()-1){
					line+=list.get(i)+",";
				}else{
					line+=list.get(i);
				}
			}
			writer.write(line+"\n");
			writer.flush();
		}
		writer.close();
	}
	
	public static void readResult(String path) throws IOException{
		BufferedReader reader=getReader(path,"GBK");
//		BufferedReader reader=getReader(etcOut,"GBK");
		String line="";
		int amount=0;
		int count=0;
		Map<String,Integer> map1=new HashMap<>();
		Map<String,Integer> map2=new HashMap<>();
		Map<String,Integer> map3=new HashMap<>();
		Map<String,Integer> map4=new HashMap<>();
		initMap(map1);
		initMap(map2);
		initMap(map3);
		initMap(map4);
		String[] p=path.split("/");
		String pp="";
		for(int i=0;i<p.length-1;i++){
			pp+=p[i]+"/";
		}
		while((line=reader.readLine())!=null){
			String[] data=line.split(",",7);
			String cs=data[3];
			String etcCs=data[5];
			String mtcCs=data[6];
			amount=Integer.parseInt(cs)+Integer.parseInt(etcCs)+Integer.parseInt(mtcCs);
			if(amount<=22){
				getPersent(map1,etcCs,mtcCs);
			}else if(amount<=110){
				getPersent(map2,etcCs,mtcCs);
			}else if(amount<=220){
				getPersent(map3,etcCs,mtcCs);
			}else{
				getPersent(map4,etcCs,mtcCs);
			}
//			getPersent(map1,etcCs,mtcCs);
		}
		reader.close();
		String outPath1=pp+"etc出行占比.csv";
		BufferedWriter writer1=getWriter(outPath1,"GBK");
		writePersent(map1,map2,map3,map4,writer1);
	}
	
	public static void readEtcResult(String path) throws IOException{
		BufferedReader reader=getReader(path,"GBK");
		String line="";
		int amount=0;
		int count=0;
		Map<String,Integer> map1=new HashMap<>();
		Map<String,Integer> map2=new HashMap<>();
		while((line=reader.readLine())!=null){
			String[] data=line.split(",",5);
			String etcCs=data[3];
			String mtcCs=data[4];
			amount=Integer.parseInt(etcCs)+Integer.parseInt(mtcCs);
			if(amount<=10){
				getPersent(map1,etcCs,mtcCs);
			}else{
				getPersent(map2,etcCs,mtcCs);
			}
		}
		reader.close();
		String outPath1="";
		String outPath2="";
		if(path.endsWith("K.csv")){
			outPath1="F:/重庆/办理ETC前后分析/频次分类/客车/etc车辆etc出行占比1.csv";
			outPath2="F:/重庆/办理ETC前后分析/频次分类/客车/etc车辆etc出行占比2.csv";
		}else if(path.endsWith("H.csv")){
			outPath1="F:/重庆/办理ETC前后分析/频次分类/货车/etc车辆etc出行占比1.csv";
			outPath2="F:/重庆/办理ETC前后分析/频次分类/货车/etc车辆etc出行占比2.csv";
		}
		BufferedWriter writer1=getWriter(outPath1,"GBK");
		writePersent(map1,writer1);
		BufferedWriter writer2=getWriter(outPath2,"GBK");
		writePersent(map2,writer2);
	}
	
	public static long getDayDiff(String a,String b) throws ParseException{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Date d1=df.parse(a);
		Date d2=df.parse(b);
		return (d1.getTime()-d2.getTime())/(1000*3600*24);
	}
	
	public static void compare() throws IOException, ParseException{
		BufferedReader reader=getReader(etcAndMtcOut,"GBK");
		BufferedWriter writer=getWriter("F:/重庆/前后频次对比.csv","GBK");
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		int amount=0;
		int count=0;
		String line="";
		while((line=reader.readLine())!=null){
			amount++;
			String[] data=line.split(",",7);
			String cs=data[3];
			String firstMtcTime=data[2];
			String firstEtcTime=data[4];
			String etcCs=data[5];
			String mtcCs=data[6];
//			long a=getDayDiff(firstEtcTime,firstMtcTime);
//			long b=getDayDiff("2016-12-31",firstEtcTime);
//			if(Math.abs(a-b)<100){
//				int k=Integer.parseInt(etcCs)+Integer.parseInt(mtcCs)-Integer.parseInt(cs);
//				writer.write(k+"\n");
//				writer.flush();
//				count++;
//			}
			int k=Integer.parseInt(etcCs)+Integer.parseInt(mtcCs)-Integer.parseInt(cs);
			writer.write(k+"\n");
			writer.flush();
		}
		reader.close();
		writer.close();
		System.out.println(count);
		System.out.println(amount);
	}
	
	public static void compareOd() throws IOException{
		BufferedReader reader=getReader(etcAndMtcOutOd,"GBK");
		String line="";
		while((line=reader.readLine())!=null){
			String[] data=line.split(",");
			
		}
		reader.close();
	}
	
	public static String getValue(Cell cell){
		if(cell.getCellType()==cell.CELL_TYPE_NUMERIC){
			return ""+cell.getNumericCellValue();
		}else if(cell.getCellType()==cell.CELL_TYPE_STRING){
			return cell.getStringCellValue();
		}
		else{
			return cell.getStringCellValue();
		}
	}
	
	public static void readStationDistance(String path,String path2){
		BufferedReader reader=getReader(path,"GBK");
		List<String> list=new ArrayList<>();
		Map<String,String> map=new LinkedMap<>();
		try{
			String line="";
			int count=0;
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				if(count==0){
					for(int i=0;i<data.length;i++){
						String[] value=data[i].split("\\|");
						if(i==0){
							list.add(data[i]);
						}else{
							String key=""+Integer.parseInt(value[0]);
							list.add(key);
						}
					}
				}else{
					String[] value=data[0].split("\\|");
					for(int i=1;i<data.length;i++){
						if(i!=count){
							String keyIn=list.get(count)+"-"+list.get(i);
							map.put(keyIn, data[i]);
						}
					}
				}
				count++;
			}
			reader.close();
			BufferedWriter writer=getWriter(path2,"GBK");
			for(String key:map.keySet()){
				String value=map.get(key);
				writer.write(key+","+value+"\n");
				writer.flush();
			}
			writer.close();
		}catch (IOException ex) {  
            ex.printStackTrace();  
        }  
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
	public static void avgDistance(String path,String outPath) throws IOException, JSONException{
		BufferedReader reader=getReader(path,"GBK");
		BufferedReader readerOd=getReader(odStation,"GBK");
		BufferedWriter writer=getWriter(outPath,"GBK");
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
			if(p<0.5){
				writer.write(data[0]+";"+data[1]+";"+cs+";"+avgDistanceOd+";"+data[4]+";"+etcCs+";"+avgDistanceEtcOd+";"+mtcCs+";"+avgDistanceMtcOd+"\n");
				writer.flush();
			}
		}
		reader.close();
		writer.close();
	}
	
	public static void compareAvgOdDistance(String path,String outPath) throws IOException{
		Map<String,Integer> map=new HashMap<>();
		BufferedReader reader=getReader(path,"GBK");
		BufferedWriter writer=getWriter(outPath,"GBK");
		String line="";
		int a=0,b=0;
		int count=0;
		while((line=reader.readLine())!=null){
			count++;
			String[] data=line.split(";",9);
			String etcDistance=data[6];
			String mtcDistance=data[8];
			if(!etcDistance.equals("no od match")&&!mtcDistance.equals("no od match")){
				if(mtcDistance.equals("")){
					a++;
				}else{
					float etcDis=Float.parseFloat(etcDistance);
					float mtcDis=Float.parseFloat(mtcDistance);
					DecimalFormat decimalFormat=new DecimalFormat(".00");
					String persent=decimalFormat.format(etcDis-mtcDis);
					writer.write(persent+"\n");
					writer.flush();
				}
			}
		}
		reader.close();
		writer.close();
		System.out.println("etc大于mtc："+a);
		System.out.println("mtc大于etc："+b);
		System.out.println("总数："+count);
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
	
	public static void getMostOd(String path) throws IOException, JSONException{
		BufferedReader reader=getReader(path,"GBK");
		BufferedWriter writer=getWriter(etcAndMtcOutMostOdK,"GBK");
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
			writer.write(data[0]+";"+data[1]+";"+cs+";"+objMostOd.toString()+";"+data[4]+";"+etcCs+";"+objMostEtcOd.toString()+";"+mtcCs+";"+objMostMtcOd.toString()+"\n");
			writer.flush();
		}
		reader.close();
		writer.close();
	}
	public static void getMostEtcOd(String path) throws IOException, JSONException{
		BufferedReader reader=getReader(path,"GBK");
		BufferedWriter writer=getWriter(etcOutMostOd,"GBK");
		String line="";
		while((line=reader.readLine())!=null){
			String[] data=line.split(";",6);
			String etcCs=data[2];
			String etcOd=data[3];
			String mtcCs=data[4];
			String mtcOd=data[5];
			int amountCs=Integer.parseInt(etcCs)+Integer.parseInt(mtcCs);
			float p=Float.parseFloat(etcCs)/(Float.parseFloat(etcCs)+Float.parseFloat(mtcCs));
			JSONObject objEtcOd=new JSONObject(etcOd);
			JSONObject objMtcOd=new JSONObject(mtcOd);
			JSONObject objMostEtcOd=getMostOdObj(objEtcOd);
			JSONObject objMostMtcOd=getMostOdObj(objMtcOd);
			writer.write(data[0]+";"+data[1]+";"+etcCs+";"+objMostEtcOd.toString()+";"+mtcCs+";"+objMostMtcOd.toString()+"\n");
			writer.flush();
		}
		reader.close();
		writer.close();
	}
	
	public static void classify() throws IOException{
		BufferedReader reader=getReader(etcAndMtcOutOd,"GBK");
		BufferedWriter writerS10s90=getWriter(s10s90,"GBK");
		BufferedWriter writerS10b90=getWriter(s10b90,"GBK");
		BufferedWriter writerB10s90=getWriter(b10s90,"GBK");
		BufferedWriter writerB10b90=getWriter(b10b90,"GBK");
		String line="";
		while((line=reader.readLine())!=null){
			String[] data=line.split(";",9);
			String cs=data[2];
			String etcCs=data[5];
			String mtcCs=data[7];
			int amountCs=Integer.parseInt(cs)+Integer.parseInt(etcCs)+Integer.parseInt(mtcCs);
			float p=Float.parseFloat(etcCs)/(Float.parseFloat(etcCs)+Float.parseFloat(mtcCs));
			if(amountCs<10){
				if(p<=0.9){
					writerS10s90.write(line+"\n");
					writerS10s90.flush();
				}else{
					writerS10b90.write(line+"\n");
					writerS10b90.flush();
				}
			}else{
				if(p<=0.9){
					writerB10s90.write(line+"\n");
					writerB10s90.flush();
				}else{
					writerB10b90.write(line+"\n");
					writerB10b90.flush();
				}
			}
		}
		reader.close();
		writerS10s90.close();
		writerS10b90.close();
		writerB10s90.close();
		writerB10b90.close();
	}
	
	public static void sameOd(String path,String outPath) throws IOException, JSONException{
		BufferedReader reader=getReader(path,"GBK");
//		BufferedWriter writer=getWriter(outPath,"GBK");
		String line="";
		int countEtc=0;
		int countMtc=0;
		int countBoth=0;
		int countNone=0;
		int amount=0;
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
//			if(amountCs>10&&p<0.5){
				JSONObject objOd=new JSONObject(od);
				JSONObject objEtcOd=new JSONObject(etcOd);
				JSONObject objMtcOd=new JSONObject(mtcOd);
				Iterator itEtc=objEtcOd.keys();
				boolean etcFlag=false;
				boolean mtcFlag=false;
				while(itEtc.hasNext()){
					String key=(String) itEtc.next();
					String[] k=key.split("-");
					String key1=k[1]+"-"+k[0];
					if(objOd.has(key)||objOd.has(key1)){
						etcFlag=true;
						break;
					}
				}
				Iterator itMtc=objMtcOd.keys();
				while(itMtc.hasNext()){
					String key=(String) itMtc.next();
					String[] k=key.split("-");
					String key1=k[1]+"-"+k[0];
					if(objOd.has(key)||objOd.has(key1)){
						mtcFlag=true;
						break;
					}
				}
				if(etcFlag&&mtcFlag){
					countBoth++;
				}else if(etcFlag){
					countEtc++;
				}else if(mtcFlag){
					countMtc++;
				}else{
					countNone++;
				}
				amount++;
			}	
//		}
		reader.close();
//		writer.close();
		System.out.println("countEtc:"+countEtc);
		System.out.println("countMtc:"+countMtc);
		System.out.println("countBoth:"+countBoth);
		System.out.println("countNone:"+countNone);
		System.out.println("amount:"+amount);
	}
	
	public static void filter2017(String path,String outPath) throws IOException{
		BufferedReader reader=getReader(path,"GBK");
		BufferedWriter writer=getWriter(outPath,"GBK");
		String line="";
		while((line=reader.readLine())!=null){
			String[] data=line.split(",",7);
			String firstMtcTime=data[2];
			String firstEtcTime=data[4];
			if(firstMtcTime.startsWith("2017")||firstEtcTime.startsWith("2017")){
				writer.write(line+"\n");
				writer.flush();
			}
		}
		reader.close();
		writer.close();
	}
	
	public static void getEtcPlate(String in,String in2) throws IOException{
		Set<String> set=new HashSet<>();
		BufferedReader reader=getReader(in,"GBK");
		BufferedReader reader2=getReader(in2,"GBK");
		BufferedWriter writer=getWriter("C:/Users/pengrui/Desktop/etcPlate.csv","GBK");
		String line="";
		while((line=reader.readLine())!=null){
			String[] data=line.split(",",5);
			String plate=data[0];
			set.add(plate);
		}
		reader.close();
		
		String line2="";
		while((line2=reader2.readLine())!=null){
			String[] data=line2.split(",",7);
			String plate=data[0];
			String etcCs=data[5];
			String mtcCs=data[6];
			float p=Float.parseFloat(etcCs)/(Float.parseFloat(etcCs)+Float.parseFloat(mtcCs));
			if(p>=0.7){
				set.add(plate);
			}
		}
		reader2.close();
		
		for(String key:set){
			writer.write(key+"\n");
			writer.flush();
		}
		writer.close();
	}

	public static void addMonthCard(Map<String,Set<String>> map,String time,String card){
		if(map.containsKey(time)){
			Set<String> set=map.get(time);
			set.add(card);
			map.put(time, set);
		}else{
			Set<String> set=new HashSet<>();
			map.put(time, set);
		}
	}
	public static void readIn1(String in,Map<String,Set<String>> map0,Map<String,Set<String>> map1,Map<String,Set<String>> map2,Map<String,Set<String>> map3,Map<String,String> mapPlate) throws NumberFormatException, IOException, JSONException, ParseException{
		DateFormat df=new SimpleDateFormat("yyyy-dd");
		Date d1=df.parse("2015-12");
		Date d2=df.parse("2017-11");
		Set<String> setEtcPlate=new HashSet<>();
		Set<String> setAllPlate=new HashSet<>();
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			String path=in+"/"+list.get(i);
			BufferedReader reader=getReader(path,"GBK");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",8);
				if(data.length==8){
					String card=data[0];
					String plate=data[1];
					String cx=data[2];
					String enTime=data[3];
					String isEtc=data[7];
					if(plate.startsWith("渝")){
						if(isEtc.equals("1")){
							setEtcPlate.add(plate+","+cx);
							String time=enTime.substring(0,7);
							Date month=df.parse(time);
							if(month.after(d1)&&month.before(d2)){
								if(cx.equals("0")){
									addMonthCard(map0,time,card);
								}else if(cx.equals("1")){
									addMonthCard(map1,time,card);
								}else if(cx.equals("2")){
									addMonthCard(map2,time,card);
								}else{
									addMonthCard(map3,time,card);
								}
							}
						}
						setAllPlate.add(plate+","+cx);
					}
				}
			}
			reader.close();
			mapPlate.put(list.get(i).substring(0,6), setEtcPlate.size()+","+setAllPlate.size());
			System.out.println("read "+path+" finish!");
		}
	}
	
	public static void readMap(Map<String,Set<String>> map,BufferedWriter writer) throws IOException{
		for(String key:map.keySet()){
			Set<String> set=map.get(key);
			int value=set.size();
			writer.write(key+","+value+"\n");
			writer.flush();
		}
		writer.close();
	}
	public static void getCardCount(String in,String in2) throws IOException, ParseException, NumberFormatException, JSONException{
		Map<String,Set<String>> map0=new HashMap<>();
		Map<String,Set<String>> map1=new HashMap<>();
		Map<String,Set<String>> map2=new HashMap<>();
		Map<String,Set<String>> map3=new HashMap<>();
		
		Map<String,String> mapPlate=new HashMap<>();
		readIn1(in,map0,map1,map2,map3,mapPlate);
		readIn1(in2,map0,map1,map2,map3,mapPlate);
		BufferedWriter writer0=getWriter("F:/重庆/0型渝籍车每月的卡数量.csv","GBK");
		BufferedWriter writer1=getWriter("F:/重庆/1型渝籍车每月的卡数量.csv","GBK");
		BufferedWriter writer2=getWriter("F:/重庆/2型渝籍车每月的卡数量.csv","GBK");
		BufferedWriter writer3=getWriter("F:/重庆/2型以上渝籍车每月的卡数量.csv","GBK");
		BufferedWriter writer=getWriter("F:/重庆/已装ETC占渝籍车比例.csv","GBK");
		readMap(map0,writer0);
		readMap(map1,writer1);
		readMap(map2,writer2);
		readMap(map3,writer3);
		
		for(String key:mapPlate.keySet()){
			String value=mapPlate.get(key);
			writer.write(key+","+value+"\n");
			writer.flush();
		}
		writer.close();
	}
	
	public static void getNewCard(String in) throws IOException, ParseException{
		Map<String,Integer> map=new HashMap<>();
		DateFormat df=new SimpleDateFormat("yyyy-dd");
		Date d1=df.parse("2015-12");
		Date d2=df.parse("2017-11");
		BufferedReader reader=getReader(in,"GBK");
		String line="";
		while((line=reader.readLine())!=null){
			String[] data=line.split(",",7);
			String month=data[4].substring(0, 7);
			if(df.parse(month).after(d1)&&df.parse(month).before(d2)){
				if(map.containsKey(month)){
					map.put(month, map.get(month)+1);
				}else{
					map.put(month, 1);
				}
			}
		}
		reader.close();
		BufferedWriter writer=getWriter("C:/Users/pengrui/Desktop/monthNewCard.csv","GBK");
		for(String key:map.keySet()){
			int value=map.get(key);
			writer.write(key+","+value+"\n");
			writer.flush();
		}
		writer.close();
	}
	
	public static void readIn2(String in,int[] count,Map<String,String> map,Set<String> setEtcPlate,Set<String> setAllPlate,Map<String,String> mapMtc) throws NumberFormatException, IOException, JSONException, ParseException{
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			int countMtc=0;
			int countEtc=0;
			String month=list.get(i).substring(0,6);
			String path=in+"/"+list.get(i);
			BufferedReader reader=getReader(path,"GBK");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",8);
				if(data.length==8){
					String card=data[0];
					String plate=data[1];
					String cx=data[2];
					String enTime=data[3];
					String isEtc=data[7];
					if(plate.startsWith("渝")){
						setAllPlate.add(plate+","+cx);
						count[0]++;
						if(cx.equals("0")){
							count[1]++;
						}
						if(isEtc.equals("1")){
							countEtc++;
							setEtcPlate.add(plate+","+cx);
							count[2]++;
							if(cx.equals("0")){
								count[3]++;
							}
						}else{
							countMtc++;
						}
					}
				}
			}
			reader.close();
			map.put(month,setEtcPlate.size()+","+setAllPlate.size());
			mapMtc.put(month, countEtc+","+countMtc);
			System.out.println("read "+path+" finish!");
		}
	}
	
	public static void getCount(String in,String in2) throws IOException, ParseException, NumberFormatException, JSONException{
		int[] count=new int[4];
		Map<String,String> map=new LinkedMap<>();
		Set<String> setAllPlate=new HashSet<>();
		Set<String> setEtcPlate=new HashSet<>();
		Map<String,String> mapMtc=new LinkedMap<>();
		readIn2(in,count,map,setEtcPlate,setAllPlate,mapMtc);
		readIn2(in2,count,map,setEtcPlate,setAllPlate,mapMtc);
		System.out.println("高速通行量共："+count[0]);
		System.out.println("小型客车共："+count[1]);
		System.out.println("ETC交易共："+count[2]);
		System.out.println("ETC小型客车交易共："+count[3]);
		
//		BufferedWriter writer=getWriter("C:/Users/pengrui/Desktop/monthNewCard2.csv","GBK");
//		for(String key:map.keySet()){
//			String value=map.get(key);
//			writer.write(key+","+value+"\n");
//			writer.flush();
//		}
//		writer.close();
		
		BufferedWriter writerMtc=getWriter("C:/Users/pengrui/Desktop/monthMtc.csv","GBK");
		for(String key:mapMtc.keySet()){
			String value=mapMtc.get(key);
			String[] v=value.split(",",2);
			writerMtc.write(key+","+v[0]+","+v[1]+"\n");
			writerMtc.flush();
		}
		writerMtc.close();
	}
	
	public static void read(String in) throws IOException{
		BufferedReader reader=getReader(in,"GBK");
		String line="";
		int allAmount=0;
		int amount110=0;
		while((line=reader.readLine())!=null){
			String[] data=line.split(",",7);
			String cs=data[3];
			String etcCs=data[5];
			String mtcCs=data[6];
			int amount=Integer.parseInt(cs)+Integer.parseInt(etcCs)+Integer.parseInt(mtcCs);
			allAmount+=amount;
			if(amount<=110){
				amount110+=amount;
			}
		}
		reader.close();
		System.out.println("所有通行量："+allAmount);
		System.out.println("小于等于5次的所有通行量："+amount110);
	}
	public static void read1(String in,String in2) throws IOException{
		BufferedReader reader=getReader(in,"GBK");
//		BufferedReader reader2=getReader(in2,"GBK");
		int count=0;
		int count2=0;
		String line="";
		while((line=reader.readLine())!=null){
			count++;
		}
		reader.close();
		System.out.println(in+" finish!");
//		String line2="";
//		while((line2=reader2.readLine())!=null){
//			count2++;
//		}
//		reader2.close();
		System.out.println("201601："+count);
//		System.out.println("201710："+count2);
	}
	
	public static void main(String[] args) throws JSONException, IOException, ParseException{
//		readCq(in,out);
//		compare();
//		readStationDistance(station,odStation);
//		avgDistance();
//		compareAvgOdDistance();
//		classify();
//		getMostOd(etcAndMtcOutOd);
//		getMostEtcOd(etcOutOd);
//		readEtcResult();
//		sameOd(etcAndMtcOutMostOd,etcLessMtcMore);
//		avgDistance(etcLessMtcMore,etcLessMtcMoreDistance);
//		compareAvgOdDistance(etcLessMtcMoreDistance);
//		write(f);
//		readResult(etcAndMtcOutK);
//		readResult(etcAndMtcOutH);
//		readEtcResult(etcOutK);
//		readEtcResult(etcOutH);
//		read2017File("F:/重庆/2017年数据");
//		write(f,"F:/重庆/2017short");
//		readResult(etcAndMtc0K);
//		readResult(etcAndMtc1K);
//		readResult(etcAndMtc2H);
//		readResult(etcAndMtcH);
//		filter2017(etcAndMtcK,"F:/重庆/办理ETC前后分析/2017频次分类/客车/2017etcAndMtcOutK.csv");
//		readResult(etcAndMtcH);
		
//		avgDistance(etcAndMtcOutOdK,"F:/重庆/办理ETC前后分析/2016-2017频次加od分类/客车/etcLess50p.csv");
//		compareAvgOdDistance("F:/重庆/办理ETC前后分析/2016-2017频次加od分类/客车/etcLess50p.csv","F:/重庆/办理ETC前后分析/2016-2017频次加od分类/客车/avgDistanceGap.csv");
//		getMostOd(etcAndMtcOutOdK);
//		sameOd(etcAndMtcOutMostOdK,"");
//		getEtcPlate(etc0K,etcAndMtc0K);
//		getCardCount(f,"F:/重庆/2017short");
//		read(etcAndMtc0K);
//		read1("D:/cq/201601/all.csv","D:/2017年重庆数据/2017年9月高速出口刷卡数据.txt");
		
//		getCount(f,"F:/重庆/2017short");
		readFile("D:/cq/");
	}
}
