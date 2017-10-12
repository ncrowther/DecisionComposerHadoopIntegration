package parameters;

public class StringParameter extends RuleParameter {

	public StringParameter(String direction, String kind, String name ) {
		super(direction, kind, name);
	}
	
	public Object convert(String stringValue) throws ConversionException {

		if ((stringValue == null)) {
			return "";
		} else {
			return stringValue;
		}
	}
}
