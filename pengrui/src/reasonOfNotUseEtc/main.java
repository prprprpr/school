package reasonOfNotUseEtc;

import java.io.IOException;
import java.text.ParseException;

import org.json.JSONException;

public class main {
	private static String in2016="F:/����/ԭʼ����";
	private static String in2017="F:/����/2017short";
	public static void main(String[] args) throws NumberFormatException, IOException, JSONException, ParseException{
//		getFirstEtcTime.write(in2016, in2017);
		getOdTime.getOdTimeInfo(in2016, in2017);
	}
}
