package re.mko.jsnprsr;

import java.io.File;
import java.util.Scanner;

import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.google.caliper.api.Macrobenchmark;
import com.google.caliper.runner.CaliperMain;

public class TwitterSpeedTest extends Benchmark {
	@Param({"jsnprsr", "minimal-json", "org.json"}) private String library;
	private String testString;
	private ParserializerInterface parserializer;
	
	@Override
	protected void setUp() throws Exception {
		File inputFile = new File(TwitterSpeedTest.class.getResource(".").getFile() + "/../../../../data/twitter.json");
		testString = new Scanner(inputFile).useDelimiter("\\Z").next();
		parserializer = ParserializerFactory.create(library);
	}
	
	@Macrobenchmark
	public void timeParse() throws Exception {
		parserializer.parse(testString);
	}
	
	@Macrobenchmark
	public void timeSerialize() throws Exception {
		Object testObject = parserializer.parse(testString);
		parserializer.serialize(testObject);
	}
	
	public static void main(String args[]) {
		CaliperMain.main(TwitterSpeedTest.class, new String[]{"-i", "macro", "-r", "Twitter"});
	}
}
