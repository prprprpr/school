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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class dataService {
	private static String GZETCOriginal="H:/贵州一月份数据/exlistelec201701.txt";
	private static String GZMTCOriginal="H:/贵州一月份数据/exlistcash201701.txt";
	
	private static String CQOriginal="F:/重庆/201701/20170101.csv";
	private static String cxDataBaseCQ="F:/重庆/cxDataBaseCQ.csv";
	private static String cxDataBase="H:/贵州数据/车牌车型数据库.csv";
	public static HashMap<String,Integer> putValue(String str) {  
        return new HashMap<String,Integer>();  
    }  
	public static void put(Map<String, HashMap<String,Integer>> map1,String a,String b){
		Map<String,Integer> map=map1.computeIfAbsent(a, k -> putValue(k));
		if(map.size()==0){
			map.put(b, 1);
		}else{
			if(map.containsKey(b))
				map.put(b, map.get(b)+1);
			else
				map.put(b, 1);
		}
	}
	public static void put(Map<String, HashMap<String,Integer>> map1,String a,String b,int c){
		Map<String,Integer> map=map1.computeIfAbsent(a, k -> putValue(k));
		if(map.size()==0){
			map.put(b, c);
		}else{
			if(map.containsKey(b))
				map.put(b, map.get(b)+c);
			else
				map.put(b, c);
		}
	}
	public static String correctTime(String time) throws ParseException{
		if(time.length()<19){
			return "0";
		}else{
			String a=time.substring(0, 19);
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");
			Date date=df.parse(a);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String outTime=sdf.format(date);
			return outTime;
		}
	}
	public static String correctTimeCq(String time) throws ParseException{
		if(time.length()<19){
			return "0";
		}else{
			String a=time.substring(0, 19);
			DateFormat df=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date=df.parse(a);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String outTime=sdf.format(date);
			return outTime;
		}
	}
	public static String isCxEqual(String inCx,String outCx){
		if(inCx.equals(outCx)){
			return "0";
		}else{
			return "1";
		}
	}
	public static String isTimeCorrect(String inTime,String outTime){
		boolean inFlag=true;
		boolean outFlag=true;
		if(inTime.length()<19||!inTime.startsWith("2")){
			inFlag=false;
		}
		if(outTime.length()<19||!outTime.startsWith("2")){
			outFlag=false;
		}
		if(inFlag&&outFlag){
			return "0";
		}else if(!inFlag&&outFlag){
			return "1";
		}else if(inFlag&&!outFlag){
			return "2";
		}else{
			return "3";
		}
	}
	
	public static String fixbinary(int num){
        StringBuilder binaries = new StringBuilder(Integer.toBinaryString(num));

        int len = binaries.length();

        for(int i=0;i<32-len;i++)   //补齐32位
        {
            binaries.insert(0, '0');
        }

        return binaries.toString();
    }
	/*
	 * 编码方式：0-默认(非0元车)、1-非紧急绿通车、2-非绿通紧急车、3-公务车、4-紧急绿通车、5-军车、6-免费车、7-车队、8-其他
	 */
	public static int freetrans(String dealstatus) {
        String dealbin = null;
        dealbin = fixbinary(Integer.parseInt(dealstatus));
        if (dealbin.charAt(7) == '1') {        //是绿通车
            if (dealbin.substring(28, 31).compareTo("101") == 0) {  //又是紧急车
                return 4;
            } else {                            //不是紧急车（只是绿通车）
                return 1;
            }
        }
        if ((dealbin.substring(28, 31).compareTo("101") == 0) && dealbin.charAt(7) != '1') {    //紧急非绿通车
            return 2;
        }
        switch (dealbin.substring(28, 31)) {
            case "011":
                return 3;   //是公务车
            case "100":
                return 5;   //是军车
            case "110":
                return 6;   //是免费车
            case "111":
                return 7;   //是车队
            default:
                return 8;   //其他
        }
    }
	public static String getIdentify(String line){
		String[] data=line.split(",");
		String inLicense=data[10].trim();
		String outLicense=data[20].trim();
		String inCX=data[8].trim();
		String outCX=data[18].trim();
		String inTime=data[4];
		String outTime=data[14];
		String licenseColor=data[26].trim();
		String isETC=data[27].trim();
		String dealStatus=data[22];
		String money=data[21];
		String result="";
		String cxId=isCxEqual(inCX,outCX);
		String timeId=isTimeCorrect(inTime,outTime);
		int freeTrans=0;
		if(Double.parseDouble(money)==0.0){
			freeTrans=freetrans(dealStatus);
		}
		return result+"0"+cxId+timeId+freeTrans;
	}
	public static String getIdentifyCq(String line){
		String[] data=line.split(",");
		String inLicense=data[10].trim();
		String outLicense=data[20].trim();
		String inCX=data[8].trim();
		String outCX=data[18].trim();
		String inTime=data[4];
		String outTime=data[14];
		String licenseColor=data[26].trim();
		String isETC=data[27].trim();
		String money=data[21];
		String result="";
		String cxId=isCxEqual(inCX,outCX);
		String timeId=isTimeCorrect(inTime,outTime);
		
		return result+"0"+cxId+timeId;
	}
	
	public static String getEtcLine(String line,int[] a) throws ParseException{
		int beginIndex=0;
		int endIndex=0;
		String h="";
		for(int k=0;k<a.length;k++){
			endIndex=beginIndex+a[k];
			if(endIndex<=line.length()){
				String w=line.substring(beginIndex, endIndex);
				//贵州ETC：12、22、52、59、67表示车牌；70是省份。都带有中文
				if(k==12||k==22||k==52||k==59||k==67){
					int countC = 0;  
					String regEx = "[\\u4e00-\\u9fa5]";
					String str=w.trim();
					Pattern p = Pattern.compile(regEx);  
					Matcher m = p.matcher(str);  
					while (m.find()){  
						for (int i = 0; i <= m.groupCount(); i++){  
							countC = countC + 1;  
					    }  
					}  
					endIndex-=countC;
					if(w.trim().contains("云Ａ7ＤＹ00")){
						endIndex--;
					}
					w=line.substring(beginIndex, endIndex);
				}
				if(k==70){
					int l=w.trim().getBytes().length;
					endIndex-=l/3;
					endIndex--;
					w=line.substring(beginIndex, endIndex);
				}
				if(k==0){
					h=h+w;
				}else{
					h=h+","+w;
				}
				beginIndex=endIndex+1;
			}
		}
		String[] h1=h.split(",");
		int length=h1.length;
		String h2="";
		if(h1.length==100&&h1[length-1].startsWith("2")){
			for(int i=0;i<h1.length;i++){
				if(i!=h1.length-1){
					h2+=h1[i].trim()+",";
				}else{
					h2+=h1[i].trim();
				}
			}
		}else{
			h2="错误数据";
		}
		return h2;
	}
	public static String getEtcNewLine(String line) throws ParseException{
		String h2="";
		String[] h1=line.split(",");
		for(int i=2;i<=23;i++){
			if(i==6||i==16){
				String time=correctTime(h1[i].trim());
				h2+=time+",";
			}else{
				h2+=h1[i].trim()+",";
			}
		}
		h2+=h1[25].trim()+","+h1[40].trim()+","+h1[47].trim()+","+h1[51].trim()+",";
		if(h1[68].trim().length()>0&&Integer.parseInt(h1[68].trim())<=6){
			h2+=h1[68].trim()+",";
		}else{
			h2+="9"+",";
		}
		h2+="1,";
		h2+=getIdentify(h2);
		return h2;
	}
	
	public static String getMtcLine(String line,int[] a) throws ParseException{
		int beginIndex=0;
		int endIndex=0;
		String h="";
		for(int k=0;k<a.length;k++){
			endIndex=beginIndex+a[k];
			if(endIndex<=line.length()){
				String w=line.substring(beginIndex, endIndex);
				if(k==12||k==22||k==44||k==52){
					int countC = 0;  
					String regEx = "[\\u4e00-\\u9fa5]";
					String str=w.trim();
					Pattern p = Pattern.compile(regEx);  
					Matcher m = p.matcher(str);  
					while (m.find()){  
						for (int i = 0; i <= m.groupCount(); i++){  
							countC = countC + 1;  
					    }  
					}  
					endIndex-=countC;
					w=line.substring(beginIndex, endIndex);
				}
				if(k==55){
					int l=w.trim().getBytes().length;
					endIndex-=l/3;
					if(l!=0){
						endIndex--;
					}
					w=line.substring(beginIndex, endIndex);
				}
				if(k==0){
					h=h+w;
				}else{
					h=h+","+w;
				}
				beginIndex=endIndex+1;
			}
		}
		String[] h1=h.split(",");
		int length=h1.length;
		String h2="";
		if(h1.length==83&&h1[length-1].startsWith("2")){
			for(int i=0;i<h1.length;i++){
				if(i!=h1.length-1){
					h2+=h1[i].trim()+",";
				}else{
					h2+=h1[i].trim();
				}
			}
		}else{
			//错误数据
			h2="错误数据";
		}
		return h2;
	}
	
	public static String getMtcNewLine(String line) throws ParseException{
		String h2="";
		String[] h1=line.split(",");
		for(int i=2;i<=24;i++){
			if(i==6||i==16){
				String time=correctTime(h1[i].trim());
				h2+=time+",";
			}else{
				h2+=h1[i].trim()+",";
			}
		}
		h2+=h1[32].trim()+","+h1[39].trim()+","+h1[43].trim()+",";
		if(h1[45].trim().length()>0&&Integer.parseInt(h1[45].trim())<=6){
			h2+=h1[45].trim()+",";
		}else{
			h2+="9"+",";
		}
		h2+="0,";
		h2+=getIdentify(h2);
		return h2;
	}
	/**
	 * 返回车辆的车型 
	 * @param khFlag 客货标志位
	 * @param vehClass 车型代码
	 * @return 0（小客车） 1（大中型客车）    2（1型货车）   3（2型货车） 4（3型货车） 5（4型货车） 6（5型货车）  
	 */
	public static int getVehicleType(String khFlag,String vehClass){
		if(khFlag.equals("0")&&Integer.valueOf(vehClass)<=1)return 0;
		if(khFlag.equals("0")&&Integer.valueOf(vehClass)>1)return 1;
		if(!khFlag.equals("0")&&Integer.valueOf(vehClass)<=1)return 2;
		if(!khFlag.equals("0")&&Integer.valueOf(vehClass)==2)return 3;
		if(!khFlag.equals("0")&&Integer.valueOf(vehClass)==3)return 4;
		if(!khFlag.equals("0")&&Integer.valueOf(vehClass)==4)return 5;
		if(!khFlag.equals("0")&&Integer.valueOf(vehClass)==5)return 6;
		return -1;
	}
	public static String getLineCQ(String line) throws ParseException{
		String h="";
		String[] data=line.split(";",51);
		if(data.length==51){
			String cardId=data[22].trim();
			String enNetId=data[37].trim();
			String enStation=data[9].trim();
			String enLane="0";
			String enTime=correctTimeCq(data[10].trim());
			String enOperatorId="0";
			String enShiftId="0";
			String enShiftList="0";
			String inCX=data[18].trim();
			String inKH=data[29].trim();
			String inRealCX=""+getVehicleType(inKH,inCX);
			String inLicense=data[20].trim();
			String exNetId=data[38].trim();
			String exStation=data[0].trim();
			String exLane=data[1].trim();
			String exTime=correctTimeCq(data[2].trim());
			String exOperatorId=data[3].trim();
			String exShiftId=data[4].trim();
			String exShiftList="0";
			String outCX=data[11].trim();
			String outKH=data[28].trim();
			String outRealCX=""+getVehicleType(outKH,outCX);
			String outLicense=data[21].trim();
			String money="0";
			String dealStatus=data[45].trim();//重庆是否是绿通车
			if(dealStatus.equals("")){
				dealStatus="E";
			}
			String tollMode="3";
			String totalWeight=data[41].trim();
			String listNo="0";
			String licenseColor="9";
			String isEasyAccess=data[45].trim();
			if(isEasyAccess.equals("")){
				isEasyAccess="E";
			}
			String isEorM=data[48].trim();
			h=cardId+","+enNetId+","+enStation+","+enLane+","+enTime+","+enOperatorId+","+enShiftId+","+enShiftList+","+inRealCX+","+inKH+","+inLicense
					+","+exNetId+","+exStation+","+exLane+","+exTime+","+exOperatorId+","+exShiftId+","+exShiftList+","+outRealCX+","+outKH+","+outLicense
					+","+money+","+dealStatus+","+tollMode+","+totalWeight+","+listNo+","+licenseColor+","+isEorM;
			h=h+","+getIdentifyCq(h)+isEasyAccess;
		}else{
			h="错误数据";
		}
		return h;
	}
	public static void writeOriginalMap(Map<String, HashMap<String,Integer>> originalMap,BufferedWriter writer) throws IOException{
		Set set=originalMap.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
		for(int i=0;i<entries.length;i++){
			String licenseAndColor=entries[i].getKey().toString();//key为车牌号
			Map<String,Integer> map=(Map<String, Integer>) entries[i].getValue();
			Set set1=map.entrySet();
			Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
			String cx="";
			String od="";
			for(int j=0;j<entries1.length;j++){
				String key1=entries1[j].getKey().toString();
				if(key1.contains("-")){
					String value=entries1[j].getValue().toString();
					if(""!=cx){
						cx+="|"+key1+"_"+value;
					}else{
						cx+=key1+"_"+value;
					}
				}else{
					String value=entries1[j].getValue().toString();
					if(""!=od){
						od+="|"+key1+"_"+value;
					}else{
						od+=key1+"_"+value;
					}
				}
			}
			writer.write(licenseAndColor+","+od+","+cx+"\n");
			writer.flush();
		}
	}
	public static BufferedReader getReader(String inPath,String encoding) throws UnsupportedEncodingException, FileNotFoundException{
		File fileIn=new File(inPath);
		InputStreamReader in=new InputStreamReader(new FileInputStream(fileIn),encoding);
		BufferedReader reader=new BufferedReader(in);
		return reader;
	}
	public static BufferedWriter getWriter(String outPath,String encoding) throws UnsupportedEncodingException, FileNotFoundException{
		File fileOut=new File(outPath);
		if(!fileOut.exists()){
			try{
				fileOut.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		OutputStreamWriter out=new OutputStreamWriter(new FileOutputStream(fileOut),encoding);
		BufferedWriter writer=new BufferedWriter(out);
		return writer;
	}
	public static void upDate(String GZETCOriginal,String GZMTCOriginal,String cxDataBase){
		Map<String,HashMap<String,Integer>> mapUpdateTable=new HashMap<>();
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			
			BufferedReader readerEtc=getReader(GZETCOriginal,encoding1);
			BufferedReader readerMtc=getReader(GZMTCOriginal,encoding1);

			BufferedReader readerUpdate=getReader(cxDataBase,encoding);
			
			String lineUpdate="";
			while((lineUpdate=readerUpdate.readLine())!=null){
				String[] data=lineUpdate.split(",");
				String license=data[0];
				String licenseColor=data[1];
				String od=data[2];
				String[] od1=od.split("\\|");
				for(int j=0;j<od1.length;j++){
					String[] odd=od1[j].split("_");
					String enStation=odd[0];
					String exStation=odd[1];
					String odCs=odd[2];
					int odCishu=Integer.parseInt(odCs);
					put(mapUpdateTable,license+","+licenseColor,enStation+"_"+exStation,odCishu);
				}
				String cxAndCs=data[3];
				String[] a=cxAndCs.split("\\|");
				for(int i=0;i<a.length;i++){
					String[] b=a[i].split("_");
					String cx=b[0];
					String cs=b[1];
					int cishu=Integer.parseInt(cs);
					put(mapUpdateTable,license+","+licenseColor,cx,cishu);
				}
			}
			readerUpdate.close();
			
			BufferedWriter writerUpdateTable=getWriter(cxDataBase,encoding);
			
			int countErrEtc=0;
			String lineEtc=null;
			int[] aEtc=new int[100];
			readerEtc.readLine();
			readerEtc.readLine();
			int countEtc=0;
			while((lineEtc=readerEtc.readLine())!=null){
				if(countEtc==0){
					String[] data=lineEtc.split(" ");
					for(int i=0;i<data.length;i++){
						aEtc[i]=data[i].length();
					}
				}else{
					String etcLine=getEtcLine(lineEtc,aEtc);
					String resultLine="";
					if(etcLine.equals("错误数据")){
						countErrEtc++;
					}else{
						String newLine=getEtcNewLine(etcLine);
						String[] newData=newLine.split(",");
						String enStation=newData[2];
						String exStation=newData[12];
						String inLicense=newData[10];
						String outLicense=newData[20];
						String inCX=newData[8];
						String outCX=newData[18];
						String licenseColor=newData[26];
						if(inLicense.equals(outLicense)&&inLicense.length()>6){
							put(mapUpdateTable,inLicense+","+licenseColor,enStation+"_"+exStation,1);
							if(!inCX.equals(outCX)){
								put(mapUpdateTable,inLicense+","+licenseColor,inCX+"/"+outCX+"-1",1);
							}else{
								put(mapUpdateTable,inLicense+","+licenseColor,inCX+"-1",1);
							}
						}else{
							
						}
					}
				}
				countEtc++;
			}
			readerEtc.close();
			System.out.println("etc finish!");
			
			
			int countErrMtc=0;
			int amountMtc=0;
			String lineMtc=null;
			int[] aMtc=new int[83];
			readerMtc.readLine();
			readerMtc.readLine();
			int countMtc=0;
			while((lineMtc=readerMtc.readLine())!=null){
				if(countMtc==0){
					String[] data=lineMtc.split(" ");
					for(int i=0;i<data.length;i++){
						aMtc[i]=data[i].length();
					}
				}else{
					String mtcLine=getMtcLine(lineMtc,aMtc);
					String resultLine="";
					if(mtcLine.equals("错误数据")){
						countErrMtc++;
					}else{
						String newLine=getMtcNewLine(mtcLine);
						String[] newData=newLine.split(",");
						String enStation=newData[2];
						String exStation=newData[12];
						String inLicense=newData[10];
						String outLicense=newData[20];
						String inCX=newData[8];
						String outCX=newData[18];
						String licenseColor=newData[26];
//						if(!inLicense.equals(outLicense)){
//							
//						}
						if(inLicense.equals(outLicense)&&inLicense.length()>6){
							put(mapUpdateTable,inLicense+","+licenseColor,enStation+"_"+exStation,1);
							if(!inCX.equals(outCX)){
								put(mapUpdateTable,inLicense+","+licenseColor,inCX+"/"+outCX+"-0",1);
							}else{
								put(mapUpdateTable,inLicense+","+licenseColor,inCX+"-0",1);
							}
						}else{
							
						}
					}
				}
				countMtc++;
			}
			readerMtc.close();
			amountMtc=countMtc-1;
			writeOriginalMap(mapUpdateTable,writerUpdateTable);
			writerUpdateTable.close();
			System.out.println("ETC错误数据条数："+countErrEtc);
			System.out.println();
			System.out.println("MTC错误数据条数："+countErrMtc);
			System.out.println("MTC总数据条数："+amountMtc);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void upDateCQ(String CQOriginal,String cxDataBaseCQ){
		File fileIn=new File(CQOriginal);
		
		File fileUpdateTable=new File(cxDataBaseCQ);
		if(!fileUpdateTable.exists()){
			try{
				fileUpdateTable.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		Map<String,HashMap<String,Integer>> mapUpdateTable=new HashMap<>();
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			InputStreamReader in=new InputStreamReader(new FileInputStream(fileIn),encoding1);
			BufferedReader reader=new BufferedReader(in);
			
			InputStreamReader inUpdate=new InputStreamReader(new FileInputStream(fileUpdateTable),encoding);
			BufferedReader readerUpdate=new BufferedReader(inUpdate);
			
			String lineUpdate="";
			while((lineUpdate=readerUpdate.readLine())!=null){
				String[] data=lineUpdate.split(",");
				String license=data[0];
				String licenseColor=data[1];
				String od=data[2];
				String[] od1=od.split("\\|");
				for(int j=0;j<od1.length;j++){
					String[] odd=od1[j].split("_");
					String enStation=odd[0];
					String exStation=odd[1];
					String odCs=odd[2];
					int odCishu=Integer.parseInt(odCs);
					put(mapUpdateTable,license+","+licenseColor,enStation+"_"+exStation,odCishu);
				}
				String cxAndCs=data[3];
				String[] a=cxAndCs.split("\\|");
				for(int i=0;i<a.length;i++){
					String[] b=a[i].split("_");
					String cx=b[0];
					String cs=b[1];
					int cishu=Integer.parseInt(cs);
					put(mapUpdateTable,license+","+licenseColor,cx,cishu);
				}
			}
			readerUpdate.close();
			
			File fileOutUpdateTable=new File(cxDataBaseCQ);
			OutputStreamWriter writeUpdateTable=new OutputStreamWriter(new FileOutputStream(fileOutUpdateTable),encoding);
			BufferedWriter writerUpdateTable=new BufferedWriter(writeUpdateTable);
			
			int countErr=0;
			int amount=0;
			String line=null;
			while((line=reader.readLine())!=null){
				amount++;
				String newLine=getLineCQ(line);
				String resultLine="";
				if(newLine.equals("错误数据")){
					countErr++;
				}else{
					String[] newData=newLine.split(",");
					String enStation=newData[2];
					String exStation=newData[12];
					String inLicense=newData[10];
					String outLicense=newData[20];
					String inCX=newData[8];
					String outCX=newData[18];
					String licenseColor=newData[26];
					String isETC=newData[27];
					if(isETC.equals("0")){
						if(inLicense.equals(outLicense)&&inLicense.length()>6){
							put(mapUpdateTable,inLicense+","+licenseColor,enStation+"_"+exStation,1);
							if(!inCX.equals(outCX)){
								put(mapUpdateTable,inLicense+","+licenseColor,inCX+"/"+outCX+"-0",1);
							}else{
								put(mapUpdateTable,inLicense+","+licenseColor,inCX+"-0",1);
							}
						}
					}else{
						if(inLicense.equals(outLicense)&&inLicense.length()>6){
							put(mapUpdateTable,inLicense+","+licenseColor,enStation+"_"+exStation,1);
							if(!inCX.equals(outCX)){
								put(mapUpdateTable,inLicense+","+licenseColor,inCX+"/"+outCX+"-1",1);
							}else{
								put(mapUpdateTable,inLicense+","+licenseColor,inCX+"-1",1);
							}
						}
					}
				}
			}
			reader.close();
			writeOriginalMap(mapUpdateTable,writerUpdateTable);
			writerUpdateTable.close();
			System.out.println("错误数据条数："+countErr);
			System.out.println("总条数："+amount);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
//		upDate(GZETCOriginal,GZMTCOriginal,cxDataBase);
		upDateCQ(CQOriginal,cxDataBaseCQ);
	}
}
