package tj.scalable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


import org.slf4j.*;

public class CrawlRunner {

	private Logger log = LoggerFactory.getLogger(CrawlRunner.class);;


	public List<Map.Entry<String, Integer>> crawl(String searchTerm, int maxRez) throws IOException, InterruptedException {
		// google search
		List<String> searchResults = null;

		log.info("searching {}", searchTerm);
		try {

			searchResults = new HtmlParser().googleSearch(searchTerm, maxRez);
			if (searchResults.size() == 0) {
				System.out.println("Google search did not return any result");
				System.exit(0);
			}
		} catch (IOException e) {
			log.error("Failed to execute google search", e);
			throw e;
		}

		//execute  js references searches
		int threadPoolSz = searchResults.size() > 10 ? 10 : searchResults.size();
		ExecutorService executor = Executors.newFixedThreadPool(threadPoolSz);
		List<CrawlTask> tasks = new ArrayList<CrawlTask>(searchResults.size());

		for (String r : searchResults) {
			tasks.add(new CrawlTask(r));
		}
		List<Future<CrawlResult>> futures = null;
		try {
			long waitSec = tasks.size()*2/threadPoolSz;
			log.info("executing {} tasks, pool size {} wait {} s", tasks.size(), threadPoolSz,waitSec);
			futures = executor.invokeAll(tasks, waitSec, TimeUnit.SECONDS);
			
		} catch (InterruptedException e) {
			log.warn("crawl task interrupted ", e);
			throw e;
		}
		executor.shutdown();

		// collect futures and format results
		// sum up results
		Map<String, Integer> libToCount = new HashMap<String, Integer>();
		int canceled = 0;
		int failed = 0;
		for (Future<CrawlResult> f : futures) {
			try {
				if (f.isCancelled()) {
					++canceled;
					log.warn("task canceled");
					continue;
				}
				CrawlResult r = f.get();
				Set<String> libs = r.getLibraries();
				for (String l : libs) {
					int count = libToCount.containsKey(l) ? libToCount.get(l) : 0;
					libToCount.put(l, ++count);
				}

			} catch (ExecutionException e) {
				++failed;
				log.error("Failed to execute task", e);

			}

		}

		log.info("{} tasks started, {} canceled {} failed ",futures.size(),canceled,failed);

		// sort + print
		List<Map.Entry<String, Integer>> orderedByUsage = new ArrayList<Map.Entry<String, Integer>>();
		orderedByUsage.addAll(libToCount.entrySet());
		
		Collections.sort(orderedByUsage, (Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) -> -1
				* e1.getValue().compareTo(e2.getValue()));

		System.out.println("----- top 5 java script libraries by usage using search term: " + searchTerm + " max results " + maxRez + " -----");
		int count = 0;
		for (Map.Entry<String, Integer> me : orderedByUsage) {
			System.out.println(++count + ". " + me.getKey() + " -> " + me.getValue());
			if (count == 5)
				break;
		}
		return orderedByUsage;

	}
}
