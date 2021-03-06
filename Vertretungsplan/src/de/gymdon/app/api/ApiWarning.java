package de.gymdon.app.api;

import java.util.*;

import org.json.*;

public class ApiWarning {

	private String warning;
	private String description;
	private Map<String, String> extra;
	private Exception exception;
	private boolean isException;

	public ApiWarning(JSONObject o) {
		extra = new HashMap<String, String>();
		try {
			@SuppressWarnings("unchecked")
			Iterator<String> keys = o.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				if ("warning".equals(key))
					warning = o.getString(key);
				else if ("description".equals(key))
					description = o.getString(key);
				else
					extra.put(key, o.getString(key));
			}
		} catch (JSONException e) {
			this.exception = e;
			isException = true;
		}
	}
	
	public ApiWarning(Exception e) {
		this.exception = e;
		isException = true;
	}

	public String getWarning() {
		return warning;
	}

	public String getDescription() {
		return description;
	}

	public Map<String, String> getExtra() {
		return extra;
	}

	public String getExtra(String key) {
		return extra.get(key);
	}
	
	public boolean isException() {
		return isException;
	}
	
	public Exception getException() {
		return exception;
	}
}
