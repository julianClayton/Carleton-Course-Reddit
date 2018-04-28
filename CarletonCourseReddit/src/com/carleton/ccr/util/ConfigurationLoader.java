package com.carleton.ccr.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationLoader {
	static private String CONF_FOLDER = "conf/";
	static private String CRAWLER_CONF = "crawler.conf";
	
	public static Properties loadCrawlerConfig() {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(CONF_FOLDER + CRAWLER_CONF);
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}
}
