package bss;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class wgsToGcj {
	/**
	 * ��wgs84����ת��Ϊgcj02����
	 */
	final static double pi=Math.PI;
	final static double a=6378245.0;
	final static double ee=0.00669342162296594323;
	
	public static double[] transform(double wgLat, double wgLon)  
    {  
        double mgLat=0;  
        double mgLon=0;  
        if (outOfChina(wgLat, wgLon))  
        {  
            mgLat = wgLat;  
            mgLon = wgLon;  
              
        }else{  
            double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);  
            double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);  
            double radLat = wgLat / 180.0 * pi;  
            double magic = Math.sin(radLat);  
            magic = 1 - ee * magic * magic;  
            double sqrtMagic = Math.sqrt(magic);  
            dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);  
            dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);  
            mgLat = wgLat + dLat;  
            mgLon = wgLon + dLon;  
        }  
        double[] point={mgLat,mgLon};  
        return point;  
    }  
  
    private static boolean outOfChina(double lat, double lon)  
    {  
        if (lon < 72.004 || lon > 137.8347)  
            return true;  
        if (lat < 0.8293 || lat > 55.8271)  
            return true;  
        return false;  
    }  
  
    private static double transformLat(double x, double y)  
    {  
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));  
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;  
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;  
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;  
        return ret;  
    }  
  
    private static double transformLon(double x, double y)  
    {  
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));  
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;  
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;  
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;  
        return ret;  
    }
    
    public static double[] bd09_To_Gcj02(double bd_lat, double bd_lon) {  
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;  
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);  
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);  
        double gg_lon = z * Math.cos(theta);  
        double gg_lat = z * Math.sin(theta);  
        double[] point={gg_lat,gg_lon}; 
        return point;
    }  
    public static void main(String[] args){
    	BufferedReader reader=topology.getReader("F:/�����켣����/�����켣/trace1.csv", "GBK");
    	BufferedWriter writer=topology.getWriter("C:/Users/pengrui/Desktop/trace.csv", "GBK");
    	try{
    		String line="";
    		while((line=reader.readLine())!=null){
    			String[] data=line.split(",",5);
    			double lng=Double.parseDouble(data[1]);
    			double lat=Double.parseDouble(data[2]);
    			double[] point=transform(lat,lng);
    			writer.write(data[0]+","+point[1]+","+point[0]+","+data[3]+","+data[4]+"\n");
    		}
    		reader.close();
    		writer.flush();
    		writer.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
}