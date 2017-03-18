package fetch;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class YelpFetcher extends HtmlDocFetcher {

	public YelpFetcher(String phrase, String loc) {
		super(phrase, loc);
	}

	@Override
	public Document Fetch() {
		if(this.phrase != null){
			// need to fetch url from DB to allow runtime fixing
			String url = "https://www.yelp.com/search?find_desc=" + this.phrase + "&find_loc=" + this.location;
			try {
				System.out.println("Fetching " + url);
				return Jsoup.connect(url).get();
			} catch (IOException e) {
				System.out.println("Couldn't fetch " + url);
			}
		}
		return null;
	}

}
