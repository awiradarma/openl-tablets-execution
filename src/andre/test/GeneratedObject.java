package andre.test;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class GeneratedObject {
	
	private Object generatedObject = null;
	private String className = null;
	private HashMap<String, String> fields = new HashMap<String, String>();
	
	public GeneratedObject addField(String name, String type) {
		this.fields.put(name, type);
		return this;
	}
		
	public GeneratedObject setClassName(String className) {
		if (this.className == null) this.className = className;
		return this;
	}
	
	public GeneratedObject build() {
		if (generatedObject == null) {
			try {
				this.generatedObject = ByteBuddyHelper.generateObject(this.className, fields);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this;
	}
	
	public void set(String fieldName, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (this.generatedObject != null) {
			ByteBuddyHelper.invokeMethod(this.generatedObject, "set"+ByteBuddyHelper.capitalize(fieldName), args);
		}
	}

	public Object get(String fieldName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (this.generatedObject != null) {
			return ByteBuddyHelper.invokeMethod(this.generatedObject, "get" + ByteBuddyHelper.capitalize(fieldName), null);
		}
		return null;
	}
}
