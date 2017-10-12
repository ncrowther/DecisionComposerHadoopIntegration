package parameters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParameter extends RuleParameter {
	
	private static String DATE_FORMAT = "yyyy-MM-dd";
	
	public DateParameter(String direction, String kind, String name ) {
		super(direction, kind, name);
	}

	public Object convert(String stringValue) throws ConversionException {

		if ((stringValue == null)) {
			// This is a mandatory variable.
			throw new ConversionException(stringValue
					+ ": Missing or invalid Date parameter ");

		} else {

			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			Date dateValue = new Date();
			try {
				dateValue = df.parse(stringValue);
			} catch (ParseException e) {
				throw new ConversionException("Error: Cannot parse date: "
						+ stringValue  + " for parameter " + getName());
			}
			return dateValue;
		}
	}

}
