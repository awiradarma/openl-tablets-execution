package andre.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ValueObject {
	
	private Object generatedObject = null;
	private String className = null;
	private HashMap<String, String> fields = new HashMap<String, String>();
	
	public ValueObject() {
	}
	
	public ValueObject generateEmptyCopy() {
		if (this.generatedObject == null) return null;
		ValueObject copy = new ValueObject();
		copy.className = this.className;
		copy.fields = new HashMap<String, String>();
		copy.fields.putAll(this.fields);
		return copy;
	}
	
	public ValueObject(Object o) {
		this.generatedObject = o;
		this.className = o.getClass().getName();
		Field[] fs = o.getClass().getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field field = fs[i];
			this.fields.put(field.getName(), field.getType().getName());
		}
	}
	
	public ValueObject addField(String name, String type) {
		if (generatedObject==null) this.fields.put(name, type);
		return this;
	}
		
	public ValueObject setClassName(String className) {
		if (this.className == null) this.className = className;
		return this;
	}
	
	public ValueObject build() {
		if (generatedObject == null) {
			try {
				this.generatedObject = ByteBuddyHelper.generateObject(this.className, fields);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this;
	}
	
	public ValueObject set(String fieldName, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (this.generatedObject != null) {
			//System.out.println("fieldName is " + fieldName + " this object is a " + this.generatedObject.getClass().getName() + " first argument is a " + args[0].getClass().getName());
			ByteBuddyHelper.invokeMethod(this.generatedObject, "set"+ByteBuddyHelper.capitalize(fieldName), args);
		}
		return this;
	}

	public Object get(String fieldName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (this.generatedObject != null) {
			return ByteBuddyHelper.invokeMethod(this.generatedObject, "get" + ByteBuddyHelper.capitalize(fieldName), null);
		}
		return null;
	}
	
	public Object getObject() {
		if (this.generatedObject != null) {
			return this.generatedObject;
		} else {
			return null;
		}
	}
	
	public String toString() {
		if (this.generatedObject != null) {
			return this.generatedObject.toString();
		} else {
			return "";
		}
	}
}
