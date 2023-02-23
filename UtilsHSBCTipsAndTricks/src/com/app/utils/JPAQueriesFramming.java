package com.app.utils;

import java.lang.reflect.Method;

import com.app.utils.domain.User;

public class JPAQueriesFramming {

	static String EMPTY_STRING = "";
	static String AND_CLAUSE_KEY = " AND obj.";

	public static void main(String[] args) {
		User user = new User();
		user.setAddress("Vizag");
		user.setName("Lova Chittumuri");
		getAllMatchingItemQuery(user);
	}

	static void getAllMatchingItemQuery(Object object) {
		StringBuilder sb = new StringBuilder("SELECT * from ").append(object.getClass().getSimpleName())
				.append(" obj Where 1=1");
		getAllWhereClause(object, sb);
	}

	static void getAllWhereClause(Object object, StringBuilder sb) {
		Method[] methods = object.getClass().getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.startsWith("get")) {
				try {
					Object tempObj = method.invoke(object, null);
					String fieldName = removeGetFromMethod(methodName, true);
					if (tempObj instanceof Long) {
						sb.append(AND_CLAUSE_KEY).append(fieldName).append("=").append((Long) tempObj);
					} else if (tempObj instanceof Integer) {
						sb.append(AND_CLAUSE_KEY).append(fieldName).append("=").append((Integer) tempObj);
					} else if (tempObj instanceof String && !((String) tempObj).isEmpty()) {
						sb.append(AND_CLAUSE_KEY).append(fieldName).append("='").append((String) tempObj).append("'");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(sb.toString());
	}

	public static String removeGetFromMethod(String str, boolean lowercaseFlag) {
		String string = str.substring(3, str.length());
		String newString;
		if (lowercaseFlag) {
			newString = Character.toLowerCase(string.charAt(0))
					+ (string.length() > 1 ? string.substring(1) : EMPTY_STRING);
		} else {
			newString = Character.toUpperCase(string.charAt(0))
					+ (string.length() > 1 ? string.substring(1) : EMPTY_STRING);
		}
		return newString;
	}
}
