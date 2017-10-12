package restapi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import parameters.ConversionException;
import parameters.RuleParameter;
import parameters.RuleParameterFactory;

public class RulesetSignatureParser {

	
	private static final Logger LOG = Logger.getLogger(RulesetSignatureParser.class.getName());
	
	public static class RulesetSignatureSaxParser extends DefaultHandler {

		
		private enum ElementName {
			PARAMETER("parameter"), DIRECTION("direction"), KIND("kind"), NAME(
					"name"), TYPE("type"), NOT_USED("notUsed");

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
		
		private static List<RuleParameter> rulesetSignatures = new ArrayList<RuleParameter>();

		private static String text = null;
		private static String direction = null;
		private static String kind = null;
		private static String name = null;
		private static String type = null;		


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
				case PARAMETER: 
					
				// The end tag of an RulesetSignature was encountered, so add
				// the RulesetSignature to the list.
				
				RuleParameter sig =  null;
				try {
					sig = RuleParameterFactory.create(direction, kind, name, type);
				} catch (ConversionException e) {
					//e.printStackTrace();
					final String errorMessage = "Failed to create parameter '" + name + "' of type: " + type;
					LOG.log(Level.SEVERE, errorMessage);
					throw new SAXException(errorMessage);
				}
				rulesetSignatures.add(sig);
			
			case DIRECTION:
				direction = text;
			
			case KIND:
				kind = text;
				
			case NAME:
				name = text;
				
			case TYPE:
				type = text;
				
			default:
				
			}				
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			text = String.copyValueOf(ch, start, length).trim();
		}

		public  List<RuleParameter> getRulesetSignatures() {
			return rulesetSignatures;
		}

	}

	public static List<RuleParameter> parse(InputStream xml)
			throws ParserConfigurationException, SAXException, IOException {

		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
		
		RulesetSignatureSaxParser handler = new RulesetSignatureSaxParser();	
		
		parser.parse(xml, handler);
		
		return handler.getRulesetSignatures();
	}

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {

		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><signature><parameter><direction>IN</direction><kind>Java</kind><name>customerName</name><type>java.lang.String</type></parameter><parameter><direction>OUT</direction><kind>Java</kind><name>response</name><type>java.lang.String</type></parameter><parameter><direction>IN</direction><kind>Java</kind><name>request</name><type>java.lang.Integer</type></parameter><parameter><direction>INOUT</direction><kind>Java</kind><name>birthDate</name><type>java.util.Date</type></parameter></signature>";

		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
			
		RulesetSignatureSaxParser handler = new RulesetSignatureSaxParser();

		InputStream in = IOUtils.toInputStream(xml, "UTF-8");
		
		LOG.info("XML:" + in);

		 parser.parse(in, handler);
		
		// Print all RulesetSignatures.
		for (RuleParameter sig : handler.getRulesetSignatures())
			System.out.println(sig.toString());
	}
	
	

}
