package tj.scalable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebLoader {

	private Logger log = LoggerFactory.getLogger(WebLoader.class);
	private String defaultAgent = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";
	private int connTO = 1000;
	private int readTO = 5000;

	public WebLoader() {

	}

	public String loadWebPage(URL url) throws IOException {
		StringWriter result = new StringWriter();

		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(connTO);
			conn.setReadTimeout(readTO);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", defaultAgent);
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
		} catch (IOException e) {
			log.error("Failed to load {}", url, e);
			;
			throw e;
		}
		return result.toString();

	}
}
