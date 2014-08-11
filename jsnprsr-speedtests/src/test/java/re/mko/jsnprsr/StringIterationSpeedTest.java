package re.mko.jsnprsr;

import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.google.caliper.runner.CaliperMain;

public class StringIterationSpeedTest extends Benchmark {
	@Param({"1", "10", "50", "100"}) int stringSize;
	private String testString;
	
	@Override
	protected void setUp() {
		StringBuffer w = new StringBuffer();
		for (int i = 0; i < stringSize; ++i) {
			w.append('a');
		}
		testString = w.toString();
	}

	public long timeCharAt(long reps) {
		long u = 0;
		for (int i = 0; i < reps; ++i) {
			u += iterateCharAt();
		}
		return u;
	}
	
	private long iterateCharAt() {
		long u = 0;
		for (int x = 0, size = testString.length(); x < size; ++x) {
			u += testString.charAt(x);
		}
		return u;
	}

	public long timeGetChars(long reps) {
		long u = 0;
		for (int i = 0; i < reps; ++i) {
			u += iterateGetChars();
		}
		return u;
	}
		
	private long iterateGetChars() {
		long u = 0;
		int length = testString.length();	
		char[] chars = new char[length];
		testString.getChars(0, length, chars, 0);
		for(int i = 0; i < length; ++i) {
			u += chars[i];
		}
		return u;
	}

	public static void main(String args[]) {
		CaliperMain.main(StringIterationSpeedTest.class, new String[]{"-i", "micro", "-r", "StringIteration"});
	}
}
