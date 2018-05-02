package com.carleton.ccr.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class AnalyticsResponse implements Serializable {

	private static final long serialVersionUID = 6076406500021189698L;
	
	public ArrayList<DataPoint> stats;
	
	public AnalyticsResponse(HashMap<String, Integer> stats) {
		this.stats = new ArrayList<DataPoint>();
		for (String key : stats.keySet()) {
			this.stats.add(new DataPoint(key, stats.get(key)));
		}
	}
	public ArrayList<DataPoint> getStats() {
		return stats;
	}


	public void setStats(ArrayList<DataPoint> stats) {
		this.stats = stats;
	}



	public class DataPoint implements Serializable {

		private static final long serialVersionUID = -860572646795470495L;
		public String name;
		public int value; 
		
		public DataPoint(String name, int value) {
			this.name = name;
			this.value = value;
		}
	}
}
