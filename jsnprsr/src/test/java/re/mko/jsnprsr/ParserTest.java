/*
 * Copyright (c) 2013 Remko Tronçon
 * Licensed under the simplified BSD license.
 * See COPYING file for more information.
 */

package re.mko.jsnprsr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

public class ParserTest {
	private Reader input;

	private Parser createParser(String s) {
		input = new StringReader(s);
		return new Parser(input);
	}

	@Test(expected = JSONException.class)
	public void testNoInput() throws JSONException {
		createParser("").parse();		
	}
	
	@Test
	public void testEmptyString() throws JSONException {
		assertEquals("", createParser("\"\"").parse().getString());
	}

	@Test
	public void testSimpleString() throws JSONException {
		assertEquals("foo", createParser("\"foo\"").parse().getString());
	}

	@Test
	public void testUnicodeString() throws JSONException {
		assertEquals("Tron\u00e7on", createParser("\"Tron\\u00e7on\"").parse().getString());
	}

	@Test
	public void testStringWithFeedCharacters() throws JSONException {
		assertEquals("\b\"\n", createParser("\"\\b\\\"\\n\"").parse().getString());
	}

	@Test
	public void testStringWithWhitespace() throws JSONException {
		assertEquals("foo", createParser("   \"foo\"").parse().getString());
	}

	@Test
	public void testArray() throws JSONException {
		List<Value> array = createParser("[\"foo\", \"bar\", \"baz\"]").parse().getArray();
		assertEquals(3, array.size());
		assertEquals("foo", array.get(0).getString());
		assertEquals("bar", array.get(1).getString());
		assertEquals("baz", array.get(2).getString());
	}
	
	@Ignore("See Parser.readArray to see how to support this")
	@Test
	public void testArrayWithTrailingComma() throws JSONException {
		List<Value> array = createParser(
				"[\n" +
					"\"foo\",\n" +
					"\"bar\",\n" +
					"\"baz\",\n" +
				"]").parse().getArray();
		assertEquals(3, array.size());
		assertEquals("foo", array.get(0).getString());
		assertEquals("bar", array.get(1).getString());
		assertEquals("baz", array.get(2).getString());
	}

	@Test
	public void testEmptyArray() throws JSONException {
		List<Value> array = createParser("[]").parse().getArray();
		assertEquals(0, array.size());
	}
	
	@Test
	public void testArrayOfEmptyArrays() throws JSONException {
		createParser("[[[],[]],[[],[]]]").parse().getArray();
	}
	
	@Test
	public void testArrayOfArrayOfFloatWithoutExponent() throws JSONException {
		createParser("[ [ 1.0 ] ]").parse().getArray();
	}

	@Test
	public void testTrue() throws JSONException {
		assertTrue(createParser(" true").parse().getBoolean());
	}

	@Test
	public void testFalse() throws JSONException {
		assertFalse(createParser(" false").parse().getBoolean());
	}

	@Test
	public void testNull() throws JSONException {
		assertTrue(createParser("null").parse().isNull());
	}

	@Test
	public void testObject() throws JSONException {
		Map<String, Value> object = createParser("{ \"foo\": \"bar\", \"baz\": true, \"bam\" : null }").parse().getObject();
		assertEquals("bar", object.get("foo").getString());
		assertTrue(object.get("baz").getBoolean());
		assertTrue(object.get("bam").isNull());
	}

	@Ignore("See Parser.readObject to see how to support this")
	@Test
	public void testObjectWithTrailingComma() throws JSONException {
		Map<String, Value> object = createParser("{ \"foo\": \"bar\", \"baz\": true, \"bam\" : null, }").parse().getObject();
		assertEquals("bar", object.get("foo").getString());
		assertTrue(object.get("baz").getBoolean());
		assertTrue(object.get("bam").isNull());
	}

