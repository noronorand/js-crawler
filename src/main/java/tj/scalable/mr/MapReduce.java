package tj.scalable.mr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.slf4j.*;

import tj.scalable.HtmlParser;

public class MapReduce {

	private static Logger log = LoggerFactory.getLogger(MapReduce.class);

	private final static boolean SEARCH_ENABLED = false;

	private static String[] search = { "https://www.w3schools.com/js/", "https://www.javascript.com/",
			"https://developer.mozilla.org/en-US/docs/Web/JavaScript", "https://hr.wikipedia.org/wiki/JavaScript",
			"https://en.wikipedia.org/wiki/JavaScript", "https://www.codecademy.com/learn/javascript",
			"https://www.codeschool.com/learn/javascript", "http://enable-javascript.com/hr/",
			"https://hackernoon.com/javascript/home", "https://repl.it/languages/javascript",
			"https://www.reddit.com/r/javascript/", "http://javascript.crockford.com/javascript.html",
			"https://stackoverflow.com/questions/tagged/javascript", "http://exercism.io/languages/javascript",
			"https://www.coursera.org/courses?query=javascript", "https://github.com/trending/javascript",
			"http://devdocs.io/javascript/", "https://egghead.io/courses",
			"https://developers.arcgis.com/javascript/3/", "http://javascriptweekly.com/",
			"https://www.udemy.com/understand-javascript/",
			"https://www.lynda.com/JavaScript-training-tutorials/244-0.html",
			"https://codecanyon.net/category/javascript", "https://learnxinyminutes.com/docs/javascript/",
			"https://play.google.com/store/apps/details?id=com.sololearn.javascript",
			"https://www.w3.org/standards/webdesign/script", "https://kotlinlang.org/docs/reference/js-overview.html",
			"https://www.udacity.com/course/javascript-basics--ud804", "https://www.javatpoint.com/javascript-tutorial",
			"https://code.visualstudio.com/docs/introvideos/quicktour",

	};

	private static class JsCollector
			implements Collector<Set<String>, Map<String, Integer>, List<Map.Entry<String, Integer>>> {

		@Override
		public BiConsumer<Map<String, Integer>, Set<String>> accumulator() {
			return (m, s) -> {
				for (String k : s) {
					int count = m.containsKey(k) ? m.get(k) : 0;
					m.put(k, ++count);
				}

			};
		}

		@Override
		public Set<java.util.stream.Collector.Characteristics> characteristics() {
			return Collections.emptySet();
		}

		@Override
		public BinaryOperator<Map<String, Integer>> combiner() {
			return (m1, m2) -> {

				for (String k1 : m1.keySet()) {
					if (m2.containsKey(k1)) {
						m1.put(k1, m2.get(k1) + m1.get(k1));
					}
				}
				for (String k2 : m2.keySet()) {
					if (!m1.containsKey(k2)) {
						m1.put(k2, m2.get(k2));
					}
				}

				return m1;

			};

		}

		@Override
		public Function<Map<String, Integer>, List<Entry<String, Integer>>> finisher() {
			return (m) -> {
				List<Entry<String, Integer>> ret = new ArrayList<Entry<String, Integer>>();
				ret.addAll(m.entrySet());
				Collections.sort(ret, (Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) -> -1
						* e1.getValue().compareTo(e2.getValue()));

				return ret;
			};

		}

		@Override
		public Supplier<Map<String, Integer>> supplier() {
			return ConcurrentHashMap<String, Integer>::new;
		}

	}

	public static void main(String[] args) {

		List<String> searchResults = null;

		if (SEARCH_ENABLED) {
			String searchTerm = "java script";
			log.info("searching {}", searchTerm);
			try {

				searchResults = new HtmlParser().googleSearch(searchTerm, 30);
				if (searchResults.size() == 0) {
					System.out.println("Google search did not return any result");
					System.exit(0);
				}
			} catch (IOException e) {
				log.error("Failed to execute google search", e);

			}
		}

		else {
			// USE PRELOADED SEARCH RESULTS
			searchResults = Arrays.asList(search);
		}

	
		Stream<String> sites = searchResults.parallelStream();

		List<Map.Entry<String, Integer>> rezList = sites

				.map(s -> {
					Set<String> ret;
					try {
						ret = new HtmlParser().extractJs(s);
					} catch (IOException e) {
						ret = new HashSet<String>();
					}
					return ret;

				})

				.collect(new JsCollector());

		int ix = 0;
		for (Map.Entry<String, Integer> me : rezList) {
			System.out.println(me.getKey() + " -> " + me.getValue());
			if (++ix == 5) {
				break;
			}
		}

	}
}
