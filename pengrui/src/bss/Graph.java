package bss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bss.Graph.Edge;
import bss.Graph.Vertex;



public class Graph {
	private List<Vertex> vertexList;   //ͼ�Ķ��㼯  
    private Map<Vertex, List<Edge>> ver_edgeList_map;  //ͼ��ÿ�������Ӧ�������  
      
    public Graph(List<Vertex> vertexList, Map<Vertex, List<Edge>> ver_edgeList_map) {  
        super();  
        this.vertexList = vertexList;  
        this.ver_edgeList_map = ver_edgeList_map;  
    }  
  
    public Graph() {
		// TODO Auto-generated constructor stub
	}

	public List<Vertex> getVertexList() {  
        return vertexList;  
    }  
  
    public void setVertexList(List<Vertex> vertexList) {  
        this.vertexList = vertexList;  
    }  
      
    public Map<Vertex, List<Edge>> getVer_edgeList_map() {  
        return ver_edgeList_map;  
    }  
  
    public void setVer_edgeList_map(Map<Vertex, List<Edge>> ver_edgeList_map) {  
        this.ver_edgeList_map = ver_edgeList_map;  
    }  
    public void setRoot(Vertex v)  
    {  
        v.setParent(null);  
        v.setAdjuDist(0);  
        v.setKnown(true);
    }  
    static class Edge{
    	private Vertex startVertex;  //������ߵ���ʼ��  
        private Vertex endVertex;  //������ߵ��յ�  
        private double weight;  //������ߵ�Ȩֵ  
        private String linkId;//������ߵ�linkId
          
        public String getLinkId() {
			return linkId;
		}

		public void setLinkId(String linkId) {
			this.linkId = linkId;
		}

		public Edge(Vertex startVertex, Vertex endVertex, double
        		weight,String linkId) {  
            super();  
            this.startVertex = startVertex;  
            this.endVertex = endVertex;  
            this.weight = weight;  
            this.linkId=linkId;
        }  
          
        public Edge()  
        {}  
          
        public Vertex getStartVertex() {  
            return startVertex;  
        }  
        public void setStartVertex(Vertex startVertex) {  
            this.startVertex = startVertex;  
        }  
        public Vertex getEndVertex() {  
            return endVertex;  
        }  
        public void setEndVertex(Vertex endVertex) {  
            this.endVertex = endVertex;  
        }

    	public double getWeight() {
    		return weight;
    	}

    	public void setWeight(double weight) {
    		this.weight = weight;
    	}  
        @Override
        public String toString(){
        	return startVertex.toString()+";"+endVertex.toString()+";"+weight;
        }
        @Override
        public Object clone() throws  CloneNotSupportedException{
        	Edge e=new Edge(this.startVertex,this.endVertex,this.weight,this.linkId);
        	return e;
        }
    }
    
    static class Vertex{
    	private final static double infinite_dis = Double.MAX_VALUE;  
        
        private String name;  //�ڵ�����  
        private boolean known; //�˽ڵ�֮ǰ�Ƿ���֪  
        private double adjuDist; //�˽ڵ����  
        private Vertex parent; //��ǰ�ӳ�ʼ�ڵ㵽�˽ڵ�����·���£��ĸ��ڵ㡣  
        private Edge edge;//��ǰ���·���ı�
        private String lat;
        private String lng;
          
        public String getLat() {
			return lat;
		}

		public void setLat(String lat) {
			this.lat = lat;
		}

		public String getLng() {
			return lng;
		}

		public void setLng(String lng) {
			this.lng = lng;
		}

		public Edge getEdge() {
			return edge;
		}

		public void setEdge(Edge edge) {
			this.edge = edge;
		}

		public Vertex()  
        {  
            this.known = false;  
            this.adjuDist = infinite_dis;  
            this.parent = null;  
            this.edge=null;
            this.lat=null;
            this.lng=null;
        }  
          
        public Vertex(String name)  
        {  
            this.known = false;  
            this.adjuDist = infinite_dis;  
            this.parent = null;  
            this.name = name;  
            this.edge=null;
            this.lat=null;
            this.lng=null;
        }  
        public Vertex(String name,String lat,String lng)  
        {  
            this.known = false;  
            this.adjuDist = infinite_dis;  
            this.parent = null;  
            this.name = name;  
            this.edge=null;
            this.lat=lat;
            this.lng=lng;
        }  
        public Vertex(String name,double adjuDist,Vertex parent,Edge edge,boolean known,String lat,String lng)  
        {  
            this.known = known;  
            this.adjuDist = adjuDist;  
            this.parent = parent;  
            this.name = name;  
            this.edge=edge;
            this.lat=lat;
            this.lng=lng;
        }  
          
        public String getName() {  
            return name;  
        }  
        public void setName(String name) {  
            this.name = name;  
        }  
        public boolean isKnown() {  
            return known;  
        }  
        public void setKnown(boolean known) {  
            this.known = known;  
        }  
        
        public double getAdjuDist() {
    		return adjuDist;
    	}

    	public void setAdjuDist(double adjuDist) {
    		this.adjuDist = adjuDist;
    	}

    	public Vertex getParent() {  
            return parent;  
        }  

        public void setParent(Vertex parent) {  
            this.parent = parent;  
        }  
          
        /** 
         * ����Object�����equals���� 
         */  
        @Override  
        public boolean equals(Object obj) {  
            if (!(obj instanceof Vertex)) {  
                throw new ClassCastException("an object to compare with a Vertext must be Vertex");  
            }  
              
            if (this.name==null) {  
                throw new NullPointerException("name of Vertex to be compared cannot be null");  
            }  
              
            return this.name.equals(obj);  
        }  
        @Override
        public String toString(){
        	if(parent!=null){
        		return name+","+adjuDist+",parent:"+parent.getName();
        	}else{
        		return name+","+adjuDist;
        	}
        }
        @Override
        public Object clone() throws  CloneNotSupportedException{
        	Vertex v=new Vertex(this.name,this.adjuDist,this.parent,this.edge,this.known,this.lat,this.lng);
        	return v;
        }
    }
      
