package de.gymdon.app.fragments;

import de.gymdon.app.R;
import de.gymdon.app.api.API;
import de.gymdon.app.api.PageObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class PageFragment extends Fragment {

	public static final String SITE_NUMBER = "SITE_NUMBER";

	// true, if today's fragment
	private int siteNumber = 0;

	public PageFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.siteNumber = getArguments().getInt(SITE_NUMBER);
		View rootView = inflater.inflate(R.layout.fragment_page, container,
				false);

		WebView web = (WebView) rootView.findViewById(R.id.page_WebView);
		PageObject po = API.DATA.pages.get(siteNumber);
		web.loadDataWithBaseURL(API.STANDARD_URL, po.content, "text/html", "UTF-8", null);
		return rootView;
	}
}