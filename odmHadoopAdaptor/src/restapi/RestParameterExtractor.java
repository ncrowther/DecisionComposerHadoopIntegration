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

import parameters.RuleParameter;

public class RestParameterExtractor {

	private static final String SIGNATURE_QUERY = "/signature";

	private static final Logger LOG = Logger.getLogger(RestParameterExtractor.class.getName());

	
	public static List<RuleParameter> getSignature(String host,
			String rulesetPath, final String username, final String password, boolean https) throws IOException, ParserConfigurationException, SAXException {

		
		String restQuery = getRestUrl(host, rulesetPath, https);
		
		restQuery += SIGNATURE_QUERY;
	
		// Determine the rule set signature
		LOG.info("Signature REST query: " + restQuery);

		List<RuleParameter> signature = new ArrayList<RuleParameter>();

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
					"Failed to get REST parameters from RES. Query was: " 
			        + restQuery +  ".\n Is the ruleset correct?  Are the user/password credetials correct?  \nHTTP error code : " + conn.getResponseCode());
		}

		// get result
		InputStream xml = conn.getInputStream();

		signature = RulesetSignatureParser.parse(xml);
		
		LOG.info("Signature: " + signature);
		
		System.out.println("Signature: " + signature);

		conn.disconnect();

		return signature;

	}
	
	private static String getRestUrl(String host, String rulesetPath, boolean https) {
		
		final String RES_URL;
		if (https) {
			RES_URL = "https://" + host + "/res/api/v1/ruleapps";
		} else {
			RES_URL = "http://" + host + "/res/apiauth/v1/ruleapps";
		}

		String REST_QUERY = RES_URL + rulesetPath;
		return REST_QUERY;
	}
	
	public static void main(String[] args) {
		   
		String host = "brsv2-cc7fcf8f.ng.bluemix.net";  
		String password = "1hrbg76fu9j6p";

		//String host = "localhost:9080";  
		//String password = "resAdmin";
		
		String rulesetPath = "/validatePnrApp/1.0/validatePnr/1.0"; 
		try {
			getSignature(host, rulesetPath, "resAdmin", password, true);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}		
	}	
}
