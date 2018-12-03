package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class poiApi {
	private static final int CONNECTION_TIMEOUT=3000;
	private static final int READ_TIMEOUT=5000;
	private static final String ENCODE_CHARSET="UTF-8";
	
	public static String postRequest(String reqUrl,JSONObject obj){
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
				urlConn.setRequestProperty("Content-Type", "application/json");
				urlConn.setRequestProperty("Charset", ENCODE_CHARSET);
//				String param=sendPostParams(params);
//				urlConn.setRequestProperty("Connection", "Keep-Alive");
//				urlConn.connect();
//				if(!"".equals(params)){
//					out=new DataOutputStream(urlConn.getOutputStream());
//					out.writeBytes(param);
//					out.flush();
//					out.close();
//				}
				out=new DataOutputStream(urlConn.getOutputStream());
				out.write((obj.toString()).getBytes());
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
	
	public static JSONObject getLocationInfo(String keyWords,int page) throws UnsupportedEncodingException{
		String url="https://restapi.amap.com/v3/place/text";
		JSONObject obj=JSONObject.fromObject(getRequest(url,"key:05acdb432e4a229b1ce8b8f76cb2a699","keywords:"+keyWords,"city:重庆","offset:25","page:"+page));
		return obj;
	}
	public static void getPoi(String keyWords,String out) throws UnsupportedEncodingException{
		BufferedWriter writer=io.getWriter(out+"/"+keyWords+".csv", "gbk");
		Map<String,String> map=new HashMap<>();
		try{
			int page=1;
			JSONObject message=getLocationInfo(keyWords,page);
			int count=Integer.valueOf(message.getString("count"));
			System.out.println(count);
			JSONArray pois=message.getJSONArray("pois");
			for(int i=0;i<pois.size();i++){
				JSONObject obj=pois.getJSONObject(i);
				String id=obj.getString("id");
				String name=obj.getString("name");
				String location=obj.getString("location");
				map.put(location, id+","+name);
			}
			while(page*20<count){
				page++;
				message=getLocationInfo(keyWords,page);
				pois=message.getJSONArray("pois");
				for(int i=0;i<pois.size();i++){
					JSONObject obj=pois.getJSONObject(i);
					String id=obj.getString("id");
					String name=obj.getString("name");
					String location=obj.getString("location");
					map.put(location, id+","+name);
				}
			}
			for(String key:map.keySet()){
				writer.write(key+","+map.get(key)+"\n");
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static JSONObject getOneObj(ArrayList<String> lngAndLatList){
		JSONObject jObject=new JSONObject();
		JSONArray jArray=new JSONArray();
		String originUrl="/v3/place/around";
		for(int i=0;i<lngAndLatList.size();i++){
			JSONObject obj=new JSONObject();
			String lngAndLat=lngAndLatList.get(i);
			String url=sendGetParams(originUrl,"offset:10","key:05acdb432e4a229b1ce8b8f76cb2a699","location:"+lngAndLat,"output:json","radius:1000","types:物流|工厂|市场");
			obj.put("url", url);
			jArray.add(obj);
		}
		jObject.put("ops", jArray);
		return jObject;
	}
	public static void getStopPoi(){
		String requestUrl="https://restapi.amap.com/v3/batch?key=05acdb432e4a229b1ce8b8f76cb2a699";
		ArrayList<String> list=new ArrayList<>();
		list.add("106.5575578956355,29.718294618872566");
		list.add("106.5575578956355,29.718294618872566");
		JSONObject obj=getOneObj(list);
		JSONArray rsArray=JSONArray.fromObject(postRequest(requestUrl,obj));
		for(int i=0;i<rsArray.size();i++){
			JSONArray pois=rsArray.getJSONObject(i).getJSONObject("body").getJSONArray("pois");
			if(pois.size()>0){
				JSONObject poi=pois.getJSONObject(0);
				String name=poi.getString("name");
				String location=poi.getString("location");
				System.out.println(name+","+location);
			}
		}
		System.out.println(rsArray);
	}
	public static void main(String[] args) throws UnsupportedEncodingException{
		String out="D:/重庆物流地点";
//		getPoi("物流",out);
		getStopPoi();
	}
}
