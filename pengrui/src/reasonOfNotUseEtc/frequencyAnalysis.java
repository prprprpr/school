package reasonOfNotUseEtc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class frequencyAnalysis {
	private static String in2016="F:/����/ԭʼ����";
	private static String in2017="F:/����/2017short";
	
	private static String classify2016="F:/����/��ʹ��ETCԭ�����/2016������";
	private static String classify2017="F:/����/��ʹ��ETCԭ�����/2017������";
	
	private static String classify20161="F:/����/��ʹ��ETCԭ�����/2016������1";
	
	private static String out="F:/����/��ʹ��ETCԭ�����/ÿ��Ƶ�ζԱ�";
	private static String outChange="F:/����/��ʹ��ETCԭ�����/frequencyChange.csv";
	private static String outChange1="F:/����/��ʹ��ETCԭ�����/frequencyChange1.csv";
	
	private static String oftenUseEtcCar="F:/����/��ʹ��ETCԭ�����/ÿ�³����벻����ETC����/����/";
	private static String seldomUseEtcCar="F:/����/��ʹ��ETCԭ�����/ÿ�³����벻����ETC����/������/";
	public static void addJsonObject(Map<String,JSONObject> map,String key,String isEtc) throws JSONException{
		if(map.containsKey(key)){
			JSONObject ob=map.get(key);
			if(isEtc.equals("0")){
				int mtcCs=ob.getInt("mtcCs");
				ob.put("mtcCs", mtcCs+1);
			}else{
				int etcCs=ob.getInt("etcCs");
				ob.put("etcCs", etcCs+1);
			}
			map.put(key, ob);
		}else{
			JSONObject ob=new JSONObject();
			int etcCs=0;
			int mtcCs=0;
			ob.put("etcCs", etcCs);
			ob.put("mtcCs", mtcCs);
			map.put(key, ob);
		}
	}
	
	public static void getCount(int[][] m,Map<String,String> mapClass,String key,int i){
		if(mapClass.containsKey(key)){
			if(mapClass.get(key).equals("0.0")||mapClass.get(key).equals("0")){
				m[i][0]++;
			}else if(mapClass.get(key).equals("1.0")||mapClass.get(key).equals("1")){
				m[i][1]++;
			}else if(mapClass.get(key).equals("2.0")||mapClass.get(key).equals("2")){
				m[i][2]++;
			}else if(mapClass.get(key).equals("3.0")||mapClass.get(key).equals("3")){
				m[i][3]++;
			}
		}
	}
	public static void writeSet(Set<String> set,BufferedWriter writer) throws IOException{
		for(String key:set){
			writer.write(key+"\n");
		}
		writer.flush();
		writer.close();
	}
	public static void process(Map<String,JSONObject> map,BufferedWriter writer,BufferedWriter writerOften,BufferedWriter writerSeldom,Map<String,String> mapClass) throws JSONException, IOException{
		Set<String> oftenSet=new HashSet<>();
		Set<String> seldomSet=new HashSet<>();
		Set set=map.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]);
		int[][] a=new int[4][4];
		int[][] b=new int[4][4];
		int[] countA=new int[4];
		int[] countB=new int[4];
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();
			JSONObject ob=map.get(key);
			int etcCs=ob.getInt("etcCs");
			int mtcCs=ob.getInt("mtcCs");
			if(etcCs>0){
				int amount=etcCs+mtcCs;
				float p=(float)(etcCs)/((float)(etcCs)+(float)(mtcCs));
				if(p>=0.6667){
					if(amount>=15){
						oftenSet.add(key);
						countA[0]++;
						getCount(a,mapClass,key,0);
					}else if(amount>=10){
						countA[1]++;
						getCount(a,mapClass,key,1);
					}else if(amount>=5){
						countA[2]++;
						getCount(a,mapClass,key,2);
					}else{
						countA[3]++;
						getCount(a,mapClass,key,3);
					}
				}
				if(p<=0.3333){
					if(amount>=15){
						seldomSet.add(key);
						countB[0]++;
						getCount(b,mapClass,key,0);
					}else if(amount>=10){
						countB[1]++;
						getCount(b,mapClass,key,1);
					}else if(amount>=5){
						countB[2]++;
						getCount(b,mapClass,key,2);
					}else{
						countB[3]++;
						getCount(b,mapClass,key,3);
					}
				}
			}
		}
		writer.write("����"+","+"Ƶ�δ��ڵ���15(0��)"+","+"Ƶ�δ��ڵ���15(1��)"+","+"Ƶ�δ��ڵ���15(2��)"+","+"Ƶ�δ��ڵ���15(3��)"+","+"Ƶ�δ��ڵ���15�ܳ�����"+","+"Ƶ�δ��ڵ���10(0��)"+","+"Ƶ�δ��ڵ���10(1��)"+","+"Ƶ�δ��ڵ���10(2��)"+","+"Ƶ�δ��ڵ���10(3��)"+","+"Ƶ�δ��ڵ���10�ܳ�����"+","+"Ƶ�δ��ڵ���5(0��)"+","+"Ƶ�δ��ڵ���5(1��)"+","+"Ƶ�δ��ڵ���5(2��)"+","+"Ƶ�δ��ڵ���5(3��)"+","+"Ƶ�δ��ڵ���5�ܳ�����"+","+"Ƶ��С��5(0��)"+","+"Ƶ��С��5(1��)"+","+"Ƶ��С��5(2��)"+","+"Ƶ��С��5(3��)"+","+"Ƶ��С��5�ܳ�����"+"\n");
		writer.write("ETCʹ���ʴ��ڵ���66.67%"+","+a[0][0]+","+a[0][1]+","+a[0][2]+","+a[0][3]+","+countA[0]+","+a[1][0]+","+a[1][1]+","+a[1][2]+","+a[1][3]+","+countA[1]+","+a[2][0]+","+a[2][1]+","+a[2][2]+","+a[2][3]+","+countA[2]+","+a[3][0]+","+a[3][1]+","+a[3][2]+","+a[3][3]+","+countA[3]+"\n");
		writer.write("ETCʹ����С�ڵ���33.33%"+","+b[0][0]+","+b[0][1]+","+b[0][2]+","+b[0][3]+","+countB[0]+","+b[1][0]+","+b[1][1]+","+b[1][2]+","+b[1][3]+","+countB[1]+","+b[2][0]+","+b[2][1]+","+b[2][2]+","+b[2][3]+","+countB[2]+","+b[3][0]+","+b[3][1]+","+b[3][2]+","+b[3][3]+","+countB[3]+"\n");
		writer.flush();
		writer.close();
		writeSet(oftenSet,writerOften);
		writeSet(seldomSet,writerSeldom);
	}
	public static void readIn(String in,String out,Map<String,String> mapClass) throws IOException, JSONException{
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			Map<String,JSONObject> map=new HashMap<>();
			String path=in+"/"+list.get(i);
			String month=list.get(i).substring(0,6);
			String outPath=out+"/"+month+".csv";
			String outOftenUseEtcCar=oftenUseEtcCar+"/"+month+".csv";
			String outSeldomUseEtcCar=seldomUseEtcCar+"/"+month+".csv";
			BufferedWriter writer=getFirstEtcTime.getWriter(outPath, "GBK");
			BufferedWriter writerOftenUseEtcCar=getFirstEtcTime.getWriter(outOftenUseEtcCar, "GBK");
			BufferedWriter writerSeldomUseEtcCar=getFirstEtcTime.getWriter(outSeldomUseEtcCar, "GBK");
			BufferedReader reader=getFirstEtcTime.getReader(path,"GBK");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",8);
				if(data.length==8){
					String plate=data[1];
					String cx=data[2];
					String enTime=data[3];
					String exTime=data[4];
					String isEtc=data[7];
					String enStation=data[5];
					String exStation=data[6];
					String key=plate+","+cx;
					if(plate.startsWith("��")){
						if(cx.equals("0")){
							addJsonObject(map,key,isEtc);
						}
					}
				}
			}
			reader.close();
			process(map,writer,writerOftenUseEtcCar,writerSeldomUseEtcCar,mapClass);
			System.out.println("read "+path+" finish!");
		}
	}
	
	public static void getFrequecy(String in,String in2,String out) throws IOException, JSONException{
		Map<String,String> mapClass=new HashMap<>();
		Map<String,String> mapClass2017=new HashMap<>();
		readClassify(classify2016,mapClass);
		readClassify(classify2017,mapClass2017);
		System.out.println(mapClass.size());
		readIn(in,out,mapClass);
		readIn(in2,out,mapClass2017);
	}
	
	public static void getFrequencyChange(String in,String out) throws IOException, JSONException{
		BufferedWriter writer=getFirstEtcTime.getWriter(out, "GBK");
		writer.write("�·�,��Ƶ0�೵����(ÿ�³���15��������ETCʹ���ʴ��ڵ���66.67%),��Ƶ1�೵����(>=15;>=66.67%),��Ƶ2�೵����(>=15;>=66.67%),��Ƶ3�೵����(>=15;>=66.67%),��Ƶ����(>=15;>=66.67%),��Ƶ0�೵����(ÿ�³���15��������ETCʹ����С�ڵ���33.33%),��Ƶ1�೵����(>=15;<=33.33%),��Ƶ2�೵����(>=15;<=33.33%),��Ƶ3�೵����(>=15;<=33.33%),��Ƶ����(>=15;<=33.33%)"+"\n");
		DecimalFormat decimalFormat=new DecimalFormat(".00");
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			String path=in+"/"+list.get(i);
			BufferedReader reader=getFirstEtcTime.getReader(path,"GBK");
			String month=list.get(i).substring(0,6);
			String line="";
			reader.readLine();
			writer.write(month);
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",21);
				writer.write(","+data[1]+","+data[2]+","+data[3]+","+data[4]+","+data[5]);
			}
			reader.close();
			writer.write("\n");
			System.out.println("read "+path+" finish!");
		}
		writer.flush();
		writer.close();
	}
	
	public static void readClassify(String in,Map<String,String> mapClassify) throws IOException{
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			String path=in+"/"+list.get(i);
			BufferedReader reader=getFirstEtcTime.getReader(path, "GBK");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",6);
				String plate=data[0];
				String type=data[5];
				mapClassify.put(plate+","+0, type);
			}
			reader.close();
			System.out.println("read "+path+" finish!");
		}
	}
	
	public static void readInMonth(String in,String out,Map<String,Map<String,String>> mapClassify) throws IOException, JSONException{
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			Map<String,JSONObject> map=new HashMap<>();
			String path=in+"/"+list.get(i);
			String month=list.get(i).substring(0,6);
			String outPath=out+"/"+month+".csv";
			String outOftenUseEtcCar=oftenUseEtcCar+"/"+month+".csv";
			String outSeldomUseEtcCar=seldomUseEtcCar+"/"+month+".csv";
			BufferedWriter writer=getFirstEtcTime.getWriter(outPath, "GBK");
			BufferedWriter writerOftenUseEtcCar=getFirstEtcTime.getWriter(outOftenUseEtcCar, "GBK");
			BufferedWriter writerSeldomUseEtcCar=getFirstEtcTime.getWriter(outSeldomUseEtcCar, "GBK");
			BufferedReader reader=getFirstEtcTime.getReader(path,"GBK");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",8);
				if(data.length==8){
					String plate=data[1];
					String cx=data[2];
					String enTime=data[3];
					String exTime=data[4];
					String isEtc=data[7];
					String enStation=data[5];
					String exStation=data[6];
					String key=plate+","+cx;
					if(plate.startsWith("��")){
						if(cx.equals("0")){
							addJsonObject(map,key,isEtc);
						}
					}
				}
			}
			reader.close();
			Map<String,String> mapClass=mapClassify.get(month);
			process(map,writer,writerOftenUseEtcCar,writerSeldomUseEtcCar,mapClass);
			System.out.println("read "+path+" finish!");
		}
	}
	
	public static void readClassifyMonth(String in,Map<String,Map<String,String>> mapClassify) throws IOException{
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			Map<String,String> mapIn=new HashMap<>();
			String path=in+"/"+list.get(i);
			String month=list.get(i).substring(0,6);
			BufferedReader reader=getFirstEtcTime.getReader(path, "GBK");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",6);
				String plate=data[0];
				String type=data[5];
				mapIn.put(plate+","+0, type);
			}
			reader.close();
			mapClassify.put(month, mapIn);
			System.out.println("read "+path+" finish!");
		}
	}
	public static void getFrequecyMonth(String in,String in2,String out) throws IOException, JSONException{
		Map<String,Map<String,String>> mapClass2016=new HashMap<>();
		readClassifyMonth(classify20161,mapClass2016);
		System.out.println(mapClass2016.size());
		readInMonth(in,out,mapClass2016);
	}
	
	public static void readStaticMonth(String in,Set<String> setEtcCar,Set<String> setAllCar,BufferedWriter writer) throws IOException, JSONException{
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			Map<String,JSONObject> map=new HashMap<>();
			String path=in+"/"+list.get(i);
			String month=list.get(i).substring(0,6);
			BufferedReader reader=getFirstEtcTime.getReader(path,"GBK");
			int amount=0;
			int etcCount=0;
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",8);
				if(data.length==8){
					String plate=data[1];
					String cx=data[2];
					String enTime=data[3];
					String exTime=data[4];
					String isEtc=data[7];
					String enStation=data[5];
					String exStation=data[6];
					String key=plate+","+cx;
					if(plate.startsWith("��")){
						amount++;
						setAllCar.add(key);
						if(isEtc.equals("1")){
							setEtcCar.add(key);
							etcCount++;
						}
						addJsonObject(map,key,isEtc);
					}
				}
			}
			reader.close();
			int highFreCar=0;
			int lowFreCar=0;
			int highFreCarTrade=0;
			int lowFreCarTrade=0;
			int over50=0;
			int less50=0;
			int progress=0;
			Set set=map.entrySet();
			Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]);
			for(int j=0;j<entries.length;j++){
				String key=entries[j].getKey().toString();
				JSONObject ob=map.get(key);
				int etcCs=ob.getInt("etcCs");
				int mtcCs=ob.getInt("mtcCs");
				if(etcCs>0){
					int a=etcCs+mtcCs;
					if(a>=15){
						highFreCar++;
						highFreCarTrade+=a;
						float p=(float)etcCs/(float)(etcCs+mtcCs);
						if(p>0.5){
							over50++;
						}else{
							less50++;
						}
						if(p<0.9){
							progress+=(a*0.9-etcCs);
						}
					}
					if(a<=5){
						lowFreCar++;
						lowFreCarTrade+=a;
					}
				}
			}
			writer.write(month+","+setEtcCar.size()+","+setAllCar.size()+","+etcCount+","+highFreCar+","+lowFreCar+","+highFreCarTrade+","+lowFreCarTrade+","+amount+","+over50+","+less50+","+progress+"\n");
			System.out.println("read "+path+" finish!");
		}
	}
	public static void justStatic(String in,String in2,String out) throws IOException, JSONException{
		Set<String> setEtcCar=new HashSet<>();
		Set<String> setAllCar=new HashSet<>();
		BufferedWriter writer=getFirstEtcTime.getWriter(out, "GBK");
		writer.write("�·�,"+"ETC�����ۼ���,"+"�ۼ��ܳ�����,"+"ÿ��ETC������,"+"ÿ�¸�Ƶ������,"+"ÿ�µ�Ƶ������,"+"��Ƶ��ÿ�½�����,"+"��Ƶ��ÿ�½�����,"+"ÿ���ܽ�����,"+"ÿ��ETCʹ���ʴ���50%�ĸ�Ƶ������,"+"ÿ��ETCʹ����С�ڵ���50%�ĸ�Ƶ������,"+"ÿ�������Ľ�����"+"\n");
		readStaticMonth(in,setEtcCar,setAllCar,writer);
		readStaticMonth(in2,setEtcCar,setAllCar,writer);
		writer.flush();
		writer.close();
	}
	
	public static void getPlate(){
		BufferedReader reader=getFirstEtcTime.getReader("F:/����/ԭʼ����/201612short.csv", "GBK");
		Set<String> set=new HashSet<>();
		try{
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				set.add(data[1]+","+data[2]);
			}
			reader.close();
			System.out.println(set.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//ͳ�������弮��ETC������ÿ��ռ��
	public static void readMonthTrade(String in,BufferedWriter writer) throws IOException, JSONException{
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			String path=in+"/"+list.get(i);
			String month=list.get(i).substring(0,6);
			BufferedReader reader=getFirstEtcTime.getReader(path,"GBK");
			int mtcCount=0;
			int etcCount=0;
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",8);
				if(data.length==8){
					String plate=data[1];
					String cx=data[2];
					String enTime=data[3];
					String exTime=data[4];
					String isEtc=data[7];
					String enStation=data[5];
					String exStation=data[6];
					String key=plate+","+cx;
					if(plate.startsWith("��")){
						if(isEtc.equals("1")){
							etcCount++;
						}else{
							mtcCount++;
						}
					}
				}
			}
			reader.close();
			writer.write(month+","+etcCount+","+mtcCount+"\n");
			System.out.println("read "+month+" close!");
		}
	}
	public static void getMonthTrade(String in1,String in2,String out) throws IOException, JSONException{
		BufferedWriter writer=getFirstEtcTime.getWriter(out, "GBK");
		readMonthTrade(in1,writer);
		readMonthTrade(in2,writer);
		writer.flush();
		writer.close();
	}
	public static void main(String[] args) throws IOException, JSONException{
//		getFrequecy(in2016,in2017,out);
//		getFrequencyChange(out,outChange);
		
//		getFrequecyMonth(in2016,in2017,out);
//		getFrequencyChange(out,outChange1);
		
//		justStatic(in2016,in2017,"C:/Users/pengrui/Desktop/static.csv");
//		getPlate();
		getMonthTrade(in2016,in2017,"C:/Users/pengrui/Desktop/etcTradeCq.csv");
	}
}
