package restapi;

import java.io.ByteArrayOutputStream;

public class RestClassLoader extends ClassLoader {

	String host;
	String user;
	String password;
	boolean https;
	
	public RestClassLoader(ClassLoader parent, String host, String user, String password, boolean https) {
		super(parent);
		this.host = host;
		this.user = user;
		this.password = password;
		this.https = https;
	}

	public Class loadClass(String classname, String xomLib, ByteArrayOutputStream bufferArr)
			throws ClassNotFoundException {

		System.out.println("Loading class: " + classname);
		
		Class returnClass = null;

		// delegate to parent first

		try {
			returnClass = this.getParent().loadClass(classname);
		} catch (ClassNotFoundException e) {
			// do nothing, returnClass is still null
		}

		if (returnClass == null) {
			
			System.out.println("Custom loading class: " + classname);

			System.out.println("Check in Xom: " + xomLib);

			//byte[] classData = RestXomExtractor.getXomAsByteStream(classname,
			//		host, xomLib, user, password, https);
			
			byte[] classData = bufferArr.toByteArray();
			
			if (classData != null && classData.length > 0) {

				classname = classname.replace(".class", "");
				returnClass = defineClass(classname, classData, 0,
						classData.length);

			}
		}

		if (returnClass == null) {
			throw new ClassNotFoundException(
					"ODM REST loader: class not found: " + classname);
		}

		return returnClass;

	}

	private static void displayClassInfo(Class restClass) {
		System.out.println("Class loaded = " + restClass.getCanonicalName());

		/*
		 * Field[] fields = restClass.getDeclaredFields(); Method[] methods =
		 * restClass.getDeclaredMethods();
		 * 
		 * System.out.println("\nFields:"); for (Field f : fields) {
		 * System.out.println("\t" + f.getName()); }
		 * 
		 * System.out.println("\nMethods:"); for (Method m : methods) {
		 * System.out.println("\t" + m.getName()); }
		 */
	}

	public static void main(String[] args) throws IllegalAccessException,
			InstantiationException {
		
		String host = "localhost:9081";
		String user = "resAdmin";
		String password = "resAdmin";
		boolean https = false;
		
		ClassLoader parentClassLoader = RestClassLoader.class.getClassLoader();
		RestClassLoader classLoader = new RestClassLoader(parentClassLoader, host, user, password, https);

		Class restClass = null;

//		try {
//			restClass = classLoader.loadClass("isb.rules.mortgagesegmentation.Loan", "	MortgageSegmentation_XOM.zip/1.0");
//
//			displayClassInfo(restClass);
//
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}

	}

}
