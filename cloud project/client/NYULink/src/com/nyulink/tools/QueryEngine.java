package com.nyulink.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.location.Location;
import android.util.Log;


import com.nyulink.tools.EventCollection;
import com.nyulink.tools.EventsParseException;
import com.nyulink.tools.EventsXMLParserFactory;
import com.nyulink.tools.IEventsXMLParser;
import com.nyulink.tools.IQueryEngineListener.EngineError;
/**
 *
 * Copyright (c) 2012 Jack Kwok http://www.mobileideafactory.com

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 * @author Jack Kwok
 *
 */
public class QueryEngine {

    private static final String EB_APP_KEY = "Q32P5EVPUXF24AHWVA";
	private static final String URL_BASE = "https://www.eventbrite.com/xml/event_search?app_key=" + EB_APP_KEY;
	private static final String LOG_TAG = "QueryEngine";
    private IEventsXMLParser mParser = null;
    private EventCollection mEvents = null; 
	private Context mContext = null;
	
	private static QueryEngine singleton = null;
	
	
	public static QueryEngine getInstance (Context context) {
		if (singleton == null) {
			singleton = new QueryEngine(context);
		}
		return singleton;
	}
	
	private QueryEngine (Context context) {
		super();
		mContext = context;
	}
	
	public EventCollection getEvents() {
		return mEvents;
	}
	
	
	private String getCompleteSearchUrl (String city, String dateRange, String keyword) throws Exception {
		StringBuffer urlBuffer = new StringBuffer();
		urlBuffer.append(URL_BASE);
		
		if (!city.equals("")) {
			urlBuffer.append("&city=");
			urlBuffer.append(URLEncoder.encode(city, "utf-8"));
		}
		if (!dateRange.equals("")) {
			urlBuffer.append("&date="); 
			urlBuffer.append(dateRange);
		}
		if (!keyword.equals("")) {
			urlBuffer.append("&keyword=");
			urlBuffer.append(URLEncoder.encode(keyword, "utf-8"));
		}
		
		return urlBuffer.toString();
	}
	
	/**
	 * asynchronous network call to search for all local events within 10 miles of a user's location. 
	 * 
	 * @throws NoLocationProviderException 
	 * @throws EventsParseException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public void searchNearbyLocalEvents (IQueryEngineListener listener, 
			String city, String dateStart, String dateEnd, String keyword) throws Exception  {
		
		String dateRange = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		if (!dateStart.equals("") && !dateEnd.equals("")) {
			dateRange = sdf.format(sdf.parse(dateStart)) + "+" + sdf.format(sdf.parse(dateEnd));
		} else if (!dateStart.equals("")) {
			dateRange = sdf.format(sdf.parse(dateStart));
		} else if (!dateEnd.equals("")) {
			dateRange = sdf.format(sdf.parse(dateEnd));
		}
		
		Log.i(LOG_TAG, "query date range: " + dateRange);
		String urlString = getCompleteSearchUrl(city, dateRange, keyword);
		mParser = EventsXMLParserFactory.getInstance().getParser();

		try {
			mEvents = mParser.parserFromURL(new URL(urlString));
			Log.i(LOG_TAG, "sorting");
			mEvents.sort();
			listener.notifyProcessingSuccess(mEvents);
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, e.toString() + ", msg=" + e.getMessage());
			listener.notifyProcessingFailure(EngineError.URL_ERR);
		} catch (IOException e) {
			Log.e(LOG_TAG, e.toString() + ", msg=" + e.getMessage());
			listener.notifyProcessingFailure(EngineError.IO_ERR);
		} catch (EventsParseException e) {
			Log.e(LOG_TAG, e.toString() + ", msg=" + e.getMessage());
			listener.notifyProcessingFailure(EngineError.PARSING_ERR);
		}
		
	}
	
}
