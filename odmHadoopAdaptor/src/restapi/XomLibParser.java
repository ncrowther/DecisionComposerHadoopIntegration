package restapi;


import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XomLibParser {

	
	private static final Logger LOG = Logger.getLogger(XomLibParser.class.getName());
	
	public static class XomLibSaxParser extends DefaultHandler {

		
		private enum ElementName {
			PROPERTIES("properties"), PROPERTY("property"), ID("id"), VALUE("value"), NOT_USED("notUsed");

			private final String text;

			ElementName(String name) {
				this.text = name;
			}

			public static ElementName fromString(String text) {
				if (text != null) {
					for (ElementName b : ElementName.values()) {
						if (text.equalsIgnoreCase(b.text)) {
							return b;
						}
					}
				}
				return NOT_USED;
			}

		};
		
		private static List<String> xomLibs = new ArrayList<String>();
		
		private static String text = null;
		
		private static String managedXoms = "";
		
		private static boolean managedXOMState = false;

		@Override
		// A start tag is encountered.
		public  void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
		}

		@Override
		public  void endElement(String uri, String localName, String qName)
				throws SAXException {

			ElementName element = ElementName.fromString(qName);
			
			switch (element) {
				case ID: 
					if (text.equals("ruleset.managedxom.uris")) {
						managedXOMState = true;
					}
					break;
					
				case VALUE:
					
					if (managedXOMState) {
						managedXoms = text.replace("resuri://", "");	
						
						String delims = "[,]";
						String[] tokens = managedXoms.split(delims);
						
						for (int i = 0; i < tokens.length; i++) {
							xomLibs.add(tokens[i]);
						}
						
						managedXOMState = false;
					}
				   			   
			default:
				
			}				
		}
		
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			text = String.copyValueOf(ch, start, length).trim();
		}
		
		public  List<String> getXomLibs() {
			return xomLibs;
		}		
		
		public String getManagedXoms() {
			return managedXoms;
		}

	}

	public static List<String> parse(InputStream xml)
			throws ParserConfigurationException, SAXException, IOException {

		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
		
		XomLibSaxParser handler = new XomLibSaxParser();	
		
		parser.parse(xml, handler);
		
		return handler.getXomLibs();
	}
	

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {

		String xml = "<properties><property><id>ruleset.engine</id><value>de</value></property><property><id>ruleset.managedxom.uris</id><value>resuri://dc-math_Xom.jar/1.0,resuri://dc-date_Xom.jar/1.0,resuri://LoanValidationXom.jar/1.0,</value></property></properties>";
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
			
		XomLibSaxParser handler = new XomLibSaxParser();

		InputStream in = IOUtils.toInputStream(xml, "UTF-8");
		
		LOG.info("XML:" + in);

		 parser.parse(in, handler);
		
		// Print all RulesetSignatures.
		System.out.println( handler.getManagedXoms());
		System.out.println( handler.getXomLibs());
	}
	
	

}
