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
	 * ΪmapEdge��Ӳ���
	 * @param mapEdge	��ͼ�ڵ�Ϊkey���Ըýڵ�Ϊ���ıߵ�listΪֵ��hashMap
	 * @param snodeVertex	��String���͵Ľڵ�Ϊkey��Vertex����Ϊֵ��hashMap����Ҫ���Խڵ�����ֽ����������ҵ��½��Ľڵ�
	 * @param start	һ���ߵ��������
	 * @param end	һ���ߵ��ص�����
	 * @param length	�����ߵĳ���
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
	 * ��ȡ���˽ṹ��������ݣ���������������
	 * @param in	�������������
	 * @param snodeVertex	��String���͵Ľڵ�Ϊkey��Vertex����Ϊֵ��hashMap����Ҫ���Խڵ�����ֽ����������ҵ��½��Ľڵ�
	 * @param mapEdge	��ͼ�ڵ�Ϊkey���Ըýڵ�Ϊ���ıߵ�listΪֵ��hashMap
	 * @param startNodeSet	��һ��������Ϊ���Ľڵ����Ƶļ���
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
	 * ���ݽڵ�ͱ߹���ͼ
	 * @param startNodeSet	��һ��������Ϊ���Ľڵ����Ƶļ���
	 * @param mapEdge	��ͼ�ڵ�Ϊkey���Ըýڵ�Ϊ���ıߵ�listΪֵ��hashMap
	 * @param snodeVertex	��String���͵Ľڵ�����Ϊkey��Vertex�ڵ�����Ϊֵ��hashMap����Ҫ���Խڵ�����ֽ����������ҵ���ڵ�
	 * @param indexMap	��¼ÿ���ڵ��ڽڵ㼯�ϵ�����λ��
	 * @return	����һ��ͼ
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
	 * ��������������ڵ����ƣ��������̵�·�����Խڵ��ʾ
	 * @param in	�����ļ�·��
	 * @param snode	���ڵ�����
	 * @param enode	�յ�ڵ�����
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
		String in="G:/��ͼ/cqTopology.csv";
		getShortestPath(in,"750617","750655");
	}
}
