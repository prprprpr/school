package pengrui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class mok {
	private static final double EARTH_RADIUS = 6378137;  
	private static final double bh_Lng = 116.35347;  
	private static final double bh_Lat = 39.986069;  
	 
    public static String connectURL(String dest_url, String commString) {  
        String rec_string = "";  
        URL url = null;  
        HttpURLConnection urlconn = null;  
        OutputStream out = null;  
        BufferedReader rd = null;  
        try {  
            url = new URL(dest_url);  
            urlconn = (HttpURLConnection) url.openConnection();  
            urlconn.setReadTimeout(1000 * 30);  
            //urlconn.setRequestProperty("content-type", "text/html;charset=UTF-8");  
            urlconn.setRequestMethod("POST");  
            urlconn.setDoInput(true);   
            urlconn.setDoOutput(true);  
            out = urlconn.getOutputStream();  
            out.write(commString.getBytes("UTF-8"));  
            out.flush();   
            out.close();  
            rd = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));  
            StringBuffer sb = new StringBuffer();  
            int ch;  
            while ((ch = rd.read()) > -1)  
                sb.append((char) ch);  
            rec_string = sb.toString();  
        } catch (Exception e) { 
        	e.printStackTrace();
            return "";  
        } finally {  
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if (urlconn != null) {  
                    urlconn.disconnect();  
                }  
                if (rd != null) {  
                    rd.close();  
                }  
            } catch (Exception e) {  
                e.printStackTrace(); 
            }  
        }  
        return rec_string;  
    }  
    
    private static double rad(double d){  
       return d * Math.PI / 180.0;  
    }  
	
	public static BufferedReader getReader(String in,String encoding){
		File file=new File(in);
		BufferedReader reader=null;
		try{
			InputStreamReader input=new InputStreamReader(new FileInputStream(file),encoding);
			reader=new BufferedReader(input);
		}catch(Exception e){
			e.printStackTrace();
		}
		return reader;
	}
	
	public static BufferedWriter getWriter(String out,String encoding){
		File file=new File(out);
		BufferedWriter writer=null;
		try{
			OutputStreamWriter output=new OutputStreamWriter(new FileOutputStream(file),encoding);
			writer=new BufferedWriter(output);
		}catch(Exception e){
			e.printStackTrace();
		}
		return writer;
	}
	public static double getDistance(double lng1, double lat1, double lng2, double lat2){
		double radLat1 = rad(lat1);  
        double radLat2 = rad(lat2);  
        double a = radLat1 - radLat2;  
        double b = rad(lng1) - rad(lng2);  
        double s = 2 * Math.asin(  
	        Math.sqrt(  
	            Math.pow(Math.sin(a/2),2)   
	            + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)  
	        )  
        );  
        s = s * EARTH_RADIUS;  
        s = Math.round(s * 10000) / 10000;  
        return s;  
	}  
	
	public static void getCoord(String coords,BufferedWriter writer) throws JSONException, IOException{
		String result =connectURL("http://api.map.baidu.com/geoconv/v1/?coords="+coords+"&from=3&to=5&output=json&ak=vjZKQU5ZiApmNtyPQgxjfnl1Hl3EG8vh","");
		JSONObject ob=new JSONObject(result);
		JSONArray ja=ob.getJSONArray("result");
		for(int i=0;i<ja.length();i++){
			JSONObject obIn=ja.getJSONObject(i);
			double lng=Double.parseDouble(obIn.getString("x"));
			double lat=Double.parseDouble(obIn.getString("y"));
//			if(getDistance(lng,lat,bh_Lng,bh_Lat)<1000){
				writer.write("{\"coord\":["+lng+","+lat+"],\"elevation\":13},"+"\n");
//			}
		}
	}
	
	public static void write(String in,String out){
		BufferedReader reader=getReader(in,"GBK");
		BufferedWriter writer=getWriter(out,"GBK");
//		BufferedWriter writer2=getWriter(out2,"GBK");
		try{
			String line="";
			reader.readLine();
			writer.write("data=[["+"\n");
//			writer2.write("data=[["+"\n");
			int i=0;
			String coords="";
			while((line=reader.readLine())!=null&&i<1000){
				String[] data=line.split(",");
				String startLng=data[7].replaceAll("\"", "");
				String startLat=data[8].replaceAll("\"", "");
				if(coords.length()==0||coords.split(";").length==100){
					if(coords.split(";").length==100){
						getCoord(coords,writer);
						coords="";
					}
					coords+=startLng+","+startLat;
				}else{
					coords+=";"+startLng+","+startLat;
				}
			}
			if(!coords.equals("")){
				getCoord(coords,writer);
			}
			reader.close();
			writer.write("]]"+"\n");
			writer.flush();
			writer.close();
//			writer2.write("]]"+"\n");
//			writer2.flush();
//			writer2.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void getBh(String in,String outBh,String outBh2){
		BufferedReader reader=getReader(in,"GBK");
		BufferedWriter writer=getWriter(outBh,"GBK");
		BufferedWriter writer2=getWriter(outBh2,"GBK");
		try{
			String line="";
			reader.readLine();
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				double startLng=Double.parseDouble(data[7].replaceAll("\"", ""));
				double startLat=Double.parseDouble(data[8].replaceAll("\"", ""));
				double startLng2=Double.parseDouble(data[18].replaceAll("\"", ""));
				double startLat2=Double.parseDouble(data[19].replaceAll("\"", ""));
				if(getDistance(startLng,startLat,bh_Lng,bh_Lat)<1000){
					writer.write(startLng+","+startLat+"\n");
					writer2.write(startLng2+","+startLat2+"\n");
				}
			}
			reader.close();
			writer.flush();
			writer.close();
			writer2.flush();
			writer2.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void writeBh(String in,String out,String out2){
		BufferedReader reader=getReader(in,"GBK");
		BufferedWriter writer=getWriter(out,"GBK");
		BufferedWriter writer2=getWriter(out2,"GBK");
		try{
			String line="";
			writer.write("data=[["+"\n");
			writer2.write("data=[["+"\n");
			String coords="";
			while((line=reader.readLine())!=null){
				String[] data=line.split(",");
				String startLng=data[0];
				String startLat=data[1];
				if(coords.length()==0||coords.split(";").length==100){
					if(coords.split(";").length==100){
						getCoord(coords,writer);
						coords="";
					}
					coords+=startLng+","+startLat;
				}else{
					coords+=";"+startLng+","+startLat;
				}
				writer2.write("{\"coord\":["+startLng+","+startLat+"],\"elevation\":13},"+"\n");
			}
			if(!coords.equals("")){
				getCoord(coords,writer);
			}
			reader.close();
			writer.write("]]"+"\n");
			writer.flush();
			writer.close();
			writer2.write("]]"+"\n");
			writer2.flush();
			writer2.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		String in="F:/共享单车数据质量监测/mok-order-20180116.csv";
		String outBh="F:/共享单车数据质量监测/outBh.csv";
		String outBh2="F:/共享单车数据质量监测/outBh2.csv";
		String out="E:/echarts/start.json";
		String outBhBd="E:/echarts/mok/outBhBd.json";
		String outBhOri="E:/echarts/mok/outBhOri.json";
		String outBhBd2="E:/echarts/mok/outBhBd2.json";
		String outBhOri2="E:/echarts/mok/outBhOri2.json";
		
		
//		getBh(in,outBh,outBh2);
		writeBh(outBh2,outBhBd2,outBhOri2);
//		write(in,out);
	}
}
