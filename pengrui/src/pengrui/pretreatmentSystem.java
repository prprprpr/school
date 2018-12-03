package pengrui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class pretreatmentSystem {
	private static Logger logger=Logger.getLogger(preSystem.class);
	private static String GZETCOriginal="H:/����һ�·�����/exlistelec201701.txt";
	private static String GZMTCOriginal="H:/����һ�·�����/exlistcash201701.txt";
	
	private static String cxDataBase="H:/��������/���Ƴ������ݿ�.csv";
	private static String etcComfirm="H:/��������/���Ƴ��Ͷ�Ӧ��/etcComfirm.csv";
	private static String etcNotComfirm="H:/��������/���Ƴ��Ͷ�Ӧ��/etcNotComfirm.csv";
	private static String mtcComfirm="H:/��������/���Ƴ��Ͷ�Ӧ��/mtcComfirm.csv";
	private static String mtcNotComfirm="H:/��������/���Ƴ��Ͷ�Ӧ��/mtcNotComfirm.csv";
	
	private static String newGz201701="H:/��������/�޸�������/newGz201701.csv";
	
	private static String stationDistance="G:/�½��ļ���/����/վ��վ֮�����.xlsx";
	
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

        for(int i=0;i<32-len;i++)   //����32λ
        {
            binaries.insert(0, '0');
        }

        return binaries.toString();
    }
	/*
	 * ���뷽ʽ��0-Ĭ��(��0Ԫ��)��1-�ǽ�����ͨ����2-����ͨ��������3-���񳵡�4-������ͨ����5-������6-��ѳ���7-���ӡ�8-����
	 */
	public static int freetrans(String dealstatus) {
        String dealbin = null;
        dealbin = fixbinary(Integer.parseInt(dealstatus));
        if (dealbin.charAt(7) == '1') {        //����ͨ��
            if (dealbin.substring(28, 31).compareTo("101") == 0) {  //���ǽ�����
                return 4;
            } else {                            //���ǽ�������ֻ����ͨ����
                return 1;
            }
        }
        if ((dealbin.substring(28, 31).compareTo("101") == 0) && dealbin.charAt(7) != '1') {    //��������ͨ��
            return 2;
        }
        switch (dealbin.substring(28, 31)) {
            case "011":
                return 3;   //�ǹ���
            case "100":
                return 5;   //�Ǿ���
            case "110":
                return 6;   //����ѳ�
            case "111":
                return 7;   //�ǳ���
            default:
                return 8;   //����
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
				//����ETC��12��22��52��59��67��ʾ���ƣ�70��ʡ�ݡ�����������
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
					if(w.trim().contains("�ƣ�7�ģ�00")){
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
			h2="��������";
		}
		return h2;
	}
	public static String getEtcNewLine(String line,Map<String,String> distance) throws ParseException{
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
		String enStation=h1[4].trim();
		String exStation=h1[14].trim();
		if(distance.containsKey(enStation+"-"+exStation)){
			h2+=","+distance.get(enStation+"-"+exStation);
		}else{
			h2+=",0";
		}
		return h2;
	}
	
	public  static boolean isNumeric(String str){  
		for (int i = str.length();--i>=0;){    
			if (!Character.isDigit(str.charAt(i))){  
				return false;  
			}  
		}  
		return true;  
	}
	//���㳵�����ƶ�
	private static int min(int one,int two,int three){
		return (one=one<two?one:two)<three?one:three;
	}
	public static float levenshtein(String str1, String str2) {  
	       // ���������ַ����ĳ��ȡ�  
	        int len1 = str1.length();  
	       int len2 = str2.length();  
	        // ��������˵�����飬���ַ����ȴ�һ���ռ�  
	       int[][] dif = new int[len1 + 1][len2 + 1];  
	       // ����ֵ������B��  
	        for (int a = 0; a <= len1; a++) {  
			dif[a][0] = a;  
	        }  
	        for (int a = 0; a <= len2; a++) {  
	           dif[0][a] = a;  
			   }  
	        // ���������ַ��Ƿ�һ�����������ϵ�ֵ  
	       int temp;  
	        for (int i = 1; i <= len1; i++) {  
	           for (int j = 1; j <= len2; j++) {  
	  
//		               System.out.println("i = " + i + " j = " + j + " str1 = "  
//		                       + str1.charAt(i - 1) + " str2 = " + str2.charAt(j - 1));  
	                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {  
	                   temp = 0;  
	                } else {  
	                    temp = 1;  
	                }  
	                // ȡ����ֵ����С��  
	                dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,  dif[i - 1][j] + 1);  
//		               System.out.println("i = " + i + ", j = " + j + ", dif[i][j] = "  
//		                       + dif[i][j]);  
	            }  
	        } 
	       // System.out.println("�ַ���\"" + str1 + "\"��\"" + str2 + "\"�ıȽ�");  
	        // ȡ�������½ǵ�ֵ��ͬ����ͬλ�ô���ͬ�ַ����ıȽ�  
	       // System.out.println("���첽�裺" + dif[len1][len2]);  
	        // �������ƶ�  
	        float similarity = 1 - (float) dif[len1][len2]  
	                / Math.max(str1.length(), str2.length()); 
	        //System.out.println("���ƶȣ�" + similarity); 
	        return similarity;
	    }
	 // ���Ʋ��淶���µ��޷��޸�
		 public  static boolean irregular(String plateen,String plateex){
			 if(isNumeric(plateen)&&isNumeric(plateex)||plateen.length()<6&&(plateex.length()<6||plateex.length()>8)||plateen.length()>8&&(plateex.length()<6&&plateex.length()>8)){
				 return true; 
			 }else{
				 return false;
			 }
		 }
        //ʶ�����1
		 public static int error1(String plateen,String plateex,HashMap<String, Integer> odplate){
			 int index=0;
			 if(!odplate.containsKey(plateen)&&odplate.containsKey(plateex)){
					index=1;
			}
			 else if(odplate.containsKey(plateen)&&!odplate.containsKey(plateex)){
					index=2;
				}
			 else if(!odplate.containsKey(plateen)&&!odplate.containsKey(plateex)){
				 index=3;
			 }
			 return index;
		 } 
		 //ʶ�����2 
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
			//���Ʋ��淶��ʶ�����
		 public static int error3(String plateen , String plateex){
			 int index=0;
			 if(plateen.length()>6&&!isNumeric(plateen)&& plateex.length()>plateen.length()&&plateex.contains(plateen)){//���ڳ��������ַ�
				 index=2;
			 }
			 else if(plateex.length()>6&&!isNumeric(plateex)&&plateen.length()>plateex.length()&&plateen.contains(plateex)){//��ڳ��������ַ�
				 index=1;
			 }
			 else if(plateen.length()<6&&plateex.length()>6&&!isNumeric(plateex)&&plateex.contains(plateen)){//��ڳ���ɾ���ַ�
				 index=1;
			 }
			 else if(plateex.length()<6&&plateen.length()>6&&!isNumeric(plateen)&&plateen.contains(plateex)){//���ڳ���ɾ���ַ�
				 index=2;
			 }
			 else if(isNumeric(plateen)&&!isNumeric(plateex)&&plateex.length()>6){//��ڳ���Ϊ����
				 index=1;
			 }
			 else if(isNumeric(plateex)&&!isNumeric(plateen)&&plateen.length()>6){//���ڳ���Ϊ����
				 index=2;
			 }else if(levenshtein(plateen,plateex)<0.6&&plateen.length()<6&&plateex.length()>6){//��ڳ���Ϊ���ƵĽϵ͵��ַ�
				 index=1;
			 }else if(levenshtein(plateen,plateex)<0.6&&plateex.length()<6&&plateen.length()>6){//���ڳ���Ϊ���ƶȽϵ͵��ַ�
				 index=2;
			 }
			 else if(plateen.contains("��")&&plateex.length()>6&&!isNumeric(plateex)){//���޳��ƵĽ�������
				 index=1;
			 }else if(plateex.contains("��")&&plateen.length()>6&&!isNumeric(plateen)){
				 index=2;
			 }
			return index;
		 }
		 public static String plateRevise(String str,HashMap<String, Integer> odplate,HashMap<String, String> od,int[] count)	{
				//count�����ܵĳ���ڳ����д�count1�����ɹ��ģ�count2ʶ������ǲ���������count3��ʶ�����������
				String temp[]=null;
				String line=null;
			    temp=str.split(",");
			    String time=temp[14].trim();
			    String plateen=temp[10].trim();
			    String plateex=temp[20].trim();
			    String s1=temp[2];
			    String s2=temp[12];
			    int i=1;
			    if(!plateen.equals(plateex)){
				    if(time.startsWith("1970")){//��ʶ�����ϵͳ��0
					   line=str;
					   count[3]++;
					   count[0]++;
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
				else if(error3(plateen,plateex)==1){//���Ʋ��淶����ڳ���ʶ�����
					count[1]++;
					count[0]++;
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
				}else if(error3(plateen,plateex)==2){//���Ʋ��淶�ĳ��ڳ���ʶ�����
					count[1]++;
					count[0]++;
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
				}
				else if(irregular(plateen,plateex)){//ʶ����󲻿��޸�
					count[2]++;
					count[0]++;
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
				else if(levenshtein(plateen,plateex)>0.6){
					if(isNumeric(plateen)&&isNumeric(plateex)){//ʶ����󲻿�����
					count[2]++;
					count[0]++;
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
					else if(error1(plateen,plateex,odplate)==1){//��ڳ��ƴ���
						count[1]++;
						count[0]++;
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
							    
				}
					else if(error2(plateen,plateex,s1,s2,od)==1||error3(plateen,plateex)==1){//��ڳ��ƴ���
						count[1]++;
						count[0]++;
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
					}
				else if(error1(plateen,plateex,odplate)==2){//���ڳ��ƴ���
					count[1]++;
					count[0]++;
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
				}
				else if(error2(plateen,plateex,s1,s2,od)==2||error3(plateen,plateex)==2){//���ڳ��ƴ���
					count[1]++;
					count[0]++;
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
				}
			  else {//���ƶȸߣ����޷�ȷ�ϳ���ڳ��ƣ�ʶ����󲻿�����
				  count[2]++;
				  count[0]++;
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
				}
			 else if(levenshtein(plateen,plateex)<0.6){//��ʶ����󣬲����޸�
				 count[3]++;
				 count[0]++;
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
				}
			    else{
			    	if(isNumeric(plateen)){
			    		count[2]++;
			    		count[0]++;
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
			    	else if(plateen.length()<7){
			    		count[0]++;
			    		count[2]++;
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
			    	else {
			    	line=str;
			    	}
			    	}
			return line;
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
			//��������
			h2="��������";
		}
		return h2;
	}
	
	public static String getMtcNewLine(String line,Map<String,String> distance) throws ParseException{
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
		String enStation=h1[4].trim();
		String exStation=h1[14].trim();
		if(distance.containsKey(enStation+"-"+exStation)){
			h2+=","+distance.get(enStation+"-"+exStation);
		}else{
			h2+=",0";
		}
		return h2;
	}
	/**
	 * ���س����ĳ��� 
	 * @param khFlag �ͻ���־λ
	 * @param vehClass ���ʹ���
	 * @return 0��С�ͳ��� 1�������Ϳͳ���    2��1�ͻ�����   3��2�ͻ����� 4��3�ͻ����� 5��4�ͻ����� 6��5�ͻ�����  
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
			String dealStatus=data[45].trim();//�����Ƿ�����ͨ��
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
			h="��������";
		}
		return h;
	}
	public static String getValue(Cell cell){
		if(cell.getCellType()==cell.CELL_TYPE_NUMERIC){
			DecimalFormat df = new DecimalFormat("0"); 
			String strCell = df.format(cell.getNumericCellValue());
			return strCell;
		}else if(cell.getCellType()==cell.CELL_TYPE_STRING){
			return cell.getStringCellValue();
		}
		else{
			return cell.getStringCellValue();
		}
	}
	public static Map<String,String> readStationDistance(String path){
		Map<String,HashMap<String,Integer>> map=new HashMap<>();
		Map<String,String> outMap=new HashMap<>();
		File file=new File(path);
		boolean isXlsx=false;
		if(path.endsWith("xlsx")){
			isXlsx=true;
		}
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			InputStreamReader in=new InputStreamReader(new FileInputStream(file),encoding1);
			BufferedReader reader=new BufferedReader(in);
			
			int amount=0;
			int count=0;
			InputStream input=new FileInputStream(path);
			Workbook wb=null;
			if(isXlsx){
				wb=new XSSFWorkbook(input);
			}else{
				wb=new HSSFWorkbook(input);
			}
			Sheet sheet=wb.getSheetAt(0);
			for(int rowNum=0;rowNum<=sheet.getLastRowNum();rowNum++){
				amount++;
				Row row=sheet.getRow(rowNum);
				if(row!=null){
					String enStation="";
					String exStation="";
					String distance="";
					Cell cellEnStation=row.getCell(3);
					Cell cellExStation=row.getCell(4);
					Cell cellDistance=row.getCell(2);
					if(cellEnStation!=null&&("")!=cellEnStation.toString()){
						enStation=getValue(row.getCell(3));
					}
					if(cellExStation!=null&&("")!=cellExStation.toString()){
						exStation=getValue(row.getCell(4));
					}
					if(cellDistance!=null&&("")!=cellDistance.toString()){
						distance=getValue(row.getCell(2));
					}
					put(map,enStation+"-"+exStation,distance);
				}
			}
			reader.close();
			Set set=map.entrySet();
			Map.Entry[] entries = (Map.Entry[])set.toArray(new Map.Entry[set.size()]); 
			for(int i=0;i<entries.length;i++){
				String key=entries[i].getKey().toString();//keyΪ���ƺ�
				Map<String,Integer> mapIn=(Map<String, Integer>) entries[i].getValue();
				if(mapIn.size()>1){
					Set set1=mapIn.entrySet();
					Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
					int min=0;
					for(int j=0;j<entries1.length;j++){
						String distance=entries1[j].getKey().toString();
						if(min==0||min>Integer.parseInt(distance)){
							min=Integer.parseInt(distance);
						}
					}
					outMap.put(key, ""+min);
				}else{
					Set set1=mapIn.entrySet();
					Map.Entry[] entries1 = (Map.Entry[])set1.toArray(new Map.Entry[set1.size()]);
					for(int j=0;j<entries1.length;j++){
						String distance=entries1[j].getKey().toString();
						outMap.put(key, distance);
					}
				}
			}
			System.out.println("finish!");
			System.out.println("amount:"+amount);
			System.out.println("count:"+outMap.size());
		}catch(Exception e){
			System.out.println("����");
			e.printStackTrace();
		}
		return outMap;
	}
	public static void preSystem(String GZETCOriginal,String GZMTCOriginal,String gzEtcComfirm,String gzMtcComfirm,String outPath,String cxDataBase,Map<String,String> distance){
		File fileEtc=new File(GZETCOriginal);
		File fileMtc=new File(GZMTCOriginal);
		File fileCXETC=new File(gzEtcComfirm);
		File fileCXMTC=new File(gzMtcComfirm);
		
		File filecxDataBase=new File(cxDataBase);
		
		File fileOut=new File(outPath);
		if(!fileOut.exists()){
			try{
				fileOut.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		Map<String,String> mapCx=new HashMap<>();
		
		HashMap<String, Integer> odplate= new HashMap<>();//����һ��һ��ˮ�ĳ���
		HashMap<String, String> od= new HashMap<>();//OD��Ϣ
		
		int countErrEtc=0;
		int amountEtc=0;
		int[] countEtcInfo=new int[4];//0�����ܵĳ���ڳ����д�1�����ɹ��ģ�2ʶ������ǲ���������3��ʶ�����������
		
		int cpNotEqualEtc=0;
		int cpEqualCxNotEqualEtc=0;
		int cxRepairSuccessEtc=0;
		int cxRepairNotSuccessEtc=0;
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			BufferedReader readerEtc=dataService.getReader(GZETCOriginal, encoding1);
			BufferedReader readerMtc=dataService.getReader(GZMTCOriginal, encoding1);
			
			BufferedReader readerCXETC=dataService.getReader(gzEtcComfirm, encoding1);
			BufferedReader readerCXMTC=dataService.getReader(gzMtcComfirm, encoding1);
			BufferedReader readerDataBase=dataService.getReader(cxDataBase, encoding);
			
			BufferedWriter writer=dataService.getWriter(outPath, encoding);
			
			String lineOd="";
			while((lineOd=readerDataBase.readLine())!=null){
				String[] data=lineOd.split(",");
				String plate=data[0].trim();
				String odinfo=data[2].trim();//cq�ĸ�Ϊ2
				if(!od.containsKey(plate)){
					od.put(plate, odinfo);
				}
				if(!odplate.containsKey(plate)){
					odplate.put(plate, 1);
				}
			}
			readerDataBase.close();
			
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
			
			String lineEtc=null;
			int[] aEtc=new int[100];
			readerEtc.readLine();
			readerEtc.readLine();
			int countEtc=0;
			while((lineEtc=readerEtc.readLine())!=null){
				String newLine=getEtcNewLine(lineEtc,distance);
				String resultLine="";
				if(newLine.equals("��������")){
					countErrEtc++;
				}else{
					String[] newData=newLine.split(",");
					String inLicense=newData[10];
					String outLicense=newData[20];
					String inCX=newData[8];
					String outCX=newData[18];
					String licenseColor=newData[26];
//					if(!inLicense.equals(outLicense)){
//						cpNotEqualEtcBefore++;
//					}
					newLine=plateRevise(newLine,odplate,od,countEtcInfo);
					String[] newData1=newLine.split(",");
					inLicense=newData1[10];
					outLicense=newData1[20];
					inCX=newData1[8];
					outCX=newData1[18];
					licenseColor=newData1[26];
					if(inLicense.equals(outLicense)&&inLicense.length()>6){
						if(!inCX.equals(outCX)){
							cpEqualCxNotEqualEtc++;
							if(mapCx.containsKey(inLicense+","+licenseColor)){
								String realCx=mapCx.get(inLicense+","+licenseColor);
								newLine=repairCxLine(newLine,realCx);
								cxRepairSuccessEtc++;
							}else{
								cxRepairNotSuccessEtc++;
							}
						}else{
						}
					}else{
						cpNotEqualEtc++;
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
			int[] countMtcInfo=new int[4];
			int cpEqualCxNotEqualMtc=0;
			int cxRepairSuccessMtc=0;
			int cxRepairNotSuccessMtc=0;
			String lineMtc=null;
			int countMtc=0;
			while((lineMtc=readerMtc.readLine())!=null){
				String newLine=getMtcNewLine(lineMtc,distance);
				String resultLine="";
				if(newLine.equals("��������")){
					countErrMtc++;
				}else{
					String[] newData=newLine.split(",");
					String inLicense=newData[10];
					String outLicense=newData[20];
					String inCX=newData[8];
					String outCX=newData[18];
					String licenseColor=newData[26];
//					if(!inLicense.equals(outLicense)){
//						
//					}
					newLine=plateRevise(newLine,odplate,od,countMtcInfo);
					String[] newData1=newLine.split(",");
					inLicense=newData1[10];
					outLicense=newData1[20];
					inCX=newData1[8];
					outCX=newData1[18];
					licenseColor=newData1[26];
					if(inLicense.equals(outLicense)&&inLicense.length()>6){
						if(!inCX.equals(outCX)){
							cpEqualCxNotEqualMtc++;
							if(mapCx.containsKey(inLicense+","+licenseColor)){
								String realCx=mapCx.get(inLicense+","+licenseColor);
								newLine=repairCxLine(newLine,realCx);
								cxRepairSuccessMtc++;
							}else{
								cxRepairNotSuccessMtc++;
							}
						}else{
						}
					}else{
//						cpNotEqualMtc++;
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
			odplate.clear();
			od.clear();
			amountMtc=countMtc-1;
			System.out.println("ETC��������������"+countErrEtc);
			System.out.println("ETC������������"+amountEtc);
			System.out.println("ETC���ƴ������ݣ�"+countEtcInfo[0]);
			System.out.println("ETC����ʶ������޸��ɹ����ݣ�"+countEtcInfo[1]);
			System.out.println("ETC����ʶ������޸�ʧ�����ݣ�"+countEtcInfo[2]);
			System.out.println("ETC���Ʒ�ʶ��������ݣ�"+countEtcInfo[3]);
			System.out.println("�����޸���ETC���복�Ʋ�ͬ����������"+cpNotEqualEtc);
			System.out.println("�����޸�ǰ��ETC���복����ͬ�����Ͳ�ͬ����������"+cpEqualCxNotEqualEtc);
			System.out.println("ETC���복����ͬ�����Ͳ�ͬ�����޸�ʧ��������"+cxRepairNotSuccessEtc);
			System.out.println("ETC���복����ͬ�����Ͳ�ͬ�����޸��ɹ�������"+cxRepairSuccessEtc);
			System.out.println();
			System.out.println("MTC��������������"+countErrMtc);
			System.out.println("MTC������������"+amountMtc);
			System.out.println("MTC���ƴ������ݣ�"+countMtcInfo[0]);
			System.out.println("MTC����ʶ������޸��ɹ����ݣ�"+countMtcInfo[1]);
			System.out.println("MTC����ʶ������޸�ʧ�����ݣ�"+countMtcInfo[2]);
			System.out.println("MTC���Ʒ�ʶ��������ݣ�"+countMtcInfo[3]);
			System.out.println("�����޸�ǰ��MTC���복����ͬ�����Ͳ�ͬ����������"+cpEqualCxNotEqualMtc);
			System.out.println("MTC���복����ͬ�����Ͳ�ͬ�����޸�ʧ��������"+cxRepairNotSuccessMtc);
			System.out.println("MTC���복����ͬ�����Ͳ�ͬ�����޸��ɹ�������"+cxRepairSuccessMtc);
			logger.info("ETC��������������"+countErrEtc);
			logger.info("ETC������������"+amountEtc);
			logger.info("ETC���ƴ������ݣ�"+countEtcInfo[0]);
			logger.info("ETC����ʶ������޸��ɹ����ݣ�"+countEtcInfo[1]);
			logger.info("ETC����ʶ������޸�ʧ�����ݣ�"+countEtcInfo[2]);
			logger.info("ETC���Ʒ�ʶ��������ݣ�"+countEtcInfo[3]);
			
			logger.info("�����޸���ETC���복�Ʋ�ͬ����������"+cpNotEqualEtc);
			logger.info("�����޸�ǰ��ETC���복����ͬ�����Ͳ�ͬ����������"+cpEqualCxNotEqualEtc);
			logger.info("ETC���복����ͬ�����Ͳ�ͬ�����޸�ʧ��������"+cxRepairNotSuccessEtc);
			logger.info("ETC���복����ͬ�����Ͳ�ͬ�����޸��ɹ�������"+cxRepairSuccessEtc);
			
			logger.info("MTC��������������"+countErrMtc);
			logger.info("MTC������������"+amountMtc);
			
			logger.info("MTC���ƴ������ݣ�"+countMtcInfo[0]);
			logger.info("MTC����ʶ������޸��ɹ����ݣ�"+countMtcInfo[1]);
			logger.info("MTC����ʶ������޸�ʧ�����ݣ�"+countMtcInfo[2]);
			logger.info("MTC���Ʒ�ʶ��������ݣ�"+countMtcInfo[3]);
			logger.info("�����޸�ǰ��MTC���복����ͬ�����Ͳ�ͬ����������"+cpEqualCxNotEqualMtc);
			logger.info("MTC���복����ͬ�����Ͳ�ͬ�����޸�ʧ��������"+cxRepairNotSuccessMtc);
			logger.info("MTC���복����ͬ�����Ͳ�ͬ�����޸��ɹ�������"+cxRepairSuccessMtc);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("��ȡ�ļ�����:",e);
		}
	}
	public static void preSystemCQ(String CQOriginal,String cqEtcComfirm,String cqMtcComfirm,String outPath,String outErrPath,String cxDataBaseCQ){
		File fileIn=new File(CQOriginal);
		File fileCXETC=new File(cqEtcComfirm);
		File fileCXMTC=new File(cqMtcComfirm);
		
		File fileDataBaseCQ=new File(cxDataBaseCQ);
		
		File fileOut=new File(outPath);
		if(!fileOut.exists()){
			try{
				fileOut.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		File fileOutErr=new File(outErrPath);
		if(!fileOutErr.exists()){
			try{
				fileOutErr.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		Map<String,String> mapCx=new HashMap<>();

		HashMap<String, Integer> odplate= new HashMap<>();//����һ��һ��ˮ�ĳ���
		HashMap<String, String> od= new HashMap<>();//OD��Ϣ
		
		int countErr=0;
		int amountEtc=0;
		int cpNotEqualEtcBefore=0;
		int[] countEtcInfo=new int[4];//0�����ܵĳ���ڳ����д�1�����ɹ��ģ�2ʶ������ǲ���������3��ʶ�����������
		int cpEqualCxNotEqualEtc=0;
		int cxRepairSuccessEtc=0;
		int cxRepairNotSuccessEtc=0;
		int countEtc=0;
		
		int amountMtc=0;
		int[] countMtcInfo=new int[4];//0�����ܵĳ���ڳ����д�1�����ɹ��ģ�2ʶ������ǲ���������3��ʶ�����������
		int cpEqualCxNotEqualMtc=0;
		int cxRepairSuccessMtc=0;
		int cxRepairNotSuccessMtc=0;
		int countMtc=0;
		
		int cxNotEqual=0;
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			InputStreamReader in=new InputStreamReader(new FileInputStream(fileIn),encoding1);
			BufferedReader reader=new BufferedReader(in);
			
			InputStreamReader inCXETC=new InputStreamReader(new FileInputStream(fileCXETC),encoding1);
			BufferedReader readerCXETC=new BufferedReader(inCXETC);
			InputStreamReader inCXMTC=new InputStreamReader(new FileInputStream(fileCXMTC),encoding1);
			BufferedReader readerCXMTC=new BufferedReader(inCXMTC);
			
			InputStreamReader inDataBaseCQ=new InputStreamReader(new FileInputStream(fileDataBaseCQ),encoding);
			BufferedReader readerDataBaseCQ=new BufferedReader(inDataBaseCQ);
			
			OutputStreamWriter write=new OutputStreamWriter(new FileOutputStream(fileOut),encoding);
			BufferedWriter writer=new BufferedWriter(write);
			OutputStreamWriter writeErr=new OutputStreamWriter(new FileOutputStream(fileOutErr),encoding);
			BufferedWriter writerErr=new BufferedWriter(writeErr);
			
			String lineOd="";
			while((lineOd=readerDataBaseCQ.readLine())!=null){
				String[] data=lineOd.split(",");
				String plate=data[0].trim();
				String odinfo=data[1].trim();//cq�ĸ�Ϊ2
				if(!od.containsKey(plate)){
					od.put(plate, odinfo);
				}
				if(!odplate.containsKey(plate)){
					odplate.put(plate, 1);
				}
			}
			readerDataBaseCQ.close();
			
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
			
			String line=null;
			while((line=reader.readLine())!=null){
				String newLine=getLineCQ(line);
				String resultLine="";
				if(newLine.equals("��������")){
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
						}
						newLine=plateRevise(newLine,odplate,od,countEtcInfo);
						String[] newData1=newLine.split(",");
						inLicense=newData1[10];
						outLicense=newData1[20];
						inCX=newData1[8];
						outCX=newData1[18];
						licenseColor=newData1[26];
						if(inLicense.equals(outLicense)&&inLicense.length()>6){
							if(!inCX.equals(outCX)){
								writerErr.write(newLine+"\n");
								writerErr.flush();
								cpEqualCxNotEqualEtc++;
								if(mapCx.containsKey(inLicense+","+licenseColor)){
									String realCx=mapCx.get(inLicense+","+licenseColor);
									newLine=repairCxLineCq(newLine,realCx);
									cxRepairSuccessEtc++;
								}else{
									cxRepairNotSuccessEtc++;
								}
							}else{
							}
						}else{
						}
						countEtc++;
					}else{
//						if(!inLicense.equals(outLicense)){
//							cpNotEqualMtcBefore++;
//						}
						newLine=plateRevise(newLine,odplate,od,countMtcInfo);
						String[] newData1=newLine.split(",");
						inLicense=newData1[10];
						outLicense=newData1[20];
						inCX=newData1[8];
						outCX=newData1[18];
						licenseColor=newData1[26];
						if(inLicense.equals(outLicense)&&inLicense.length()>6){
//							cpEqualMtc++;
							if(!inCX.equals(outCX)){
								writerErr.write(newLine+"\n");
								writerErr.flush();
								cpEqualCxNotEqualMtc++;
								if(mapCx.containsKey(inLicense+","+licenseColor)){
									String realCx=mapCx.get(inLicense+","+licenseColor);
									newLine=repairCxLineCq(newLine,realCx);
									cxRepairSuccessMtc++;
								}else{
									cxRepairNotSuccessMtc++;
								}
							}else{
							}
						}else{
//							cpNotEqualMtc++;
						}
						countMtc++;
					}
					resultLine=newLine;
					writer.write(resultLine+"\n");
					writer.flush();
				}
			}
			reader.close();
			writer.close();
			writerErr.close();
			mapCx.clear();
			odplate.clear();
			od.clear();
			amountEtc=countEtc;
			amountMtc=countMtc;
			System.out.println("���Ͳ�ͬ������"+cxNotEqual);
			System.out.println("�ܴ�������������"+countErr);
			System.out.println("ETC������������"+amountEtc);
			System.out.println("�����޸�ǰ��ETC���복�Ʋ���ͬ����������"+cpNotEqualEtcBefore);
			System.out.println("ETC���ƴ������ݣ�"+countEtcInfo[0]);
			System.out.println("ETC����ʶ������޸��ɹ����ݣ�"+countEtcInfo[1]);
			System.out.println("ETC����ʶ������޸�ʧ�����ݣ�"+countEtcInfo[2]);
			System.out.println("ETC���Ʒ�ʶ��������ݣ�"+countEtcInfo[3]);
			System.out.println("�����޸�ǰ��ETC���복����ͬ�����Ͳ�ͬ����������"+cpEqualCxNotEqualEtc);
			System.out.println("ETC���복����ͬ�����Ͳ�ͬ�����޸�ʧ��������"+cxRepairNotSuccessEtc);
			System.out.println("ETC���복����ͬ�����Ͳ�ͬ�����޸��ɹ�������"+cxRepairSuccessEtc);
			System.out.println();
			
			System.out.println("MTC������������"+amountMtc);
			System.out.println("MTC���ƴ������ݣ�"+countMtcInfo[0]);
			System.out.println("MTC����ʶ������޸��ɹ����ݣ�"+countMtcInfo[1]);
			System.out.println("MTC����ʶ������޸�ʧ�����ݣ�"+countMtcInfo[2]);
			System.out.println("MTC���Ʒ�ʶ��������ݣ�"+countMtcInfo[3]);
			System.out.println("�����޸�ǰ��MTC���복����ͬ�����Ͳ�ͬ����������"+cpEqualCxNotEqualMtc);
			System.out.println("MTC���복����ͬ�����Ͳ�ͬ�����޸�ʧ��������"+cxRepairNotSuccessMtc);
			System.out.println("MTC���복����ͬ�����Ͳ�ͬ�����޸��ɹ�������"+cxRepairSuccessMtc);
			logger.info("���Ͳ�ͬ������"+cxNotEqual);
			logger.info("�ܴ�������������"+countErr);
			logger.info("ETC������������"+amountEtc);
			logger.info("�����޸�ǰ��ETC���복�Ʋ���ͬ����������"+cpNotEqualEtcBefore);
			logger.info("ETC���ƴ������ݣ�"+countEtcInfo[0]);
			logger.info("ETC����ʶ������޸��ɹ����ݣ�"+countEtcInfo[1]);
			logger.info("ETC����ʶ������޸�ʧ�����ݣ�"+countEtcInfo[2]);
			logger.info("ETC���Ʒ�ʶ��������ݣ�"+countEtcInfo[3]);
			logger.info("�����޸�ǰ��ETC���복����ͬ�����Ͳ�ͬ����������"+cpEqualCxNotEqualEtc);
			logger.info("ETC���복����ͬ�����Ͳ�ͬ�����޸�ʧ��������"+cxRepairNotSuccessEtc);
			logger.info("ETC���복����ͬ�����Ͳ�ͬ�����޸��ɹ�������"+cxRepairSuccessEtc);
			
			logger.info("MTC������������"+amountMtc);
			logger.info("MTC���ƴ������ݣ�"+countMtcInfo[0]);
			logger.info("MTC����ʶ������޸��ɹ����ݣ�"+countMtcInfo[1]);
			logger.info("MTC����ʶ������޸�ʧ�����ݣ�"+countMtcInfo[2]);
			logger.info("MTC���Ʒ�ʶ��������ݣ�"+countMtcInfo[3]);
			logger.info("�����޸�ǰ��MTC���복����ͬ�����Ͳ�ͬ����������"+cpEqualCxNotEqualMtc);
			logger.info("MTC���복����ͬ�����Ͳ�ͬ�����޸�ʧ��������"+cxRepairNotSuccessMtc);
			logger.info("MTC���복����ͬ�����Ͳ�ͬ�����޸��ɹ�������"+cxRepairSuccessMtc);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("��ȡ�ļ�����:",e);
		}
	}
	public static void main(String[] args){
//		preSystem(GZETCOriginal,GZMTCOriginal,etcComfirm,mtcComfirm,newGz201701,cxDataBase);
//		Map<String,String> map=readStationDistance(stationDistance);
	}
}
