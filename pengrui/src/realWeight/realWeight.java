package realWeight;

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

public class realWeight {
	private static String tradeData="I:/2018-03";
	public static void readTrade(String in){
		File file=new File(in);
		List<String> listFile=Arrays.asList(file.list());
		Map<String,Map<String,Integer>> map=new HashMap<>();
		for(int i=0;i<listFile.size();i++){
			String path=in+"/"+listFile.get(i);
			try{
				BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
				String line="";
				while((line=reader.readLine())!=null){
					String[] data=line.split(",");
					if(data.length==26){
						String id=data[1];
						String province=id.substring(5,7);
						String weight=data[4];
						String enPlateAndColor=data[11];
						String exPlateAndColor=data[12];
						String enCx=data[14];
						String exCx=data[15];
						if(enPlateAndColor.equals(exPlateAndColor)&&enCx.equals(exCx)&&Integer.valueOf(enCx)>=11&&enPlateAndColor.length()>8&&weight.matches("[0-9]+")){
							String car=enPlateAndColor+","+enCx;
							int w=Integer.valueOf(weight);
							if(w>0){
								if(map.containsKey(province)){
									Map<String,Integer> mapCarWeight=map.get(province);
									if(!mapCarWeight.containsKey(car)){
										mapCarWeight.put(car, w);
									}else if(mapCarWeight.containsKey(car)&&w<mapCarWeight.get(car)){
										mapCarWeight.put(car, w);
									}
									map.put(province, mapCarWeight);
								}else{
									Map<String,Integer> mapCarWeight=new HashMap<>();
									mapCarWeight.put(car, w);
									map.put(province, mapCarWeight);
								}
							}
						}
					}
				}
				reader.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println(in+"/"+listFile.get(i));
		}
		
		try{
			BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:/营改增数据/carWeight.csv"),"gbk"));
			for(String province:map.keySet()){
				Map<String,Integer> mapCarWeight=map.get(province);
				for(String car:mapCarWeight.keySet()){
					int weight=mapCarWeight.get(car);
					writer.write(province+","+car+","+weight+"\n");
				}
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		readTrade(tradeData);
	}
}
