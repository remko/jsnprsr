package re.mko.jsnprsr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.google.caliper.runner.CaliperMain;

public class ParserializerSpeedTest extends Benchmark {
	@Param({"jsnprsr", "minimal-json", "org.json"}) private String library;
	enum Variant {
		LargeIntegers,
		SmallIntegers,
		Floats,
		Strings,
		Arrays,
		Objects,
		Booleans,
		Nulls,
		UnicodeEscapes,
		Whitespace,
	}
	@Param private Variant variant;
	private String testString;
	private ParserializerInterface parserializer;
	
	@Override
	protected void setUp() throws Exception {
		switch (variant) {
			case LargeIntegers: {
				List<Value> values = new ArrayList<Value>(); 
				for (int i = 0; i < 100; ++i) {
					values.add(Value.number(12345678901234567l));
				}
				testString = JSON.serialize(Value.array(values));
				break;
			}
			case SmallIntegers: {
				List<Value> values = new ArrayList<Value>(); 
				for (int i = 0; i < 100; ++i) {
					values.add(Value.number(1));
				}
				testString = JSON.serialize(Value.array(values));
			}
			case Floats: {
				List<Value> values = new ArrayList<Value>(); 
				for (int i = 0; i < 100; ++i) {
					values.add(Value.number(3.1415679e12));
				}
				testString = JSON.serialize(Value.array(values));
				break;
			}
			case Strings: {
				List<Value> values = new ArrayList<Value>(); 
				for (int i = 0; i < 100; ++i) {
					values.add(Value.string("Blablabla" + Integer.toString(i)));
				}
				testString = JSON.serialize(Value.array(values));
				break;
			}
			case Arrays: {
				List<Value> values = new ArrayList<Value>(); 
				List<Value> values2 = new ArrayList<Value>(); 
				for (int i = 0; i < 10; ++i) {
					values2.add(Value.array());
				}
				for (int i = 0; i < 10; ++i) {
					values.add(Value.array(values2));
				}
				testString = JSON.serialize(Value.array(values));
				break;
			}
			case Objects: {
				Map<String,Value> values = new HashMap<String, Value>();
				for (int i = 0; i < 100; ++i) {
					values.put("key" + Integer.toString(i), Value.number(i));
				}
				testString = JSON.serialize(Value.array(Value.object(values)));
				break;
			}
			case Booleans: {
				List<Value> values = new ArrayList<Value>(); 
				for (int i = 0; i < 100; ++i) {
					values.add(Value.TRUE);
					values.add(Value.FALSE);
				}
				testString = JSON.serialize(Value.array(values));
				break;
			}
			case Nulls: {
				List<Value> values = new ArrayList<Value>(); 
				for (int i = 0; i < 100; ++i) {
					values.add(Value.NULL);
				}
				testString = JSON.serialize(Value.array(values));
				break;
			}
			case UnicodeEscapes : {
				List<Value> values = new ArrayList<Value>(); 
				for (int i = 0; i < 10; ++i) {
					values.add(Value.string("\"\\u2603\\u2604\\u2605\\u2606\\u2607\\u2608\\u2603\\u2604\\u2605\\u2606\\u2607\\u2608\\u2603\\u2604\\u2605\\u2606\\u2607\\u2608\\u2603\\u2604\\u2605\\u2606\\u2607\\u2608\""));
				}
				testString = JSON.serialize(Value.array(values));
				break;
			}
			case Whitespace : {
				StringBuilder b = new StringBuilder();
				for (int i = 0; i < 1000; ++i) {
					b.append(' ');
				}
				b.append("[]");
				testString = b.toString(); 
				break;
			}
		}
		
		parserializer = ParserializerFactory.create(library);
		
		// Run a quick sanity check
		String result = parserializer.serialize(parserializer.parse(testString));
		if (normalize(result).length() != normalize(testString).length()) {
			System.out.println(normalize(testString));
			System.out.println(normalize(result));
			throw new RuntimeException("Parser/Serializer is yielding unexpected results");
		}
	}
	
	public void timeParse(int reps) throws Exception {
		for (int i = 0; i < reps; ++i) {
			parserializer.parse(testString);
		}
	}
	
	private String normalize(String string) {
		return string.replaceAll("\\s", "");
	}

	public void timeSerialize(int reps) throws Exception {
		Object testObject = parserializer.parse(testString);
		for (int i = 0; i < reps; ++i) {
			parserializer.serialize(testObject);
		}		
	}
	
	public static void main(String args[]) {
		CaliperMain.main(ParserializerSpeedTest.class, new String[]{"-i", "micro", "-r", "Parserializer", "--time-limit", "0"});
	}
}
