package andre.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.IsNamedMatcher;
import net.bytebuddy.matcher.NameMatcher;

public class ByteBuddyHelper {
	
	private static ConcurrentHashMap<String, Class> generatedClasses = new ConcurrentHashMap<>();

	public static void main(String[] args) throws Exception{
		Map<String, String> m = new HashMap<String, String>();
//		m.put("className", "org.openl.generated.beans.Policy1");
//		m.put("classFieldCount", "5");
		m.put("policyID", "java.lang.String");
		m.put("driverID", "java.lang.String");
		m.put("vehicleID", "java.lang.String");
		m.put("effectiveDate", "java.util.Date");
		m.put("isRenewal", "java.lang.Boolean");

//		Class<?> c = generateClass(m);
		
		Object o = generateObject("org.openl.generated.beans.Policy1", m);
		invokeMethod(o, "setEffectiveDate", new Date());
		invokeMethod(o, "setPolicyID", "This is the policy ID 1234");
//		Method method = findMethod(o, "setEffectiveDate");
//		method.invoke(o, new Date("2/2/2012"));
//		method = findMethod(o, "getEffectiveDate");
//		System.out.println(method.invoke(o, null));
		System.out.println(invokeMethod(o, "getEffectiveDate", null));
	}

	public static Object invokeMethod(Object o, String methodName, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method m = findMethod(o, methodName);		
//		System.out.println(o.getClass().getName());
//		Parameter[] p = m.getParameters();
//		for (int i = 0; i < p.length; i++) {
//			Parameter parameter = p[i];
//			System.out.println(parameter.getType().getName());
//		}
		return m.invoke(o, args);
	}

	public static Object generateObject(String className, Map<String, String> m) throws Exception {
		Class<?> c = generateClass(className, m);
		return c.newInstance();
	}
	
	public static Class<?> generateClass(String className, Map<String, String> m) {
		if (generatedClasses.containsKey(className)) return generatedClasses.get(className);
		
		DynamicType.Builder<?> dynamicType = new ByteBuddy()
			.subclass(Object.class).name(className);
		
//		int numFields = m.size(); // Integer.parseInt(m.get("classFieldCount"));
		for (Iterator iterator = m.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();

			Type t = null;
			switch (entry.getValue()) {
			case "String":
				t = String.class;
				break;
			case "Boolean":
				t = Boolean.class;
				break;
			case "Date":
				t = Date.class;
				break;
			case "Double":
				t = Double.class;
				break;
			case "Integer":
				t = Integer.class;
				break;
			case "String[]":
				t = String[].class;
				break;
			case "Boolean[]":
				t = Boolean[].class;
				break;
			case "Date[]":
				t = Date[].class;
				break;
			case "Double[]":
				t = Double[].class;
				break;
			case "Integer[]":
				t = Integer[].class;
				break;
			default:
				if (generatedClasses.containsKey(entry.getValue())) {
					t = generatedClasses.get(entry.getValue());
				}
				break;
			}
			dynamicType = dynamicType.defineField(entry.getKey(), t, Visibility.PRIVATE);
			dynamicType = dynamicType.defineMethod("get"+capitalize(entry.getKey()), t, Visibility.PUBLIC).intercept(FieldAccessor.ofBeanProperty())
			.defineMethod("set"+capitalize(entry.getKey()), Void.TYPE, Visibility.PUBLIC).withParameter(t).intercept(FieldAccessor.ofBeanProperty());
		}
		Class<?> c = dynamicType.method(ElementMatchers.named("toString")).intercept(MethodDelegation.to(ToStringInterception.class)).
				make().load(ByteBuddyHelper.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
        .getLoaded();
		generatedClasses.put(className, c);
		return c;
	}

	private static Method findMethod(Object o, String methodName) {
		Method[] methods = o.getClass().getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			//System.out.println(method.getName());
			if (method.getName().equalsIgnoreCase(methodName)) return method;
		}
		return null;
	}

	static String capitalize(final String word) {
		   return Character.toUpperCase(word.charAt(0)) + word.substring(1);
		}
}
