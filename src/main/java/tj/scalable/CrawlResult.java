package tj.scalable;
import java.util.*;

public class CrawlResult {
	
	private String url;
	private Set<String> libraries;
	
	
	public CrawlResult(String url){
		this.url = url;
	}
	
	
	public Set<String> getLibraries() {
		return libraries;
	}

	public String getUrl() {
		return url;
	}


	public void setLibraries(Set<String> libraries) {
		this.libraries = libraries;
	}


	@Override
	public String toString() {
		return "CrawlResult [url=" + url + ", libraries=" + libraries + "]";
	}
	

}
