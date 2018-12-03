package bss;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import bs.readData;

public class tollStation {
	private String inTollSquare="G:/地图/收费广场.xlsx";
	private String inCheDao="G:/地图/shoufeichedao.csv";
	private Map<String,String> mapSquareToChedao=new HashMap<>();
	private Map<String,String> mapCheDaoGps=new HashMap<>();;
	
	public tollStation(){
	}
	public String getInTollSquare() {
		return inTollSquare;
	}

	public void setInTollSquare(String inTollSquare) {
		this.inTollSquare = inTollSquare;
	}

	public String getInCheDao() {
		return inCheDao;
	}

	public void setInCheDao(String inCheDao) {
		this.inCheDao = inCheDao;
	}
	public Map<String, String> getMapCheDaoGps() {
		return mapCheDaoGps;
	}
	public void setMapCheDaoGps(Map<String, String> mapCheDaoGps) {
		this.mapCheDaoGps = mapCheDaoGps;
	}
	public Map<String, String> getMapSquareToChedao() {
		return mapSquareToChedao;
	}
	public void setMapSquareToChedao(Map<String, String> mapSquareToChedao) {
		this.mapSquareToChedao = mapSquareToChedao;
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
	
	public void readTollSquare(String inPath){
		Map<String,String> map=new HashMap<>();
		boolean isXlsx=false;
		if(inPath.endsWith("xlsx")){
			isXlsx=true;
		}
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			InputStream input=new FileInputStream(inPath);
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
					String id=getValue(row.getCell(0));
					String province=getValue(row.getCell(13));
					String lat=getValue(row.getCell(6));
					String lng=getValue(row.getCell(7));
					String name=getValue(row.getCell(10));
					if(!lat.equals("")&&!lng.equals("")){
						double[] point=wgsToGcj.bd09_To_Gcj02(Double.parseDouble(lat), Double.parseDouble(lng));
						double latD=point[0];
						double lngD=point[1];
						if(province.equals("HE_BEI")){
							mapSquareToChedao.put(id, latD+","+lngD);
						}
					}
				}
			}
			wb.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void readCheDao(String in){
		BufferedReader reader=topologyV2.getReader(in, "GBK");
		try{
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",",15);
				String cheDao=data[0].replaceAll("\"", "").trim();
				String province=data[7].replaceAll("\"", "").trim();
				if(province.equals("HE_BEI")){
					if(cheDao.length()>18){
						String squareId=cheDao.substring(0, 18);
						if(mapSquareToChedao.containsKey(squareId)){
							mapCheDaoGps.put(cheDao, mapSquareToChedao.get(squareId));
						}
					}
				}
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
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
	public static void main(String[] args){
		String tollStation="G:/地图/收费站.xlsx";
		tollStation t=new tollStation();
		t.getCheDaoToll(tollStation, t.getInCheDao());
	}
}
