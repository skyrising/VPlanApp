package com.inf1315.vertretungsplan.api;

import org.json.*;

public class PageObject extends ApiResult implements Comparable<PageObject> {

	public final int id;
	public final int order;
	public final String title;
	public final String content;
	public final long fromTimestamp;
	public final long toTimestamp;
	public final boolean pupils;
	public final boolean teachers;

	public PageObject(JSONObject obj) throws JSONException {
		id = obj.getInt("id");
		order = obj.getInt("order");
		title = obj.getString("title");
		content = obj.getString("content");
		fromTimestamp = obj.getLong("fromTimestap");
		toTimestamp = obj.getLong("toTimestamp");
		pupils = obj.getInt("pupils") != 0;
		teachers = obj.getInt("teachers") != 0;
	}
	
	@Override
	public int compareTo(PageObject other) {
		return fromTimestamp > other.fromTimestamp ? 1
			: fromTimestamp < other.fromTimestamp ? -1
			: toTimestamp > other.toTimestamp ? 1
			: toTimestamp < other.toTimestamp ? -1
			: order > other.order ? 1
			: order < other.order ? -1
			: id - other.id;
	}
}
