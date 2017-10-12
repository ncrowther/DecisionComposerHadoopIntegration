
package parameters;

public class FloatParameter extends RuleParameter {

	
	public FloatParameter(String direction, String kind, String name ) {
		super(direction, kind, name);
	}
	
	public Object convert(String stringValue) throws ConversionException {

		if ((stringValue == null)) {
			// This is a mandatory variable.
			//throw new ConversionException(stringName + ": Missing Integer parameter ");
			
			return 0.0f;

		} else {

			Float floatValue = null;
			try {
				floatValue = Float.parseFloat(stringValue);
			} catch (NumberFormatException e) {
				floatValue = 0.0f;
				throw new ConversionException("Error: Cannot parse float: "
						+ stringValue + " for parameter " + getName());
			}
			return floatValue;
		}
	}
}
