package api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
//import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
	
	private final static int ERR_PHRASE = 1;
	private final static int ERR_ENCODING = 2;
	private final static int YELP_MODE = 0;
	private final static int YP_MODE = 1;
	
	private final static String YELP = "yelp";
	private final static String YELP_ID = "yelp";
	private final static String YP = "yellowpages";
	private final static String YP_ID = "yellowpages";
	
	// build generic function that handles logic

//	@GET
//	@Path("/yellowpages")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response ypSearch(@QueryParam("phrase") String phrase, @QueryParam("loc") String loc) {
//		System.out.println("Reached 'search/yellowpages/" + phrase);
//		String output = "{\"status\":\"FAILURE\", \"err\":\"Invalid phrase : " + phrase + "\", \"errnum\" : \""
//				+ ERR_PHRASE + "\"}";
//		if (phrase.length() > 0) {
//			try {
//				YellowPagesFetcher fetcher = new YellowPagesFetcher(URLEncoder.encode(phrase, "UTF-8"), URLEncoder.encode(loc, "UTF-8"));
//				Document doc = fetcher.Fetch();
//				if(doc != null){
//					YellowPagesParser parser = new YellowPagesParser(doc);
//					Link[] theLinks = parser.Parse();
//					Gson objGson = new GsonBuilder().setPrettyPrinting().create();
//					String mapToJson = objGson.toJson(theLinks);
//					output = "{\"status\":\"SUCCESS\", \"tableid\"" + " \"yp\"," + " \"domain\"" + " \"Yellow Pages\"," + "\"input\": \"" + phrase + "\", \"result\": " + mapToJson + "}";
//					return Response.status(200).entity(output).build();
//				}
//			} catch (UnsupportedEncodingException e) {}
//		}
//		return Response.status(200).entity(output).build();
//	}
	
//	@GET
//	@Path("/yellowpages")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response ypSearch(@QueryParam("phrase") String phrase, @QueryParam("loc") String loc) {
//		System.out.println("Reached 'search/yellowpages/" + phrase);
//		String output = "{\"status\":\"FAILURE\", \"err\":\"Invalid phrase : " + phrase + "\", \"errnum\" : \""
//				+ ScraperAPI.ERR_PHRASE + "\"}";
//		if (phrase.length() > 0 && loc.length() > 0) { //trivial validation
//			try {
//				Link[] theLinks = this.search(phrase, loc, ScraperAPI.YP_MODE); //this is new method call
//				if(theLinks != null){
//					String mapToJson = ScraperAPI.JsonMapLinks(theLinks);
//					output = "{\"status\":\"SUCCESS\", \"tableid\"" + " \"yelp\"," + " \"domain\"" + " \"Yelp\"," + "\"input\": \"" + phrase + "\", \"result\": " + mapToJson + "}";
//					return Response.status(200).entity(output).build();
//				}
//			} catch (UnsupportedEncodingException e) {
//				output = ScraperAPI.errorString(phrase, loc);
//			}
//		}
//		return Response.status(200).entity(output).build();
//	}
	
//	@GET
//	@Path("/yelp")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response yelpSearch(@QueryParam("phrase") String phrase, @QueryParam("loc") String loc) {
//		System.out.println("Reached 'search/yelp/" + phrase);
//		String output = "{\"status\":\"FAILURE\", \"err\":\"Invalid phrase : " + phrase + "\", \"errnum\" : \""
//				+ ScraperAPI.ERR_PHRASE + "\"}";
//		if (phrase.length() > 0 && loc.length() > 0) { //trivial validation
//			try {
//				Link[] theLinks = this.search(phrase, loc, ScraperAPI.YELP_MODE); //this is new method call
//				if(theLinks != null){
//					String mapToJson = ScraperAPI.JsonMapLinks(theLinks);
//					output = "{\"status\":\"SUCCESS\", \"tableid\"" + " \"yelp\"," + " \"domain\"" + " \"Yelp\"," + "\"input\": \"" + phrase + "\", \"result\": " + mapToJson + "}";
//					return Response.status(200).entity(output).build();
//				}
//			} catch (UnsupportedEncodingException e) {
//				output = ScraperAPI.errorString(phrase, loc);
//			}
//		}
//		return Response.status(200).entity(output).build();
//	}
	@GET
	@Path("/yellowpages")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response ypSearch(@QueryParam("phrase") String phrase, @QueryParam("loc") String loc) {
		return Response.status(200).entity(this.start(phrase, loc, ScraperAPI.YP_ID, ScraperAPI.YP_MODE)).build();
	}
	
	@GET
	@Path("/yelp")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response yelpSearch(@QueryParam("phrase") String phrase, @QueryParam("loc") String loc) {
		return Response.status(200).entity(this.start(phrase, loc, ScraperAPI.YELP_ID, ScraperAPI.YELP_MODE)).build();
	}
	
	private String start(String phrase, String loc, String tabid, int mode){
		if (phrase.length() > 0 && loc.length() > 0) { //trivial validation
			try {
				Link[] theLinks = this.search(phrase, loc, mode); //this is new method call
				if(theLinks != null){
					String mapToJson = ScraperAPI.JsonMapLinks(theLinks);
					return "{\"status\":\"SUCCESS\"," +
						   "\"tableid\":" + " \"" + tabid + "\"," + 
					       "\"domain\":" + " \"" + tabid +"\"," + 
						   "\"input\":"  + " \"" + phrase + "\"," + 
					       "\"result\":" + mapToJson + "}";
				}
			} catch (UnsupportedEncodingException e) {
			}
		}
		return ScraperAPI.errorString(phrase, loc);
	}
	
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
	
	private static String JsonMapLinks(Link[] links){
		Gson objGson = new GsonBuilder().setPrettyPrinting().create();
		return objGson.toJson(links);
	}
	
	private static String errorString(String phrase, String loc){
		return "{\"status\":\"FAILURE\", " + 
				"\"err\":\"INVALID ENCODING\", " + 
				" \"phrase\":\"" + phrase + "\", " + 
				"\"loc\" : \"" + loc + "\", " + 
				"\"errnum\" : \"" + ScraperAPI.ERR_ENCODING + "\"}";
	}
}
