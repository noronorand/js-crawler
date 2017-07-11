package tj.scalable;
import java.io.IOException;

public class Start {

	public static void main(String[] args) {

		int maxRez = 20;

		if (args.length == 0) {
			System.out.println("usage: start.sh searchterm ");
			System.out.println("e.g. start.sh javascript examples ");
			System.exit(0);
		}
		String searchTerm = String.join(" ", args);

		try {
			new CrawlRunner().crawl(searchTerm, maxRez);
		} catch (IOException|InterruptedException e) {
			System.out.println("Fauiled to complete task");
		} 

	}
}
