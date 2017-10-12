package parameters;

import java.util.Date;
import java.util.logging.Logger;

import restapi.RestClassLoader;
import restapi.XomLoader;


public class RuleParameterFactory {

	private static final Logger LOG = Logger.getLogger(RuleParameterFactory.class.getName());

	public static Class getType(String type) throws ConversionException {
		try {

			return XomLoader.classloader.loadClass(type);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ConversionException("Cannot convert class: " + type);
		}
	}
	
	public static RuleParameter create(String direction, String kind,
			String name, String type) throws ConversionException {

		LOG.info("Name::"  + name + "  type::"  + type);
		Class classType = getType(type);
				
		if (classType.equals(Date.class)) {
			//LOG.info("IN Date Param =" + name);
			return new DateParameter(direction, kind, name);
		} else if (classType.equals(Integer.class)) {
			//LOG.info("IN Integer Param =" + name);
			return new IntegerParameter(direction, kind, name);
		} else if (classType.equals(Double.class)) {
			//LOG.info("IN Date Param =" + name);
			return new DoubleParameter(direction, kind, name);
		} else if (classType.equals(String.class)) {
			//LOG.info("IN String Param =" + name);
			return new StringParameter(direction, kind, name);
		} else if (classType.equals(Float.class)) {
			//LOG.info("IN String Param =" + name);
			return new FloatParameter(direction, kind, name);
		} else if (classType.equals(Boolean.class)) {
			//LOG.info("IN String Param =" + name);
			return new BooleanParameter(direction, kind, name);
		} else {
		    System.out.println("Class Param =" + classType);
			return new ClassParameter(direction, kind, name, classType);			
		}
	}
}
