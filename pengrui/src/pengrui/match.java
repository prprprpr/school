package pengrui;


import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class match {
    private static String fileOut1 = "H:/新建文件夹/fileOut1/";
    private static String fileOut6 = "H:/新建文件夹/fileOut6/";
    private static String fileOut = "C:/Users/pengrui/Desktop/fileOut/1.csv";

    public static BufferedReader getReader(String path, String encoding) throws UnsupportedEncodingException, FileNotFoundException {
        File file = new File(path);
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
        BufferedReader reader = new BufferedReader(read);
        return reader;
    }

    public static BufferedWriter getWriter(String path, String encoding) throws UnsupportedEncodingException, FileNotFoundException {
        File file = new File(path);
        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), encoding);
        BufferedWriter writer = new BufferedWriter(write);
        return writer;
    }

    public static Map<String,List<String>> read1() throws IOException {
        String encoding = "UTF-8";
        String encoding1 = "GBK";
        Map<String,List<String>> map=new HashMap<>();
        File fileIn = new File(fileOut1);
        List<String> listFile = Arrays.asList(fileIn.list());
        for (int i = 0; i < listFile.size(); i++) {
            String path = fileOut1 + "/" + listFile.get(i);
            BufferedReader readerFileAll = getReader(path, encoding1);
            String lineFileAll = "";
            while ((lineFileAll = readerFileAll.readLine()) != null) {
                String[] data = lineFileAll.split(";");
                String key=data[0];
                if(!map.containsKey(key)){
                    List<String> list=new ArrayList<>();
                    list.add(data[3]);
                    map.put(key, list);
                }else{
                    List<String> list=(List<String>) map.get(key);
                    list.add(data[3]);
                    map.put(key, list);
                }
            }
            readerFileAll.close();
        }
        return map;
    }
    public static Map<String,List<String>> read6() throws IOException {
        String encoding = "UTF-8";
        String encoding1 = "GBK";
        Map<String,List<String>> map=new HashMap<>();
        File fileIn = new File(fileOut6);
        List<String> listFile = Arrays.asList(fileIn.list());
        for (int i = 0; i < listFile.size(); i++) {
            String path = fileOut6 + "/" + listFile.get(i);
            BufferedReader readerFileAll = getReader(path, encoding1);
            String lineFileAll = "";
            while ((lineFileAll = readerFileAll.readLine()) != null) {
                String[] data = lineFileAll.split(";");
                String key=data[0]+"#"+data[1];
                if(!map.containsKey(key)){
                    List<String> list=new ArrayList<>();
                    list.add(data[3]);
                    map.put(key, list);
                }else{
                    List<String> list=(List<String>) map.get(key);
                    list.add(data[3]);
                    map.put(key, list);
                }
            }
            readerFileAll.close();
        }
        return map;
    }
    public static void distance(List<String> list1,List<String> list6,String key,BufferedWriter writer) throws IOException, ParseException {
    	Double min=1000000.0;
    	Double distance=0.0;
    	int h=0,j=0,k=0;
    	DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	for(j=0;j+list6.size()<list1.size();j++)
    	{
    		k=j;
    		for(int i=0;i<list6.size();i++,k++){
    			distance+=((Double.parseDouble(list6.get(i)))-(Double.parseDouble(list1.get(k))))*((Double.parseDouble(list6.get(i)))-(Double.parseDouble(list1.get(k))));
    		}
    		if(distance<min)
    		{
    			min=distance;
    			h=k+1;
    		}
    		distance=0.0;
    	}
    	String time1=key.split("#")[1]+" 08:00:00";
		String time2=df.format(df.parse(time1).getTime()+120000);
		int count=h+30;
		for(;h<count;h++)
		{
//			System.out.println(h);
//			System.out.println(key.split("#")[0]+";"+key.split("#")[1]+";["+time1+","+time2+");"+list1.get(h));
			writer.write(key.split("#")[0]+"#"+key.split("#")[1]+"#["+time1+","+time2+")#"+list1.get(h)+"\n");
			writer.flush();
			time1=time2;
			time2=df.format(df.parse(time1).getTime()+120000);
		}
    }
    public static void match() throws IOException, ParseException {
    	Map<String,List<String>> map1=read1();
    	Map<String,List<String>> map6=read6();
    	BufferedWriter writer=getWriter(fileOut,"GBK");
    	for (Entry<String,List<String>> entry : map6.entrySet()) {
    		String key=entry.getKey();
    		List<String> list6=new ArrayList<>();
    		List<String> list1=new ArrayList<>();
    		list6=entry.getValue();
    		list1=map1.get(key.split("#")[0]);
    		distance(list1,list6,key,writer);
    	}
    	writer.close();
    }


    public static void main(String[] args) throws ParseException, IOException {
    	match();
    }
}

