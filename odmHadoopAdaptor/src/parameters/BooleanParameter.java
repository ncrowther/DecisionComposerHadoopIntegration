package parameters;

public class BooleanParameter extends RuleParameter {

	
	public BooleanParameter(String direction, String kind, String name ) {
		super(direction, kind, name);
	}
	
	public Object convert(String stringValue) throws ConversionException {

		if ((stringValue == null)) {
			// This is a mandatory variable.
			//throw new ConversionException(stringName + ": Missing Integer parameter ");
			
			return false;

		} else {

			Boolean BooleanValue = null;
			try {
				BooleanValue = Boolean.parseBoolean(stringValue);
			} catch (NumberFormatException e) {
				BooleanValue = false;
				throw new ConversionException("Error: Cannot parse Boolean: "
						+ stringValue + " for parameter " + getName());
			}
			return BooleanValue;
		}
	}
}
