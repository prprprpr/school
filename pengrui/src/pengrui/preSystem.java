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
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.log4j.Logger;

public class preSystem {
	private static String GZETCOriginal="H:/贵州一月份数据/exlistelec201701.txt";
	private static String GZMTCOriginal="H:/贵州一月份数据/exlistcash201701.txt";
	private static String enGZETCOriginal="H:/贵州一月份数据/enlistelec201701.txt";
	private static String enGZMTCOriginal="H:/贵州一月份数据/enlistcash201701.txt";
	private static String guizhouETC="H:/贵州一月份数据/GZETC.csv";
	private static String guizhouCashCorrectFlow="H:/贵州一月份数据/MTCcorrectflow.csv";
	private static String guizhouEtcCorrectFlow="H:/贵州一月份数据/ETCcorrectflow.csv";
	private static String guizhouCash="H:/贵州一月份数据/GZMTC.csv";
	private static String guizhouEnETC="H:/贵州一月份数据/入口数据/enGZETC.csv";
	private static String guizhouEnCash="H:/贵州一月份数据/入口数据/enGZMTC.csv";
	
	private static String gz201701="H:/贵州一月份数据/GZ2017-01.csv";
	
	private static String g1="H:/贵州一月份数据/mtc.csv";
	private static String gzETC="H:/贵州一月份数据/车型确认/ETC.csv";
	private static String gzMTC="H:/贵州一月份数据/车型确认/MTC.csv";
	private static String gzAll="H:/贵州一月份数据/车型确认/gzAll.csv";
	
	
	private static String errETC="H:/贵州一月份数据/出入车型不同\\errETC.csv";
	private static String errMTC="H:/贵州一月份数据/出入车型不同\\errMTC.csv";
	private static String err="H:/贵州一月份数据/出入车型不同\\err.csv";
	
	private static String repairETC="H:/贵州一月份数据/车型确认/repairETC.csv";
	private static String repairMTC="H:/贵州一月份数据/车型确认/repairMTC.csv";
	private static String repair="H:/贵州一月份数据/车型确认/repair.csv";
	
	private static String repairAll="H:/贵州一月份数据/车型确认/repairAll.csv";
	private static String repairETCDevideByDay="H:/贵州一月份数据/车型确认/修复后数据按天分/ETC数据";
	private static String repairMTCDevideByDay="H:/贵州一月份数据/车型确认/修复后数据按天分/MTC数据";
	private static String repairDevideByDay="H:/贵州一月份数据/车型确认/修复后数据按天分/全部数据";
	
	
	private static String notCheckAndOneCX="H:/贵州一月份数据/车型确认/结果文件/MTC数据车型唯一.csv";
	private static String notCheckAndTwoCX="H:/贵州一月份数据/车型确认/结果文件/MTC数据不唯一.csv";
	private static String matchAndOneCX="H:/贵州一月份数据/车型确认/结果文件/ETC数据车型唯一.csv";
	private static String matchAndTwoCX="H:/贵州一月份数据/车型确认/结果文件/ETC数据车型不唯一.csv";
	private static String etcLicenseNotMatch="H:/贵州一月份数据/车型确认/结果文件/etc车牌相同车型不同.csv";
	private static String mtcLicenseNotMatch="H:/贵州一月份数据/车型确认/结果文件/mtc车牌相同车型不同.csv";
	
	private static String etcComfirm="H:/贵州一月份数据/车型确认/结果文件2/etc车牌一个车型.csv";
	private static String etcNotComfirm="H:/贵州一月份数据/车型确认/结果文件2/etc车牌最有可能车型.csv";
	private static String mtcComfirm="H:/贵州一月份数据/车型确认/结果文件2/mtc车牌一个车型.csv";
	private static String mtcNotComfirm="H:/贵州一月份数据/车型确认/结果文件2/mtc车牌最有可能车型.csv";
	
	private static String gzEtcComfirm="H:/贵州一月份数据/预处理系统/etcComfirm.csv";	
	private static String gzMtcComfirm="H:/贵州一月份数据/预处理系统/mtcComfirm.csv";

	
	private static String etcOneCX="H:/贵州一月份数据/车型确认/结果文件2/etcOneCX.csv";
	private static String mtcOneCX="H:/贵州一月份数据/车型确认/结果文件2/mtcOneCX.csv";
	
	private static String newGz201701="H:/贵州一月份数据/newGz201701.csv";
	private static String cxDataBase="H:/贵州一月份数据/车型确认/车牌车型数据库.csv";
	
	private static String mtcPartFlow="H:/贵州一月份数据/车牌修复外部文件/MTCpartflow.csv";
	private static String od="H:/贵州一月份数据/车牌修复外部文件/OD.csv";
	private static Logger logger=Logger.getLogger(preSystem.class);
	
	private static String cq201703="G:/新建文件夹/重庆/201703";
	private static String cq201701="F:/重庆/201701";
	private static String cq201702="F:/重庆/201702";
	private static String cqCxDataBase="G:/新建文件夹/重庆/201703结果/cxDataBase.csv";

	private static String cqEtcCxComfirm="G:/新建文件夹/重庆/201703结果/车型确认/etcComfirm.csv";
	private static String cqMtcCxComfirm="G:/新建文件夹/重庆/201703结果/车型确认/mtcComfirm.csv";
	
	private static String cqOd="G:/新建文件夹/重庆/CQOD_vehclass.csv";
	private static String cqEqualPlate="G:/新建文件夹/重庆/enEqualPlate.csv";
	
	private static String cqRepair="G:/新建文件夹/重庆/201703结果/修复后数据";
	private static String cqRepair201701="F:/重庆/cq201701结果/201701修复后数据";
	private static String cqRepair201702="F:/重庆/cq201702结果/201702修复后数据";
	
