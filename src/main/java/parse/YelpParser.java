package parse;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YelpParser extends Parser {

	public YelpParser(Document doc) {
		super(doc);
	}

	@Override
	public Link[] Parse() {
		// need to fetch these from DB to allow runtime "fixing"
		Link[] results;
		String resultSelector = "li.regular-search-result";
		String urlSelector = "a.biz-name";
		String nameSelector = "span";
		Elements theResults = doc.select(resultSelector);
		int size = theResults.size();
		System.out.println("Results Selector Returned " + size);
		results = new Link[size];
		int res = 0;
		for(Element result: theResults){
			Element linkEl = result.select(urlSelector).get(0);
			if(linkEl != null){
				String link = linkEl.attr("abs:href");
				String name = linkEl.select(nameSelector).text();
				System.out.println("Name: " + name + " Link: " + link);
				results[res++] = new Link(link, name);
			}
			else
				System.out.println("Url Selector Returned Null.");
		}
		return results;
	}

}
