package com.piyush.jsoupclient;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsouptTestMain {
	public static void main(String[] args) throws Exception {

		Connection.Response loginForm = Jsoup.connect("http://192.168.110.61/index.php").method(Connection.Method.POST)
				.execute();

		System.out.println(loginForm.cookies());

		Connection.Response indexResponse = Jsoup.connect("http://192.168.110.61/index.php").data("username", "admin")
				.data("password", "AtinAgarwal123").data("_submit_check", "1").cookies(loginForm.cookies())
				.method(Connection.Method.POST).execute();

		// System.out.println(indexResponse.body());

		System.out.println(indexResponse.cookies());

		Document doc = Jsoup.connect("http://192.168.110.61/outbox.php").method(Connection.Method.GET)
				.cookies(loginForm.cookies()).get();

		Map<String, List<String>> tableData = new LinkedHashMap<String, List<String>>();

		Elements elements = doc.getElementsByTag("table");
		Iterator<Element> tr = elements.iterator();
		while (tr.hasNext()) {
			Element el = tr.next();
			Elements trs = el.getElementsByTag("tr");
			Iterator<Element> trsIt = trs.iterator();
			int count = 0;
			while (trsIt.hasNext()) {
				Element dataTr = trsIt.next();
				if (count == 0) {
					Elements headings = dataTr.getElementsByTag("th");
					for (Element head : headings) {
						String key = head.html();
						tableData.put(key, new ArrayList<String>());
					}
				} else {
					Elements dataTds = dataTr.getElementsByTag("td");
					int keyIndex = 0;
					for (Element head : dataTds) {
						String data = head.html();
						tableData.get(tableData.keySet().toArray()[keyIndex]).add(data);

						keyIndex++;
					}
				}
				count++;
			}
		}

		System.out.println(tableData);

	}

}