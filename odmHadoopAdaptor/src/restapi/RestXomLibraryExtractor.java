package restapi;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
public class RestXomLibraryExtractor {

	private static final Logger LOG = Logger.getLogger(RestXomLibraryExtractor.class.getName());

	
	public static List<String> getXomList(String host, final String username, final String password, String ruleset, boolean https) throws IOException, ParserConfigurationException, SAXException {

		
		String restQuery = getRestUrl(host, ruleset, https);
	
		// Determine the rule set signature
		LOG.info("XOM REST query: " + restQuery);

		List<String> xomList = new ArrayList<String>();

		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password
						.toCharArray());
			}
		});

		URL url = new URL(restQuery);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/xml");

		if (conn.getResponseCode() != 200) {
			throw new IOException(
					"Failed to execute REST XOM query from RES. Query was: " 
			        + restQuery +  ".\n Is the res url correct?  Are the user/password credetials correct?  \nHTTP error code : " + conn.getResponseCode());
		}

		// get result
		InputStream xml = conn.getInputStream();

		xomList = XomLibParser.parse(xml);
		
		LOG.info("Xoms: " + xomList);
		
		System.out.println("Xoms: " + xomList);

		conn.disconnect();

		return xomList;

	}
	
	private static String getRestUrl(String host, String ruleset, boolean https) {
		
		final String REST_QUERY_URL;
		if (https) {
			REST_QUERY_URL = "https://" + host + "/res/api/v1/ruleapps" + ruleset + "/properties";
		} else {
			REST_QUERY_URL = "http://" + host + "/res/api/v1/ruleapps" + ruleset + "/properties";
		}

		return REST_QUERY_URL;
	}
	
	public static void main(String[] args) {
		   
		String host = "brsv2-b49ea470.eu-gb.bluemix.net";  
		String password = "1k1r9501uzqyc";

		//String host = "localhost:9090";  
		//String password = "resAdmin";
		
		try {
			getXomList(host, "resAdmin", password, "/CarRentalRuleApp/1.0/CarRental/1.0",  true);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}		
	}	
}
