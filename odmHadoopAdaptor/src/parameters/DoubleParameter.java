package parameters;

public class DoubleParameter extends RuleParameter {

	
	public DoubleParameter(String direction, String kind, String name ) {
		super(direction, kind, name);
	}
	
	public Object convert(String stringValue) throws ConversionException {

		if ((stringValue == null)) {
			// This is a mandatory variable.
			//throw new ConversionException(stringName + ": Missing Integer parameter ");
			
			return 0d;

		} else {

			Double doubleValue = null;
			try {
				doubleValue = Double.parseDouble(stringValue);
			} catch (NumberFormatException e) {
				doubleValue = 0d;
				throw new ConversionException("Error: Cannot parse double: "
						+ stringValue + " for parameter " + getName());
			}
			return doubleValue;
		}
	}
}
