package restapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class XomLoader {
	
	public static ClassLoader classloader = null;

	public static RestClassLoader  loadXomClasses(String host, String user, String password, String ruleset, boolean https) {		
		

		ClassLoader parentClassLoader = RestClassLoader.class.getClassLoader();
		RestClassLoader classLoader = new RestClassLoader(parentClassLoader, host, user, password, https);
		
		List<String> xomLibs = new ArrayList<String>();
		try {
			xomLibs = RestXomLibraryExtractor.getXomList(host, user, password, ruleset, https);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		for (String xomLib : xomLibs) {

			System.out.println("Check in Xom: " + xomLib);

			List<ClassLib> classNames = RestXomExtractor.getClassNames(host,
					xomLib, user, password, https);

			System.out.println("Classes: " + classNames);

			for (ClassLib className : classNames) {

				Class restClass = null;

				try {

					restClass = classLoader.loadClass(className.getClassName(), className.getLibname(), className.getBufferArr());
					
					System.out.println("Loaded Class: " + className.getClassName());

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		
		classloader = classLoader;
		
		return classLoader;
	}
	
	public static void main(String args[]) throws ClassNotFoundException { 
		
		String host = "localhost:9090";
		String user = "resAdmin";
		String password = "resAdmin";
		
		XomLoader.loadXomClasses(host, user, password, "	/CarRentalRuleApp/1.0/CarRental/1.0", false);
	}
		
		
}
