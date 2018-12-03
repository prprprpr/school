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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class yueBao {
	private static String in="H:/月报/全国收费站匹配（补充）1209.xlsx";
	private static String in2="H:/月报/所有收费站（添加省代码）.csv";
	private static String out="H:/月报/收费站对应经纬度.csv";
	
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
	
	public static void read(String path,String path2,String outPath){
		File file=new File(path2);
		File fileOut=new File(outPath);
		Map<String,String> map=new HashMap<>();
		boolean isXlsx=false;
		if(path.endsWith("xlsx")){
			isXlsx=true;
		}
		try{
			String encoding="UTF-8";
			String encoding1="GBK";
			InputStreamReader in=new InputStreamReader(new FileInputStream(file),encoding1);
			BufferedReader reader=new BufferedReader(in);
			
			OutputStreamWriter out=new OutputStreamWriter(new FileOutputStream(fileOut),encoding1);
			BufferedWriter writer=new BufferedWriter(out);
			int amount=0;
			int count=0;
			String line="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				String id=data[0];
				String point=data[2]+","+data[3];
				map.put(id, point);
			}
			reader.close();
			System.out.println("write map finish!");
			InputStream input=new FileInputStream(path);
			Workbook wb=null;
			if(isXlsx){
				wb=new XSSFWorkbook(input);
			}else{
				wb=new HSSFWorkbook(input);
			}
			Sheet sheet=wb.getSheetAt(0);
			for(int rowNum=0;rowNum<=sheet.getLastRowNum();rowNum++){
				Row row=sheet.getRow(rowNum);
				if(row!=null){
					String id=getValue(row.getCell(0));
					String name=getValue(row.getCell(1));
					String stationId="";
					Cell cell=row.getCell(3);
					if(cell!=null&&("")!=cell.toString()){
						stationId=getValue(row.getCell(3));
						if(map.containsKey(stationId)){
							String point=map.get(stationId);
//							System.out.println(stationId+","+point);
							String city=getCity(point);
							amount++;
							if(city.equals("")){
								count++;
							}
							writer.write(id+","+name+","+stationId+","+point+","+city+"\n");
							writer.flush();
						}else{
							writer.write(id+","+name+","+stationId+","+""+","+""+"\n");
							writer.flush();
						}
					}else{
						writer.write(id+","+name+","+stationId+","+""+","+""+"\n");
						writer.flush();
					}
				}
			}
			writer.close();
			System.out.println("finish!");
			System.out.println("amount:"+amount);
			System.out.println("count:"+count);
		}catch(Exception e){
			System.out.println("错误");
			e.printStackTrace();
		}
	}
	public static String getCity(String point){
		String[] a=point.split(",");
		String lng=a[0];
		String lat=a[1];
		String newPoint=baiduMap.coordinateChange(lat, lng);
		String[] b=newPoint.split(",");
		String newLng=b[0];
		String newLat=b[1];
		if(newLng.equals("0")&&newLat.equals("0")){
			return "";
		}else{
			String city=baiduMap.getCity(newLat, newLng);
			return city;
		}
	}
	public static void main(String[] args) throws IOException{
        read(in,in2,out);
	}
}
