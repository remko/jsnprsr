package re.mko.jsnprsr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.google.caliper.api.Macrobenchmark;
import com.google.caliper.runner.CaliperMain;

public class LargeFileSpeedTest extends Benchmark {
	@Param({/* Runs out of memory "minimal-json",*/ "jsnprsr"}) public String library;
	private File inputFile;
	private ParserializerInterface parserializer;

	@Override
	public void setUp() throws JSONException, FileNotFoundException {
		parserializer = ParserializerFactory.create(library);
		inputFile = new File(LargeFileSpeedTest.class.getResource(".").getFile() + "/../../../../data/citylots.json");
	}
	
	@Macrobenchmark
	public void loadFile() throws Exception {
		FileReader reader = new FileReader(inputFile);
		try {
			parserializer.parse(reader);
		}
		finally {
			reader.close();
		}
	}
	
	public static void main(String args[]) {
		CaliperMain.main(LargeFileSpeedTest.class, new String[]{
			"-i", "macro", 
			"-r", "LargeFile",
			"--time-limit", "0"
		});
	}
}
