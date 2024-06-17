package com.framework.comm;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
	
	public static synchronized String getDateTime(String dFormat) {
		
		SimpleDateFormat formatter = new SimpleDateFormat(dFormat);
		Date date = new Date();
		return ""+formatter.format(date);
	}

}
