package api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import db.Dao;
import fetch.HtmlDocFetcher;
import fetch.YellowPagesFetcher;
import fetch.YelpFetcher;
import parse.Link;
import parse.Parser;
import parse.YellowPagesParser;
import parse.YelpParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@Path("/search")
public class ScraperAPI {
	
	private final static String ERR_LENGTH = "Incorrect Input Length";
	private final static String ERR_ENCODING = "Incorrect Encoding";
	private final static int YELP_MODE = 0;
	private final static int YP_MODE = 1;
	
	private final static String YELP = "Yelp";
	private final static String YELP_ID = "yelp";
	private final static String YP = "YellowPages";
	private final static String YP_ID = "yellowpages";

	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response Search(@QueryParam("domain") String domain, @QueryParam("phrase") String phrase, @QueryParam("loc") String loc) {
		return Response.status(200).entity(this.start(phrase, loc, domain)).build();
	}
	
//  TODO:
//	Lets move this to another API class (like StorageAPI?)
//	@POST
//	@Path("/save")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response saveSearch(@QueryParam("phrase") String phrase, @QueryParam("loc") String loc, @QueryParam("site") String site) {
//		String output = "{\"status\":\"SUCCESS\"}";
//		Dao.saveSearch(phrase, loc, Integer.decode(site));
//		return Response.status(200).entity(output).build();
//	}
	
	/**
	 * Starts the execution of a site scraping. Does generic message-building and exception-catching.
	 * @param phrase The phrase you are searching for on the target site.
	 * @param loc The location the results should be tailored to.
	 * @param domain The domain of the site to be scraped.
	 * @return The message to be built into the HTTP response.
	 */
	private String start(String phrase, String loc, String domain){ //this is new method 
		int mode = this.getMode(domain); //should we use server context to store hashmap?
		String tabid = this.getID(domain);
		if (phrase.length() > 0 && loc.length() > 0) { //trivial validation
			try {
				Link[] theLinks = this.search(phrase, loc, mode); 
				if(theLinks != null){
					String mapToJson = ScraperAPI.JsonMapLinks(theLinks);
					return "{\"status\":\"SUCCESS\"," +
						   "\"tableid\":" + " \"" + tabid + "\"," + 
					       "\"domain\":" + " \"" + domain +"\"," + 
						   "\"phrase\":"  + " \"" + phrase + "\"," + 
					       "\"loc\":"  + " \"" + loc + "\"," +
					       "\"result\":" + mapToJson + "}";
				}
			} catch (UnsupportedEncodingException e) {
				return ScraperAPI.errorString(phrase, loc, ScraperAPI.ERR_ENCODING);
			}
		}
		return ScraperAPI.errorString(phrase, loc, ScraperAPI.ERR_LENGTH);
	}
	
	/**
	 * Performs the search of the target site. Initializes core classes to capture and parse HTML content from target site.
	 * @param phrase The phrase you are searching for on the target site.
	 * @param loc The location the results should be tailored to.
	 * @param mode The target site.
	 * @return The links returned from the search of the target site.
	 * @throws UnsupportedEncodingException
	 */
	private Link[] search(String phrase, String loc, int mode) throws UnsupportedEncodingException{
		HtmlDocFetcher fetcher;
		Parser parser;
		String safePhrase = URLEncoder.encode(phrase, "UTF-8");
		String safeLoc = URLEncoder.encode(loc, "UTF-8");
		fetcher = this.createFetcher(safePhrase, safeLoc, mode);
		Document doc = fetcher.Fetch();
		if(doc != null){
			parser = createParser(doc, mode);
			return parser.Parse();
		}
		// log when scraper doesn't work
		System.out.format("Unable to fetch:\nPhrase:%s, Location:%s", safePhrase, safeLoc);
		return null;
		
	}
	
	/**
	 * Creates a HtmlDocFetcher object corresponding to the arguments. 
	 * @param phrase The phrase you are searching for on the target site.
	 * @param loc The location the results should be tailored to.
	 * @param mode The target site.
	 * @return a HtmlDocFetcher object corresponding to the arguments.
	 */
	private HtmlDocFetcher createFetcher(String phrase, String loc, int mode){
		switch(mode){
		case ScraperAPI.YELP_MODE:
			return new YelpFetcher(phrase, loc); 
		case ScraperAPI.YP_MODE:
			return new YellowPagesFetcher(phrase, loc);
		default:
			return new YelpFetcher(phrase, loc);
		}
	}
	
	/**
	 * Creates a Parser object corresponding to the arguments.
	 * @param doc The JSoup Document object to be parsed.
	 * @param mode The target (source) website.
	 * @return a Parser object corresponding to the arguments.
	 */
	private Parser createParser(Document doc, int mode){
		switch(mode){
		case ScraperAPI.YELP_MODE:
			return new YelpParser(doc); 
		case ScraperAPI.YP_MODE:
			return new YellowPagesParser(doc);
		default:
			return new YelpParser(doc);
		}
	}
	
	private int getMode(String domain){
		if(domain.equals("yelp"))
			return ScraperAPI.YELP_MODE;
		else if(domain.equals("yellowpages"))
			return ScraperAPI.YP_MODE;
		else
			return -1;
	}
	private String getID(String domain){
		if(domain.equals("yelp"))
			return ScraperAPI.YELP_ID;
		else if(domain.equals("yellowpages"))
			return ScraperAPI.YP_ID;
		else
			return "";
	}
	
	private static String JsonMapLinks(Link[] links){
		Gson objGson = new GsonBuilder().setPrettyPrinting().create();
		return objGson.toJson(links);
	}
	
	private static String errorString(String phrase, String loc, String error){
		return "{\"status\":\"FAILURE\", " + 
				"\"err\":\"" + error + "\", " + 
				" \"phrase\":\"" + phrase + "\", " + 
				"\"loc\" : \"" + loc + "\", " + 
				"\"errnum\" : \"" + Integer.toString(0) + "\"}";
	}
}
