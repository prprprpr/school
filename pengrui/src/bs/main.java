package bs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class main {
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
	public static void write(String path,ArrayList<ArrayList<float[]>> cluster){
		try{
			for(int i=0;i<cluster.size();i++){
				String[] a=path.split("\\.");
				BufferedWriter writer=getWriter(a[0]+i+".csv","GBK");
				ArrayList<float[]> arr=cluster.get(i);
				for(int j=0;j<arr.size();j++){
					writer.write(arr.get(j)[0]+","+arr.get(j)[1]+"\n");
				}
				writer.flush();
				writer.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void writeStop(ArrayList<point> points,String path) throws IOException{
		Map<Integer,ArrayList<point>> map=new HashMap<>();
		for(point p:points){
			if(p.getCluster()!=0){
				if(!map.containsKey(p.getCluster())){
					ArrayList<point> stop=new ArrayList<point>();
					stop.add(p);
					map.put(p.getCluster(), stop);
				}else{
					ArrayList<point> stop=map.get(p.getCluster());
					stop.add(p);
					map.put(p.getCluster(), stop);
				}
			}
		}
		System.out.println(map.size());
		for(int key:map.keySet()){
			ArrayList<point> stop=map.get(key);
			BufferedWriter writer=getWriter(path+"/stop"+key+".csv","GBK");
			for(point p:stop){
				writer.write(p+"\n");
			}
			writer.flush();
			writer.close();
		}
	}
	
	public static void main(String[] args) throws IOException{
//		readCCM reader=new readCCM();
//		String path="F:/货车轨迹数据/单条轨迹/trace2.csv";
//		ArrayList<point> trace=reader.read(path);
//		DBSCAN d=new DBSCAN(100,20);
//		d.process(trace);
//		String outDir="F:/货车轨迹数据/测试结果/"+path.split("\\/")[3].split("\\.")[0]+"的停留点";
//		File file=new File(outDir);
//		if(!file.exists()){
//			file.mkdirs();
//		}
//		writeStop(trace,outDir);
	}
}
