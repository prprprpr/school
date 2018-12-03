package bss;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class topologyV2 {
	/**
	 * 读文件
	 * @param in 读文件路径
	 * @param encoding	文件格式
	 * @return	返回BufferedReader
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
	public static void add(Map<String,HashSet<String>> snodeStart,String key,String value){
		if(snodeStart.containsKey(key)){
			HashSet<String> set=snodeStart.get(key);
			set.add(value);
			snodeStart.put(key, set);
		}else{
			HashSet<String> set=new HashSet<String>();
			set.add(value);
			snodeStart.put(key, set);
		}
	}
	/**
	 * 先读Rchongqing.mid文件，以行号为key，路链ID，道路属性，SNode，ENode，路链长度，通行方向，为值。同时记录以Snode为起点的路链ID，建立hash。以Enode为终点的路链ID，建立hash。记录所有的行数；
	 * @param path Rchongqing.mid文件路径
	 * @param idWithLinkMessage 以行号为key，路链ID，道路属性，SNode，ENode，路链长度，通行方向为值的hashmap
	 * @param snodeStart Snode为key，路链ID为值建立的hashmap
	 * @param enodeEnd Enode为key，路链ID为值建立的hashmap
	 */
	public static void readRMid(String path,Map<Integer,String> idWithLinkMessage,Map<String,HashSet<String>> snodeStart,Map<String,HashSet<String>> enodeEnd){
		BufferedReader reader=getReader(path,"GBK");
		String line="";
		int lineId=0;
		try{
			while((line=reader.readLine())!=null){
				lineId++;
				String[] data=line.split(",",42);
				String linkId=data[1].replaceAll("\"", "").trim();
				String kind=data[3].replaceAll("\"", "").trim();
				String snode=data[9].replaceAll("\"", "").trim();
				String enode=data[10].replaceAll("\"", "").trim();
				String length=data[12].replaceAll("\"", "").trim();
				String direction=data[5].replaceAll("\"", "").trim();
				idWithLinkMessage.put(lineId, linkId+";"+kind+";"+snode+";"+enode+";"+length+";"+direction);
				if(direction.equals("2")){
					add(snodeStart,snode,linkId);
					add(enodeEnd,enode,linkId);
				}else if(direction.equals("3")){
					add(snodeStart,enode,linkId);
					add(enodeEnd,snode,linkId);
				}else{
					add(snodeStart,snode,linkId);
					add(enodeEnd,enode,linkId);
					add(snodeStart,enode,linkId);
					add(enodeEnd,snode,linkId);
				}
			}
			reader.close();
			System.out.println("Rhebei.mid:"+lineId);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 读取Rchongqing.mif文件中，以文件中PLine出现次数，对应行号，将经纬度的值加入路链中；
	 * @param path Rchongqing.mif文件路径
	 * @param idWithLinkMessage 以行号为key，路链ID，道路属性，SNode，ENode，路链长度，通行方向为值的hashmap
	 * @return 以行号为key，路链ID，道路属性，SNode，ENode，路链长度，通行方向，路链点集合的经纬度为值的hashmap
	 */
	public static void addGpsPoint(String path,Map<Integer,String> idWithLinkMessage){
		BufferedReader reader=getReader(path,"GBK");
		String line="";
		int lineId=0;
		int flag=-1;
		String f2="";
		try{
			while((line=reader.readLine())!=null){
				String f="";
				if(line.startsWith("Line")){
					lineId++;
					String[] data=line.split(" ",5);
					f+=data[1]+" "+data[2]+","+data[3]+" "+data[4];
					String value=idWithLinkMessage.get(lineId);
					value=value+";"+f;
					idWithLinkMessage.put(lineId, value);
				}
				if(line.startsWith("Pline")){
					lineId++;
					String[] data=line.split(" ",2);
					flag=Integer.parseInt(data[1])-1;
				}else{
					if(flag>=0){
						if(flag==0){
							f2+=line;
							String value=idWithLinkMessage.get(lineId);
							value=value+";"+f2;
							idWithLinkMessage.put(lineId, value);
							f2="";
						}else{
							f2+=line+",";
						}
						flag--;
					}
				}
			}
			reader.close();
			System.out.println("Rhebei.mif:"+lineId);
		}catch(Exception e){
			e.printStackTrace();
		}
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
			System.out.println("Rhebei.mid:"+lineId);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void addGpsPointCmid(String path,Map<Integer,String> idWithPointMessage){
		BufferedReader reader=getReader(path,"GBK");
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
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 输出拓扑
	 * @param outPath 输出路径
	 * @throws IOException 
	 */
	public static void getTopology(String outPath) throws IOException{
		BufferedWriter writer=getWriter(outPath,"GBK");
		String RHebeiMid="G:/最新地图/road2018q2/chongqing/road/Rchongqing.mid";
		String RHebeiMif="G:/最新地图/road2018q2/chongqing/road/Rchongqing.mif";
		String CHebeiMid="G:/最新地图/road2018q2/chongqing/road/Cchongqing.mid";
		String CHebeiMif="G:/最新地图/road2018q2/chongqing/road/Cchongqing.mif";
		Map<Integer,String> idWithLinkMessage=new HashMap<>();
		Map<Integer,String> idWithPointMessage=new HashMap<>();
		Map<String,HashSet<String>> snodeStart=new HashMap<>();
		Map<String,HashSet<String>> enodeEnd=new HashMap<>();
		readRMid(RHebeiMid,idWithLinkMessage,snodeStart,enodeEnd);
		System.out.println("read RChongqingMid finish!");
		System.out.println(idWithLinkMessage.size());
		addGpsPoint(RHebeiMif,idWithLinkMessage);
		System.out.println("read RChongqingMif finish!");
		System.out.println(idWithLinkMessage.size());
		
		readCMid(CHebeiMid,idWithPointMessage);
		System.out.println("read RChongqingMid finish!");
		System.out.println(idWithPointMessage.size());
		addGpsPointCmid(CHebeiMif,idWithPointMessage);
		System.out.println("read RChongqingMif finish!");
		System.out.println(idWithPointMessage.size());
		
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
			}
		}
		
		Set set=idWithLinkMessage.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]);
		for(int i=0;i<entries.length;i++){
			String value=entries[i].getValue().toString();
//			System.out.println(value);
			String[] v=value.split(";",7);
			String linkId=v[0];
			String kind=v[1];
			String snode=v[2];
			String enode=v[3];
			String length=v[4];
			String direction=v[5];
			String gps=v[6];
			String pre="";
			if(enodeEnd.containsKey(snode)){
				Set<String> setEnode=enodeEnd.get(snode);
				for(String key:setEnode){
					if(pre.equals("")){
						pre+=key;
					}else{
						pre+="#"+key;
					}
				}
			}
			String post="";
			if(snodeStart.containsKey(enode)){
				Set<String> setSnode=snodeStart.get(enode);
				for(String key:setSnode){
					if(post.equals("")){
						post+=key;
					}else{
						post+="#"+key;
					}
				}
			}
			String tollCollection="";
			if(mapTollCollection.containsKey(linkId)){
				tollCollection=mapTollCollection.get(linkId);
			}
			writer.write(linkId+";"+kind+";"+pre+";"+post+";"+snode+";"+enode+";"+length+";"+tollCollection+";"+direction+";"+gps+"\n");
		}
		writer.flush();
		writer.close();
	}
	public static void main(String[] args) throws IOException{
		getTopology("G:/地图/cqTopology2.csv");
	}
}

