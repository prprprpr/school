package writeNumber;

import java.sql.Connection;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class random {
	public static void randomDate(String beginDate,String endDate) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date start = format.parse(beginDate);  
        Date end = format.parse(endDate);  
	}
	
}
