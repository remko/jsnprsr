/*
 * Copyright (c) 2013 Remko Tronçon
 * Licensed under the MIT license.
 * See COPYING file for more information.
 */

package re.mko.jsnprsr;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * This class provides methods to parse and serialize JSON values.
 */
public final class JSON {
	private final static Charset UTF32BE = Charset.forName("UTF-32BE");
	private final static Charset UTF16BE = Charset.forName("UTF-16BE");
	private final static Charset UTF32LE = Charset.forName("UTF-32LE");
	private final static Charset UTF16LE = Charset.forName("UTF-16LE");
	private final static Charset UTF8 = Charset.forName("UTF-8");
	
	private JSON() {
	}

	/**
	 * Parses a JSON string.
	 * @param s the string to parse
	 * @return the parsed JSON Value.
	 * @throws JSONException
	 */
	public static Value parse(String s) throws JSONException {
		return parse(new StringReader(s));
	}

	/**
	 * Parses JSON data.
	 * @param reader the reader to read the data from
	 * @return the parsed JSON Value.
	 * @throws JSONException
	 */
	public static Value parse(Reader reader) throws JSONException {
		try {
			return new Parser(reader).parse();
		}
		finally {
			try {
				reader.close();
			}
			catch (IOException e) {
			}
		}
	}

	/**
	 * Serializes a JSON value to a string.
	 * @param value the value to serialize
	 * @return the serialized value
	 * @throws JSONException
	 */
	public static String serialize(Value value) throws JSONException {
		StringWriter writer = new StringWriter();
		Serializer.serialize(value, writer);
		return writer.toString();
	}
	
	/**
	 * Serializes a JSON value to a writer.
	 * @param value the value to serialize
	 * @param writer the writer to serialize to
	 * @throws JSONException
	 */
	public static void serialize(Value value, Writer writer) throws JSONException {
		Serializer.serialize(value, writer);
	}	

	/**
	 * Parses JSON data from a file.
	 * @param file the file to read the data from
	 * @param charset the character set of the input file
	 * @return the parsed JSON Value.
	 * @throws JSONException
	 */
	public static Value parse(File file, Charset charset) throws JSONException {
		try {
			return parse(new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), charset));
		} catch (FileNotFoundException e) {
			throw new JSONException(e);
		}
	}
	
	/**
	 * Parses JSON data from a file.
	 * 
	 * The character set of the file is auto-detected.
	 * The file will be opened 2 times (once for detecting the character set, and once
	 * for the actual parsing)
	 * 
	 * @param file the file to read the data from
	 * @return the parsed JSON Value.
	 * @throws JSONException
	 */
	public static Value parse(File file) throws JSONException {
		InputStream input = null;
		try {
			byte[] header = new byte[4];
			
			// Guess charset
			input = new FileInputStream(file);
			input.read(header);			
			Charset charset;
			if (header[0] == 0) {
				charset = (header[1] == 0 ? UTF32BE : UTF16BE);
			}
			else if (header[1] == 0) {
				charset = (header[2] == 0 ? UTF32LE : UTF16LE);
			}
			else {
				charset = UTF8;
			}
			input.close();
			input = null;
			
			// Parse the whole file
			return parse(file, charset);
		}
		catch (IOException e) {
			throw new JSONException(e);
		}
		finally {
			if (input != null) {
				try {
					input.close();
				}
				catch (IOException e) {
				}
			}
		}
	}
}
