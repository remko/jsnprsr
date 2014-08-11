package re.mko.jsnprsr;

import java.io.Reader;

public class JsnPrsrInterface implements ParserializerInterface {

	@Override
	public Object parse(String s) throws JSONException {
		return JSON.parse(s);
	}

	@Override
	public Object parse(Reader reader) throws JSONException {
		return JSON.parse(reader);
	}

	@Override
	public String serialize(Object value) throws JSONException {
		return JSON.serialize((Value) value);
	}

}
