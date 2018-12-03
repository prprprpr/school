package practice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class test {
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
	public static void readCMid(String path,Map<Integer,String> idWithPointMessage){
		BufferedReader reader=getReader(path,"GBK");
		String line="";
		int lineId=0;
		try{
			while((line=reader.readLine())!=null){
				lineId++;
				String[] data=line.split(",",10);
				String inLinkId=data[3].replaceAll("\"", "").trim();
				String outLinkId=data[4].replaceAll("\"", "").trim();
				String CondType=data[5].replaceAll("\"", "").trim();
				idWithPointMessage.put(lineId, inLinkId+";"+outLinkId+";"+CondType);
			}
			reader.close();
//			System.out.println("Rhebei.mid:"+lineId);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void addGpsPointCmid(String path,Map<Integer,String> idWithPointMessage){
		BufferedReader reader=getReader(path,"GBK");
		BufferedWriter writer=getWriter("C:/Users/pengrui/Desktop/mid.csv","GBK");
		String line="";
		int lineId=0;
		try{
			while((line=reader.readLine())!=null){
				if(line.startsWith("Point")){
					String f="";
					lineId++;
					String[] data=line.split(" ",3);
					f+=data[1]+" "+data[2];
					String value=idWithPointMessage.get(lineId);
					value=value+";"+f;
					idWithPointMessage.put(lineId, value);
				}
			}
			reader.close();
			System.out.println("Rhebei.mif:"+lineId);
			
			Map<String,String> mapTollCollection=new HashMap<>();
			Set setPoint=idWithPointMessage.entrySet();
			Map.Entry[] entriesPoint = (Map.Entry[])setPoint.toArray(new Map.Entry[setPoint.size()]);
			for(int i=0;i<entriesPoint.length;i++){
				String value=entriesPoint[i].getValue().toString();
				String[] v=value.split(";",4);
				String inLinkId=v[0];
				String outLinkId=v[1];
				String contentType=v[2];
				String gps=v[3];
				if(contentType.equals("3")){
					mapTollCollection.put(inLinkId, gps);
					mapTollCollection.put(outLinkId, gps);
					writer.write(inLinkId+","+gps+"\n");
					writer.write(outLinkId+","+gps+"\n");
				}
			}
			writer.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void read(String in) throws IOException{
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			String inPath=in+"/"+list.get(i);
			BufferedReader reader=getReader(inPath,"GBK");
			String line="";
			double a=0;
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				double length=Double.parseDouble(data[3]);
				a+=length;
			}
			reader.close();
			System.out.println(inPath+":"+a);
		}
	}
	
	public static void readCl(String in1,String in2,String out){
		Set<String> carSet=new HashSet<>();
		try{
			BufferedReader reader=getReader(in1,"utf-8");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				String plate=data[0];
				String cx=data[1];
				carSet.add(plate);
			}
			reader.close();
			
			System.out.println(carSet.size());
			reader=getReader(in2,"utf-8");
			BufferedWriter writer=getWriter(out,"gbk");
			line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				String inp=data[9];
				String outp=data[10];
				String fee=data[2];
				if(inp.equals(outp)){
					String plate=inp.split("_",2)[0];
					if(carSet.contains(plate)){
						writer.write(inp+","+fee+"\n");
					}
				}
			}
			reader.close();
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
    public static void main(String[] args) throws IOException {
//    	String in="G:/地图/djjkstra最短路径输出/OD最短路径（不同收费广场）/半壁山收费站/北戴河收费站";
//    	read(in);
//    	readCl("D:/truck_cluster_result.txt","D:/201801.csv","D:/fee.csv");
    	System.out.println("324".compareTo("3"));
    } 
}
