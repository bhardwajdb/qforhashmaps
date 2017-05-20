package com.diwakar.qforhashmaps.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class WritablePacket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5588841303799478394L;
	private Integer key=-1;
	private List<Integer> value;
	private Integer size = 0;
	private String uuid="";	
	
	public WritablePacket(){}
	public WritablePacket(Integer k, List<Integer> v, Integer s, String t){
		key = k;
		value = v;
		size = s;
		uuid=t;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public List<Integer> getValue() {
		return value;
	}

	public void setValue(ArrayList<Integer> value) {
		this.value = value;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String toString(){
		return uuid+"-"+key+":"+value;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public TreeMap<Integer, ArrayList<Integer>> toMap()
	{
		TreeMap<Integer,ArrayList<Integer>> map = new TreeMap<Integer,ArrayList<Integer>>();
		ArrayList<Integer> lst = new ArrayList<Integer>();
		lst.addAll(value);
		map.put(key,lst);
		return map;
	}
}
