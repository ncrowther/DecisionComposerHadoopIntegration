package restapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class RestXomExtractor {

	private static final Logger LOG = Logger.getLogger(RestXomExtractor.class
			.getName());

	public static List<ClassLib> getClassNames(final String host,
			final String xomPath, final String username, final String password,
			boolean https) {

		List<ClassLib> classNames = new ArrayList<ClassLib>();

		String restQuery = getRestUrl(host, xomPath, https);

		// Load the rule set using Rest API
		LOG.info("XOM REST query: " + restQuery);

		try {

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
				LOG.log(Level.SEVERE,
						"Failed : HTTP error code : " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			// get result

			InputStream input = conn.getInputStream();

			classNames = getClassNames(input, xomPath);

			input.close();

			input.close();
			conn.disconnect();

		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.toString());
		}

		return classNames;
	}

	private static List<ClassLib> getClassNames(InputStream input, String xomLib)
			throws IOException {

		List<ClassLib> classNames = new ArrayList<ClassLib>();
		ZipInputStream zis = new ZipInputStream(input);	

		ZipEntry zipEntry = zis.getNextEntry();
		while (zipEntry != null) {
			String fileName = zipEntry.getName();
			fileName = fileName.replace('/', '.');

			//System.out.println("**" + fileName);

			if (fileName.endsWith(".class")) {

				ByteArrayOutputStream bufferArr = new ByteArrayOutputStream();
				byte[] buffer = new byte[2046];	
				
				//
				int len;
				while ((len = zis.read(buffer)) > 0) {
					bufferArr.write(buffer, 0, len);
				}
				
				ClassLib classLib = new ClassLib(xomLib, fileName, bufferArr);
				classNames.add(classLib);
				
				//break;
			}

			zis.closeEntry();
			zipEntry = zis.getNextEntry();
		}
		zis.close();

		return classNames;
	}

	private static String getRestUrl(String host, String xomPath, boolean https) {

		final String RES_URL;
		if (https) {
			RES_URL = "https://" + host + "/res/api/v1/xoms";
		} else {
			RES_URL = "http://" + host + "/res/api/v1/xoms";
		}

		String REST_QUERY = RES_URL + "/" + xomPath + "/bytecode";
		return REST_QUERY;
	}
}
