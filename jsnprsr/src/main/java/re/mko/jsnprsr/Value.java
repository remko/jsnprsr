/*
 * Copyright (c) 2013 Remko Tronçon
 * Licensed under the MIT license.
 * See COPYING file for more information.
 */

package re.mko.jsnprsr;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Value {
	public enum Type {
		NULL,
		STRING,
		NUMBER,
		OBJECT,
		ARRAY,
		BOOLEAN
	}
	public static final Value TRUE = new Value(true);
	public static final Value FALSE = new Value(false);
	public static final Value NULL = new Value();	
	private static final Map<String, Value> STRING_VALUES = new HashMap<String, Value>();
	private final Type type;
	private final java.lang.Object value;

	private Value(String s) {
		type = Type.STRING;
		value = s;
	}

	private Value(Number n) {
		type = Type.NUMBER;
		value = n;
	}

	private Value(Map<String, Value> o) {
		type = Type.OBJECT;
		value = o;
	}

	private Value(List<Value> l) {
		type = Type.ARRAY;
		value = l;
	}

	private Value(boolean b) {
		type = Type.BOOLEAN;
		value = Boolean.valueOf(b);
	}

	private Value() {
		type = Type.NULL;
		value = null;
	}

	public Type getType() {
		return type;
	}

	public String getString() {
		return (String) value;
	}

	@SuppressWarnings("unchecked")
	public List<Value> getArray() {
		return (List<Value>) value;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Value> getObject() {
		return (Map<String, Value>) value;
	}

	public Number getNumber() {
		return (Number) value;
	}

	public boolean getBoolean() { // NOPMD - This actually needs to be a getX()
		return ((Boolean) value).booleanValue();
	}

	public boolean isNull() {
		return value == null;
	}

	public static Value string(String s) {
		Value value = STRING_VALUES.get(s);
		if (value == null) {
			value = new Value(s);
			STRING_VALUES.put(s, value);
		}
		return value;
	}
	
	public static Value number(Number n) {
		return new Value(n);
	}

	public static Value number(long n) {
		return new Value(Long.valueOf(n));
	}

	public static Value number(double n) {
		return new Value(Double.valueOf(n));
	}

	public static Value object(Map<String, Value> o) {
		return new Value(o);
	}
	
	public static Value object() {
		return new Value(new HashMap<String, Value>());
	}
	
	public static Value array(Value... values) {
		return array(Arrays.asList(values));
	}
	
	public static Value array(List<Value> v) {
		return new Value(v);
	}
}
