package pengrui;

import org.apache.log4j.Logger;

public class log4j {
	private static Logger logger=Logger.getLogger(log4j.class);
	
	
	public static void main(String[] args){
		logger.debug("debug message");
		logger.info("info message");
		logger.error("error message");
		
	}
}
