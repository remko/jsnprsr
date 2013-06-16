/*
 * Copyright (c) 2013 Remko Tronçon
 * Licensed under the simplified BSD license.
 * See COPYING file for more information.
 */

package re.mko.jsnprsr;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SerializerTest {
	private Writer output;

	@Before
	public void setUp() {
		output = new StringWriter();
	}

	private String serialize(Value value) throws JSONException {
		Serializer.serialize(value, output);
		return output.toString();
	}

	@Test
	public void testEmptyString() throws JSONException {
		assertEquals("\"\"", serialize(Value.string("")));
	}

	@Test
	public void testSimpleString() throws JSONException {
		assertEquals("\"foo\"", serialize(Value.string("foo")));
	}

	@Test
	public void testStringWithFeedCharacters() throws JSONException {
		assertEquals("\"\\b\\\"\\n\"", serialize(Value.string("\b\"\n")));
	}

	@Test
	public void testArray() throws JSONException {
		Value value = Value.array(Value.string("foo"), Value.string("bar"), Value.string("baz"));
		assertEquals("[\"foo\",\"bar\",\"baz\"]", serialize(value));
	}

	@Test
	public void testEmptyArray() throws JSONException {
		assertEquals("[]", serialize(Value.array()));
	}

	@Test
	public void testTrue() throws JSONException {
		assertEquals("true", serialize(Value.TRUE));
	}

	@Test
	public void testFalse() throws JSONException {
		assertEquals("false", serialize(Value.FALSE));
	}

	@Test
	public void testNull() throws JSONException {
		assertEquals("null", serialize(Value.NULL));
	}

	@Test
	public void testObject() throws JSONException {
		Map<String, Value> map = new HashMap<String, Value>();
		map.put("foo", Value.string("bar"));
		map.put("baz", Value.TRUE);
		map.put("bam", Value.NULL);
		assertEquals("{\"bam\":null,\"baz\":true,\"foo\":\"bar\"}", serialize(Value.object(map)));
	}

	@Test
	public void testEmptyObject() throws JSONException {
		assertEquals("{}", serialize(Value.object()));
	}

	@Test
	public void testNegativeInteger() throws JSONException {
		assertEquals("-54", serialize(Value.number(-54)));
	}

	@Test
	public void testPositiveInteger() throws JSONException {
		assertEquals("54", serialize(Value.number(54)));
	}

	@Test
	public void testFloat() throws JSONException {
		assertEquals("0.314", serialize(Value.number(0.314)));
	}

	public void testFloatWithFractionalAndExponent() throws JSONException {
		assertEquals("31.415e12", serialize(Value.number(31.415e12)));
	}
}
