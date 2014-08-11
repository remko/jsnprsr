package re.mko.jsnprsr;

import java.io.Reader;


public interface ParserializerInterface {
	Object parse(String s) throws Exception;
	Object parse(Reader r) throws Exception;
	String serialize(Object s) throws Exception;
}
