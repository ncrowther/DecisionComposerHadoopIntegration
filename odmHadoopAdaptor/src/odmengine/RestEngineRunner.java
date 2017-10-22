package odmengine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class RestEngineRunner implements IRuleEngineRunner {

	private final HttpClient client = new DefaultHttpClient();
	private HttpPost httpPost;
	
	private static final Logger LOG = Logger.getLogger(RestEngineRunner.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see odmengine.IRuleEngineRunner#initialise()
	 */
	@Override
	public void initialise(final String rulesetPath, final String host,
			final String user, final String resPwd, final String executionPwd, boolean https) throws Exception {

		// Now initialise the REST client
		final String restUrl;
		
		// Now determine RES URL depending on whether it is HTTPS or HTTP		
		if (https) {
			restUrl = "https://"  + host + ":443" + "/DecisionService/rest/v1" + rulesetPath + "/json";  
		} else {
			restUrl = "http://"  + host +  "/DecisionService/rest/v1" + rulesetPath + "/json";  
		}
		
		LOG.info("REST Execution URL: " + restUrl);
		
		httpPost = new HttpPost(restUrl);  
		
	    httpPost.addHeader("Authorization", executionPwd); 
	    httpPost.addHeader("Content-Type", "application/json");
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see odmengine.IRuleEngineRunner#runRules(java.util.Map)
	 */
	public String runRules(String jsonData) {

		StringEntity input = null;
		StringBuffer responseBuf = new StringBuffer("");

		try {
			
			input = new StringEntity(jsonData);

			httpPost.setEntity(input);
			HttpResponse httpResponse = client.execute(httpPost);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent()));
			
			String line = rd.readLine();

			while (line != null) {

				responseBuf.append(line);
				line = rd.readLine();
			}
	
		
		} catch (Exception e) {
			
			LOG.log(Level.SEVERE, "Failed to read output parameters: " + e.toString());
			e.printStackTrace();
		}

		return responseBuf.toString();
	}
}
