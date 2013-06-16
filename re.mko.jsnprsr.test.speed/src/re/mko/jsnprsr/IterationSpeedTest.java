package re.mko.jsnprsr;

import java.util.ArrayList;
import java.util.List;

import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;

public class IterationSpeedTest extends Benchmark {
	private List<String> testValue;
	
	@Override
	public void setUp() throws JSONException {
		testValue = new ArrayList<String>();
		for (int i = 0; i < 100; ++i) {
			testValue.add(Integer.toString(i));
		}
	}
	
	private void dummy(String s) {
	}
	
	public int timeForEach(int reps) {
		int result = 0;
		for (int i = 0; i < reps; ++i) {
			for (String s : testValue) {
				dummy(s);
				result++;
			}
		}
		return result;
	}

	public int timeIndexed(int reps) {
		int result = 0;
		for (int i = 0; i < reps; ++i) {
			int size = testValue.size();
			for (int j = 0; j < size; ++j) {
				dummy(testValue.get(j));
				result++;
			}
		}
		return result;
	}

	public int timeIndexedSizeInForTest(int reps) {
		int result = 0;
		for (int i = 0; i < reps; ++i) {
			for (int j = 0; j < testValue.size(); ++j) {
				dummy(testValue.get(j));
				result++;
			}
		}
		return result;
	}
	
	public int timeIndexedSizeInForInit(int reps) {
		int result = 0;
		for (int i = 0; i < reps; ++i) {
			for (int j = 0, size = testValue.size(); j < size; ++j) {
				dummy(testValue.get(j));
				result++;
			}
		}
		return result;
	}

	public static void main(String args[]) {
		CaliperMain.main(IterationSpeedTest.class, new String[]{"-i", "micro", "-r", "Iteration"});
	}
}
