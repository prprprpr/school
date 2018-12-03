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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class pr1 {
	private static String linktp="H:/新建文件夹/linktp.txt";
	private static String linkinfo="H:/新建文件夹/linkinfo.txt";
	private static String file="H:/新建文件夹/file.txt";
	private static String file4="H:/新建文件夹/4";
	private static String file6="H:/新建文件夹/5";
	private static String fileAll="H:/新建文件夹/2" ;
	private static String fileOut="H:/新建文件夹/fileOut1/";
	private static String fileOut6="H:/新建文件夹/fileOut6/";
	
	private static String in="C:/Users/pengrui/Desktop/1.txt";
	private static String out="C:/Users/pengrui/Desktop/2.txt";
	
	private static String outTZ="C:/Users/pengrui/Desktop/outTZ.csv";
	private static String outTime="C:/Users/pengrui/Desktop/outTime.csv";
	private static String outTZ2="C:/Users/pengrui/Desktop/outTZ2.csv";
	
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
	public static Double get2(String value,String pre,String post,Map<String,String> mapTimeSection) throws IOException{
		Double time=0.0;
		String key=value+","+pre.substring(11,19)+","+post.substring(11,19);
		if(mapTimeSection.containsKey(key)){
			time=Double.parseDouble(mapTimeSection.get(key));
		}else{
			System.out.println("cannot find time match: "+value);
		}
		return time;
	}
	
	public static void checkFirstLine(int j,List<String> listIn,String first,String second,Map<String,String> mapTimeSection,Map<String,String> mapLinktp,BufferedWriter writer) throws IOException, ParseException{
		String c=listIn.get(j);
		String[] dataC=c.split(";");
		if(!dataC[2].equals("["+first+","+second+")")){
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Double t=get2(dataC[0],first,second,mapTimeSection);
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
				time=get2(dataC[0],pre,post,mapTimeSection);
				writer.write(dataC[0]+";"+dataC[1]+";["+pre+","+post+");"+time+";1"+"\n");
				writer.flush();
				pre=post;
			}
		}
	}
	public static void checkLastLine(int j,List<String> listIn,String first,String second,Map<String,String> mapTimeSection,BufferedWriter writer) throws IOException, ParseException{
		String c=listIn.get(j);
		String[] dataC=c.split(";");
//		writer.write(listIn.get(j)+";0"+"\n");
//		writer.flush();
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
				time=get2(dataC[0],pre,post,mapTimeSection);
				writer.write(dataC[0]+";"+dataC[1]+";["+pre+","+post+");"+time+";1"+"\n");
				writer.flush();
				pre=post;
			}
		}
	}
	public static void createDay(String[] dataC,String day,String first,String second,Map<String,String> mapTimeSection,BufferedWriter writer) throws IOException, ParseException{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Double time1=get2(dataC[0],first,second,mapTimeSection);
		String line1=dataC[0]+";"+day+";["+first+","+second+");"+time1;
		String[] data6=line1.split(";");
		writer.write(line1+";1"+"\n");
		writer.flush();
		if(!data6[2].equals("["+day+" 07:58:00"+","+day+" 08:00:00"+")")){
			String timePre=data6[2].substring(21, 40);
			Long time16=df.parse(timePre).getTime()/1000/60;
			String timePos=day+" 08:00:00";
			Long time26=df.parse(timePos).getTime()/1000/60;
			int k=(int) ((time26-time16)/2);
			String pre=timePre;
			Double time=0.0;
			for(int h=0;h<k;h++){
				String post=df.format(df.parse(pre).getTime()+120000);
				time=get2(data6[0],pre,post,mapTimeSection);
				writer.write(data6[0]+";"+data6[1]+";["+pre+","+post+");"+time+";1"+"\n");
				writer.flush();
				pre=post;
			}
		}
	}
	
	public static void createAnotherDay(String key,String day,String first,Map<String,String> mapLinktp,Map<String,String> mapLinkinfo,BufferedWriter writer) throws IOException, ParseException{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String id=key;
		Long timeLong=df.parse(day+" "+first).getTime()/60000;
		String length=mapLinkinfo.get(id).split("-",2)[0];
		String width=mapLinkinfo.get(id).split("-",2)[1];
		String preLinks=mapLinktp.get(id).split("-",2)[0];
		String posLinks=mapLinktp.get(id).split("-",2)[1];
		int preLinksCount=0;
		int posLinksCount=0;
		if(!preLinks.equals("")){
			preLinksCount=preLinks.split("#").length;
		}
		if(!posLinks.equals("")){
			posLinksCount=posLinks.split("#").length;
		}
		double[] pre=new double[8];
		for(int j=0;j<preLinksCount;j+=2){
			String preLink=preLinks.split("#")[j];
			String preLength=mapLinkinfo.get(preLink).split("-",2)[0];
			String preWidth=mapLinkinfo.get(preLink).split("-",2)[1];
//			String preTime=mapTimeSection.get(preLink+","+time);
			pre[j]=Double.parseDouble(preLength);
			pre[j+1]=Double.parseDouble(preWidth);
//			pre[j+2]=Double.parseDouble(preTime);
		}
		String preInfo="";
		for(int j=0;j<pre.length;j++){
			preInfo+=pre[j]+",";
		}
		
		double[] pos=new double[8];
		for(int j=0;j<posLinksCount;j+=2){
			String posLink=posLinks.split("#")[j];
			String posLength=mapLinkinfo.get(posLink).split("-",2)[0];
			String posWidth=mapLinkinfo.get(posLink).split("-",2)[1];
//			String posTime=mapTimeSection.get(posLink+","+time);
			pos[j]=Double.parseDouble(posLength);
			pos[j+1]=Double.parseDouble(posWidth);
//			pos[j+2]=Double.parseDouble(posTime);
		}
		
		String posInfo="";
		for(int j=0;j<pos.length;j++){
			posInfo+=pos[j]+",";
		}
		for(int i=0;i<30;i++){
			String out=id+","+length+","+width+","+preLinksCount+","+posLinksCount+","+timeLong+","+preInfo+posInfo;
			writer.write(out+"\n");
			writer.flush();
			timeLong+=(df.parse(day+" "+first).getTime()+120000)/60000;
		}
	}
	
	public static void repair(int j,List<String> listIn,Map<String,String> mapLinktp,Map<String,String> mapTimeSection,BufferedWriter writer) throws IOException, ParseException{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String c=listIn.get(j);
		String[] dataC=c.split(";");
//		writer.write(c+";0"+"\n");
//		writer.flush();
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
					time=get2(dataC[0],pre,post,mapTimeSection);
				}
				writer.write(dataC[0]+";"+dataC[1]+";["+pre+","+post+");"+time+";1"+"\n");
				writer.flush();
				pre=post;
			}
		}
	}
	
	public static void readMap(Map<String,String> mapLinktp,Map<String,String> mapLinkinfo,Map<String,Integer> mapFile,Map<String,String> mapTimeSection,String in){
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
			
			Map<String,List<String>> map=new HashMap<>();
			File fileIn=new File(in);
			List<String> listFile=Arrays.asList(fileIn.list());
			for(int i=0;i<listFile.size();i++){
				String path=in+"/"+listFile.get(i);
				BufferedReader readerFileAll=getReader(path,encoding1);
				String lineFileAll="";
				while((lineFileAll=readerFileAll.readLine())!=null){
					String[] data=lineFileAll.split(";");
					String key=data[0]+","+data[2].substring(12,20)+","+data[2].substring(32, 40);
					if(!map.containsKey(key)){
						List<String> list=new ArrayList<>();
						list.add(data[3]);
						map.put(key, list);
					}else{
						List<String> list=(List<String>) map.get(key);
						list.add(data[3]);
						map.put(key, list);
					}
				}
				readerFileAll.close();
			}
			
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
	
	public static void readMap1(Map<String,String> mapLinktp,Map<String,String> mapLinkinfo,Map<String,Integer> mapFile){
		String encoding="UTF-8";
		String encoding1="GBK";
		try{
			BufferedReader readerLinktp=getReader(linktp,encoding1);
			BufferedReader readerLinkinfo=getReader(linkinfo,encoding1);
			BufferedReader readerLinkfile=getReader(file,encoding1);
			String lineLinktp="";
			while((lineLinktp=readerLinktp.readLine())!=null){
				String[] data=lineLinktp.split(";",3);
				if(data.length<3){
					mapLinktp.put(data[0], data[1]);
				}else if(data[1].equals("")||data[2].equals("")){
					if(data[1].equals("")){
						mapLinktp.put(data[0], "-"+data[2]);
					}else{
						mapLinktp.put(data[0], data[1]+"-");
					}
				}else{
					mapLinktp.put(data[0], data[1]+"-"+data[2]);
				}
			}
			readerLinktp.close();
			
			String lineLinkinfo="";
			while((lineLinkinfo=readerLinkinfo.readLine())!=null){
				String[] data=lineLinkinfo.split(";",4);
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
	public static void read(String in,String out,String inAll){
		Map<String,String> mapLinktp=new HashMap<>();
		Map<String,String> mapLinkinfo=new HashMap<>();
		Map<String,Integer> mapFile=new HashMap<>();
		Map<String,String> mapTimeSection=new HashMap<>();
		String encoding="UTF-8";
		String encoding1="GBK";
		try{
			readMap(mapLinktp,mapLinkinfo,mapFile,mapTimeSection,inAll);
			System.out.println("readMap finish!");
			File fileIn=new File(in);
			List<String> list=Arrays.asList(fileIn.list());
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(int i=0;i<list.size();i++){
				List<String> listIn=new ArrayList<>();
				String path=in+"/"+list.get(i);
				String outPath=out+"/"+list.get(i);
				BufferedReader reader=getReader(path,encoding1);
				BufferedWriter writer=getWriter(outPath,encoding1);
				String line="";
				while((line=reader.readLine())!=null){
					listIn.add(line);
				}
				reader.close();
				for(int j=0;j<listIn.size();j++){
					String c=listIn.get(j);
					String[] dataC=c.split(";");
					if(j==0){
						checkFirstLine(j,listIn,"2017-03-01 00:00:00","2017-03-01 00:02:00",mapTimeSection,mapLinktp,writer);
						String c2=listIn.get(j+1);
						String[] dataC2=c2.split(";");
						if(!dataC2[2].substring(1,20).equals(dataC[2].substring(21, 40))){
							writer.write(listIn.get(j)+";0"+"\n");
							writer.flush();
							repair(j,listIn,mapLinktp,mapTimeSection,writer);
						}else{
							writer.write(listIn.get(j)+";0"+"\n");
							writer.flush();
						}
					}else if(j==listIn.size()-1){
						writer.write(listIn.get(j)+";0"+"\n");
						writer.flush();
						checkLastLine(j,listIn,"2017-05-31 23:58:00","2017-06-01 00:00:00",mapTimeSection,writer);
					}else{
						writer.write(listIn.get(j)+";0"+"\n");
						writer.flush();
						repair(j,listIn,mapLinktp,mapTimeSection,writer);
					}
				}
				writer.close();
				System.out.println(outPath+" finish!");
 			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void read6(String in,String out,String inAll){
		Map<String,String> mapLinktp=new HashMap<>();
		Map<String,String> mapLinkinfo=new HashMap<>();
		Map<String,Integer> mapFile=new HashMap<>();
		Map<String,String> mapTimeSection=new HashMap<>();
		String encoding="UTF-8";
		String encoding1="GBK";
		try{
			readMap(mapLinktp,mapLinkinfo,mapFile,mapTimeSection,inAll);
			System.out.println("readMap finish!");
			File fileIn=new File(in);
			List<String> list=Arrays.asList(fileIn.list());
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(int i=0;i<list.size();i++){
				List<String> listIn=new ArrayList<>();
				String path=in+"/"+list.get(i);
				String outPath=out+"/"+list.get(i);
				BufferedReader reader=getReader(path,encoding1);
				BufferedWriter writer=getWriter(outPath,encoding1);
				String line="";
				while((line=reader.readLine())!=null){
					listIn.add(line);
				}
				reader.close();
				
				String flag="2017-06-01";
				List<String> listInin=new ArrayList<>();
				for(int j=0;j<listIn.size();j++){
					String c=listIn.get(j);
					String[] dataC=c.split(";");
					if(!flag.equals(dataC[1])||j==listIn.size()-1){
						if(listInin.size()==0&&j==0){
							String today=dataC[1];
							DateFormat df1=new SimpleDateFormat("yyyy-MM-dd");
							int d=(int) ((df1.parse(today).getTime()-df1.parse(flag).getTime())/1000/60/60/24);
							String day=flag;
							for(int f=0;f<d;f++){
								createDay(dataC,day,day+" 06:00:00",day+" 06:02:00",mapTimeSection,writer);
								day=df1.format(df1.parse(day).getTime()+24*3600*1000);
							}
							flag=day;
						}else{
							for(int k=0;k<listInin.size();k++){
								if(listInin.size()>1){
									if(k==0){
										checkFirstLine(k,listInin,flag+" 06:00:00",flag+" 06:02:00",mapTimeSection,mapLinktp,writer);
										String c2=listInin.get(k+1);
										String[] dataC2=c2.split(";");
										if(!dataC2[2].substring(1,20).equals(dataC[2].substring(21, 40))){
											writer.write(listInin.get(k)+";0"+"\n");
											writer.flush();
											repair(k,listInin,mapLinktp,mapTimeSection,writer);
										}else{
											writer.write(listInin.get(k)+";0"+"\n");
											writer.flush();
										}
									}else if(k==listInin.size()-1){
										writer.write(listInin.get(k)+";0"+"\n");
										writer.flush();
										checkLastLine(k,listInin,flag+" 07:58:00",flag+" 08:00:00",mapTimeSection,writer);
									}else{
										writer.write(listInin.get(k)+";0"+"\n");
										writer.flush();
										repair(k,listInin,mapLinktp,mapTimeSection,writer);
									}
								}else{
									checkFirstLine(k,listInin,flag+" 06:00:00",flag+" 06:02:00",mapTimeSection,mapLinktp,writer);
									writer.write(listInin.get(k)+";0"+"\n");
									writer.flush();
									checkLastLine(k,listInin,flag+" 07:58:00",flag+" 08:00:00",mapTimeSection,writer);
								}
							}
							String today=dataC[1];
							DateFormat df1=new SimpleDateFormat("yyyy-MM-dd");
							int d=(int) ((df1.parse(today).getTime()-df1.parse(flag).getTime())/1000/60/60/24);
							if(d>1){
								String day=flag;
								for(int f=0;f<d-1;f++){
									day=df1.format(df1.parse(day).getTime()+24*3600*1000);
									createDay(dataC,day,day+" 06:00:00",day+" 06:02:00",mapTimeSection,writer);
								}
							}
							flag=dataC[1];
							listInin.clear();
							listInin.add(c);
						}
					}else{
						listInin.add(c);
					}
					if(j==listIn.size()-1&&!flag.equals("2017-06-30")){
						createDay(dataC,"2017-06-30","2017-06-30 06:00:00","2017-06-30 06:02:00",mapTimeSection,writer);
					}
				}
				writer.close();
				System.out.println(outPath+" finish!");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void readTest(){
		Map<String,String> mapLinktp=new HashMap<>();
		Map<String,String> mapLinkinfo=new HashMap<>();
		Map<String,Integer> mapFile=new HashMap<>();
		Map<String,String> mapTimeSection=new HashMap<>();
		String encoding="UTF-8";
		String encoding1="GBK";
		try{
			readMap(mapLinktp,mapLinkinfo,mapFile,mapTimeSection,fileAll);
			System.out.println("readMap finish!");
			BufferedReader reader=getReader(in,encoding1);
			BufferedWriter writer=getWriter(out,encoding1);
			String line="";
			List<String> listIn=new ArrayList<>();
			while((line=reader.readLine())!=null){
				listIn.add(line);
				for(int j=0;j<listIn.size();j++){
					checkFirstLine(j,listIn,"2016-06-17 06:00:00","2016-06-17 06:02:00",mapTimeSection,mapLinktp,writer);
					writer.write(listIn.get(j)+",0"+"\n");
					writer.flush();
					checkLastLine(j,listIn,"2016-06-17 07:58:00","2016-06-17 08:00:00",mapTimeSection,writer);
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void readTZ(String in,Map<String,String> mapLinktp,Map<String,String> mapLinkinfo,BufferedWriter writer,BufferedWriter writer1) throws ParseException, NumberFormatException, IOException{
		File file=new File(in);
		List<String> list=Arrays.asList(file.list());
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i=0;i<list.size();i++){
			String path=in+list.get(i);
			BufferedReader reader=getReader(path,"GBK");
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(";");
				String id=data[0];
				String time=data[2];
				Long timeLong=df.parse(data[2].substring(1, 20)).getTime()/60000;
				String startTime=time.substring(15, 17);
				String length=mapLinkinfo.get(id).split("-",2)[0];
				String width=mapLinkinfo.get(id).split("-",2)[1];
				String preLinks=mapLinktp.get(id).split("-",2)[0];
				String posLinks=mapLinktp.get(id).split("-",2)[1];
				int preLinksCount=0;
				int posLinksCount=0;
				if(!preLinks.equals("")){
					preLinksCount=preLinks.split("#").length;
				}
				if(!posLinks.equals("")){
					posLinksCount=posLinks.split("#").length;
				}
				double[] pre=new double[8];
				for(int j=0;j<preLinksCount;j+=2){
					String preLink=preLinks.split("#")[j];
					String preLength=mapLinkinfo.get(preLink).split("-",2)[0];
					String preWidth=mapLinkinfo.get(preLink).split("-",2)[1];
//					String preTime=mapTimeSection.get(preLink+","+time);
					pre[j]=Double.parseDouble(preLength);
					pre[j+1]=Double.parseDouble(preWidth);
//					pre[j+2]=Double.parseDouble(preTime);
				}
				String preInfo="";
				for(int j=0;j<pre.length;j++){
					preInfo+=pre[j]+",";
				}
				
				double[] pos=new double[8];
				for(int j=0;j<posLinksCount;j+=2){
					String posLink=posLinks.split("#")[j];
					String posLength=mapLinkinfo.get(posLink).split("-",2)[0];
					String posWidth=mapLinkinfo.get(posLink).split("-",2)[1];
//					String posTime=mapTimeSection.get(posLink+","+time);
					pos[j]=Double.parseDouble(posLength);
					pos[j+1]=Double.parseDouble(posWidth);
//					pos[j+2]=Double.parseDouble(posTime);
				}
				
				String posInfo="";
				for(int j=0;j<pos.length;j++){
					posInfo+=pos[j]+",";
				}
				String presTime=data[3];
				String out=id+","+length+","+width+","+preLinksCount+","+posLinksCount+","+timeLong+","+preInfo+posInfo;
				writer.write(out+"\n");
				writer.flush();
				writer1.write(presTime+"\n");
				writer1.flush();
			}
			reader.close();
			System.out.println(path+" finish!");
		}
	}
	public static void getTZ(){
		Map<String,String> mapLinktp=new HashMap<>();
		Map<String,String> mapLinkinfo=new HashMap<>();
		Map<String,Integer> mapFile=new HashMap<>();
		String encoding="UTF-8";
		String encoding1="GBK";
		try{
			readMap1(mapLinktp,mapLinkinfo,mapFile);
			System.out.println("readMap finish!");
			BufferedWriter writer=getWriter(outTZ,encoding1);
			BufferedWriter writer1=getWriter(outTime,encoding1);
			readTZ(fileOut,mapLinktp,mapLinkinfo,writer,writer1);
			readTZ(fileOut6,mapLinktp,mapLinkinfo,writer,writer1);
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void writeJune(){
		String encoding="UTF-8";
		String encoding1="GBK";
		Map<String,String> mapLinktp=new HashMap<>();
		Map<String,String> mapLinkinfo=new HashMap<>();
		Map<String,Integer> mapFile=new HashMap<>();
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat df1=new SimpleDateFormat("yyyy-MM-dd");
		try{
			readMap1(mapLinktp,mapLinkinfo,mapFile);
			System.out.println("readMap finish!");
			BufferedReader readerLinkfile=getReader(file,encoding1);
			BufferedWriter writer=getWriter(outTZ2,encoding1);
			String lineFile="";
			while((lineFile=readerLinkfile.readLine())!=null){
				String day="2016-06-01";
				for(int i=0;i<30;i++){
					System.out.println(day);
					createAnotherDay(lineFile,day,"08:00:00",mapLinktp,mapLinkinfo,writer);
					day=df1.format(df1.parse(day).getTime()+24*3600*1000);
				}
			}
			readerLinkfile.close();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws ParseException{
		read("D:/4","C:/Users/pengrui/Desktop/fileOut/fileOut4/","D:/2");
		read6("D:/3","C:/Users/pengrui/Desktop/fileOut/fileOut3/","D:/2");
//		readTest();
//		getTZ();
//		writeJune();
	}
}
