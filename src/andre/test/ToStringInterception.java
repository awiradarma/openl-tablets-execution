package andre.test;

import java.lang.reflect.Field;

import net.bytebuddy.implementation.bind.annotation.This;


public class ToStringInterception {

	public static String intercept(@This Object type) {
		StringBuilder s = new StringBuilder("{ ");
		Field[] fs = type.getClass().getDeclaredFields();
		Field field;
		boolean isNumeric;
		for (int i = 0; i < fs.length; i++) {
			field = fs[i];
			isNumeric = false;
			if ((field.getType() == Integer.class) || (field.getType() == Double.class)) {
				isNumeric = true;
			}
			field.setAccessible(true);
			try {
				s.append("\"").append(field.getName()).append("\": " );
				if (!isNumeric) s.append("\"");
				s.append(field.get(type));
				if (!isNumeric) s.append("\"");
				if (i < fs.length - 1) s.append(", ");
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		s.append(" }");
		return s.toString();
	}
}
