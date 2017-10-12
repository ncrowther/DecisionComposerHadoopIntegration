package parameters;

import java.io.IOException;
import java.text.ParseException;


import org.codehaus.jackson.map.ObjectMapper;

public class ClassParameter extends RuleParameter {
	
	private Class classname;

	public ClassParameter(String direction, String kind, String name, Class classname) {
		super(direction, kind, name);
		
		this.classname = classname;
	}

	public Object convert(String stringValue) throws ConversionException {

		if ((stringValue == null)) {
			throw new ConversionException(stringValue + ": Null data");
		} else {

			Object obj = null;

			try {
				obj = extractObjectFromJson(stringValue);
			} catch (ParseException e) {
				throw new ConversionException("Error: Cannot parse date: "
						+ stringValue + " for parameter " + getName());
			}
			return obj;
		}
	}

	private Object extractObjectFromJson(String jsonString) throws ParseException {
		 
			Object jsonSerializer = null;
			try {	
				
				ObjectMapper mapper = new ObjectMapper();

				// Convert JSON string to Object			
				jsonSerializer = mapper.readValue(jsonString, classname); 
				
			} catch ( IOException e) {
	
				e.printStackTrace();
			}
			
			return jsonSerializer;
	}

}
