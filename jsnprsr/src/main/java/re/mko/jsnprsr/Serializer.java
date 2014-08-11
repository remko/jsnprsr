/*
 * Copyright (c) 2013 Remko Tronçon
 * Licensed under the MIT license.
 * See COPYING file for more information.
 */

package re.mko.jsnprsr;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map.Entry;

public final class Serializer {
	public static void serialize(Value value, Writer writer) throws JSONException {
		try {
			switch (value.getType()) {
				case ARRAY:
					writer.write('[');
					List<Value> values = value.getArray();
					int end = values.size() - 1;
					if (end >= 0) {
						for (int i = 0; i < end; ++i) {
							serialize(values.get(i), writer);
							writer.write(',');
						}
						serialize(values.get(end), writer);
					}
					writer.write(']');
					break;
				case BOOLEAN:
					writer.write(value.getBoolean() ? "true" : "false");
					break;
				case NULL:
					writer.write("null");
					break;
				case NUMBER:
					writer.write(value.getNumber().toString());
					break;
				case OBJECT:
					writer.write('{');
					boolean insertComma = false;
					for (Entry<String, Value> entry : value.getObject().entrySet()) {
						if (insertComma) {
							writer.write(',');
						}
						else {
							insertComma = true;
						}
						serializeString(entry.getKey(), writer);
						writer.write(':');
						serialize(entry.getValue(), writer);
					}
					writer.write('}');
					break;
				case STRING:
					serializeString(value.getString(), writer);
					break;
			}
		}
		catch (IOException e) {
			throw new JSONException(e);
		}
	}

	private static void serializeString(String string, Writer writer) throws IOException {
		writer.write('"');
		for (int i = 0, size = string.length(); i < size; ++i) {
			char c = string.charAt(i);
			switch (c) {
				case '\b': writer.write("\\b"); break;
				case '\f': writer.write("\\f"); break;
				case '\n': writer.write("\\n"); break;
				case '\r': writer.write("\\r"); break;
				case '\t': writer.write("\\t"); break;
				case '\\': writer.write("\\\\"); break;
				case '/': writer.write("\\/"); break;
				case '"' : writer.write("\\\""); break;
				default:
					writer.write(c);
			}
		}
		writer.write('"');
	}
}
