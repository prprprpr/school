package writeNumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class mysqlDatabase {
	public static String url="jdbc:mysql://182.92.104.216:3306/mall_analytics?useSSL=false";
	public static String urlTest="jdbc:mysql://182.92.104.216:3306/test_mall_analytics?useSSL=false";
	public static String name="com.mysql.jdbc.Driver";
	public static String user="root";
	public static String password="bus";
	public static String in="C:/Users/pengrui/Desktop/data.txt";
	
	public Connection conn=null;
	public Statement stmt =null;
	
	public mysqlDatabase(){
		try{
			Class.forName(name);
			conn=DriverManager.getConnection(url, user, password);
			stmt = conn.createStatement();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void close(){
		try{
			this.conn.close();  
            this.stmt.close(); 
		}catch (SQLException e) {  
            e.printStackTrace();  
        }  
	}
	
	public static void insert_analyse_stay(mysqlDatabase db,int id,int g_id,int b_id,String data_time,int unrepeat_count,int repeat_count,int stay_count,int avg_stay_time,String analy_time) throws SQLException{
		String sql="insert into "+"analyse_stay"+"(id,g_id,b_id,data_time,unrepeat_count,repeat_count,stay_count,avg_stay_time,analy_time) values('"+id+"','"+g_id+"','"+b_id+"','"+data_time+"','"+unrepeat_count+"','"+repeat_count+"','"+stay_count+"','"+avg_stay_time+"','"+analy_time+"')";
		int ret=db.stmt.executeUpdate(sql);
		if(ret!=1){
			System.out.println("insert_analyse_stay err!");
		}
	}
	
	public static void insert_analyse_stay_ext(mysqlDatabase db,int id,int g_id,int b_id,String data_time,int range_5_30,int range_10_30,int range_30_60,int range_60_120,int range_120_180,int gt_5,int gt_10,int gt_60,int gt_120,int gt_180,int lt_5,int lt_10,int lt_60,int analy_id) throws SQLException{
		String sql="insert into "+"analyse_stay_ext"+"(id,g_id,b_id,data_time,range_5_30,range_10_30,range_30_60,range_60_120,range_120_180,gt_5,gt_10,gt_60,gt_120,gt_180,lt_5,lt_10,lt_60,analy_id) values('"+id+"','"+g_id+"','"+b_id+"','"+data_time+"',"
				+ "'"+range_5_30+"','"+range_10_30+"','"+range_30_60+"','"+range_60_120+"','"+range_120_180+"','"+gt_5+"','"+gt_10+"','"+gt_60+"','"+gt_120+"','"+gt_180+"','"+lt_5+"','"+lt_10+"','"+lt_60+"','"+analy_id+"')";
		int ret=db.stmt.executeUpdate(sql);
		if(ret!=1){
			System.out.println("insert_analyse_stay_ext err!");
		}
	}
	
	public static void deleteTable(mysqlDatabase db) throws SQLException{
		String sql="delete from analyse_stay";
		String sql1="delete from analyse_stay_ext";
		int ret=db.stmt.executeUpdate(sql);
		int ret1=db.stmt.executeUpdate(sql1);
	}
	public static void writeSql(String in) throws IOException, SQLException{
		BufferedReader reader=reasonOfNotUseEtc.getFirstEtcTime.getReader(in, "GBK");
		mysqlDatabase db=new mysqlDatabase();
		deleteTable(db);
		System.out.println("delete table success!");
		String line="";
		int i=1;
		while((line=reader.readLine())!=null){
			String[] data=line.split(",");
			if(data.length==22){
				int id=Integer.parseInt(data[0]);
				int g_id=Integer.parseInt(data[1]);
				int b_id=Integer.parseInt(data[2]);
				String data_time=data[3];
				int repeat_count=Integer.parseInt(data[4]);
				int unrepeat_count=repeat_count;
				int stay_count=Integer.parseInt(data[5]);
				int avg_stay_time=Integer.parseInt(data[6]);
				int range_5_30=Integer.parseInt(data[7]);
				int range_10_30=Integer.parseInt(data[8]);
				int range_30_60=Integer.parseInt(data[9]);
				int range_60_120=Integer.parseInt(data[10]);
				int range_120_180=Integer.parseInt(data[11]);
				int gt_5=Integer.parseInt(data[12]);
				int gt_10=Integer.parseInt(data[13]);
				int gt_60=Integer.parseInt(data[14]); 
				int gt_120=Integer.parseInt(data[15]);
				int gt_180=Integer.parseInt(data[16]);
				int lt_5=Integer.parseInt(data[17]);
				int lt_10=Integer.parseInt(data[18]);
				int lt_60=Integer.parseInt(data[19]);
				int analy_id=Integer.parseInt(data[21]);
				String analy_time=data[20];
				insert_analyse_stay(db,id, g_id, b_id, data_time,unrepeat_count, repeat_count, stay_count, avg_stay_time, analy_time);
				insert_analyse_stay_ext(db,i, g_id, b_id, data_time, range_5_30, range_10_30, range_30_60, range_60_120, range_120_180, gt_5, gt_10, gt_60, gt_120, gt_180, lt_5, lt_10, lt_60, analy_id);
			}else{
				int id=Integer.parseInt(data[0]);
				int g_id=Integer.parseInt(data[1]);
				int b_id=Integer.parseInt(data[2]);
				String data_time=data[3];
				int repeat_count=Integer.parseInt(data[4]);
				int unrepeat_count=repeat_count;
				int stay_count=Integer.parseInt(data[5]);
				int avg_stay_time=Integer.parseInt(data[6]);
				String analy_time=data[7];
				insert_analyse_stay(db,id, g_id, b_id, data_time,unrepeat_count, repeat_count, stay_count, avg_stay_time, analy_time);
			}
			i++;
		}
		reader.close();
		db.close();
	}
	public static void main(String[] args) throws SQLException, IOException{
		writeSql(in);
	}
}
