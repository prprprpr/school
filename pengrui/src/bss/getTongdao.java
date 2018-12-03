package bss;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class getTongdao {
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
	public Map<String,String> getCheDaoToll(String inTollStation,String inCheDao){
		Map<String,String> mapToll=new HashMap<>();
		Map<String,String> mapCheDaoTollName=new HashMap<>();
		boolean isXlsx=false;
		if(inTollStation.endsWith("xlsx")){
			isXlsx=true;
		}
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			InputStream input=new FileInputStream(inTollStation);
			Workbook wb=null;
			if(isXlsx){
				wb=new XSSFWorkbook(input);
			}else{
				wb=new HSSFWorkbook(input);
			}
			Sheet sheet=wb.getSheetAt(0);
			for(int rowNum=1;rowNum<=sheet.getLastRowNum();rowNum++){
				Row row=sheet.getRow(rowNum);
				if(row!=null){
					String idToll=getValue(row.getCell(0));
					String province=getValue(row.getCell(7));
					String stationName=getValue(row.getCell(4));
					if(province.equals("HE_BEI")){
						mapToll.put(idToll, stationName);
					}
				}
			}
			wb.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		BufferedReader reader=topologyV2.getReader(inCheDao, "GBK");
		try{
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",15);
				String cheDao=data[0].replaceAll("\"", "").trim();
				String province=data[7].replaceAll("\"", "").trim();
				if(province.equals("HE_BEI")){
					if(cheDao.length()>18){
						String tollId=cheDao.substring(0, 14);
						if(mapToll.containsKey(tollId)){
							String stationName=mapToll.get(tollId);
							mapCheDaoTollName.put(cheDao, stationName);
						}
					}
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
//		for(String key:mapCheDaoTollName.keySet()){
//			System.out.println(key+","+mapCheDaoTollName.get(key));
//		}
		return mapCheDaoTollName;
	}
	
	public static void matchStationName(String in){
		int count=0;
		File fileIn=new File(in);
		List<String> list=Arrays.asList(fileIn.list());
		for(int i=0;i<list.size();i++){
			String inStation=in+"/"+list.get(i);
			File fileInStation=new File(inStation);
			if(fileInStation.isDirectory()){
				count++;
				System.out.println(inStation);
			}
		}
	}
	public static void main(String[] args){
		matchStationName("G:/地图/输出数据");
	}
}
