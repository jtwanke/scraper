package parse;

import org.jsoup.nodes.Document;

public abstract class Parser {

	public Document doc;
	
	public Parser(Document doc) {
		this.doc = doc;
	}
	
	public abstract Link[] Parse();

}
