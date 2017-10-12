package parameters;

public class IntegerParameter extends RuleParameter {

	
	public IntegerParameter(String direction, String kind, String name ) {
		super(direction, kind, name);
	}
	
	public Object convert(String stringValue) throws ConversionException {

		if ((stringValue == null)) {
			// This is a mandatory variable.
			//throw new ConversionException("Missing Integer parameter: "  + stringName);
			return 0;

		} else {

			Integer intValue = null;
			try {
				intValue = Integer.parseInt(stringValue);
			} catch (NumberFormatException e) {
				intValue = 0;
				throw new ConversionException("Error.  Cannot parse integer: "
						+ stringValue + " for parameter:  " + getName());
			}
			return intValue;
		}
	}
}
