package bss;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bss.Graph.Edge;
import bss.Graph.Vertex;

public class shortestPath {
	/**
	 * 为mapEdge添加参数
	 * @param mapEdge	以图节点为key，以该节点为起点的边的list为值的hashMap
	 * @param snodeVertex	以String类型的节点为key，Vertex类型为值的hashMap，主要是以节点的名字建立索引，找到新建的节点
	 * @param start	一条边的起点名称
	 * @param end	一条边的重点名称
	 * @param length	该条边的长度
	 */
	public static void addMapEdge(Map<Vertex,LinkedList<Edge>> mapEdge,Map<String,Vertex> snodeVertex,String start,String end,String length,String linkId){
		Vertex sv=snodeVertex.get(start);
		Vertex ev=snodeVertex.get(end);
		Edge e=new Edge(sv,ev,Double.parseDouble(length),linkId);
		if(mapEdge.containsKey(sv)){
			LinkedList<Edge> list=mapEdge.get(sv);
			list.add(e);
			mapEdge.put(sv, list);
		}else{
			LinkedList<Edge> list=new LinkedList<>();
			list.add(e);
			mapEdge.put(sv, list);
		}
	}
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
	 * 读取拓扑结构输出的数据，填入三个容器中
	 * @param in	拓扑输出的数据
	 * @param snodeVertex	以String类型的节点为key，Vertex类型为值的hashMap，主要是以节点的名字建立索引，找到新建的节点
	 * @param mapEdge	以图节点为key，以该节点为起点的边的list为值的hashMap
	 * @param startNodeSet	在一条边中作为起点的节点名称的集合
	 */
	public static void readTopology(String in,Map<String,Vertex> snodeVertex,Map<Vertex,LinkedList<Edge>> mapEdge,Set<String> startNodeSet){
		BufferedReader reader=topology.getReader(in, "GBK");
		try{
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(";",10);
				String linkId=data[0];
				String kind=data[1];
				String snode=data[4];
				String enode=data[5];
				String length=data[7];
				String direction=data[8];
				String gps=data[9];
				getSnodeVertex(snodeVertex,snode);
				getSnodeVertex(snodeVertex,enode);
				if(direction.equals("0")||direction.equals("1")){
					addMapEdge(mapEdge,snodeVertex,snode,enode,length,linkId);
					addMapEdge(mapEdge,snodeVertex,enode,snode,length,linkId);
					startNodeSet.add(snode);
					startNodeSet.add(enode);
				}else if(direction.equals("2")){
					addMapEdge(mapEdge,snodeVertex,snode,enode,length,linkId);
					startNodeSet.add(snode);
				}else{
					addMapEdge(mapEdge,snodeVertex,enode,snode,length,linkId);
					startNodeSet.add(enode);
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 根据节点和边构建图
	 * @param startNodeSet	在一条边中作为起点的节点名称的集合
	 * @param mapEdge	以图节点为key，以该节点为起点的边的list为值的hashMap
	 * @param snodeVertex	以String类型的节点名称为key，Vertex节点类型为值的hashMap，主要是以节点的名字建立索引，找到其节点
	 * @param indexMap	记录每个节点在节点集合的索引位置
	 * @return	返回一个图
	 */
	public static Graph getGraph(Set<String> startNodeSet,Map<Vertex,LinkedList<Edge>> mapEdge,Map<String,Vertex> snodeVertex,Map<String,Integer> indexMap){
		List<Vertex> verList = new LinkedList<Vertex>();
		Map<Vertex, List<Edge>> vertex_edgeList_map = new HashMap<Vertex,List<Edge>>();
		int index=0;
		for(String start:startNodeSet){
			Vertex v=snodeVertex.get(start);
			verList.add(v);
			indexMap.put(start, index);
			index++;
			List<Edge> listEdge=mapEdge.get(v);
			vertex_edgeList_map.put(v, listEdge);
		}
		Graph g=new Graph(verList,vertex_edgeList_map);
		return g;
	}
	/**
	 * 根据输入的两个节点名称，输出其最短的路径，以节点表示
	 * @param in	拓扑文件路径
	 * @param snode	起点节点名称
	 * @param enode	终点节点名称
	 */
	public static void getShortestPath(String in,String snode,String enode){
		Set<String> startNodeSet=new HashSet<>();
		Map<String,Vertex> snodeVertex=new HashMap<>();
		Map<Vertex,LinkedList<Edge>> mapEdge=new HashMap<>();
		Map<String,Integer> indexMap=new HashMap<>();
		readTopology(in,snodeVertex,mapEdge,startNodeSet);
		System.out.println("startNodeSet:"+startNodeSet.size());
		System.out.println("mapEdge:"+mapEdge.size());
		Graph g=getGraph(startNodeSet,mapEdge,snodeVertex,indexMap);
		g.dijkstraTravasal(indexMap.get(snode), indexMap.get(enode));
	}
	public static void main(String[] args){
		String in="G:/地图/cqTopology.csv";
		getShortestPath(in,"750617","750655");
	}
}
