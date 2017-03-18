package fetch;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class YellowPagesFetcher extends HtmlDocFetcher {

	public YellowPagesFetcher(String phrase, String loc) {
		super(phrase, loc);
	}
	
	public Document Fetch(){
		if(this.phrase != null){
			// need to fetch url from DB to allow runtime fixing
			String url = "https://www.yellowpages.com/search?search_terms=" + this.phrase + "&geo_location_terms=" + this.location;
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
