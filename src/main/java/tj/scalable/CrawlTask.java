package tj.scalable;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrawlTask implements Callable<CrawlResult> {
	private Logger log = LoggerFactory.getLogger(CrawlTask.class);
	private String url;

	public CrawlTask(String url) {
		this.url = url;
	}

	@Override 
	public CrawlResult call() throws IOException {
		HtmlParser parser = new HtmlParser();
		Set<String> jslibs = parser.extractJs(url);
		CrawlResult result = new CrawlResult(url);
		result.setLibraries(jslibs);
		log.info("{} done {} jslibs",url, jslibs.size());
		return result;

	}

	public String getUrl() {
		return url;
	}

}
