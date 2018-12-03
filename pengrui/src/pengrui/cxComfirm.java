package pengrui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class cxComfirm {
	private static String cxDataBase="H:/贵州数据/车牌车型数据库.csv";
	
	private static String etcComfirm="H:/贵州数据/车牌车型对应表/etcComfirm.csv";
	private static String etcNotComfirm="H:/贵州数据/车牌车型对应表/etcNotComfirm.csv";
	private static String mtcComfirm="H:/贵州数据/车型确认/车牌车型对应表/mtcComfirm.csv";
	private static String mtcNotComfirm="H:/贵州数据/车牌车型对应表/mtcNotComfirm.csv";
	
	public static void insertSort(int[] a ){  
	    int temp=0;  
	    for(int i=1;i<a.length;i++){  
	       int j=i-1;  
	       temp=a[i];  
	       for(;j>=0&&temp<a[j];j--){  
	       a[j+1]=a[j];                       //将大于temp的值整体后移一个单位  
	       }  
	       a[j+1]=temp;  
	    }  
	}  
	private static void addFloat(Map<String,String> map,String a,String b){
		if(map.size()==0){
			map.put(a, b);
		}else{
			if(map.containsKey(a)){
				float p=Float.parseFloat(map.get(a));
				float p1=Float.parseFloat(b);
				String p2=""+(p+p1);
				map.put(a, p2);
			}else{
				map.put(a, b);
			}
		}
	}
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
	
	//取车型唯一的数据
	public static Map<String, HashMap<String,Integer>> getMapOneCX(Map<String, HashMap<String,Integer>> mapCXEqual){
		Map<String, HashMap<String,Integer>> mapOneCX=new HashMap<>();
		Set set=mapCXEqual.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]);
		for(int i=0;i<entries.length;i++){
			String license=entries[i].getKey().toString();
			Map<String,Integer> map=(Map<String, Integer>) entries[i].getValue();
			if(map.size()==1){
				Set set1=map.entrySet();
				Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
				for(int j=0;j<entries1.length;j++){
					String cx=entries1[j].getKey().toString();
					put(mapOneCX,license,cx+","+"100%");
				}
			}
		}
		return mapOneCX;
	}
	public static String getMaxCountCX(Map<String,Integer> map){
		Set set1=map.entrySet();
		Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
		int[] a=new int[entries1.length];
		for(int j=0;j<entries1.length;j++){
			int value=(int) entries1[j].getValue();
			a[j]=value;
		}
		insertSort(a);
		int length=a.length;
		int max=a[length-1];
		int lessMax=a[length-2];
		String maxCountCX=null;
		for(int k=0;k<entries1.length;k++){
			if(map.get(entries1[k].getKey())==max){
				maxCountCX=entries1[k].getKey().toString();
			}
		}
		return maxCountCX;
	}
	public static String getMaxAndLessMaxCountCX(Map<String,Integer> map){
		Set set1=map.entrySet();
		Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
		int[] a=new int[entries1.length];
		for(int j=0;j<entries1.length;j++){
			int value=(int) entries1[j].getValue();
			a[j]=value;
		}
		insertSort(a);
		int length=a.length;
		int max=a[length-1];
		int lessMax=a[length-2];
		String maxCountCX=null;
		String lessMaxCountCX=null;
		if(max==lessMax){
			String[] b1=new String[entries1.length];
			int bc=0;
			for(int k=0;k<entries1.length;k++){
				if(map.get(entries1[k].getKey())==max){
					b1[bc]=entries1[k].getKey().toString();
					bc++;
				}
			}
			maxCountCX=b1[0];
			lessMaxCountCX=b1[1];
		}else{
			for(int k=0;k<entries1.length;k++){
				if(map.get(entries1[k].getKey())==max){
					maxCountCX=entries1[k].getKey().toString();
				}
				if(map.get(entries1[k].getKey())==lessMax){
					lessMaxCountCX=entries1[k].getKey().toString();
				}
			}
		}
		return maxCountCX+","+lessMaxCountCX;
	}
	public static String getUsedMaxCX(Map<String,Integer> map){
		Set set1=map.entrySet();
		Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
		int mCXNumber=0;
		for(int j=0;j<entries1.length;j++){
			int key1=0;
			if(!entries1[j].getKey().toString().equals("")){
				key1=Integer.parseInt(entries1[j].getKey().toString());
			}
			if(mCXNumber<key1){
				mCXNumber=key1;
			}
		}
		return ""+mCXNumber;
	}
	public static String getPersent(Map<String,Integer> map,String cx){
		Set set1=map.entrySet();
		Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
		int countCS=0;
		int cxAppearCS=0;
		for(int j=0;j<entries1.length;j++){
			String key=entries1[j].getKey().toString();
			int value=(int) entries1[j].getValue();
			countCS+=value;
			if(key.equals(cx)){
				cxAppearCS=value;
			}
		}
		float p=(float)cxAppearCS/(float)countCS*100;
		DecimalFormat decimalFormat=new DecimalFormat(".00");
		String persent=decimalFormat.format(p)+"%";
		return persent;
	}
	
	//取车型不唯一的数据
	public static Map<String, HashMap<String,Integer>> getMapTwoCX(Map<String, HashMap<String,Integer>> mapCXEqual){
		Map<String, HashMap<String,Integer>> outMap=new HashMap<>();
		Set set=mapCXEqual.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]);
		for(int i=0;i<entries.length;i++){
			String license=entries[i].getKey().toString();
			Map<String,Integer> map=(Map<String, Integer>) entries[i].getValue();
			if(map.size()>1){
				String mL=getMaxAndLessMaxCountCX(map);
				String [] mL1=mL.split(",");
				String maxCountCX=mL1[0];
//				String lessMaxCountCX=mL1[1];
//				String usedMaxCX=getUsedMaxCX(map);
				String persentMax=getPersent(map,maxCountCX);
//					if(isDX(maxCountCX,usedMaxCX)){
//						put(outMap,license,maxCountCX);
//					}else{
//						put(outMap,license,usedMaxCX);
//					}
				put(outMap,license,maxCountCX+","+persentMax);
			}
		}
		return outMap;
	}
	//取出车牌相同，车型不同数据中，车牌号在车型唯一数据中出现过的数据
	public static Map<String, HashMap<String,Integer>> getLicenseMatch(Map<String, HashMap<String,Integer>> mapCXNotEqual,Map<String, HashMap<String,Integer>> mapCXEqual){
		Map<String, HashMap<String,Integer>> outMap=new HashMap<>();
		Set setCXNotEqual=mapCXNotEqual.entrySet();
		Map.Entry[] entriesCXNotEqual = (Map.Entry[])setCXNotEqual.toArray(new Map.Entry[setCXNotEqual.size()]);
		for(int i=0;i<entriesCXNotEqual.length;i++){
			String licenseCXNotEqual=entriesCXNotEqual[i].getKey().toString();
			Map<String,Integer> mapCXNotE=(Map<String, Integer>) entriesCXNotEqual[i].getValue();
			if(mapCXEqual.containsKey(licenseCXNotEqual)){
				Set set2=mapCXNotE.entrySet();
				Map.Entry[] entries2=(Map.Entry[])set2.toArray(new Map.Entry[set2.size()]);
				for(int k=0;k<entries2.length;k++){
					String cx=entries2[k].getKey().toString();
					put(outMap,licenseCXNotEqual,cx);
				}
			}
		}
		return outMap;
	}
	
	//取出车牌相同，车型不同数据中，车牌号在车型唯一数据中没有出现过的数据
	public static Map<String, HashMap<String,Integer>> getLicenseNotMatch(Map<String, HashMap<String,Integer>> mapCXNotEqual,Map<String, HashMap<String,Integer>> mapCXEqual){
		Map<String, HashMap<String,Integer>> outMap=new HashMap<>();
		Set setCXNotEqual=mapCXNotEqual.entrySet();
		Map.Entry[] entriesCXNotEqual = (Map.Entry[])setCXNotEqual.toArray(new Map.Entry[setCXNotEqual.size()]);
		for(int i=0;i<entriesCXNotEqual.length;i++){
			String licenseCXNotEqual=entriesCXNotEqual[i].getKey().toString();
			Map<String,Integer> mapCXNotE=(Map<String, Integer>) entriesCXNotEqual[i].getValue();
			if(!mapCXEqual.containsKey(licenseCXNotEqual)){
				Set set2=mapCXNotE.entrySet();
				Map.Entry[] entries2=(Map.Entry[])set2.toArray(new Map.Entry[set2.size()]);
				for(int k=0;k<entries2.length;k++){
					String cx=entries2[k].getKey().toString();
					put(outMap,licenseCXNotEqual,cx);
				}
			}
		}
		return outMap;
	}
	
	//对于同一车牌号，通过车型唯一的数据和出入车型不同的数据，计算出、入车型的正确率
	public static String getInAndOutCXPersent(Map<String, HashMap<String,Integer>> mapOneCX,Map<String, HashMap<String,Integer>> mapCXNotEqualLicenseMatch){
		int inCXRightCount=0;
		int outCXRightCount=0;
		Set setCXNotEqualLicenseEqual=mapCXNotEqualLicenseMatch.entrySet();
		Map.Entry[] entriesCXNotEqualLicenseEqual = (Map.Entry[])setCXNotEqualLicenseEqual.toArray(new Map.Entry[setCXNotEqualLicenseEqual.size()]);
		for(int i=0;i<entriesCXNotEqualLicenseEqual.length;i++){
			String licenseCXNotEqual=entriesCXNotEqualLicenseEqual[i].getKey().toString();
			Map<String,Integer> mapCXNotE=(Map<String, Integer>) entriesCXNotEqualLicenseEqual[i].getValue();
			if(mapOneCX.containsKey(licenseCXNotEqual)){
				Map<String,Integer> map=mapOneCX.get(licenseCXNotEqual);
				Set set1=map.entrySet();
				Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
				String cx="";
				for(int j=0;j<entries1.length;j++){
					String cxAndPersent=entries1[j].getKey().toString();
					String[] a=cxAndPersent.split(",",2);
					cx=a[0];
					int cs=Integer.parseInt(entries1[j].getValue().toString());
					inCXRightCount+=cs;
					outCXRightCount+=cs;
				}
				Set set2=mapCXNotE.entrySet();
				Map.Entry[] entries2=(Map.Entry[])set2.toArray(new Map.Entry[set2.size()]);
				for(int k=0;k<entries2.length;k++){
					String cxNotE=entries2[k].getKey().toString();
					int cs=Integer.parseInt(entries2[k].getValue().toString());
					String[] a=cxNotE.split("/",2);
					String inCXNotE=a[0];
					String outCXNotE=a[1];
					if(inCXNotE.equals(cx)){
						inCXRightCount++;
					}
					if(outCXNotE.equals(cx)){
						outCXRightCount++;
					}
				}
			}
		}
		float pIn=(float)inCXRightCount/(float)(inCXRightCount+outCXRightCount);
		float pOut=(float)outCXRightCount/(float)(inCXRightCount+outCXRightCount);
		DecimalFormat decimalFormat=new DecimalFormat("0.00");
		String inCXRightPersent=decimalFormat.format(pIn);
		String outCXRightPersent=decimalFormat.format(pOut);
		return inCXRightPersent+","+outCXRightPersent;
	}
	
	private static Map<String, String> getMapCxNotEqualLicenseNotMatch(Map<String, HashMap<String,Integer>> mapLicenseNotMatch,String inCXRightPersent,String outCXRightPersent){
		float inPersent=Float.parseFloat(inCXRightPersent);
		float outPersent=Float.parseFloat(outCXRightPersent);
		Map<String, String> outMap=new HashMap<>();
		Set setLicense=mapLicenseNotMatch.entrySet();
		Map.Entry[] entriesLicense = (Map.Entry[])setLicense.toArray(new Map.Entry[setLicense.size()]);
		for(int i=0;i<entriesLicense.length;i++){
			float amount=0;
			Map<String,String> mapResult=new HashMap<>();
			Map<String,String> mapCountIn=new HashMap<>();
			Map<String,String> mapCountOut=new HashMap<>();
			String license=entriesLicense[i].getKey().toString();
			Map<String,Integer> mapIn=(Map<String, Integer>) entriesLicense[i].getValue();
			Set set1=mapIn.entrySet();
			Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
			for(int j=0;j<entries1.length;j++){
				String cx=entries1[j].getKey().toString();
				String cs=entries1[j].getValue().toString();
				String[] a=cx.split("/");
				if(a.length==1){
					String inCX=a[0];
					addFloat(mapCountIn,inCX,cs);
				}else if(a.length==2){
					String inCX=a[0];
					String outCX=a[1];
					addFloat(mapCountIn,inCX,cs);
					addFloat(mapCountOut,outCX,cs);
				}
			}
			Set setInCX=mapCountIn.entrySet();
			Map.Entry[] entriesInCX = (Map.Entry[])setInCX.toArray(new Map.Entry[setInCX.size()]);
			for(int j=0;j<entriesInCX.length;j++){
				String chexing=entriesInCX[j].getKey().toString();
				float cs=Float.parseFloat(entriesInCX[j].getValue().toString());
				String p=""+cs*inPersent;
				addFloat(mapResult,chexing,p);
			}
			Set setOutCX=mapCountOut.entrySet();
			Map.Entry[] entriesOutCX = (Map.Entry[])setOutCX.toArray(new Map.Entry[setOutCX.size()]);
			for(int j=0;j<entriesOutCX.length;j++){
				String chexing=entriesOutCX[j].getKey().toString();
				float cs=Float.parseFloat(entriesOutCX[j].getValue().toString());
				String p=""+cs*outPersent;
				addFloat(mapResult,chexing,p);
			}
			Set setResult=mapResult.entrySet();
//			System.out.println("mapResult:"+license+","+mapResult);
			Map.Entry[] entriesResult = (Map.Entry[])setResult.toArray(new Map.Entry[setResult.size()]);
			float max=0;
			for(int j=0;j<entriesResult.length;j++){
				String chexing=entriesResult[j].getKey().toString();
				float p=Float.parseFloat(entriesResult[j].getValue().toString());
				if(max<p){
					max=p;
				}
				amount+=p;
			}
			for(int j=0;j<entriesResult.length;j++){
				String chexing=entriesResult[j].getKey().toString();
				String value=entriesResult[j].getValue().toString();
				float p=Float.parseFloat(value);
				if(max==p){
					max=p;
					p=p/amount*100;
					DecimalFormat decimalFormat=new DecimalFormat("0.00");
					String persent=decimalFormat.format(p)+"%";
					outMap.put(license, chexing+","+persent);
//					outMap.put(license, chexing);//只输出车型
				}
			}
		}
		return outMap;
	}
	
	public static Map<String, HashMap<String,Integer>> getMapOneCXMTCOnly(Map<String, HashMap<String,Integer>> mapCXEqual,Set<String> licenseETC){
		Map<String, HashMap<String,Integer>> mapOneCX=new HashMap<>();
		Set set=mapCXEqual.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]);
		for(int i=0;i<entries.length;i++){
			String license=entries[i].getKey().toString();
			Map<String,Integer> map=(Map<String, Integer>) entries[i].getValue();
			if(map.size()==1&&!licenseETC.contains(license)){
				Set set1=map.entrySet();
				Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
				for(int j=0;j<entries1.length;j++){
					String cx=entries1[j].getKey().toString();
					put(mapOneCX,license,cx+","+"100%");
				}
			}
		}
		return mapOneCX;
	}
	
	public static Map<String, HashMap<String,Integer>> getMapTwoCXMTCOnly(Map<String, HashMap<String,Integer>> mapCXEqual,Set<String> licenseETC){
		Map<String, HashMap<String,Integer>> outMap=new HashMap<>();
		Set set=mapCXEqual.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]);
		for(int i=0;i<entries.length;i++){
			String license=entries[i].getKey().toString();
			Map<String,Integer> map=(Map<String, Integer>) entries[i].getValue();
			if(map.size()>1&&!licenseETC.contains(license)){
				String mL=getMaxAndLessMaxCountCX(map);
				String [] mL1=mL.split(",");
				String maxCountCX=mL1[0];
//				String lessMaxCountCX=mL1[1];
//				String usedMaxCX=getUsedMaxCX(map);
				String persentMax=getPersent(map,maxCountCX);
//				String persentLessMax=getPersent(map,lessMaxCountCX);
//				if(isDX(maxCountCX,usedMaxCX)){
//					put(outMap,license,maxCountCX);
//				}else{
//					put(outMap,license,usedMaxCX);
//				}
				put(outMap,license,maxCountCX+","+persentMax);
			}
		}
		return outMap;
	}
	
	//取出车牌相同，车型不同数据中，车牌号在车型唯一数据中没有出现过的数据
	public static Map<String, HashMap<String,Integer>> getLicenseNotMatchForMtc(Map<String, HashMap<String,Integer>> mapCXNotEqual,Map<String, HashMap<String,Integer>> mapCXEqual,Set<String> licenseETC){
		Map<String, HashMap<String,Integer>> outMap=new HashMap<>();
		Set setCXNotEqual=mapCXNotEqual.entrySet();
		Map.Entry[] entriesCXNotEqual = (Map.Entry[])setCXNotEqual.toArray(new Map.Entry[setCXNotEqual.size()]);
		for(int i=0;i<entriesCXNotEqual.length;i++){
			String licenseCXNotEqual=entriesCXNotEqual[i].getKey().toString();
			Map<String,Integer> mapCXNotE=(Map<String, Integer>) entriesCXNotEqual[i].getValue();
			if(!mapCXEqual.containsKey(licenseCXNotEqual)&&!licenseETC.contains(licenseCXNotEqual)){
				Set set2=mapCXNotE.entrySet();
				Map.Entry[] entries2=(Map.Entry[])set2.toArray(new Map.Entry[set2.size()]);
				for(int k=0;k<entries2.length;k++){
					String cx=entries2[k].getKey().toString();
					put(outMap,licenseCXNotEqual,cx);
				}
			}
		}
		return outMap;
	}
	
	public static void writeOriginalMap(Map<String, HashMap<String,Integer>> originalMap,BufferedWriter writer) throws IOException{
		Set set=originalMap.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();//key为车牌号
			Map<String,Integer> map=(Map<String, Integer>) entries[i].getValue();
			Set set1=map.entrySet();
			Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
			for(int j=0;j<entries1.length;j++){
				String key1=entries1[j].getKey().toString();
				String value=entries1[j].getValue().toString();
				writer.write(key+","+key1+"\n");
				writer.flush();
			}
		}
	}
	
	public static void writeMap(Map<String,String> map,BufferedWriter writer)throws IOException{
		Set set=map.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();
			String value=entries[i].getValue().toString();
			writer.write(key+","+value+"\n");
			writer.flush();
		}
	}
	
	public static void cxComfirm(String cxDataBase,String etcComfirm,String etcNotComfirm,String mtcComfirm,String mtcNotComfirm){
		File file=new File(cxDataBase);
		
		File fileEtcComfirmOneCX=new File(etcComfirm);
		File fileEtcNotComfirm=new File(etcNotComfirm);
		File fileMtcComfirmOneCX=new File(mtcComfirm);
		File fileMtcNotComfirm=new File(mtcNotComfirm);

		
		Map<String, HashMap<String,Integer>> mapMtcOriginal = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapMtcCxNotEqualOriginal = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapEtcOriginal = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapEtcCxNotEqualOriginal = new HashMap<>();

		
		Map<String, HashMap<String,Integer>> mapMtcOneCX = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapMtcTwoCX = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapMtcLicenseMatch = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapMtcLicenseNotMatch = new HashMap<>();
		
		Map<String, HashMap<String,Integer>> mapEtcOneCX = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapEtcTwoCX = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapEtcLicenseMatch = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapEtcLicenseNotMatch = new HashMap<>();

		Map<String,String> mapEtcCxNotEqualLicenseNotMatch=new HashMap<>();
		Map<String,String> mapMtcCxNotEqualLicenseNotMatch=new HashMap<>();
		Set<String> licenseCount=new HashSet<>();//出入口车牌相同的车牌数统计
		Set<String> licenseEtc=new HashSet<>();//出入口车牌相同的车牌数统计
		
		Map<String,String> damageMap=new HashMap<>();	
		
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			int countMoreCX=0;
			int amount=0;
			int cxNotEqualMtc=0;
			int cxNotEqualEtc=0;
			InputStreamReader in=new InputStreamReader(new FileInputStream(file),encoding);
			BufferedReader reader=new BufferedReader(in);
			
			OutputStreamWriter outEtcComfirm=new OutputStreamWriter(new FileOutputStream(fileEtcComfirmOneCX),encoding1);
			BufferedWriter writerEtcComfirm=new BufferedWriter(outEtcComfirm);
			OutputStreamWriter outEtcNotComfirm=new OutputStreamWriter(new FileOutputStream(fileEtcNotComfirm),encoding1);
			BufferedWriter writerEtcNotComfirm=new BufferedWriter(outEtcNotComfirm);
			OutputStreamWriter outMtcComfirm=new OutputStreamWriter(new FileOutputStream(fileMtcComfirmOneCX),encoding1);
			BufferedWriter writerMtcComfirm=new BufferedWriter(outMtcComfirm);
			OutputStreamWriter outMtcNotComfirm=new OutputStreamWriter(new FileOutputStream(fileMtcNotComfirm),encoding1);
			BufferedWriter writerMtcNotComfirm=new BufferedWriter(outMtcNotComfirm);
			
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",4);
				String license=data[0];
				String licenseColor=data[1];
				String cxAndCx=data[3];
				
				String[] a=cxAndCx.split("\\|");
				for(int i=0;i<a.length;i++){
					String b=a[i];
					String[] c=b.split("_",2);
					String chexing=c[0];
					String[] chexingS=chexing.split("-");
					String cx=chexingS[0];
					String isETC=chexingS[1];
					String cs=c[1];
//					System.out.println(cx+","+cs);
					if(cx.contains("/")){
						licenseCount.add(license+","+licenseColor);
						if(isETC.equals("1")){
							licenseEtc.add(license+","+licenseColor);
							put(mapEtcCxNotEqualOriginal,license+","+licenseColor,cx,Integer.parseInt(cs));
						}else{
							put(mapMtcCxNotEqualOriginal,license+","+licenseColor,cx,Integer.parseInt(cs));
						}
					}else{
						licenseCount.add(license+","+licenseColor);
						if(isETC.equals("1")){
							licenseEtc.add(license+","+licenseColor);
							put(mapEtcOriginal,license+","+licenseColor,cx,Integer.parseInt(cs));
						}else{
							put(mapMtcOriginal,license+","+licenseColor,cx,Integer.parseInt(cs));
						}
					}
				}
			}
			reader.close();
			System.out.println("read cxDataBase finish");
			
			mapEtcOneCX=getMapOneCX(mapEtcOriginal);
			mapEtcTwoCX=getMapTwoCX(mapEtcOriginal);
			mapEtcOriginal.clear();

			mapEtcLicenseMatch=getLicenseMatch(mapEtcCxNotEqualOriginal,mapEtcOneCX);
			mapEtcLicenseNotMatch=getLicenseNotMatch(mapEtcCxNotEqualOriginal,mapEtcOneCX);
			mapEtcCxNotEqualOriginal.clear();
			String etcInCxPersent="0.5";
			String etcOutCxPersent="0.5";
			if(mapEtcLicenseMatch.size()==0){
				System.out.println("车牌相同、车型不同的所有ETC数据里的车牌，没有在车型唯一ETC的数据里出现");
			}else{
				String aEtc=getInAndOutCXPersent(mapEtcOneCX,mapEtcLicenseMatch);
				String[] aEtc1=aEtc.split(",");
				etcInCxPersent=aEtc1[0];
				etcOutCxPersent=aEtc1[1];
				System.out.println("etcInCxPersent:"+etcInCxPersent);
				System.out.println("etcOutCxPersent:"+etcOutCxPersent);
			}
			mapEtcCxNotEqualLicenseNotMatch=getMapCxNotEqualLicenseNotMatch(mapEtcLicenseNotMatch,etcInCxPersent,etcOutCxPersent);
			
			mapMtcOneCX=getMapOneCXMTCOnly(mapMtcOriginal,licenseEtc);
			mapMtcTwoCX=getMapTwoCXMTCOnly(mapMtcOriginal,licenseEtc);
			mapMtcOriginal.clear();
			mapMtcLicenseMatch=getLicenseMatch(mapMtcCxNotEqualOriginal,mapMtcOneCX);
			mapMtcLicenseNotMatch=getLicenseNotMatchForMtc(mapMtcCxNotEqualOriginal,mapMtcOneCX,licenseEtc);
			mapMtcCxNotEqualOriginal.clear();
			String mtcInCxPersent="0.5";
			String mtcOutCxPersent="0.5";
			if(mapMtcLicenseMatch.size()==0){
				System.out.println("车牌相同、车型不同的所有MTC数据里的车牌，没有在车型唯一的MTC数据里出现");
			}else{
				String aMtc=getInAndOutCXPersent(mapMtcOneCX,mapMtcLicenseMatch);
				String[] aMtc1=aMtc.split(",");
				mtcInCxPersent=aMtc1[0];
				mtcOutCxPersent=aMtc1[1];
				System.out.println("mtcInCxPersent:"+mtcInCxPersent);
				System.out.println("mtcOutCxPersent:"+mtcOutCxPersent);
			}
			mapMtcCxNotEqualLicenseNotMatch=getMapCxNotEqualLicenseNotMatch(mapMtcLicenseNotMatch,mtcInCxPersent,mtcOutCxPersent);
			
			
			//综合结果
			writeOriginalMap(mapEtcOneCX,writerEtcComfirm);
			writeOriginalMap(mapEtcTwoCX,writerEtcComfirm);
			writerEtcComfirm.close();
			writeMap(mapEtcCxNotEqualLicenseNotMatch,writerEtcNotComfirm);
			writerEtcNotComfirm.close();
			
			writeOriginalMap(mapMtcOneCX,writerMtcComfirm);
			writeOriginalMap(mapMtcTwoCX,writerMtcComfirm);
			writerMtcComfirm.close();
			writeMap(mapMtcCxNotEqualLicenseNotMatch,writerMtcNotComfirm);
			writerMtcComfirm.close();
			
			
			int size1=mapEtcOneCX.size()+mapMtcOneCX.size()+mapEtcTwoCX.size()+mapMtcTwoCX.size();
			int size2=mapEtcLicenseNotMatch.size()+mapMtcLicenseNotMatch.size();;
			
			System.out.println("etc确认车型："+mapEtcOneCX.size());
			System.out.println("etc车型不唯一："+mapEtcTwoCX.size());
			System.out.println("etc车牌相同车型不同："+mapEtcLicenseNotMatch.size());
			System.out.println("mtc确认车型："+mapMtcOneCX.size());
			System.out.println("mtc车型不唯一："+mapMtcTwoCX.size());
			System.out.println("mtc车牌相同车型不同："+mapMtcLicenseNotMatch.size());
			System.out.println("确认车型车牌数："+size1+" 有车牌颜色："+(mapEtcOneCX.size()+mapEtcTwoCX.size())+" 无车牌颜色："+(mapMtcOneCX.size()+mapMtcTwoCX.size()));
			System.out.println("不确认车型的车牌数："+size2);
			System.out.println("总车牌数："+(size1+size2));
			float a=(float)size1/(float)(size1+size2)*100;
			DecimalFormat decimalFormat=new DecimalFormat("0.00");
			String persent=decimalFormat.format(a)+"%";
			System.out.println("匹配率："+persent);
		}catch(Exception e){
			System.out.println("读取文件出错");
			e.printStackTrace();
		}
	}
	public static void cxComfirm1(String cxDataBase,String cxComfirm,String cxNotComfirm){
		File file=new File(cxDataBase);
		
		File fileCxComfirm=new File(cxComfirm);
		File fileCxNotComfirm=new File(cxNotComfirm);

		
		Map<String, HashMap<String,Integer>> mapMtcOriginal = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapMtcCxNotEqualOriginal = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapEtcOriginal = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapEtcCxNotEqualOriginal = new HashMap<>();

		
		Map<String, HashMap<String,Integer>> mapMtcOneCX = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapMtcTwoCX = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapMtcLicenseMatch = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapMtcLicenseNotMatch = new HashMap<>();
		
		Map<String, HashMap<String,Integer>> mapEtcOneCX = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapEtcTwoCX = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapEtcLicenseMatch = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapEtcLicenseNotMatch = new HashMap<>();

		Map<String,String> mapEtcCxNotEqualLicenseNotMatch=new HashMap<>();
		Map<String,String> mapMtcCxNotEqualLicenseNotMatch=new HashMap<>();
		Set<String> licenseCount=new HashSet<>();//出入口车牌相同的车牌数统计
		Set<String> licenseEtc=new HashSet<>();//出入口车牌相同的车牌数统计
		
		Map<String,String> damageMap=new HashMap<>();	
		
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			int countMoreCX=0;
			int amount=0;
			int cxNotEqualMtc=0;
			int cxNotEqualEtc=0;
			BufferedReader reader=dataService.getReader(cxDataBase, encoding);
			
			BufferedWriter writerCxComfirm=dataService.getWriter(cxComfirm, encoding1);
			BufferedWriter writerCxNotComfirm=dataService.getWriter(cxNotComfirm, encoding1);
			
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",4);
				String license=data[0];
				String licenseColor=data[1];
				String cxAndCx=data[3];
				
				String[] a=cxAndCx.split("\\|");
				for(int i=0;i<a.length;i++){
					String b=a[i];
					String[] c=b.split("_",2);
					String chexing=c[0];
					String[] chexingS=chexing.split("-");
					String cx=chexingS[0];
					String isETC=chexingS[1];
					String cs=c[1];
//					System.out.println(cx+","+cs);
					if(cx.contains("/")){
						licenseCount.add(license+","+licenseColor);
						if(isETC.equals("1")){
							licenseEtc.add(license+","+licenseColor);
							put(mapEtcCxNotEqualOriginal,license+","+licenseColor,cx,Integer.parseInt(cs));
						}else{
							put(mapMtcCxNotEqualOriginal,license+","+licenseColor,cx,Integer.parseInt(cs));
						}
					}else{
						licenseCount.add(license+","+licenseColor);
						if(isETC.equals("1")){
							licenseEtc.add(license+","+licenseColor);
							put(mapEtcOriginal,license+","+licenseColor,cx,Integer.parseInt(cs));
						}else{
							put(mapMtcOriginal,license+","+licenseColor,cx,Integer.parseInt(cs));
						}
					}
				}
			}
			reader.close();
			System.out.println("read cxDataBase finish");
			
			mapEtcOneCX=getMapOneCX(mapEtcOriginal);
			mapEtcTwoCX=getMapTwoCX(mapEtcOriginal);
			mapEtcOriginal.clear();

			mapEtcLicenseMatch=getLicenseMatch(mapEtcCxNotEqualOriginal,mapEtcOneCX);
			mapEtcLicenseNotMatch=getLicenseNotMatch(mapEtcCxNotEqualOriginal,mapEtcOneCX);
			mapEtcCxNotEqualOriginal.clear();
			String etcInCxPersent="0.5";
			String etcOutCxPersent="0.5";
			if(mapEtcLicenseMatch.size()==0){
				System.out.println("车牌相同、车型不同的所有ETC数据里的车牌，没有在车型唯一ETC的数据里出现");
			}else{
				String aEtc=getInAndOutCXPersent(mapEtcOneCX,mapEtcLicenseMatch);
				String[] aEtc1=aEtc.split(",");
				etcInCxPersent=aEtc1[0];
				etcOutCxPersent=aEtc1[1];
				System.out.println("etcInCxPersent:"+etcInCxPersent);
				System.out.println("etcOutCxPersent:"+etcOutCxPersent);
			}
			mapEtcCxNotEqualLicenseNotMatch=getMapCxNotEqualLicenseNotMatch(mapEtcLicenseNotMatch,etcInCxPersent,etcOutCxPersent);
			
			mapMtcOneCX=getMapOneCXMTCOnly(mapMtcOriginal,licenseEtc);
			mapMtcTwoCX=getMapTwoCXMTCOnly(mapMtcOriginal,licenseEtc);
			mapMtcOriginal.clear();
			mapMtcLicenseMatch=getLicenseMatch(mapMtcCxNotEqualOriginal,mapMtcOneCX);
			mapMtcLicenseNotMatch=getLicenseNotMatchForMtc(mapMtcCxNotEqualOriginal,mapMtcOneCX,licenseEtc);
			mapMtcCxNotEqualOriginal.clear();
			String mtcInCxPersent="0.5";
			String mtcOutCxPersent="0.5";
			if(mapMtcLicenseMatch.size()==0){
				System.out.println("车牌相同、车型不同的所有MTC数据里的车牌，没有在车型唯一的MTC数据里出现");
			}else{
				String aMtc=getInAndOutCXPersent(mapMtcOneCX,mapMtcLicenseMatch);
				String[] aMtc1=aMtc.split(",");
				mtcInCxPersent=aMtc1[0];
				mtcOutCxPersent=aMtc1[1];
				System.out.println("mtcInCxPersent:"+mtcInCxPersent);
				System.out.println("mtcOutCxPersent:"+mtcOutCxPersent);
			}
			mapMtcCxNotEqualLicenseNotMatch=getMapCxNotEqualLicenseNotMatch(mapMtcLicenseNotMatch,mtcInCxPersent,mtcOutCxPersent);
			
			
			//综合结果
			writeOriginalMap(mapEtcOneCX,writerCxComfirm);
			writeOriginalMap(mapEtcTwoCX,writerCxComfirm);
			writeOriginalMap(mapMtcOneCX,writerCxComfirm);
			writeOriginalMap(mapMtcTwoCX,writerCxComfirm);
			writerCxComfirm.close();
			writeMap(mapEtcCxNotEqualLicenseNotMatch,writerCxNotComfirm);
			writeMap(mapMtcCxNotEqualLicenseNotMatch,writerCxNotComfirm);
			writerCxNotComfirm.close();
			
			
			int size1=mapEtcOneCX.size()+mapMtcOneCX.size()+mapEtcTwoCX.size()+mapMtcTwoCX.size();
			int size2=mapEtcLicenseNotMatch.size()+mapMtcLicenseNotMatch.size();;
			
			System.out.println("etc确认车型："+mapEtcOneCX.size());
			System.out.println("etc车型不唯一："+mapEtcTwoCX.size());
			System.out.println("etc车牌相同车型不同："+mapEtcLicenseNotMatch.size());
			System.out.println("mtc确认车型："+mapMtcOneCX.size());
			System.out.println("mtc车型不唯一："+mapMtcTwoCX.size());
			System.out.println("mtc车牌相同车型不同："+mapMtcLicenseNotMatch.size());
			System.out.println("确认车型车牌数："+size1+" 有车牌颜色："+(mapEtcOneCX.size()+mapEtcTwoCX.size())+" 无车牌颜色："+(mapMtcOneCX.size()+mapMtcTwoCX.size()));
			System.out.println("不确认车型的车牌数："+size2);
			System.out.println("总车牌数："+(size1+size2));
			float a=(float)size1/(float)(size1+size2)*100;
			DecimalFormat decimalFormat=new DecimalFormat("0.00");
			String persent=decimalFormat.format(a)+"%";
			System.out.println("匹配率："+persent);
		}catch(Exception e){
			System.out.println("读取文件出错");
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
//		cxComfirm(cxDataBase,etcComfirm,etcNotComfirm,mtcComfirm,mtcNotComfirm);//输出4个
//		cxComfirm1()//输出2个
	}
}
