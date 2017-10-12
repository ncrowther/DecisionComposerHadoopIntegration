package restapi;

import java.io.ByteArrayOutputStream;

public class ClassLib {
	
	private String libname = "";
	private String className = "";
	private ByteArrayOutputStream bufferArr = null;

	public ClassLib(String libname, String className, ByteArrayOutputStream bufferArr) {
		super();
		this.libname = libname;
		this.className = className;
		this.bufferArr = bufferArr;
	}
	
	public ByteArrayOutputStream getBufferArr() {
		return bufferArr;
	}

	public String getLibname() {
		return libname;
	}

	public String getClassName() {
		return className;
	}

}
