package com.automation.test.define;

import org.apache.commons.collections4.Equator;

import java.util.Map;

public class MapEquator implements Equator<Map<String, Object>> {

	@Override
	public boolean equate(Map<String, Object> m1, Map<String, Object> m2) {
		for (String key : m1.keySet()) {
			if (m2.containsKey(key)) {
				Object v1 = m1.get(key);
				Object v2 = m2.get(key);
				
				if (v1 != null) {
					if (v1.equals(v2)) {
						// Do nothing
					}
					else {
						return false;
					}
				}
				else {
					if (v2 == null) {
						// Do nothing
					}
					else {
						return false;
					}
				}
			}
			else {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public int hash(Map<String, Object> m) {
		return m.hashCode();
	}

}
