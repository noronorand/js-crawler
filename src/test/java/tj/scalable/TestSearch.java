package tj.scalable;



import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("tests can block IP for google search")
public class TestSearch {

	

	 
	@Test
	public void testSearchAjax10() throws IOException, InterruptedException {
		String searchTerm = "ajax";
		int maxRez = 10;		
		List<Map.Entry<String, Integer>> rez = new CrawlRunner().crawl(searchTerm, maxRez);
		assert(rez.size() >0);
		assert(rez.get(0).getValue() >0);
		
	}
	
	 
	@Test
	public void testSearchAjax50() throws IOException, InterruptedException {
		String searchTerm = "ajax";
		int maxRez = 50;		
		List<Map.Entry<String, Integer>> rez = new CrawlRunner().crawl(searchTerm, maxRez);
		assert(rez.size() >0);
		assert(rez.get(0).getValue() >1);
		
	}
	
	 
	@Test
	public void testSearchJavascriptExample10() throws IOException, InterruptedException {
		String searchTerm = "javascript examples";
		int maxRez = 10;		
		List<Map.Entry<String, Integer>> rez = new CrawlRunner().crawl(searchTerm, maxRez);
		assert(rez.size() >0);
		assert(rez.get(0).getValue() >1);
		
	}
	
	 
	@Test
	public void testSearchJavascriptExample30() throws IOException, InterruptedException {
		String searchTerm = "javascript examples";
		int maxRez = 30;		
		List<Map.Entry<String, Integer>> rez = new CrawlRunner().crawl(searchTerm, maxRez);
		assert(rez.size() >0);
		assert(rez.get(0).getValue() >1);
		
	}

}
