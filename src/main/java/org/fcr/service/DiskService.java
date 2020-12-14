package org.fcr.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class DiskService {

	public ExecutorService readDisk(long time,String path){
		try {
			ExecutorService executor = CommonService.getExecutor();
			if(path==null) {
				List<File> rootFiles = Arrays.asList(File.listRoots());
				rootFiles.forEach(
						x->executor.submit(
									()->Files.walkFileTree(Paths.get(x.getAbsolutePath()),new CustomFileVisitor(time))));
				
			} else {
				executor.submit(()->Files.walkFileTree(Paths.get(path), new CustomFileVisitor(time)));
			}
			CommonService.shutdownExecutor(executor);
			return executor;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
