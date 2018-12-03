package bss;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bss.Graph.Edge;
import bss.Graph.Vertex;

//精简了v1
public class shortestPathV2 {
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
	/**
	 * 读取拓扑文件，输入起点编号和终点编号，建立图并得到最短路径
	 * @param in	输入拓扑文件的路径
	 * @param startNode	起点节点编号
	 * @param endNode	终点节点编号
	 */
	public static void readTopology(String in,String startNode,String endNode){
		List<Vertex> verList = new LinkedList<Vertex>();//图中节点集合
		Map<String,Vertex> snodeVertex=new HashMap<>();//以节点String为key，Vertex为value，保证有唯一节点
		Map<Vertex, List<Edge>> vertex_edgeList_map = new HashMap<Vertex,List<Edge>>();
		Map<String,Integer> indexMap=new HashMap<>();//节点在verList中的索引
		Map<String,String> mapNodeGps=new HashMap<>();
		
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
				double length=Double.parseDouble(data[7]);
				String direction=data[8];
				String gps=data[9];
				String[] g=gps.split(",");
				String snodeGps=g[0];
				String enodeGps=g[g.length-1];
				getSnodeVertex(snodeVertex,snode);
				getSnodeVertex(snodeVertex,enode);
				mapNodeGps.put(snode, snodeGps);
				mapNodeGps.put(enode, enodeGps);
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
			System.out.println(verList.size());
			System.out.println(vertex_edgeList_map.size());
			Graph g=new Graph(verList,vertex_edgeList_map);
			g.dijkstraTravasal(indexMap.get(startNode), indexMap.get(endNode));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		String in="G:/地图/cqTopology.csv";
		readTopology(in,"751233","751233");
	}
}
