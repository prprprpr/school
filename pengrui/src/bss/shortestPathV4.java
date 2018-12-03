package bss;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bs.point;
import bss.Graph.Edge;
import bss.Graph.Vertex;
//�����ļ����ˣ�ʹ��ԭ��poi�����µ�ͼƥ�䣬topology2
public class shortestPathV4 {
	private static final double EARTH_RADIUS = 6378137;  
	/**
	 * �����ڵ����ƺͽڵ����ݽṹ������
	 * @param snodeVertex	��String���͵Ľڵ�Ϊkey��Vertex����Ϊֵ��hashMap����Ҫ���Խڵ�����ֽ����������ҵ��½��Ľڵ�
	 * @param snode	�ڵ�����
	 */
	public static void getSnodeVertex(Map<String,Vertex> snodeVertex,String snode){
		if(!snodeVertex.containsKey(snode)){
			Vertex v=new Vertex(snode);
			snodeVertex.put(snode, v);
		}
	}
	/**
	 * ���ͼ����ĳ���ڵ�Ϊ���ıߵļ���
	 * @param vertex_edgeList_map �Խڵ�Ϊkey���ýڵ�Ϊ���ıߵļ���Ϊvalue�ļ���
	 * @param node	���ڵ�
	 * @param edge	��
	 */
	public static void addVertexEdgeListMap(Map<Vertex, List<Edge>> vertex_edgeList_map,Vertex node,Edge edge){
		if(vertex_edgeList_map.containsKey(node)){
			List<Edge> list=vertex_edgeList_map.get(node);
			list.add(edge);
			vertex_edgeList_map.put(node, list);
		}else{
			List<Edge> list=new LinkedList<>();
			list.add(edge);
			vertex_edgeList_map.put(node, list);
		}
	}
	public static void readTopology2(String in,List<Vertex> verList,Map<Vertex, List<Edge>> vertex_edgeList_map,Map<String,String> mapToll,Map<String,Integer> indexMap){	
		Map<String,Vertex> snodeVertex=new HashMap<>();//�Խڵ�StringΪkey��VertexΪvalue����֤��Ψһ�ڵ�
		BufferedReader reader=topology.getReader(in, "GBK");
		try{
			String line="";
			int index=0;
			while((line=reader.readLine())!=null){
				String[] data=line.split(";",10);
				String linkId=data[0];
				String kind=data[1];
				String snode=data[4];
				String enode=data[5];
				double length=Double.parseDouble(data[6]);
				String tollCollectionGps=data[7];
				String direction=data[8];
				String gps=data[9];
				String[] g=gps.split(",");
				String snodeGps=g[0];
				String enodeGps=g[g.length-1];
				getSnodeVertex(snodeVertex,snode);
				getSnodeVertex(snodeVertex,enode);
				
				if(!tollCollectionGps.equals("")){
					if(snodeGps.equals(tollCollectionGps)){
						mapToll.put(snodeGps, snode);
					}else{
						mapToll.put(enodeGps, enode);
					}
				}
				if(direction.equals("0")||direction.equals("1")){
					Vertex s1=snodeVertex.get(snode);
					Vertex s2=snodeVertex.get(enode);
					verList.add(s1);
					indexMap.put(snode, index);
					index++;
					Edge edge1=new Edge(s1,s2,length,linkId);
					addVertexEdgeListMap(vertex_edgeList_map,s1,edge1);
					verList.add(s2);
					indexMap.put(enode, index);
					index++;
					Edge edge2=new Edge(s2,s1,length,linkId);
					addVertexEdgeListMap(vertex_edgeList_map,s2,edge2);
				}else if(direction.equals("2")){
					Vertex start=snodeVertex.get(snode);
					Vertex end=snodeVertex.get(enode);
					verList.add(start);
					indexMap.put(snode, index);
					index++;
					Edge edge=new Edge(start,end,length,linkId);
					addVertexEdgeListMap(vertex_edgeList_map,start,edge);
				}else{
					Vertex start=snodeVertex.get(enode);
					Vertex end=snodeVertex.get(snode);
					verList.add(start);
					indexMap.put(enode, index);
					index++;
					Edge edge=new Edge(start,end,length,linkId);
					addVertexEdgeListMap(vertex_edgeList_map,start,edge);
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private static double rad(double d){  
	    return d * Math.PI / 180.0;  
	}  
	public static double getDistance(double lat1,double lng1,double lat2,double lng2){
		double radLat1 = rad(lat1);  
		double radLat2 = rad(lat2);  
		double a = radLat1 - radLat2;  
		double b = rad(lng1) - rad(lng2);  
		double s = (float) (2 * Math.asin(  
	        Math.sqrt(  
	            Math.pow(Math.sin(a/2),2)   
	            + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)  
	        )  
        ));  
        s = (float) (s * EARTH_RADIUS);  
        s = Math.round(s * 10000) / 10000;  
        return s;  
	}
	
	 public static Graph getGraph(List<Vertex> verList,Map<Vertex, List<Edge>> vertex_edgeList_map){
	    	Map<String,Vertex> map=new HashMap<>();
			List<Vertex> verListCopy=new LinkedList<>();
			Map<Vertex, List<Edge>> vertex_edgeList_mapCopy=new HashMap<>();
			for(int i=0;i<verList.size();i++){
				Vertex v=verList.get(i);
				Vertex vc=new Vertex(v.getName(),v.getAdjuDist(),v.getParent(),v.getEdge(),v.isKnown(),v.getLat(),v.getLng());
				map.put(vc.getName(), vc);
				verListCopy.add(vc);
			}
			Set set=vertex_edgeList_map.entrySet();
			Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]);
			for(int i=0;i<entries.length;i++){
				Vertex v=(Vertex) entries[i].getKey();
				Vertex vC=map.get(v.getName());
				List<Edge> list=(List<Edge>) entries[i].getValue();
				List<Edge> listC=new LinkedList<>();
				for(int j=0;j<list.size();j++){
					Edge e=list.get(j);
					Edge eC=new Edge(map.get(e.getStartVertex().getName()), map.get(e.getEndVertex().getName()),e.getWeight(),e.getLinkId());
					listC.add(eC);
				}
				vertex_edgeList_mapCopy.put(vC, listC);
			}
			return new Graph(verListCopy,vertex_edgeList_mapCopy);
		}
	public static void getShortestPath(String in,String in2) throws IOException{
		BufferedWriter writer=topology.getWriter("G:/��ͼ/�������/hebei2.csv", "GBK");
		Map<String,List<String>> mapCheDaoGps=getCheDaoGps();
		System.out.println("read mapCheDaoGps finish!");
		
		List<Vertex> verList=new LinkedList<>();
		Map<Vertex, List<Edge>> vertex_edgeList_map=new HashMap<>();
		Map<String,Integer> indexMap=new HashMap<>();
		Map<String,String> mapToll=new HashMap<>();
		
		readTopology2(in,verList,vertex_edgeList_map,mapToll,indexMap);
		System.out.println("read Topology2 finish!");
		
		System.out.println("verList:"+verList.size());
		System.out.println("vertex_edgeList_map:"+vertex_edgeList_map.size());
		
		Map<String,List<Integer>> mapChedaoToNode=matchChedaoAndNode(mapCheDaoGps,mapToll,indexMap);
		System.out.println("mapCheDaoGps:"+mapCheDaoGps.size());
		System.out.println("mapChedaoToNode:"+mapChedaoToNode.size());
//		
//		for(String key:mapChedaoToNode.keySet()){
//			System.out.println(key+","+mapChedaoToNode.get(key));
//		}
//		
		int count=0;
		int hebeiAmount=0;
		int amount=0;
		File fileIn=new File(in2);
		List<String> list=Arrays.asList(fileIn.list());
		Graph g=getGraph(verList,vertex_edgeList_map);
		System.out.println("ok");
		for(int i=0;i<list.size();i++){
			String inPath=in2+"/"+list.get(i);
			BufferedReader reader=topology.getReader(inPath, "utf-8");
			try{
				String line="";
				while((line=reader.readLine())!=null){
					amount++;
					String[] data=line.split(",",26);
					String id=data[1];
					String outLaneId=id.substring(0, 21);
					String inLaneId=data[5].substring(0, 21);
					String inProvince=id.substring(5,7);
					String outProvince=outLaneId.substring(5,7);
					String inCx=data[14];
					String outCx=data[15];
					String inPlate=data[11];
					String outPlate=data[12];
					String weight=data[4];
					String inTime=data[6];
					String outTime=id.substring(21,35);
					String fee=data[3];
					if(inProvince.equals("13")){
						hebeiAmount++;
						if(mapChedaoToNode.containsKey(inLaneId)&&mapChedaoToNode.containsKey(outLaneId)){
							List<Integer> inNodeList=mapChedaoToNode.get(inLaneId);
							List<Integer> outNodeList=mapChedaoToNode.get(outLaneId);
							String rs="";
							if(inNodeList.size()==1&&outNodeList.size()==1){
								rs=g.dijkstraTravasal(inNodeList.get(0), outNodeList.get(0));
							}else{
								String min="";
								int flag=Integer.MAX_VALUE;
								for(int k=0;k<inNodeList.size();k++){
									for(int l=0;l<outNodeList.size();l++){
//										Graph g=getGraph(verList,vertex_edgeList_map);
										String a=g.dijkstraTravasal(inNodeList.get(k), outNodeList.get(l));
										if(!a.contains("no path!")&&flag>a.split("-->").length){
											min=a;
											flag=a.split("-->").length;
										}
									}
								}
								rs=min;
							}
							System.out.println(inPlate+";"+inCx+";"+inTime+";"+inLaneId+";"+outPlate+";"+outCx+";"+outTime+";"+outLaneId+";"+weight+";"+fee+";"+rs);
							if(rs.contains("no path!")){
								count++;
							}else{
//								writer.write(inPlate+";"+inCx+";"+inTime+";"+inLaneId+";"+outPlate+";"+outCx+";"+outTime+";"+outLaneId+";"+weight+";"+fee+";"+rs+"\n");
//								System.out.println(inPlate+";"+inCx+";"+inTime+";"+inLaneId+";"+outPlate+";"+outCx+";"+outTime+";"+outLaneId+";"+weight+";"+fee+";"+rs);
							}
						}else{
//							System.out.println(inLaneId+","+outLaneId);
						}
					}
				}
				reader.close();
			}catch(Exception error){
				error.printStackTrace();
			}
			System.out.println("read "+inPath+" finish!");
		}
		writer.flush();
		writer.close();
		System.out.println("����Ϊ������:"+count);
		System.out.println("����Ϊ��Ϊ������:"+hebeiAmount);
		System.out.println("������:"+amount);
//		Graph g=new Graph(verList,vertex_edgeList_map);
//		System.out.println("getGraph finish!");
//		
//		System.out.println(g.dijkstraTravasal(indexMap.get("2050892"), indexMap.get("2092190")));
	}
	public static Map<String,List<Integer>> matchChedaoAndNode(Map<String,List<String>> mapCheDaoGps,Map<String,String> mapToll,Map<String,Integer> indexMap){
		int amount=0;
		int count=0;
		Map<String,List<Integer>> map=new HashMap<>();
		for(String key:mapCheDaoGps.keySet()){
			List<String> cheDaoGpsList=mapCheDaoGps.get(key);
			List<Integer> list=new ArrayList<>();
			boolean flag=false;
			for(int i=0;i<cheDaoGpsList.size();i++){
				amount++;
				String gps=cheDaoGpsList.get(i);
				String k=gps.split(",")[0]+" "+gps.split(",")[1];
				if(mapToll.containsKey(k)){
					flag=true;
					list.add(indexMap.get(mapToll.get(k)));
				}
			}
			if(!flag){
				count++;
			}else{
				map.put(key, list);
			}
		}
		System.out.println("δƥ�䳵���㣺"+count);
		System.out.println("���г����㣺"+amount);
		return map;
	}
	public static Map<String,List<String>> readPoi(String in,String in2){
		Map<String,List<String>> mapNameGps=new HashMap<>();
		Map<String,String> mapPoiGps=new HashMap<>();
		BufferedReader reader2=topology.getReader(in2, "GBK");
		try{
			String line2="";
			while((line2=reader2.readLine())!=null){
				String[] data=line2.split(",",23);
				String poiId=data[7].replaceAll("\"", "").trim();
				String lng=data[5].replaceAll("\"", "").trim();
				String lat=data[6].replaceAll("\"", "").trim();
				mapPoiGps.put(poiId, lng+","+lat);
			}
			reader2.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		BufferedReader reader=topology.getReader(in, "GBK");
		try{
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",10);
				String poiId=data[0].replaceAll("\"", "").trim();
				String poiName=data[2].replaceAll("\"", "").trim();
				if(poiName.endsWith("�շ�վ")){
					if(mapPoiGps.containsKey(poiId)){
						if(mapNameGps.containsKey(poiName)){
							List<String> list=mapNameGps.get(poiName);
							list.add(mapPoiGps.get(poiId));
							mapNameGps.put(poiName, list);
						}else{
							List<String> list=new ArrayList<>();
							list.add(mapPoiGps.get(poiId));
							mapNameGps.put(poiName, list);
						}
					}
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return mapNameGps;
	}
	
	public static Map<String,List<String>> getCheDaoGps(){
		Map<String,List<String>> mapCheDaoGps=new HashMap<>();
		String pNamehebei="D:/mapinfo_ditu/��ͼ/level2/level2/hebei/other/PNamehebei.mid";
		String poiHebei="D:/mapinfo_ditu/��ͼ/level2/level2/hebei/index/POIhebei.mid";
		String tollStation="G:/��ͼ/�շ�վ.xlsx";
		String cheDao="G:/��ͼ/shoufeichedao.csv";
		Map<String,List<String>> mapNameGps=readPoi(pNamehebei,poiHebei);
		tollStation t=new tollStation();
		Map<String,String> mapCheDaoTollName=t.getCheDaoToll(tollStation, cheDao);
		int count=0;
		int amount=0;
		for(String cd:mapCheDaoTollName.keySet()){
			amount++;
			String cheDaoName=mapCheDaoTollName.get(cd).substring(0,2);
			boolean flag=false;
			for(String name:mapNameGps.keySet()){
				if(name.contains(cheDaoName)){
					flag=true;
//					System.out.println(cd+":"+mapNameGps.get(name));
					mapCheDaoGps.put(cd, mapNameGps.get(name));
					break;
				}
			}
			if(!flag){
				count++;
//				System.out.println(cd+":"+mapCheDaoTollName.get(cd));
			}
		}
		System.out.println("fail to match:"+count);
		System.out.println("amount:"+amount);
		return mapCheDaoGps;
	}
	public static void presentTime(){
		Date startTime=new Date(System.currentTimeMillis());
		SimpleDateFormat sdFormatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String retStrFormatNowDate = sdFormatter.format(startTime);
		System.out.println(retStrFormatNowDate);
	}
	public static void main(String[] args) throws CloneNotSupportedException, IOException{
		presentTime();
		String in="G:/��ͼ/cqTopology2.csv";
		String in2="G:/��ͼ/����/2018-01-27";
		getShortestPath(in,in2);
		presentTime();
	}
}
