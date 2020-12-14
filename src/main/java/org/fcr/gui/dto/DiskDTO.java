package org.fcr.gui.dto;

import java.util.Comparator;

public class DiskDTO implements Comparable<DiskDTO> {

	private String name;

	private String size;

	private String time;

	private String path;

	public DiskDTO() {}

	public DiskDTO(String name,String size, String time, String path) {
		super();
		this.name = name;
		this.size = size;
		this.time = time;
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public static Comparator<DiskDTO> getSizeComparator(String sortType) {
		Comparator<DiskDTO> comparator = (obj1,obj2)->{
			if(sortType.contains("DESC")) {
				DiskDTO temp = new DiskDTO(obj1.getName(), obj1.getSize(), obj1.getTime(), obj1.getPath());
				obj1=obj2;
				obj2=temp;
			}
			String arrobj1[] = obj1.getSize().split(" ");
			String arrobj2[] = obj2.getSize().split(" ");
			if(arrobj1[1].equals(arrobj2[1]))
				return Integer.parseInt(arrobj1[0])-Integer.parseInt(arrobj2[0]);
			if(arrobj1[1].equals("GB"))
				return 1;
			if(arrobj2[1].equals("GB"))
				return -1;
			if(arrobj1[1].equals("MB"))
				return 1;
			if(arrobj2[1].equals("MB"))
				return -1;
			return 0;
		};
		return comparator;
	}

	@Override
	public int compareTo(DiskDTO o) {
		return 0;
	}

}
