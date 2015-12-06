package es.udc.vvs.va.test.model.quickcheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortedList {

	private List<Integer> list;

	public SortedList(List<Integer> list) {
		this.list = new ArrayList<Integer>(list);
		Collections.sort(this.list);
	}

	public void add(Integer i) {
		list.add(i);
		Collections.sort(this.list);
	}

	public void delete(Integer i) {
		list.remove(i);
	}

	public boolean member(Integer i) {
		return list.contains(i);
	}

	public List<Integer> toList() {
		return list;
	}
	
	public int getNumberOfOcurrences(Integer match) {
		int count = 0;
		for(Integer i: list) {
			if (i.equals(match)) {
				count++;
			}
		}		
		return count;
	}
}