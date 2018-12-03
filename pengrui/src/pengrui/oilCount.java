package pengrui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class oilCount {
	private static String in1="F:/新建文件夹/out";
	private static String out1="F:/新建文件夹/fuelCount";
	private static String driver="com.mysql.jdbc.Driver";
	private static String dbName="pr";
	private static String userName="root";
	private static String password="";
	private static String url="jdbc:mysql://localhost:3306/"+dbName;
	
	public static void presentTime(){
		Date startTime=new Date(System.currentTimeMillis());
		SimpleDateFormat sdFormatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String retStrFormatNowDate = sdFormatter.format(startTime);
		System.out.println(retStrFormatNowDate);
	}
	
	public static double getOilNotRedLight(double v){
		return -5.76*(Math.log(v))+30.27;
	}
	
	public static double getOilRedLight(double v){
		return (Math.pow(v, -0.553))*57.15;
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
	
	public static void fuelCount3(String path,String outPath){
		File file=new File(path);
		File fileOut=new File(outPath);
		int count=0;
		int amount=0;
		Map<String,HashMap<String,Integer>> map=new HashMap<>();
		List<String> list=Arrays.asList(file.list());
		for(int i=0;i<list.size();i++){
			File file1=new File(path+"/"+list.get(i));
			File file2=new File(outPath+"/"+list.get(i));
			try{
				String encoding="UTF-8";
				String encoding1="GBK";
				InputStreamReader in=new InputStreamReader(new FileInputStream(file1),encoding1);
				BufferedReader reader=new BufferedReader(in);
				
				OutputStreamWriter write=new OutputStreamWriter(new FileOutputStream(file2),encoding1);
				BufferedWriter writer=new BufferedWriter(write);
				
				String line="";
				while((line=reader.readLine())!=null){
					amount++;
					String[] data=line.split(",");
					if(data.length>5){
						count++;
						String distance=data[2];
						String time=data[3];
						String isRedLight=data[4];
						DecimalFormat df= new DecimalFormat("#.00");   
						double a=Double.parseDouble(distance);
						double b=Double.parseDouble(time);
						double v=(a/1000)/(b/3600);
						String fuel="";
						if(isRedLight.equals("0")){
							fuel=df.format(getOilNotRedLight(v));
						}else{
							fuel=df.format(getOilRedLight(v));
						}
						String midPoint=data[7]+","+data[8];
						if(!fuel.contains("-")){
							writer.write(data[1]+","+fuel+","+midPoint+"\n");
							writer.flush();
						}
					}
				}
				reader.close();
				writer.close();
				System.out.println("write "+outPath+"/"+list.get(i)+" finish!");
			}catch(Exception e){
				System.out.println("读取文件出错");
				e.printStackTrace();
			}
		}
		System.out.println("总条数："+amount);
		System.out.println("筛选出的总条数："+count);
	}
	
	private static void jdbc(){
		String sql="insert into user(userName,password) values('gg','1234')";
//		String sql="delete from user where userName='gg'";
		try{
			Class.forName(driver);
			Connection conn=DriverManager.getConnection(url,userName,password);
			PreparedStatement ps=conn.prepareStatement(sql);
			int rows=ps.executeUpdate(sql);
			System.out.println(rows);
//			ResultSet rs=ps.executeQuery();
//			while(rs.next()){
//				System.out.println("id:"+rs.getInt(1)+" name:"+rs.getString(2)+" password:"+rs.getString(3));
//			}
//			if(rs!=null){
//				try{
//					rs.close();
//				}catch (SQLException e) {
//                    e.printStackTrace();
//                }
//			}
			if(ps!=null){
				try{
					ps.close();
				}catch (SQLException e) {
                    e.printStackTrace();
                }
			}
			if(conn!=null){
				try{
					conn.close();
				}catch (SQLException e) {
                    e.printStackTrace();
                }
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		presentTime();
//		fuelCount3(in1,out1);
		jdbc();
		presentTime();
	}
}
