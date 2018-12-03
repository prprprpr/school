package pengrui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class huoYueDu {
	private static String ETC201703="F:/ETC/ETC2017-03";
	private static String outK="C:/Users/pengrui/Desktop/客车outETC2017-03.csv";
	private static String outH="C:/Users/pengrui/Desktop/货车outETC2017-03.csv";
	
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
	public static void read(String path,String outPathK,String outPathH){
		String encoding="UTF-8";
		String encoding1="GBK";
		File file=new File(path);
		File fileOutK=new File(outPathK);
		File fileOutH=new File(outPathH);
		List list=Arrays.asList(file.list());
		Map<String,Integer> mapK=new HashMap<>();
		Map<String,Integer> mapH=new HashMap<>();
		try{
			for(int i=0;i<list.size();i++){
				String in=path+"/"+list.get(i);
				File fileIn=new File(in);
				InputStreamReader input=new InputStreamReader(new FileInputStream(fileIn),encoding);
				BufferedReader reader=new BufferedReader(input);
				String line="";
				while((line=reader.readLine())!=null){
					String[] data=line.split(",",47);
					if(data[33]!=null){
						if(!(data[34].getBytes().length!=8&&!data[34].matches("[A-Z][A-Z][0-9]{5}")&&!data[34].contains("WJ")&&!data[34].contains("黄")&&!data[34].contains("蓝")&&!data[34].contains("警"))){
							if(!((data[20].matches("[0-9]+")&&(Float.parseFloat(data[20])<1||Float.parseFloat(data[20])>255))||(!data[20].matches("[0-9]+")))){
								String cardId=data[33];
								String license=data[34];
								String cx=data[20];
								if(Integer.parseInt(cx)<10){
									add(mapK,cardId+","+license+","+cx);	
								}else{
									add(mapH,cardId+","+license+","+cx);
								}
//								System.out.println(cardId+","+license+","+cx);
							}
						}
					}
				}
				reader.close();
				System.out.println(path+"/"+list.get(i)+" finish!");
			}
			
			OutputStreamWriter outputK=new OutputStreamWriter(new FileOutputStream(fileOutK),encoding);
			BufferedWriter writerK=new BufferedWriter(outputK);
			OutputStreamWriter outputH=new OutputStreamWriter(new FileOutputStream(fileOutH),encoding);
			BufferedWriter writerH=new BufferedWriter(outputH);
			Set setK=mapK.entrySet();
			Map.Entry[] entriesK = (Map.Entry[])setK.toArray(new Map.Entry[setK.size()]);
			for(int i=0;i<entriesK.length;i++){
				String key=entriesK[i].getKey().toString();
				String value=entriesK[i].getValue().toString();
				writerK.write(key+","+value+"\n");
				writerK.flush();
			}
			writerK.close();
			
			Set setH=mapH.entrySet();
			Map.Entry[] entriesH = (Map.Entry[])setH.toArray(new Map.Entry[setH.size()]);
			for(int i=0;i<entriesH.length;i++){
				String key=entriesH[i].getKey().toString();
				String value=entriesH[i].getValue().toString();
				writerH.write(key+","+value+"\n");
				writerH.flush();
			}
			writerH.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		read(ETC201703,outK,outH);
	}
}
