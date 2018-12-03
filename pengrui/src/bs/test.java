package bs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class test {
	public static String in="F:/货车轨迹数据/20170526/part-m-00000";
	public static String out="F:/货车轨迹数据/trace.csv";
	public static String out1="F:/货车轨迹数据/speed0.csv";
	
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
	
	public static String stampToDate(String s){
		String rs;
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt=new Long(s);
		Date date=new Date(lt);
		rs=simpleDateFormat.format(date);
		return rs;
	}
	
	public static void getTrace(String in,String out){
		try{
			BufferedReader reader=getReader(in,"GBK");
			String line="";
			Map<String,ArrayList<String>> map=new HashMap<>();
			int flag=0;
			String curId="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				String id=data[0];
				String time=stampToDate(data[1]);
				String lng=data[2];
				String lat=data[3];
				String v=data[4];
				String fx=data[5];
				if(map.size()==0||map.containsKey(id)){
					if(map.size()==0){
						ArrayList<String> list=new ArrayList<>();
						list.add(time+","+lng+","+lat+","+v+","+fx);
						map.put(id, list);
						curId=id;
					}else{
						ArrayList<String> list=map.get(id);
						list.add(time+","+lng+","+lat+","+v+","+fx);
						map.put(id, list);
					}
				}else{
					flag++;
					BufferedWriter writer=getWriter(out+"/trace"+flag+".csv","GBK");
					ArrayList<String> list=map.get(curId);
					for(int i=0;i<list.size();i++){
						writer.write(list.get(i)+"\n");
					}
					writer.flush();
					writer.close();
					map.clear();
					list.clear();
					list.add(time+","+lng+","+lat+","+v+","+fx);
					map.put(id, list);
					curId=id;
				}
			}
			reader.close();
			System.out.println(map.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void getSpeed0(String in){
		try{
			BufferedReader reader=getReader(in,"GBK");
			BufferedWriter writer=getWriter(out1,"GBK");
			String line="";
			int i=0;
			while((line=reader.readLine())!=null&&i<1164){
				i++;
				String[] data=line.split(",");
				float speed=Float.parseFloat(data[4]);
				if(speed==0.0){
					writer.write(line+"\n");
				}
			}
			reader.close();
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		getTrace(in,"F:/货车轨迹数据/单条轨迹");
//		getSpeed0(in);
	}
}
