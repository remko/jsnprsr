package re.mko.jsnprsr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;

public class TraverseSpeedTest extends Benchmark {
	private Value testValue;
	
	@Override
	public void setUp() throws JSONException {
		testValue = JSON.parse("{ \"a1\": { \"a2\": { \"a3\": \"\", \"b3\": \"\", \"c3\": \"\" }, \"b2\": { \"a3\": \"\", \"b3\": \"\", \"c3\": \"\" }, \"c2\": { \"a3\": \"\", \"b3\": \"\", \"c3\": \"\" } }, \"b1\": { \"a2\": { \"a3\": \"\", \"b3\": \"\", \"c3\": \"\" }, \"b2\": { \"a3\": \"\", \"b3\": \"\", \"c3\": \"\" }, \"c2\": { \"a3\": \"\", \"b3\": \"\", \"c3\": \"\" } }, \"c1\": { \"a2\": { \"a3\": \"\", \"b3\": \"\", \"c3\": \"\" }, \"b2\": { \"a3\": \"\", \"b3\": \"\", \"c3\": \"\" }, \"c2\": { \"a3\": \"\", \"b3\": \"\", \"c3\": \"\" } } }");
	}
	
	public int timeRecursive(int reps) {
		int result = 0;
		for (int i = 0; i < reps; ++i) {
			traverseRecursive(testValue);
		}
		return result;
	}
	
	private void traverseRecursive(Value value) {
		switch (value.getType()) {
			case ARRAY: {
				List<Value> values = value.getArray();
				for (Value v : values) {
					traverseRecursive(v);
				}
				break;
			}
			case OBJECT:
				Set<Entry<String, Value>> values = value.getObject().entrySet();
				for (Entry<String, Value> v : values) {
					traverseRecursive(v.getValue());
				}
				break;
			default:
				break;
		}
	}

	public int timeIterativeArrayList(int reps) {
		int result = 0;
		for (int i = 0; i < reps; ++i) {
			List<Value> queue = new ArrayList<Value>();
			queue.add(testValue);
			while (!queue.isEmpty()) {
				Value value = queue.remove(queue.size() - 1);
				switch (value.getType()) {
					case ARRAY: {
						List<Value> values = value.getArray();
						for (int j = values.size() - 1; i >= 0; --i) {
							queue.add(values.get(j));
						}
						break;
					}
					case OBJECT:
						Set<Entry<String, Value>> values = value.getObject().entrySet();
						for (Entry<String, Value> v : values) {
							queue.add(v.getValue());
						}
						break;
					default:
						break;
				}
			}
		}
		return result;
	}

	public int timeIterativeArrayListIncorrectOrder(int reps) {
		int result = 0;
		for (int i = 0; i < reps; ++i) {
			List<Value> queue = new ArrayList<Value>();
			queue.add(testValue);
			while (!queue.isEmpty()) {
				Value value = queue.remove(queue.size() - 1);
				switch (value.getType()) {
					case ARRAY: {
						queue.addAll(value.getArray());
						break;
					}
					case OBJECT:
						Set<Entry<String, Value>> values = value.getObject().entrySet();
						for (Entry<String, Value> v : values) {
							queue.add(v.getValue());
						}
						break;
					default:
						break;
				}
			}
		}
		return result;
	}

	public int timeIterativeLinkedList(int reps) {
		int result = 0;
		for (int i = 0; i < reps; ++i) {
			List<Value> queue = new LinkedList<Value>();
			queue.add(testValue);
			while (!queue.isEmpty()) {
				Value value = queue.remove(0);
				switch (value.getType()) {
					case ARRAY: {
						queue.addAll(0, value.getArray());
						break;
					}
					case OBJECT:
						Set<Entry<String, Value>> values = value.getObject().entrySet();
						for (Entry<String, Value> v : values) {
							queue.add(0, v.getValue());
						}
						break;
					default:
						break;
				}
			}
		}
		return result;
	}

	public static void main(String args[]) {
		CaliperMain.main(TraverseSpeedTest.class, new String[]{"-i", "micro", "-r", "Traverse"});
	}
}
