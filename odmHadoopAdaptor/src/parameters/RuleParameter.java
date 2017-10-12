package parameters;


import parameters.ConversionException;


public abstract class RuleParameter {

	private String direction;
	private String kind;
	private String name;

	public RuleParameter(String direction, String  kind, String name) {
		this.direction = direction;
		this.kind = kind;
		this.name = name;
	}

	@Override
	public String toString() {
		return "RuleParameter [direction=" + direction + ", kind=" + kind
				+ ", name=" + name  + ", type=" + this.getClass().getName() +"]";
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public abstract Object convert(String stringValue) throws ConversionException;

}
