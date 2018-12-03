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
//输入文件变了，没有poi的，topology2
public class shortestPathV3 {
	private static final double EARTH_RADIUS = 6378137;  
	/**
	 * 建立节点名称和节点数据结构的索引
	 * @param snodeVertex	以String类型的节点为key，Vertex类型为值的hashMap，主要是以节点的名字建立索引，找到新建的节点
	 * @param snode	节点名称
	 */
	public static void getSnodeVertex(Map<String,Vertex> snodeVertex,String snode){
		if(!snodeVertex.containsKey(snode)){
			Vertex v=new Vertex(snode);
			snodeVertex.put(snode, v);
		}
	}
	/**
	 * 添加图中以某个节点为起点的边的集合
	 * @param vertex_edgeList_map 以节点为key，该节点为起点的边的集合为value的集合
	 * @param node	起点节点
	 * @param edge	边
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
		Map<String,Vertex> snodeVertex=new HashMap<>();//以节点String为key，Vertex为value，保证有唯一节点
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
						mapToll.put(snode, snodeGps);
					}else{
						mapToll.put(enode, enodeGps);
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
	public static Graph getGraph(List<Vertex> verList,Map<Vertex, List<Edge>> vertex_edgeList_map) throws CloneNotSupportedException{
		List<Vertex> verListCopy=new LinkedList<>();
		Map<Vertex, List<Edge>> vertex_edgeList_mapCopy=new HashMap<>();
		for(int i=0;i<verList.size();i++){
			Vertex v=(Vertex) verList.get(i).clone();
			verListCopy.add(v);
		}
		Set set=vertex_edgeList_map.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]);
		for(int i=0;i<entries.length;i++){
			Vertex v=(Vertex) entries[i].getKey();
			Vertex vC=(Vertex) v.clone();
			List<Edge> list=(List<Edge>) entries[i].getValue();
			List<Edge> listC=new LinkedList<>();
			for(int j=0;j<list.size();j++){
				Edge eC=(Edge) list.get(j).clone();
				listC.add(eC);
			}
			vertex_edgeList_mapCopy.put(vC, listC);
		}
		return new Graph(verListCopy,vertex_edgeList_mapCopy);
	}
	/**
	 * 读取拓扑文件，输入起点编号和终点编号，建立图并得到最短路径
	 * @param in	输入拓扑文件的路径
	 * @param startNode	起点节点编号
	 * @param endNode	终点节点编号
	 */
	public static Graph readTopology(String in,Map<String,String> mapToll){
		List<Vertex> verList = new LinkedList<Vertex>();//图中节点集合
		Map<String,Vertex> snodeVertex=new HashMap<>();//以节点String为key，Vertex为value，保证有唯一节点
		Map<Vertex, List<Edge>> vertex_edgeList_map = new HashMap<Vertex,List<Edge>>();
		Map<String,Integer> indexMap=new HashMap<>();//节点在verList中的索引
		
//		Map<String,String> mapToll=new HashMap<>();//收费站节点gps
		
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
						mapToll.put(snode, snodeGps);
					}else{
						mapToll.put(enode, enodeGps);
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
		Graph g=new Graph(verList,vertex_edgeList_map);
		return g;
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
	public static void getShortestPath(String in,String in2) throws IOException{
		BufferedWriter writer=topology.getWriter("G:/地图/输出数据/hebei2.csv", "GBK");
		tollStation t=new tollStation();
		t.readTollSquare(t.getInTollSquare());
		t.readCheDao(t.getInCheDao());
		Map<String,String> mapCheDaoGps=t.getMapCheDaoGps();
		System.out.println("read mapCheDaoGps finish!");
		
		List<Vertex> verList=new LinkedList<>();
		Map<Vertex, List<Edge>> vertex_edgeList_map=new HashMap<>();
		Map<String,Integer> indexMap=new HashMap<>();
		Map<String,String> mapToll=new HashMap<>();
		
		readTopology2(in,verList,vertex_edgeList_map,mapToll,indexMap);
		System.out.println("read Topology2 finish!");
		
		System.out.println("verList:"+verList.size());
		System.out.println("vertex_edgeList_map:"+vertex_edgeList_map.size());
		
		Map<String,Integer> mapChedaoToNode=matchChedaoAndNode(mapCheDaoGps,mapToll,indexMap);
		System.out.println("mapCheDaoGps:"+mapCheDaoGps.size());
		System.out.println("mapChedaoToNode:"+mapChedaoToNode.size());
		
		int count=0;
		int hebeiAmount=0;
		int amount=0;
		Graph g=new Graph(verList,vertex_edgeList_map);
		File fileIn=new File(in2);
		List<String> list=Arrays.asList(fileIn.list());
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
							String rs=g.dijkstraTravasal(mapChedaoToNode.get(inLaneId), mapChedaoToNode.get(outLaneId));
							if(rs.contains("no path!")){
								count++;
							}else{
								writer.write(inPlate+";"+inCx+";"+inTime+";"+inLaneId+";"+outPlate+";"+outCx+";"+outTime+";"+outLaneId+";"+weight+";"+fee+";"+rs+"\n");
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
		System.out.println("数据为空条数:"+count);
		System.out.println("数据为不为空条数:"+hebeiAmount);
		System.out.println("总条数:"+amount);
//		Graph g=new Graph(verList,vertex_edgeList_map);
//		System.out.println("getGraph finish!");
//		
//		System.out.println(g.dijkstraTravasal(indexMap.get("2077971"), indexMap.get("2077966")));
	}
	public static Map<String,Integer> matchChedaoAndNode(Map<String,String> mapCheDaoGps,Map<String,String> mapToll,Map<String,Integer> indexMap){
		Map<String,Integer> map=new HashMap<>();
		for(String key:mapCheDaoGps.keySet()){
			String cheDaoGps=mapCheDaoGps.get(key);
			String[] cd=cheDaoGps.split(",",2);
			double cdLat=Double.parseDouble(cd[0]);
			double cdLng=Double.parseDouble(cd[1]);
			String flag="";
			double min=Double.MAX_VALUE;
			for(String tKey:mapToll.keySet()){
				String tollGps=mapToll.get(tKey);
				String[] t=tollGps.split(" ",2);
				double tLng=Double.parseDouble(t[0]);
				double tLat=Double.parseDouble(t[1]);
				double distance=getDistance(cdLat,cdLng,tLat,tLng);
				if(min>distance){
					min=distance;
					flag=tKey;
				}
			}
			map.put(key, indexMap.get(flag));
//			System.out.println(key+":"+indexMap.get(flag));
		}
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
				mapPoiGps.put(poiId, lat+","+lng);
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
				if(poiName.endsWith("收费站")){
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
	
	public Map<String,List<String>> getCheDaoGps(){
		Map<String,List<String>> mapCheDaoGps=new HashMap<>();
		String pNamehebei="D:/mapinfo_ditu/地图/level2/level2/hebei/other/PNamehebei.mid";
		String poiHebei="D:/mapinfo_ditu/地图/level2/level2/hebei/index/POIhebei.mid";
		String tollStation="G:/地图/收费站.xlsx";
		String cheDao="G:/地图/shoufeichedao.csv";
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
		String in="G:/地图/cqTopology2.csv";
		String in2="G:/地图/数据/2018-01-27";
		getShortestPath(in,in2);
		presentTime();
	}
}
