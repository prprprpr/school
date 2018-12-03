package pengrui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class main {
	private static String GZETCOriginal="H:/����һ�·�����/exlistelec201701.txt";
	private static String GZMTCOriginal="H:/����һ�·�����/exlistcash201701.txt";
	private static String GZETCOriginal201608="G:/�½��ļ���/����/exlist201608/exlistelec201608.txt";
	private static String GZMTCOriginal201708="G:/�½��ļ���/����/exlist201608/exlistcash201608.txt";
	
	private static String cxDataBase="H:/��������/���Ƴ������ݿ�.csv";
	private static String etcComfirm="H:/��������/���Ƴ��Ͷ�Ӧ��/etcComfirm.csv";
	private static String etcNotComfirm="H:/��������/���Ƴ��Ͷ�Ӧ��/etcNotComfirm.csv";
	private static String mtcComfirm="H:/��������/���Ƴ��Ͷ�Ӧ��/mtcComfirm.csv";
	private static String mtcNotComfirm="H:/��������/���Ƴ��Ͷ�Ӧ��/mtcNotComfirm.csv";
	private static String newGz201701="H:/��������/�޸�������/newGz201701.csv";
	
	private static String CxComfirm="H:/��������/���Ƴ��Ͷ�Ӧ��/cxComfirm.csv";
	private static String CxNotComfirm="H:/��������/���Ƴ��Ͷ�Ӧ��/cxNotComfirm.csv";
	
	private static String mtcPartFlow="H:/����һ�·�����/�����޸��ⲿ�ļ�/MTCpartflow.csv";
	private static String od="H:/����һ�·�����/�����޸��ⲿ�ļ�/OD.csv";
	
	private static String cq="F:/����/201701";
	private static String cqOutRepair="F:/����/201701Repair";
	private static String cqOutErr="F:/����/201701Err";
	private static String cxDataBaseCQ="F:/����/cxDataBaseCQ.csv";
	
	private static String cqEtcComfirm="F:/����/���Ƴ���ȷ�ϱ�/etcComfirm.csv";
	private static String cqEtcNotComfirm="F:/����/���Ƴ���ȷ�ϱ�/etcNotComfirm.csv";
	private static String cqMtcComfirm="F:/����/���Ƴ���ȷ�ϱ�/mtcComfirm.csv";
	private static String cqMtcNotComfirm="F:/����/���Ƴ���ȷ�ϱ�/mtcNotComfirm.csv";
	
	private static String stationDistance="G:/�½��ļ���/����/վ��վ֮�����.xlsx";
	public static void presentTime(){
		Date startTime=new Date(System.currentTimeMillis());
		SimpleDateFormat sdFormatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String retStrFormatNowDate = sdFormatter.format(startTime);
		System.out.println(retStrFormatNowDate);
	}
	
	public static void readGZ(){
		presentTime();
//		dataService.upDate(GZETCOriginal, GZMTCOriginal, cxDataBase);
//		System.out.println("update finish!");
//		presentTime();
		cxComfirm.cxComfirm1(cxDataBase, CxComfirm, CxNotComfirm);
		System.out.println("cxComfirm finish!");
//		presentTime();
//		Map<String,String> mapDistance=pretreatmentSystem.readStationDistance(stationDistance);
//		pretreatmentSystem.preSystem(GZETCOriginal, GZMTCOriginal, etcComfirm, mtcComfirm, newGz201701, cxDataBase,mapDistance);
//		System.out.println("pretreatmentSystem finish!");
		presentTime();
	}
	public static void readCQ(){
		presentTime();
		File file=new File(cq);
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			String fileIn=cq+"/"+list.get(i);
			dataService.upDateCQ(fileIn, cxDataBaseCQ);
			cxComfirm.cxComfirm(cxDataBaseCQ, cqEtcComfirm, cqEtcNotComfirm, cqMtcComfirm, cqMtcNotComfirm);
			String fileOutRepair=cqOutRepair+"/repair"+list.get(i);
			String fileOutErr=cqOutErr+"/err"+list.get(i);
			pretreatmentSystem.preSystemCQ(fileIn, cqEtcComfirm, cqMtcComfirm, fileOutRepair, fileOutErr,cxDataBaseCQ);
			System.out.println(cq+"/"+list.get(i));
		}
		presentTime();
	}
	public static void read(){
		File fileEtc=new File(GZETCOriginal);
		File fileMtc=new File(GZMTCOriginal);
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			InputStreamReader inEtc=new InputStreamReader(new FileInputStream(fileEtc),encoding1);
			BufferedReader readerEtc=new BufferedReader(inEtc);
			InputStreamReader inMtc=new InputStreamReader(new FileInputStream(fileMtc),encoding1);
			BufferedReader readerMtc=new BufferedReader(inMtc);
			
			String lineEtc=null;
			int[] aEtc=new int[100];
			readerEtc.readLine();
			readerEtc.readLine();
			int countEtc=0;
			while((lineEtc=readerEtc.readLine())!=null){
				System.out.println(lineEtc);
				if(countEtc==0){
					String[] data=lineEtc.split(" ");
					for(int i=0;i<data.length;i++){
						aEtc[i]=data[i].length();
					}
				}else{
					String newLine=dataService.getEtcLine(lineEtc, aEtc);
					System.out.println(newLine);
//					String newLine2=dataService.getEtcNewLine(newLine);
//					System.out.println(newLine2);
				}
				countEtc++;
			}
			readerEtc.close();
			
//			String lineMtc=null;
//			int[] aMtc=new int[83];
//			readerMtc.readLine();
//			readerMtc.readLine();
//			int countMtc=0;
//			while((lineMtc=readerMtc.readLine())!=null){
//				System.out.println(lineMtc);
//				if(countMtc==0){
//					String[] data=lineMtc.split(" ");
//					for(int i=0;i<data.length;i++){
//						aMtc[i]=data[i].length();
//					}
//				}else{
//					String newLine=dataService.getMtcLine(lineMtc, aMtc);
//					System.out.println(newLine);
//					String newLine2=dataService.getMtcNewLine(newLine);
//					System.out.println(newLine2);
//				}
//				countMtc++;
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
//		readCQ();
//		readGZ();
		read();
	}
}
