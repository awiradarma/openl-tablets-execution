package andre.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
		copy.build();
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
			invokeMethod(this.generatedObject, "set"+ByteBuddyHelper.capitalize(fieldName), args);
		}
		return this;
	}

	public Object get(String fieldName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (this.generatedObject != null) {
			return invokeMethod(this.generatedObject, "get" + ByteBuddyHelper.capitalize(fieldName), null);
		}
		return null;
	}
	
	public Object getWrappedObject() {
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
	
	private Object invokeMethod(Object o, String methodName, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method m = findMethod(o, methodName);		
//		System.out.println(o.getClass().getName());
//		Parameter[] p = m.getParameters();
//		for (int i = 0; i < p.length; i++) {
//			Parameter parameter = p[i];
//			System.out.println(parameter.getType().getName());
//		}
		return m.invoke(o, args);
	}

	private Method findMethod(Object o, String methodName) {
		Method[] methods = o.getClass().getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			//System.out.println(method.getName());
			if (method.getName().equalsIgnoreCase(methodName)) return method;
		}
		return null;
	}

	public HashMap<String, String> getFields() {
		return this.fields;
	}
	
	public List<String> getFieldNames() {
		ArrayList<String> fieldNames = new ArrayList<String>();
		fieldNames.addAll(fields.keySet());
		return fieldNames;
	}
	
	public static ValueObject[] generateValueObjectArray(Object[] objArray) {
		ValueObject[] outputArray = new ValueObject[objArray.length];
		for (int i = 0; i < outputArray.length; i++) {
			outputArray[i] = new ValueObject(objArray[i]);
		}
		return outputArray;

	}
}
