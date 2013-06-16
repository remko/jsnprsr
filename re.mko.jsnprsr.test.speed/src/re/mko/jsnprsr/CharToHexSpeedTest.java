package re.mko.jsnprsr;

import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;

public class CharToHexSpeedTest extends Benchmark {
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
	private int char0;
	private int char1;
	private int char2;
	private int char3;
	
	@Override
	protected void setUp() {
		char0 = '6';
		char1 = '8';
		char2 = 'c';
		char3 = 'F';
	}

	public long timeArray(long reps) {
		long u = 0;
		for (int i = 0; i < reps; ++i) {
			int u1 = CHAR2HEX[char0];
			int u2 = CHAR2HEX[char1];
			int u3 = CHAR2HEX[char2];
			int u4 = CHAR2HEX[char3];
			u += (u1<<12 | u2<<8 | u3<<4 | u4);
		}
		return u;
	}
	
	public long timeNaive(long reps) throws Exception {
		long u = 0;
		for (int i = 0; i < reps; ++i) {
			int u1 = naiveChar2Hex(char0);
			int u2 = naiveChar2Hex(char1);
			int u3 = naiveChar2Hex(char2);
			int u4 = naiveChar2Hex(char3);
			u += (u1<<12 | u2<<8 | u3<<4 | u4);
		}
		return u;
	}
	
	public long timeUnchecked(long reps) throws Exception {
		long u = 0;
		for (int i = 0; i < reps; ++i) {
			int u1 = uncheckedChar2Hex(char0);
			int u2 = uncheckedChar2Hex(char1);
			int u3 = uncheckedChar2Hex(char2);
			int u4 = uncheckedChar2Hex(char3);
			u += (u1<<12 | u2<<8 | u3<<4 | u4);
		}
		return u;
	}
	
	public long timeUnchecked2(long reps) throws Exception {
		long u = 0;
		for (int i = 0; i < reps; ++i) {
			int u1 = uncheckedChar2Hex2(char0);
			int u2 = uncheckedChar2Hex2(char1);
			int u3 = uncheckedChar2Hex2(char2);
			int u4 = uncheckedChar2Hex2(char3);
			u += (u1<<12 | u2<<8 | u3<<4 | u4);
		}
		return u;
	}


	
	private int naiveChar2Hex(int c) {
		if (c >= '0' && c <= '9') {
			return c - '0';
		}
		else if (c >= 'a' && c <= 'z') {
			return c - 'a' + 10;
		}
		else if (c >= 'A' && c <= 'Z') {
			return c - 'A' + 10;
		}
		else {
			throw new RuntimeException();
		}
	}
	
	private int uncheckedChar2Hex(int c) {
		if (c >= 'a') {
			return c - 'a' + 10;
		}
		else if (c >= 'A') {
			return c - 'A' + 10;
		}
		return c - '0';
	}
	
	private int uncheckedChar2Hex2(int c) {
		if (c <= '9') {
			return c - '9';
		}
		else if (c <= 'A') {
			return c - 'A' + 10;
		}
		return c - 'a' + 10;
	}

	
	/*
	public long timeParse(int reps) {
		long u = 0;
		char[] chars = new char[4];
		for (int i = 0; i < reps; ++i) {
			chars[0] = (char) char0;
			chars[1] = (char) char1;
			chars[2] = (char) char2;
			chars[3] = (char) char3;
			u += Integer.parseInt(String.valueOf(chars), 16);
		}
		return u;
	}*/
	
	public static void main(String args[]) {
		CaliperMain.main(CharToHexSpeedTest.class, new String[]{"-i", "micro", "-r", "CharToHex"});
	}
}
