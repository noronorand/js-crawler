package tj.scalable;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class HtmlParser {

	private Logger log = LoggerFactory.getLogger(HtmlParser.class);
	// google search service
	private String svcURL = "http://www.google.com/search?q=";

	// google results
	private String startRez = "<h3 class=\"r\"><a href=\"/url?q=";
	private String endRez = "&amp";


	// js tag patterns
	private String jsRegex = "<script(\\s+)src(\\S+)(.js)(\\'|\\\")";

	private WebLoader webLoad = new WebLoader();

	public Set<String> extractJs(String url) throws MalformedURLException, IOException {
		
		Pattern p = Pattern.compile(jsRegex);
		Set<String> ret = new HashSet<String>();
		String content = webLoad.loadWebPage(new URL(url));

		Matcher matcher = p.matcher(content);
		while (matcher.find()) {
			String script = matcher.group();
			log.debug("script match {}", script);
			int lix = script.lastIndexOf("/");
			String fname = script.substring(lix + 1, script.length() - 1);
			log.debug("js match {}", fname);
			ret.add(fname);
		}
		return ret;

	}

	public List<String> googleSearch(String searchTerm, int maxRez) throws IOException {

		List<String> ret = new ArrayList<String>(maxRez);
		String searchTermEnc = URLEncoder.encode(searchTerm, Charset.defaultCharset().toString());
		String endpoint = svcURL + searchTermEnc + "&num=" + maxRez;
		log.debug("search url {}",endpoint);
		URL url = new URL(endpoint);

		String pageContent = webLoad.loadWebPage(url);
		
		int ixStop = 0;
		int ixStart = 0;
		while (ixStart != -1 && ixStop != -1 && ret.size() < maxRez) {
			ixStart = pageContent.indexOf(startRez, ixStop);
			ixStop = pageContent.indexOf(endRez, ixStart);

			if (ixStart != -1 && ixStop != -1) {
				String aLink = pageContent.substring(ixStart + startRez.length(), ixStop);
				String aLinkDec = URLDecoder.decode(aLink, Charset.defaultCharset().toString());
				ret.add(aLinkDec);

			}

		}
		log.debug("google search returned {}",ret.size());
		return ret;
	}
	

	
}