	@Ignore("See Parser.readObject to see how to support this")
	@Test
	public void testObjectWithTrailingCommaAndNoWhitespace() throws JSONException {
		Map<String, Value> object = createParser("{ \"foo\": \"bar\", \"baz\": true, \"bam\" : null,}").parse().getObject();
		assertEquals("bar", object.get("foo").getString());
		assertTrue(object.get("baz").getBoolean());
		assertTrue(object.get("bam").isNull());
	}

	@Test
	public void testObjectWithEmptyLists() throws JSONException {
		Map<String, Value>  object = createParser("{ \"foo\": [], \"baz\": []}").parse().getObject();
		assertTrue(object.get("foo").getArray().isEmpty());
		assertTrue(object.get("baz").getArray().isEmpty());
	}


	@Test
	public void testEmptyObject() throws JSONException {
		createParser("{}").parse().getObject();
	}

	@Test
	public void testNegativeInteger() throws JSONException {
		assertEquals(-54, createParser("-54").parse().getNumber().intValue());
	}

	@Test
	public void testPositiveInteger() throws JSONException {
		assertEquals(54, createParser("54").parse().getNumber().intValue());
	}

	@Test
	public void testZero() throws JSONException {
		assertEquals(0, createParser("0").parse().getNumber().intValue());
	}
	
	@Test
	public void testMaximumInteger() throws JSONException {
		assertEquals(9223372036854775807L, createParser("9223372036854775807").parse().getNumber().longValue());
	}

	@Test
	public void testMinimumInteger() throws JSONException {
		assertEquals(-9223372036854775808L, createParser("-9223372036854775808").parse().getNumber().longValue());		
	}
	
	@Test
	public void testFloat() throws JSONException {
		assertEquals(0.314, createParser("0.314").parse().getNumber().floatValue(), 0.0001);
	}

	@Test
	public void testFloat2() throws JSONException {
		assertEquals(31.415, createParser("31.415").parse().getNumber().floatValue(), 0.0001);
	}
	
	@Test
	public void testFloatWithFractionalAndExponent() throws JSONException {
		assertEquals(31.415e12, createParser("31.415e12").parse().getNumber().floatValue(), 0.0001e12);
	}

	@Test
	public void testFloatWithoutFractionalAndExponent() throws JSONException {
		assertEquals(31.0e12, createParser("31e12").parse().getNumber().floatValue(), 0.1e12);
	}

	@Test
	public void testFloatWithPositiveExponent() throws JSONException {
		assertEquals(31.415e12, createParser("31.415e+12").parse().getNumber().floatValue(), 0.0001e12);
	}

	@Test
	public void testFloatWithNegativeExponent() throws JSONException {
		assertEquals(31.415e-12, createParser("31.415e-12").parse().getNumber().floatValue(), 0.0001e-12);
	}

	
	@Test
	public void testMinimumFloat() throws JSONException {
		assertEquals(Double.MIN_VALUE, createParser(Double.valueOf(Double.MIN_VALUE).toString()).parse().getNumber().doubleValue(), Double.MIN_VALUE);
	}

	@Test
	public void testMaximumFloat() throws JSONException {
		// Close enough
		assertEquals("1.7976931348623157E308", Double.valueOf(Double.MAX_VALUE).toString());
		assertEquals("1.7976931348623145E308", createParser(Double.valueOf(Double.MAX_VALUE).toString()).parse().getNumber().toString());
	}

	@Test
	public void testMinimumNormalFloat() throws JSONException {
		// Close enough
		assertEquals("2.2250738585072014E-308", Double.valueOf(Double.MIN_NORMAL).toString());
		assertEquals("2.225073858507203E-308", createParser(Double.valueOf(Double.MIN_NORMAL).toString()).parse().getNumber().toString());
	}

	@Test
	public void testParseNumberArray() throws JSONException {
		List<Value> array = createParser("[1,2,3]").parse().getArray();
		assertEquals(3, array.size());
		assertEquals(1, array.get(0).getNumber().intValue());
		assertEquals(2, array.get(1).getNumber().intValue());
		assertEquals(3, array.get(2).getNumber().intValue());
	}
}
