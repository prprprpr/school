package bs;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class tencentMap {
	private static final int CONNECTION_TIMEOUT=3000;
	private static final int READ_TIMEOUT=5000;
	private static final String ENCODE_CHARSET="UTF-8";
	
	public static String postRequest(String reqUrl,String... params){
		StringBuilder resultData=new StringBuilder();
		URL url=null;
		try{
			url=new URL(reqUrl);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}
		HttpURLConnection urlConn=null;
		InputStreamReader in=null;
		BufferedReader buffer=null;
		String inputLine=null;
		DataOutputStream out=null;
		if(url!=null){
			try{
				urlConn=(HttpURLConnection) url.openConnection();
				urlConn.setDoInput(true);
				urlConn.setDoOutput(true);
				urlConn.setRequestMethod("POST");
				urlConn.setUseCaches(false);
				urlConn.setConnectTimeout(CONNECTION_TIMEOUT);
				urlConn.setReadTimeout(READ_TIMEOUT);
				urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				urlConn.setRequestProperty("Charset", ENCODE_CHARSET);
				String param=sendPostParams(params);
				urlConn.setRequestProperty("Connection", "Keep-Alive");
				urlConn.connect();
				if(!"".equals(params)){
					out=new DataOutputStream(urlConn.getOutputStream());
					out.writeBytes(param);
					out.flush();
					out.close();
				}
				in=new InputStreamReader(urlConn.getInputStream(),ENCODE_CHARSET);
				buffer=new BufferedReader(in);
				if(urlConn.getResponseCode()==200){
					while((inputLine=buffer.readLine())!=null){
						resultData.append(inputLine);
					}
				}
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				try{
					if(buffer!=null){
						buffer.close();
					}
					if(in!=null){
						in.close();
					}
					if(urlConn!=null){
						urlConn.disconnect();
					}
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		return resultData.toString();
	}
	
	 
	private static String sendPostParams(String...params){
		StringBuilder sbd=new StringBuilder("");
		if(params!=null&&params.length>0){
			for(int i=0;i<params.length;i++){
				String[] temp=params[i].split(":");
				sbd.append(temp[0]);
				sbd.append("=");
				sbd.append(urlEncode(temp[1]));
				sbd.append("&");
			}
			sbd.setLength(sbd.length()-1);
		}
		return sbd.toString();
	}
	
	
	private static String getRequest(String httpUrl,String...params){
		StringBuilder resultData=new StringBuilder();
		URL url=null;
		try{
			String paramUrl=sendGetParams(httpUrl,params);
			url=new URL(paramUrl);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}
		HttpURLConnection urlConn=null;
		InputStreamReader in=null;
		BufferedReader buffer=null;
		String inputLine=null;
		if(url!=null){
			try{
				urlConn=(HttpURLConnection) url.openConnection();
				urlConn.setRequestMethod("GET");
				urlConn.setConnectTimeout(CONNECTION_TIMEOUT);
				in=new InputStreamReader(urlConn.getInputStream(),ENCODE_CHARSET);
				buffer=new BufferedReader(in);
				if(urlConn.getResponseCode()==200){
					while((inputLine=buffer.readLine())!=null){
						resultData.append(inputLine);
					}
				}
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				try{
					if(buffer!=null){
						buffer.close();
					}
					if(in!=null){
						in.close();
					}
					if(urlConn!=null){
						urlConn.disconnect();
					}
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		 return resultData.toString();  
	}
	
	private static String sendGetParams(String reqUrl,String...params){
		StringBuilder sbd=new StringBuilder(reqUrl);
		if(params!=null&&params.length>0){
			if(isExist(reqUrl,"?")){
				sbd.append("&");
			}else{
				sbd.append("?");
			}
			for(int i=0;i<params.length;i++){
				String[] temp=params[i].split(":");
				sbd.append(temp[0]);
				sbd.append("=");
				sbd.append(urlEncode(temp[1]));
				sbd.append("&");
			}
			sbd.setLength(sbd.length()-1);
		}
		return sbd.toString();
	}
	
	private static boolean isExist(String str,String fstr){
		return str.indexOf(fstr)==-1?false:true;
	}
	
	private static String urlEncode(String source){
		String result=source;
		try{
			result=URLEncoder.encode(source,ENCODE_CHARSET);
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static JSONObject getLocationInfo(String lat,String lng){
		String url="https://apis.map.qq.com/ws/geocoder/v1/?location="+lat+","+lng+"&get_poi=1&key=Q4NBZ-2WVKJ-F3DFC-KK32A-SKRE5-PIF4U&coord_type=2";
		JSONObject obj=JSONObject.fromObject(getRequest(url));
		return obj;
	}
	public static void main(String[] args){
		JSONArray pois=getLocationInfo("29.603562003138","106.55886572983").getJSONObject("result").getJSONArray("pois");
		for(int i=0;i<pois.size();i++){
			JSONObject obj=pois.getJSONObject(i);
			System.out.println(obj);
		}
	}
}
