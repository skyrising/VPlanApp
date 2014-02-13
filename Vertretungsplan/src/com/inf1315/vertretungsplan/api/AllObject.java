package com.inf1315.vertretungsplan.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class AllObject extends ApiResult {

	public List<PageObject> pages = new ArrayList<PageObject>();
	public List<OtherObject> todayOthers = new ArrayList<OtherObject>();
	public List<OtherObject> tomorrowOthers = new ArrayList<OtherObject>();
	public List<TickerObject> tickers = new ArrayList<TickerObject>();
	public List<ReplacementObject> todayReplacementsList = new ArrayList<ReplacementObject>();
	public List<ReplacementObject> tomorrowReplacementsList = new ArrayList<ReplacementObject>();
	public List<Event> events = new ArrayList<Event>();
	public List<MensaClient> mensa = new ArrayList<MensaClient>();
	public UserInfo userInfo;
	public String hash = "";
	public String timeString = "";
	public long timestamp;
	private String token = "";

	public AllObject() {
		userInfo = new UserInfo("");
	}

	@SuppressLint("SimpleDateFormat")
	public AllObject(JSONObject json) throws JSONException {
		userInfo = new UserInfo(json.getJSONObject("user"));
		API.DATA.userInfo = userInfo;

		TickerObject[] ticker = new ApiResultArray(json.getJSONArray("ticker"),
				TickerObject.class).getArray(new TickerObject[0]);
		tickers = Arrays.asList(ticker);

		ReplacementObject[] replacementsArray = new ApiResultArray(
				json.getJSONArray("replacements"), ReplacementObject.class)
				.getArray(new ReplacementObject[0]);
		List<ReplacementObject> replacements = Arrays.asList(replacementsArray);
		todayReplacementsList.clear();
		tomorrowReplacementsList.clear();
		for (ReplacementObject ro : replacements) {
			if (ro.isToday)
				todayReplacementsList.add(ro);
			else
				tomorrowReplacementsList.add(ro);
		}
		Collections.sort(todayReplacementsList);
		Collections.sort(tomorrowReplacementsList);

		PageObject[] pagesArray = new ApiResultArray(
				json.getJSONArray("pages"), PageObject.class)
				.getArray(new PageObject[0]);
		pages = Arrays.asList(pagesArray);

		OtherObject[] othersArray = new ApiResultArray(
				json.getJSONArray("others"), OtherObject.class)
				.getArray(new OtherObject[0]);
		List<OtherObject> others = Arrays.asList(othersArray);
		todayOthers.clear();
		tomorrowOthers.clear();
		for (OtherObject oo : others) {
			if (oo.isToday)
				todayOthers.add(oo);
			else
				tomorrowOthers.add(oo);
		}

		Event[] event = new ApiResultArray(json.getJSONArray("events"),
				Event.class).getArray(new Event[0]);
		events = Arrays.asList(event);

		MensaServer[] mensaArray = new ApiResultArray(
				json.getJSONArray("mensa"), MensaServer.class)
				.getArray(new MensaServer[0]);
		Map<Long, MensaClient> map = new HashMap<Long, MensaClient>();
		for (MensaServer o : mensaArray) {
			if (!map.containsKey(o.timestamp))
				map.put(o.timestamp, new MensaClient(o.timestamp));
			if (o.type == 0)
				map.get(o.timestamp).menu1 = o.value;
			else if (o.type == 1)
				map.get(o.timestamp).menu2 = o.value;
		}
		mensa = new ArrayList<MensaClient>(map.values());

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		timeString = sdf.format(new Date());

		timestamp = System.currentTimeMillis() / 1000L;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
		SharedPreferences.Editor spe = API.CONTEXT.getSharedPreferences("data",
				Context.MODE_PRIVATE).edit();
		String json = (new Gson()).toJson(this);
		spe.putString("data", json);
		spe.commit();
	}

	public void deleteToken() {
		setToken("");
	}
}
