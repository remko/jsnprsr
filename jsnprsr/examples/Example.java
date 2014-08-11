/*
 * Copyright (c) 2013 Remko Tronçon
 * Licensed under the MIT license.
 * See COPYING file for more information.
 */

import re.mko.jsnprsr.JSON;
import re.mko.jsnprsr.JSONException;
import re.mko.jsnprsr.Value;


public class Example {
	public static void main(String[] args) throws JSONException {
		Value value = JSON.parse("{\"foo\": \"bar\", \"baz\": [4, 5]}");
		System.out.println(value.getType());
		System.out.println(value.getObject().get("foo").getString());
		System.out.println(value.getObject().get("baz").getArray().get(0).getNumber());

		value.getObject().put("foo", Value.array(Value.string("a"), Value.string("b")));
		
		System.out.println(JSON.serialize(value));
	}
}
