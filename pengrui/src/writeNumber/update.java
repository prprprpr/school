package writeNumber;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class update {
	public static String url="jdbc:mysql://182.92.104.216:3306/mall_analytics?useSSL=false";
	public static String urlTest="jdbc:mysql://182.92.104.216:3306/test_mall_analytics?useSSL=false";
	public static String name="com.mysql.jdbc.Driver";
	public static String user="root";
	public static String password="bus";
	public static String in="C:/Users/pengrui/Desktop/data.txt";
	
	public Connection conn=null;
	public Statement stmt =null;
	
	public update(){
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
	
	public static void update_analyse_stay(update db,int unrepeat_count,int repeat_count,int stay_count,int avg_stay_time,int id) throws SQLException{
		String sql="update analyse_stay set unrepeat_count="+unrepeat_count+",repeat_count="+repeat_count+",stay_count="+stay_count+",avg_stay_time="+avg_stay_time+" where id='"+id+"'";
		int ret=db.stmt.executeUpdate(sql);
		if(ret!=1){
			System.out.println("insert_analyse_stay err!");
		}
	}
	
	public static void update_analyse_stay_ext(update db,int range_5_30,int range_10_30,int range_30_60,int range_60_120,int range_120_180,int gt_5,int gt_10,int gt_60,int gt_120,int gt_180,int lt_5,int lt_10,int lt_60,int id) throws SQLException{
		String sql="update analyse_stay_ext set range_5_30="+range_5_30+",range_10_30="+range_10_30+",range_30_60="+range_30_60+",range_60_120="+range_60_120+",range_120_180="+range_120_180+",gt_5="+gt_5+",gt_10="+gt_10+",gt_60="+gt_60+",gt_120="+gt_120+",gt_180="+gt_180+",lt_5="+lt_5+",lt_10="+lt_10+",lt_60="+lt_60+" where id='"+id+"'";
		int ret=db.stmt.executeUpdate(sql);
		if(ret!=1){
			System.out.println("insert_analyse_stay_ext err!");
		}
	}
	
	public static float getPersent(){
		Date startTime=new Date(System.currentTimeMillis());
		SimpleDateFormat sdFormatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String presentTime = sdFormatter.format(startTime);
		String hour=presentTime.substring(11,13);
		String minute=presentTime.substring(14,16);
		Map<String,Float> map=new HashMap<>();
		map.put("06:00", (float) 0.010);
		map.put("06:15", (float) 0.020);
		map.put("06:30", (float) 0.035);
		map.put("06:45", (float) 0.05);
		map.put("07:00", (float) 0.07);
		map.put("07:15", (float) 0.09);
		map.put("07:30", (float) 0.110);
		map.put("07:45", (float) 0.1350);
		map.put("08:00", (float) 0.160);
		map.put("08:15", (float) 0.1850);
		map.put("08:30", (float) 0.210);
		map.put("08:45", (float) 0.2350);
		map.put("09:00", (float) 0.2550);
		map.put("09:15", (float) 0.2750);
		map.put("09:30", (float) 0.290);
		map.put("09:45", (float) 0.3050);
		map.put("10:00", (float) 0.320);
		map.put("10:15", (float) 0.3320);
		map.put("10:30", (float) 0.3440);
		map.put("10:45", (float) 0.3560);
		map.put("11:00", (float) 0.3680);
		map.put("11:15", (float) 0.380);
		map.put("11:30", (float) 0.3920);
		map.put("11:45", (float) 0.4040);
		map.put("12:00", (float) 0.4160);
		map.put("12:15", (float) 0.4310);
		map.put("12:30", (float) 0.4460);
		map.put("12:45", (float) 0.4610);
		map.put("13:00", (float) 0.4730);
		map.put("13:15", (float) 0.4850);
		map.put("13:30", (float) 0.4970);
		map.put("13:45", (float) 0.5090);
		map.put("14:00", (float) 0.5210);
		map.put("14:15", (float) 0.5330);
		map.put("14:30", (float) 0.5450);
		map.put("14:45", (float) 0.5570);
		map.put("15:00", (float) 0.5690);
		map.put("15:15", (float) 0.5810);
		map.put("15:30", (float) 0.5930);
		map.put("15:45", (float) 0.6050);
		map.put("16:00", (float) 0.6170);
		map.put("16:15", (float) 0.6290);
		map.put("16:30", (float) 0.6410);
		map.put("16:45", (float) 0.6560);
		map.put("17:00", (float) 0.6710);
		map.put("17:15", (float) 0.6910);
		map.put("17:30", (float) 0.7110);
		map.put("17:45", (float) 0.7310);
		map.put("18:00", (float) 0.7560);
		map.put("18:15", (float) 0.7810);
		map.put("18:30", (float) 0.8060);
		map.put("18:45", (float) 0.8260);
		map.put("19:00", (float) 0.8460);
		map.put("19:15", (float) 0.8610);
		map.put("19:30", (float) 0.8760);
		map.put("19:45", (float) 0.8880);
		map.put("20:00", (float) 0.900);
		map.put("20:15", (float) 0.910);
		map.put("20:30", (float) 0.920);
		map.put("20:45", (float) 0.930);
		map.put("21:00", (float) 0.940);
		map.put("21:15", (float) 0.950);
		map.put("21:30", (float) 0.960);
		map.put("21:45", (float) 0.970);
		map.put("22:00", (float) 0.980);
		map.put("22:15", (float) 0.9850);
		map.put("22:30", (float) 0.990);
		map.put("22:45", (float) 0.9950);
		map.put("23:00", (float) 1);
		map.put("23:15", (float) 1);
		map.put("23:30", (float) 1);
		map.put("23:45", (float) 1);
		int s=Integer.parseInt(minute);
		String key="";
		if(s<15){
			key=hour+":00";
		}else if(s<30){
			key=hour+":15";
		}else if(s<45){
			key=hour+":30";
		}else{
			key=hour+":45";
		}
		if(map.containsKey(key)){
			return map.get(key);
		}else{
			return 0;
		}
	}
	
	public static int getRandom(int min, int max){
	    Random random = new Random();
	    int s = random.nextInt(max) % (max - min + 1) + min;
 	    return s;
	}
	public static void update_data() throws SQLException, ParseException{
		Date startTime=new Date(System.currentTimeMillis());
		SimpleDateFormat sdFormatter=new SimpleDateFormat("yyyy-MM-dd");
		String presentTime = sdFormatter.format(startTime);
		String twoYears="2020"+presentTime.substring(4,10);
		
		String sql="select * from analyse_stay where data_time like '%"+twoYears+"%'";
		update db=new update();
		ResultSet rs=db.stmt.executeQuery(sql);
		update db1=new update();
		while(rs.next()){
			int id=rs.getInt("id");
			int b_id=rs.getInt("b_id");
			int g_id=rs.getInt("g_id");
			int avg_stay_time=0;
			if(g_id==9||(g_id==1&&b_id==4)){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String pd=sdf.format(startTime);
				if(Integer.parseInt(pd.substring(11,13))<18){
					String d=pd.substring(0,10)+" 08:30:00";
					avg_stay_time=(int) ((sdf.parse(pd).getTime()-sdf.parse(d).getTime())/1000);
				}else{
					String d=pd.substring(0,10)+" 08:30:00";
					String d1=pd.substring(0,10)+" 18:00:00";
					avg_stay_time=(int) ((sdf.parse(d1).getTime()-sdf.parse(d).getTime())/1000);
				}
			}else{
				int avg=rs.getInt("avg_stay_time");
				avg_stay_time=avg+(int)(avg*getRandom(-5,5)*0.01);
			}
			int unrepeat_count=(int) (rs.getInt("unrepeat_count")*getPersent());
			int repeat_count=(int) (rs.getInt("unrepeat_count")*getPersent());
			int stay_count=(int) (rs.getInt("stay_count")*getPersent());
			update_analyse_stay(db1,unrepeat_count,repeat_count,stay_count,avg_stay_time,id-23);
		}
		
		String sql_ext="select * from analyse_stay_ext where data_time like '%"+twoYears+"%'";
		ResultSet rs_ext=db.stmt.executeQuery(sql_ext);
		while(rs_ext.next()){
			int id=rs_ext.getInt("id");
			int range_5_30=(int) (rs_ext.getInt("range_5_30")*getPersent());
			int range_10_30=(int) (rs_ext.getInt("range_10_30")*getPersent());
			int range_30_60=(int) (rs_ext.getInt("range_30_60")*getPersent());
			int range_60_120=(int) (rs_ext.getInt("range_60_120")*getPersent());
			int range_120_180=(int) (rs_ext.getInt("range_60_120")*getPersent());
			int gt_5=(int) (rs_ext.getInt("gt_5")*getPersent());
			int gt_10=(int) (rs_ext.getInt("gt_10")*getPersent());
			int gt_60=(int) (rs_ext.getInt("gt_60")*getPersent());
			int gt_120=(int) (rs_ext.getInt("gt_120")*getPersent());
			int gt_180=(int) (rs_ext.getInt("gt_180")*getPersent());
			int lt_5=(int) (rs_ext.getInt("lt_5")*getPersent());
			int lt_10=(int) (rs_ext.getInt("lt_10")*getPersent());
			int lt_60=(int) (rs_ext.getInt("lt_60")*getPersent());
			update_analyse_stay_ext(db1,range_5_30,range_10_30,range_30_60,range_60_120,range_120_180,gt_5,gt_10,gt_60,gt_120,gt_180,lt_5,lt_10,lt_60,id-23);
		}		
		db.close();
		db1.close();
	}
}
