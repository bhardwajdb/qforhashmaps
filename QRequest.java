package com.diwakar.qforhashmaps.domain;

import java.util.ArrayList;

public final class QRequest {

	private Integer key;
	private ArrayList<Integer> value;
	private Integer size;
	public QRequest(){}
	
	public QRequest(int k, ArrayList<Integer> v){
		key = k; value = v;
	}

	public Integer getKey() {
		return key;
	}

	public ArrayList<Integer> getValue() {
		return value;
	}

	public Integer getSize() { return size; }
}
