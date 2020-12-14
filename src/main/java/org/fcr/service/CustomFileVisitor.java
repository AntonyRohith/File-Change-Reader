package org.fcr.service;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;

import org.fcr.gui.controller.DiskController;
import org.fcr.gui.dto.DiskDTO;

public class CustomFileVisitor implements FileVisitor<Path> {
	
	private long startTime = 0;
	
	private long endTime = 0;
	
	public CustomFileVisitor(long time) {
		this.startTime = time;
		this.endTime=time+(60*60*24*1000);
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		long updatedTime=attrs.lastModifiedTime().toMillis();
		if(updatedTime>=startTime && updatedTime<=endTime) 
			DiskController.addDiskData(new DiskDTO(
					file.getFileName().toString(),
					formatFileSize(attrs.size()),
					formatFileTime((updatedTime-startTime)/1000),
					file.toAbsolutePath().toString()));
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}
	
	private String formatFileSize(long size) {
		long kb = size/1024;
		long mb = kb/1024;
		long gb = mb/1024;
		if(gb>=1)
			return gb+" GB";
		if(mb>=1)
			return mb+" MB";
		return kb+" KB";
	}
	
	private String formatFileTime(long seconds) {
		long hour = TimeUnit.SECONDS.toHours(seconds);
		long min = TimeUnit.SECONDS.toMinutes(seconds)-TimeUnit.SECONDS.toHours(seconds)*60;
		long sec = TimeUnit.SECONDS.toSeconds(seconds)-TimeUnit.SECONDS.toMinutes(seconds)*60;
		return modulusTime(hour)+":"+modulusTime(min)+":"+modulusTime(sec)+"  ";
	}
	
	private String modulusTime(long val) {
		return (val/10>=1)?val+"":"0"+val;
	}

}
