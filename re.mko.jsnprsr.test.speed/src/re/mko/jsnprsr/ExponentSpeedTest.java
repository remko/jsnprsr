package re.mko.jsnprsr;

import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.google.caliper.runner.CaliperMain;

public class ExponentSpeedTest extends Benchmark {
	private @Param({"1", "1023", "-1022"}) int exponent;
	
	public long timeMathPowLong(int reps) {
		long base = 123456;
		long result = 0;
		for (int i = 0; i < reps; ++i) {
			result += base * Math.pow(10.0, exponent);
		}
		return result;
	}
	
	public double timeMathPowDouble(int reps) {
		double base = 1234.56;
		double result = 0;
		for (int i = 0; i < reps; ++i) {
			result += base * Math.pow(10.0, exponent);
		}
		return result;
	}

	
	public double timeLoop(int reps) {
		double result = 123456;
		if (exponent < 0) {
			for (int i = 0; i < reps; ++i) {
				result /= 10;
			}
		}
		else {
			for (int i = 0; i < reps; ++i) {
				result *= 10;
			}			
		}
		return result;
	}
	
	public static void main(String args[]) {
		CaliperMain.main(ExponentSpeedTest.class, new String[]{"-i", "micro", "-r", "Exponent"});
	}
}
