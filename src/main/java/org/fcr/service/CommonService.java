package org.fcr.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CommonService {
	
	public static ExecutorService getExecutor() {
		return Executors.newFixedThreadPool(10);
	}
	
	public static void shutdownExecutor(ExecutorService executor) {
		try {
			executor.awaitTermination(1, TimeUnit.SECONDS);
			executor.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static Properties getProperties() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File(CommonService.class.getClassLoader().getResource("conf/application.properties").getFile())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;
	}
}