	private static String cqErr="G:/新建文件夹/重庆/201703结果/出入车型不同情况\\errCq.csv";
	private static String cqErr201701="F:/重庆/cq201701结果/出入车型不同情况\\errCq201701.csv";
	private static String cqErr201702="F:/重庆/cq201702结果/出入车型不同情况\\errCq201702.csv";
	private static String errCx="G:/新建文件夹/重庆/201703结果/出入车型不同情况\\errCx.csv";
	private static String errInStation="G:/新建文件夹/重庆/201703结果/出入车型不同情况\\errInStation.csv";
	private static String errOutStation="G:/新建文件夹/重庆/201703结果/出入车型不同情况\\errOutStation.csv";
	private static String errInPerson="G:/新建文件夹/重庆/201703结果/出入车型不同情况\\errInPerson.csv";
	private static String errOutPerson="G:/新建文件夹/重庆/201703结果/出入车型不同情况\\errOutPerson.csv";
	private static String errInLane="G:/新建文件夹/重庆/201703结果/出入车型不同情况\\errInLane.csv";
	private static String errOutLane="G:/新建文件夹/重庆/201703结果/出入车型不同情况\\errOutLane.csv";
	//字符串是数字序列
	public  static boolean isNumeric(String str){  
		for (int i = str.length();--i>=0;){    
			if (!Character.isDigit(str.charAt(i))){  
				return false;  
			}  
		}  
		return true;  
	}
	//计算车牌相似度
	private static int min(int one,int two,int three){
		return (one=one<two?one:two)<three?one:three;
	}
	public static float levenshtein(String str1, String str2) {  
		// 计算两个字符串的长度。  
		int len1 = str1.length();  
		int len2 = str2.length();  
		// 建立上面说的数组，比字符长度大一个空间  
		int[][] dif = new int[len1 + 1][len2 + 1];  
		// 赋初值，步骤B。  
		for (int a = 0; a <= len1; a++) {  
			dif[a][0] = a;  
		}  
		for (int a = 0; a <= len2; a++) {  
			dif[0][a] = a;  
		}  
		// 计算两个字符是否一样，计算左上的值  
		int temp;  
		for (int i = 1; i <= len1; i++) {  
			for (int j = 1; j <= len2; j++) {  
				if (str1.charAt(i - 1) == str2.charAt(j - 1)) {  
					temp = 0;  
				}else{  
					temp = 1;  
				}  
				// 取三个值中最小的  
				dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,  dif[i - 1][j] + 1);   
			}  
		}  
		float similarity = 1 - (float) dif[len1][len2]/ Math.max(str1.length(), str2.length()); 
		return similarity;
	}
	// 车牌不规范导致的无法修复
	public  static boolean irregular(String plateen,String plateex){
		if(isNumeric(plateen)&&isNumeric(plateex)||plateen.length()<6&&(plateex.length()<6||plateex.length()>8)||plateen.length()>8&&(plateex.length()<6&&plateex.length()>8)){
			return true; 
		}else{
			return false;
		}
	}
	//识别错误1
	public static int error1(String plateen,String plateex,HashMap<String, Integer> enplate ,HashMap<String, Integer> explate,HashMap<String, Integer> odplate){
		int index=0;
		if((!explate.containsKey(plateen)&&enplate.containsKey(plateex))||(!odplate.containsKey(plateen)&&odplate.containsKey(plateex))){
			index=1;
		}
		else if((explate.containsKey(plateen)&&!enplate.containsKey(plateex))||(odplate.containsKey(plateen)&&!odplate.containsKey(plateex))){
			index=2;
		}
		else if(!explate.containsKey(plateen)&&!enplate.containsKey(plateex)){
			index=3;
		}
		return index;
	}
	 //识别错误2 
	public static int error2(String plateen,String plateex,String s1,String s2,HashMap<String, String> od){
		int index=0;
		String od_match=s1+"_"+s2;
		if(od.containsKey(plateex)&&od.containsKey(plateen)){
			String strin[]=od.get(plateen).split("\\|");
			String strout[] = od.get(plateex).split("\\|");
			int flag=0;
			int count_flag1=0;
			int count_flag2=0;
			for(int i=0;i<strin.length;i++) {
				if(strin[i].contains(od_match)){
					count_flag1++;
				}
					//System.out.println(entry.getKey()+":"+entry.getValue());
			}
			for(int i=0;i<strout.length;i++) {
				if(strout[i].contains(od_match)){
					count_flag2++;
				}
					//System.out.println(entry.getKey()+":"+entry.getValue());
			}
			if(count_flag1>count_flag2){
				index=1;
			} 
			else if(count_flag1<count_flag2){
				index=2;
			}	
		}
		return index;
	}
	//车牌不规范，识别错误
	public static int error3(String plateen , String plateex){
		int index=0;
		if(plateen.length()>6&&!isNumeric(plateen)&& plateex.length()>plateen.length()&&plateex.contains(plateen)){//出口车牌增加字符
			index=2;
		}
		else if(plateex.length()>6&&!isNumeric(plateex)&&plateen.length()>plateex.length()&&plateen.contains(plateex)){//入口车牌增加字符
			index=1;
		}
		else if(plateen.length()<6&&plateex.length()>6&&!isNumeric(plateex)&&plateex.contains(plateen)){//入口车牌删减字符
			index=1;
		}
		else if(plateex.length()<6&&plateen.length()>6&&!isNumeric(plateen)&&plateen.contains(plateex)){//出口车牌删减字符
			index=2;
		}
		else if(isNumeric(plateen)&&!isNumeric(plateex)&&plateex.length()>6){//入口车牌为数字
			index=1;
		}
		else if(isNumeric(plateex)&&!isNumeric(plateen)&&plateen.length()>6){//出口车牌为数字
			index=2;
		}else if(levenshtein(plateen,plateex)<0.6&&plateen.length()<6&&plateex.length()>6){//入口车牌为相似u的较低的字符
			index=1;
		}else if(levenshtein(plateen,plateex)<0.6&&plateex.length()<6&&plateen.length()>6){//出口车牌为相似度较低的字符
			index=2;
		}
		else if(plateen.contains("无")&&plateex.length()>6&&!isNumeric(plateex)){//把无车牌的进行修正
			index=1;
		}else if(plateex.contains("无")&&plateen.length()>6&&!isNumeric(plateen)){
			index=2;
		}
		return index;
	}
	public static String plateRevise(String str,HashMap<String, Integer> enplate ,HashMap<String, Integer> explate,HashMap<String, Integer> odplate,HashMap<String, String> od)	{
		String temp[]=null;
		String line=null;
		int count1=0;
		int count2=0;
		temp=str.split(",");
		String time=temp[14].trim();
		String plateen=temp[10].trim();
		String plateex=temp[20].trim();
		String s1=temp[2];
		String s2=temp[12];
		int i=1;
		if(!plateen.equals(plateex)){
			if(time.startsWith("1970")){
				count1++;
			}else if(error3(plateen,plateex)==1){//车牌不规范的入口车牌识别错误
				temp[10]=temp[20];
				String s=temp[28];
				temp[28]=0+s.substring(1);
				line=temp[0]+",";
			    while(i<temp.length-1 ){
			    	line=line+temp[i]+",";
			    	i++;
			    }
			    line=line+temp[temp.length-1];
			    i=1;
			}else if(error3(plateen,plateex)==2){//车牌不规范的出口车牌识别错误
				temp[20]=temp[10];
				String s=temp[28];
				temp[28]=0+s.substring(1);
				line=temp[0]+",";
				while(i<temp.length-1 ){
				    line=line+temp[i]+",";
				    i++;
			    }
			    line=line+temp[temp.length-1];
			    i=1;
			}else if(irregular(plateen,plateex)){
				String s=temp[28];
				temp[28]=1+s.substring(1);
				line=temp[0]+",";
				while(i<temp.length-1 ){
				    line=line+temp[i]+",";	
				    i++;
				}
				line=line+temp[temp.length-1];
				i=1;
			}else if(levenshtein(plateen,plateex)>0.6){
				if(isNumeric(plateen)&&isNumeric(plateex)){
					count2++;	
				}else if(error1(plateen,plateex,enplate,explate,odplate)==1){//入口车牌错误
					temp[10]=temp[20];
					String s=temp[28];
					temp[28]=0+s.substring(1);
					line=temp[0]+",";
					while(i<temp.length-1 ){
						line=line+temp[i]+",";	
					    i++;
					}
					line=line+temp[temp.length-1];
					i=1;
				}else if(error2(plateen,plateex,s1,s2,od)==1||error3(plateen,plateex)==1){//入口车牌错误
					temp[10]=temp[20];
					String s=temp[28];
					temp[28]=0+s.substring(1);
					line=temp[0]+",";
					while(i<temp.length-1 ){
					    line=line+temp[i]+",";	
					    i++;
					    }
					line=line+temp[temp.length-1];
					i=1;
				}else if(error1(plateen,plateex,enplate,explate,odplate)==2){//出口车牌错误
					temp[20]=temp[10];
					String s=temp[28];
					temp[28]=0+s.substring(1);
					line=temp[0]+",";
					while(i<temp.length-1 ){
					    line=line+temp[i]+",";	
					    i++;
				    }
				    line=line+temp[temp.length-1];
				    i=1;
				}else if(error2(plateen,plateex,s1,s2,od)==2||error3(plateen,plateex)==2){//出口车牌错误
					temp[20]=temp[10];
					String s=temp[28];
					temp[28]=0+s.substring(1);
					line=temp[0]+",";
					while(i<temp.length-1 ){
					    line=line+temp[i]+",";	
					    i++;
				    }
				    line=line+temp[temp.length-1];
				    i=1;
				}else {//相似度高，但无法确认出入口车牌
					String s=temp[28];
					temp[28]=1+s.substring(1);
					line=temp[0]+",";
					while(i<temp.length-1 ){
					    line=line+temp[i]+",";	
					    i++;
					}
				    line=line+temp[temp.length-1];
				    i=1;
				}
			}else if(levenshtein(plateen,plateex)<0.6){//非识别错误，不可修复
				String s=temp[28];
				temp[28]=2+s.substring(1);
				line=temp[0]+",";
				while(i<temp.length-1 ){
				    line=line+temp[i]+",";	
				    i++;
			    }
			    line=line+temp[temp.length-1];
			    i=1;
			}
			return line;
		}else{
			return str;
		}
	}
	
	private static void add(Map<String,Integer> map,String a){
		if(map.size()==0){
			map.put(a, 1);
		}else{
			if(map.containsKey(a)){
				map.put(a, map.get(a)+1);
			}else{
				map.put(a, 1);
			}
		}
	}
	private static void addString(Map<String,String> map,String a,String b){
		if(map.size()==0){
			map.put(a, b);
		}else{
			if(map.containsKey(a)){
				int p=Integer.parseInt(map.get(a));
				int p1=Integer.parseInt(b);
				String p2=""+(p+p1);
				map.put(a, p2);
			}else{
				map.put(a, b);
			}
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
	public static boolean isDX(String a,String b){
		int aa=Integer.parseInt(a);
		int bb=Integer.parseInt(b);
		if((aa>=10&&bb<10)||(aa<10&&bb>=10)){
			return true;
		}else{
			return false;
		}
	}
	public static boolean isSame(String a,String b,String c){
		int aa=Integer.parseInt(a);
		int bb=Integer.parseInt(b);
		int cc=Integer.parseInt(c);
		if((aa>=10&&bb>=10&&cc>=10)||(aa<=10&&bb<=10&&cc<=10)){
			return true;
		}else{
			return false;
		}
	}
	public static void presentTime(){
		Date startTime=new Date(System.currentTimeMillis());
		SimpleDateFormat sdFormatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String retStrFormatNowDate = sdFormatter.format(startTime);
		System.out.println(retStrFormatNowDate);
	}
	public static void coverMap(Map<String,Integer> map){
		Set set=map.entrySet();
		Map.Entry[] entries=(Map.Entry[])set.toArray(new Map.Entry[set.size()]);
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();
			String value=entries[i].getValue().toString();
			System.out.println(key+","+value);
		}
	}
	public static void coverMapMap(Map<String, HashMap<String,Integer>> map){
		Set set=map.entrySet();
		Map.Entry[] entries=(Map.Entry[])set.toArray(new Map.Entry[set.size()]);
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();
			Map<String,Integer> mapIn=(Map<String, Integer>) entries[i].getValue();
			Set setIn=mapIn.entrySet();
			Map.Entry[] entriesIn=(Map.Entry[])setIn.toArray(new Map.Entry[setIn.size()]);
			for(int j=0;j<entriesIn.length;j++){
				String keyIn=entriesIn[j].getKey().toString();
				String value=entriesIn[j].getValue().toString();
				System.out.println(key+","+keyIn+","+value);
			}
		}
	}
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
			int key1=Integer.parseInt(entries1[j].getKey().toString());
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
				String lessMaxCountCX=mL1[1];
				String usedMaxCX=getUsedMaxCX(map);
				String persentMax=getPersent(map,maxCountCX);
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
	public static void addDamageNumber(Map<String,Integer> map,String usedMaxCx,Map<String,String> outMap){
		Set set1=map.entrySet();
		Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
		for(int j=0;j<entries1.length;j++){
			String cx=entries1[j].getKey().toString();
			String cs=entries1[j].getValue().toString();
			if(!cx.equals(usedMaxCx)){
				addString(outMap,cx+","+usedMaxCx,cs);
			}
		}
	}
	public static void getDamageMap(Map<String, HashMap<String,Integer>> mapCXEqual,Map<String, String> outMap){
		Set set=mapCXEqual.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]);
		for(int i=0;i<entries.length;i++){
			String license=entries[i].getKey().toString();
			Map<String,Integer> map=(Map<String, Integer>) entries[i].getValue();
			if(map.size()>1){
				String usedMaxCX=getUsedMaxCX(map);
				int a=Integer.parseInt(usedMaxCX);
				if(a<=6){
					addDamageNumber(map,usedMaxCX,outMap);
				}
			}
		}
	}
	public static void getDamageMapForMtc(Map<String, HashMap<String,Integer>> mapCXEqual,Map<String, String> outMap,Set<String> licenseETC){
		Set set=mapCXEqual.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]);
		for(int i=0;i<entries.length;i++){
			String license=entries[i].getKey().toString();
			Map<String,Integer> map=(Map<String, Integer>) entries[i].getValue();
			if(map.size()>1&&!licenseETC.contains(license)){
				String usedMaxCX=getUsedMaxCX(map);
				int a=Integer.parseInt(usedMaxCX);
				if(a<=6){
					addDamageNumber(map,usedMaxCX,outMap);
				}
			}
		}
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
				String lessMaxCountCX=mL1[1];
				String usedMaxCX=getUsedMaxCX(map);
				String persentMax=getPersent(map,maxCountCX);
				String persentLessMax=getPersent(map,lessMaxCountCX);
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
	
	
	
	public static void writeOneOutCXToMap(Map<String,Integer> map,Map<String,String> outMap,String key){
		Set set1=map.entrySet();
		Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
		for(int j=0;j<entries1.length;j++){
			String chexing=entries1[j].getKey().toString();
			String cishu=entries1[j].getValue().toString();
			outMap.put(key, chexing+","+chexing);
		}
	}
	private static Map<String,String> createMap(Map<String, HashMap<String,Integer>> originalMap){
		Map<String,String> realMap=new HashMap<>();
		Set set=originalMap.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();
			Map<String,Integer> map=(Map<String, Integer>) entries[i].getValue();
			if(map.size()>1){
				String maxCountCX=getMaxCountCX(map);
				String usedMaxCX=getUsedMaxCX(map);
				String persentMax=getPersent(map,maxCountCX);
				realMap.put(key, maxCountCX+","+persentMax+","+usedMaxCX);
			}else{
				writeOneOutCXToMap(map,realMap,key);
			}
		}
		return realMap;
	}
	public static void writeOneCXToMap(Map<String,Integer> map,Map<String, HashMap<String,Integer>> outMap,String key){
		Set set1=map.entrySet();
		Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
		for(int j=0;j<entries1.length;j++){
			String chexing=entries1[j].getKey().toString();
			String cishu=entries1[j].getValue().toString();
			put(outMap,key, chexing);
		}
	}
	private static Map<String,HashMap<String,Integer>> getMapEtcNoColor(Map<String, HashMap<String,Integer>> originalMap,Map<String, HashMap<String,Integer>> mapEtcOneCX){
		Map<String,HashMap<String,Integer>> realMap=new HashMap<>();
		Set set=originalMap.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
		for(int i=0;i<entries.length;i++){
			String license=entries[i].getKey().toString();
			Map<String,Integer> map=(Map<String, Integer>) entries[i].getValue();
			boolean flag=false;
			for(int g=0;g<=5;g++){
				if(mapEtcOneCX.containsKey(license+","+g)){
					flag=true;
				}
			}
			if(!flag){
				if(map.size()>1){
					String mL=getMaxAndLessMaxCountCX(map);
					String [] mL1=mL.split(",");
					String maxCountCX=mL1[0];
					String lessMaxCountCX=mL1[1];
					String usedMaxCX=getUsedMaxCX(map);
					String persentMax=getPersent(map,maxCountCX);
					String persentLessMax=getPersent(map,lessMaxCountCX);
					
					put(realMap,license+"(ETCnoColor)",maxCountCX+","+persentMax+","+usedMaxCX);
//					put(realMap,license,usedMaxCX);//只输出一个最大车型
					
//					put(realMap,license,maxCountCX+","+persentMax+","+usedMaxCX);
//					put(realMap,license,lessMaxCountCX+","+persentLessMax+","+usedMaxCX);
				}else{
					writeOneCXToMap(map,realMap,license);
				}
			}
		}
		return realMap;
	}

	//输出两个结果
	private static void insertMapOutCX(String licenseMTC,String maxCX,String minCX,String outCX,Map<String,HashMap<String,Integer>> realMap,boolean isDX,String persentMax,String persentMin,String mCX){
		String[] a=outCX.split(",");
		String out=a[0];
		int length=a.length;
		String outMax=a[length-1];
		boolean isSame=isSame(maxCX,minCX,out);
		if(Integer.parseInt(mCX)>Integer.parseInt(outMax)){
			if(isSame){
				if(length==3){
					put(realMap,licenseMTC,out+","+a[1]+","+mCX);
				}else{
					put(realMap,licenseMTC,out+","+mCX);
				}
			}else{
				if(!isDX){
					put(realMap,licenseMTC,maxCX+","+persentMax+","+mCX);
					if(length==3){
						put(realMap,licenseMTC,out+","+a[1]+","+mCX);
					}else{
						put(realMap,licenseMTC,out+","+mCX);
					}
				}else{
					boolean isETC=isDX(minCX,out);
					if(isETC){
						put(realMap,licenseMTC,minCX+","+persentMin+","+mCX);
						if(length==3){
							put(realMap,licenseMTC,out+","+a[1]+","+mCX);
						}else{
							put(realMap,licenseMTC,out+","+mCX);
						}
					}else{
						put(realMap,licenseMTC,maxCX+","+persentMax+","+mCX);
						if(length==3){
							put(realMap,licenseMTC,out+","+a[1]+","+mCX);
						}else{
							put(realMap,licenseMTC,out+","+mCX);
						}
					}
				}
			}
		}else{
			if(isSame){
				put(realMap,licenseMTC,outCX);
			}else{
				if(!isDX){
					put(realMap,licenseMTC,maxCX+","+persentMax+","+outMax);
					put(realMap,licenseMTC,outCX);//?
				}else{
					boolean isETC=isDX(minCX,out);
					if(isETC){
						put(realMap,licenseMTC,minCX+","+persentMin+","+outMax);
						put(realMap,licenseMTC,outCX);//?
					}else{
						put(realMap,licenseMTC,maxCX+","+persentMax+","+outMax);
						put(realMap,licenseMTC,outCX);//?
					}
				}
			}
		}
	}
	private static void cxReal(String path){
		File file=new File(path);
		
		File fileOutMatchAndOneCX=new File(matchAndOneCX);
		File fileOutMatchAndTwoCX=new File(matchAndTwoCX);
		File fileOutNotCheckAndOneCX=new File(notCheckAndOneCX);
		File fileOutNotCheckAndTwoCX=new File(notCheckAndTwoCX);
		File fileEtcLicenseNotMatch=new File(etcLicenseNotMatch);
		File fileMtcLicenseNotMatch=new File(mtcLicenseNotMatch);
		
		File fileEtcComfirmOneCX=new File(etcComfirm);
		File fileEtcNotComfirm=new File(etcNotComfirm);
		File fileMtcComfirmOneCX=new File(mtcComfirm);
		File fileMtcNotComfirm=new File(mtcNotComfirm);
		
		File fileOutEtcOneCx=new File(etcOneCX);
		File fileOutMtcOneCx=new File(mtcOneCX);
		
		Map<String, HashMap<String,Integer>> mapMtcOriginal = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapMtcCxNotEqualOriginal = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapEtcOriginal = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapETCnoColorOriginal = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapEtcCxNotEqualOriginal = new HashMap<>();
		
		Map<String, HashMap<String,Integer>> mapMtcOneCX = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapMtcTwoCX = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapMtcLicenseMatch = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapMtcLicenseNotMatch = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapEtcOneCX = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapEtcTwoCX = new HashMap<>();
		Map<String,HashMap<String,Integer>> mapEtcnoColor=new HashMap<>();
		Map<String, HashMap<String,Integer>> mapEtcLicenseMatch = new HashMap<>();
		Map<String, HashMap<String,Integer>> mapEtcLicenseNotMatch = new HashMap<>();
		Map<String,String> mapEtcCxNotEqualLicenseNotMatch=new HashMap<>();
		Map<String,String> mapMtcCxNotEqualLicenseNotMatch=new HashMap<>();
		Set<String> licenseCount=new HashSet<>();//出入口车牌相同的车牌数统计
		Set<String> licenseEtc=new HashSet<>();//出入口车牌相同的车牌数统计
		Set<String> licenseEtcWithColor=new HashSet<>();//出入口车牌相同的车牌数统计
		
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
			
			
			OutputStreamWriter outMatchAndOneCX=new OutputStreamWriter(new FileOutputStream(fileOutMatchAndOneCX),encoding1);
			BufferedWriter writerMatchAndOneCX=new BufferedWriter(outMatchAndOneCX);
			OutputStreamWriter outMatchAndTwoCX=new OutputStreamWriter(new FileOutputStream(fileOutMatchAndTwoCX),encoding1);
			BufferedWriter writerMatchAndTwoCX=new BufferedWriter(outMatchAndTwoCX);
			OutputStreamWriter outNotCheckAndOneCX=new OutputStreamWriter(new FileOutputStream(fileOutNotCheckAndOneCX),encoding1);
			BufferedWriter writerNotCheckAndOneCX=new BufferedWriter(outNotCheckAndOneCX);
			OutputStreamWriter outNotCheckAndTwoCX=new OutputStreamWriter(new FileOutputStream(fileOutNotCheckAndTwoCX),encoding1);
			BufferedWriter writerNotCheckAndTwoCX=new BufferedWriter(outNotCheckAndTwoCX);
			
			OutputStreamWriter outEtcLicenseNotMatch=new OutputStreamWriter(new FileOutputStream(fileEtcLicenseNotMatch),encoding1);
			BufferedWriter writerEtcLicenseNotMatch=new BufferedWriter(outEtcLicenseNotMatch);
			OutputStreamWriter outMtcLicenseNotMatch=new OutputStreamWriter(new FileOutputStream(fileMtcLicenseNotMatch),encoding1);
			BufferedWriter writerMtcLicenseNotMatch=new BufferedWriter(outMtcLicenseNotMatch);
			
			OutputStreamWriter outEtcComfirm=new OutputStreamWriter(new FileOutputStream(fileEtcComfirmOneCX),encoding1);
			BufferedWriter writerEtcComfirm=new BufferedWriter(outEtcComfirm);
			OutputStreamWriter outEtcNotComfirm=new OutputStreamWriter(new FileOutputStream(fileEtcNotComfirm),encoding1);
			BufferedWriter writerEtcNotComfirm=new BufferedWriter(outEtcNotComfirm);
			OutputStreamWriter outMtcComfirm=new OutputStreamWriter(new FileOutputStream(fileMtcComfirmOneCX),encoding1);
			BufferedWriter writerMtcComfirm=new BufferedWriter(outMtcComfirm);
			OutputStreamWriter outMtcNotComfirm=new OutputStreamWriter(new FileOutputStream(fileMtcNotComfirm),encoding1);
			BufferedWriter writerMtcNotComfirm=new BufferedWriter(outMtcNotComfirm);
			
			OutputStreamWriter outEtcOneCx=new OutputStreamWriter(new FileOutputStream(fileOutEtcOneCx),encoding1);
			BufferedWriter writerOutEtcOneCx=new BufferedWriter(outEtcOneCx);
			OutputStreamWriter outMtcOneCx=new OutputStreamWriter(new FileOutputStream(fileOutMtcOneCx),encoding1);
			BufferedWriter writerOutMtcOneCx=new BufferedWriter(outMtcOneCx);
			
			String line="";

			while((line=reader.readLine())!=null){
				String[] data=line.trim().split(",");
				String inLicense=data[0].trim();
				String outLicense=data[1].trim();
				String inCX=data[2].trim();
				String outCX=data[3].trim();//贵州
				String licenseColor=data[4].trim();
				String isETC=data[5];
				if(isETC.equals("0")){
					if(inLicense.equals(outLicense)&&inLicense.length()>5&&inCX.equals(outCX)){
						put(mapMtcOriginal,inLicense+","+licenseColor,inCX);
					}
					if(inLicense.equals(outLicense)&&inLicense.length()>5&&!inCX.equals(outCX)){
						cxNotEqualMtc++;
						put(mapMtcCxNotEqualOriginal,inLicense+","+licenseColor,inCX+","+outCX);
					}
					if(inLicense.equals(outLicense)&&inLicense.length()>5){
						licenseCount.add(inLicense+","+licenseColor);
					}
				}else{
					int color=Integer.parseInt(licenseColor);
					if(inLicense.equals(outLicense)&&inLicense.length()>5&&inCX.equals(outCX)){
						licenseEtc.add(inLicense+","+licenseColor);
						if(color<6){
							put(mapEtcOriginal,inLicense+","+color,inCX);//选出ETC内车牌对应的车牌颜色
						}else{
							put(mapETCnoColorOriginal,inLicense,inCX);
						}
					}
					if(inLicense.equals(outLicense)&&inLicense.length()>5&&!inCX.equals(outCX)){
						cxNotEqualEtc++;
						if(color<6){
							licenseEtc.add(inLicense+","+licenseColor);
							put(mapEtcCxNotEqualOriginal,inLicense+","+color,inCX+","+outCX);
						}else{
							put(mapMtcCxNotEqualOriginal,inLicense,inCX+","+outCX);
						}
					}
					if(inLicense.equals(outLicense)&&inLicense.length()>5){
						licenseCount.add(inLicense+","+licenseColor);
					}
				}
			}
			reader.close();
			
			getDamageMap(mapEtcOriginal,damageMap);
			getDamageMapForMtc(mapMtcOriginal,damageMap,licenseEtc);
//			getDamageMap(mapMtcOriginal,damageMap);
			System.out.println(damageMap);
			
			mapEtcOneCX=getMapOneCX(mapEtcOriginal);
			mapEtcTwoCX=getMapTwoCX(mapEtcOriginal);
			mapEtcOriginal.clear();
			mapEtcnoColor=getMapEtcNoColor(mapETCnoColorOriginal,mapEtcOneCX);
			mapETCnoColorOriginal.clear();
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
			
			//细分结果
			writeOriginalMap(mapEtcOneCX,writerMatchAndOneCX);
			writerMatchAndOneCX.close();
			writeOriginalMap(mapEtcTwoCX,writerMatchAndTwoCX);
			writerMatchAndTwoCX.close();
			writeMap(mapEtcCxNotEqualLicenseNotMatch,writerEtcLicenseNotMatch);
			writerEtcLicenseNotMatch.close();
			
			writeOriginalMap(mapMtcOneCX,writerNotCheckAndOneCX);
			writeOriginalMap(mapEtcnoColor,writerNotCheckAndOneCX);
			writerNotCheckAndOneCX.close();
			writeOriginalMap(mapMtcTwoCX,writerNotCheckAndTwoCX);
			writerNotCheckAndTwoCX.close();
			writeMap(mapMtcCxNotEqualLicenseNotMatch,writerMtcLicenseNotMatch);
			writerMtcLicenseNotMatch.close();
			
			//综合结果
			writeOriginalMap(mapEtcOneCX,writerEtcComfirm);
			writeOriginalMap(mapEtcTwoCX,writerEtcComfirm);
			writerEtcComfirm.close();
			writeMap(mapEtcCxNotEqualLicenseNotMatch,writerEtcNotComfirm);
			writerEtcNotComfirm.close();
			
			writeOriginalMap(mapMtcOneCX,writerMtcComfirm);
			writeOriginalMap(mapEtcnoColor,writerMtcComfirm);
			writeOriginalMap(mapMtcTwoCX,writerMtcComfirm);
			writerMtcComfirm.close();
			writeMap(mapMtcCxNotEqualLicenseNotMatch,writerMtcNotComfirm);
			writerMtcComfirm.close();
			
			//输出两个结果
			writeOriginalMap(mapEtcOneCX,writerOutEtcOneCx);
			writeOriginalMap(mapEtcTwoCX,writerOutEtcOneCx);
			writeMap(mapEtcCxNotEqualLicenseNotMatch,writerOutEtcOneCx);
			writerOutEtcOneCx.close();
			
			writeOriginalMap(mapMtcOneCX,writerOutMtcOneCx);
			writeOriginalMap(mapMtcTwoCX,writerOutMtcOneCx);
			writeOriginalMap(mapEtcnoColor,writerOutMtcOneCx);
			writeMap(mapMtcCxNotEqualLicenseNotMatch,writerOutMtcOneCx);
			writerOutMtcOneCx.close();

			int size1=mapEtcOneCX.size()+mapMtcOneCX.size()+mapEtcTwoCX.size()+mapEtcnoColor.size()+mapMtcTwoCX.size();
			int size2=mapEtcLicenseNotMatch.size()+mapMtcLicenseNotMatch.size();;
			
			System.out.println("etc确认车型："+mapEtcOneCX.size());
			System.out.println("etc车型不唯一："+mapEtcTwoCX.size());
			System.out.println("etc车牌相同车型不同："+mapEtcLicenseNotMatch.size());
			System.out.println("etc车牌无颜色："+mapEtcnoColor.size());
			System.out.println("mtc确认车型："+mapMtcOneCX.size());
			System.out.println("mtc车型不唯一："+mapMtcTwoCX.size());
			System.out.println("mtc车牌相同车型不同："+mapMtcLicenseNotMatch.size());
			System.out.println("确认车型车牌数："+size1+" 有车牌颜色："+(mapEtcOneCX.size()+mapEtcTwoCX.size())+" 无车牌颜色："+(mapMtcOneCX.size()+mapEtcnoColor.size()+mapMtcTwoCX.size()));
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
	public static void writeOriginalMapEtc(Map<String, HashMap<String,Integer>> originalMap,BufferedWriter writer) throws IOException{
		Set set=originalMap.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();//key为车牌号
			Map<String,Integer> map=(Map<String, Integer>) entries[i].getValue();
			Set set1=map.entrySet();
			Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
			String a="";
			for(int j=0;j<entries1.length;j++){
				String key1=entries1[j].getKey().toString();
				String value=entries1[j].getValue().toString();
				if(j!=entries1.length-1){
					a+=key1+"_"+value+"|";
				}else{
					a+=key1+"_"+value;
				}
			}
			writer.write(key+","+a+","+"1"+"\n");
			writer.flush();
		}
	}
	public static void writeOriginalMapMtc(Map<String, HashMap<String,Integer>> originalMap,BufferedWriter writer) throws IOException{
		Set set=originalMap.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();//key为车牌号
			Map<String,Integer> map=(Map<String, Integer>) entries[i].getValue();
			Set set1=map.entrySet();
			Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
			String a="";
			for(int j=0;j<entries1.length;j++){
				String key1=entries1[j].getKey().toString();
				String value=entries1[j].getValue().toString();
				if(j!=entries1.length-1){
					a+=key1+"_"+value+"|";
				}else{
					a+=key1+"_"+value;
				}
			}
			writer.write(key+","+a+","+"0"+"\n");
			writer.flush();
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
		}else{
			//错误数据
			h2="错误数据";
		}
		return h2;
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
		}else{
			h2="错误数据";
		}
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

	public static String repairCxLine(String line,String realCx){
		String[] data=line.split(",");
		String inLicense=data[10].trim();
		String outLicense=data[20].trim();
		String inCX=data[8].trim();
		String outCX=data[18].trim();
		String isETC=data[27].trim();
		String licenseColor=data[26].trim();
		String id=data[28].trim();
		String result="";
		for(int i=0;i<data.length-1;i++){
			if(i<data.length-2){
				if(i==8||i==18){
					result+=realCx+",";
				}else{
					result+=data[i]+",";
				}
			}else{
				result+=data[i];
			}
		}
		String newId=id.substring(0, 1)+"0"+id.substring(2, 4);
		result+=","+newId;
		return result;
	}
	public static String repairCxLineCq(String line,String realCx){
		String[] data=line.split(",");
		String inLicense=data[10].trim();
		String outLicense=data[20].trim();
		String inCX=data[8].trim();
		String outCX=data[18].trim();
		String isETC=data[27].trim();
		String licenseColor=data[26].trim();
		String id=data[28].trim();
		if(data.length>29){
			outLicense=data[20]+","+data[21];
			licenseColor=data[27];
			isETC=data[28];
			id=data[29];
		}
		String result="";
		for(int i=0;i<data.length-1;i++){
			if(i<data.length-2){
				if(i==8||i==18){
					result+=realCx+",";
				}else{
					result+=data[i]+",";
				}
			}else{
				result+=data[i];
			}
		}
		String newId=id.substring(0, 1)+"0"+id.substring(2, 4);
		result+=","+newId;
		return result;
	}
	
	public static void preSystem(String path,String path1,String path2,String path3,String path4,String outPath,String mtcPartFlow,String OD){
		File fileEtc=new File(path);
		File fileMtc=new File(path1);
		File fileCXETC=new File(path2);
		File fileCXMTC=new File(path3);
		
		File fileMtcPartFlow=new File(mtcPartFlow);
		File fileOd=new File(OD);
		
		File fileUpdateTable=new File(path4);
		File fileOut=new File(outPath);
		
		Map<String,String> mapCx=new HashMap<>();
		Map<String,HashMap<String,Integer>> mapUpdateTableEtc=new HashMap<>();
		Map<String,HashMap<String,Integer>> mapUpdateTableMtc=new HashMap<>();
		
		HashMap<String, Integer> enplate = new HashMap<>();//所有入口车牌
		HashMap<String, Integer> explate= new HashMap<>();//所有出口车牌
		HashMap<String, Integer> odplate= new HashMap<>();//所有一对一流水的车牌
		HashMap<String, String> od= new HashMap<>();//OD信息
		
		int countErrEtc=0;
		int amountEtc=0;
		int cpNotEqualEtcBefore=0;
		int cpEqualEtc=0;
		int cpNotEqualEtc=0;
		int cpEqualCxNotEqualEtc=0;
		int cxRepairSuccessEtc=0;
		int cxRepairNotSuccessEtc=0;
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			InputStreamReader inEtc=new InputStreamReader(new FileInputStream(fileEtc),encoding1);
			BufferedReader readerEtc=new BufferedReader(inEtc);
			InputStreamReader inMtc=new InputStreamReader(new FileInputStream(fileMtc),encoding1);
			BufferedReader readerMtc=new BufferedReader(inMtc);
			
			InputStreamReader inCXETC=new InputStreamReader(new FileInputStream(fileCXETC),encoding1);
			BufferedReader readerCXETC=new BufferedReader(inCXETC);
			InputStreamReader inCXMTC=new InputStreamReader(new FileInputStream(fileCXMTC),encoding1);
			BufferedReader readerCXMTC=new BufferedReader(inCXMTC);
			
			InputStreamReader inMtcPartFlow=new InputStreamReader(new FileInputStream(fileMtcPartFlow),encoding);
			BufferedReader readerMtcPartFlow=new BufferedReader(inMtcPartFlow);
			InputStreamReader inOd=new InputStreamReader(new FileInputStream(fileOd),encoding);
			BufferedReader readerOd=new BufferedReader(inOd);
			
			InputStreamReader inUpdate=new InputStreamReader(new FileInputStream(fileUpdateTable),encoding);
			BufferedReader readerUpdate=new BufferedReader(inUpdate);
			
			OutputStreamWriter write=new OutputStreamWriter(new FileOutputStream(fileOut),encoding);
			BufferedWriter writer=new BufferedWriter(write);
			
			String lineMtcPartFlow="";
			while((lineMtcPartFlow=readerMtcPartFlow.readLine())!=null){
				String[] data=lineMtcPartFlow.split(",");
				String inLicense=data[0].trim();
				String outLicense=data[1].trim();
				add(enplate,inLicense);
				add(explate,outLicense);
			}
			readerMtcPartFlow.close();
			
			String lineOd="";
			while((lineOd=readerOd.readLine())!=null){
				String[] data=lineOd.split(",");
				String plate=data[0].trim();
				String odinfo=data[1].trim();//cq的改为2
				if(!od.containsKey(plate)){
					od.put(plate, odinfo);
				}
				if(!odplate.containsKey(plate)){
					odplate.put(plate, 1);
				}
			}
			readerOd.close();
			
			String lineCXMTC="";
			while((lineCXMTC=readerCXMTC.readLine())!=null){
				String[] data=lineCXMTC.split(",");
				String license=data[0];
				String color=data[1];
				String cx=data[2];
				mapCx.put(license+","+color, cx);
			}
			readerCXMTC.close();
			
			String lineCXETC="";
			while((lineCXETC=readerCXETC.readLine())!=null){
				String[] data=lineCXETC.split(",");
				String license=data[0];
				String color=data[1];
				String cx=data[2];
				mapCx.put(license+","+color, cx);
			}
			readerCXETC.close();
			
			String lineUpdate="";
			while((lineUpdate=readerUpdate.readLine())!=null){
				String[] data=lineUpdate.split(",");
				String license=data[0];
				String licenseColor=data[1];
				String cxAndCs=data[2];
				String isETC=data[3];
				String[] a=cxAndCs.split("\\|");
				for(int i=0;i<a.length;i++){
					String[] b=a[i].split("_");
					String cx=b[0];
					String cs=b[1];
					int cishu=Integer.parseInt(cs);
					if(isETC.equals("0")){
						put(mapUpdateTableMtc,license+","+licenseColor,cx,cishu);
					}else{
						put(mapUpdateTableEtc,license+","+licenseColor,cx,cishu);
					}
				}
			}
			readerUpdate.close();
			
			File fileOutUpdateTable=new File(path4);
			OutputStreamWriter writeUpdateTable=new OutputStreamWriter(new FileOutputStream(fileOutUpdateTable),encoding);
			BufferedWriter writerUpdateTable=new BufferedWriter(writeUpdateTable);
			
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
					String newLine=getEtcLine(lineEtc,aEtc);
					String resultLine="";
					if(newLine.equals("错误数据")){
						countErrEtc++;
					}else{
						String[] newData=newLine.split(",");
						String inLicense=newData[10];
						String outLicense=newData[20];
						String inCX=newData[8];
						String outCX=newData[18];
						String licenseColor=newData[26];
						if(!inLicense.equals(outLicense)){
							cpNotEqualEtcBefore++;
							newLine=plateRevise(newLine,enplate ,explate,odplate,od);
							String[] newData1=newLine.split(",");
							inLicense=newData1[10];
							outLicense=newData1[20];
							inCX=newData1[8];
							outCX=newData1[18];
							licenseColor=newData1[26];
						}
						if(inLicense.equals(outLicense)&&inLicense.length()>6){
							cpEqualEtc++;
							if(!inCX.equals(outCX)){
								put(mapUpdateTableEtc,inLicense+","+licenseColor,inCX+"/"+outCX,1);
								cpEqualCxNotEqualEtc++;
								if(mapCx.containsKey(inLicense+","+licenseColor)){
									String realCx=mapCx.get(inLicense+","+licenseColor);
									newLine=repairCxLine(newLine,realCx);
									cxRepairSuccessEtc++;
								}else{
									cxRepairNotSuccessEtc++;
								}
							}else{
								put(mapUpdateTableEtc,inLicense+","+licenseColor,inCX,1);
							}
						}else{
							cpNotEqualEtc++;
						}
					}
					resultLine=newLine;
					writer.write(resultLine+"\n");
					writer.flush();
				}
				countEtc++;
			}
			amountEtc=countEtc-1;
			readerEtc.close();
			System.out.println("etc finish!");
			
			
			int countErrMtc=0;
			int amountMtc=0;
			int cpEqualMtc=0;
			int cpNotEqualMtcBefore=0;
			int cpNotEqualMtc=0;
			int cpEqualCxNotEqualMtc=0;
			int cxRepairSuccessMtc=0;
			int cxRepairNotSuccessMtc=0;
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
					String newLine=getMtcLine(lineMtc,aMtc);
					String resultLine="";
					if(newLine.equals("错误数据")){
						countErrMtc++;
					}else{
						String[] newData=newLine.split(",");
						String inLicense=newData[10];
						String outLicense=newData[20];
						String inCX=newData[8];
						String outCX=newData[18];
						String licenseColor=newData[26];
						if(!inLicense.equals(outLicense)){
							cpNotEqualMtcBefore++;
							newLine=plateRevise(newLine,enplate ,explate,odplate,od);
							String[] newData1=newLine.split(",");
							inLicense=newData1[10];
							outLicense=newData1[20];
							inCX=newData1[8];
							outCX=newData1[18];
							licenseColor=newData1[26];
						}
						if(inLicense.equals(outLicense)&&inLicense.length()>6){
							cpEqualMtc++;
							if(!inCX.equals(outCX)){
								put(mapUpdateTableMtc,inLicense+","+licenseColor,inCX+"/"+outCX,1);
								cpEqualCxNotEqualMtc++;
								if(mapCx.containsKey(inLicense+","+licenseColor)){
									String realCx=mapCx.get(inLicense+","+licenseColor);
									newLine=repairCxLine(newLine,realCx);
									cxRepairSuccessMtc++;
								}else{
									cxRepairNotSuccessMtc++;
								}
							}else{
								put(mapUpdateTableMtc,inLicense+","+licenseColor,inCX,1);
							}
						}else{
							cpNotEqualMtc++;
						}
					}
					resultLine=newLine;
					writer.write(resultLine+"\n");
					writer.flush();
				}
				countMtc++;
			}
			readerMtc.close();
			writer.close();
			mapCx.clear();
			enplate.clear();
			explate.clear();
			odplate.clear();
			od.clear();
			amountMtc=countMtc-1;
			writeOriginalMapEtc(mapUpdateTableEtc,writerUpdateTable);
			writeOriginalMapMtc(mapUpdateTableMtc,writerUpdateTable);
			writerUpdateTable.close();
			System.out.println("ETC错误数据条数："+countErrEtc);
			System.out.println("ETC总数据条数："+amountEtc);
			System.out.println("车牌修复前，ETC出入车牌不相同数据条数："+cpNotEqualEtcBefore);
			System.out.println("车牌修复后，ETC出入车牌相同数据条数："+cpEqualEtc);
			System.out.println("车牌修复后，ETC出入车牌不同数据条数："+cpNotEqualEtc);
			System.out.println("车型修复前，ETC出入车牌相同、车型不同数据条数："+cpEqualCxNotEqualEtc);
			System.out.println("ETC出入车牌相同、车型不同数据修复失败条数："+cxRepairNotSuccessEtc);
			System.out.println("ETC出入车牌相同、车型不同数据修复成功条数："+cxRepairSuccessEtc);
			System.out.println();
			System.out.println("MTC错误数据条数："+countErrMtc);
			System.out.println("MTC总数据条数："+amountMtc);
			System.out.println("车牌修复前，MTC出入车牌不相同数据条数："+cpNotEqualMtcBefore);
			System.out.println("车牌修复后，MTC出入车牌相同数据条数："+cpEqualMtc);
			System.out.println("车牌修复后，MTC出入车牌不同数据条数："+cpNotEqualMtc);
			System.out.println("车型修复前，MTC出入车牌相同、车型不同数据条数："+cpEqualCxNotEqualMtc);
			System.out.println("MTC出入车牌相同、车型不同数据修复失败条数："+cxRepairNotSuccessMtc);
			System.out.println("MTC出入车牌相同、车型不同数据修复成功条数："+cxRepairSuccessMtc);
			logger.info("ETC错误数据条数："+countErrEtc);
			logger.info("ETC总数据条数："+amountEtc);
			logger.info("车牌修复前，ETC出入车牌不相同数据条数："+cpNotEqualEtcBefore);
			logger.info("车牌修复后，ETC出入车牌相同数据条数："+cpEqualEtc);
			logger.info("车牌修复后，ETC出入车牌不同数据条数："+cpNotEqualEtc);
			logger.info("车型修复前，ETC出入车牌相同、车型不同数据条数："+cpEqualCxNotEqualEtc);
			logger.info("ETC出入车牌相同、车型不同数据修复失败条数："+cxRepairNotSuccessEtc);
			logger.info("ETC出入车牌相同、车型不同数据修复成功条数："+cxRepairSuccessEtc);
			
			logger.info("MTC错误数据条数："+countErrMtc);
			logger.info("MTC总数据条数："+amountMtc);
			logger.info("车牌修复前，MTC出入车牌不相同数据条数："+cpNotEqualMtcBefore);
			logger.info("车牌修复后，MTC出入车牌相同数据条数："+cpEqualMtc);
			logger.info("车牌修复后，MTC出入车牌不同数据条数："+cpNotEqualMtc);
			logger.info("车型修复前，MTC出入车牌相同、车型不同数据条数："+cpEqualCxNotEqualMtc);
			logger.info("MTC出入车牌相同、车型不同数据修复失败条数："+cxRepairNotSuccessMtc);
			logger.info("MTC出入车牌相同、车型不同数据修复成功条数："+cxRepairSuccessMtc);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("读取文件出错:",e);
		}
	}
	private static void cxComfirm(String path){
		File file=new File(path);
		
		File fileEtcComfirmOneCX=new File("G:/新建文件夹/重庆/201703结果/车型确认/etcComfirm.csv");
		File fileEtcNotComfirm=new File("G:/新建文件夹/重庆/201703结果/车型确认/etcNotComfirm.csv");
		File fileMtcComfirmOneCX=new File("G:/新建文件夹/重庆/201703结果/车型确认/mtcComfirm.csv");
		File fileMtcNotComfirm=new File("G:/新建文件夹/重庆/201703结果/车型确认/mtcNotComfirm.csv");

		
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
				String cxAndCx=data[2];
				String isETC=data[3];
				
				String[] a=cxAndCx.split("\\|");
				for(int i=0;i<a.length;i++){
					String b=a[i];
					String[] c=b.split("_",2);
					String cx=c[0];
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
			getDamageMap(mapEtcOriginal,damageMap);
			getDamageMapForMtc(mapMtcOriginal,damageMap,licenseEtc);
//			getDamageMap(mapMtcOriginal,damageMap);
			System.out.println(damageMap);
			
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
			logger.info("etc确认车型："+mapEtcOneCX.size());
			logger.info("etc车型不唯一："+mapEtcTwoCX.size());
			logger.info("etc车牌相同车型不同："+mapEtcLicenseNotMatch.size());
			logger.info("mtc确认车型："+mapMtcOneCX.size());
			logger.info("mtc车型不唯一："+mapMtcTwoCX.size());
			logger.info("mtc车牌相同车型不同："+mapMtcLicenseNotMatch.size());
			logger.info("确认车型车牌数："+size1+" 有车牌颜色："+(mapEtcOneCX.size()+mapEtcTwoCX.size())+" 无车牌颜色："+(mapMtcOneCX.size()+mapMtcTwoCX.size()));
			logger.info("不确认车型的车牌数："+size2);
			logger.info("总车牌数："+(size1+size2));
			float a=(float)size1/(float)(size1+size2)*100;
			DecimalFormat decimalFormat=new DecimalFormat("0.00");
			String persent=decimalFormat.format(a)+"%";
			System.out.println("匹配率："+persent);
			logger.info("匹配率："+persent);
		}catch(Exception e){
			System.out.println("读取文件出错");
			e.printStackTrace();
		}
	}
	public static void preSystemCq(String path,String outPath) throws IOException{
		File file=new File(path);
		List<String> list=Arrays.asList(file.list());
		
		Map<String,HashMap<String,Integer>> mapUpdateTableEtc=new HashMap<>();
		Map<String,HashMap<String,Integer>> mapUpdateTableMtc=new HashMap<>();
		
		int countErrEtc=0;
		String encoding="UTF-8";
		String encoding1="GBK";
		File fileOutUpdateTable=new File(outPath);
		
		InputStreamReader inUpdate=new InputStreamReader(new FileInputStream(fileOutUpdateTable),encoding);
		BufferedReader readerUpdate=new BufferedReader(inUpdate);
		String lineUpdate="";
		while((lineUpdate=readerUpdate.readLine())!=null){
			String[] data=lineUpdate.split(",");
			String license=data[0];
			String licenseColor=data[1];
			String cxAndCs=data[2];
			String isETC=data[3];
			String[] a=cxAndCs.split("\\|");
			for(int i=0;i<a.length;i++){
				String[] b=a[i].split("_");
				String cx=b[0];
				String cs=b[1];
				int cishu=Integer.parseInt(cs);
				if(isETC.equals("0")){
					put(mapUpdateTableMtc,license+","+licenseColor,cx,cishu);
				}else{
					put(mapUpdateTableEtc,license+","+licenseColor,cx,cishu);
				}
			}
		}
		readerUpdate.close();
		
		OutputStreamWriter writeUpdateTable=new OutputStreamWriter(new FileOutputStream(fileOutUpdateTable),encoding);
		BufferedWriter writerUpdateTable=new BufferedWriter(writeUpdateTable);
		
		for(int i=0;i<list.size();i++){
			File file2=new File(path+"/"+list.get(i));
//			System.out.println(path+"/"+list.get(i));
			try{
				InputStreamReader in=new InputStreamReader(new FileInputStream(file2),encoding1);
				BufferedReader reader=new BufferedReader(in);
				String line="";
				while((line=reader.readLine())!=null){
					String newLine=getLineCQ(line);
//					System.out.println(newLine);
					if(newLine.equals("错误数据")){
						countErrEtc++;
					}else{
						String[] newData=newLine.split(",");
						String inLicense=newData[10];
						String outLicense=newData[20];
						String inCX=newData[8];
						String outCX=newData[18];
						String licenseColor=newData[26];
						String isETC=newData[27];
						if(isETC.equals("0")){
							if(inLicense.equals(outLicense)&&inLicense.length()>6){
								if(!inCX.equals(outCX)){
									put(mapUpdateTableMtc,inLicense+","+licenseColor,inCX+"/"+outCX,1);
								}else{
									put(mapUpdateTableMtc,inLicense+","+licenseColor,inCX,1);
								}
							}
						}else{
							if(inLicense.equals(outLicense)&&inLicense.length()>6){
								if(!inCX.equals(outCX)){
									put(mapUpdateTableEtc,inLicense+","+licenseColor,inCX+"/"+outCX,1);
								}else{
									put(mapUpdateTableEtc,inLicense+","+licenseColor,inCX,1);
								}
							}
						}
					}
				}
				reader.close();
				System.out.println("read "+path+"/"+list.get(i)+" finish!");
			}catch(Exception e){
				System.out.println("读取文件出错");
				e.printStackTrace();
			}
		}
		writeOriginalMapEtc(mapUpdateTableEtc,writerUpdateTable);
		writeOriginalMapMtc(mapUpdateTableMtc,writerUpdateTable);
		writerUpdateTable.close();
	}
	public static void writeErrCq(String path,String outPath) throws IOException{
		File file=new File(path);
		List<String> list=Arrays.asList(file.list());
		
		Map<String,HashMap<String,Integer>> mapUpdateTableEtc=new HashMap<>();
		Map<String,HashMap<String,Integer>> mapUpdateTableMtc=new HashMap<>();
		
		int countErrEtc=0;
		String encoding="UTF-8";
		String encoding1="GBK";
		
		File fileOutErr=new File(outPath);
		OutputStreamWriter writeErr=new OutputStreamWriter(new FileOutputStream(fileOutErr),encoding);
		BufferedWriter writerErr=new BufferedWriter(writeErr);
		
		for(int i=0;i<list.size();i++){
			File file2=new File(path+"/"+list.get(i));
//			System.out.println(path+"/"+list.get(i));
			try{
				InputStreamReader in=new InputStreamReader(new FileInputStream(file2),encoding1);
				BufferedReader reader=new BufferedReader(in);
				String line="";
				while((line=reader.readLine())!=null){
					String newLine=getLineCQ(line);
//					System.out.println(newLine);
					if(newLine.equals("错误数据")){
						countErrEtc++;
					}else{
						String[] newData=newLine.split(",");
						String inLicense=newData[10];
						String outLicense=newData[20];
						String inCX=newData[8];
						String outCX=newData[18];
						String licenseColor=newData[26];
						if(inLicense.equals(outLicense)&&inLicense.length()>6&&!inCX.equals(outCX)){
							writerErr.write(newLine+"\n");
							writerErr.flush();
						}
					}
				}
				reader.close();
				System.out.println("read "+path+"/"+list.get(i)+" finish!");
			}catch(Exception e){
				System.out.println("读取文件出错");
				e.printStackTrace();
			}
		}
		writerErr.close();
	}
	
	public static void preSystemCq1(String path,String pathCxEtc,String pathCxMtc,String mtcPartFlow,String OD,String outPath,String newErrPath) throws IOException{
		File file=new File(path);
		File fileCXETC=new File(pathCxEtc);
		File fileCXMTC=new File(pathCxMtc);
		
		File fileMtcPartFlow=new File(mtcPartFlow);
		File fileOd=new File(OD);
		
		File fileOutErr=new File(newErrPath);
		
		Map<String,String> mapCx=new HashMap<>();
//		Map<String,HashMap<String,Integer>> mapUpdateTableEtc=new HashMap<>();
//		Map<String,HashMap<String,Integer>> mapUpdateTableMtc=new HashMap<>();
		
		HashMap<String, Integer> enplate = new HashMap<>();//所有入口车牌
		HashMap<String, Integer> explate= new HashMap<>();//所有出口车牌
		HashMap<String, Integer> odplate= new HashMap<>();//所有一对一流水的车牌
		HashMap<String, String> od= new HashMap<>();//OD信息
		int countErr=0;
		int amountEtc=0;
		int cpNotEqualEtcBefore=0;
		int cpEqualEtc=0;
		int cpNotEqualEtc=0;
		int cpEqualCxNotEqualEtc=0;
		int cxRepairSuccessEtc=0;
		int cxRepairNotSuccessEtc=0;
		int countEtc=0;
		
		int amountMtc=0;
		int cpEqualMtc=0;
		int cpNotEqualMtcBefore=0;
		int cpNotEqualMtc=0;
		int cpEqualCxNotEqualMtc=0;
		int cxRepairSuccessMtc=0;
		int cxRepairNotSuccessMtc=0;
		int countMtc=0;
		
		int cxNotEqual=0;
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			
			InputStreamReader inCXETC=new InputStreamReader(new FileInputStream(fileCXETC),encoding1);
			BufferedReader readerCXETC=new BufferedReader(inCXETC);
			InputStreamReader inCXMTC=new InputStreamReader(new FileInputStream(fileCXMTC),encoding1);
			BufferedReader readerCXMTC=new BufferedReader(inCXMTC);
			
			InputStreamReader inMtcPartFlow=new InputStreamReader(new FileInputStream(fileMtcPartFlow),encoding);
			BufferedReader readerMtcPartFlow=new BufferedReader(inMtcPartFlow);
			InputStreamReader inOd=new InputStreamReader(new FileInputStream(fileOd),encoding);
			BufferedReader readerOd=new BufferedReader(inOd);
			
			OutputStreamWriter writeErr=new OutputStreamWriter(new FileOutputStream(fileOutErr),encoding);
			BufferedWriter writerErr=new BufferedWriter(writeErr);
			
			String lineMtcPartFlow="";
			while((lineMtcPartFlow=readerMtcPartFlow.readLine())!=null){
				String[] data=lineMtcPartFlow.split(",",2);
				String inLicense=data[0].trim();
				String outLicense=data[1].trim();
				add(enplate,inLicense);
				add(explate,outLicense);
			}
			readerMtcPartFlow.close();
			
			String lineOd="";
			while((lineOd=readerOd.readLine())!=null){
				String[] data=lineOd.split(",");
				String plate=data[0].trim();
				String odinfo=data[1].trim();//cq的改为2
				if(!od.containsKey(plate)){
					od.put(plate, odinfo);
				}
				if(!odplate.containsKey(plate)){
					odplate.put(plate, 1);
				}
			}
			readerOd.close();
			
			String lineCXMTC="";
			while((lineCXMTC=readerCXMTC.readLine())!=null){
				String[] data=lineCXMTC.split(",");
				String license=data[0];
				String color=data[1];
				String cx=data[2];
				mapCx.put(license+","+color, cx);
			}
			readerCXMTC.close();
			
			String lineCXETC="";
			while((lineCXETC=readerCXETC.readLine())!=null){
				String[] data=lineCXETC.split(",");
				String license=data[0];
				String color=data[1];
				String cx=data[2];
				mapCx.put(license+","+color, cx);
			}
			readerCXETC.close();
			
			List<String> list=Arrays.asList(file.list());
			for(int i=0;i<list.size();i++){
				File file2=new File(path+"/"+list.get(i));
				InputStreamReader in=new InputStreamReader(new FileInputStream(file2),encoding1);
				BufferedReader reader=new BufferedReader(in);
				File fileOut=new File(outPath+"/"+list.get(i));
				OutputStreamWriter write=new OutputStreamWriter(new FileOutputStream(fileOut),encoding);
				BufferedWriter writer=new BufferedWriter(write);
				String line="";
				while((line=reader.readLine())!=null){
					String newLine=getLineCQ(line);
					String resultLine="";
					if(newLine.equals("错误数据")){
						countErr++;
					}else{
						String[] newData=newLine.split(",");
						String inLicense=newData[10];
						String outLicense=newData[20];
						String inCX=newData[8];
						String outCX=newData[18];
						String licenseColor=newData[26];
						String isETC=newData[27];
						if(!inCX.equals(outCX)){
							cxNotEqual++;
						}
						if(newData.length>29){
							outLicense=newData[20]+","+newData[21];
							licenseColor=newData[27];
							isETC=newData[28];
						}
						if(isETC.equals("1")){
							if(!inLicense.equals(outLicense)){
								cpNotEqualEtcBefore++;
								newLine=plateRevise(newLine,enplate ,explate,odplate,od);
								String[] newData1=newLine.split(",");
								inLicense=newData1[10];
								outLicense=newData1[20];
								inCX=newData1[8];
								outCX=newData1[18];
								licenseColor=newData1[26];
							}
							if(inLicense.equals(outLicense)&&inLicense.length()>6){
								cpEqualEtc++;
								if(!inCX.equals(outCX)){
									writerErr.write(newLine+"\n");
									writerErr.flush();
//									put(mapUpdateTableEtc,inLicense+","+licenseColor,inCX+"/"+outCX,1);
									cpEqualCxNotEqualEtc++;
									if(mapCx.containsKey(inLicense+","+licenseColor)){
										String realCx=mapCx.get(inLicense+","+licenseColor);
										newLine=repairCxLineCq(newLine,realCx);
										cxRepairSuccessEtc++;
									}else{
										cxRepairNotSuccessEtc++;
									}
								}else{
//									put(mapUpdateTableEtc,inLicense+","+licenseColor,inCX,1);
								}
							}else{
								cpNotEqualEtc++;
							}
							countEtc++;
						}else{
							if(!inLicense.equals(outLicense)){
								cpNotEqualMtcBefore++;
								newLine=plateRevise(newLine,enplate ,explate,odplate,od);
								String[] newData1=newLine.split(",");
								inLicense=newData1[10];
								outLicense=newData1[20];
								inCX=newData1[8];
								outCX=newData1[18];
								licenseColor=newData1[26];
							}
							if(inLicense.equals(outLicense)&&inLicense.length()>6){
								cpEqualMtc++;
								if(!inCX.equals(outCX)){
									writerErr.write(newLine+"\n");
									writerErr.flush();
//									put(mapUpdateTableMtc,inLicense+","+licenseColor,inCX+"/"+outCX,1);
									cpEqualCxNotEqualMtc++;
									if(mapCx.containsKey(inLicense+","+licenseColor)){
										String realCx=mapCx.get(inLicense+","+licenseColor);
										newLine=repairCxLineCq(newLine,realCx);
										cxRepairSuccessMtc++;
									}else{
										cxRepairNotSuccessMtc++;
									}
								}else{
//									put(mapUpdateTableMtc,inLicense+","+licenseColor,inCX,1);
								}
							}else{
								cpNotEqualMtc++;
							}
							countMtc++;
						}
					}
					resultLine=newLine;
					writer.write(resultLine+"\n");
					writer.flush();
				}
				reader.close();
				writer.close();
				System.out.println("repair "+path+"/"+list.get(i)+" finish");
			}
			writerErr.close();
			mapCx.clear();
			enplate.clear();
			explate.clear();
			odplate.clear();
			od.clear();
			amountEtc=countEtc;
			amountMtc=countMtc;
			System.out.println("车型不同条数："+cxNotEqual);
			System.out.println("总错误数据条数："+countErr);
			System.out.println("ETC总数据条数："+amountEtc);
			System.out.println("车牌修复前，ETC出入车牌不相同数据条数："+cpNotEqualEtcBefore);
			System.out.println("车牌修复后，ETC出入车牌相同数据条数："+cpEqualEtc);
			System.out.println("车牌修复后，ETC出入车牌不同数据条数："+cpNotEqualEtc);
			System.out.println("车型修复前，ETC出入车牌相同、车型不同数据条数："+cpEqualCxNotEqualEtc);
			System.out.println("ETC出入车牌相同、车型不同数据修复失败条数："+cxRepairNotSuccessEtc);
			System.out.println("ETC出入车牌相同、车型不同数据修复成功条数："+cxRepairSuccessEtc);
			System.out.println();
			
			System.out.println("MTC总数据条数："+amountMtc);
			System.out.println("车牌修复前，MTC出入车牌不相同数据条数："+cpNotEqualMtcBefore);
			System.out.println("车牌修复后，MTC出入车牌相同数据条数："+cpEqualMtc);
			System.out.println("车牌修复后，MTC出入车牌不同数据条数："+cpNotEqualMtc);
			System.out.println("车型修复前，MTC出入车牌相同、车型不同数据条数："+cpEqualCxNotEqualMtc);
			System.out.println("MTC出入车牌相同、车型不同数据修复失败条数："+cxRepairNotSuccessMtc);
			System.out.println("MTC出入车牌相同、车型不同数据修复成功条数："+cxRepairSuccessMtc);
			logger.info("车型不同条数："+cxNotEqual);
			logger.info("总错误数据条数："+countErr);
			logger.info("ETC总数据条数："+amountEtc);
			logger.info("车牌修复前，ETC出入车牌不相同数据条数："+cpNotEqualEtcBefore);
			logger.info("车牌修复后，ETC出入车牌相同数据条数："+cpEqualEtc);
			logger.info("车牌修复后，ETC出入车牌不同数据条数："+cpNotEqualEtc);
			logger.info("车型修复前，ETC出入车牌相同、车型不同数据条数："+cpEqualCxNotEqualEtc);
			logger.info("ETC出入车牌相同、车型不同数据修复失败条数："+cxRepairNotSuccessEtc);
			logger.info("ETC出入车牌相同、车型不同数据修复成功条数："+cxRepairSuccessEtc);
			
			logger.info("MTC总数据条数："+amountMtc);
			logger.info("车牌修复前，MTC出入车牌不相同数据条数："+cpNotEqualMtcBefore);
			logger.info("车牌修复后，MTC出入车牌相同数据条数："+cpEqualMtc);
			logger.info("车牌修复后，MTC出入车牌不同数据条数："+cpNotEqualMtc);
			logger.info("车型修复前，MTC出入车牌相同、车型不同数据条数："+cpEqualCxNotEqualMtc);
			logger.info("MTC出入车牌相同、车型不同数据修复失败条数："+cxRepairNotSuccessMtc);
			logger.info("MTC出入车牌相同、车型不同数据修复成功条数："+cxRepairSuccessMtc);
		}catch(Exception e){
			logger.error("读取文件出错:",e);
			e.printStackTrace();
		}
	}
	public static LinkedMap<String,Integer> getMaxCountStation(Map<String,Integer> map){
		LinkedMap<String,Integer> outMap=new LinkedMap<>();
		Set set1=map.entrySet();
		Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
		int[] a=new int[entries1.length];
		for(int j=0;j<entries1.length;j++){
			int value=(int) entries1[j].getValue();
			a[j]=value;
		}
		insertSort(a);
		int length=a.length;
		System.out.println(length);
		for(int i=length-1;i>length-11;i--){
			for(int k=0;k<entries1.length;k++){
				if(map.get(entries1[k].getKey())==a[i]){
					outMap.put(entries1[k].getKey().toString(), a[i]);
				}
			}
		}
		return outMap;
	}
	public static void writeIntegerMap1(Map<String,Integer> map,BufferedWriter writer)throws IOException{
		int count=0;
		Set set=map.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();
			String value=entries[i].getValue().toString();
			count+=Integer.parseInt(value);
		}
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();
			String value=entries[i].getValue().toString();
			int a=Integer.parseInt(value);
			float p=(float)a/(float)count*100;
			DecimalFormat decimalFormat=new DecimalFormat(".00");
			String persent=decimalFormat.format(p)+"%";
			writer.write(key+","+value+","+persent+"\n");
			writer.flush();
		}
		writer.close();
	}
	public static String getMaxCount(Map<String,Integer> map){
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
		String maxCountCX=null;
		for(int k=0;k<entries1.length;k++){
			if(map.get(entries1[k].getKey())==max){
				maxCountCX=entries1[k].getKey().toString();
			}
		}
		return maxCountCX+","+max;
	}
	
	public static void writeIntegerMap2(Map<String,HashMap<String,Integer>> map,Map<String,Integer> mapStation,BufferedWriter writer)throws IOException{
		int count=0;
		Set set=mapStation.entrySet();
		Map.Entry[] entries=(Map.Entry[])set.toArray(new Map.Entry[set.size()]);
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();
			Map<String,Integer> mapIn=map.get(key);
			String result=getMaxCount(mapIn);
			String[] a=result.split(",");
			int b=Integer.parseInt(a[1]);
			float p=(float)b/(float)mapStation.get(key)*100;
			DecimalFormat decimalFormat=new DecimalFormat(".00");
			String persent=decimalFormat.format(p)+"%";
			writer.write(key+","+result+","+persent+"\n");
			writer.flush();
		}
		writer.close();
	}
	private static void errCxAndStation(String err,String outErrCx,String outErrInStation,String outErrOutStation,String InPerson,String OutPerson,String InLane,String OutLane){
		File fileErr =new File(err);
		File fileOutErrCx=new File(outErrCx);
		File fileOutErrInStation=new File(outErrInStation);
		File fileOutErrOutStation=new File(outErrOutStation);
//		File fileOutErrInPerson=new File(InPerson);
		File fileOutErrOutPerson=new File(OutPerson);
//		File fileOutErrInLane=new File(InLane);
		File fileOutErrOutLane=new File(OutLane);
		
		Map<String,Integer> mapCx=new HashMap<>();
		Map<String,Integer> mapInStation=new HashMap<>();
		Map<String,Integer> mapInStation1=new HashMap<>();
		Map<String,Integer> mapOutStation=new HashMap<>();
		Map<String,Integer> mapOutStation1=new HashMap<>();
		
//		Map<String,HashMap<String,Integer>> mapInPerson=new HashMap<>();
		Map<String,HashMap<String,Integer>> mapOutPerson=new HashMap<>();
//		Map<String,HashMap<String,Integer>> mapInLane=new HashMap<>();
		Map<String,HashMap<String,Integer>> mapOutLane=new HashMap<>();
		int count=0;
		int count1=0;
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			InputStreamReader inErr=new InputStreamReader(new FileInputStream(fileErr),encoding1);
			BufferedReader readerErr=new BufferedReader(inErr);
			
			OutputStreamWriter errCx=new OutputStreamWriter(new FileOutputStream(fileOutErrCx),encoding);
			BufferedWriter writerErrCx=new BufferedWriter(errCx);
			
			OutputStreamWriter errInStation=new OutputStreamWriter(new FileOutputStream(fileOutErrInStation),encoding);
			BufferedWriter writerErrInStation=new BufferedWriter(errInStation);
			OutputStreamWriter errOutStation=new OutputStreamWriter(new FileOutputStream(fileOutErrOutStation),encoding);
			BufferedWriter writerErrOutStation=new BufferedWriter(errOutStation);
			
			OutputStreamWriter errOutPerson=new OutputStreamWriter(new FileOutputStream(fileOutErrOutPerson),encoding);
			BufferedWriter writerErrOutPerson=new BufferedWriter(errOutPerson);
			
//			OutputStreamWriter errInLane=new OutputStreamWriter(new FileOutputStream(fileOutErrInLane),encoding);
//			BufferedWriter writerErrInLane=new BufferedWriter(errInLane);
			OutputStreamWriter errOutLane=new OutputStreamWriter(new FileOutputStream(fileOutErrOutLane),encoding);
			BufferedWriter writerErrOutLane=new BufferedWriter(errOutLane);
			String lineErr="";
			while((lineErr=readerErr.readLine())!=null){
				count++;
				String[] data=lineErr.split(",");
				String inLicense=data[10];
				String outLicense=data[20];
				String inCX=data[8];
				String outCX=data[18];
				String inStation=data[2];
				String outStation=data[12];
				String outPerson=data[15];
				String outLane=data[13];
				String isEtc=data[data.length-2];
				if(inCX.length()>0&&outCX.length()>0&&inStation.length()>0&&outStation.length()>0&&outPerson.length()>0&&outLane.length()>0){
					if(inLicense.equals(outLicense)&&inLicense.length()>5&&!inCX.equals(outCX)){
						add(mapCx, inCX+"--"+outCX);
						add(mapInStation,inStation);
						add(mapOutStation,outStation);
						
//						put(mapInPerson,inStation,inPerson);
						put(mapOutPerson,outStation,outPerson);
//						put(mapInLane,inStation,inLane);
						put(mapOutLane,outStation,outLane);
						if(isEtc.equals("0")){
							count1++;
						}
					}
				}
			}
			readerErr.close();
			System.out.println("read err finish!");
			writeIntegerMap1(mapCx,writerErrCx);
			mapInStation1=getMaxCountStation(mapInStation);
			writeIntegerMap1(mapInStation,writerErrInStation);
			mapOutStation1=getMaxCountStation(mapOutStation);
			writeIntegerMap1(mapOutStation,writerErrOutStation);
			
//			writeIntegerMap2(mapInPerson,mapInStation1,writerErrInPerson);
			writeIntegerMap2(mapOutPerson,mapOutStation1,writerErrOutPerson);
//			writeIntegerMap2(mapInLane,mapInStation1,writerErrInLane);
			writeIntegerMap2(mapOutLane,mapOutStation1,writerErrOutLane);
			System.out.println("错误总条数："+count);
			System.out.println("出口车道为MTC的条数："+count1);
		}catch(Exception e){
			System.out.println("读取文件出错");
			e.printStackTrace();
		}
	}
	private static void read(String path,String pathCxEtc,String pathCxMtc){
		File file =new File(path);
		
		File fileCXETC=new File(pathCxEtc);
		File fileCXMTC=new File(pathCxMtc);
		Map<String,String> mapCx=new HashMap<>();
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			InputStreamReader in=new InputStreamReader(new FileInputStream(file),encoding);
			BufferedReader reader=new BufferedReader(in);
			
			InputStreamReader inCXETC=new InputStreamReader(new FileInputStream(fileCXETC),encoding1);
			BufferedReader readerCXETC=new BufferedReader(inCXETC);
			InputStreamReader inCXMTC=new InputStreamReader(new FileInputStream(fileCXMTC),encoding1);
			BufferedReader readerCXMTC=new BufferedReader(inCXMTC);
			
			String lineCXMTC="";
			while((lineCXMTC=readerCXMTC.readLine())!=null){
				String[] data=lineCXMTC.split(",");
				String license=data[0];
				String color=data[1];
				String cx=data[2];
				mapCx.put(license+","+color, cx);
			}
			readerCXMTC.close();
			
			String lineCXETC="";
			while((lineCXETC=readerCXETC.readLine())!=null){
				String[] data=lineCXETC.split(",");
				String license=data[0];
				String color=data[1];
				String cx=data[2];
				mapCx.put(license+","+color, cx);
			}
			readerCXETC.close();
			
			int count=0;
			int count1=0;
			int amount=0;
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				String inLicense=data[10];
				String outLicense=data[20];
				String inCX=data[8];
				String outCX=data[18];
				String licenseColor=data[26];
				String isETC=data[27];
				if(data.length>29){
					outLicense=data[20]+","+data[21];
					licenseColor=data[27];
					isETC=data[28];
				}
				if(mapCx.containsKey(inLicense+","+licenseColor)){
					count++;
				}else{
					count1++;
				}
			}
			reader.close();
			System.out.println("能修复："+count);
			System.out.println("不能修复："+count1);
		}catch(Exception e){
			System.out.println("读取文件出错");
			e.printStackTrace();
		}
	}
	private static void readCq(String path) throws IOException{
		File file =new File(path);
		List<String> list=Arrays.asList(file.list());
		int count=0;
		int count1=0;
		int amount=0;
		File fileOut=new File("C:\\Users\\pengrui\\Desktop\\各车型每天交易量.csv");
		LinkedMap<String,HashMap<String,Integer>> map=new LinkedMap<>();
		Map<String,Integer> map1=new HashMap<>();
		OutputStreamWriter write=new OutputStreamWriter(new FileOutputStream(fileOut),"UTF-8");
		BufferedWriter writer=new BufferedWriter(write);
		for(int i=0;i<list.size();i++){
			File file2=new File(path+"/"+list.get(i));
			try{
				String encoding="UTF-8";
				String encoding1="GBK";
				InputStreamReader in=new InputStreamReader(new FileInputStream(file2),encoding1);
				BufferedReader reader=new BufferedReader(in);
				String line="";
				while((line=reader.readLine())!=null){
					String[] data=line.split(";");
					amount++;
					String inCX=data[18].trim();
					String inKH=data[29].trim();
					String outCX=data[11].trim();
					String outKH=data[28].trim();
					String inRealCX=""+getVehicleType(inKH,inCX);
					String outRealCX=""+getVehicleType(outKH,outCX);
					String isEasyAccess=data[45].trim();
					put(map,list.get(i),outRealCX);
					add(map1,isEasyAccess);
				}
				reader.close();
			}catch(Exception e){
				System.out.println("读取文件出错");
				e.printStackTrace();
			}
			System.out.println("read "+path+"/"+list.get(i)+" finish");
		}
		System.out.println("总条数："+amount);
		System.out.println("车型不一致条数："+count);
		Set set=map.entrySet();
		Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
		for(int i=0;i<entries.length;i++){
			String key=entries[i].getKey().toString();//key为车牌号
			Map<String,Integer> mapIn=(Map<String, Integer>) entries[i].getValue();
			Set set1=mapIn.entrySet();
			Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
			for(int j=0;j<entries1.length;j++){
				String key1=entries1[j].getKey().toString();
				String value=entries1[j].getValue().toString();
				writer.write(key+","+key1+","+value+"\n");
				writer.flush();
			}
		}
		writer.close();
		System.out.println(map1);
	}
	public static void main(String[] args) throws IOException{
		presentTime();
//		readGuiZhouMTC(GZMTCOriginal);
//		System.out.println("readGZMTCOriginal complete!");
//		readGuiZhouETC(GZETCOriginal);
//		System.out.println("readGZETCOriginal complete!");
//		
//		readGZ(gz201701,gzAll);
//		System.out.println("readGZ complete!");
//		cxReal(gzAll);
//		System.out.println("cxReal complete!");
//		repair(err,etcComfirm,mtcComfirm);
//		System.out.println("repair complete!");
//		
//		
//		writeByDay(repair,repairDevideByDay);
//		System.out.println("writeByDay complete!");
		
//		tiaoShiEnGZETC(enGZETCOriginal);
//		tiaoShiEnGZMTC(enGZMTCOriginal);
//		read(gz201701);
		
//		preSystem(GZETCOriginal,GZMTCOriginal,gzEtcComfirm,gzMtcComfirm,cxDataBase,newGz201701,mtcPartFlow,od);
//		cxComfirm(cxDataBase);
//		read(newGz201701);
		
//		preSystemCq(cq201701,cqCxDataBase);
//		preSystemCq(cq201702,cqCxDataBase);
//		preSystemCq(cq201703,cqCxDataBase);
//		cxComfirm(cqCxDataBase);
//		preSystemCq1(cq201703,cqEtcCxComfirm,cqMtcCxComfirm,cqEqualPlate,cqOd,cqRepair,cqErr);
//		preSystemCq1(cq201701,cqEtcCxComfirm,cqMtcCxComfirm,cqEqualPlate,cqOd,cqRepair201701,cqErr201701);
		preSystemCq1(cq201702,cqEtcCxComfirm,cqMtcCxComfirm,cqEqualPlate,cqOd,cqRepair201702,cqErr201702);
//		writeErrCq(cq201703,cqErr);
//		errCxAndStation(cqErr,errCx,errInStation,errOutStation,errInPerson,errOutPerson,errInLane,errOutLane);
		
//		readCq(cq201703);
//		read("G:\\新建文件夹\\重庆\\201703结果\\出入车型不同情况\\errCq.csv",cqEtcCxComfirm,cqMtcCxComfirm);
		presentTime();
	}
}
