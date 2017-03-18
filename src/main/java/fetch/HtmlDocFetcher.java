package fetch;

import org.jsoup.nodes.Document;

public abstract class HtmlDocFetcher{
	
	public String phrase;
	public String location;

	public HtmlDocFetcher(String phrase, String location) {
		this.phrase = phrase;
		this.location = location;
	}
	
	public abstract Document Fetch();
}
