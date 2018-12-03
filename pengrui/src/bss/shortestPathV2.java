package bss;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bss.Graph.Edge;
import bss.Graph.Vertex;

//������v1
public class shortestPathV2 {
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
	/**
	 * ��ȡ�����ļ�����������ź��յ��ţ�����ͼ���õ����·��
	 * @param in	���������ļ���·��
	 * @param startNode	���ڵ���
	 * @param endNode	�յ�ڵ���
	 */
	public static void readTopology(String in,String startNode,String endNode){
		List<Vertex> verList = new LinkedList<Vertex>();//ͼ�нڵ㼯��
		Map<String,Vertex> snodeVertex=new HashMap<>();//�Խڵ�StringΪkey��VertexΪvalue����֤��Ψһ�ڵ�
		Map<Vertex, List<Edge>> vertex_edgeList_map = new HashMap<Vertex,List<Edge>>();
		Map<String,Integer> indexMap=new HashMap<>();//�ڵ���verList�е�����
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
		String in="G:/��ͼ/cqTopology.csv";
		readTopology(in,"751233","751233");
	}
}
