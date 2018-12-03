package pengrui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class pr {
	private static String linktp="H:/新建文件夹/linktp.txt";
	private static String linkinfo="H:/新建文件夹/linkinfo.txt";
	private static String file="H:/新建文件夹/file.txt";
	private static String file4="H:/新建文件夹/4";
	private static String file6="H:/新建文件夹/5";
	private static String fileAll="H:/新建文件夹/2";
	private static String fileOut="H:/新建文件夹/fileOut/";
	private static String fileOut6="H:/新建文件夹/fileOut6/";
	
	public static BufferedReader getReader(String path,String encoding) throws UnsupportedEncodingException, FileNotFoundException{
		File file=new File(path);
		InputStreamReader read=new InputStreamReader(new FileInputStream(file),encoding);
		BufferedReader reader=new BufferedReader(read);
		return reader;
	}
	public static BufferedWriter getWriter(String path,String encoding) throws UnsupportedEncodingException, FileNotFoundException{
		File file=new File(path);
		OutputStreamWriter write=new OutputStreamWriter(new FileOutputStream(file),encoding);
		BufferedWriter writer=new BufferedWriter(write);
		return writer;
	}
	
	public static Double get1(String value,List<String> list,String pre,String post,Map<String,String> mapLinkinfo) throws IOException{
		Double time=0.0;
		String a=value+".csv";
		int l=0;
		boolean flag=false;
		Double amount=0.0;
		int count=0;
		for(;l<list.size();l++){
			if(a.equals(list.get(l))){
				BufferedReader readerValue=getReader(file4+"/"+list.get(l),"GBK");
				String lineValue="";
				while((lineValue=readerValue.readLine())!=null){
					String[] data=lineValue.split(";");
					String v=data[2].substring(12,20)+","+data[2].substring(32, 40);
					if(v.equals(pre.substring(11,19)+","+post.substring(11,19))){
						flag=true;
						Double t=Double.parseDouble(data[3]);
						if(mapLinkinfo.containsKey(data[0])&&mapLinkinfo.containsKey(value)){
							amount+=t;
							count++;
						}else{
							System.out.println("err");
						}
					}
				}
				readerValue.close();
			}
		}
		time=amount/count;
		if(!flag){
			System.out.println("cannot find time match: "+value);
		}
		return time;
	}
	
//	public static Double get(String value,List<String> list,String pre,String post,Map<String,String> mapLinkinfo,String[] dataC) throws IOException{
//		Double time=0.0;
//		String a=value+".csv";
//		int l=0;
//		boolean flag=false;
//		for(;l<list.size();l++){
//			if(a.equals(list.get(l))){
//				BufferedReader readerValue=getReader(file4+"/"+list.get(l),"GBK");
//				String lineValue="";
//				while((lineValue=readerValue.readLine())!=null){
//					String[] data=lineValue.split(";");
//					if(data[2].equals("["+pre+","+post+")")){
//						flag=true;
//						Double t=Double.parseDouble(data[3]);
//						if(mapLinkinfo.containsKey(data[0])&&mapLinkinfo.containsKey(dataC[0])){
//							String valueA=mapLinkinfo.get(data[0]);
//							String valueC=mapLinkinfo.get(dataC[0]);
//							Double lengthA=Double.parseDouble(valueA.split("-")[0]);
//							Double widthA=Double.parseDouble(valueA.split("-")[1]);
//							Double lengthC=Double.parseDouble(valueC.split("-")[0]);
//							Double widthC=Double.parseDouble(valueC.split("-")[1]);
//							time=widthA*t*lengthC/widthC/lengthA;
//							break;
//						}else{
//							System.out.println("err");
//						}
//					}
//				}
//				readerValue.close();
//			}
//		}
//		if(l==list.size()-1){
//			System.out.println("cannot find"+value);
//		}
//		if(!flag){
//			time=get1(value,list,pre,post,mapLinkinfo);
//		}
//		return time;
//	}
	
	public static void checkFirstLine(int j,List<String> list,List<String> listIn,String first,String second,Map<String,String> mapLinkinfo,BufferedWriter writer) throws IOException, ParseException{
		String c=listIn.get(j);
		String[] dataC=c.split(";");
		if(!dataC[2].equals("["+first+","+second+")")){
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Double t=get1(dataC[0],list,first,second,mapLinkinfo);
			writer.write(dataC[0]+";"+dataC[1]+";["+first+","+second+");"+t+";1"+"\n");
			writer.flush();
			String timePre=second;
			Long time1=df.parse(timePre).getTime()/1000/60;
			String timePos=dataC[2].substring(1, 20);
			Long time2=df.parse(timePos).getTime()/1000/60;
			int k=(int) ((time2-time1)/2);
			String pre=timePre;
			Double time=0.0;
			for(int h=0;h<k;h++){
				String post=df.format(df.parse(pre).getTime()+120000);
				time=get1(dataC[0],list,pre,post,mapLinkinfo);
				writer.write(dataC[0]+";"+dataC[1]+";["+pre+","+post+");"+time+";1"+"\n");
				writer.flush();
				pre=post;
			}
		}else{
			writer.write(listIn.get(j)+";0"+"\n");
			writer.flush();
		}
	}
	public static void checkLastLine(int j,List<String> list,List<String> listIn,String first,String second,Map<String,String> mapLinkinfo,BufferedWriter writer) throws IOException, ParseException{
		String c=listIn.get(j);
		String[] dataC=c.split(";");
		writer.write(listIn.get(j)+";0"+"\n");
		writer.flush();
		if(!dataC[2].equals("["+first+","+second+")")){
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timePre=dataC[2].substring(21, 40);
			Long time1=df.parse(timePre).getTime()/1000/60;
			String timePos=second;
			Long time2=df.parse(timePos).getTime()/1000/60;
			int k=(int) ((time2-time1)/2);
			String pre=timePre;
			Double time=0.0;
			for(int h=0;h<k;h++){
				String post=df.format(df.parse(pre).getTime()+120000);
				time=get1(dataC[0],list,pre,post,mapLinkinfo);
				writer.write(dataC[0]+";"+dataC[1]+";["+pre+","+post+");"+time+";1"+"\n");
				writer.flush();
				pre=post;
			}
		}
	}
	
	public static void repair(int j,List<String> list,List<String> listIn,Map<String,String> mapLinktp,Map<String,String> mapLinkinfo,BufferedWriter writer) throws IOException, ParseException{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String c=listIn.get(j);
		String[] dataC=c.split(";");
		writer.write(c+";0"+"\n");
		writer.flush();
		String timePre=dataC[2].substring(21, 40);
		Long time1=df.parse(timePre).getTime()/1000/60;
		String p=listIn.get(j+1);
		String[] dataP=p.split(";");
		String timePos=dataP[2].substring(1, 20);
		Long time2=df.parse(timePos).getTime()/1000/60;
		int k=(int) ((time2-time1)/2);
		Double flag=(Double.parseDouble(dataP[3])-Double.parseDouble(dataC[3]))/(k+1);
		if((time2-time1)<=30&&(time2-time1)>0){
			String pre=timePre;
			Double time=Double.parseDouble(dataC[3]);
			for(int h=0;h<k;h++){
				String post=df.format(df.parse(pre).getTime()+120000);
				time+=flag;
				writer.write(dataC[0]+";"+dataC[1]+";["+pre+","+post+");"+time+";1"+"\n");
				writer.flush();
				pre=post;
			}
		}else{
			String pre=timePre;
			Double time=0.0;
			for(int h=0;h<k;h++){
				String post=df.format(df.parse(pre).getTime()+120000);
				if(mapLinktp.containsKey(dataC[0])){
//					String value=mapLinktp.get(dataC[0]);
//					if(value.contains("-")){
//						String[] a=value.split("-");
//						if(a[0].contains("#")&&!a[1].contains("#")){
//							time=get(a[1],list,pre,post,mapLinkinfo,dataC);
//						}else if(!a[0].contains("#")&&a[1].contains("#")){
//							time=get(a[0],list,pre,post,mapLinkinfo,dataC);
//						}else if(!a[0].contains("#")&&!a[1].contains("#")){
//							Double time00=get(a[0],list,pre,post,mapLinkinfo,dataC);
//							Double time01=get(a[1],list,pre,post,mapLinkinfo,dataC);
//							time=(time00+time01)/2;
//						}else{
//							time=get1(dataC[0],list,pre,post,mapLinkinfo,dataC);
//						}
//					}else{
//						time=get(value,list,pre,post,mapLinkinfo,dataC);
//					}
					time=get1(dataC[0],list,pre,post,mapLinkinfo);
				}
				writer.write(dataC[0]+";"+dataC[1]+";["+pre+","+post+");"+time+";1"+"\n");
				writer.flush();
				pre=post;
			}
		}
	}
	public static void readMap(Map<String,String> mapLinktp,Map<String,String> mapLinkinfo,Map<String,Integer> mapFile){
		String encoding="UTF-8";
		String encoding1="GBK";
		try{
			BufferedReader readerLinktp=getReader(linktp,encoding1);
			BufferedReader readerLinkinfo=getReader(linkinfo,encoding1);
			BufferedReader readerLinkfile=getReader(file,encoding1);
			String lineLinktp="";
			while((lineLinktp=readerLinktp.readLine())!=null){
				String[] data=lineLinktp.split(";");
				if(data.length<3){
					mapLinktp.put(data[0], data[1]);
				}else if(data[1].equals("")||data[2].equals("")){
					if(data[1].equals("")){
						mapLinktp.put(data[0], data[2]);
					}else{
						mapLinktp.put(data[0], data[1]);
					}
				}else{
					mapLinktp.put(data[0], data[1]+"-"+data[2]);
				}
			}
			readerLinktp.close();
			
			String lineLinkinfo="";
			while((lineLinkinfo=readerLinkinfo.readLine())!=null){
				String[] data=lineLinkinfo.split(";");
				mapLinkinfo.put(data[0], data[1]+"-"+data[2]);
			}
			readerLinkinfo.close();
			
			String lineFile="";
			while((lineFile=readerLinkfile.readLine())!=null){
				mapFile.put(lineFile, 1);
			}
			readerLinkfile.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void readMap(Map<String,String> mapLinktp,Map<String,String> mapLinkinfo,Map<String,Integer> mapFile,Map<String,String> mapTimeSection){
		String encoding="UTF-8";
		String encoding1="GBK";
		try{
			BufferedReader readerLinktp=getReader(linktp,encoding1);
			BufferedReader readerLinkinfo=getReader(linkinfo,encoding1);
			BufferedReader readerLinkfile=getReader(file,encoding1);
			BufferedReader readerFileAll=getReader(fileAll,encoding1);
			String lineLinktp="";
			while((lineLinktp=readerLinktp.readLine())!=null){
				String[] data=lineLinktp.split(";");
				if(data.length<3){
					mapLinktp.put(data[0], data[1]);
				}else if(data[1].equals("")||data[2].equals("")){
					if(data[1].equals("")){
						mapLinktp.put(data[0], data[2]);
					}else{
						mapLinktp.put(data[0], data[1]);
					}
				}else{
					mapLinktp.put(data[0], data[1]+"-"+data[2]);
				}
			}
			readerLinktp.close();
			
			String lineLinkinfo="";
			while((lineLinkinfo=readerLinkinfo.readLine())!=null){
				String[] data=lineLinkinfo.split(";");
				mapLinkinfo.put(data[0], data[1]+"-"+data[2]);
			}
			readerLinkinfo.close();
			
			String lineFile="";
			while((lineFile=readerLinkfile.readLine())!=null){
				mapFile.put(lineFile, 1);
			}
			readerLinkfile.close();
			
			Map<String,Map<String,List<String>>> map=new HashMap<>();
			String lineFileAll="";
			while((lineFileAll=readerFileAll.readLine())!=null){
				String[] data=lineFileAll.split(";");
				String key=data[0]+","+data[2].substring(12,20)+","+data[2].substring(32, 40);
				if(!map.containsKey(key)){
					List<String> list=new ArrayList<>();
					list.add(data[3]);
				}else{
					List<String> list=(List<String>) map.get(key);
					list.add(data[3]);
				}
			}
			readerLinkfile.close();
			
			Set set=map.entrySet();
			Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
			for(int i=0;i<entries.length;i++){
				String keyIn=entries[i].getKey().toString();
				List<String> list=(List<String>) entries[i].getValue();
				Double amount=0.0;
				for(int j=0;j<list.size();j++){
					amount+=Double.parseDouble(list.get(j));
				}
				Double rs=amount/list.size();
				mapTimeSection.put(keyIn, ""+rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static void read(){
		Map<String,String> mapLinktp=new HashMap<>();
		Map<String,String> mapLinkinfo=new HashMap<>();
		Map<String,Integer> mapFile=new HashMap<>();
		String encoding="UTF-8";
		String encoding1="GBK";
		try{
			readMap(mapLinktp,mapLinkinfo,mapFile);
			File fileIn=new File(file4);
			List<String> list=Arrays.asList(fileIn.list());
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(int i=0;i<list.size();i++){
				List<String> listIn=new ArrayList<>();
				String path=file4+"/"+list.get(i);
				String outPath=fileOut+list.get(i);
				BufferedReader reader=getReader(path,encoding1);
				BufferedWriter writer=getWriter(outPath,encoding1);
				String line="";
				while((line=reader.readLine())!=null){
					listIn.add(line);
				}
				reader.close();
				for(int j=0;j<listIn.size();j++){
					if(j==0){
						checkFirstLine(j,list,listIn,"2016-03-01 00:00:00","2016-03-01 00:02:00",mapLinkinfo,writer);
					}else if(j==listIn.size()-1){
						checkLastLine(j,list,listIn,"2016-05-31 23:58:00","2016-06-01 00:00:00",mapLinkinfo,writer);
					}else{
						repair(j,list,listIn,mapLinktp,mapLinkinfo,writer);
					}
				}
				writer.close();
				System.out.println(outPath+" finish!");
 			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void read6(){
		Map<String,String> mapLinktp=new HashMap<>();
		Map<String,String> mapLinkinfo=new HashMap<>();
		Map<String,Integer> mapFile=new HashMap<>();
		String encoding="UTF-8";
		String encoding1="GBK";
		try{
			readMap(mapLinktp,mapLinkinfo,mapFile);
			File fileIn=new File(file6);
			List<String> list=Arrays.asList(fileIn.list());
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(int i=0;i<1;i++){
				List<String> listIn=new ArrayList<>();
				String path=file6+"/"+list.get(i);
				String outPath=fileOut6+list.get(i);
				BufferedReader reader=getReader(path,encoding1);
				BufferedWriter writer=getWriter(outPath,encoding1);
				String line="";
				while((line=reader.readLine())!=null){
					listIn.add(line);
				}
				reader.close();
				File fileIn2=new File(fileAll);
				List<String> list2=Arrays.asList(fileIn2.list());
				String flag="2016-06-01";
				List<String> listInin=new ArrayList<>();
				for(int j=0;j<listIn.size();j++){
					String c=listIn.get(j);
					String[] dataC=c.split(";");
					listInin.add(listIn.get(j));
					if(!flag.equals(dataC[1])||j==listIn.size()-1){
						for(int k=0;k<listInin.size();k++){
							if(k==0){
								checkFirstLine(k,list2,listInin,flag+" 06:00:00",flag+" 06:02:00",mapLinkinfo,writer);
							}else if(k==listInin.size()-1){
								checkLastLine(k,list2,listInin,flag+" 07:58:00",flag+" 08:00:00",mapLinkinfo,writer);
							}else{
								repair(k,list2,listInin,mapLinktp,mapLinkinfo,writer);
							}
						}
						flag=dataC[1];
						listInin.clear();
					}
					if(j==listIn.size()-1&&!flag.equals("2016-06-30")){
						Double time1=get1(dataC[0],list2,"2016-06-30 06:00:00","2016-06-30 06:02:00",mapLinkinfo);
						String line1=dataC[0]+";2016-06-30;[2016-06-30 06:00:00,2016-06-30 06:02:00);"+time1;
						String[] data6=line1.split(";");
						writer.write(line1+";1"+"\n");
						writer.flush();
						if(!data6[2].equals("[2016-06-30 07:58:00,2016-06-30 08:00:00)")){
							String timePre=data6[2].substring(21, 40);
							Long time16=df.parse(timePre).getTime()/1000/60;
							String timePos="2016-06-30 08:00:00";
							Long time26=df.parse(timePos).getTime()/1000/60;
							int k=(int) ((time26-time16)/2);
							String pre=timePre;
							Double time=0.0;
							for(int h=0;h<k;h++){
								String post=df.format(df.parse(pre).getTime()+120000);
								time=get1(data6[0],list,pre,post,mapLinkinfo);
								writer.write(data6[0]+";"+data6[1]+";["+pre+","+post+");"+time+";1"+"\n");
								writer.flush();
								pre=post;
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws ParseException{
		read();
	}
}