    /** 
     *  
     * @param startIndex dijkstra���������ڵ��±� 
     * @param destIndex dijkstra�������յ�ڵ��±� 
     */  
    public String dijkstraTravasal(int startIndex,int destIndex)  
    {  
        Vertex start = vertexList.get(startIndex);  
        Vertex dest = vertexList.get(destIndex);  
        String path = "["+dest.getName()+"]";  
          
        setRoot(start);  
        updateChildren(start);  
        String linkIdPath="";
        if(dest.getEdge()!=null){
        	linkIdPath=dest.getEdge().getLinkId();  
        }else{
        	linkIdPath="no path!";
        }
        double shortest_length = dest.getAdjuDist();   
//        System.out.println(shortest_length);
          
        while((dest.getParent()!=null)&&(!dest.equals(start)))  
        {  
            path = "["+dest.getParent().getName()+"] --> "+path;
            if(dest.parent.parent!=null){
            	 linkIdPath=dest.getParent().getEdge().getLinkId()+"-->"+linkIdPath;
            }
            dest = dest.getParent();  
        }  
        
        System.out.println("["+vertexList.get(startIndex).getName() +"] to ["+  
                vertexList.get(destIndex).getName()+"] dijkstra shortest path :: "+path); 
        
        System.out.println("["+vertexList.get(startIndex).getName() +"] to ["+  
                vertexList.get(destIndex).getName()+"] dijkstra shortest linkId path :: "+linkIdPath); 
        System.out.println("shortest length::"+shortest_length);  
        return shortest_length+";"+linkIdPath;
    }  
      
    /** 
     * �ӳ�ʼ�ڵ㿪ʼ�ݹ�����ڽӱ� 
     * @param v 
     */  
    private void updateChildren(Vertex v)  
    {  
        if (v==null) {  
            return ;  
        }  
          
        if (ver_edgeList_map.get(v)==null||ver_edgeList_map.get(v).size()==0) {  
            return ;  
        }  
        List<Vertex> childrenList = new LinkedList<Vertex>();  
        for(Edge e:ver_edgeList_map.get(v))  
        {  
            Vertex childVertex = e.getEndVertex();  
            //����ӽڵ�֮ǰδ֪����ѵ�ǰ�ӽڵ��������б�  
            if(!childVertex.isKnown())  
            {  
                childVertex.setKnown(true);  
                childVertex.setAdjuDist(v.getAdjuDist()+e.getWeight());  
                childVertex.setParent(v);  
                childVertex.setEdge(e);
                childrenList.add(childVertex);
            }else{
            	 //�ӽڵ�֮ǰ��֪����Ƚ��ӽڵ��ajduDist&&nowDist  
                double nowDist = v.getAdjuDist()+e.getWeight(); 
                if(nowDist<childVertex.getAdjuDist())  
                {  
                	childVertex.setAdjuDist(nowDist);  
                    childVertex.setParent(v);  
                    childVertex.setEdge(e);
                }  
            }
        }  
          
        //����ÿһ���ӽڵ�  
        for(Vertex vc:childrenList)  
        {  
            updateChildren(vc);  
        }  
    }
    public static Graph getGraph(List<Vertex> verList,Map<Vertex, List<Edge>> vertex_edgeList_map) throws CloneNotSupportedException{
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
    public static void main(String[] args) throws CloneNotSupportedException{
    	List<Vertex> verList=new LinkedList<>();
    	Map<Vertex, List<Edge>> vertex_edgeList_map=new HashMap<>();
    	Vertex v1=new Vertex("v1");
    	Vertex v2=new Vertex("v2");
    	Vertex v3=new Vertex("v3");
    	Vertex v4=new Vertex("v4");
    	Vertex v5=new Vertex("v5");
    	Vertex v6=new Vertex("v6");
    	verList.add(v1);
    	verList.add(v2);
    	verList.add(v3);
    	verList.add(v4);
    	verList.add(v5);
    	verList.add(v6);
    	Edge e1=new Edge(v1,v2,1,"1");
    	Edge e6=new Edge(v2,v1,1,"6");
    	Edge e2=new Edge(v2,v3,2,"2");
    	Edge e7=new Edge(v3,v2,2,"7");
    	Edge e3=new Edge(v3,v4,3,"3");
    	Edge e4=new Edge(v4,v5,4,"4");
    	Edge e5=new Edge(v5,v3,1,"5");
    	List<Edge> list1=new LinkedList<>();
    	list1.add(e1);
    	List<Edge> list2=new LinkedList<>();
    	list2.add(e2);
    	list2.add(e6);
    	List<Edge> list3=new LinkedList<>();
    	list3.add(e3);
    	list3.add(e7);
    	List<Edge> list4=new LinkedList<>();
    	list4.add(e4);
    	List<Edge> list5=new LinkedList<>();
    	list5.add(e5);
    	vertex_edgeList_map.put(v1, list1);
    	vertex_edgeList_map.put(v2, list2);
    	vertex_edgeList_map.put(v3, list3);
    	vertex_edgeList_map.put(v4, list4);
    	vertex_edgeList_map.put(v5, list5);
    	Graph g=getGraph(verList,vertex_edgeList_map);
    	g.dijkstraTravasal(0, 3);
    	Graph g1=getGraph(verList,vertex_edgeList_map);
    	g1.dijkstraTravasal(5, 3);
    	
    }
}
