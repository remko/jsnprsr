/*
 * Copyright (c) 2013 Remko Tronçon
 * Licensed under the MIT license.
 * See COPYING file for more information.
 */

package re.mko.jsnprsr;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Parser {
	private static final int[] CHAR2HEX = new int[] {
		255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
		255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
		255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 255, 255, 255, 255, 255, 255,
		255, 10, 11, 12, 13, 14, 15, 255, 255, 255, 255, 255, 255, 255, 255, 255,
		255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
		255, 10, 11, 12, 13, 14, 15, 255, 255, 255, 255, 255, 255, 255, 255, 255,
		255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
		255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
		255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
		255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
		255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
		255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
		255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
		255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
		255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	};
	private final Reader reader;
	private final StringBuilder stringBuilder = new StringBuilder(); // NOPMD - We actually want this to be a field
	private int currentCharacter;
	//private int lineNumber = 0;

	public Parser(Reader reader) {
		this.reader = reader;
	}

	public Value parse() throws JSONException {
		try {
			munchWhitespace();
			return readValue();
		}
		catch (IOException e) {
			throw new JSONException(e);
		}
	}

	private Value readValue() throws JSONException, IOException {
		switch (currentCharacter) { // NOPMD - This switch does not have a break
			case '{': return Value.object(readObject());
			case '[': return Value.array(readArray());
			case '\"': return Value.string(readString());
			case 't': {
				munchCharacter();
				int c2 = currentCharacter;
				munchCharacter();
				int c3 = currentCharacter;
				munchCharacter();
				int c4 = currentCharacter;
				if (c2 != 'r' || c3 != 'u' || c4 != 'e') {
					throw new JSONException("Illegal value");
				}
				munchWhitespace();
				return Value.TRUE;
			}
			case 'f': {
				munchCharacter();
				int c2 = currentCharacter;
				munchCharacter();
				int c3 = currentCharacter;
				munchCharacter();
				int c4 = currentCharacter;
				munchCharacter();
				int c5 = currentCharacter;
				if (c2 != 'a' || c3 != 'l' || c4 != 's' || c5 != 'e') {
					throw new JSONException("Illegal value");
				}
				munchWhitespace();
				return Value.FALSE;
			}
			case 'n':
				munchCharacter();
				int c2 = currentCharacter;
				munchCharacter();
				int c3 = currentCharacter;
				munchCharacter();
				int c4 = currentCharacter;
				if (c2 != 'u' || c3 != 'l' || c4 != 'l') {
					throw new JSONException("Illegal value");
				}
				munchWhitespace();
				return Value.NULL;
			case '-':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				return Value.number(readNumber());
			case -1:
				throw new JSONException("Unexpected end of stream");
			default:
				throw new JSONException("Illegal start character: '" + currentCharacter + "'");
		}
	}

	private Map<String, Value> readObject() throws JSONException, IOException {
		if (currentCharacter != '{') {
			throw new JSONException("Expected '{'");
		}
		munchWhitespace();
		Map<String, Value> result = new HashMap<String, Value>();
		// Move this `if` into the while loop below to support trailing commas in an array
		if (currentCharacter == '}') {
			munchWhitespace();
			return result;
		}
		while (true) {
			String key = readString(); // NOPMD - False positive
			if (currentCharacter != ':') {
				throw new JSONException("Expected :");
			}
			munchWhitespace();
			Value value = readValue();
			result.put(key, value);
			switch (currentCharacter) {
				case '}': munchWhitespace(); return result;
				case ',': munchWhitespace(); continue;
				default:
					throw new JSONException("Expected } or ,");
			}
		}
	}

	private List<Value> readArray() throws IOException, JSONException {
		if (currentCharacter != '[') {
			throw new JSONException("Expected '['");
		}
		munchWhitespace();
		List<Value> result = new ArrayList<Value>();
		// Move this `if` into the while loop below to support trailing commas in an array
		if (currentCharacter == ']') {
			munchWhitespace();
			return result;
		}
		while (true) {
			result.add(readValue());
			switch (currentCharacter) {
				case ']': munchWhitespace(); return result;
				case ',': munchWhitespace(); continue;
				default:
					throw new JSONException("Expected ] or ,");
			}
		}
	}

	private Number readNumber() throws IOException {
		long integral = 0;
		long fractional = 0;
		long dividor = 1;
		int sign = 1;
		while (true) {
			switch (currentCharacter) {
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					integral = integral * 10 + (currentCharacter - '0');
					munchCharacter();
					break;
				case '-':
					sign = -1;
					munchCharacter();
					break;
				case '.': {
					// Fraction
					munchCharacter();
					while (true) {
						switch (currentCharacter) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								fractional = fractional * 10 + (currentCharacter - '0');
								dividor *= 10;
								munchCharacter();
								break;
							case 'e':
							case 'E':
								munchCharacter();
								return readExponent(integral + (fractional / (double) dividor));
							case ' ':
							case '\n':
							case '\t':
								munchWhitespace();
								// $FALL-THROUGH$
							default:
								return Double.valueOf(integral + (fractional / (double) dividor));
						}
					}
				}
				case 'e':
				case 'E':
					munchCharacter();
					return readExponent((double) integral);	
				case ' ':
				case '\n':
				case '\t':
					munchWhitespace();
					// $FALL-THROUGH$
				default:
					return Long.valueOf(sign*integral);
			}
		}
	}
	
	private Number readExponent(double value) throws IOException { // NOPMD - We reassign value
		int exponent = 0;
		boolean positive = true; 
		while (true) {
			switch (currentCharacter) {
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					exponent = (exponent*10) + (currentCharacter - '0');
					munchCharacter();
					break;
				case '+':
					munchCharacter();
					break;
				case '-':
					positive = false;
					munchCharacter();
					break;
				case ' ':
				case '\n':
				case '\t':
					munchWhitespace();
					// $FALL-THROUGH$
				default:
					if (positive) {
						for (int i = 0; i < exponent; ++i) {
							value *= 10;
						}
					}
					else {
						for (int i = 0; i < exponent; ++i) {
							value /= 10;
						}						
					}
					return Double.valueOf(value);
			}
		}
	}

	private String readString() throws IOException, JSONException {
		if (currentCharacter != '\"') {
			throw new JSONException("Expected '\"'");
		}
		munchCharacter();
		StringBuilder builder = stringBuilder;
		builder.setLength(0);
		while (true) {
			switch (currentCharacter) {
				case '\\':
					munchCharacter();
					int escapedCharacter = currentCharacter;
					switch (escapedCharacter) {
						case 'u':
							munchCharacter();
							int u1 = CHAR2HEX[currentCharacter];
							munchCharacter();
							int u2 = CHAR2HEX[currentCharacter];
							munchCharacter();
							int u3 = CHAR2HEX[currentCharacter];
							munchCharacter();
							int u4 = CHAR2HEX[currentCharacter];
							int u = u1<<12 | u2<<8 | u3<<4 | u4;
							builder.append((char) u);
							break;
						case 'b': builder.append('\b'); break;
						case 'f': builder.append('\f'); break;
						case 'n': builder.append('\n'); break;
						case 'r': builder.append('\r'); break;
						case 't': builder.append('\t'); break;
						case -1: throw new JSONException("Premature end of string");
						default:
							builder.append((char) escapedCharacter);
					}
					munchCharacter();
					break;
				case '"':
					munchWhitespace();
					return builder.toString();
				case -1:
					throw new JSONException("Premature end of string");
				default:
					builder.append((char) currentCharacter);
					munchCharacter();
			}
		}
	}

	private void munchWhitespace() throws IOException {
		while (true) {
			int c = reader.read();
			switch (c) {
				case '\n': //lineNumber++;
				case ' ':
				case '\t':
					continue;
				default:
					currentCharacter = c;
					return;
			}
		}
	}

	private void munchCharacter() throws IOException {
		currentCharacter = reader.read();
	}

}
