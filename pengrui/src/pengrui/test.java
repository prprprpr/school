package pengrui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.map.LinkedMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class test {
	private static String in1="C:/Users/pengrui/Desktop/card.csv";
	private static String in="C:/Users/pengrui/Desktop/odh.csv";
	
	public static void writeMap(Map<String,Integer> map,BufferedWriter writer)throws IOException{
		Set set=map.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();
			String value=entries[i].getValue().toString();
			writer.write(key+","+value+"\n");
			writer.flush();
		}
	}
	public static HashMap<String,Integer> putValue(String str) {  
        return new HashMap<String,Integer>();  
    }  
	public static void put(Map<String, HashMap<String,Integer>> map1,String a,String b){
		Map<String,Integer> map=map1.computeIfAbsent(a, k -> putValue(k));
		if(map.size()==0){
			map.put(b, 1);
		}else{
			if(map.containsKey(b))
				map.put(b, map.get(b)+1);
			else
				map.put(b, 1);
		}
	}
	public static void put(Map<String, HashMap<String,Integer>> map1,String a,HashMap<String,Integer> b){
		b=map1.computeIfAbsent(a, k -> putValue(k));
	}
	
	public static void writeOriginalMap(Map<String, HashMap<String,Integer>> originalMap,BufferedWriter writer) throws IOException{
		Set set=originalMap.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();//key为车牌号
			Map<String,Integer> map=(Map<String, Integer>) entries[i].getValue();
			Set set1=map.entrySet();
			Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
			for(int j=0;j<entries1.length;j++){
				String key1=entries1[j].getKey().toString();
				String value=entries1[j].getValue().toString();
				writer.write(key+","+key1+","+value+"\n");
				writer.flush();
			}
		}
	}
	private static void add(Map<String,Integer> map,String a){
		if(map.size()==0){
			map.put(a, 1);
		}else{
			if(map.containsKey(a)){
				map.put(a, map.get(a)+1);
			}else{
				map.put(a, 1);
			}
		}
	}
	public static void read(String path){
		File file=new File(path);
		File fileoutEn=new File("C:/Users/pengrui/Desktop/enStation.csv");
		File fileoutEx=new File("C:/Users/pengrui/Desktop/exStation.csv");
		File fileoutOd=new File("C:/Users/pengrui/Desktop/od.csv");
		Map<String, HashMap<String,Integer>> mapEnStation=new HashMap<>();
		Map<String, HashMap<String,Integer>> mapExStation=new HashMap<>();
		Map<String,Integer> mapEn=new HashMap<>();
		Map<String,Integer> mapEx=new HashMap<>();
		Map<String,Integer> mapOd=new HashMap<>();
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			InputStreamReader in=new InputStreamReader(new FileInputStream(file),encoding1);
			BufferedReader reader=new BufferedReader(in);
			
			OutputStreamWriter outEn=new OutputStreamWriter(new FileOutputStream(fileoutEn),encoding);
			BufferedWriter writerEn=new BufferedWriter(outEn);
			OutputStreamWriter outEx=new OutputStreamWriter(new FileOutputStream(fileoutEx),encoding);
			BufferedWriter writerEx=new BufferedWriter(outEx);
			OutputStreamWriter outOd=new OutputStreamWriter(new FileOutputStream(fileoutOd),encoding);
			BufferedWriter writerOd=new BufferedWriter(outOd);
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				String exTime=data[2].substring(0,7);
				String enStation=data[3];
				String exStation=data[4];
				Date date=sdf.parse(exTime);
				Date d=sdf.parse("2016-12");
				if(date.before(d)){
//					put(mapEnStation,exTime,enStation);
//					put(mapExStation,exTime,exStation);
					add(mapEn,enStation);
					add(mapEx,exStation);
					add(mapOd,enStation+"-"+exStation);
				}
			}
			reader.close();
//			writeOriginalMap(mapEnStation,writerEn);
			writeMap(mapEn,writerEn);
			writerEn.close();
//			writeOriginalMap(mapExStation,writerEx);
			writeMap(mapEx,writerEx);
			writerEx.close();
			
			writeMap(mapOd,writerOd);
			writerOd.close();
		}catch(Exception e){
			System.out.println("读取文件出错");
			e.printStackTrace();
		}
	}
	public static void readOd(String path){
		File file=new File(path);
		File fileout=new File("C:/Users/pengrui/Desktop/1.csv");
		HashMap<String,Integer> mapIn=new HashMap<>();
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			InputStreamReader in=new InputStreamReader(new FileInputStream(file),encoding1);
			BufferedReader reader=new BufferedReader(in);
			
			OutputStreamWriter out=new OutputStreamWriter(new FileOutputStream(fileout),encoding);
			BufferedWriter writer=new BufferedWriter(out);
			
			String line="";
			String flag="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",2);
				String a=data[0];
				String b=data[1];
				String[] aa=a.split("->",2);
				String city=aa[0];
				if(flag.equals("")){
					flag=city;
				}
				if(flag.equals(city)){
					mapIn.put(a, Integer.parseInt(b));
				}else{
					String f="";
					int max=0;
					for(String key:mapIn.keySet()){
						if(max<mapIn.get(key)){
							max=mapIn.get(key);
						}
					}
					for(String key:mapIn.keySet()){
						if(max==mapIn.get(key)){
							System.out.println(key+":"+max);
						}
					}
					mapIn.clear();
					mapIn.put(a, Integer.parseInt(b));
					flag=city;
				}
			}
			reader.close();
		}catch(Exception e){
			System.out.println("读取文件出错");
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException, ParseException, JSONException{
//		read(in1);
//		read1();
//		readOd(in);
		String a="1,2,3";
		String[] data=a.split(",",4);
		System.out.println(data[2]);
	}
}
